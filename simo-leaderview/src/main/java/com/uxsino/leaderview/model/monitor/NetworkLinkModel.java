package com.uxsino.leaderview.model.monitor;

import com.uxsino.commons.db.criteria.Criteria;
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
public class NetworkLinkModel {

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

    public NetworkLinkModel(String id, String sourceId, String targetId, String sourceIfIndex, String targetIfIndex, String sourceIfName,
                            String targetIfName, InterfaceStatus sourceIfStatus, InterfaceStatus targetIfStatus, Long speed, Long handSpeed,
                            Double speedUsage, Double upBps, Double downBps, Double traffic, String alertLevel, NetworkLinkStatus linkStatus,
                            Date lastestUpdateTime, Integer valueInterface, String sourceIp, RunStatus sourceNeRunStatus,
                            boolean sourceNeMonitoring, String targetIp, RunStatus targetNeRunStatus, boolean targetNeMonitoring) {
        this.id = id;
        this.sourceId = sourceId;
        this.targetId = targetId;
        this.sourceIfIndex = sourceIfIndex;
        this.targetIfIndex = targetIfIndex;
        this.sourceIfName = sourceIfName;
        this.targetIfName = targetIfName;
        this.sourceIfStatus = sourceIfStatus;
        this.targetIfStatus = targetIfStatus;
        this.speed = speed;
        this.handSpeed = handSpeed;
        this.speedUsage = speedUsage;
        this.upBps = upBps;
        this.downBps = downBps;
        this.traffic = traffic;
        this.alertLevel = alertLevel;
        this.linkStatus = linkStatus;
        this.lastestUpdateTime = lastestUpdateTime;
        this.valueInterface = valueInterface;
        this.sourceIp = sourceIp;
        this.sourceNeRunStatus = sourceNeRunStatus;
        this.sourceNeMonitoring = sourceNeMonitoring;
        this.targetIp = targetIp;
        this.targetNeRunStatus = targetNeRunStatus;
        this.targetNeMonitoring = targetNeMonitoring;
    }

    public Map<String, Object> createJPQL() {
        Map<String, Object> temp = new HashMap<String, Object>(2);
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuffer jpql = new StringBuffer(" from NetworkLink nl, NetworkEntity sne, NetworkEntity tne" +
                " where nl.sourceId = sne.id and nl.targetId = tne.id" +
                " and sne.manageStatus = '" + ManageStatus.Online.toString() + "' and tne.manageStatus = '" + ManageStatus.Online.toString() +
                "' and sne.neClass in (" + NeClass.getHardware() + ") and tne.neClass in (" + NeClass.getHardware() + ")");
        if (linkStatus == null) {
            jpql.append(" and nl.linkStatus <> :linkStatus");
            params.put("linkStatus", NetworkLinkStatus.Delected);
        } else {
            jpql.append(" and nl.linkStatus = :linkStatus");
            params.put("linkStatus", linkStatus);
        }
        if (networkLinkIds != null && networkLinkIds.size() > 0) {
            jpql.append(" and nl.id in :networkLinkIds");
            params.put("networkLinkIds", networkLinkIds);
        }
        if (neIds != null && neIds.size() > 0) {
            jpql.append(" and nl.sourceId in :neIds");
            jpql.append(" and nl.targetId in :neIds");
            params.put("neIds", neIds);
        }
        if (StringUtils.isNotBlank(keyword)) {
            jpql.append(" and (lower(sne.ip) like :keyword or lower(tne.ip) like :keyword or lower(nl.sourceIfName) like :keyword or lower(nl.targetIfName) like :keyword)");
            params.put("keyword", Criteria.escapeLike(keyword.toLowerCase()));
        }
        if (StringUtils.isNotBlank(sourceIp)) {
            jpql.append(" and lower(sne.ip) like :sourceIp");
            params.put("sourceIp", Criteria.escapeLike(sourceIp.toLowerCase()));
        }
        if (StringUtils.isNotBlank(targetIp)) {
            jpql.append(" and lower(tne.ip) like :targetIp");
            params.put("targetIp", Criteria.escapeLike(targetIp.toLowerCase()));
        }
        if (StringUtils.isNotBlank(sourceIfName)) {
            jpql.append(" and lower(nl.sourceIfName) like :sourceIfName");
            params.put("sourceIfName", Criteria.escapeLike(sourceIfName.toLowerCase()));
        }
        if (StringUtils.isNotBlank(targetIfName)) {
            jpql.append(" and lower(nl.targetIfName) like :targetIfName");
            params.put("targetIfName", Criteria.escapeLike(targetIfName.toLowerCase()));
        }
        temp.put("jpql", jpql.toString());
        temp.put("params", params);
        return temp;
    }

}
