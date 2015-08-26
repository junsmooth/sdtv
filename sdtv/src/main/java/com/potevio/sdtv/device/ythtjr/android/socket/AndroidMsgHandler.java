package com.potevio.sdtv.device.ythtjr.android.socket;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.potevio.sdtv.App;
import com.potevio.sdtv.device.ythtjr.android.processor.IAndroidMessageProcessor;
import com.potevio.sdtv.domain.BedData;
import com.potevio.sdtv.util.CacheUtil;

@Component
public class AndroidMsgHandler extends IoHandlerAdapter {
	private static Logger logger = LoggerFactory
			.getLogger(AndroidMsgHandler.class);
	private static Map<Long, IoSession> sessionMap = new HashMap<Long, IoSession>();

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		logger.info("android process " + message);
		try {
			AndroidMSG androidMsg = JSON.parseObject(message.toString(),
					AndroidMSG.class);
			androidMsg.setSession(session);
			String type = androidMsg.getType();
			IAndroidMessageProcessor pro = (IAndroidMessageProcessor) App
					.getContext().getBean("android_" + type.toLowerCase());
			if (pro == null) {
				logger.warn("processor " + "android_" + type.toLowerCase()
						+ " not found");
			} else {
				pro.process(androidMsg);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void cleanSession(IoSession session, String token) {
	}

	@PostConstruct
	public void sendBedData() {
		Executors.newSingleThreadExecutor().execute(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						BedData bd = CacheUtil.getAndroidBedQueue().take();
						try {
							Map m = new HashMap();
							m.put("type", AndroidMSG.BED_DATA);
							m.put("data", bd);
							String json = JSON.toJSONString(m);
							for (IoSession session : sessionMap.values()) {
								session.write(json);
							}
						} catch (Exception e) {
							logger.error("SEND BED MSG ERROR: " + e.getCause());
							e.printStackTrace();
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

				}

			}
		});

	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		sessionMap.put(session.getId(), session);
		logger.info("ANDROID OPENED " + session);
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		logger.info("ANDROID CONNECTED " + session);

	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		sessionMap.remove(session.getId());
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
	}
}
