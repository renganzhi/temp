package com.uxsino.leaderview.dao;

import com.uxsino.commons.db.repository.ICustomRepository;
import com.uxsino.leaderview.entity.HomeTemplateImg;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IHomeTemplateImgDao extends ICustomRepository<HomeTemplateImg, Long> {

    @Query("select max(id) from HomeTemplateImg ")
    Long getMaxId();

    List<HomeTemplateImg> getByIdIn(List<Long> ids);

    List<HomeTemplateImg> findByName(String name);
}
