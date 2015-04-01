package com.potevio.sdtv.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.potevio.sdtv.dao.LbsDao;
import com.potevio.sdtv.dao.WatchDao;

@Service
@Transactional
public class LbsService {
	@Autowired
	private LbsDao lbsDao;
}
