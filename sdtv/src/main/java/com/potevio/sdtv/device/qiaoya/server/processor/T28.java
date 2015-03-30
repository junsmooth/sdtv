package com.potevio.sdtv.device.qiaoya.server.processor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.potevio.sdtv.device.qiaoya.server.QEBaseMsg;
import com.potevio.sdtv.device.qiaoya.server.QEClientMsg;
import com.potevio.sdtv.device.qiaoya.server.Util;
import com.potevio.sdtv.util.DateUtil;

/**
 * pulse data
 * 
 * @author sdtv
 *
 */
public class T28 extends AbstractRequestMsg {
	private String returnMsgCode = "S28";
	private static final Logger logger = LoggerFactory.getLogger(T28.class);

	@Override
	public void process(QEClientMsg msg) {
		List<String> paramsList = msg.getParamsList();
		String dataType = paramsList.get(0);
		String pulseCount = paramsList.get(1);
		String timen = paramsList.get(2);
		// deal with data store, send data to platform etc.
		Util.sendReturnMessage(msg, returnMsgCode);
	}

}
