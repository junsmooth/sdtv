package com.potevio.sdtv.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.potevio.sdtv.device.ythtjr.BedService;
import com.potevio.sdtv.domain.BedData;
import com.potevio.sdtv.util.CacheUtil;

@Controller
@RequestMapping(value = "bed")
public class BedController {
	private static Logger logger = LoggerFactory.getLogger(BedController.class);
	@Autowired
	private BedService bedService;

	@RequestMapping("latest")
	public @ResponseBody Object latestBedData() {
//		Bed bed = bedService.latestBedData();
		
		BedData msg = CacheUtil.getBedlatest();
		if(msg==null){
			return "";
		}
		msg.setWarn(0);
		return msg;
//
//		if (bed != null) {
//			int heart = bed.getHeartrating();
//			int res = bed.getResping();
//			int status = bed.getStatus();
//			if (status == 30 || status == 20 || status == 41) {
//				if (heart < 40 || heart > 140 || res < 8 || res > 20) {
//					bed.setWarn(1);
//				}
//			}
//			return bed;
//		}
//		return "";
	}
}
