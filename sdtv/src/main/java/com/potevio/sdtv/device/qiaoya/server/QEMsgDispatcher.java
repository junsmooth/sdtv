package com.potevio.sdtv.device.qiaoya.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.potevio.sdtv.device.qiaoya.server.processor.QEMsgProcessor;
import com.potevio.sdtv.device.qiaoya.server.processor.T1;

public class QEMsgDispatcher {
	private static final Logger logger = LoggerFactory.getLogger(QEMsgDispatcher.class);

	public static void dispatch(QEClientMsg msg) {
		String msgCode = msg.getMsgCode();
		try {
			Class<QEMsgProcessor> cls = (Class<QEMsgProcessor>) Class
					.forName("com.potevio.sdtv.device.qiaoya.server.processor."
							+ msgCode);
			QEMsgProcessor processor = cls.newInstance();
			processor.process(msg);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

	}
}
