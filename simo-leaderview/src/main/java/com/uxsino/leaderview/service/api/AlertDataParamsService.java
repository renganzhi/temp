package com.uxsino.leaderview.service.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.primitives.Longs;
import com.uxsino.authority.lib.model.DomainInfo;
import com.uxsino.authority.lib.util.DomainUtils;
import com.uxsino.commons.model.JsonModel;
import com.uxsino.commons.model.NeClass;
import com.uxsino.commons.utils.Dates;
import com.uxsino.commons.utils.SessionUtils;
import com.uxsino.leaderview.model.AlertType;
import com.uxsino.leaderview.model.alert.*;
import com.uxsino.leaderview.model.monitor.NetworkEntity;
import com.uxsino.leaderview.model.monitor.NetworkEntityCriteria;
import com.uxsino.leaderview.rpc.MCService;
import com.uxsino.leaderview.rpc.MonitorService;
import com.uxsino.leaderview.utils.MonitorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.uxsino.leaderview.utils.MonitorUtils.empObj;

@Component
public class AlertDataParamsService {

    @Autowired
    RpcProcessService rpcProcessService;

    public JSONArray getStatus(String neId) {
        JSONArray result = new JSONArray();
        if (StringUtils.isEmpty(neId)) {
            AlertHandleStatus[] values = AlertHandleStatus.values();
            for (AlertHandleStatus handleStatus : values) {
                JSONObject map = new JSONObject();
                map.put("name", handleStatus);
                map.put("value", handleStatus.getText());
                result.add(map);
            }
        } else {
            AlertQuery query = new AlertQuery();
            query.setObjectIds(neId);
            query.setAlertType(AlertType.Alert);
            List<AlertRecord> alertRecords = null;
            try {
                alertRecords = rpcProcessService.findAlert(query, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (CollectionUtils.isEmpty(alertRecords)) {
                return result;
            }
            Set<AlertHandleStatus> collect = alertRecords.stream().map(ar -> ar.getHandleStatus())
                    .collect(Collectors.toSet());
            result = collect.stream().map(handleStatus -> {
                JSONObject map = new JSONObject();
                map.put("name", handleStatus);
                map.put("value", handleStatus.getText());
                return map;
            }).collect(Collectors.toCollection(JSONArray::new));
        }
        return result;
    }
}
