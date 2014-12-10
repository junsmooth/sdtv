package com.potevio.sdtv.service;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.fluent.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.potevio.sdtv.device.syshelp.WatchMSG;
import com.potevio.sdtv.device.ythtjr.BedMSG;
import com.potevio.sdtv.device.ythtjr.BedServer;
import com.potevio.sdtv.domain.PlatformProperties;
import com.potevio.sdtv.util.CacheUtil;

@Service
public class SendDataTimer {

	/**
	 * http://182.92.169.222/zhyl/saveBedMsg.action?deviceid=2D
	 * 5EB08B-A517-8E1A-F79A-06FEE05D1031&heartrating=62&resping=102&status=0
	 */
	@Autowired
	private PlatformProperties prop;

	static {
		// start ythtjr bed server
		BedServer server = new BedServer();
		server.start();
	}

	@Scheduled(fixedDelay = Long.MAX_VALUE)
	private void sendDataToPlatform() throws Exception {
		System.out.println("start  timer");
		Executors.newSingleThreadExecutor().execute(new Runnable() {
			@Override
			public void run() {
				while(true){
					BedMSG msg;
					try {
						msg = (BedMSG) CacheUtil.getYthtjrbedQueue().take();
						sendBedMsg(msg);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				

			}
		});

		Executors.newSingleThreadExecutor().execute(new Runnable() {
			@Override
			public void run() {
				while (true) {
					WatchMSG msg;
					try {
						msg = (WatchMSG) CacheUtil.getSyshelpWatchQueue()
								.take();
						System.out.println("begin send watch.");
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
		System.out.println(contentUrl);
		try {
			String result = Request.Get(contentUrl).execute().returnContent()
					.asString();
			System.out.println("Watch API RESULT:" + result);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void sendBedMsg(BedMSG msg) {
		String url = prop.getBaseurl() + "/" + prop.getBedaction();
		String parm = "?" + "deviceid=" + msg.getDeviceid() + "&heartrating="
				+ msg.getHeartrating() + "&resping=" + msg.getResping()
				+ "&status=" + msg.getStatus();
		String contentUrl = url + parm;
		System.out.println(contentUrl);
		try {
			String result = Request.Get(contentUrl).execute().returnContent()
					.asString();
			System.out.println("BED API RESULT:" + result);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void fluentAPIDemo(String contentUrl) throws IOException {
		try {
			String resultString = Request.Get(contentUrl).execute()
					.returnContent().asString();

		} catch (HttpResponseException e) {
		}
	}
}
