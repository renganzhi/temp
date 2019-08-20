package com.uxsino.leaderview.service;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uxsino.leaderview.dao.IHomePageDao;
import com.uxsino.leaderview.entity.HomePage;

@Service
public class HomePageService {

    @Autowired
    private IHomePageDao homePageDao;

    @Transactional
    public void add(HomePage homePage) {
        homePage.setLastUpdateTime(new Date());
        homePageDao.rightPageIndex(homePage.getPageIndex(), getMaxIndex());
        homePageDao.save(homePage);
    }

    /**
     * 保存大屏页面配置
     * 
     * @param homePage
     */
    @Transactional
    public void save(HomePage homePage) {
        homePage.setLastUpdateTime(new Date());
        homePageDao.save(homePage);
    }

    @Transactional
    public void update(HomePage homePage) {
        homePage.setLastUpdateTime(new Date());
        homePageDao.update(homePage.getId(), homePage);
    }

    @Transactional
    public void move(HomePage homePage, boolean back) {
        int index = homePage.getPageIndex();
        if (index == 0) {
            return;
        }
        if (back) {
            index = index - 1;
        } else {
            index = index + 1;
        }
        HomePage otherPage = homePageDao.getByPageIndex(index);
        if (otherPage != null) {
            otherPage.setPageIndex(homePage.getPageIndex());
            homePageDao.update(otherPage.getId(), otherPage);
        }
        homePage.setPageIndex(index);
        homePageDao.update(homePage.getId(), homePage);
    }

    @Transactional
    public void moveAll(HomePage homePage, boolean back) {
        int index = homePage.getPageIndex();
        if (index == 0) {
            return;
        }
        if (back) {
            if (index == 1) {
                return;
            }
            homePageDao.rightPageIndex(1, index - 1);
            homePage.setPageIndex(1);
        } else {
            int maxIndex = getMaxIndex();
            if (maxIndex == index) {
                return;
            }
            homePageDao.leftPageIndex(index + 1, maxIndex);
            homePage.setPageIndex(maxIndex);
        }
        homePageDao.update(homePage.getId(), homePage);
    }

    public int getMaxIndex() {
        Integer maxIndex = homePageDao.getMaxPageIndex();
        return maxIndex == null ? 0 : maxIndex;
    }

    public HomePage getById(Long id) {
        return homePageDao.getOne(id);
    }

    public HomePage getByIndex(int pageIndex) {
        return homePageDao.getByPageIndex(pageIndex);
    }

    /**
     * 过滤掉pageIndex<=0的信息
     */
    public List<HomePage> findAll() {
        return homePageDao.findAllOrderly();
    }

    public List<HomePage> findAllWithoutConf() {
        return homePageDao.findAllWithoutConfOrderly();
    }

    /**
     * 获取所有页面，排除掉页面视图配置和缩略图
     */
    public List<HomePage> findNoView() {
        return homePageDao.findNoView();
    }

    /**
     * 查询可参与轮播的主页，即visible = true
     * 排除掉viewImage字段
     */
    public List<HomePage> findVisible() {
        List<HomePage> pages = homePageDao.findByVisible(true);
        pages.forEach(e -> e.setViewImage(null));
        return pages;
    }

    /**
     * 查询指定用户的首页配置信息
     * 
     * @param employeeId
     * @return
     */
    public List<HomePage> findByUserId(long employeeId) {
        return homePageDao.findByUserId(employeeId);
    }

    /**
     * 查询指定角色的首页配置信息
     * 
     * @param userIds
     * @return
     */
    public List<HomePage> findByRoleIds(String userIds) {
        return homePageDao.findByRoleIds(userIds);
    }

    /**
     * 查询指定用户的首页指定序号配置信息
     * 
     * @param employeeId
     * @param index
     * @return
     */
    public HomePage getByUserIdAndIndex(long employeeId, int index) {
        return homePageDao.getByUserIdAndPageIndex(employeeId, index);
    }

    /**
     * 查询指定角色的首页指定序号配置信息
     * 
     * @param pageIndex
     * @return
     */
    public HomePage getByRoleIdsAndPageIndex(String roleIds, int pageIndex) {
        return homePageDao.getByRoleIdsAndPageIndex(roleIds, pageIndex);
    }

    /**
     * 先将后续page_index往前移一位再删除该主页
     * @param page 待删除主页
     */
    @Transactional
    public void delete(HomePage page) {
        homePageDao.leftPageIndex(page.getPageIndex() + 1, getMaxIndex());
        homePageDao.delete(page);
    }

}
