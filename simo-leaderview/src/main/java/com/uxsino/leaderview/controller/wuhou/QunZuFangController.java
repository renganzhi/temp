package com.uxsino.leaderview.controller.wuhou;


import com.uxsino.commons.model.JsonModel;
import com.uxsino.leaderview.service.QunZuFangService;
import com.uxsino.leaderview.service.wuhou.WuHouService;
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

    @Autowired
    WuHouService wuHouService;

    /**
     * 1、获取群租房不同类型的数量
     * @param type 群租房类型
     * @return
     */
    @GetMapping("/getQZFHomeCount")
    public JsonModel getQZFHomeCount(Integer type){

        return qunZuFangService.getQZFHomeCount(type);

    }

    /**
     * 2、获取群租房当日登记量和当日入住量以及当月累计量
     * @param type
     * @return
     */
    @GetMapping("/getTodayLiveCount")
    public JsonModel getTodayLiveCount(String type){
        return qunZuFangService.getTodayLiveCount(type);
    }

    /**
     * 3、房屋住房率、空置率排行
     * @return
     */
    @GetMapping("/getLiveSort")
    public JsonModel getLiveSort(){

        return qunZuFangService.getLiveSort();
    }

    /**
     * 4、入住人员时间分析
     * @param when
     * @return
     */
    @GetMapping("/getLiveByTime")
    public JsonModel getLiveByTime(String when){

        return qunZuFangService.getLiveByTime(when);
    }

    /**
     * 5、通过地址查询入住记录
     * @param address
     * @return
     */
    @GetMapping("/getRegisterByAddress")
    public JsonModel getRegisterByAddress(String address){
        return qunZuFangService.getRegisterByAddress(address);
    }

    /**
     * 6、通过地址查询走访记录
     * @param address
     * @return
     */
    @GetMapping("/getPatrolByAddress")
    public JsonModel getPatrolByAddress(String address){
        return qunZuFangService.getPatrolByAddress(address);
    }

    @GetMapping("/getQZF1")
    public JsonModel getQZF1(){
        return wuHouService.getQZF1();
    }

    @GetMapping("/getQZF2")
    public JsonModel getQZF2(){
        return wuHouService.getQZF2();
    }

    /**
     * 3、群租房入住人员来源地(归属地)分析
     * @return
     */
    @GetMapping("/getQZF3")
    public JsonModel getQZF3(){
        return wuHouService.getQZF3();
    }

    /**
     * 4、未办证住所：全区网约房地图打点
     * @return
     */
    @GetMapping("/getQZF4")
    public JsonModel getQZF4(){
        return wuHouService.getQZF4();
    }

    /**
     * 5、未办证住所：街道网约房分布
     * @return
     */
    @GetMapping("/getQZF5")
    public JsonModel getQZF5(){
        return wuHouService.getQZF5();
    }

}
