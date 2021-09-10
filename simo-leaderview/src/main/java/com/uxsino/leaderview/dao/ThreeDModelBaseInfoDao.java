package com.uxsino.leaderview.dao;

import com.uxsino.commons.db.repository.ICustomRepository;
import com.uxsino.leaderview.entity.ThreeDModelBaseInfo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ThreeDModelBaseInfoDao extends ICustomRepository<ThreeDModelBaseInfo,Long> {

    @Modifying
    @Transactional
    @Query(value = "delete from ThreeDModelBaseInfo where 1=1 and id in :ids")
    void deleteByIdIn(@Param("ids") List<Long> ids);

    Boolean existsBySourceEquals(String source);

    boolean existsById(Long id);

    Boolean existsBySourceAndName(String source, String name);

    Boolean existsByIdAndName(Long source, String name);

    Boolean existsByName(String name);

    List<ThreeDModelBaseInfo> getByModelType(String modelType);

    List<ThreeDModelBaseInfo> getByModelTypeIn(List<String> modelTypes);

    @Query(value = "from ThreeDModelBaseInfo where id in ?1")
    List<ThreeDModelBaseInfo> getByIds(List<Long> ids);

    List<ThreeDModelBaseInfo> findByModelTypeAndName(String modelType, String name);

}
