package com.potevio.sdtv.web;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.potevio.sdtv.domain.Watch;
import com.potevio.sdtv.service.WatchService;

@Controller
@RequestMapping(value = "watch")
public class WatchController {
	@Autowired
	private WatchService watchService;

	@RequestMapping("latest")
	public @ResponseBody Object latestData() {
		Watch watch = watchService.latestData();
		System.out.println(watch);
		if (watch != null) {
			return watch;
		}
		return "";
	}

	@RequestMapping("put")
	public @ResponseBody void putData(HttpServletRequest req,
			HttpServletResponse rep) {
		String query = req.getQueryString();
		System.out.println(query);
		String[] paramArray = query.split("&");
		Map m = new HashMap();
		for (String str : paramArray) {
			if (StringUtils.isNotEmpty(str)) {
				String[] arr = str.split("=");
				m.put(arr[0], arr[1]);
			}
		}
		String mobile = m.get("mobile") != null ? m.get("mobile").toString()
				: "";
		String datatype = m.get("datatype") != null ? m.get("datatype")
				.toString() : "";
		if (!"pulse".equals(datatype)) {
			return;
		}
		String pulsecount = m.get("pulsecount") != null ? m.get("pulsecount")
				.toString() : "-";
		String temperature = m.get("temperature") != null ? m
				.get("temperature").toString() : "-";
		String humidity = m.get("humidity") != null ? m.get("humidity")
				.toString() : "-";
		Watch w = new Watch();
		w.setHumidity(humidity);
		w.setHeartbeat(pulsecount);
		w.setTemperature(temperature);
		w.setCreateDate(new Date());
		watchService.insertWatch(w);

	}
}
