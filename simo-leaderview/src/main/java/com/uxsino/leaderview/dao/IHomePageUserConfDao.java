package com.uxsino.leaderview.dao;

import com.uxsino.commons.db.repository.ICustomRepository;
import com.uxsino.leaderview.entity.HomePageUserConf;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface IHomePageUserConfDao extends ICustomRepository<HomePageUserConf, Long> {

    @Query("from HomePageUserConf where userId=?1 order by pageIndex")
    List<HomePageUserConf> findByUserId(Long userId);

    int countByUserId(Long uesrId);

    int countByUserIdAndShared(Long userId, boolean shared);

    int countByUserIdAndVisible(Long userId, boolean visible);

    List<HomePageUserConf> findByUserIdAndVisible(Long userId, boolean visible);

    List<HomePageUserConf> findByUserIdAndShared(Long userId, boolean shared);

    List<HomePageUserConf> findByPageId(Long pageId);

    HomePageUserConf findByUserIdAndPageId(Long userId, Long pageId);

    HomePageUserConf findOneByPageIndexAndUserId(int pageIndex, Long userId);

    @Modifying
    @Query(nativeQuery = true,
            value = "update simo_mc_home_page_user_conf set page_index=page_index+1 where user_id = ?3 and page_index>=?1 and page_index<=?2")
    void rightPageIndex(int startIndex, int endIndex, Long userId);

    @Modifying
    @Query(nativeQuery = true,
            value = "update simo_mc_home_page_user_conf set page_index=page_index-1 where user_id = ?3 and page_index>=?1 and page_index<=?2")
    void leftPageIndex(int startIndex, int endIndex, Long userId);

    @Modifying
    @Transactional
    void deleteByPageId(Long pageId);
}
