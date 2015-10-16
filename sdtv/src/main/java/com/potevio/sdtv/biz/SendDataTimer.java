//package com.potevio.sdtv.biz;
//
//import java.io.IOException;
//import java.util.Map;
//import java.util.Random;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;
//
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.HttpResponseException;
//import org.apache.http.client.fluent.Request;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//
//import com.alibaba.fastjson.JSON;
//import com.potevio.sdtv.device.hiyo.HiyoMSG;
//import com.potevio.sdtv.device.syshelp.WatchMSG;
//import com.potevio.sdtv.domain.BedData;
//import com.potevio.sdtv.domain.PlatformProperties;
//import com.potevio.sdtv.util.CacheUtil;
//
////@Service
//public class SendDataTimer {
//	private static Logger logger = LoggerFactory.getLogger(SendDataTimer.class);
//	/**
//	 * http://182.92.169.222/zhyl/saveBedMsg.action?deviceid=2D
//	 * 5EB08B-A517-8E1A-F79A-06FEE05D1031&heartrating=62&resping=102&status=0
//	 */
//	@Autowired
//	private PlatformProperties prop;
//
//	// static {
//	// // start ythtjr bed server
//	// BedServer server = new BedServer();
//	// server.start();
//	// }
//	// static {
//	// SysHelpWatchServer watchServer = new SysHelpWatchServer();
//	// watchServer.start();
//	// }
//
//	@Scheduled(fixedDelay = Long.MAX_VALUE)
//	private void sendDataToPlatform() throws Exception {
//		// startSendYTHTJR_BED();
//
//		startSendSyshelpWATCH();
//
//		startSendMockWatchData();
//
//		// startHiyoClient();
//
//		// startSendHiyoBed();
//	}
//
//	private void startSendHiyoBed() {
//		Executors.newSingleThreadExecutor().execute(new Runnable() {
//			@Override
//			public void run() {
//				while (true) {
//					HiyoMSG msg;
//					try {
//						msg = CacheUtil.getHiyoQueue().take();
//
//						// cast hiyo to bed
//						String json = msg.getMsg();
//
//						logger.info("IN HIYO:" + json);
//
//						Map<String, Object> jsonMap = (Map<String, Object>) JSON
//								.parse(json);
//						Integer z = (Integer) jsonMap.get("z");
//
//						if (1 == z) {
//
//							String alt = jsonMap.get("alt").toString();
//							BedData bedMsg = new BedData();
//							bedMsg.setSeriesId(jsonMap.get("dev").toString());
//
//							if ("6".equals(alt) || "7".equals(alt)) {
//								// 离床
//								bedMsg.setHeartrating("0");
//								bedMsg.setResping("0");
//								bedMsg.setStatus("50");
//							}
//							if ("2".equals(alt) || "1".equals(alt)) {
//								// 体动
//								bedMsg.setHeartrating(jsonMap.get("hit")
//										.toString());
//								bedMsg.setResping(jsonMap.get("bre").toString());
//								bedMsg.setStatus("41");
//							}
//							if ("3".equals(alt) || "4".equals(alt)) {
//								bedMsg.setHeartrating(jsonMap.get("hit")
//										.toString());
//								bedMsg.setResping(jsonMap.get("bre").toString());
//								bedMsg.setStatus("20");
//							}
//							sendBedMsg(bedMsg);
//						}
//
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//
//			}
//		});
//	}
//
//	// private void startHiyoClient() {
//	// Executors.newSingleThreadExecutor().execute(new HiyoClient());
//	// }
//
//	private void startSendMockWatchData() {
//		Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(
//				new Runnable() {
//					@Override
//					public void run() {
//						WatchMSG msg = new WatchMSG();
//						msg.setDatatype("pulse");
//						msg.setMobile("862559019013673");
//						int max = 100;
//						int min = 60;
//						Random random = new Random();
//						int s = random.nextInt(max) % (max - min + 1) + min;
//						msg.setPulsecount(s + "");
//						try {
//							CacheUtil.getSyshelpWatchQueue().put(msg);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
//					}
//				}, 10, 60, TimeUnit.SECONDS);
//	}
//
//	private void startSendSyshelpWATCH() {
//		Executors.newSingleThreadExecutor().execute(new Runnable() {
//			@Override
//			public void run() {
//				while (true) {
//					WatchMSG msg;
//					try {
//						msg = (WatchMSG) CacheUtil.getSyshelpWatchQueue()
//								.take();
//						sendWatchMSG(msg);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//
//			}
//		});
//	}
//
//	private void startSendYTHTJR_BED() {
//		Executors.newSingleThreadExecutor().execute(new Runnable() {
//			@Override
//			public void run() {
//				while (true) {
//					BedData msg;
//					try {
//						msg = (BedData) CacheUtil.getYthtjrbedQueue().take();
//						sendBedMsg(msg);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//
//			}
//		});
//	}
//
//	private void sendWatchMSG(WatchMSG msg) {
//		String url = prop.getBaseurl() + "/" + prop.getWatchaction();
//		String parm = "?" + "mobile=" + msg.getMobile() + "&datatype="
//				+ msg.getDatatype();
//		if ("pulse".equals(msg.getDatatype())) {
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
//		try {
//			logger.info("OUT WATCH:" + contentUrl);
//			String result = Request.Get(contentUrl).execute().returnContent()
//					.asString();
//			// logger.info("OUT WATCH RESULT:" + contentUrl);
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//	}
//
//	private void sendBedMsg(BedData msg) {
//		String url = prop.getBaseurl() + "/" + prop.getBedaction();
//		String parm = "?" + "deviceid=" + msg.getSeriesId() + "&heartrating="
//				+ msg.getHeartrating() + "&resping=" + msg.getResping()
//				+ "&status=" + msg.getStatus();
//		String contentUrl = url + parm;
//		try {
//			logger.info("OUT BED:" + contentUrl);
//			String result = Request.Get(contentUrl).execute().returnContent()
//					.asString();
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//	}
//
//	public void fluentAPIDemo(String contentUrl) throws IOException {
//		try {
//			String resultString = Request.Get(contentUrl).execute()
//					.returnContent().asString();
//
//		} catch (HttpResponseException e) {
//		}
//	}
//}
