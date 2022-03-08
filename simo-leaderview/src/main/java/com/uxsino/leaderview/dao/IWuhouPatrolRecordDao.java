package com.uxsino.leaderview.dao;

import com.uxsino.commons.db.repository.ICustomRepository;
import com.uxsino.leaderview.entity.WuhouPatrolRecord;

import java.util.List;

public interface IWuhouPatrolRecordDao extends ICustomRepository<WuhouPatrolRecord, Long> {
    List<WuhouPatrolRecord> findByHotelId(Long hotelId);
}
