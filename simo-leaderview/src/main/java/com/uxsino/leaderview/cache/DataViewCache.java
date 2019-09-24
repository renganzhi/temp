package com.uxsino.leaderview.cache;

import java.util.Iterator;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class DataViewCache {
    public static JSONArray apiList = new JSONArray();

    public static void cacheApi(JSONArray apis) {
        if (apis == null || apis.isEmpty()) {
            return;
        }
        String component = apis.getJSONObject(0).getString("component");
        // 移除此组件原来存在的所有API，重新注册
        if (component != null) {
            Iterator<Object> it = apiList.iterator();
            while (it.hasNext()) {
                JSONObject api = (JSONObject)it.next();
                if (component.equals(api.getString("component"))) {
                    it.remove();
                }
            }
        }
        apiList.addAll(apis);
    }
}
