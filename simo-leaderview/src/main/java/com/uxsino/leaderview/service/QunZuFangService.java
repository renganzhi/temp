package com.uxsino.leaderview.service;


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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        dataArray.put("dataArry",dataArray2);
        dataArray.put("nameArry",nameArray);

        result.put("rows",new JSONObject());
        result.put("columns",new JSONObject());
        result.put("unit", "次");
        result.put("dataArry",dataArray);

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
}
