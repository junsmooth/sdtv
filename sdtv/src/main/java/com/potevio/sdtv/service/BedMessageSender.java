package com.potevio.sdtv.service;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.potevio.sdtv.device.hiyo.HiyoMSG;
import com.potevio.sdtv.device.ythtjr.BedMSG;
import com.potevio.sdtv.domain.PlatformProperties;
import com.potevio.sdtv.util.CacheUtil;

@Component
public class BedMessageSender {
	private static Logger logger = LoggerFactory
			.getLogger(BedMessageSender.class);
	@Autowired
	private PlatformProperties prop;

	@PostConstruct
	public void send() {
		getAndSendHTJRBedMSG();
		getAndSendHiyoBedMSG();

	}

	private void getAndSendHiyoBedMSG() {

		Executors.newSingleThreadExecutor().execute(new Runnable() {
			@Override
			public void run() {
				while (true) {
					HiyoMSG msg;
					try {
						msg = CacheUtil.getHiyoQueue().take();
						// cast hiyo to bed
						String json = msg.getMsg();

						logger.info("IN HIYO:" + json);

						Map<String, Object> jsonMap = (Map<String, Object>) JSON
								.parse(json);
						Integer z = (Integer) jsonMap.get("z");

						if (1 == z) {

							String alt = jsonMap.get("alt").toString();
							BedMSG bedMsg = new BedMSG();
							bedMsg.setDeviceid(jsonMap.get("dev").toString());

							if ("6".equals(alt) || "7".equals(alt)) {
								// 离床
								bedMsg.setHeartrating("0");
								bedMsg.setResping("0");
								bedMsg.setStatus("50");
							}
							if ("2".equals(alt) || "1".equals(alt)) {
								// 体动
								bedMsg.setHeartrating(jsonMap.get("hit")
										.toString());
								bedMsg.setResping(jsonMap.get("bre").toString());
								bedMsg.setStatus("41");
							}
							if ("3".equals(alt) || "4".equals(alt)) {
								bedMsg.setHeartrating(jsonMap.get("hit")
										.toString());
								bedMsg.setResping(jsonMap.get("bre").toString());
								bedMsg.setStatus("20");
							}
							sendBedMsg(bedMsg);
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}
		});

	}

	private void getAndSendHTJRBedMSG() {
		Executors.newSingleThreadExecutor().execute(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						BedMSG msg = (BedMSG) CacheUtil.getYthtjrbedQueue()
								.take();
						sendBedMsg(msg);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}
		});
	}

	public void sendBedMsg(BedMSG msg) {
		String url = prop.getBaseurl() + "/" + prop.getBedaction();
		String parm = "?" + "deviceid=" + msg.getDeviceid() + "&heartrating="
				+ msg.getHeartrating() + "&resping=" + msg.getResping()
				+ "&status=" + msg.getStatus();
		String contentUrl = url + parm;
		try {
			logger.info("OUT BED:" + contentUrl);
			String result = Request.Get(contentUrl).execute().returnContent()
					.asString();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
