package com.potevio.sdtv.service.ythtjr;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.potevio.sdtv.dao.BedDao;
import com.potevio.sdtv.domain.Bed;

@Service
public class BedService {
	@Autowired
	private BedDao bedDao;

	public Bed latestBedData() {
		List<Bed> bedList = (List<Bed>) bedDao.findAll();
		if (bedList.size() > 0) {
			return bedList.get(0);
		}
		return null;
	}
}
