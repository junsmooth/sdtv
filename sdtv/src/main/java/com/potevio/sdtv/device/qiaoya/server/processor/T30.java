package com.potevio.sdtv.device.qiaoya.server.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.potevio.sdtv.device.qiaoya.server.QEClientMsg;
import com.potevio.sdtv.device.qiaoya.server.Util;

@Component
public class T30 extends AbstractRequestMsg {
	private static final Logger logger = LoggerFactory.getLogger(T30.class);
	private String returnMsgCode = "S30";

	@Override
	public void process(QEClientMsg msg) {
		Util.sendReturnMessage(msg, returnMsgCode);
	}

}
