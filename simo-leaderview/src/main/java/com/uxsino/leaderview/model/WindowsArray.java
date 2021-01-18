package com.uxsino.leaderview.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.uxsino.leaderview.model.monitor.NetworkEntity;
import lombok.Data;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.List;

/**
 * 弹窗数据处理类 对资源 指标 部件 属性 四个类型做处理
 */
@Data
public class WindowsArray {
    //原始弹窗数据
    private JSONArray windowsJsonArray;

    private JSONArray componentArray;
    private JSONArray indicatorArray;
    private String[] componentNames;
    private String[] neIds;
    private HashMap<String, String> componentMap;
    private HashMap<String, NetworkEntity> neMap;

    public WindowsArray(String windows, String[] neIds){
        this.windowsJsonArray = windowBreakUp(JSONArray.parseArray(windows));
        init(neIds);
    }

    public HashMap getNeMap(List<NetworkEntity> nes){
        if (!ObjectUtils.isEmpty(this.neMap)){
            return this.neMap;
        }
        HashMap<String, NetworkEntity> neMap = new HashMap<>();
        nes.forEach(ne -> neMap.put(ne.getId(), ne));
        return this.neMap = neMap;
    }

    private void init(String[] neIds){
        JSONArray componentArray = new JSONArray();
        JSONArray indicatorArr = new JSONArray();
        for (int i = 0; i < windowsJsonArray.size(); i++) {
            componentArray.addAll(windowsJsonArray.getJSONObject(i).getJSONArray("ne"));
            indicatorArr.add(windowsJsonArray.getJSONObject(i).getString("indicator"));
        }
        String[] componentNames = new String[componentArray.size()];
        for (int i = 0; i < componentArray.size(); i++) {
            componentNames[i] = componentArray.getJSONArray(i).getJSONObject(0).getString("component");
        }
        HashMap<String, String> componentMap = new HashMap<>();
        for (int i = 0; i < componentArray.size(); i++) {
            componentNames[i] = componentArray.getJSONObject(i).getString("component");
            neIds[i] = componentArray.getJSONObject(i).getString("id");
            componentMap.put(neIds[i], componentNames[i]);
        }
        this.componentNames = componentNames;
        this.indicatorArray = indicatorArr;
        this.componentArray = componentArray;
        this.componentMap = componentMap;
    }

    /**
     * 将弹窗多选属性数据和多选部件数据进行拆分
     * @param window
     * @return
     */
    private JSONArray windowBreakUp(JSONArray window) {
        JSONArray tmpResult = new JSONArray();
        for (int i = 0; i < window.size(); i++) {
            JSONObject obj = window.getJSONObject(i);
            Object json = obj.get("fields");
            if (json instanceof JSONArray) {
                JSONArray fieldArr = JSONArray.parseArray(json.toString());
                if (ObjectUtils.isEmpty(fieldArr)) {
                    tmpResult.add(obj);
                    continue;
                }
                for (int j = 0; j < fieldArr.size(); j++) {
                    String field = fieldArr.getString(j);
                    JSONObject object = new JSONObject();
                    object.put("indicator", obj.get("indicator"));
                    object.put("ne", obj.get("ne"));
                    object.put("fields", field);
                    tmpResult.add(object);
                }
            } else {
                tmpResult.add(obj);
            }
        }
        JSONArray result = new JSONArray();
        for (int i = 0; i < tmpResult.size(); i++) {
            JSONObject obj = tmpResult.getJSONObject(i);
            JSONArray neArr = obj.getJSONArray("ne");
            for (int j = 0; j < neArr.size(); j++) {
                JSONObject neObj = neArr.getJSONObject(j);
                if ("false".equals(neObj.getString("multipleComponent"))) {
                    break;
                } else {
                    Object json = neObj.get("component");
                    if (json instanceof JSONArray) {
                        JSONArray compArr = neObj.getJSONArray("component");
                        if (ObjectUtils.isEmpty(compArr)) {
                            continue;
                        }
                        for (int k = 0; k < compArr.size(); k++) {
                            JSONObject object = new JSONObject();
                            JSONObject newNeObj = new JSONObject();
                            newNeObj.put("component", compArr.getString(k));
                            newNeObj.put("id", neObj.get("id"));
                            newNeObj.put("multipleComponent", neObj.get("multipleComponent"));
                            object.put("indicator", obj.get("indicator"));
                            object.put("ne", newNeObj);
                            object.put("fields", obj.get("fields"));
                            result.add(object);
                        }
                    } else {
                        JSONObject object = new JSONObject();
                        JSONObject newNeObj = new JSONObject();
                        newNeObj.put("component", json);
                        newNeObj.put("id", neObj.get("id"));
                        newNeObj.put("multipleComponent", neObj.get("multipleComponent"));
                        object.put("indicator", obj.get("indicator"));
                        object.put("ne", newNeObj);
                        object.put("fields", obj.get("fields"));
                        result.add(object);
                    }
                }
            }
        }
        result = ObjectUtils.isEmpty(result) ? tmpResult : result;
        return result;
    }
}
