package com.uxsino.leaderview.rpc;


import com.uxsino.commons.model.JsonModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @description 调用SIMO-MC的服务类
 * 已经可以正常调用MC中的方法了，但暂时没有需要使用的地方。
 * @date 2019年10月29日
 */
@FeignClient(value = "${service-mc}", fallback = MCClientHystrixLeaderview.class)
public interface MCService {
    @RequestMapping(method = RequestMethod.GET, value = "mc/getMenu", consumes = "application/json")
    JsonModel getMenu(@RequestHeader("Cookie") String cookie);
}
