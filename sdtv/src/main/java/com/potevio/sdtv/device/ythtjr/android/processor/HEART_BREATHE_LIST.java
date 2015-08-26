package com.potevio.sdtv.device.ythtjr.android.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.potevio.sdtv.device.ythtjr.android.socket.AndroidMSG;
import com.potevio.sdtv.domain.BedData;
import com.potevio.sdtv.util.CacheUtil;

@Service("android_heart_breath_list")
public class HEART_BREATHE_LIST implements IAndroidMessageProcessor {


	@Override
	public void process(AndroidMSG msg) {

		try {
			Map m = (Map) msg.getData();
			String bedSeriedId = m.get("bedSeriesId").toString();
			String num = m.get("num").toString();
			int numInt = Integer.parseInt(num);

			Map<String, List<BedData>> cache = CacheUtil.getBedCacheMap();
			List cachList = cache.get("Z20245");
			List<BedData> dataList = new ArrayList();
			if(cachList!=null){
				if (cachList.size() <= numInt) {
					dataList = cachList;
				} else {
					int size = cachList.size();
					dataList = cachList.subList(size - numInt - 1, size - 1);
				}
			}
			
			List resultList = new ArrayList();
			// int len = bedDataList.size();
			for (BedData data : dataList) {
				int h = Integer.parseInt(data.getHeartrating());
				int r = Integer.parseInt(data.getResping());
				long t = data.getOccurTime().getTime();
				Map v = new HashMap();
				v.put("heart", h);
				v.put("breath", r);
				v.put("time", t);
				resultList.add(v);
			}
			Map returnMap = new HashMap();
			returnMap.put("type", AndroidMSG.HEART_BREATH_LIST_RSP);
			returnMap.put("data", resultList);
			String json = JSON.toJSONString(returnMap);
			msg.getSession().write(json);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
