package com.uxsino.leaderview.rpc;


import com.uxsino.commons.model.JsonModel;
import com.uxsino.leaderview.rpc.hystrix.MCClientHystrixLeaderview;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @description 调用SIMO-MC的服务类
 * 在调用MC服务类的时候，输入流被关闭，无法正常调用，测试环境上无法正常运行
 * @date 2019年10月29日
 */
@FeignClient(value = "${service-mc}", fallback = MCClientHystrixLeaderview.class)
public interface MCService {

    @RequestMapping(method = RequestMethod.POST,value = "mcApi/organization/list",consumes = "application/json")
    JsonModel getOrganList(@RequestBody String params);

    @RequestMapping(method = RequestMethod.GET, value = "mc/getMenu", consumes = "application/json")
    JsonModel getMenu(@RequestHeader("Cookie") String cookie);

    @RequestMapping(method = RequestMethod.GET, value = "mc/role/findAllUserByRoleId", consumes = "application/json")
    JsonModel findAllUserByRoleId(@RequestHeader("Cookie") String cookie, @RequestParam("roleIds") Long[] roleIds);

    @RequestMapping(method = RequestMethod.GET, value = "department/list", consumes = "application/json")
    JsonModel getDomainList(@RequestHeader("Cookie") String cookie);
}
