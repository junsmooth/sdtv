package com.potevio.sdtv.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.potevio.sdtv.domain.BedData;
import com.potevio.sdtv.domain.BedDataRaw;

public interface BedRawDataDao extends CrudRepository<BedDataRaw, Integer> {
	List<BedData> findByOccurTimeGreaterThan(Date date);
}
