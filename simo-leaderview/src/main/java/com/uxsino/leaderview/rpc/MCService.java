package com.uxsino.leaderview.rpc;


import com.uxsino.commons.model.JsonModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @description 调用SIMO-MC的服务类
 * 在调用MC服务类的时候，输入流被关闭，无法正常调用，测试环境上无法正常运行
 * @date 2019年10月29日
 */
@FeignClient(value = "${service-mc}", fallback = MCClientHystrixLeaderview.class)
public interface MCService {
    @RequestMapping(method = RequestMethod.GET, value = "mc/getMenu", consumes = "application/json")
    JsonModel getMenu(@RequestHeader("Cookie") String cookie);

    @RequestMapping(method = RequestMethod.GET, value = "mc/role/findAllUserByRoleId", consumes = "application/json")
    JsonModel findAllUserByRoleId(@RequestHeader("Cookie") String cookie, @RequestParam("roleIds") Long[] roleIds);
}
