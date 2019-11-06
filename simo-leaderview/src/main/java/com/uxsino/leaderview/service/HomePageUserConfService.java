package com.uxsino.leaderview.service;

import com.uxsino.leaderview.dao.IHomePageUserConfDao;
import com.uxsino.leaderview.entity.HomePage;
import com.uxsino.leaderview.entity.HomePageUserConf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Service
public class HomePageUserConfService {
    @Autowired
    private IHomePageUserConfDao homePageUserConfDao;

    public HomePageUserConf findOneByIndexAndUserId(int pageIndex, Long userId){
        return homePageUserConfDao.findOneByPageIndexAndUserId(pageIndex, userId);
    }

    public List<HomePageUserConf> findAll(){
        return homePageUserConfDao.findAll();
    }

    public List<HomePageUserConf> findByPageId(Long pageId){
        return homePageUserConfDao.findByPageId(pageId);
    }

    public void save(List<HomePageUserConf> list){
        homePageUserConfDao.saveAll(list);
    }

    public void update(Long id ,HomePageUserConf homePageUserConf){
        homePageUserConfDao.update(id,homePageUserConf);
    }

    public void delete(HomePageUserConf homePageUserConf){
        homePageUserConfDao.delete(homePageUserConf);
    }

    @Transactional
    public void add(HomePageUserConf homePageUserConf, boolean isBegin) {
        int countByUser = homePageUserConfDao.countByUserId(homePageUserConf.getUserId()) + 1;
        //新增的页面自动设为首位，其他的页面自动向后移一位
        if (isBegin){
            homePageUserConfDao.rightPageIndex(1, countByUser, homePageUserConf.getUserId());
            homePageUserConf.setPageIndex(1);
        }
        // 复制和分享的页面自动设为末位
        else {
            homePageUserConf.setPageIndex(countByUser);
        }
        homePageUserConfDao.save(homePageUserConf);
    }

    @Transactional
    public void addShare(Long userId, HomePage homePage) {
        HomePageUserConf homePageUserConf = new HomePageUserConf();
        homePageUserConf.setPageId(homePage.getId());
        homePageUserConf.setUserId(userId);
        //添加配置前进行判断，查看该用户是否已经被分享了当前页面
        HomePageUserConf exist = homePageUserConfDao.findByUserIdAndPageId(userId,homePage.getId());
        if (ObjectUtils.isEmpty(exist)){
            homePageUserConf.setPageIndex(homePageUserConfDao.countByUserId(userId) + 1);
            homePageUserConfDao.save(homePageUserConf);
        }
    }

    @Transactional
    public void move(HomePageUserConf homePageUserConf, boolean back, Long userId) {
        int index = homePageUserConf.getPageIndex();
        if (index == 0) {
            return;
        }
        if (back) {
            index = index - 1;
        } else {
            index = index + 1;
        }
        HomePageUserConf otherPage = findOneByIndexAndUserId(index, userId);
        if (otherPage != null) {
            otherPage.setPageIndex(homePageUserConf.getPageIndex());
            homePageUserConfDao.update(otherPage.getId(), otherPage);
        }
        homePageUserConf.setPageIndex(index);
        homePageUserConfDao.update(homePageUserConf.getId(), homePageUserConf);
    }

    @Transactional
    public void moveAll(HomePageUserConf homePageUserConf, boolean back, Long userId) {
        int index = homePageUserConf.getPageIndex();
        if (index == 0) {
            return;
        }
        if (back) {
            if (index == 1) {
                return;
            }
            homePageUserConfDao.rightPageIndex(1, index - 1, homePageUserConf.getUserId());
            homePageUserConf.setPageIndex(1);
        } else {
            int maxIndex = homePageUserConfDao.countByUserId(homePageUserConf.getUserId());
            if (maxIndex == index) {
                return;
            }
            homePageUserConfDao.leftPageIndex(index + 1, maxIndex, homePageUserConf.getUserId());
            homePageUserConf.setPageIndex(maxIndex);
        }
        homePageUserConfDao.update(homePageUserConf.getId(), homePageUserConf);
    }

    public void leftPageIndex(int startIndex, int endIndex, Long userId){
        homePageUserConfDao.leftPageIndex(startIndex, endIndex, userId);
    }

    public int getMaxPageByUserId(Long userId){
        return homePageUserConfDao.countByUserId(userId);
    }
}
