package com.uxsino.leaderview.rpc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.uxsino.commons.db.model.PageModel;
import com.uxsino.commons.model.BaseNeClass;
import com.uxsino.commons.model.JsonModel;
import com.uxsino.leaderview.model.monitor.NetworkEntityQO;
import com.uxsino.leaderview.model.monitor.NetworkLinkModel;
import com.uxsino.leaderview.rpc.hystrix.MonitorClientHystrixLeaderview;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * @description 调用SIMO-Monitoring的服务类
 * @date 2020年09月29日
 */
@FeignClient(value = "${service-monitoring}", fallback = MonitorClientHystrixLeaderview.class)
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


}
