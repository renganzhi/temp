package com.uxsino.leaderview.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.uxsino.commons.json.Jsons;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.uxsino.leaderview.cache.DataViewCache;

import java.io.InputStream;
import java.io.InputStreamReader;

@Component
public class HomeDataApiHandler {
    private static Logger logger = LoggerFactory.getLogger(HomeDataApiHandler.class);
    private final String MONITOR_FILE_NAME = "monitor_home_api.json";
    private final String BUSINESS_FILE_NAME = "business_home_api.json";
//    public void register(String str) {
//        JSONArray apis = JSON.parseArray(str);
//        DataViewCache.cacheApi(apis);
//    }
    //上述方法因需要调整为leaderview这边自行管理monitor_home_api.json文件而采用下列方法

    /**
     *
     * @param message 和上面的方法相比，由于只接收monitor服务上线的消息，又由于monitor所读取的
     *                Api字符串是在初始化过程中发送的，因此我们可以认为当这边的register方法被回
     *                调时，就表示monitor微服务启动了，然后就可以通过这个回调方法来读取json文件，
     *                至于那边返回的字符串是什么，这我们并不关心，直接忽略掉即可。
     */
    public void register(String message){
        String apiString = readFile();
        JSONArray api = JSON.parseArray(apiString);
        DataViewCache.cacheApi(api);
    }

    private String readFile(){
        try{
            InputStream monitorInputStream = this.getClass().getClassLoader().getResourceAsStream(MONITOR_FILE_NAME);
            InputStream businessInputStream = this.getClass().getClassLoader().getResourceAsStream(BUSINESS_FILE_NAME);
            if(monitorInputStream == null){
                logger.error("LEADERVIEW -> 文件【{}】未找到！", MONITOR_FILE_NAME);
                return null;
            }
            JSONArray monitorJsonArray;
            JSONArray businessJsonArray;
            JsonReader jsonReader = new JsonReader(new InputStreamReader(monitorInputStream, "UTF-8"));
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement = jsonParser.parse(jsonReader);
            if(jsonElement.isJsonArray()){
                monitorJsonArray = JSON.parseArray(Jsons.of(jsonElement).toJson());
            }else{
                logger.error("LEADERVIEW -> 文件【{}】格式错误！必须是一个数组。", MONITOR_FILE_NAME);
                return null;
            }
            jsonReader = new JsonReader(new InputStreamReader(businessInputStream, "UTF-8"));
            jsonElement = jsonParser.parse(jsonReader);
            if(jsonElement.isJsonArray()){
                businessJsonArray = JSON.parseArray(Jsons.of(jsonElement).toJson());
            }else{
                logger.error("LEADERVIEW -> 文件【{}】格式错误！必须是一个数组。", BUSINESS_FILE_NAME);
                return null;
            }
            return jsonArrayMerge(monitorJsonArray, businessJsonArray);
        }catch (Exception e){
            logger.error("LEADERVIEW -> 文件【{}】和【{}】读取失败！", MONITOR_FILE_NAME, BUSINESS_FILE_NAME);
            return null;
        }
    }

    private String jsonArrayMerge(JSONArray array1, JSONArray array2){
        StringBuffer buffer = new StringBuffer();
        try {
            buffer = addJsonObject(array1, buffer);
            if(array2.size() > 0){
                buffer.append(",");
                buffer = addJsonObject(array2, buffer);
            }
            buffer.insert(0, "[");
            buffer.append("]");
            return buffer.toString();
        }catch (Exception e){
            logger.error("LEADERVIEW -> 文件【{}】和【{}】数组合并异常，请重新检查两个文件！", MONITOR_FILE_NAME, BUSINESS_FILE_NAME);
            return null;
        }
    }

    private StringBuffer addJsonObject(JSONArray jsonArray, StringBuffer buffer){
        int size = jsonArray.size();
        for(int i=0; i<size; i++){
            JSONObject object =(JSONObject)jsonArray.get(i);
            if(i == size-1){
                buffer.append(object.toString());
            }else{
                buffer.append(object.toString()).append(",");
            }
        }

        return buffer;
    }
}
