package com.uxsino.leaderview.dao;

import java.util.List;
import java.util.Map;

import com.uxsino.leaderview.entity.HomePageVo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.uxsino.commons.db.repository.ICustomRepository;
import com.uxsino.leaderview.entity.HomePage;

public interface IHomePageDao extends ICustomRepository<HomePage, Long> {

    HomePage getByPageIndex(int pageIndex);

    HomePage getByUserIdAndPageIndex(long employeeId, int pageIndex);

    HomePage getByRoleIdsAndPageIndex(String roleIds, int pageIndex);

    @Query("from HomePage  h where h.visible = true  order by h.pageIndex")
    List<HomePage> findByVisible(boolean visible);

    @Query("from HomePage h where h.pageIndex > 0 order by h.pageIndex")
    List<HomePage> findAllOrderly();

    @Query("select new com.uxsino.leaderview.entity.HomePage(id,userId,roleIds,pageIndex,name,visible,lastUpdateTime,viewImage,createUserId,handoverId,shareConf) from HomePage order by pageIndex")
    List<HomePage> findAllWithoutConfOrderly();

    @Query("select new com.uxsino.leaderview.entity.HomePage(id,userId,roleIds,pageIndex,name,visible,lastUpdateTime) from HomePage order by pageIndex")
    List<HomePage> findNoView();

    @Query("select max(pageIndex) from HomePage")
    Integer getMaxPageIndex();

    Integer countByUserId(Long uesrId);

    @Query("from HomePage where userId=?1 order by  pageIndex")
    List<HomePage> findByUserId(long employeeId);

    @Query("from HomePage where roleIds=?1 order by  pageIndex")
    List<HomePage> findByRoleIds(String roleIds);

    @Modifying
    @Query(nativeQuery = true,
        value = "update simo_mc_home_page set page_index=page_index+1 where page_index>=?1 and page_index<=?2")
    void rightPageIndex(int startIndex, int endIndex);

    @Modifying
    @Query(nativeQuery = true,
        value = "update simo_mc_home_page set page_index=page_index-1 where page_index>=?1 and page_index<=?2")
    void leftPageIndex(int startIndex, int endIndex);

}
