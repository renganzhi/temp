package com.uxsino.leaderview.dao;

import com.uxsino.commons.db.repository.ICustomRepository;
import com.uxsino.leaderview.entity.HomeTemplate;

import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IHomeTemplateDao extends ICustomRepository<HomeTemplate, Long> {

    @Query("select new com.uxsino.leaderview.entity.HomeTemplate(id, name, viewImage, lastUpdateTime) from HomeTemplate order by id")
    List<HomeTemplate> noConf();
}
