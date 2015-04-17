package com.potevio.sdtv.device.ythtjr.android.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.potevio.sdtv.device.ythtjr.android.socket.AndroidMSG;

@Component("android_area_report")
public class AREA_REPORT implements IAndroidMessageProcessor {
	public static final String KEY_SELECTED_AREAS = "key_selected_areas";
	private static Logger logger = LoggerFactory
			.getLogger(AREA_REPORT.class);
	@Override
	public void process(AndroidMSG msg) {
		List<String> areas = getAreaNameList(msg);
		msg.getSession().setAttribute(KEY_SELECTED_AREAS, areas);
		Map m = new HashMap();
		m.put("status", "SUCCESS");
		AndroidMSG rtmsg = new AndroidMSG();
		rtmsg.setType(AndroidMSG.AREA_REPORT_RSP);
		rtmsg.setData(m);
		msg.getSession().write(JSON.toJSONString(rtmsg));
	}

	private List<String> getAreaNameList(AndroidMSG msg) {
		List<String> areaList = new ArrayList<String>();
		List areaListRec = (List) msg.getData();
		for (Object hpArea : areaListRec) {
			areaList.add(((Map) hpArea).get("areaName").toString());
		}
		return areaList;
	}
}
