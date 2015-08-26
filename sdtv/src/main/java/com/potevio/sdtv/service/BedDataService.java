package com.potevio.sdtv.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.potevio.sdtv.dao.BedDao;
import com.potevio.sdtv.domain.BedData;

@Service
@Transactional
public class BedDataService {
	@Autowired
	private BedDao dao;

	public void insert(BedData data) {
		dao.save(data);
	}

	public List<BedData> findAll(Date date) {
		return dao.findByOccurTimeGreaterThan(date);
	}

}
