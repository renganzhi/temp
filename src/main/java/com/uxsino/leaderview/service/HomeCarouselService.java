package com.uxsino.leaderview.service;

import java.util.List;

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

    /**
     * 保存大屏展示轮播配置
     * 
     */
    @Transactional
    public void save(HomeCarousel carouselTime, List<HomePage> pages) {
        HomeCarousel exist = get();
        if (exist == null) {
            homeCarouselDao.save(carouselTime);
        } else {
            homeCarouselDao.update(exist.getId(), carouselTime);
        }
        pages.forEach(page -> {
            HomePage existPage = homePageDao.findOne(page.getId());
            if (existPage == null) {
                return;
            }
            existPage.setVisible(page.isVisible());
            existPage.setPageIndex(page.getPageIndex());
            homePageDao.update(existPage.getId(), existPage);
        });
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
}
