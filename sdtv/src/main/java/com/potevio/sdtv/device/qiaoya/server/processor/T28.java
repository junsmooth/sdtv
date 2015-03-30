package com.potevio.sdtv.device.qiaoya.server.processor;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.potevio.sdtv.device.qiaoya.server.QEClientMsg;
import com.potevio.sdtv.device.qiaoya.server.Util;
import com.potevio.sdtv.domain.Watch;
import com.potevio.sdtv.service.WatchService;
import com.potevio.sdtv.util.DateUtil;

/**
 * pulse data
 * 
 * @author sdtv
 *
 */
@Component
public class T28 extends AbstractRequestMsg {
	private String returnMsgCode = "S28";
	private static final Logger logger = LoggerFactory.getLogger(T28.class);
	@Autowired
	private WatchService watchService;

	@Override
	public void process(QEClientMsg msg) {
		try {
			List<String> paramsList = msg.getParamsList();
			String dataType = paramsList.get(0);
			String pulseCount = paramsList.get(1);
			String timen = paramsList.get(2);
			// deal with data store, send data to platform etc.
			Util.sendReturnMessage(msg, returnMsgCode);
			// Save to database
			//
			Watch watch = new Watch();
			watch.setVendor("QE");
			watch.setDataType(Watch.DT_PULSE);
			watch.setHeartbeat(pulseCount);
			watch.setImeiString(msg.getImei());
			try {
				watch.setDataTime(DateUtil.string2Date(msg.getDateTime()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			watch.setCreateDate(new Date());
			watchService.insertWatch(watch);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
