package com.uxsino.leaderview.handler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.uxsino.leaderview.dao.IHomePageUserConfDao;
import com.uxsino.leaderview.entity.HomePage;
import com.uxsino.leaderview.entity.HomePageUserConf;
import com.uxsino.leaderview.service.HomePageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: hxd
 * @Date: 2021/9/27 11:41
 * @Description: 初始化新建用户的大屏配置
 * @Version: 1.0
 */
@Component
public class UserDataHandler {
    private static Logger logger = LoggerFactory.getLogger(UserDataHandler.class);

    @Autowired
    private HomePageService pageService;

    @Autowired
    private IHomePageUserConfDao homePageUserConfDao;

    // 用户信息变更
    public void handle(JSONObject user) {
        JSONObject users = user.getJSONObject("users");
        JSONObject userRoles = user.getJSONObject("userRoles");
        if(users == null || users.getJSONArray("add") == null){
            return;
        }
        JSONObject addUser = users.getJSONArray("add").getJSONObject(0);
        Long id= addUser.getLong("id");
        JSONArray addRoles = userRoles.getJSONArray("add");
        Set<Long> roleIds = new HashSet();
        for (int i = 0; i < addRoles.size(); i++) {
            JSONObject role = addRoles.getJSONObject(i);
            if(id.equals(role.getLong("userId"))){
                roleIds.add(role.getLong("roleId"));
            }
        }
        Set<Long> userIds = new HashSet<>();
        Map<Long, List<Object>> map = addRoles.stream().collect(Collectors.groupingBy(role -> ((JSONObject) role).getLong("roleId")));
        for (Long roleId : roleIds) {
            List<Object> list = map.get(roleId);
            for (int i = 0; i < list.size(); i++) {
                JSONObject role = (JSONObject)list.get(i);
                userIds.add(role.getLong("userId"));
            }
        }
        List<HomePage> homePages = pageService.findByUserIds(new ArrayList<>(userIds));
        for (HomePage page : homePages) {
            HomePageUserConf pageUserConf = new HomePageUserConf();
            pageUserConf.setUserId(id);
            pageUserConf.setPageId(page.getId());
            pageUserConf.setVisible(false);
            pageUserConf.setShared(true);
            pageUserConf.setPageIndex(homePageUserConfDao.countByUserId(id) + 1);
            homePageUserConfDao.save(pageUserConf);
        }
    }


}
