package com.potevio.sdtv.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.potevio.sdtv.dao.BedDataDao;
import com.potevio.sdtv.dao.BedRawDataDao;
import com.potevio.sdtv.domain.BedData;
import com.potevio.sdtv.domain.BedDataRaw;

@Service
@Transactional
public class BedDataService {
	@Autowired
	private BedRawDataDao rawDao;
	@Autowired
	private BedDataDao dao;

	public void insertData(BedData data) {
		dao.save(data);
	}
	public void insertRawData(BedData data) {
		BedDataRaw raw=new BedDataRaw();
		BeanUtils.copyProperties(data, raw);
		rawDao.save(raw);
	}
	public List<BedData> findAll(Date date) {
		return dao.findByOccurTimeGreaterThan(date);
	}

}
