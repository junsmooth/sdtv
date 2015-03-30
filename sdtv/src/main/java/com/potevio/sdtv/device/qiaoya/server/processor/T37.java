package com.potevio.sdtv.device.qiaoya.server.processor;

import com.potevio.sdtv.device.qiaoya.server.QEClientMsg;
import com.potevio.sdtv.device.qiaoya.server.Util;

public class T37 extends AbstractRequestMsg {
	private String returnMsgCode = "S37";

	@Override
	public void process(QEClientMsg msg) {
		//
		Util.sendReturnMessage(msg, returnMsgCode);
	}

}
