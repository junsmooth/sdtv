package com.potevio.sdtv.service;

import java.io.IOException;
import java.net.URI;
import java.sql.Date;
import java.text.SimpleDateFormat;
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

import com.potevio.sdtv.device.syshelp.WatchMSG;
import com.potevio.sdtv.device.syshelp.S8.SyshelpClient;
import com.potevio.sdtv.domain.PlatformProperties;
import com.potevio.sdtv.util.CacheUtil;

@Component
public class WatchMessageSender {
	@Autowired
	private PlatformProperties prop;
	private static Logger logger = LoggerFactory
			.getLogger(WatchMessageSender.class);

	private final static String ACCEPT = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";
	private final static String AGENT = "Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0";

	@PostConstruct
	private void startSender() {
		getAndSendSysHelpMSG();
		getAndSendSyshelpString();
	}

	private void retriveMessage(String timeStr) throws Exception {
		Thread.sleep(20000);
		BasicCookieStore cookieStore = new BasicCookieStore();
		CloseableHttpClient httpclient = HttpClients.custom()
				.setDefaultCookieStore(cookieStore).build();
		try {
			HttpUriRequest login = RequestBuilder
					.post()
					.setUri(new URI("http://" + SyshelpClient.host
							+ ":8006/Web/Common/Member.ashx"))
					.addParameter(
							"__VIEWSTATE",
							"/wEPDwUKMTIxODI1MjU1NGQYAQUeX19Db250cm9sc1JlcXVpcmVQb3N0QmFja0tleV9fFgEFCmlzcmVtZW1iZXIlno+U3SXD6L3iitGwlUv1IIc4KzaEqTcZ2ri3qWLlZQ==")
					.addParameter("__VIEWSTATEGENERATOR", "C714824B")

					.addParameter(
							"__EVENTVALIDATION",
							"/wEWBALAtOC8DwKpwK/OBgK1qbSRCwLQw5PyCxQBzqH6uli+cb6wzo/8F6JXFibokbRS/JQu01cmT8nV")
					.addParameter("postType", "login")
					.addParameter("txtLoginName", "putian")
					.addParameter("txtPassword", "putian").build();
			CloseableHttpResponse response2 = httpclient.execute(login);
			try {
				HttpEntity entity = response2.getEntity();
				logger.info("Login form get: " + response2.getStatusLine());
				EntityUtils.consume(entity);
				logger.debug("Post logon cookies:");
				List<Cookie> cookies = cookieStore.getCookies();
				if (cookies.isEmpty()) {
					logger.debug("None");
				} else {
					for (int i = 0; i < cookies.size(); i++) {
						logger.debug("- " + cookies.get(i).toString());
					}
				}
			} finally {
				response2.close();
			}
			HttpGet httpget = new HttpGet("http://" + SyshelpClient.host
					+ ":8006//Web/DeviceLocation.aspx" + "?id="
					+ "7B67D3EC054CBAC657C9445F927818F8");
			httpget.addHeader("Accept", ACCEPT);
			httpget.addHeader("User-Agent", AGENT);

			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

				public String handleResponse(final HttpResponse response)
						throws ClientProtocolException, IOException {
					int status = response.getStatusLine().getStatusCode();
					if (status >= 200 && status < 300) {
						HttpEntity entity = response.getEntity();
						return entity != null ? EntityUtils.toString(entity)
								: null;
					} else {
						throw new ClientProtocolException(
								"Unexpected response status: " + status);
					}
				}

			};
			String resultString = httpclient.execute(httpget, responseHandler);
			resultString = StringUtils.substringBetween(resultString,
					"Location(", ")");
			// try second time
			if (!resultString.contains(timeStr)) {
				resultString = httpclient.execute(httpget, responseHandler);
				resultString = StringUtils.substringBetween(resultString,
						"Location(", ")");
			}
			System.out.println(timeStr + "," + resultString);
			// if timestr match
			WatchMSG watch = new WatchMSG();
			watch.setDatatype(WatchMSG.DATATYPE_GPS);
			String[] arrStrings = StringUtils.split(resultString, ",");
			String lon = StringUtils.substringBetween(arrStrings[0], "'");
			String lat = StringUtils.substringBetween(arrStrings[1], "'");
			watch.setMobile(StringUtils.substringBetween(arrStrings[2], "'"));
			// jingdu
			watch.setLongitude(lon);
			// weidu
			watch.setLatitude(lat);
			System.out.println(resultString);
			CacheUtil.getSyshelpWatchQueue().put(watch);
		} finally {
			httpclient.close();
		}

	}

	// send watch message to syshelp
	private void getAndSendSyshelpString() {
		Executors.newSingleThreadExecutor().execute(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						String msgString = CacheUtil.getSyshelpMessageQueue()
								.take();

						IoSession session = SyshelpClient.getSession();
						if (session != null) {
							session.write(msgString);
						}
						// match msg time

						// /login syshelp web, and retrive gps message
						if (msgString.contains("LBS")) {
							String timeString = StringUtils.substringBetween(
									msgString, "[", "]");
							String[] arrStrings = timeString.split(",");
							// V1.0.0,460026103449054,1,abcd,2015-02-15
							// 13:55:10,1-2,869816413469526,8,T52,LBS,46000,4566,29990,1424008510
							timeString = arrStrings[4];

							retriveMessage(timeString);
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}
		});

	}

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

	private void sendWatchMSG(WatchMSG msg) {
		String url = prop.getBaseurl() + "/" + prop.getWatchaction();
		String parm = "?" + "mobile=" + msg.getMobile() + "&datatype="
				+ msg.getDatatype();
		if ("pulse".equals(msg.getDatatype())) {
			parm += "&pulsecount=" + msg.getPulsecount();

		} else if ("GPS_AT".equals(msg.getDatatype())) {
			parm += "&longitude=" + msg.getLongitude() + "&latitude="
					+ msg.getLatitude() + "&height=" + msg.getHeight()
					+ "&speed=" + msg.getSpeed() + "&timen" + msg.getTimen()
					+ "&LBS=" + msg.getLBS();
		}

		String contentUrl = url + parm;
		try {
			logger.info("OUT WATCH:" + contentUrl);
			String result = Request.Get(contentUrl).execute().returnContent()
					.asString();
			// logger.info("OUT WATCH RESULT:" + contentUrl);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
