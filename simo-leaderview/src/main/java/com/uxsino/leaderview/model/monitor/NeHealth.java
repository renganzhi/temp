package com.uxsino.leaderview.model.monitor;

import com.alibaba.fastjson.JSONArray;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.Id;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NeHealth {
    /**
     * 资源ID
     */
    @Id
    private String id;

    /**
     * 健康度得分
     */
    private int health;

    /**
     * 健康度详情，格式如：[{"levelHealth":40,"indicatorName":"vm_info","fieldName":"num_host","level":6,"indicatorText":"ESXI信息","fieldConfId":1515,"weight":1,"fieldText":"虚拟机数量"}]
     */
    @Type(type = "jsonb")
    private JSONArray detail;

    /**
     * 最近更新时间
     */
    private Date updateDate;

}
