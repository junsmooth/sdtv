package com.potevio.sdtv.device.hiyo;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.potevio.sdtv.util.CacheUtil;
import com.potevio.sdtv.util.MD5Util;
import com.potevio.sdtv.util.SocketUtil;

public class HiyoClientHandler implements IoHandler {
	private static final Logger logger = LoggerFactory
			.getLogger(HiyoClientHandler.class);
	private static final String KEY_HEARTBEAT_TIME = "heart_beat_time";
	private static ConcurrentHashMap<Long, IoSession> sessionMap = new ConcurrentHashMap();

	public HiyoClientHandler() {
		Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
				new Runnable() {
					@Override
					public void run() {
						for (Long id : sessionMap.keySet()) {
							IoSession session = sessionMap.get(id);
							if (session != null) {
								Long time = (Long) session.getAttribute(KEY_HEARTBEAT_TIME);
								Long current = System.currentTimeMillis();
								if ((current - time) > 5000) {
									// 超过5秒，未收到返回心跳
									logger.info("HeartBeat TimeOut,Close Session.");
									session.close(true);
									sessionMap.remove(session.getId());
								}
							}
						}
					}
				}, 0, 10, TimeUnit.SECONDS);
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		logger.info("SESSION CREATED:" + session);
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		logger.info("SESSION OPENED:" + session);
		doLogin(session);
	}

	private void doLogin(IoSession session) {
		HiyoMSG msg = new HiyoMSG();
		byte[] cmd = new byte[2];
		cmd[0] = 0x1f;
		cmd[1] = 0x40;
		msg.setCmd(cmd);
		Map<String, String> bodyMap = new HashMap<String, String>();
		bodyMap.put("apikey", "e121a422cd24c55005ea81b0ade9d7f9");
		bodyMap.put("tm", "" + System.currentTimeMillis() / 1000);

		String content = bodyMap.get("apikey") + bodyMap.get("tm")
				+ "f3c3095bd0ba39868a3de6a901021e24";
		bodyMap.put("sign", MD5Util.string2MD5(content));

		String json = JSON.toJSONString(bodyMap);
		msg.setMsg(json);
		session.write(msg);

	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		logger.error("CLOSED:" + session);
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
//		logger.info("IDLE:" + session);
		sendHeartBeat(session);

	}

	private void sendHeartBeat(IoSession session) {
		HiyoMSG msg = new HiyoMSG();
		byte[] cmd = new byte[2];
		cmd[0] = 0x1f;
		cmd[1] = 0x41;
		msg.setCmd(cmd);
		Map<String, String> bodyMap = new HashMap();
		bodyMap.put("apikey", "e121a422cd24c55005ea81b0ade9d7f9");
		String json = JSON.toJSONString(bodyMap);
		msg.setMsg(json);
//		logger.error("HIYO WRITE HEARTBEAT:" + session);
		session.write(msg);
		session.setAttribute(KEY_HEARTBEAT_TIME, System.currentTimeMillis());
		sessionMap.put(session.getId(), session);
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		logger.error("HIYO Client EXCEPTION:" + session);
		logger.error(cause.getMessage());
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		logger.info("RECV MSG:" + message);
		HiyoMSG msg = (HiyoMSG) message;

		byte[] cmdByts = msg.getCmd();

		int cmd = SocketUtil.byteArrayToInt(cmdByts, 0, 2);
		if (8001 == cmd) {
			// heartbeat resp
			sessionMap.remove(session.getId());
		}
		if (8003 == cmd) {
			CacheUtil.getHiyoQueue().put(msg);
		}
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		logger.info("SENT:" + message);
	}

	@Override
	public void inputClosed(IoSession session) throws Exception {

	}

}
