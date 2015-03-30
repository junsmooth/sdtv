package com.potevio.sdtv.device.qiaoya.server.processor;

abstract public class AbstractMsgProcessor implements QEMsgProcessor {
	private static QEMsgProcessor instance;

	@Override
	public QEMsgProcessor getInstance() {
		if (instance == null) {
			try {
				instance = this.getClass().newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return instance;
	}
}
