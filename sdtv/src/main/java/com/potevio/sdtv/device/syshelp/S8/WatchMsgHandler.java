package com.potevio.sdtv.device.syshelp.S8;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.potevio.sdtv.device.syshelp.WatchMSG;
import com.potevio.sdtv.util.CacheUtil;
import com.potevio.sdtv.util.DateUtil;

public class WatchMsgHandler extends IoHandlerAdapter {
	private static final Logger logger = LoggerFactory
			.getLogger(WatchMsgHandler.class);

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		String msg = message.toString();
//		CacheUtil.getSyshelpMessageQueue().add(msg);
		WatchMSG watch = new WatchMSG();
		logger.info("IN WATCH:" + msg);
		String str = msg.replace('[', ' ').replace(']', ' ').trim();
		String[] arr = str.split(",");

		String ver = arr[0];
		String V1 = "V1.0.0";
		String mobile = arr[6];
		String timeStr=arr[4];
		watch.setDataTime(DateUtil.string2Date(timeStr));
		watch.setMobile(mobile);
		watch.setDatatype("");
		if (str.contains("mt_pulse")) {
			watch.setDatatype(WatchMSG.DATATYPE_PUSE);
			String pulse = arr[10];
			watch.setPulsecount(pulse);
			CacheUtil.setWatchLatest(watch);
		}
		if (str.contains("LBS")) {
			watch.setDatatype(WatchMSG.DATATYPE_GPS);
			String MCCMNC = arr[10];
			String LAC = arr[11];
			String CELL = arr[12];
			watch.setLBS("1;" + MCCMNC + "," + LAC + "," + CELL);
		}
		if (str.contains("GPS")) {
			watch.setDatatype(WatchMSG.DATATYPE_GPS);
			String lat = arr[11];
			String lon = arr[10];
			watch.setLongitude(lon);
			watch.setLatitude(lat);
		}

		watch.setTimen(arr[arr.length - 1]);
		CacheUtil.getSyshelpWatchQueue().put(watch);
//		if (str.contains("LBS")) {
//			// LBS request syshelp web to get gps
//		} else {
//			CacheUtil.getSyshelpWatchQueue().put(watch);
//		}

	}

}
