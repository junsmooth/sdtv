package com.potevio.sdtv.device.qiaoya.server.processor;

import com.potevio.sdtv.device.qiaoya.server.QEClientMsg;

public interface QEMsgProcessor {

	void process(QEClientMsg msg);
	QEMsgProcessor getInstance();
	
}
