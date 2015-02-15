package com.potevio.sdtv.service;

import java.io.IOException;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.potevio.sdtv.device.syshelp.WatchMSG;
import com.potevio.sdtv.device.syshelp.S8.SyshelpClient;
import com.potevio.sdtv.device.ythtjr.BedMsgHandler;
import com.potevio.sdtv.domain.PlatformProperties;
import com.potevio.sdtv.util.CacheUtil;

@Component
public class WatchMessageSender {
	@Autowired
	private PlatformProperties prop;
	private static Logger logger = LoggerFactory
			.getLogger(WatchMessageSender.class);

	@PostConstruct
	private void startSender() {
		getAndSendSysHelpMSG();
		getAndSendSyshelpString();
	}

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
