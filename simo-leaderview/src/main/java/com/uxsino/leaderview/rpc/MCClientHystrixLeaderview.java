package com.uxsino.leaderview.rpc;

import com.uxsino.commons.model.JsonModel;
import org.springframework.stereotype.Service;

@Service
public class MCClientHystrixLeaderview implements MCService {
    @Override
    public JsonModel getMenu(String cookie) {
        return new JsonModel(false, "MC服务调用失败");
    }
}
