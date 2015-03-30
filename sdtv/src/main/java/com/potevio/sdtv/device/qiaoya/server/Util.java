package com.potevio.sdtv.device.qiaoya.server;

import java.util.UUID;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.potevio.sdtv.util.DateUtil;
import com.potevio.sdtv.util.MD5Util;

public class Util {
	private static final Logger logger = LoggerFactory.getLogger(Util.class);

	public static void sendReturnMessage(QEClientMsg clientMsg,
			String returnMsgCode) {
		QEBaseMsg returnMsg = createBaseMsg(clientMsg, returnMsgCode);
		clientMsg.getSession().write(returnMsg.toString());
	}

	public static QEBaseMsg createBaseMsg(QEClientMsg clientMsg,
			String returnMsgCode) {
		QEBaseMsg returnMsg = new QEBaseMsg();
		returnMsg.setPackSN(clientMsg.getPackSN());
		returnMsg.setDateTime(DateUtil.currentCommoneDateString());
		returnMsg.setEncryptType(clientMsg.getEncryptType());
		returnMsg.setEncryptValue(clientMsg.getEncryptValue());
		returnMsg.setMsgCode(returnMsgCode);
		returnMsg.setImei(clientMsg.getImei());
		return returnMsg;
	}

	public static QEBaseMsg createBaseMsg(IoSession session,
			String returnMsgCode, String imei) {
		QEBaseMsg returnMsg = new QEBaseMsg();
		returnMsg.setPackSN(MD5Util.string2MD5(UUID.randomUUID().toString())
				.substring(8, 24));
		returnMsg.setDateTime(DateUtil.currentCommoneDateString());
		returnMsg.setEncryptType(1);
		returnMsg.setEncryptValue("1234");
		returnMsg.setMsgCode(returnMsgCode);
		returnMsg.setImei(imei);
		return returnMsg;
	}
}
