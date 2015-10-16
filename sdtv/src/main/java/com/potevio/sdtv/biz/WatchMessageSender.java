package com.potevio.sdtv.biz;

import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.potevio.sdtv.device.minigps.CellTower;
import com.potevio.sdtv.device.minigps.MiniGPS;
import com.potevio.sdtv.device.minigps.MiniGPSRequest;
import com.potevio.sdtv.device.minigps.MiniGPSResult;
import com.potevio.sdtv.device.syshelp.WatchMSG;
import com.potevio.sdtv.device.syshelp.S8.SyshelpClient;
import com.potevio.sdtv.domain.LBS;
import com.potevio.sdtv.domain.MapXY;
import com.potevio.sdtv.domain.PlatformProperties;
import com.potevio.sdtv.domain.Watch;
import com.potevio.sdtv.service.WatchService;
import com.potevio.sdtv.util.CacheUtil;

@Component
public class WatchMessageSender {
	@Autowired
	private PlatformProperties prop;
	private static Logger logger = LoggerFactory
			.getLogger(WatchMessageSender.class);

	@Autowired
	private WatchService watchService;

	private final static String ACCEPT = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";
	private final static String AGENT = "Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0";

	@PostConstruct
	private void startSender() {
		getAndSendSysHelpMSG();
		// getAndSendSyshelpString();
		getAndSendWatch();
	}

	private void getAndSendWatch() {

		Executors.newSingleThreadExecutor().execute(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Watch msg = (Watch) CacheUtil.getWatchQueue().take();
						sendWatch(msg);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	// private void retriveMessage(String timeStr) throws Exception {
	// Thread.sleep(20000);
	// BasicCookieStore cookieStore = new BasicCookieStore();
	// CloseableHttpClient httpclient = HttpClients.custom()
	// .setDefaultCookieStore(cookieStore).build();
	// try {
	// HttpUriRequest login = RequestBuilder
	// .post()
	// .setUri(new URI("http://" + SyshelpClient.host
	// + ":8006/Web/Common/Member.ashx"))
	// .addParameter(
	// "__VIEWSTATE",
	// "/wEPDwUKMTIxODI1MjU1NGQYAQUeX19Db250cm9sc1JlcXVpcmVQb3N0QmFja0tleV9fFgEFCmlzcmVtZW1iZXIlno+U3SXD6L3iitGwlUv1IIc4KzaEqTcZ2ri3qWLlZQ==")
	// .addParameter("__VIEWSTATEGENERATOR", "C714824B")
	//
	// .addParameter(
	// "__EVENTVALIDATION",
	// "/wEWBALAtOC8DwKpwK/OBgK1qbSRCwLQw5PyCxQBzqH6uli+cb6wzo/8F6JXFibokbRS/JQu01cmT8nV")
	// .addParameter("postType", "login")
	// .addParameter("txtLoginName", "putian")
	// .addParameter("txtPassword", "putian").build();
	// CloseableHttpResponse response2 = httpclient.execute(login);
	// try {
	// HttpEntity entity = response2.getEntity();
	// logger.info("Login form get: " + response2.getStatusLine());
	// EntityUtils.consume(entity);
	// logger.debug("Post logon cookies:");
	// List<Cookie> cookies = cookieStore.getCookies();
	// if (cookies.isEmpty()) {
	// logger.debug("None");
	// } else {
	// for (int i = 0; i < cookies.size(); i++) {
	// logger.debug("- " + cookies.get(i).toString());
	// }
	// }
	// } finally {
	// response2.close();
	// }
	// HttpGet httpget = new HttpGet("http://" + SyshelpClient.host
	// + ":8006//Web/DeviceLocation.aspx" + "?id="
	// + "7B67D3EC054CBAC657C9445F927818F8");
	// httpget.addHeader("Accept", ACCEPT);
	// httpget.addHeader("User-Agent", AGENT);
	//
	// ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
	//
	// public String handleResponse(final HttpResponse response)
	// throws ClientProtocolException, IOException {
	// int status = response.getStatusLine().getStatusCode();
	// if (status >= 200 && status < 300) {
	// HttpEntity entity = response.getEntity();
	// return entity != null ? EntityUtils.toString(entity)
	// : null;
	// } else {
	// throw new ClientProtocolException(
	// "Unexpected response status: " + status);
	// }
	// }
	//
	// };
	// String resultString = httpclient.execute(httpget, responseHandler);
	// resultString = StringUtils.substringBetween(resultString,
	// "Location(", ")");
	// // try second time
	// if (!resultString.contains(timeStr)) {
	// resultString = httpclient.execute(httpget, responseHandler);
	// resultString = StringUtils.substringBetween(resultString,
	// "Location(", ")");
	// }
	// System.out.println(timeStr + "," + resultString);
	// // if timestr match
	// WatchMSG watch = new WatchMSG();
	// watch.setDatatype(WatchMSG.DATATYPE_GPS);
	// String[] arrStrings = StringUtils.split(resultString, ",");
	// String lon = StringUtils.substringBetween(arrStrings[0], "'");
	// String lat = StringUtils.substringBetween(arrStrings[1], "'");
	// watch.setMobile(StringUtils.substringBetween(arrStrings[2], "'"));
	//
	// //
	// http://api.map.baidu.com/geoconv/v1/?coords=114.21892734521,29.575429778924;114.21892734521,29.575429778924&from=1&to=5&ak=
	// MapXY pointMapXY = new MapXY();
	// pointMapXY.setX(lon);
	// pointMapXY.setY(lat);
	//
	// pointMapXY = transferToBDMap(pointMapXY);
	// // jingdu
	// watch.setLongitude(pointMapXY.getX());
	// // weidu
	// watch.setLatitude(pointMapXY.getY());
	// System.out.println(resultString);
	// CacheUtil.getSyshelpWatchQueue().put(watch);
	// } finally {
	// httpclient.close();
	// }
	//
	// }

	// private MapXY transferToBDMap(MapXY pointMapXY) {
	// //
	// http://api.map.baidu.com/geoconv/v1/?coords=114.21892734521,29.575429778924;114.21892734521,29.575429778924&from=1&to=5&ak=30b8356b2e5d2693e2391f3e39aef2ad
	// String apiHostString = "http://api.map.baidu.com/geoconv/v1/?";
	// String coordsString = pointMapXY.getX() + "," + pointMapXY.getY();
	// String urlString = apiHostString + "coords=" + coordsString
	// + "&from=3&to=5&ak=30b8356b2e5d2693e2391f3e39aef2ad";
	// try {
	// String resultString = Request.Get(urlString).execute()
	// .returnContent().asString();
	// System.out.println(resultString);
	// String xString = StringUtils.substringBetween(resultString, "x\":",
	// ",");
	// System.out.println(xString);
	// String yString = StringUtils.substringAfterLast(resultString,
	// "y\":");
	// yString = StringUtils.substringBefore(yString, "}");
	// System.out.println(yString);
	// pointMapXY.setX(xString);
	// pointMapXY.setY(yString);
	//
	// } catch (ClientProtocolException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return pointMapXY;
	// }

	// send watch message to syshelp
	// private void getAndSendSyshelpString() {
	// Executors.newSingleThreadExecutor().execute(new Runnable() {
	// @Override
	// public void run() {
	// while (true) {
	// try {
	// String msgString = CacheUtil.getSyshelpMessageQueue()
	// .take();
	//
	// IoSession session = SyshelpClient.getSession();
	// if (session != null) {
	// session.write(msgString);
	// }
	// // match msg time
	//
	// // /login syshelp web, and retrive gps message
	// if (msgString.contains("LBS")) {
	// String timeString = StringUtils.substringBetween(
	// msgString, "[", "]");
	// String[] arrStrings = timeString.split(",");
	// // V1.0.0,460026103449054,1,abcd,2015-02-15
	// // 13:55:10,1-2,869816413469526,8,T52,LBS,46000,4566,29990,1424008510
	// timeString = arrStrings[4];
	//
	// retriveMessage(timeString);
	// }
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// }
	// });
	//
	// }

	private void getAndSendSysHelpMSG() {
		Executors.newSingleThreadExecutor().execute(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						WatchMSG msg = (WatchMSG) CacheUtil
								.getSyshelpWatchQueue().take();
						sendWatchMSG(msg);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}
		});

	}

