package com.potevio.sdtv.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.potevio.sdtv.dao.BedDataDao;
import com.potevio.sdtv.device.ythtjr.android.BedData;

@Service
@Transactional
public class BedDataService {
	@Autowired
	private BedDataDao dao;

	public void insert(BedData data) {
		dao.save(data);
	}
	
	

	public List<BedData> findAll(Date date) {
		return dao.findByDataTimeGreaterThan(date);
	}

}
