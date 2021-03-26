package com.uxsino.leaderview.entity;

import com.alibaba.fastjson.JSONObject;
import com.uxsino.commons.db.JsonObjectUserType;
import com.uxsino.simo.networkentity.EntityInfo.SOURCE_TYPE;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "network_entity")
@TypeDef(name = "jsonb", typeClass = JsonObjectUserType.class)
public class NetworkEntity {

    @Id
    public String id;

    public String collectorId;

    @Type(type = "jsonb")
    public JSONObject j;

    public Date releaseTime;

    public Date updateTime;

    @Enumerated(EnumType.STRING)
    public SOURCE_TYPE sourceType;
}
