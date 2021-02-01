package com.uxsino.leaderview.rpc;

import com.alibaba.fastjson.JSONObject;
import com.uxsino.commons.db.model.PageModel;
import com.uxsino.commons.db.model.network.NeComponentQuery;
import com.uxsino.commons.model.BaseNeClass;
import com.uxsino.commons.model.JsonModel;
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
@FeignClient(value = "${service-monitoring}", fallback = MonitorClientHystrixLeaderview.class)
//@FeignClient(value = "${service-monitoring}")
public interface MonitorService {
    @RequestMapping(method = RequestMethod.GET, value = "/homeData/params/networkEntity/findAllByDomainIdIn", consumes = "application/json")
    JsonModel findAllByDomainIdIn(@RequestParam(value = "domainIds") List<Long> domainIds);

    @RequestMapping(method = RequestMethod.POST, value = "/homeData/params/networkEntity/findAllNetworkEntity", consumes = "application/json")
    JsonModel findAllNetworkEntity(@RequestBody NetworkEntityQO entityQO);

    @RequestMapping(method = RequestMethod.GET, value = "/homeData/params/networkEntity/findNetworkEntityByIdIn", consumes = "application/json")
    JsonModel findNetworkEntityByIdIn(@RequestParam(value = "ids") String[] ids);

    @RequestMapping(method = RequestMethod.POST, value = "/homeData/params/indicatorTable/findUsableIndForNe", consumes = "application/json")
    JsonModel findUsableIndForNe(@RequestBody Map<String,List> map);

    @RequestMapping(method = RequestMethod.POST, value = "/homeData/params/indicatorTable/findIndByNeClass", consumes = "application/json")
    JsonModel findIndByNeClass(@RequestParam("neClasses") List<String> neClasses);

    @RequestMapping(method = RequestMethod.GET, value = "/homeData/params/indicatorTable/getIndicatorInfoByName", consumes = "application/json")
    JsonModel getIndicatorInfoByName(@RequestParam("indicatorName") String indicatorName);

    @RequestMapping(method = RequestMethod.GET, value = "/homeData/params/component/findNeComps", consumes = "application/json")
    JsonModel findNeComps(@RequestParam("neIds") List<String> neIds, @RequestParam("indicatorName") String indicatorName,
                          @RequestParam("componentName") String componentName, @RequestParam("neName") String neName,
                          @RequestParam("neCompIdNotIn") List<String> neCompIdNotIn, @RequestParam("keyword") String... keyword);

    @RequestMapping(method = RequestMethod.POST, value = "/homeData/params/networkLink/getNeIdsByDomainIds", consumes = "application/json")
    JsonModel getNeIdsByDomainIds(@RequestParam("domainIds") Long[] domainIds, @RequestHeader("Cookie") String cookie);

    @RequestMapping(method = RequestMethod.POST, value = "/homeData/params/networkLink/findPage", consumes = "application/json")
    JsonModel findPage(@RequestParam("pageModel") String pageModel, @RequestBody NetworkLinkModel networkLinkModel);

    @RequestMapping(method = RequestMethod.GET, value = "/homeData/networkEntity/vmStatics", consumes = "application/json")
    JsonModel vmStatics(@RequestParam("domain") Long domain);

    @RequestMapping(method = RequestMethod.POST, value = "/homeData/networkEntity/neStatistics", consumes = "application/json")
    JsonModel neStatistics(@RequestParam("domainId") Long domainId, @RequestParam("baseNeClass") BaseNeClass baseNeClass);

    @RequestMapping(method = RequestMethod.GET, value = "/homeData/networkEntity/countVr", consumes = "application/json")
    JsonModel countVr(@RequestParam("domainId") Long domainId);

    @RequestMapping(method = RequestMethod.POST, value = "/homeData/networkEntity/neStatusStatistics", consumes = "application/json")
    JsonModel neStatusStatistics(@RequestParam("domainId") List<Long> domainId, @RequestParam("baseNeClass") BaseNeClass baseNeClass);

    @RequestMapping(method = RequestMethod.POST, value = "/homeData/networkEntity/findByIdsAndBaseNe", consumes = "application/json")
    JsonModel findByIdsAndBaseNe(@RequestParam("domainId") List<Long> domainId,
                                 @RequestParam("itObjectIds") String itObjectIds,
                                 @RequestParam("baseNeClass") BaseNeClass baseNeClass);

    @RequestMapping(method = RequestMethod.POST, value = "/homeData/neHealth/getNeHealth", consumes = "application/json")
    JsonModel getNeHealth(@RequestParam("neId") String neId);

