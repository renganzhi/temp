package com.uxsino.leaderview.rpc;

import com.alibaba.fastjson.JSONObject;
import com.uxsino.commons.db.model.PageModel;
import com.uxsino.commons.db.model.network.NeComponentQuery;
import com.uxsino.commons.model.BaseNeClass;
import com.uxsino.commons.model.JsonModel;
import com.uxsino.commons.model.NeClass;
import com.uxsino.leaderview.model.monitor.IndicatorValueQO;
import com.uxsino.leaderview.model.monitor.NetworkEntityCriteria;
import com.uxsino.leaderview.model.monitor.NetworkEntityQO;
import com.uxsino.leaderview.model.monitor.NetworkLinkModel;
import com.uxsino.leaderview.rpc.hystrix.MonitorClientHystrixLeaderview;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @description 调用SIMO-Monitoring的服务类
 * @date 2020年09月29日
 */
@FeignClient(value = "${service-monitoring}", fallback = MonitorClientHystrixLeaderview.class , decode404 = true)
//@FeignClient(value = "${service-monitoring}")
public interface MonitorService {
    @RequestMapping(method = RequestMethod.GET, value = "/homeData/params/networkEntity/findAllByDomainIdIn", consumes = "application/json")
    JsonModel findAllByDomainIdIn(@RequestParam(value = "domainIds") List<Long> domainIds);

    @RequestMapping(method = RequestMethod.POST, value = "/monitorApi/getNeList", consumes = "application/json")
    JsonModel getNeList(@RequestBody String param);

    @RequestMapping(method = RequestMethod.GET, value = "/monitorApi/getUsableInd", consumes = "application/json")
    JsonModel getUsableInd(@RequestParam Map<String,Object> map);

    @RequestMapping(method = RequestMethod.POST, value = "/monitorApi/current/getIndValues", consumes = "application/json")
    JsonModel getCurIndValues(@RequestBody String param);

    @RequestMapping(method = RequestMethod.POST, value = "/monitorApi/history/getIndValues", consumes = "application/json")
    JsonModel getHisIndValues(@RequestBody String param);

    @RequestMapping(method = RequestMethod.GET, value = "/monitorApi/findNeComps", consumes = "application/json")
    JsonModel findNeComps(@RequestParam Map<String, Object> map);

    @RequestMapping(method = RequestMethod.POST, value = "/monitorApi/findNeLinks", consumes = "application/json")
    JsonModel findNeLinks(@RequestParam("pagination") boolean pagination, @RequestBody NetworkLinkModel networkLinkModel);

    @RequestMapping(method = RequestMethod.POST, value = "/monitorApi/statisticsResourceNodes", consumes = "application/json")
    JsonModel statisticsResourceNodes(@RequestParam("topoId") String topoId,
                                      @RequestParam(value = "neClasses",required = false) List<NeClass> neClasses,
                                      @RequestParam(value = "baseNeClasses",required = false) List<BaseNeClass> baseNeClasses);

    @RequestMapping(method = RequestMethod.GET, value = "/monitorApi/findNeHealth", consumes = "application/json")
    JsonModel findNeHealth(@RequestParam("neIds")List<String> neIds,
                           @RequestParam("isHistory") boolean isHistory,
                           @RequestParam("order") String order);

    @RequestMapping(method = RequestMethod.GET, value = "/monitorApi/FindFieldIsMonitoringInStrategy", consumes = "application/json")
    JsonModel FindFieldIsMonitoringInStrategy(@RequestParam("neId") String neId,@RequestParam("indicatorNames") String indicatorNames);

    @RequestMapping(method = RequestMethod.POST, value = "/monitorApi/getIndAggValues", consumes = "application/json")
    JsonModel getIndAggValues(@RequestBody IndicatorValueQO indicatorValueQO);

    @RequestMapping(method = RequestMethod.GET, value = "/monitorApi/getTopoData/{topoId}", consumes = "application/json")
    JsonModel getTopoData(@PathVariable("topoId") String topoId, @RequestParam("mapLocationId") String mapLocationId,@RequestParam("userId") Long userId);

    @RequestMapping(method = RequestMethod.POST, value = "/topo/mapTopoDomain", consumes = "application/json")
    JsonModel getMapNodesAndLinks(@RequestParam("userId") String userId, @RequestParam("mapLocationId") String mapLocationId);

    @RequestMapping(method = RequestMethod.GET, value = "/mapTopo/getMapLocationTree", consumes = "application/json")
    JsonModel getMapLocationTree(@RequestParam("topoId") String topoId);

    @RequestMapping(method = RequestMethod.POST, value = "/ne/search", consumes = "application/json")
    JsonModel searchNe(@RequestBody JSONObject jsonObject);

    @RequestMapping(method = RequestMethod.GET, value = "/hcnet/channelList/{neId}", consumes = "application/json")
    JsonModel getChannelList(@PathVariable("neId") String neId);

    @RequestMapping(method = RequestMethod.POST, value = "/monitorApi/statisticsLinkAlarms", consumes = "application/json")
    JsonModel statisticsLinkAlarms(@RequestParam("topoId") String topoId);

    @RequestMapping(method = RequestMethod.POST, value = "/monitorApi/statisticsEachLevelAlarms", consumes = "application/json")
    JsonModel statisticsEachLevelAlarms(@RequestParam("topoId") String topoId);

    @RequestMapping(method = {RequestMethod.GET}, value = {"/realTime/perf/topEvent"}, consumes = {"application/json"})
    JsonModel topEvent(@RequestParam("neId") String neId, @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime);

    @RequestMapping(method = {RequestMethod.GET}, value = {"/realTime/perf/topSql"}, consumes = {"application/json"})
    JsonModel topSQL(@RequestParam("neId") String neId, @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime);

    @RequestMapping(method = {RequestMethod.GET}, value = {"/realTime/perf/topSession"}, consumes = {"application/json"})
    JsonModel topSession(@RequestParam("neId") String neId, @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime);
}
