package com.potevio.sdtv.device.ythtjr.android.processor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.potevio.sdtv.device.ythtjr.android.socket.AndroidMSG;

@Component("android_heartbeat")
public class HEARTBEAT implements IAndroidMessageProcessor {

	@Override
	public void process(AndroidMSG msg) {
		Map m = new HashMap();
		m.put("type", AndroidMSG.HEARTBEAT_RSP);
		msg.getSession().write(JSON.toJSONString(m));
	}

}
