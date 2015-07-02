package com.potevio.sdtv.device.ythtjr;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.potevio.sdtv.device.ythtjr.android.BedData;
import com.potevio.sdtv.device.ythtjr.android.BedStatus;
import com.potevio.sdtv.service.BedDataService;
import com.potevio.sdtv.util.CacheUtil;

@Service
public class BedMsgHandler extends IoHandlerAdapter {
	private static Logger logger = LoggerFactory.getLogger(BedMsgHandler.class);

	@Autowired
	private BedDataService service;
	
	@Override
	public void sessionOpened(IoSession session) throws Exception {
		logger.info("sessionOpened" + session);
	}
	
	
	@Override
	public void sessionClosed(IoSession session) throws Exception {
		logger.info("sessionClosed" + session);
	}
	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		BedMSG msg = (BedMSG) message;
		logger.info("BED RAW:" + msg);
		if (!msg.getDeviceid().equals("Z20023")) {
			CacheUtil.setBedlatest(msg);
		}
		// else{
		// CacheUtil.setBedlatest(msg);
		// }
		CacheUtil.getYthtjrbedQueue().put(msg);

		if ("Z20245".equals(msg.getDeviceid())) {
			try {
				BedData bd = new BedData();
				bd.setBedSeriesId(msg.getDeviceid());
				bd.setDataTime(new Date());
				bd.setHeartrate(Integer.parseInt(msg.getHeartrating()));
				bd.setResp(Integer.parseInt(msg.getResping()));
				bd.setStatus(BedStatus.getNameByCode(msg.getStatus()));
				bd.setStatusCode(msg.getStatus());
				CacheUtil.getAndroidBedQueue().put(bd);
				service.insert(bd);
				saveCache(bd);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	private void saveCache(BedData bd) {
		Map<String, List<BedData>> dataCache = CacheUtil.getBedCacheMap();
		List cachList = dataCache.get(bd.getBedSeriesId());
		if (cachList == null) {
			cachList = new ArrayList();
		}
		dataCache.put(bd.getBedSeriesId(), cachList);
		cachList.add(bd);
		// \u7f13\u5b58\u5bb9\u91cf\u8fbe\u5230\u4e8c\u500d\u5bb9\u91cf\u540e\u5bb9\u91cf\u51cf\u534a
		if (cachList.size() > 1200 * 2) {
			int size = cachList.size();
			List temList = cachList.subList(size - 1200 - 1, size - 1);
			cachList = temList;
		}
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		super.exceptionCaught(session, cause);
	}
}
