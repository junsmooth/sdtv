package com.potevio.sdtv.device.hiyo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.potevio.sdtv.util.MD5Util;

@Controller
@RequestMapping(value = "hiyo")
public class HiyoController {
	private static final Logger logger = LoggerFactory
			.getLogger(HiyoController.class);
	public static final String apikey = "e121a422cd24c55005ea81b0ade9d7f9";
	public static final String secretkey = "f3c3095bd0ba39868a3de6a901021e24";
	public static final String baseURL = "http://api.hi-yo.com:8080/app/";

	@RequestMapping(value = "bind")
	public @ResponseBody Object bind(@RequestParam String dids,
			HttpServletRequest req, HttpServletResponse rsp) {
		logger.info("into bind: dids=" + dids);
		try {
			long tm = System.currentTimeMillis()/1000;
			String contentUrl = baseURL + "BindDeviceID" + "?apikey=" + apikey
					+ "&dids=" + dids + "&tm=" + tm + "&sign="
					+ MD5Util.string2MD5(apikey + dids + tm + secretkey);
			logger.info("URL:"+contentUrl);
			String obj = Request.Post(contentUrl).execute().returnContent()
					.asString();
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}

	}
	@RequestMapping(value="data")
	public @ResponseBody Object data(){
		
		String dids="8020114B59030000000062";
		try {
			long tm = System.currentTimeMillis()/1000;
			String contentUrl = baseURL + "GetSleepData" + "?apikey=" + apikey
					+ "&did=" + dids + "&tm=" + tm + "&sign="
					+ MD5Util.string2MD5(apikey + dids + tm + secretkey);
			logger.info("URL:"+contentUrl);
			String obj = Request.Post(contentUrl).execute().returnContent()
					.asString();
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		
		
	}

	@RequestMapping(value = "status")
	public @ResponseBody Object bindStatus(@RequestParam String dids) {
		long tm = System.currentTimeMillis()/1000;
		String contentUrl = baseURL + "CheckBindStatus" + "?apikey=" + apikey
				+ "&dids="+dids+"&tm=" + tm + "&sign="
				+ MD5Util.string2MD5(apikey + tm + secretkey);
		try {
			logger.info("URL:"+contentUrl);
			String obj = Request.Post(contentUrl).execute().returnContent()
					.asString();
			logger.info("status result:" + obj);
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
}
