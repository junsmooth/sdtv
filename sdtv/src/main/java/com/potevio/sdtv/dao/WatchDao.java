package com.potevio.sdtv.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.potevio.sdtv.domain.Watch;

public interface WatchDao extends CrudRepository<Watch, Long> {

	@Query("from Watch w where w.createDate = (select max(createDate) from w)")
	public List<Watch> findLatestWatch();
	
	public Page<Watch> findAll(Pageable page);
}
