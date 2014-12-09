package com.potevio.sdtv.service;

import java.util.concurrent.LinkedBlockingQueue;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class BedMsgHandler extends IoHandlerAdapter {

	private static LinkedBlockingQueue queue = new LinkedBlockingQueue();

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		BedMSG msg = (BedMSG) message;
		System.out.println(msg);
		queue.put(msg);
	}

	public static LinkedBlockingQueue getQueue() {
		return queue;
	}

}
