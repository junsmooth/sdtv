package com.potevio.sdtv.device.syshelp.S8;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SyshelpClientHandler extends IoHandlerAdapter {
	private static final Logger logger = LoggerFactory
			.getLogger(SyshelpClientHandler.class);

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		logger.info("MSG received:" + session);
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		logger.info("MSG SENT:" + message);
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		logger.info("SESSION CREATED:" + session);
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		logger.info("SESSION OPENED:" + session);
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
		logger.info("SESSION IDLE:" + session);
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		logger.info("SESSION CLOSED:" + session);
	}
}
