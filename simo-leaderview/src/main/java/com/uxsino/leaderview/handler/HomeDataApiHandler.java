package com.uxsino.leaderview.handler;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.uxsino.leaderview.cache.DataViewCache;

@Component
public class HomeDataApiHandler {
    public void register(String str) {
        JSONArray apis = JSON.parseArray(str);
        DataViewCache.cacheApi(apis);
    }
}
