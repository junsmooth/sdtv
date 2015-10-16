//package com.potevio.sdtv.biz;
//
//import java.io.IOException;
//
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.fluent.Request;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//
//import com.potevio.sdtv.device.syshelp.WatchMSG;
//import com.potevio.sdtv.domain.PlatformProperties;
//import com.potevio.sdtv.util.CacheUtil;
//
////@Service
//public class WatchTimer {
//	@Autowired
//	private PlatformProperties prop;
//
//	@Scheduled(fixedDelay = Long.MAX_VALUE)
//	private void sendWatchTimer() throws Exception {
//		System.out.println("start watch timer");
//		while (true) {
//			WatchMSG msg;
//			try {
//				msg = (WatchMSG) CacheUtil.getSyshelpWatchQueue().take();
//				System.out.println("begin send watch.");
//				sendWatchMSG(msg);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//
//		}
//
//	}
//
//	private void sendWatchMSG(WatchMSG msg) {
//		String url = prop.getBaseurl() + "/" + prop.getWatchaction();
//		String parm = "?" + "mobile=" + msg.getMobile() + "&datatype="
//				+ msg.getDatatype();
//		if ("pulsecount".equals(msg.getDatatype())) {
//			parm += "&pulsecount=" + msg.getPulsecount();
//
//		} else if ("GPS_AT".equals(msg.getDatatype())) {
//			parm += "&longitude=" + msg.getLongitude() + "&latitude="
//					+ msg.getLatitude() + "&height=" + msg.getHeight()
//					+ "&speed=" + msg.getSpeed() + "&timen" + msg.getTimen()
//					+ "&LBS=" + msg.getLBS();
//		}
//
//		String contentUrl = url + parm;
//		System.out.println(contentUrl);
//		try {
//			String result = Request.Get(contentUrl).execute().returnContent()
//					.asString();
//			System.out.println("Watch API RESULT:" + result);
//		} catch (ClientProtocolException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
//
//}
