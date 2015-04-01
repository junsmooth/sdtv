package com.potevio.sdtv.device.qiaoya.server.processor;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.potevio.sdtv.device.qiaoya.server.QEClientMsg;
import com.potevio.sdtv.device.qiaoya.server.Util;
import com.potevio.sdtv.domain.LBS;
import com.potevio.sdtv.domain.Watch;
import com.potevio.sdtv.service.WatchService;
import com.potevio.sdtv.util.CacheUtil;
import com.potevio.sdtv.util.DateUtil;

/**
 * GPS data
 * 
 * @author sdtv
 *
 */
@Component
public class T29 extends AbstractRequestMsg {
	private static final Logger logger = LoggerFactory.getLogger(T29.class);
	private String returnMsgCode = "S29";

	@Autowired
	private WatchService watchService;

	@Override
	public void process(QEClientMsg msg) {
		try {
			Util.sendReturnMessage(msg, returnMsgCode);
			List<String> paramsList = msg.getParamsList();
			String dataType = paramsList.get(0);
			String longitude = paramsList.get(1);
			String latitude = paramsList.get(2);
			// gcj02 -to baidu
			String lbsString = paramsList.get(3);
			String height = paramsList.get(4);
			String speed = paramsList.get(5);
			String direction = paramsList.get(6);
			String timen = paramsList.get(7);

			Watch watch = new Watch();
			watch.setVendor(Watch.VENDOR_QE);
			watch.setImeiString(msg.getImei());
			watch.setCreateDate(new Date());
			watch.setDataTime(DateUtil.string2Date(msg.getDateTime()));
			watch.setDataType(Watch.DT_GPS);
			watch.setLongitude(longitude);
			watch.setLatitude(latitude);

			// parse lbs string
			if (lbsString.contains("mcount")) {
				// multi lbs

				// mcount$count$mcc$mnc$response#lac|cell|power|level#lac2|cell2|...
				String headString = StringUtils.substringBefore(lbsString, "#");
				String parseString = StringUtils
						.substringAfter(headString, "$");
				String[] arr = StringUtils.split(parseString, "$");
				int num = Integer.parseInt(arr[0]);
				String mcc = arr[1];
				String mnc = arr[2];
				String responseTime = arr[3];
				String[] bodyArr = StringUtils.substringAfter(lbsString, "#")
						.split("#");
				for (int i = 0; i < num; i++) {
					try {
						LBS lbs = new LBS();
						lbs.setMcc(mcc);
						lbs.setMnc(mnc);
						String[] lbsArr = StringUtils.split(bodyArr[i], "|");

						String lac = lbsArr[0];

						String cell = lbsArr[1];

						String power = lbsArr[2];

						String level = lbsArr[3];

						lbs.setLac(lac);
						lbs.setCell(cell);
						lbs.setPower(Integer.parseInt(power));
						lbs.setLevel(Integer.parseInt(level));
						lbs.setWatch(watch);
						watch.addLbs(lbs);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			} else {
				// single lbs
				// count$0$1|1111|xxxx

				String str = StringUtils.substringBetween(lbsString, "$", "|");
				String[] arr = StringUtils.split(str, "$");
				LBS lbs = new LBS();
				lbs.setMcc(arr[0]);
				lbs.setMnc(arr[1]);
				String bodyStr = StringUtils.substringAfter(lbsString, "|");
				String[] bodyArr = StringUtils.split(bodyStr, "|");
				lbs.setLac(bodyArr[0]);
				lbs.setCell(bodyArr[1]);
				lbs.setWatch(watch);
				watch.addLbs(lbs);
			}
			// watch.setLbsList(lbsList);
			// lbs.setWatch
			// savewwatch
			watchService.insertWatch(watch);
			CacheUtil.getWatchQueue().put(watch);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
