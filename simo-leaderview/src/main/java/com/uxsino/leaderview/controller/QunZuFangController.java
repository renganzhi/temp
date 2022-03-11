package com.uxsino.leaderview.controller;


import com.uxsino.commons.model.JsonModel;
import com.uxsino.leaderview.service.QunZuFangService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"武侯智慧城市-群租房小程序数据接口"})
@RestController
@RequestMapping("/QZF")
@Slf4j
public class QunZuFangController {

    @Autowired
    QunZuFangService qunZuFangService;

    /**
     * 获取群租房不同类型的数量
     * @param type 群租房类型
     * @return
     */
    @GetMapping("/getQZFHomeCount")
    public JsonModel getQZFHomeCount(Integer type){

        return qunZuFangService.getQZFHomeCount(type);

    }

    /**
     * 获取群租房当日登记量和当日入住量以及当月累计量
     * @param type
     * @return
     */
    @GetMapping("/getTodayLiveCount")
    public JsonModel getTodayLiveCount(String type){
        return qunZuFangService.getTodayLiveCount(type);
    }

    /**
     * 房屋住房率、空置率排行
     * @return
     */
    @GetMapping("/getLiveSort")
    public JsonModel getLiveSort(){

        return qunZuFangService.getLiveSort();
    }

    @GetMapping("/getLiveByTime")
    public JsonModel getLiveByTime(String when){

        return qunZuFangService.getLiveByTime(when);
    }

    @GetMapping("/getRegisterByAddress")
    public JsonModel getRegisterByAddress(String address){
        return qunZuFangService.getRegisterByAddress(address);
    }

    @GetMapping("/getPatrolByAddress")
    public JsonModel getPatrolByAddress(String address){
        return qunZuFangService.getPatrolByAddress(address);
    }

}