	private void sendWatch(Watch watch) {
		String typeString = watch.getDataType();
		if (typeString == null) {
			// not pulse or gps
			return;
		}
		if (typeString.equals(Watch.DT_PULSE)) {
			typeString = WatchMSG.DATATYPE_PUSE;
		} else if (typeString.equals(Watch.DT_GPS)
				|| typeString.equals(Watch.DT_LBS)) {
			typeString = WatchMSG.DATATYPE_GPS;
		}

		String parm = "?" + "mobile=" + watch.getImeiString() + "&datatype="
				+ typeString;
		if ("pulse".equals(typeString)) {
			parm += "&pulsecount=" + watch.getHeartbeat();

		} else if ("GPS_AT".equals(typeString)) {
			// TODO lbs
			String lbsString = "";
			/**
			 * num$mcc$mnc#lac,cell,power,level#lac,cell,power,level
			 * 
			 * 
			 */
			if (watch.getLbsList().size() > 0) {
				StringBuffer sBuffer = new StringBuffer();
				sBuffer.append(watch.getLbsList().size()).append(";");
				LBS common = watch.getLbsList().get(0);
				sBuffer.append(common.getMcc()).append(";");
				sBuffer.append(common.getMnc());
				for (LBS lbsobj : watch.getLbsList()) {
					sBuffer.append("~");
					sBuffer.append(lbsobj.getLac()).append("_");
					sBuffer.append(lbsobj.getCell()).append("_");
					sBuffer.append(lbsobj.getPower()).append("_");
					sBuffer.append(lbsobj.getLevel());
				}
				lbsString = sBuffer.toString();
			}

			parm += "&longitude=" + watch.getLongitude() + "&latitude="
					+ watch.getLatitude() + "&height=" + watch.getHeight()
					+ "&speed=" + watch.getSpeed() + "&timen="
					+ watch.getDataTime().getTime() + "&LBS=" + lbsString;
		}

		String[] baseUrls = prop.getBaseUrls();
		for (String baseUrl : baseUrls) {
			String contentUrl = baseUrl + "/" + prop.getWatchaction() + parm;
			try {
				logger.info("OUT WATCH:" + contentUrl);
				String result = Request.Get(contentUrl).execute()
						.returnContent().asString();
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("OUT WATCH ERR:" + e.getMessage());
			}

		}

	}

