package com.potevio.sdtv.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.potevio.sdtv.dao.WatchDao;
import com.potevio.sdtv.domain.Watch;

@Service
@Transactional
public class WatchService {
	@Autowired
	private WatchDao watchDao;

	public void insertWatch(Watch watch) {
		Watch w = watchDao.save(watch);
		System.out.println(w);

		// watchDao.getSession().save(watch);
	}

	public Watch latestData() {
		// Page<Watch> page = watchDao.findAll(new PageRequest(0, 1,
		// Direction.DESC, "createdate"));
		//
		// Watch w = page.getContent().get(0);
		List<Watch> list = watchDao.findLatestWatch();
		return list.get(0);

	}
}
