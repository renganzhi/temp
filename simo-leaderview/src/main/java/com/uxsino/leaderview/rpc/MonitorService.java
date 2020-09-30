package com.uxsino.leaderview.rpc;

import com.uxsino.commons.model.JsonModel;
import com.uxsino.leaderview.rpc.hystrix.MonitorClientHystrixLeaderview;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @description 调用SIMO-Monitoring的服务类
 * @date 2020年09月29日
 */
@FeignClient(value = "${service-monitoring}", fallback = MonitorClientHystrixLeaderview.class)
public interface MonitorService {
    @RequestMapping(method = RequestMethod.GET, value = "/homeData/params/findAllByDomainIdIn", consumes = "application/json")
    JsonModel findAllByDomainIdIn(@RequestParam(value = "domainIds") List<Long> domainIds);
}
