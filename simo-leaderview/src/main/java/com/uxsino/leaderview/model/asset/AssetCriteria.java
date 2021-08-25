package com.uxsino.leaderview.model.asset;

import com.uxsino.commons.db.model.PageModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;

@Data
public class AssetCriteria extends PageModel {
    /**
     * 固定条件
     */
    @ApiModelProperty(value="固定查询条件",required=false)
    public ArrayList<QueryCond> fixQuery;

}
