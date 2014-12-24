package com.potevio.sdtv.device.hiyo;

import java.nio.ByteBuffer;
import java.util.Arrays;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.potevio.sdtv.util.SocketUtil;

public class HiyoCodecFactory implements ProtocolCodecFactory {
	private static final Logger logger = LoggerFactory
			.getLogger(HiyoCodecFactory.class);

	private Decoder decoder;
	private Encoder encoder;

	public HiyoCodecFactory() {
		this.decoder = new Decoder();
		this.encoder = new Encoder();
	}

	@Override
	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		return this.encoder;
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
			HiyoMSG hiyoMSG = (HiyoMSG) session.getAttribute(DECODER_STATE_KEY);

			if (hiyoMSG == null) {
				int remaing = in.remaining();
				if (remaing < 14) {
					return false;
				} else {
					hiyoMSG = readHeadAndNeck(in);
					logger.debug("PARSE NECK:" + hiyoMSG.toString());
					short leftLen = (short) (hiyoMSG.getLen() - 6);
					session.setAttribute(DECODER_STATE_KEY, hiyoMSG);
					remaing = in.remaining();
					if (remaing < leftLen) {
						// return false, and parse it next time when hiyoMSG !=
						// null
						return false;
					} else {
						readBodyAndTail(in, hiyoMSG);
						out.write(hiyoMSG);
						session.setAttribute(DECODER_STATE_KEY, null);
						return true;
					}

				}
			} else {
				short len = hiyoMSG.getLen();
				int remaing = in.remaining();
				if (remaing < len-6) {
					return false;
				} else {
					readBodyAndTail(in, hiyoMSG);
					out.write(hiyoMSG);
					session.setAttribute(DECODER_STATE_KEY, null);
					return true;
				}
			}

		}

		private void readBodyAndTail(IoBuffer in, HiyoMSG hiyoMSG) {
			short len = hiyoMSG.getLen();
			// NO CRC
			byte[] body = new byte[len - 6];
			in.get(body);
			String bodyMsg = new String(body);
			hiyoMSG.setMsg(bodyMsg);

		}

		private HiyoMSG readHeadAndNeck(IoBuffer in) {

			HiyoMSG msg = null;
			try {
				msg = new HiyoMSG();
				
				byte[] ver=new byte[2];
				in.get(ver);
				msg.setVer(ver);
				
				byte[] flag=new byte[2];
				in.get(flag);
				msg.setFlag(flag);
				
				byte[] resv=new byte[2];
				in.get(resv);
				msg.setResv(resv);
				
				byte[] lenbyte=new byte[2];
				in.get(lenbyte);
				short len = (short) SocketUtil.byteArrayToInt(lenbyte, 0, 2);
				msg.setLen(len);
				
				byte[] cmd=new byte[2];
				in.get(cmd);
				msg.setCmd(cmd);
				
				byte[] cflag=new byte[2];
				in.get(cflag);
				msg.setCfalg(cflag);
				
				byte[] ret=new byte[2];
				in.get(ret);
				msg.setRet(ret);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return msg;
		}

	}

	private class Encoder extends ProtocolEncoderAdapter {

		@Override
		public void encode(IoSession session, Object message,
				ProtocolEncoderOutput out) throws Exception {

			if (!(message instanceof HiyoMSG)) {
				return;
			}
			HiyoMSG msg = (HiyoMSG) message;
			IoBuffer buf = IoBuffer.allocate(1024).setAutoExpand(true);
			// ver + flag+resv 6byte
			buf.put(msg.getVer());
			buf.put(msg.getFlag());
			buf.put(msg.getResv());

			String msg_body = msg.getMsg();
			byte[] msgBytes = msg_body.getBytes();

			//
			// LEN 2byte
			Short len = (short) ((2 + 2 + 2) + msgBytes.length);
			ByteBuffer buffer = ByteBuffer.allocate(2);
			buffer.putShort(len);
			buf.put(buffer.array());

			// CMD+cflag+reserved 6byte
			buf.put(msg.getCmd());
			buf.put(msg.getCfalg());
			buf.put(msg.getReserved());
			// BODY
			buf.put(msgBytes);

			// TAIL CRC

			buf.flip();
			out.write(buf);

		}

	}
}
