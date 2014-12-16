package com.potevio.sdtv.device.syshelp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.potevio.sdtv.util.CacheUtil;

@Controller
@RequestMapping(value = "s8")
public class S8Controller {

	private static Logger logger = LoggerFactory.getLogger(S8Controller.class);

	@RequestMapping
	public void s8Data( HttpServletRequest req,
			HttpServletResponse rep) {
		
		String query = req.getQueryString();
		logger.info("S8Query:" + query);
		if (!query.contains("mobile") || !query.contains("datatype")) {
			return;
		}
//		try {
//			CacheUtil.getSyshelpWatchQueue().put(msg);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
}
