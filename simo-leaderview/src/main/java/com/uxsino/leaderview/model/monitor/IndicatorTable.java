package com.uxsino.leaderview.model.monitor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import com.uxsino.commons.db.JsonObjectUserType;
import com.uxsino.commons.utils.StringUtils;
import com.uxsino.simo.indicator.INDICATOR_TYPE;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@TypeDef(name = "jsonb", typeClass = JsonObjectUserType.class)
public class IndicatorTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 255, message = "name最大长度255")
    private String name;

    @Size(max = 255, message = "label最大长度255")
    private String label;

    @Size(max = 255, message = "indicatorType最大长度255")
    private String indicatorType;

    @Column(columnDefinition = "text")
    private String neclass;

    @Type(type = "jsonb")
    private JSONArray versions;

    @Type(type = "jsonb")
    private JSONArray vendorIds;

    @Column(columnDefinition = "text")
    private String neIds;

    /*private String componentType;

    private String componentKey;*/

    private Boolean withoutrule;

    @Type(type = "jsonb")
    private JSONArray fields;

    @Type(type = "jsonb")
    private JSONArray protocols;// 指标支持的协议，格式["snmp","wmi"]

    @Type(type = "jsonb")
    private JSONObject attribute; // 用于控制监控视图是否展示该指标等其他控制的配置

    private String tag;

    private String componentNameFormula;//表达式，标识子资源名称

    private Boolean noKey;

    @Transient
    private Boolean isPerformance = false;

    public Map<String, Object> createAccuracyJPQL() {
        Map<String, Object> temp = new HashMap<String, Object>(2);
        Map<String, Object> params = new HashMap<String, Object>(2);
        String jpql = "from IndicatorTable where 1=1 ";
        if (StringUtils.isStrictNotEmpty(name)) {
            jpql += " and name = :name";
            params.put("name", name);
        }
        if (StringUtils.isStrictNotEmpty(label)) {
            jpql += " and label = :label";
            params.put("label", label);
        }
        temp.put("jpql", jpql);
        temp.put("params", params);
        return temp;
    }

    public String getTag() {
        if ("useable".equals(tag) || "performance".equals(tag)) {
            return tag;
        }
        tag = "info";
        if (fields != null && !fields.isEmpty()) {
            for (int i = 0; i < fields.size(); i++) {
                JSONObject obj = fields.getJSONObject(i);
                if ("performance".equals(obj.getString("tag"))) {
                    tag = "performance";
                    break;
                }
            }
        }
        return tag;
    }

    public JSONObject toCollectorInd(){
        JSONObject ind = new JSONObject();
        ind.put("tagName", "indicator");
        ind.put("name", this.getName());
        ind.put("label", this.getLabel());
        ind.put("type", this.getIndicatorType());
        ind.put("neclass", this.getNeclass());
        ind.put("noKey", this.getNoKey());
        if( this.getProtocols() != null ) {
            Set<String> protocols = Sets.newHashSet();
            for (int i = 0; i < this.getProtocols().size(); i++) {
                protocols.add(this.getProtocols().getString(i));
            }

            ind.put("protocols", protocols.stream().collect(Collectors.joining(",")));
        }
        if(INDICATOR_TYPE.NUMBER.name().equals(this.getIndicatorType()) || INDICATOR_TYPE.PERCENT.name().equals(this.getIndicatorType())){
            if(!Strings.isNullOrEmpty(this.getTag())){
                ind.put("tag", this.getTag());
            }
        }
        if (INDICATOR_TYPE.COMPOUND.name().equals(this.getIndicatorType())) {
            JSONArray indFields = new JSONArray();
            JSONObject indField = new JSONObject();
            indField.put("field", this.getFields());
            indFields.add(indField);
            ind.put("fields", indFields);
        } else if (INDICATOR_TYPE.LIST.name().equals(this.getIndicatorType())) {
            if(!Strings.isNullOrEmpty(this.getComponentNameFormula())) {
                ind.put("componentNameFormula", this.getComponentNameFormula());
            }
            ind.put("fields", this.getFields());
        }

        return ind;
    }


}
