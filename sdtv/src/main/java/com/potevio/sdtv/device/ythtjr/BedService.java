package com.potevio.sdtv.device.ythtjr;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.potevio.sdtv.dao.BedDao;
import com.potevio.sdtv.domain.BedData;
//遗留API，供机顶盒查询床垫最新数据
@Service
public class BedService {
//	@Autowired
//	private BedDao bedDao;

	public BedData latestBedData() {
//		List<Bed> bedList = (List<Bed>) bedDao.findAll();
//		if (bedList.size() > 0) {
//			return bedList.get(0);
//		}
		return null;
	}
}
