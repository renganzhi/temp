package com.uxsino.leaderview.model.business;

import com.uxsino.commons.utils.StringUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

@Data
@ApiModel(description = "业务指标查询参数")
public class BnsIndValQuery {

    private static final Logger logger = LoggerFactory.getLogger(BnsIndValQuery.class);

    @ApiModelProperty(value = "业务ID")
    private String bnsId;

    @ApiModelProperty(value = "业务ID,多个")
    private List<String> bnsIds;

    @ApiModelProperty(value = "指标名称")
    private String indicatorName;

    @ApiModelProperty(value = "开始日期")
    private Date startDate;

    @ApiModelProperty(value = "结束日期")
    private Date endDate;

    @ApiModelProperty(value = "是否分页")
    boolean pagination;


    @ApiModelProperty(value = "业务名称")
    private String bnsName;

    @ApiModelProperty(value = "业务运行状态：未监控、正常、告警、不可用")
    private RunStatus runStatus;

    @ApiModelProperty(value = "排序方式：按创建时间排序、按健康度排序、按告警数排序、按可用率排序、自定义排序")
    private SortWay sortWay;

    @ApiModelProperty(value = "排序，倒序desc")
    private String valSort;

    @ApiModelProperty(value = "展示条数")
    private Integer limit;

    public Indicator getEnumIndicatorName() {
        if (StringUtils.isNotBlank(indicatorName)){
            try {
                Indicator indicator = Indicator.valueOf(indicatorName);
                return indicator;
            }catch (Exception e){
                logger.error("指标名错误");
                return null;
            }
        }
        return null;
    }
}
