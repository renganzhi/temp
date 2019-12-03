package com.uxsino.leaderview.handler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;



@Component
public class LeaderViewAuthorityHandler {

    private static Logger logger = LoggerFactory.getLogger(LeaderViewAuthorityHandler.class);

    // 域信息变更广播
    public void handle(JSONArray arr){
        logger.info("域信息变更");
    }

    // 资源域变更广播
    public void handleDomainChange(JSONObject obj){
        logger.info("资源域变更");
    }

}
