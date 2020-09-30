package com.uxsino.leaderview.rpc.hystrix;

import com.uxsino.commons.model.JsonModel;
import com.uxsino.leaderview.rpc.MonitorService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("leaderview-MonitorClient-hystrix-leaderview")
public class MonitorClientHystrixLeaderview implements MonitorService {

    @Override
    public JsonModel findAllByDomainIdIn(List<Long> domainIds) {
        return new JsonModel(false, "Monitor服务调用失败");
    }
}
