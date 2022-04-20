package com.uxsino.leaderview.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.uxsino.commons.model.JsonModel;
import com.uxsino.leaderview.dao.IWuhouHomestayDao;
import com.uxsino.leaderview.dao.IWuhouHotelRegisterDao;
import com.uxsino.leaderview.dao.IWuhouPatrolRecordDao;
import com.uxsino.leaderview.entity.WuhouHomestay;
import com.uxsino.leaderview.entity.WuhouHotelRegister;
import com.uxsino.leaderview.entity.WuhouPatrolRecord;
import com.uxsino.leaderview.service.wuhou.WuHouService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class QunZuFangService {

    @Autowired
    IWuhouHomestayDao wuhouHomestayDao;

    @Autowired
    IWuhouHotelRegisterDao wuhouHotelRegisterDao;

    @Autowired
    IWuhouPatrolRecordDao wuhouPatrolRecordDao;

    @Autowired
    WuHouService wuHouService;

    public JsonModel getQZFHomeCount(Integer type){

        List<WuhouHomestay> list = new ArrayList<>();
        if(type == 0){
            list = wuhouHomestayDao.findAll();
        }else {
            list = wuhouHomestayDao.findByPlaceType(type);
        }

        list = list.stream().filter(s -> s.getIsDeleted() == 0).collect(Collectors.toList());

        int count = list.size();

        JSONObject result = new JSONObject();
        result.put("name",type);
        result.put("info",count);
        result.put("value",count);

        List<String> columns = Arrays.asList("群租房ID","经营者姓名","床铺数");
        JSONArray rows = new JSONArray();
        for (int i = 0; i < list.size(); i++){
            if(i >= 20) break;
            JSONObject row = new JSONObject();
            WuhouHomestay homestay = list.get(i);
            row.put("群租房ID", homestay.getId());
            row.put("经营者姓名", homestay.getOperatorName());
            row.put("床铺数", homestay.getBedNum());
            rows.add(row);
        }
        JSONObject listObj = new JSONObject();
        listObj.put("columns",columns);
        listObj.put("rows",rows);
        result.put("list",listObj);

        return new JsonModel(true ,result);
    }

    public JsonModel getTodayLiveCount(String type){

        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DAY_OF_MONTH,0);

        //一天的开始时间 yyyy:MM:dd 00:00:00
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        Date dayStart = calendar.getTime();


        Date dayEnd = new Date();

        Date date = new Date();
        calendar.setTime(date);
        //获得本月第一天
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date monthStart = calendar.getTime();


        List<WuhouHotelRegister> list = new ArrayList<>();
        Long count;

        if("checkIn".equals(type)){
            list = wuhouHotelRegisterDao.findBycheckInDate(dayStart,dayEnd);
        }else if("checkOut".equals(type)){
            list = wuhouHotelRegisterDao.findByCheckOutDateDate(dayStart,dayEnd);
        }else {
            list = wuhouHotelRegisterDao.findBycheckInDate(monthStart,dayEnd);
        }

        count = Long.valueOf(list.size());

        List<String> columns = Arrays.asList("场所ID","住户姓名","场所名称");
        JSONArray rows = new JSONArray();
        for (int i = 0; i < list.size(); i++){
            if(i >= 20) break;
            JSONObject row = new JSONObject();
            WuhouHotelRegister register = list.get(i);
            row.put("场所ID", register.getHotelId());
            row.put("住户姓名", Strings.isNullOrEmpty(register.getGuestName()) ? "未登记" :register.getGuestName());
            row.put("场所名称", Strings.isNullOrEmpty(register.getHotelName()) ? "未登记" :register.getHotelName());
            rows.add(row);
        }

        JSONObject result = new JSONObject();
        result.put("name",type);
        result.put("info",count);
        result.put("value",count);

        if(!"month".equals(type)) {
            JSONObject listObj = new JSONObject();
            listObj.put("columns", columns);
            listObj.put("rows", rows);
            result.put("list", listObj);
        }

        return new JsonModel(true ,result);
    }

    public JsonModel getLiveSort(){

        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        //获得两个月前第一天
        calendar.add(Calendar.MONTH, -2);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date monthStart = calendar.getTime();

        List<String[]> listDesc = new ArrayList<>();
        List<String[]> listAsc = new ArrayList<>();
        listDesc = wuhouHotelRegisterDao.getLiveSortDesc(monthStart);
        listAsc = wuhouHotelRegisterDao.getLiveSortAsc(monthStart);

        //过滤掉场所ID为空的数据
        listDesc =listDesc.stream().filter(strings ->!Strings.isNullOrEmpty(strings[0])).collect(Collectors.toList());
        listAsc =listAsc.stream().filter(strings ->!Strings.isNullOrEmpty(strings[0])).collect(Collectors.toList());


        JSONObject result = new JSONObject();
        List<String> columns = Arrays.asList("群租房ID","次数");
        JSONArray rows1 = new JSONArray();
        JSONArray rows2 = new JSONArray();


        for (int i = 0; i < listDesc.size(); i++){
            if (i >= 5) break;
            JSONObject row1 = new JSONObject();
            String[] desc = listDesc.get(i);
            row1.put("群租房ID",desc[0]);
            row1.put("次数",desc[1]);
            rows1.add(row1);
        }

        for (int i = 0; i < listAsc.size(); i++){
            if (i >= 5) break;
            JSONObject row2 = new JSONObject();
            String[] asc = listAsc.get(i);
            row2.put("群租房ID",asc[0]);
            row2.put("次数",asc[1]);
            rows2.add(row2);
        }

        JSONObject data1 = new JSONObject();
        JSONObject data2 = new JSONObject();
        data1.put("columns",columns);
        data2.put("columns",columns);
        data1.put("unit","次");
        data2.put("unit","次");
        data1.put("rows",rows1);
        data2.put("rows",rows2);

        JSONArray dataArray2 = new JSONArray();
        dataArray2.add(data1);
        dataArray2.add(data2);

        JSONArray nameArray = new JSONArray();
        nameArray.add("高入住房TOP5");
        nameArray.add("高空置房TOP5");

        JSONObject dataArray = new JSONObject();
        dataArray.put("dataArray",dataArray2);
        dataArray.put("nameArray",nameArray);

        result.put("rows",new JSONObject());
        result.put("columns",new JSONObject());
        result.put("unit", "次");
        result.put("dataArray",dataArray);

        return new JsonModel(true,result);
    }

    public JsonModel getLiveByTime(String when){

        Calendar calendar = new GregorianCalendar();
        if("today".equals(when)) {
            calendar.add(Calendar.DAY_OF_MONTH, 0);
        }else if("yesterday".equals(when)){
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }

        //一天的开始时间 yyyy:MM:dd 00:00:00
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        JSONArray rows = new JSONArray();

        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

        for(int i = 0;i < 8; i++){
            Date start = calendar.getTime();
            calendar.add(Calendar.HOUR_OF_DAY,3);
            Date end = calendar.getTime();
            List<WuhouHotelRegister> list = wuhouHotelRegisterDao.findBycheckInDate(start,end);
            JSONObject row = new JSONObject();
            row.put("时间", format.format(start) + "~" + format.format(end));
            row.put("次数",list.size());
            rows.add(row);
        }

        JSONObject result = new JSONObject();
        List<String> columns = Arrays.asList("时间","次数");
        result.put("rows",rows);
        result.put("columns",columns);
        result.put("unit","次");
        result.put("unitX","时间");

        /*calendar.add(Calendar.HOUR_OF_DAY,3);
        Date am3 = calendar.getTime();
        calendar.add(Calendar.HOUR_OF_DAY,3);
        Date am6 = calendar.getTime();
        calendar.add(Calendar.HOUR_OF_DAY,3);
        Date am9 = calendar.getTime();
        calendar.add(Calendar.HOUR_OF_DAY,3);
        Date am12 = calendar.getTime();
        calendar.add(Calendar.HOUR_OF_DAY,3);
        Date am15 = calendar.getTime();
        calendar.add(Calendar.HOUR_OF_DAY,3);
        Date am18 = calendar.getTime();
        calendar.add(Calendar.HOUR_OF_DAY,3);
        Date am21 = calendar.getTime();
        calendar.add(Calendar.HOUR_OF_DAY,3);
        Date am24 = calendar.getTime();

        List<WuhouHotelRegister> list03 = wuhouHotelRegisterDao.findBycheckInDate(am0,am3);
        List<WuhouHotelRegister> list36 = wuhouHotelRegisterDao.findBycheckInDate(am3,am6);
        List<WuhouHotelRegister> list69 = wuhouHotelRegisterDao.findBycheckInDate(am6,am9);
        List<WuhouHotelRegister> list912 = wuhouHotelRegisterDao.findBycheckInDate(am9,am12);
        List<WuhouHotelRegister> list1215 = wuhouHotelRegisterDao.findBycheckInDate(am12,am15);
        List<WuhouHotelRegister> list1518 = wuhouHotelRegisterDao.findBycheckInDate(am15,am18);
        List<WuhouHotelRegister> list1821 = wuhouHotelRegisterDao.findBycheckInDate(am18,am21);
        List<WuhouHotelRegister> list2124 = wuhouHotelRegisterDao.findBycheckInDate(am21,am24);*/

        return new JsonModel(true,result);
    }

    public JsonModel getRegisterByAddress(String address) {

        List<WuhouHomestay> homestay = wuhouHomestayDao.findByAddress(address);
        homestay = homestay.stream().filter(s -> s.getIsDeleted() == 0).collect(Collectors.toList());
        Long hotelId = homestay.get(0).getId();
        List<WuhouHotelRegister> list = wuhouHotelRegisterDao.findByHotelId(hotelId);

        return new JsonModel(true,list);
    }


    public JsonModel getPatrolByAddress(String address) {

        List<WuhouHomestay> homestay = wuhouHomestayDao.findByAddress(address);
        homestay = homestay.stream().filter(s -> s.getIsDeleted() == 0).collect(Collectors.toList());
        Long hotelId = homestay.get(0).getId();
        List<WuhouPatrolRecord> list = wuhouPatrolRecordDao.findByHotelId(hotelId);

        return new JsonModel(true,list);

    }

    //——————————————————————————————————————————————————智慧社区-群租房版块—————————————————————————————————————————————————————————
    //——————————————————————————————————————————————————智慧社区-群租房版块—————————————————————————————————————————————————————————
    //——————————————————————————————————————————————————智慧社区-群租房版块—————————————————————————————————————————————————————————

    /**
     * 1、群租房：敏感人员分析
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/y08-01/data
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getQZF1(){

        String res = null;
        try {
            res = wuHouService.getData("y65-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("人员类别","type");
        map.put("数量","sum");
        JSONObject result = new JSONObject();

        result = wuHouService.getPieResult(map,data);

        return new JsonModel(true,result);

    }

    /**
     * 、群租房：
     * 接口URL： {{baseUrl}}/apis/daas/pro/3/components/y13-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getQZF2(String param){

        String res = null;
        try {
            res = wuHouService.getData("y78-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");
        JSONObject dataObj = (JSONObject) data.get(0);
        //民宿旅社
        Integer type1 = dataObj.getInteger("a");
        //小旅馆
        Integer type2 = dataObj.getInteger("b");
        //总量
        Integer type3 = type1 + type2;

        //场所类型名称和数量的map
        HashMap<String, Integer> nameAndValueMap = new HashMap<>();
        nameAndValueMap.put("民宿旅社",type1);
        nameAndValueMap.put("小旅馆",type2);
        nameAndValueMap.put("总量",type3);
        //场所类型名称和对应placeType的map
        HashMap<String, String> nameAndTypeMap = new HashMap<>();
        nameAndTypeMap.put("民宿旅社","1");
        nameAndTypeMap.put("小旅馆","2");
        nameAndTypeMap.put("总量","总量");

        Integer info = nameAndValueMap.get(param);
        String type = nameAndTypeMap.get(param);
        JSONObject result = new JSONObject();
        result.put("name",type);
        result.put("info",info);

//        String url = "/leaderview/QZF/getQZF11?query=placeType&param=" + param + ":";
        String url = "/leaderview/QZF/getQZF11?query=placeType&param=name:";
        result.put("url",url);
//        result = wuHouService.getPieResult(map,data);

        return new JsonModel(true,result);

    }

    /**
     * 3、群租房：群租房入住人员归属地分析
     * 接口URL： {{baseUrl}}/apis/daas/pro/3/components/y13-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getQZF3(){

        String res = null;
        try {
            res = wuHouService.getData("y13-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        JSONObject result = new JSONObject();
        result.put("rowsArray", data);

        return new JsonModel(true,result);

    }

    /**
     * 4、群租房：入住异常预警
     * 接口URL： {{baseUrl}}/apis/daas/pro/3/components/y08-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getQZF4(){

        String res = null;
        try {
            res = wuHouService.getData("y08-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("姓名","guestName");
        map.put("手机","guestPhone");
        map.put("入住日期","checkInDate");
        map.put("离店日期","checkOutDate");
        map.put("身份证","guestIdentity");
        map.put("场所名称","placeName");
        map.put("地址","address");
        map.put("人员类型","type");

        JSONObject result = new JSONObject();

        result = wuHouService.getPieResult(map,data);
        List<String> columns = Arrays.asList("姓名","入住日期","场所名称","地址","人员类型");
        result.put("columns",columns);
        return new JsonModel(true,result);

    }

    /**
     * 5、群租房-入住房top5:高频入住、高流动性
     * 接口URL： {{baseUrl}}/apis/daas/pro/3/components/y13-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getQZF5(){

        JSONObject result = new JSONObject();
        JSONArray dataArry = new JSONArray();
        JSONObject dataArray = new JSONObject();
        JSONArray nameArry = new JSONArray();
        nameArry.add("高频入住人员TOP10");
        nameArry.add("高流动性人员TOP10");
//        result.put("dataArry",dataArry);
        dataArray.put("nameArray",nameArry);

        String res1 = null;
        String res2 = null;
        try {
            //高频入住
            res1 = wuHouService.getData("y61-01","per_page=10000&page=1",null,false,true);
            //高频流动
            res2 = wuHouService.getData("y62-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res1);
        JSONObject object2 = JSONObject.parseObject(res2);
        JSONArray data1 = object.getJSONArray("data");
        JSONArray data2 = object2.getJSONArray("data");

        LinkedHashMap<String, String> map1 = new LinkedHashMap<>();
        LinkedHashMap<String, String> map2 = new LinkedHashMap<>();

        map1.put("姓名","guestName");
        map2.put("姓名","guestName");
        map1.put("次数","num");
        map2.put("次数","num");

        JSONObject result1 = new JSONObject();
        JSONObject result2 = new JSONObject();

        result1 = wuHouService.getPieResult(map1,data1);
        result2 = wuHouService.getPieResult(map2,data2);
        result1.put("unit","次");
        result2.put("unit","次");
        //给前端的返回中guestName已经被转换为了“姓名”，所以要给前端“姓名”
        String url = "/leaderview/QZF/getQZF10?query=guestName&param=姓名:";
        result1.put("url", url);
        result2.put("url", url);
        dataArry.add(result1);
        dataArry.add(result2);
        dataArray.put("dataArray",dataArry);

        result.put("dataArray",dataArray);
        result.put("rows",new JSONArray());
        result.put("columns",new JSONArray());
        result.put("unit", "次");
//        String url = "/leaderview/QZF/getQZF10?params=guestName&param=guestName:";
        result.put("url", url);

        return new JsonModel(true,result);

    }

    /**
     * 6、群租房-入住房率top5:高入住房、高空置房
     * 接口URL： {{baseUrl}}/apis/daas/pro/3/components/y13-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getQZF6(){

        JSONObject result = new JSONObject();
        JSONArray dataArry = new JSONArray();
        JSONObject dataArray = new JSONObject();
        JSONArray nameArry = new JSONArray();
        nameArry.add("高入住房TOP5");
        nameArry.add("高空置房TOP5");
//        result.put("dataArry",dataArry);
        dataArray.put("nameArray",nameArry);

        String res1 = null;
        String res2 = null;
        try {
            //高频入住
            res1 = wuHouService.getData("y63-01","per_page=10000&page=1",null,false,true);
            //高频流动
            res2 = wuHouService.getData("y64-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res1);
        JSONObject object2 = JSONObject.parseObject(res2);
        JSONArray data1 = object.getJSONArray("data");
        JSONArray data2 = object2.getJSONArray("data");

        LinkedHashMap<String, String> map1 = new LinkedHashMap<>();
        LinkedHashMap<String, String> map2 = new LinkedHashMap<>();

        map1.put("场所id","hotelId");
        map2.put("场所id","hotelId");
        map1.put("次数","num");
        map2.put("次数","num");

        JSONObject result1 = new JSONObject();
        JSONObject result2 = new JSONObject();

        result1 = wuHouService.getPieResult(map1,data1);
        result2 = wuHouService.getPieResult(map2,data2);
        result1.put("unit","次");
        result2.put("unit","次");
        String url = "/leaderview/QZF/getQZF11?query=_id&param=场所id:";
        result1.put("url", url);
        result2.put("url", url);
        dataArry.add(result1);
        dataArry.add(result2);
        dataArray.put("dataArray",dataArry);

        result.put("dataArray",dataArray);
        result.put("rows",new JSONArray());
        result.put("columns",new JSONArray());
        result.put("unit", "次");
        result.put("url", url);

        return new JsonModel(true,result);

    }

    /**
     * 7、群租房：敏感人员人类占比
     * 接口URL： {{baseUrl}}/apis/daas/pro/3/components/y65-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getQZF7(){

        String res = null;
        try {
            res = wuHouService.getData("y65-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("人员类型","type");
        map.put("数量","sum");

        JSONObject result = new JSONObject();
        result = wuHouService.getPieResult(map,data);

        return new JsonModel(true,result);

    }

    /**
     * 8、群租房：入住时间分析
     * 接口URL： {{baseUrl}}/apis/daas/pro/3/components/y66-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getQZF8(){

        String res = null;
        try {
            res = wuHouService.getData("y66-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("00:00:00~03:00:00","0-3");
        map.put("03:00:00~06:00:00","3-6");
        map.put("06:00:00~09:00:00","6-9");
        map.put("09:00:00~12:00:00","9-12");
        map.put("12:00:00~15:00:00","12-15");
        map.put("15:00:00~18:00:00","15-18");
        map.put("18:00:00~21:00:00","18-21");
        map.put("21:00:00~00:00:00","21-24");
        JSONArray columns = new JSONArray();
        columns.add("时间");
        columns.add("次数");
        JSONObject result = new JSONObject();
        result = wuHouService.getOneToPie(columns, data, map);

        return new JsonModel(true,result);

    }

    /**
     * 9、群租房：入住情况统计-当日登记/离店/当月累计量
     * 接口URL： {{baseUrl}}/apis/daas/pro/3/components/y13-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getQZF9(){

        String res = null;
        try {
            res = wuHouService.getData("y77-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        //    "aa": 3, //今日离店量
        //    "bb": 0, //当日登记量
        //    "cc": 53 //本月累积量
        map.put("当日登记量","bb");
        map.put("今日离店量","aa");
        map.put("本月累积量","cc");

        JSONObject result = new JSONObject();

        result = wuHouService.getPieResult(map,data);

        return new JsonModel(true,result);

    }

    /**
     * 10、群租房-入住信息查询接口
     * 接口URL： {{baseUrl}}/apis/daas/pro/3/components/y13-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getQZF10(String query, String param){
        if(!ObjectUtils.isEmpty(query) && !ObjectUtils.isEmpty(param)) {
            query = query + "=" + param.split(":")[1];
        }
        String res = null;
        try {
            res = wuHouService.getData("y58-01","per_page=10000&page=1",query,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        //"checkInDate": "2022-03-07", //入住日期
        //"checkOutDate": "2022-03-08", //离店日期
        //"createTime": "1646739517862", //记录创建时间
        //"createUser": "-1", //记录创建人id
        //"guestIdentity": "511022198908151235", //住客身份证号
        //"guestName": "张刘", //住客名称
        //"guestPhone": "17364847969", //住客手机
        //"hotelId": "254", //场所id
        //"hotelName": " ", //场所名称
        map.put("姓名","guestName");
        map.put("手机","guestPhone");
        map.put("入住日期","checkInDate");
        map.put("离店日期","checkOutDate");
        map.put("身份证","guestIdentity");
        map.put("场所id","hotelId");
        map.put("场所名称","placeName");

        JSONObject result = new JSONObject();

        result = wuHouService.getPieResult(map,data);
        return new JsonModel(true,result);

    }

    /**
     * 11、群租房-场所信息查询接口
     * 接口URL： {{baseUrl}}/apis/daas/pro/3/components/y13-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getQZF11(String query, String param){
        if(!ObjectUtils.isEmpty(query) && !ObjectUtils.isEmpty(param)) {
            query = query + "=" + param;
            if("总量".equals(param)){
                query = null;
            }
        }
        String res = null;
        try {
            res = wuHouService.getData("y59-01","per_page=10000&page=1",query,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("地址","address");
        map.put("房间数","roomNum");
        map.put("床铺数","bedNum");
        map.put("业主（房东）姓名","landlordName");
        map.put("业主（房东）联系电话","landlordPhone");
        map.put("名称","placeName");
        map.put("社区民警","communityPolice");
        map.put("社区民警电话","communityPolicePhone");
        map.put("微消站","fireStation");
        map.put("微消站电话","fireStationPhone");
        map.put("网格员","gridMember");
        map.put("网格员电话","gridMemberPhone");
        map.put("经营者姓名","operatorName");
        map.put("经营者联系电话","operatorPhone");
        map.put("营业状态(1:营业,2:暂停营业)","status");
        map.put("房屋类型","placeType");

        JSONObject result = new JSONObject();

        result = wuHouService.getPieResult(map,data);

        return new JsonModel(true,result);

    }

    /**
     * 12、群租房-入住人员明细信息
     * 接口URL： {{baseUrl}}/apis/daas/pro/3/components/y75-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getQZF12(String param){

        HashMap<String,String> nameAndIdMap = new HashMap<>();
        nameAndIdMap.put("今日离店量","y74-01");
        nameAndIdMap.put("当日登记量","y75-01");
        nameAndIdMap.put("本月累积量","y76-01");
        String formId = nameAndIdMap.get(param.split(":")[1]);

        String res = null;
        try {
            res = wuHouService.getData(formId,"per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();

        map.put("住客名称","uname");
        map.put("住客手机","replace");
        map.put("住客身份证号","idcard_number");

        JSONObject result = new JSONObject();
        result = wuHouService.getPieResult(map,data);

        return new JsonModel(true,result);

    }

    /**
     * 13、群租房-当日离店人员明细信息
     * 接口URL： {{baseUrl}}/apis/daas/pro/3/components/y13-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getQZF13(){

        String res = null;
        try {
            res = wuHouService.getData("y74-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("住客名称","uname");
        map.put("住客手机","replace");
        map.put("住客身份证号","idcard_number");

        JSONObject result = new JSONObject();

        result = wuHouService.getPieResult(map,data);

        return new JsonModel(true,result);

    }

    /**
     * 、群租房：
     * 接口URL： {{baseUrl}}/apis/daas/pro/3/components/y13-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getQZF14(){

        String res = null;
        try {
            res = wuHouService.getData("y13-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("","");

        JSONObject result = new JSONObject();

        result = wuHouService.getPieResult(map,data);

        return new JsonModel(true,result);

    }

    /**
     * 、群租房：
     * 接口URL： {{baseUrl}}/apis/daas/pro/3/components/y13-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getQZF15(){

        String res = null;
        try {
            res = wuHouService.getData("y13-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("","");

        JSONObject result = new JSONObject();

        result = wuHouService.getPieResult(map,data);

        return new JsonModel(true,result);

    }


    /**
     * 1、未办证住所-全区网约房地图打点
     * 接口URL： {{baseUrl}}/apis/daas/pro/3/components/y49-02/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getWBZ1(){

        String res = null;
        try {
            res = wuHouService.getData("y49-02","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        JSONObject result = new JSONObject();
        /*LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("id","id");
        map.put("所属街道","street");
        map.put("名称","room_name");
        map.put("地址","address");
        map.put("经度","longitude");
        map.put("纬度","latitude");
        map.put("业主（房东）姓名","owner_name");
        map.put("业主（房东）联系电话","owner_phone");
        map.put("经营者姓名","manager_name");
        map.put("经营者联系电话","maager_phone");
        map.put("房间数","room_number");
        map.put("床铺数","bed_number");
        map.put("网格员姓名","gridman_name");
        map.put("网格员联系电话","gridman_phone");
        map.put("社区民警姓名","police_name");
        map.put("社区民警联系电话","police_phone");
        map.put("区域微型消防站联络员姓名","liaison_name");
        map.put("区域微型消防站联络电话","liaison_phone");

        result = getPieResult(map,data);*/
        result.put("dataArray", data);

        return new JsonModel(true,result);

    }

    /**
     * 2、未办证住所-街道网约房分布
     * 接口URL： {{baseUrl}}/apis/daas/pro/3/components/y50-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getWBZ2(){

        String res = null;
        try {
            res = wuHouService.getData("y50-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");
        data = JSON.parseArray(JSON.toJSONString(data.stream().filter(obj -> !((JSONObject) obj).getString("street").equals("全区数")).collect(Collectors.toList())));

        JSONObject result = new JSONObject();
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        //    "street": "簇锦街道", //所属街道
        //    "num": 44, //网约房数量
        //    "fjzs": 709 //房间总数
        map.put("街道","street");
        map.put("网约房数量","num");
//        map.put("房间总数","fjzs");
        result = wuHouService.getPieResult(map,data);

        return new JsonModel(true,result);

    }

    /**
     * 3、未办证住所-入住街道top5接口
     * 接口URL： {{baseUrl}}/apis/daas/pro/3/components/y13-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getWBZ3(){

        String res = null;
        try {
            res = wuHouService.getData("y93-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");
        data = JSON.parseArray(JSON.toJSONString(data.stream().filter(obj -> !((JSONObject) obj).getString("street").equals("总店铺数")).collect(Collectors.toList())));

        JSONObject result = new JSONObject();
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("所属街道","street");
        map.put("数量","num");
        result = wuHouService.getPieResult(map,data);
        String jsonString = result.toJSONString();

        return new JsonModel(true,result);

    }

    /**
     * 4、未办证住所-入住人员户籍地TOP10
     * 接口URL： {{baseUrl}}/apis/daas/pro/3/components/y94-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getWBZ4(){

        String res = null;
        try {
            res = wuHouService.getData("y94-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = new JSONArray();
        //因为sublist方法JSONArray无法调用，所以先用List<Object>接收
        List<Object> list = object.getJSONArray("data");

        JSONObject result = new JSONObject();
        if(list.size() >= 10) {
            list = list.subList(0, 10);
        }
        data = JSONArray.parseArray(JSON.toJSONString(list));

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("省份","province");
        map.put("数量","num");
        result = wuHouService.getPieResult(map,data);
        String jsonString = result.toJSONString();

        return new JsonModel(true,result);

    }

    /**
     * 5、未办证住所-入住情况统计
     * 接口URL： {{baseUrl}}/apis/daas/pro/3/components/y13-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getWBZ5(){

        String res = null;
        try {
            res = wuHouService.getData("y95-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        JSONObject result = new JSONObject();
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        //    "num": 0, //今日登记人数
        //    "jrdjfjs": 0 //今日登记房间数
        map.put("今日登记人数","num");
        map.put("今日登记房间数","jrdjfjs");
        result = wuHouService.getPieResult(map,data);
        String jsonString = result.toJSONString();

        return new JsonModel(true,result);

    }

    /**
     * 6、未办证住所-一月内空置排名
     * 接口URL： {{baseUrl}}/apis/daas/pro/3/components/y13-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getWBZ6(){

        String res = null;
        try {
            res = wuHouService.getData("y96-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        JSONObject result = new JSONObject();
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("场所名称","placeName");
        map.put("场所地址","address");
        map.put("入住次数","num");
        result = wuHouService.getPieResult(map,data);

        return new JsonModel(true,result);

    }

    /**
     * 7、未办证住所-一月内入住排名
     * 接口URL： {{baseUrl}}/apis/daas/pro/3/components/y13-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getWBZ7(){

        String res = null;
        try {
            res = wuHouService.getData("y97-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        JSONObject result = new JSONObject();
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("场所名称","placeName");
        map.put("场所地址","address");
        map.put("入住次数","num");
        result = wuHouService.getPieResult(map,data);

        return new JsonModel(true,result);

    }

    /**
     * 、未办证住所-
     * 接口URL： {{baseUrl}}/apis/daas/pro/3/components/y13-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getWBZ8(){

        String res = null;
        try {
            res = wuHouService.getData("y97-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        JSONObject result = new JSONObject();
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("场所名称","placeName");
        map.put("场所地址","address");
        map.put("入住次数","num");
        result = wuHouService.getPieResult(map,data);

        return new JsonModel(true,result);

    }

    /**
     * 、未办证住所-
     * 接口URL： {{baseUrl}}/apis/daas/pro/3/components/y13-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getWBZ9(){

        String res = null;
        try {
            res = wuHouService.getData("y97-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        JSONObject result = new JSONObject();
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("场所名称","placeName");
        map.put("场所地址","address");
        map.put("入住次数","num");
        result = wuHouService.getPieResult(map,data);

        return new JsonModel(true,result);

    }

}
