package com.uxsino.leaderview.dao;

import com.uxsino.commons.db.repository.ICustomRepository;
import com.uxsino.leaderview.entity.WuhouHomestay;

import java.util.List;

public interface IWuhouHomestayDao extends ICustomRepository<WuhouHomestay, Long> {
    List<WuhouHomestay> findByPlaceType(Integer type);

    List<WuhouHomestay> findByAddress(String address);
}
