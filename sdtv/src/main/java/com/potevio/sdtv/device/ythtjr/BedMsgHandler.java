package com.potevio.sdtv.device.ythtjr;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.potevio.sdtv.util.CacheUtil;

public class BedMsgHandler extends IoHandlerAdapter {

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		BedMSG msg = (BedMSG) message;
		System.out.println(msg);
		CacheUtil.getYthtjrbedQueue().put(msg);
	}

}
