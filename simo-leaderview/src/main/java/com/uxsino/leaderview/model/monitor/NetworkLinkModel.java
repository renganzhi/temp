package com.uxsino.leaderview.model.monitor;

import com.uxsino.commons.db.criteria.Criteria;
import com.uxsino.commons.db.model.PageModel;
import com.uxsino.commons.model.InterfaceStatus;
import com.uxsino.commons.model.NeClass;
import com.uxsino.commons.model.RunStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "链路信息")
public class NetworkLinkModel extends PageModel {

    /** 链路ID */
    @ApiModelProperty(value = "链路ID")
    private String id; // 链路ID

    /** 源监控节点资源ID */
    @ApiModelProperty(value = "源端资源ID")
    private String sourceId;

    /** 目标监控节点资源ID */
    @ApiModelProperty(value = "目的端资源ID")
    private String targetId;

    @ApiModelProperty(value = "源端资源接口序号")
    /** 源监控对象接口序号 */
    private String sourceIfIndex;

    /** 目标监控对象 接口序号 */
    @ApiModelProperty(value = "目的端资源接口序号")
    private String targetIfIndex;

    /** 源监控对象接口名称 */
    @ApiModelProperty(value = "源端资源接口名称")
    private String sourceIfName;

    /** 目标监控对象接口名称 */
    @ApiModelProperty(value = "目的端资源接口名称")
    private String targetIfName;

    /** 源端资源接口状态 */
    @ApiModelProperty(value = "源端资源接口状态")
    private InterfaceStatus sourceIfStatus;

    /** 目的端资源接口状态 */
    @ApiModelProperty(value = "目的端资源接口状态")
    private InterfaceStatus targetIfStatus;

    /** 带宽,单位Mbps */
    @ApiModelProperty(value = "带宽")
    private Long speed;

    /** 前端手动设置的带宽,单位Mbps */
    @ApiModelProperty(value = "前端手动设置的带宽")
    private Long handSpeed;

    /** 带宽利用率((in + out)Mb/speed) */
    @ApiModelProperty(value = "带宽利用率")
    private Double speedUsage;

    /** 上行带宽（in）,单位bps */
    @ApiModelProperty(value = "上行带宽")
    private Double upBps;

    /** 下行带宽(out),单位bps */
    @ApiModelProperty(value = "下行带宽")
    private Double downBps;

    /** 流量(求和) */
    @ApiModelProperty(value = "流量")
    private Double traffic;

    /** 告警级别 */
    @ApiModelProperty(value = "告警级别")
    private String alertLevel;

    /** 链路状态 */
    @ApiModelProperty(value = "链路状态")
    private NetworkLinkStatus linkStatus;

    /** 最新检测时间 */
    @ApiModelProperty(value = "最新检测时间")
    private Date lastestUpdateTime;

    /** 取值接口: 0源端，1目的端 */
    @ApiModelProperty(value = "取值接口")
    private Integer valueInterface;

    /** 源监控节点ip */
    @ApiModelProperty(value = "源监控节点ip")
    private String sourceIp;

    /** 源监控节点运行状态 */
    @ApiModelProperty(value = "源监控节点运行状态")
    private RunStatus sourceNeRunStatus;

    /** 源监控节点监控状态 */
    @ApiModelProperty(value = "源监控节点监控状态")
    private boolean sourceNeMonitoring;

    /** 源监控节点是否能进入监控视图 */
    private boolean sourceIsHardware = true;

    /** 目标监控节点ip */
    @ApiModelProperty(value = "目标监控节点ip")
    private String targetIp;

    /** 目标监控节点运行状态 */
    @ApiModelProperty(value = "目标监控节点运行状态")
    private RunStatus targetNeRunStatus;

    /** 目标监控节点监控状态 */
    @ApiModelProperty(value = "目标监控节点监控状态")
    private boolean targetNeMonitoring;

    /** 目标监控节点是否能进入监控视图 */
    private boolean targetIsHardware = true;

    /** 查询条件-关键字 */
    @ApiModelProperty(value = "查询条件-关键字")
    private String keyword;

    private List<String> networkLinkIds;

    private List<String> neIds;

    @ApiModelProperty(value = "丢包数")
    private Double packetLoss;

    @ApiModelProperty(value = "丢包率")
    private Double packetLossRate;

    @ApiModelProperty("错包数")
    private Double packetWrong;

    @ApiModelProperty(value = "广播包数")
    private Double packetBroadcast;

    @ApiModelProperty(value = "排序")
    private String sortField;

    @ApiModelProperty(value = "排序方式")
    private String desc;

}
