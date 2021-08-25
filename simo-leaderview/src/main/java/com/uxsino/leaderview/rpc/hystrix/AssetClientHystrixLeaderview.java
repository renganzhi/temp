package com.uxsino.leaderview.rpc.hystrix;


import com.uxsino.commons.model.JsonModel;
import com.uxsino.leaderview.rpc.AssetService;
import org.springframework.stereotype.Component;

@Component("leaderview-AssetClient-hystrix-leaderview")
public class AssetClientHystrixLeaderview implements AssetService {


    @Override
    public JsonModel alert_report() {
        return new JsonModel(false,"Asset服务调用失败");
    }

    @Override
    public JsonModel alarmMsgByPage(Integer lastNum) {
        return new JsonModel(false,"Asset服务调用失败");
    }

    @Override
    public JsonModel search(String param) {
        return new JsonModel(false,"Asset服务调用失败");
    }
}
