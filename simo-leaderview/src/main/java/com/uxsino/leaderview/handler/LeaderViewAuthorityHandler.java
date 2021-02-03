package com.uxsino.leaderview.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.uxsino.authority.lib.util.DomainUtils;
import com.uxsino.commons.model.JsonModel;
import com.uxsino.leaderview.entity.HomePage;
import com.uxsino.leaderview.rpc.MonitorService;
import com.uxsino.leaderview.service.HomePageService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LeaderViewAuthorityHandler {

    private static Logger logger = LoggerFactory.getLogger(LeaderViewAuthorityHandler.class);

    @Autowired
    private HomePageService homePageService;

    @Autowired
    private MonitorService monitorService;

    @Autowired
    private DomainUtils domainUtils;


    // 域信息变更广播
    public void handle(JSONArray array) {
        logger.info("域信息变更");
        //大屏部件所属资源进行域权限判断,不属于权限内的进行移除
        if (array == null || array.isEmpty()) {
            return;
        }
        array.forEach(obj -> {
            JSONObject json = JSONObject.parseObject(JSON.toJSONString(obj));
            Long userId = json.getLongValue("uid");
            JSONArray domainArray = json.getJSONArray("domainIds");
            if (userId == null || domainArray == null || domainArray.isEmpty()) {
                return;
            }
            List<Long> domainIds = domainArray.stream().map(domainId -> Long.valueOf(String.valueOf(domainId)))
                    .collect(Collectors.toList());
            List<HomePage> homePages = homePageService.findByUserId(userId);
            //如果更改权限的用户并没有大屏页面，则大屏信息不用做处理
            if (homePages == null || homePages.isEmpty()) {
                return;
            }
            //获取用户所拥有的资源
            JsonModel nes = monitorService.findAllByDomainIdIn(domainIds);
            List<String> neIdsInDomains = (List<String>) nes.getObj();
            if (homePages != null && !homePages.isEmpty()) {
                for (HomePage homePage : homePages) {
                    JSONArray hp = new JSONArray();
                    String viewConf = homePage.getViewConf();
                    JSONArray vcArr = viewConf != null ? JSONArray.parseArray(viewConf) : null;
                    if (vcArr != null) {
                        vcArr.forEach(vc -> {
                            JSONObject vcObj = JSONObject.parseObject(JSON.toJSONString(vc));
                            if (vcObj.containsKey("params") && vcObj.get("params") != null && vcObj
                                    .getJSONObject("params").containsKey("neIds") && StringUtils
                                    .isNotBlank(vcObj.getJSONObject("params").getString("neIds"))) {
                                String neIds = vcObj.getJSONObject("params").getString("neIds");
                                List<String> neIdList = Arrays.asList(neIds.split(","));
                                //过滤掉不属于用户的资源
                                neIdList = neIdList.stream()
                                        .filter(neId -> StringUtils.isNotBlank(neId) && (neIdsInDomains != null
                                                && neIdsInDomains.contains(neId))).collect(Collectors.toList());
                                vcObj.getJSONObject("params").put("neIds", StringUtils.join(neIdList, ","));
                            }
                            hp.add(vcObj);
                        });
                        homePage.setViewConf(hp.toJSONString());
                    }
                }
                //批量更新大屏
                homePageService.saveAll(homePages);
            }
        });
    }

    // 资源域变更广播
    public void handleDomainChange(JSONObject obj){
        logger.info("资源域变更");
        //大屏部件所属资源进行域权限判断,不属于权限内的进行移除
        String domainId = obj.getString("domainId");
        String neIds = obj.getString("neIds");
        if(StringUtils.isBlank(domainId)||StringUtils.isBlank(neIds)){
            return;
        }
        List<HomePage> homePages = homePageService.findAll();
        homePages.forEach(h->{
            JSONArray hp = new JSONArray();
            Long userId = h.getUserId();
            List<Long> userDomainIds = domainUtils.getUserDomainIds(userId);
            //如果当前用户没有更变的域权限，需要对该用户的大屏资源更新
            if(!userDomainIds.contains(domainId)){
                String viewConf = h.getViewConf();
                JSONArray vcArr = viewConf != null ? JSONArray.parseArray(viewConf) : null;
                if(vcArr==null){
                    return;
                }
                vcArr.forEach(vc->{
                    JSONObject vcObj = JSONObject.parseObject(JSON.toJSONString(vc));
                    if (vcObj.containsKey("params") && vcObj.get("params") != null && vcObj.getJSONObject("params")
                            .containsKey("neIds") && StringUtils
                            .isNotBlank(vcObj.getJSONObject("params").getString("neIds"))) {
                        String neIdInLV = vcObj.getJSONObject("params").getString("neIds");
                        List<String> neIdList = Arrays.asList(neIdInLV.split(","));
                        //过滤当前用户没有权限的资源
                        neIdList=neIdList.stream().filter(ne->!neIds.contains(ne)).collect(Collectors.toList());
                        vcObj.getJSONObject("params").put("neIds",StringUtils.join(neIdList,","));
                    }
                    hp.add(vcObj);
                });
                h.setViewConf(hp.toJSONString());
            }
        });
        //批量更新大屏资源
        homePageService.saveAll(homePages);
    }

}
