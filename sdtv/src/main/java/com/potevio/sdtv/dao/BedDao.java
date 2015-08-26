package com.potevio.sdtv.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.potevio.sdtv.domain.BedData;

public interface BedDao extends CrudRepository<BedData, Integer> {
	List<BedData> findByOccurTimeGreaterThan(Date date);
}