    @RequestMapping(method = RequestMethod.GET, value = "/homeData/indicator/getStrategy", consumes = "application/json")
    JsonModel getStrategy(@RequestParam("neId") String neId, @RequestParam("indicatorNames") String indicatorNames);

    @RequestMapping(method = RequestMethod.POST, value = "/homeData/health/findHealthByNeIdIn", consumes = "application/json")
    JsonModel findHealthByNeIdIn(@RequestParam("neIdIn") List<String> neIdIn);

    @RequestMapping(method = RequestMethod.GET, value = "/homeData/topn/getTopNByItObjects", consumes = "application/json")
    JsonModel getTopNByItObjects(@RequestParam("indicator") String indicator, @RequestParam("itObjectIds") String itObjectIds,
                                 @RequestParam("topStr") String topStr, @RequestParam("field") String field,
                                 @RequestParam("windowsStr") String windowsStr, @RequestParam("order") String order);

    @RequestMapping(method = RequestMethod.GET, value = "/homeData/neHealth/findNeHealthOrderByHealthy", consumes = "application/json")
    JsonModel findNeHealthOrderByHealthy(@RequestParam("regexpSplitToTable") String regexpSplitToTable,
                                         @RequestParam("order") String order);

    @RequestMapping(method = RequestMethod.GET, value = "/homeData/indicator/getFieldLables", consumes = "application/json")
    JsonModel getFieldLables(@RequestParam("indicatorID") String indicatorID);

    @RequestMapping(method = RequestMethod.GET, value = "/homeData/indicator/findLastObject", consumes = "application/json")
    JsonModel findLastObject(@RequestParam("neId") String neId, @RequestParam("indicatorName") String indicatorName,
                             @RequestParam("params") String params, @RequestParam("fetchDate") String fetchDate);

    // 根据参数模糊查询，获取资源ID
    @RequestMapping(method = RequestMethod.GET, value = "/monitorAlert/findNeIdByParams", consumes = "application/json")
    JsonModel findNeIdByParams(@RequestParam(value = "sourceManage") Boolean sourceManage,
                               @RequestParam(value = "objectIds") String objectIds, @RequestParam(value = "ip") String ip,
                               @RequestParam(value = "name") String name, @RequestParam(value = "type") String type,
                               @RequestParam(value = "objType") String objType, @RequestParam(value = "domainIds") Long[] domainIds,
                               @RequestParam(value = "groupId") Long groupId);

    // 根据资源的各项参数查询资源列表
    @RequestMapping(method = RequestMethod.GET, value = "/monitorAlert/findNeListByParams", consumes = "application/json")
    JsonModel findNeListByParams(@RequestParam(value = "sourceManage") Boolean sourceManage,
                                 @RequestParam(value = "objectIds") String objectIds, @RequestParam(value = "ip") String ip,
                                 @RequestParam(value = "name") String name, @RequestParam(value = "type") String type,
                                 @RequestParam(value = "objType") String objType, @RequestParam(value = "domainIds") Long[] domainIds,
                                 @RequestParam(value = "groupId") Long groupId);

    /**
     * 以上所有非monitor提供的公用数据获取接口 均应删除 功能调整通过公用接口获取
     */

    @RequestMapping(method = RequestMethod.GET, value = "/monitorApi/getNeList", consumes = "application/json")
    JsonModel getNeList(@RequestParam Map<String , Object> map);

    @RequestMapping(method = RequestMethod.GET, value = "/monitorApi/getUsableInd", consumes = "application/json")
    JsonModel getUsableInd(@RequestParam Map<String,Object> map);

    @RequestMapping(method = RequestMethod.GET, value = "/monitorApi/getIndValues", consumes = "application/json")
    JsonModel getIndValues(@RequestParam Map<String,Object> map);

    @RequestMapping(method = RequestMethod.GET, value = "/monitorApi/findNeComps", consumes = "application/json")
    JsonModel findNeComps(@RequestParam Map<String, Object> map);

    @RequestMapping(method = RequestMethod.GET, value = "/monitorApi/findNeLinks", consumes = "application/json")
    JsonModel findNeLinks(@RequestParam Map<String, Object> map);

    @RequestMapping(method = RequestMethod.GET, value = "/monitorApi/findNeHealth", consumes = "application/json")
    JsonModel findNeHealth(@RequestParam("neIds")List<String> neIds,
                           @RequestParam("isHistory") boolean isHistory,
                           @RequestParam("order") String order);

    @RequestMapping(method = RequestMethod.GET, value = "/monitorApi/findNeHealth", consumes = "application/json")
    JsonModel findNeHealth(@RequestParam Map<String, Object> map);
}
