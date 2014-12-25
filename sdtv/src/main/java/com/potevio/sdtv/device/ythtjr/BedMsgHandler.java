package com.potevio.sdtv.device.ythtjr;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.potevio.sdtv.util.CacheUtil;
import com.potevio.sdtv.web.BedController;

public class BedMsgHandler extends IoHandlerAdapter {
	private static Logger logger = LoggerFactory.getLogger(BedMsgHandler.class);

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		BedMSG msg = (BedMSG) message;
		logger.info("BED RAW:" + msg);
		if (!msg.getDeviceid().equals("Z20023")) {
			CacheUtil.setBedlatest(msg);
		}
//		else{
//			CacheUtil.setBedlatest(msg);
//		}
		CacheUtil.getYthtjrbedQueue().put(msg);
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		super.exceptionCaught(session, cause);
	}
}
