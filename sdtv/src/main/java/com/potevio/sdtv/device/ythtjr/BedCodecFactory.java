package com.potevio.sdtv.device.ythtjr;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.potevio.sdtv.domain.BedData;
import com.potevio.sdtv.util.SocketUtil;

public class BedCodecFactory implements ProtocolCodecFactory {
	private Decoder decoder;
	private Encoder encoder;

	public BedCodecFactory() {
		this.decoder = new Decoder();
		this.encoder = new Encoder();
	}

	@Override
	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		return null;
	}

	@Override
	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
		return this.decoder;
	}

	private static class Decoder extends CumulativeProtocolDecoder {
		private static final String DECODER_STATE_KEY = Decoder.class.getName()
				+ ".STATE";

		@Override
		protected boolean doDecode(IoSession session, IoBuffer in,
				ProtocolDecoderOutput out) throws Exception {
			int remaing = in.remaining();
			if (remaing < 16) {
				return false;
			} else {
				byte[] head = new byte[16];
				in.get(head);
				String str = SocketUtil.bytesToHexString(head);
				String headStr = str.substring(0, 2);
				if (!"06".equals(headStr)) {
					return false;
				}
				String tailStr = str.substring(30);
				if (!"04".equals(tailStr)) {
					return false;
				}

				String b1 = str.substring(2, 4);
				String b2 = str.substring(4, 6);
				String b3 = str.substring(6, 8);
				String b4 = str.substring(8, 10);
				String b5 = str.substring(10, 12);
				String b6 = str.substring(12, 14);
				String bedID = "";

				bedID += (char) Integer.parseInt(b1, 16);
				bedID += (char) Integer.parseInt(b2, 16);
				bedID += (char) Integer.parseInt(b3, 16);
				bedID += (char) Integer.parseInt(b4, 16);
				bedID += (char) Integer.parseInt(b5, 16);
				bedID += (char) Integer.parseInt(b6, 16);

//				BedMSG msg = new BedMSG();
				BedData msg=new BedData();
//				msg.setDeviceid(bedID);
				msg.setSeriesId(bedID);

				String status = str.substring(14, 16);
				msg.setStatus(status);

				String hb1 = str.substring(16, 18);
				String hb2 = str.substring(18, 20);
				String hb3 = str.substring(20, 22);

				String heartBeat = "";
				heartBeat += (char) Integer.parseInt(hb1, 16);
				heartBeat += (char) Integer.parseInt(hb2, 16);
				heartBeat += (char) Integer.parseInt(hb3, 16);

				msg.setHeartrating(heartBeat);

				String rasping1 = str.substring(22, 24);

				String rasping2 = str.substring(24, 26);

				String rasping = "";
				rasping += (char) Integer.parseInt(rasping1, 16);
				rasping += (char) Integer.parseInt(rasping2, 16);
				msg.setResping(rasping);
				out.write(msg);
				return true;
			}
		}
	}

	private class Encoder extends ProtocolEncoderAdapter {

		@Override
		public void encode(IoSession arg0, Object arg1,
				ProtocolEncoderOutput arg2) throws Exception {
			// if (!(arg1 instanceof PTGWMsg)) {
			// return;
			// }
			// PTGWMsg msg = (PTGWMsg) arg1;
			IoBuffer buf = IoBuffer.allocate(1024).setAutoExpand(true);
			buf.put((byte) 0);
			buf.flip();
			arg2.write(buf);
		}

	}
}
