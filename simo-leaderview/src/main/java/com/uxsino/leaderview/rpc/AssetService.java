package com.uxsino.leaderview.rpc;


import com.uxsino.commons.model.JsonModel;
import com.uxsino.leaderview.rpc.hystrix.AssetClientHystrixLeaderview;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "${service-asset}", fallback = AssetClientHystrixLeaderview.class)
public interface AssetService {

    @RequestMapping(method = RequestMethod.GET,value = "/apis/report/alertcountup",consumes = "application/json")
    JsonModel alert_report();

    @RequestMapping(method = RequestMethod.GET,value = "/apis/report/alarmMsgByPage",consumes = "application/json")
    JsonModel  alarmMsgByPage(@RequestParam("lastNum") Integer lastNum);

    @RequestMapping( method = RequestMethod.POST,value = "/apis/view",consumes = "application/json")
    JsonModel  search(@RequestBody String param);


}
