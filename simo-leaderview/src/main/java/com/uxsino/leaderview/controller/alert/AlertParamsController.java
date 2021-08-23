package com.uxsino.leaderview.controller.alert;

import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.uxsino.commons.model.JsonModel;
import com.uxsino.leaderview.model.AlertType;
import com.uxsino.watcher.lib.annoation.Business;
import com.uxsino.watcher.lib.enums.BusinessConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Api(tags = { "告警-大屏展示数据项接口" })
@RestController
@RequestMapping("/alert/params")
@Business(name = BusinessConstants.ALERT)
public class AlertParamsController {
    /**
     * 获取所有的告警类型下拉框
     * @return
     */
    @GetMapping("/getType")
    @ApiOperation("获取所有的告警类型下拉框")
    public JsonModel getType() {
        List<Map<String, String>> list = new ArrayList<>();
        List<AlertType> alertTypes = Lists.newArrayList(AlertType.values());
        for (AlertType a: alertTypes) {
            if (a.equals(AlertType.TerminalAlert)) continue;
            Map<String,String> map = Maps.newHashMap();
            map.put("name",a.getText());
            map.put("value",a.toString());
            list.add(map);
        }
        Map<String ,String > map = Maps.newHashMap();
        map.put("name","虚拟化告警");
        map.put("value","virtualization");
        list.add(map);
        map = Maps.newHashMap();
        map.put("name","云监控告警");
        map.put("value","cloud");
        list.add(map);
        map = Maps.newHashMap();
        map.put("name","视频监控告警");
        map.put("value","video");
        list.add(map);
        return new JsonModel(true, list);
    }

    @ApiOperation("其他告警类型，列筛选")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", paramType = "query", dataType = "String", value = "告警类型")
    })
    @RequestMapping(value = "/otherAlertColumn", method = RequestMethod.GET)
    public JsonModel otherAlertColumn(@RequestParam String type) {
        List<String > columns;
        if (!"ThirdPartyAlert".equals(type) && !"BusinessAlert".equals(type) && !"ThirdPartyAlert".equals(type)
                && !"SystemAlert".equals(type) && !"TerminalAlert".equals(type)) {
            if ("NELinkAlert".equals(type)) {
                columns = Lists.newArrayList("状态","告警来源","告警内容","告警时间");
            } else {
                columns = Lists.newArrayList("状态","IP地址","告警内容","告警时间");
            }
        }else {
            columns = Lists.newArrayList("状态","告警内容","告警时间");
        }
        JSONArray result = new JSONArray();
        columns.forEach(column -> {
            Map<String ,String > map = Maps.newHashMap();
            map.put("name", column);
            map.put("value", column);
            result.add(map);
        });
        return new JsonModel(true, result);
    }

}