	private void sendWatchMSG(WatchMSG msg) {

		Watch w = new Watch();
		w.setCreateDate(new Date());
		// pulse, GPS, LBS

		if (WatchMSG.DATATYPE_PUSE.equals(msg.getDatatype())) {
			w.setDataType(Watch.DT_PULSE);
		} else if (WatchMSG.DATATYPE_GPS.equals(msg.getDatatype())) {
			w.setDataType(Watch.DT_GPS);
		}
		w.setDataTime(msg.getDataTime());
		w.setHeartbeat(msg.getPulsecount());
		w.setHeight(msg.getHeight());
		w.setImeiString(msg.getMobile());
		w.setLatitude(msg.getLatitude());
		w.setLongitude(msg.getLongitude());
		w.setSpeed(msg.getSpeed());
		w.setVendor(Watch.VENDOR_SYSHELP);
		String lbs = msg.getLBS();
		logger.info("syshelp LBS ->" + lbs);
		if (StringUtils.isNotBlank(lbs)) {
			String numString = StringUtils.substringBefore(lbs, ";");
			int num = Integer.parseInt(numString);
			String body = StringUtils.substringAfter(lbs, ";");
			String[] stations = StringUtils.split(body, ";");
			for (int i = 0; i < num; i++) {
				String str = stations[i];
				String[] arr = StringUtils.split(str, ",");
				String mccMnc = arr[0];
				String lac = arr[1];
				String cell = arr[2];
				LBS lbs2 = new LBS();
				lbs2.setMcc(StringUtils.substring(mccMnc, 0, 3));
				lbs2.setMnc(StringUtils.substring(mccMnc, 3));
				lbs2.setLac(lac);
				lbs2.setCell(cell);
				w.addLbs(lbs2);
				lbs2.setWatch(w);
			}
			// parse lbs

			List<LBS> lbsList = w.getLbsList();
			if (lbsList != null && lbsList.size() > 0) {
				LBS selctedLbs = lbsList.get(0);
				MiniGPSRequest req = new MiniGPSRequest();
				CellTower ct = new CellTower(selctedLbs.getMcc(),
						Integer.parseInt(selctedLbs.getMnc()) + "",
						selctedLbs.getLac(), selctedLbs.getCell());
				req.addCellTower(ct);
				try {
					MiniGPSResult rst = MiniGPS.getGPS(req);
					logger.info("MiniGPS Location:" + rst.getLocation());
					double lat = rst.getLocation().getLatitude();
					rst.getLocation().getAddress().getStreet();
					double lon = rst.getLocation().getLongitude();
					MapXY pointMapXY = new MapXY();
					pointMapXY.setX(lon + "");
					pointMapXY.setY(lat + "");
					// transferToBDMap(pointMapXY);
					w.setStreet(rst.getLocation().getAddress().getStreet());
					w.setLatitude(pointMapXY.getY());
					w.setLongitude(pointMapXY.getX());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		watchService.insertWatch(w);
		sendWatch(w);

		// String url = prop.getBaseurl() + "/" + prop.getWatchaction();
		// String parm = "?" + "mobile=" + msg.getMobile() + "&datatype="
		// + msg.getDatatype();
		// if ("pulse".equals(msg.getDatatype())) {
		// parm += "&pulsecount=" + msg.getPulsecount();
		//
		// } else if ("GPS_AT".equals(msg.getDatatype())) {
		// parm += "&longitude=" + msg.getLongitude() + "&latitude="
		// + msg.getLatitude() + "&height=" + msg.getHeight()
		// + "&speed=" + msg.getSpeed() + "&timen" + msg.getTimen()
		// + "&LBS=" + msg.getLBS();
		// }
		//
		// String contentUrl = url + parm;
		// try {
		// logger.info("OUT WATCH:" + contentUrl);
		// String result = Request.Get(contentUrl).execute().returnContent()
		// .asString();
		// // logger.info("OUT WATCH RESULT:" + contentUrl);
		// } catch (ClientProtocolException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

	}
}
