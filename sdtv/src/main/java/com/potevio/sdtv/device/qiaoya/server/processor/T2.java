package com.potevio.sdtv.device.qiaoya.server.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.potevio.sdtv.device.qiaoya.server.QEBaseMsg;
import com.potevio.sdtv.device.qiaoya.server.QEClientMsg;
import com.potevio.sdtv.device.qiaoya.server.Util;
import com.potevio.sdtv.util.DateUtil;

/**
 * Heart Beat
 * 
 * @author sdtv
 *
 */
@Component
public class T2 extends AbstractRequestMsg {
	private String returnMsgCode = "S2";
	private static final Logger logger = LoggerFactory.getLogger(T2.class);

	@Override
	public void process(QEClientMsg msg) {

		Util.sendReturnMessage(msg, returnMsgCode);
	}

}
