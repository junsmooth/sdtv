package com.potevio.sdtv.biz;

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
import com.potevio.sdtv.domain.BedData;
import com.potevio.sdtv.domain.PlatformProperties;
import com.potevio.sdtv.service.BedDbService;
import com.potevio.sdtv.util.CacheUtil;

@Component
public class BedMessageSender {
	private static Logger logger = LoggerFactory
			.getLogger(BedMessageSender.class);
	@Autowired
	private PlatformProperties prop;

	@Autowired
	private BedDbService bedService;

	@PostConstruct
	public void send() {
		getAndSendHTJRBedMSG();
//		getAndSendHiyoBedMSG();

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
//							BedMSG bedMsg = new BedMSG();
							BedData bedMsg=new BedData();
							bedMsg.setSeriesId(jsonMap.get("dev").toString());

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
							if ("3".equals(alt) || "4".equals(alt)||"5".equals(alt)) {
								bedMsg.setHeartrating(jsonMap.get("hit")
										.toString());
								bedMsg.setResping(jsonMap.get("bre").toString());
								bedMsg.setStatus("20");
							}

//							Bed bed = new Bed();
//							bed.setSeriesId(bedMsg.getDeviceid());
//							bed.setOccurTime(new Date());
//							bed.setResping(bedMsg.getResping());
//							bed.setHeartrating(bedMsg.getHeartrating());
//							bed.setStatus(bedMsg.getStatus());
							bedService.insert(bedMsg);
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
						BedData msg = (BedData) CacheUtil.getYthtjrbedQueue()
								.take();
						sendBedMsg(msg);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}
		});
	}

	public void sendBedMsg(BedData msg) {
		String url = prop.getBaseurl() + "/" + prop.getBedaction();
		String parm = "?" + "deviceid=" + msg.getSeriesId() + "&heartrating="
				+ msg.getHeartrating() + "&resping=" + msg.getResping()
				+ "&status=" + msg.getStatus();
		String contentUrl = url + parm;
		try {
			logger.info("OUT BED:" + contentUrl);
			String result = Request.Get(contentUrl).execute().returnContent()
					.asString();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("OUT BED ERR:"+e.getMessage());
		}

	}
}
