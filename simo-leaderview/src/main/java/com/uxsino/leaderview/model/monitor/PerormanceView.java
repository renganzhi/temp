//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.uxsino.leaderview.model.monitor;

import com.uxsino.commons.model.NeClass;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.map.LinkedMap;

public enum PerormanceView {
    TopSql("TopSql", NeClass.oracle,
            new String[]{"Sql_Id:sql_id", "DB Time(%):db_time_rate", "执行耗时(ms):elapsed_time", "平均执行耗时(ms):elapsed_time_avg", "执行次数:executions",
                    "平均返回行:return_rows_avg", "用户I/O平均等待时间(ms):wait_time_avg", "平均逻辑读/次:read_avg", "平均物理读/次:physical_read_avg"}),
    TopEvent("TopEvent", NeClass.oracle,
            new String[]{"Event:event", "Wait_Class:wait_class", "等待时间(ms):time", "DB Time(%):db_time_rate"}),
    TopSession("TopSession", NeClass.oracle,
            new String[]{"Sid:sid", "Serial号:serial", "UserName:username", "Wait_Time:time_waited", "Event:event", "Program:program"});

    private String value;
    private NeClass neClass;
    private String[] columns;

    private PerormanceView(String value, NeClass neClass, String[] columns) {
        this.value = value;
        this.neClass = neClass;
        this.columns = columns;
    }

    public Map getKeyToValues() {
        Map<String, String> map = new LinkedMap();
        for(int i = 0; i < this.columns.length; ++i) {
            String[] split = this.columns[i].split(":");
            map.put(split[0], split[1]);
        }
        return map;
    }

    public Map getTranslator() {
        Map<String, String> map = new LinkedMap();
        for(int i = 0; i < this.columns.length; ++i) {
            String[] split = this.columns[i].split(":");
            map.put(split[1], split[0]);
        }
        return map;
    }

    public NeClass getNeClass() {
        return this.neClass;
    }

    public String getValue() {
        return this.value;
    }

    public static List<PerormanceView> getPerormanceViewByNeclass(NeClass neClass) {
        PerormanceView[] values = PerormanceView.values();
        List list = new ArrayList();
        for(int i = 0; i < values.length; i++) {
            PerormanceView perormanceView = values[i];
            if (neClass.equals(perormanceView.getNeClass())) {
                list.add(perormanceView);
            }
        }
        return list;
    }
}
