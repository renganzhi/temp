package com.uxsino.leaderview.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.uxsino.leaderview.dao.IHomePageDao;
import com.uxsino.leaderview.dao.IHomePageUserConfDao;
import com.uxsino.leaderview.entity.HomePage;
import com.uxsino.leaderview.entity.HomePageUserConf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class AuthorityService {

    private static Logger log = LoggerFactory.getLogger(AuthorityService.class);

    @Autowired
    private IHomePageDao homePageDao;

    @Autowired
    private IHomePageUserConfDao homePageUserConfDao;

    /**
     * 用户管理工作移交，将被移交者的页面和分享者修改
     * @param msg 移交广播
     */
    @Transactional
    public void handOver(String msg){
        JSONArray arr = JSON.parseArray(msg);
        if (!ObjectUtils.isEmpty(arr)){
            for (int i = 0; i < arr.size(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                if (!ObjectUtils.isEmpty(obj)){
                    Long handoverId = obj.getLong("handoverId");
                    Long uid = obj.getLong("uid");
                    List<HomePage> pageList = homePageDao.findByUserId(uid);
                    List<HomePageUserConf> confList = homePageUserConfDao.findByUserIdAndShared(uid, false);
                    int count = homePageUserConfDao.countByUserIdAndVisible(handoverId, true);
                    for (HomePage homePage : pageList) {
                        homePage.setHandoverId(handoverId);
                        homePageDao.update(homePage.getId(), homePage);
                    }
                    boolean isUpdate = false;
                    for (HomePageUserConf conf : confList) {
                        HomePageUserConf oldConf = homePageUserConfDao.findByUserIdAndPageId(handoverId, conf.getPageId());
                        if(oldConf != null){
                            conf = oldConf;
                            isUpdate = true;
                        }else {
                            homePageUserConfDao.rightPageIndex(count, homePageUserConfDao.countByUserId(handoverId), handoverId);
                            conf.setPageIndex(count);
                        }
                        conf.setVisible(false);
                        conf.setShared(false);
                        conf.setUserId(handoverId);
                        if(isUpdate){
                            homePageUserConfDao.update(conf.getId(),conf);
                        }else {
                            homePageUserConfDao.save(conf);
                        }
                        count++;
                    }
                }
            }
        }
    }

}
