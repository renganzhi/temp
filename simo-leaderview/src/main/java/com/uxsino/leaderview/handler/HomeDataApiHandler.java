package com.uxsino.leaderview.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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
    private final String FILE_NAME = "monitor_home_api.json";
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
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(FILE_NAME);
            if(inputStream == null){
                logger.error("LEADERVIEW -> 文件【{}】未找到！", FILE_NAME);
                return null;
            }
            JsonReader jsonReader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement = jsonParser.parse(jsonReader);
            if(jsonElement.isJsonArray()){
                return Jsons.of(jsonElement).toJson();
            }else{
                logger.error("LEADERVIEW -> 文件【{}】格式错误！必须是一个数组。", FILE_NAME);
                return null;
            }
        }catch (Exception e){
            logger.error("LEADERVIEW -> 文件【{}】读取失败！", FILE_NAME);
            return null;
        }
    }
}
