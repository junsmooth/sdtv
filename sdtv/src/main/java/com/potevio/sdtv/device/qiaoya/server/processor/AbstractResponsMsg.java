package com.potevio.sdtv.device.qiaoya.server.processor;

import java.util.concurrent.ConcurrentHashMap;

import com.potevio.sdtv.device.qiaoya.server.QEClientMsg;

public abstract class AbstractResponsMsg extends AbstractMsgProcessor {
	private static ConcurrentHashMap<String, QEClientMsg> msgMap = new ConcurrentHashMap<String, QEClientMsg>();

	@Override
	public void process(QEClientMsg msg) {
		String imeiString = msg.getImei();
		String codeString = msg.getMsgCode();
		String packageSN = msg.getPackSN();
		String key = imeiString + packageSN + codeString;
		msgMap.put(key, msg);
	}

	public static QEClientMsg getAndRemoveMsg(String packageSN) {
		QEClientMsg msg = msgMap.get(packageSN);
		if (msg != null) {
			msgMap.remove(packageSN);
		}
		return msg;
	}

}
