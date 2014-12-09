package com.potevio.sdtv.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.potevio.sdtv.domain.Bed;
import com.potevio.sdtv.service.ythtjr.BedService;

@Controller
@RequestMapping(value = "bed")
public class BedController {

	@Autowired
	private BedService bedService;

	@RequestMapping("latest")
	public @ResponseBody Object latestBedData() {
		Bed bed = bedService.latestBedData();
		if (bed != null) {
			int heart = bed.getHeartrating();
			int res = bed.getResping();
			int status = bed.getStatus();
			if (status == 30 || status == 20 || status == 41) {
				if (heart < 40 || heart > 140 || res < 8 || res > 20) {
					bed.setWarn(1);
				}
			}
			return bed;
		}
		return "";
	}
}
