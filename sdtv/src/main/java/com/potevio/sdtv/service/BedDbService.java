package com.potevio.sdtv.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.potevio.sdtv.dao.BedDao;
import com.potevio.sdtv.domain.BedData;

@Service
@Transactional
public class BedDbService {
	@Autowired
	private BedDao dao;

	public void insert(BedData bed) {
		dao.save(bed);
	}
}
