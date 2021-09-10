package com.uxsino.leaderview.model.monitor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.uxsino.commons.model.BaseNeClass;
import com.uxsino.commons.model.NeClass;
import com.uxsino.commons.model.RunStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * ********************************************************** 管理对象的实体类
 * 
 * 
 * @version iSoc Service Platform, 2015-3-6
 ***********************************************************
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "资源记录类")
public class NetworkEntity {

    @ApiModelProperty(value = "资源ID")
    private String id;

    @ApiModelProperty(value = "名称")
    @Size(max = 255, message = "名称最大长度255")
    private String name;

    @ApiModelProperty(value = "资产的编号")
    private Long assetId; // 资产的编号

    @ApiModelProperty(value = "宿主机ID")
    @Size(max = 255, message = "宿主机ID最大长度255")
    private String hostId; // 所在主机编号，独立软件需要安装在主机上才可使用，如数据库等。

    @ApiModelProperty(value = "资源型号")
    @Size(max = 255, message = "model最大长度255")
    private String model; // 监控对象型号

    @ApiModelProperty(value = "IP")
    @Pattern(regexp = "^([0-9]{1,3}\\.){3}[0-9]{1,3}$", message = "IP地址格式错误")
    private String ip;

    @Enumerated(EnumType.STRING)
    @ApiModelProperty(value = "资源类型")
    private NeClass neClass;

    @ApiModelProperty(value = "是否由资源统一管理")
    private Boolean sourceManage;// 是否由资源统一管理

    @ApiModelProperty(value = "厂商")
    @Size(max = 255, message = "厂商最大长度255")
    private String vendorId;

    @ApiModelProperty(value = "系列")
    @Size(max = 255, message = "系列最大长度255")
    private String series;

    @ApiModelProperty(value = "版本号")
    @Size(max = 255, message = "版本号最大长度255")
    private String version;

    @Type(type = "jsonb")
    @ApiModelProperty(value = "支持的协议")
    private JSONObject protocols; // 监控对象支持的协议，格式{"snmp":"configured","ssh":"unconfigured"}

    @Column(length = 2000)
    @ApiModelProperty(value = "描述")
    @Size(max = 2000, message = "描述最大长度2000")
    private String description; // 对象的描述

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(value = "运行状态")
    @NotNull(message = "运行状态不能为空")
    private RunStatus runStatus; // 对象的运行状态

    @ApiModelProperty(value = "最新检测时间")
    private Date patrolTime; // 最新检测时间

    @ApiModelProperty(value = "监控状态；true-监控中，false-未监控")
    private boolean monitoring; // 监控状态，true：监控中，false：未监控

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(value = "管理状态")
    @NotNull(message = "管理状态不能为空")
    private ManageStatus manageStatus; // 对象的管理状态

    @ApiModelProperty(value = "上线日期")
    private Date launchDate; // 对象的上线日期

    @ApiModelProperty(value = "所处位置")
    @Size(max = 255, message = "所处位置最大长度255")
    private String neLocation; // 对象的位置（网元位置）

    @ApiModelProperty(value = "域ID")
    private Long domainId; // 对象的所属域ID

    @ApiModelProperty(value = "所属网域")
    private Long netDomainId; // 所属网域

    @ApiModelProperty(value = "宿主机类型")
    @Size(max = 255, message = "宿主机类型最大长度255")
    private String hostType; // apache所属主机类型

    @ApiModelProperty(value = "下线时间")
    private Date abolishDate; // 下线时间

    @ApiModelProperty(value = "所属采集器")
    @Size(max = 255, message = "采集器ID最大长度255")
    private String collectorId; // 所属巡检号

    @Enumerated(EnumType.STRING)
    @ApiModelProperty(value = "父类型")
    private BaseNeClass baseNeClass;

    @Type(type = "jsonb")
    @ApiModelProperty(value = "配置的指标；格式[\"cpu_usage\",\"sw_run_entry\"]，为空时表示全部")
    private JSONArray indicators;// 监控对象配置的指标，格式["cpu_usage","sw_run_entry"]，为空时表示全部

    @ApiModelProperty(value = "上级ID（虚拟主机的上级虚拟化平台资源的ID）")
    @Size(max = 255, message = "parentId最大长度255")
    private String parentId; // 上级ID（虚拟主机的上级虚拟化平台资源的ID）

    @ApiModelProperty(value = "健康度")
    private Integer health;

    public NetworkEntity(String id, String name, String ip, String runstatus, String neClass, Date patrolTime) {
        this.id = id;
        this.name = name;
        this.ip = ip;
        this.runStatus = RunStatus.valueOf(runstatus);
        this.neClass = NeClass.valueOf(neClass);
        this.patrolTime = patrolTime;
    }

    public List<String> getUsableProtocolNames() {
        List<String> protocolNames = new ArrayList<>();
        if (this.protocols != null) {
            Set<String> set = protocols.keySet();
            set.forEach(protocol -> {
                if ("configured".equals(protocols.getString(protocol))) {
                    protocolNames.add(protocol.toUpperCase());
                }
            });
        }
        return protocolNames;
    }

}
