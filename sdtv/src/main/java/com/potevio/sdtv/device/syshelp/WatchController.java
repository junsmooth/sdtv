package com.potevio.sdtv.device.syshelp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.potevio.sdtv.domain.Watch;
import com.potevio.sdtv.service.WatchService;
import com.potevio.sdtv.util.CacheUtil;

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
	public @ResponseBody void putData(@ModelAttribute WatchMSG msg,
			HttpServletRequest req, HttpServletResponse rep) {
		String query = req.getQueryString();
		if (!query.contains("mobile") || !query.contains("datatype")) {
			return;
		}

		try {
			CacheUtil.getSyshelpWatchQueue().put(msg);
			System.out.println(query);
			System.out.println(msg);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// String[] paramArray = query.split("&");
		// Map m = new HashMap();
		// for (String str : paramArray) {
		// if (StringUtils.isNotEmpty(str)) {
		// String[] arr = str.split("=");
		// m.put(arr[0], arr[1]);
		// }
		// }
		// String mobile = m.get("mobile").toString();
		// String datatype = m.get("datatype").toString();
		// msg.setDatatype(datatype);
		//
		// if (!"pulse".equals(datatype)) {
		// return;
		// }
		// String pulsecount = m.get("pulsecount") != null ? m.get("pulsecount")
		// .toString() : "-";
		// String temperature = m.get("temperature") != null ? m
		// .get("temperature").toString() : "-";
		// String humidity = m.get("humidity") != null ? m.get("humidity")
		// .toString() : "-";
		// Watch w = new Watch();
		// w.setHumidity(humidity);
		// w.setHeartbeat(pulsecount);
		// w.setTemperature(temperature);
		// w.setCreateDate(new Date());
		// watchService.insertWatch(w);

	}
}