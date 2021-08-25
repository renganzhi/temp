package com.uxsino.leaderview.service.api;


import com.uxsino.commons.model.JsonModel;
import com.uxsino.leaderview.model.asset.AssetStatusEnum;
import com.uxsino.leaderview.model.asset.RemindLevelEnum;
import com.uxsino.leaderview.model.asset.RemindTypeEnum;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AssetDataParamsService {
    public JsonModel getAssetStatusEnum() {
        List<Map<String,String>> list = new ArrayList<>();
        for(AssetStatusEnum assetStatus : AssetStatusEnum.values()){
            Map<String,String> map = new HashMap<>(2);
            map.put("name",assetStatus.getText());
            map.put("value",assetStatus.getValue());
            list.add(map);
        }
        return new JsonModel(true,list);
    }

    public JsonModel getRemindTypeEnum(){
        List<Map<String,String>> list = new ArrayList<>();
        for(RemindTypeEnum typeEnum:RemindTypeEnum.values() ){
            Map<String,String> map = new HashMap<>(2);
            map.put("name",typeEnum.getContent());
            map.put("value", typeEnum.getValue());
            list.add(map);
        }
        return new JsonModel(true,list);
    }

    public JsonModel getRemindLevelEnum() {
        List<Map<String,String>> list = new ArrayList<>();
        for (RemindLevelEnum levelEnum:RemindLevelEnum.values()){
            Map<String,String> map = new HashMap<>(2);
            map.put("name",levelEnum.getValue());
            map.put("value", String.valueOf(levelEnum.getLevel()));
            list.add(map);
        }
        return new JsonModel(true,list);
    }
}
