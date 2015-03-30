package com.potevio.sdtv.device.qiaoya.server.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.potevio.sdtv.device.qiaoya.server.QEBaseMsg;
import com.potevio.sdtv.device.qiaoya.server.QEClientMsg;
import com.potevio.sdtv.util.DateUtil;

public class T1 extends AbstractRequestMsg {

	private String returnMsgCode = "S1";
	private static final Logger logger = LoggerFactory.getLogger(T1.class);

	private static T1 instance = new T1();

	@Override
	public void process(QEClientMsg msg) {
		String imsi = msg.getParamsList().get(0);
		String softVer = msg.getParamsList().get(1);
		// [V1.0.0, a1d83kdeio3fg33k,1,abcd,2011-12-15 10:00:00,
		// 355372020827303,S1,1,2013-1-1 10:10:10,]
		QEBaseMsg returnMsg = new QEBaseMsg();
		returnMsg.setMsgCode(returnMsgCode);
		returnMsg.setPackSN(msg.getPackSN());
		returnMsg.setDateTime(DateUtil.currentCommoneDateString());
		returnMsg.setEncryptType(1);
		returnMsg.setEncryptValue("1234");
		returnMsg.setImei(msg.getImei());
		// 1 login success
		returnMsg.getParamsList().add("1");
		returnMsg.getParamsList().add(DateUtil.currentCommoneDateString());
		returnMsg.getParamsList().add("");
		msg.getSession().write(returnMsg.toString());
	}

	@Override
	public QEMsgProcessor getInstance() {
		return instance;
	}

}
