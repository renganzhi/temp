package com.uxsino.leaderview.model.monitor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.uxsino.commons.model.JsonModel;
import com.uxsino.leaderview.model.monitor.IndicatorTable;
import com.uxsino.leaderview.model.monitor.NetworkEntity;
import com.uxsino.leaderview.utils.MonitorUtils;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.ObjectUtils;

import static com.uxsino.leaderview.utils.MonitorUtils.empObj;

@Data
public class FieldModel {
    NetworkEntity ne;
    IndicatorTable indicator;
    String component;
    String field;

    // 属性字段的JSON
    JSONObject fieldLabel;

    public JSONObject getFieldLabel(){
        if (!ObjectUtils.isEmpty(this.fieldLabel)){
            return this.fieldLabel;
        }
        if (!ObjectUtils.isEmpty(this.indicator) && !ObjectUtils.isEmpty(this.field)){
            if (MonitorUtils.validHasFields(this.indicator)) {
                // 查找属性的label
                JSONArray fields = this.indicator.getFields();
                fields = MonitorUtils.filter(fields, o -> field.equalsIgnoreCase(o.getString("name")));
                if (CollectionUtils.isEmpty(fields)) {
                    return null;
                }
                this.fieldLabel = fields.getJSONObject(0);
                return this.fieldLabel;
            }
        }
        return null;
    }
}
