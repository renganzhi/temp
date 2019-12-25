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

    public List<HomePageUserConf> findByUserId(Long userId){
        return homePageUserConfDao.findByUserId(userId);
    }

    @Transactional
    public void add(HomePageUserConf homePageUserConf, boolean isNewPage, String[] adminId) {
        Long userId = homePageUserConf.getUserId();
        int countByShared = homePageUserConfDao.countByUserIdAndShared(homePageUserConf.getUserId(), false) + 1;
        int countByUser = homePageUserConfDao.countByUserId(homePageUserConf.getUserId()) + 1;
        //新增的选择显示的页面设为首位，其他的页面自动向后移一位
        if (isNewPage){
            homePageUserConfDao.rightPageIndex(1, countByUser, homePageUserConf.getUserId());
            homePageUserConf.setPageIndex(1);
        }
        // 复制的页面自动设为属于该用户的末位
        else {
            homePageUserConfDao.rightPageIndex(countByShared, countByUser, homePageUserConf.getUserId());
            homePageUserConf.setPageIndex(countByShared);
        }
        homePageUserConfDao.save(homePageUserConf);
        //保存的时候向超级管理员用户中保存一份
        if (ObjectUtils.isEmpty(adminId)){
            adminId = new String[1];
            adminId[0] = "1";
        }
        for (String admin : adminId) {
            if (Long.parseLong(admin) == userId) continue;
            HomePageUserConf otherHomePageUserConf = new HomePageUserConf();
            otherHomePageUserConf.setUserId(Long.parseLong(admin));
            otherHomePageUserConf.setPageId(homePageUserConf.getPageId());
            otherHomePageUserConf.setVisible(false);
            otherHomePageUserConf.setShared(true);
            countByUser = homePageUserConfDao.countByUserId(otherHomePageUserConf.getUserId()) + 1;
            otherHomePageUserConf.setPageIndex(countByUser);
            homePageUserConfDao.save(otherHomePageUserConf);
        }
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
            homePageUserConf.setVisible(false);
            homePageUserConf.setShared(true);
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

    public int getMaxMinePage(Long userId, boolean shared){
        return homePageUserConfDao.countByUserIdAndShared(userId, shared);
    }

    /**
     * 紧急页面排序修复方案
     * @param userId
     */
    @Transactional
    public void RescueConfSort(Long userId) {
        List<HomePageUserConf> confList = findByUserId(userId);
        HomePageUserConf lastConf = confList.get(0);
        for (int i = 0; i < confList.size(); i++) {
            HomePageUserConf conf = confList.get(i);
            int index = conf.getPageIndex();
            int lastIndex = i==0 ? 0 : lastConf.getPageIndex();
            if (index - 1 != lastIndex){
                conf.setPageIndex(i + 1);
                update(conf.getId(),conf);
                //先不进行复杂交换，保证顺序正确就行了
                /*boolean visible = conf.isVisible();
                boolean lastVisible = lastConf.isVisible();
                boolean lastShared = lastConf.isShared();
                // 上一个页面是可见的，不用交换位置
                if (lastVisible){
                    conf.setPageIndex(i + 1);
                    update(conf.getId(),conf);
                }else {
                    // 上一个页面不可见，当前页面可见，需换位置
                    if (visible){
                        conf.setPageIndex(i);
                        update(conf.getId(),conf);
                        lastConf.setPageIndex(i + 1);
                        update(lastConf.getId(),lastConf);
                    }else {
                        // 上一个页面不是被分享的，不用交换位置
                        if (!lastShared){
                            conf.setPageIndex(i + 1);
                            update(conf.getId(),conf);
                        } else {
                            conf.setPageIndex(i);
                            update(conf.getId(),conf);
                            lastConf.setPageIndex(i + 1);
                            update(lastConf.getId(),lastConf);
                        }
                    }
                }*/
            }
            lastConf = conf;
        }
    }
}
