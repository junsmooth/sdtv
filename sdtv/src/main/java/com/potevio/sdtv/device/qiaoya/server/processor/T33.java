package com.potevio.sdtv.device.qiaoya.server.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.potevio.sdtv.device.qiaoya.server.QEClientMsg;
import com.potevio.sdtv.device.qiaoya.server.Util;

public class T33 extends AbstractRequestMsg {
	private static final Logger logger = LoggerFactory.getLogger(T33.class);
	private String returnMsgCode = "S33";

	@Override
	public void process(QEClientMsg msg) {
		Util.sendReturnMessage(msg, returnMsgCode);
	}

}
