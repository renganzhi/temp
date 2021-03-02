package com.uxsino.leaderview.rpc.hystrix;

import com.alibaba.fastjson.JSONArray;
import com.uxsino.commons.model.AlertType;
import com.uxsino.commons.model.JsonModel;
import com.uxsino.leaderview.model.alert.AlertLevelQuery;
import com.uxsino.leaderview.model.alert.AlertQuery;
import com.uxsino.leaderview.rpc.AlertService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Map;

@Component("leaderview-AlertClient-hystrix-leaderview")
public class AlertClinetHystrixLeaderview implements AlertService {

    @Override
    public JsonModel findByChooseForLeaderview(String[] neIds, Long number) {
        return new JsonModel(false, "Alert服务调用失败");
    }

    @Override
    public JsonModel getAlertLevels(Map<String, Object> map) {
        return new JsonModel(false, "Alert服务调用失败");
    }

    @Override
    public JsonModel getAlertRecord(AlertQuery alertQuery, Map<String, String> orderBy) {
        return new JsonModel(false, "Alert服务调用失败");
    }

    @Override
    public JsonModel getAlertCount(Map<String, Object> map) {
        return new JsonModel(false, "Alert服务调用失败");
    }

    @Override
    public JsonModel getObjectIdsByAlertType(String cookie, AlertQuery query) {
        return new JsonModel(false, "Alert服务调用失败");
    }

    @Override
    public JsonModel getLevelStatisticsResult(Map<String, Object> map) {
        return new JsonModel(false, "Alert服务调用失败");
    }
}
