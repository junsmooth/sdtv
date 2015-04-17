package com.potevio.sdtv.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.potevio.sdtv.device.ythtjr.android.BedData;

public interface BedDataDao extends CrudRepository<BedData, Long> {
	List<BedData> findByDataTimeGreaterThan(Date date);
}
