package com.uxsino.leaderview.dao;

import com.uxsino.commons.db.repository.ICustomRepository;
import com.uxsino.leaderview.entity.TimeData;
import com.uxsino.leaderview.model.DataJob;

public interface ITimeDataDao extends ICustomRepository<TimeData, Long> {

    TimeData findByName(String name);
}
