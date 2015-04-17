package com.potevio.sdtv.device.ythtjr.android.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.potevio.sdtv.device.ythtjr.android.BedData;
import com.potevio.sdtv.device.ythtjr.android.socket.AndroidMSG;
import com.potevio.sdtv.device.ythtjr.android.socket.AndroidMsgHandler;
import com.potevio.sdtv.service.BedDataService;

@Service("android_bed_status_list")
public class BED_STATUS_LIST implements IAndroidMessageProcessor {
	private static Logger logger = LoggerFactory
			.getLogger(BED_STATUS_LIST.class);
	@Autowired
	private BedDataService service;

	@Override
	public void process(AndroidMSG msg) {
		try {
			Map m = (Map) msg.getData();
			String bedSeriedId = m.get("bedSeriesId").toString();
			Map search = new HashMap();
			search.put("bedSeriesId", bedSeriedId);
			DateTime dt = DateTime.now().withTimeAtStartOfDay();
			search.put("dataTimeBegin", dt.toDate());
			search.put("sortColumns", "dataTime asc");
			// List<BedData> bedDataList = bedDataManager.findAll(search);
			List<BedData> bedDataList = service.findAll(dt.toDate());
			List timeMapList = new ArrayList();
			Map timeMap = null;
			for (BedData bedData : bedDataList) {
				String statusCode = bedData.getStatusCode();
				if (!"50".equals(statusCode) && !"D0".equals(statusCode)) {
					if (timeMap == null) {
						timeMap = new HashMap();
						timeMap.put("startTime", bedData.getDataTime()
								.getTime());
					}
					timeMap.put("endTime", bedData.getDataTime().getTime());
				} else {
					if (timeMap != null) {
						timeMapList.add(timeMap);
						timeMap = null;
					}
				}
			}
			Map returnMap = new HashMap();
			returnMap.put("type", AndroidMSG.BED_STATUS_LIST_RSP);
			returnMap.put("data", timeMapList);
			String json = JSON.toJSONString(returnMap);
			msg.getSession().write(json);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
