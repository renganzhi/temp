package com.uxsino.leaderview.service;

import java.util.List;

import com.uxsino.commons.model.JsonModel;
import com.uxsino.leaderview.dao.IHomePageUserConfDao;
import com.uxsino.leaderview.entity.HomePageUserConf;
import com.uxsino.leaderview.entity.HomePageVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uxsino.leaderview.dao.IHomeCarouselDao;
import com.uxsino.leaderview.dao.IHomePageDao;
import com.uxsino.leaderview.entity.HomeCarousel;
import com.uxsino.leaderview.entity.HomePage;

import javax.transaction.Transactional;
@Service
public class HomeCarouselService {

    @Autowired
    private IHomeCarouselDao homeCarouselDao;

    @Autowired
    private IHomePageDao homePageDao;

    @Autowired
    private IHomePageUserConfDao homePageUserConfDao;

    /**
     * 保存大屏展示轮播配置
     * 
     */
    @Transactional
    public JsonModel save(HomeCarousel carouselTime, List<HomePageVo> pages, Long userId) {
        HomeCarousel exist = getByUserId(userId);
        if (exist == null) {
            homeCarouselDao.save(carouselTime);
        } else {
            homeCarouselDao.update(exist.getId(), carouselTime);
        }
        //进行可见页面个数统计
        Integer num = 0;
        for (HomePageVo pageVo : pages) {
            if (pageVo.isVisible()){
                num++;
            }
        }
        if (num > 20){
            return new JsonModel(false, "页面可见数量设置不可大于20个！");
        }
        pages.forEach(page -> {
            HomePageUserConf existPage = homePageUserConfDao.findOne(page.getId());
            if (existPage == null) {
                return;
            }
            existPage.setVisible(page.isVisible());
            existPage.setPageIndex(page.getPageIndex());
            homePageUserConfDao.update(existPage.getId(), existPage);
        });
        //对所有不可见页面进行排序，始终保持[不可见的分享页面]在[不可见的当前用户拥有页面]之后
        List<HomePageUserConf> noVisibleList = homePageUserConfDao.findByUserIdAndVisible(userId, false);
        int visibleCount = homePageUserConfDao.countByUserIdAndVisible(userId, true);
        int countAll = homePageUserConfDao.countByUserId(userId);
        int correction = 1;
        for (HomePageUserConf homePageUserConf : noVisibleList) {
            if (!homePageUserConf.isShared()){
                homePageUserConfDao.rightPageIndex(visibleCount + correction, countAll, userId);
                homePageUserConf.setPageIndex(visibleCount + correction);
                homePageUserConfDao.update(homePageUserConf.getId(), homePageUserConf);
                correction++;
            }
        }
        return new JsonModel(true);
    }

    /**
     * 查询大屏展示轮播配置
     * 
     */
    public HomeCarousel get() {
        List<HomeCarousel> list = homeCarouselDao.findAll();
        if (CollectionUtils.isEmpty(list)) {
            return null;
        } else {
            return list.get(0);
        }
    }

    /**
     * 获得当前用户的大屏轮播配置
     */
    public HomeCarousel getByUserId(Long userId){
        List<HomeCarousel> list = homeCarouselDao.findByUserId(userId);
        if (CollectionUtils.isEmpty(list)){
            return null;
        } else {
            return list.get(0);
        }
    }
}
