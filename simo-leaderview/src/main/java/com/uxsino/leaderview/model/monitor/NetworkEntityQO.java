package com.uxsino.leaderview.model.monitor;

import com.google.common.base.Strings;
import com.uxsino.commons.db.criteria.Criteria;
import com.uxsino.commons.model.BaseNeClass;
import com.uxsino.commons.model.NeClass;
import com.uxsino.commons.model.RunStatus;
import com.uxsino.commons.utils.TimeUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@ApiModel(description = "资源记录查询类")
public class NetworkEntityQO {

    // private static final Logger logger = LoggerFactory.getLogger(NetworkEntityQO.class);

    @ApiModelProperty(value = "查询条件-类型")
    private NeClass type; // 对象自身的类型，如数据库的类型(db2,oracle...),操作系统的类型(windows,linux...)

    @ApiModelProperty(value = "查询条件-版本")
    private String version; // 对象自身的版本

    @ApiModelProperty(value = "查询条件-资源名称")
    private String name; // 对象名称

    @ApiModelProperty(value = "查询条件-IP")
    private String ip;

    @ApiModelProperty(value = "查询条件-关键字")
    private String ipOrName;

    @ApiModelProperty(value = "查询条件-宿主机ID")
    private String hostId;

    @ApiModelProperty(value = "查询条件-运行状态")
    private RunStatus runStatus;	// 对象的运行状态

    @ApiModelProperty(value = "查询条件-管理状态")
    private ManageStatus manageStatus;	// 对象的管理状态

    @ApiModelProperty(value = "查询条件-域ID")
    private Long netDomainId; // 所属网域

    @ApiModelProperty(value = "查询条件-域ID包含")
    private Long[] domainIds;  // 所属安全域

    @ApiModelProperty(value = "查询条件-资源ID包含")
    private String itObjectIds; // 某自定义分组中所有itObject的id

    @ApiModelProperty(value = "查询条件-资源ID不包含")
    private String idsInHomeConf; // 首页关注设备中已有的所有itObject的id

    @ApiModelProperty(value = "查询条件-父类型")
    private BaseNeClass baseNeClass;   // 对象自身的父类型：主机\网络\数据库...

    @ApiModelProperty(value = "查询条件-上线时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date launchDate;   // 资源上线时间

    @ApiModelProperty(value = "查询条件-是否被监控")
    private boolean isMonitoring; // 是否被监控

    @ApiModelProperty(value = "查询条件-是否由资源同一管理")
    private Boolean sourceManage = Boolean.TRUE;

    @ApiModelProperty(value = "查询条件-父类型包含")
    private String baseNeclasses;

    @ApiModelProperty(value = "查询条件-类型包含")
    private String neClasses;

    @ApiModelProperty(value = "查询条件-资源ID包含")
    private String neIds;

    /*@ApiModelProperty(value = "查询条件-资源ID不包含")
    private String customNeIds;*/

    @ApiModelProperty(value = "查询条件-类型包含")
    private String customNeClasses;

    /*@ApiModelProperty(value = "查询条件-资源ID不包含")
    private String notInSubnetTopoNeIds;*/

    @ApiModelProperty(value = "查询条件-资源ID不包含")
    private String idNotIn;// ID 不包括 TODO 请根据需要将对应的查询字段做统一的调整， customNeIds neIds idsInHomeConf 等

    @ApiModelProperty(value = "查询条件-软硬件区分")
    private Boolean hardOrSoft;// 只查询软件资源或者物理资源

    @ApiModelProperty(value = "查询条件-协议包含")
    private String protocols;// 支持的协议， 字符串形式，以逗号分隔

    @ApiModelProperty(value = "查询条件-分组")
    private String groupType; // 分组

    @ApiModelProperty(value = "查询条件-分组包含")
    private String groupTypes; // 多个分组查询条件，以英文逗号隔开

    @ApiModelProperty(value = "查询条件-排序")
    private String orderBy = "";	   // 排序 不指定排序字段，默认情况取消排序字段

    public String createCountQuery() {
        return "select count(o) " + createSearchQuery();
    }

    /**
     * 模糊检索
     * @return
     */
    public String createSearchQuery() {
        StringBuffer queryString = new StringBuffer(" from NetworkEntity o where 1=1");

        if (sourceManage != null) {
            queryString.append(" and o.sourceManage=").append(sourceManage);
        }
        if (!StringUtils.isBlank(name)) {
            queryString.append(" and lower(o.name) like '").append(Criteria.escapeLike(name.toLowerCase())).append("'");
        }
        if (!StringUtils.isBlank(ip)) {
            queryString.append(" and lower(o.ip) like '").append(Criteria.escapeLike(ip.toLowerCase())).append("'");
        }
        if (!StringUtils.isBlank(ipOrName)) {
            queryString.append(" and (lower(o.ip) like '").append(Criteria.escapeLike(ipOrName.toLowerCase())).append("'")
                .append("or lower(o.name) like '").append(Criteria.escapeLike(ipOrName.toLowerCase())).append("')");
        }
        if (type != null) {
            queryString.append(" and o.neClass='").append(type).append("'");
        } /* else if (baseClass != null) {
             queryString.append(" and o.neClass in(" + baseClass.getNeClassString() + ")");
          }*/
        if (version != null) {
            queryString.append(" and o.version='").append(version).append("'");
        }
        if (manageStatus != null) {
            queryString.append(" and o.manageStatus='").append(manageStatus.toString()).append("'");
        } else {
            queryString.append(" and o.manageStatus <> '").append(ManageStatus.Delected.toString()).append("'");
        }
        queryString.append(" and (o.domainId is null or o.domainId != 0)");
        queryString.append(" and (o.netDomainId is null or o.netDomainId != 0)");
        if (runStatus != null) {
            queryString.append(" and o.runStatus='").append(runStatus.toString()).append("'");
        }
        if (netDomainId != null && netDomainId != 0) {
            queryString.append(" and o.netDomainId=").append(netDomainId);
        }
        if (domainIds != null && domainIds.length != 0) {
            queryString.append(" and o.domainId in(").append(StringUtils.join(domainIds, ",")).append(")");
        }
        if (StringUtils.isNotBlank(hostId)) {
            queryString.append(" and o.hostId='").append(hostId).append("'");
        }
        if (!StringUtils.isBlank(itObjectIds)) {
            queryString.append(" and o.id in (")
                .append(Arrays.stream(itObjectIds.split(",")).collect(Collectors.joining("','", "'", "'"))).append(")");
        }
        if (launchDate != null) {
            queryString.append(" and o.launchDate < '").append(TimeUtils.formatTime(launchDate)).append("'");
        }
        if (null != baseNeClass) {
            queryString.append(" and o.neClass in (")
                .append(
                    StringUtils.isNotBlank(baseNeClass.getNeClassString()) ? baseNeClass.getNeClassString() : "'null'")
                .append(")");
        }
        if (isMonitoring) {
            queryString.append(" and o.monitoring=true");
        }
        if (!StringUtils.isBlank(idsInHomeConf)) {
            queryString.append(" and o.id not in (")
                .append(Arrays.stream(idsInHomeConf.split(",")).collect(Collectors.joining("','", "'", "'")))
                .append(")");
        }

        if (!StringUtils.isBlank(baseNeclasses) || !StringUtils.isBlank(neClasses) || !StringUtils.isBlank(neIds)) {
            queryString.append(" and ( 1!=1 ");
            if (!StringUtils.isBlank(neIds)) {
                queryString.append(" or o.id in (")
                    .append(Arrays.stream(neIds.split(",")).collect(Collectors.joining("','", "'", "'"))).append(")");
            } else if (!StringUtils.isBlank(neClasses)) {
                queryString.append(" or o.neClass  in (").append(Arrays.stream(neClasses.split(",")).collect(Collectors.joining("','", "'", "'"))).append(")");
            } else if (!StringUtils.isBlank(baseNeclasses)) {
                queryString.append(" or o.baseNeClass  in (").append(baseNeclasses).append(")");
            }
            queryString.append(" )");
        }
        // 报表-报表配置中自定义分组中资源ids
        /*if (!StringUtils.isBlank(customNeIds)) {
            queryString.append(" and o.id not in (")
                .append(Arrays.stream(customNeIds.split(",")).collect(Collectors.joining("','", "'", "'"))).append(")");
        }*/
        // 报表-报表配置中资源类型
        if (!StringUtils.isBlank(customNeClasses)) {
            queryString.append(" and o.neClass  in (").append(customNeClasses).append(")");
        }

        // 排除掉不查询的ID
        if (!Strings.isNullOrEmpty(this.idNotIn)) {
            List<String> ids = Arrays.asList(this.idNotIn.trim().split(","));
            if (!ids.isEmpty()) {
                queryString.append(" and o.id not in (")
                    .append(ids.stream().collect(Collectors.joining("','", "'", "'"))).append(")");
            }
        }

        // 只查询软件/硬件
        if (this.hardOrSoft != null) {
            queryString.append(" and o.neClass  in (").append(BaseNeClass.getNeClass(this.hardOrSoft).stream()
                .map(Enum::name).collect(Collectors.joining("','", "'", "'"))).append(")");
        }

        if (!Strings.isNullOrEmpty(this.protocols)) {
            queryString.append(" and o.neClass  in (")
                .append(BaseNeClass.getNeClassByProtocols(this.protocols.split(",")).stream().map(Enum::name)
                    .collect(Collectors.joining("','", "'", "'")))
                .append(")");
        }
        /*if (null != objType) {
            switch (objType) {
            case 1:
                queryString.append(" and o.objectType = 'Host' ");
                break;
            case 2:
                queryString.append(" and o.objectType in ('Firewall', 'AP', 'Switch', 'Router') ");
                break;
            case 3:
                queryString.append(" and o.objectType in ('LoadBalancing', 'Backup', 'Database', 'Rac', 'Server') ");
                break;
            case 4:
                queryString.append(" and o.objectType in ('Storage', 'TapeLibrary') ");
                break;
            }
        }*/

        return queryString.toString();
    }

    /**
     * 精确查询
     * @return
     */
    public String createFindQuery() {
        StringBuffer queryString = new StringBuffer(" from NetworkEntity o where 1=1");
        if (sourceManage != null) {
            queryString.append(" and o.sourceManage=").append(sourceManage);
        }
        // 此处针对自定义策略设置监控vmware类型能够选择其资源，sourceManage=false类型
        if (null != type && !type.equals(NeClass.vmware)) {
            if (sourceManage != null) {
                queryString.append(" and o.sourceManage=").append(sourceManage);
            }
        }

        if (!StringUtils.isBlank(name)) {
            queryString.append(" and o.name='").append(name.replaceAll("_", "\\\\_")).append("'");
        }
        if (!StringUtils.isBlank(ip)) {
            queryString.append(" and o.ip='").append(ip.replaceAll("_", "\\\\_")).append("'");
        }
        if (type != null) {
            queryString.append(" and o.neClass='").append(type).append("'");
        } /* else if (baseClass != null) {
             queryString.append(" and o.neClass in(" + baseClass.getNeClassString() + ")");
          }*/
        if (version != null) {
            queryString.append(" and o.version='").append(version).append("'");
        }
        if (manageStatus != null) {
            queryString.append(" and o.manageStatus='").append(manageStatus.toString()).append("'");
        } else {
            queryString.append(" and o.manageStatus <> '").append(ManageStatus.Delected.toString()).append("'");
        }
        if (runStatus != null) {
            queryString.append(" and o.runStatus='").append(runStatus.toString()).append("'");
        }
        if (netDomainId != null && netDomainId != 0) {
            queryString.append(" and o.netDomainId=").append(netDomainId);
        }
        if (domainIds != null && domainIds.length != 0) {
            queryString.append(" and o.domainId in(").append(StringUtils.join(domainIds, ",")).append(")");
        }
        if (StringUtils.isNotBlank(hostId)) {
            queryString.append(" and o.hostId='").append(hostId).append("'");
        }
        if (!StringUtils.isBlank(itObjectIds)) {
            queryString.append(" and o.id in (")
                .append(Arrays.stream(itObjectIds.split(",")).collect(Collectors.joining("','", "'", "'"))).append(")");
        }
        if (launchDate != null) {
            queryString.append(" and o.launchDate < '").append(TimeUtils.formatTime(launchDate)).append("'");
        }
        if (null != baseNeClass) {
            queryString.append(" and o.neClass in (")
                .append(
                    StringUtils.isNotBlank(baseNeClass.getNeClassString()) ? baseNeClass.getNeClassString() : "'null'")
                .append(")");
        }
        if (isMonitoring) {
            queryString.append(" and o.monitoring=true");
        }
        if (!StringUtils.isBlank(idsInHomeConf)) {
            queryString.append(" and o.id not in (")
                .append(Arrays.stream(idsInHomeConf.split(",")).collect(Collectors.joining("','", "'", "'")))
                .append(")");
        }

        if (!StringUtils.isBlank(baseNeclasses) || !StringUtils.isBlank(neClasses) || !StringUtils.isBlank(neIds)) {
            queryString.append(" and ( 1<>1 ");
            if (!StringUtils.isBlank(neIds)) {
                queryString.append(" or o.id  in (")
                    .append(Arrays.stream(neIds.split(",")).collect(Collectors.joining("','", "'", "'"))).append(")");
            } else if (!StringUtils.isBlank(neClasses)) {
                queryString.append(" or o.neClass  in (").append(Arrays.stream(neClasses.split(",")).collect(Collectors.joining("','", "'", "'"))).append(")");
            } else if (!StringUtils.isBlank(baseNeclasses)) {
                queryString.append(" or o.baseNeClass  in (").append(baseNeclasses).append(")");
            }
            queryString.append(" )");
        }

        // 不属于子网拓扑id
        /* if (!StringUtils.isBlank(notInSubnetTopoNeIds)) {
            queryString.append(" and o.id not in (")
                .append(Arrays.stream(notInSubnetTopoNeIds.split(",")).collect(Collectors.joining("','", "'", "'")))
                .append(")");
        }*/

        // 排除掉不查询的ID
        if (!Strings.isNullOrEmpty(this.idNotIn)) {
            List<String> ids = Arrays.asList(this.idNotIn.trim().split(","));
            if (!ids.isEmpty()) {
                queryString.append(" and o.id not in (")
                    .append(ids.stream().collect(Collectors.joining("','", "'", "'"))).append(")");
            }
        }

        // 只查询软件/硬件
        if (this.hardOrSoft != null) {
            queryString.append(" and o.neClass  in (").append(BaseNeClass.getNeClass(this.hardOrSoft).stream()
                .map(Enum::name).collect(Collectors.joining("','", "'", "'"))).append(")");
        }

        if (!Strings.isNullOrEmpty(this.protocols)) {
            queryString.append(" and o.neClass  in (")
                .append(BaseNeClass.getNeClassByProtocols(this.protocols.split(",")).stream().map(Enum::name)
                    .collect(Collectors.joining("','", "'", "'")))
                .append(")");
        }

        if (!StringUtils.isBlank(orderBy)) {
            queryString.append(" order by ").append(orderBy);
        }

        return queryString.toString();
    }

    public String createSearchPingQuery() {
        StringBuffer queryString = new StringBuffer(" from NetworkEntity o where 1=1");
        if (sourceManage != null) {
            queryString.append(" and o.sourceManage=" + sourceManage);
        }
        if (!StringUtils.isBlank(name)) {
            queryString.append(" and lower(o.name) like '" + Criteria.escapeLike(name.toLowerCase()) + "'");
        }
        if (!StringUtils.isBlank(ip)) {
            queryString.append(" and lower(o.ip) like '" + Criteria.escapeLike(ip.toLowerCase()) + "'");
        }
        if (type != null) {
            queryString.append(" and o.neClass='" + type + "'");
        } /* else if (baseClass != null) {
             queryString.append(" and o.neClass in(" + baseClass.getNeClassString() + ")");
          }*/
        if (version != null) {
            queryString.append(" and o.version='" + version + "'");
        }
        if (manageStatus != null) {
            queryString.append(" and o.manageStatus='" + manageStatus.toString() + "'");
        } else {
            queryString.append(" and o.manageStatus <> '" + ManageStatus.Delected.toString() + "'");
        }
        if (runStatus != null) {
            queryString.append(" and o.runStatus='" + runStatus.toString() + "'");
        }
        if (netDomainId != null && netDomainId != 0) {
            queryString.append(" and o.netDomainId=" + netDomainId);
        }
        if (domainIds != null && domainIds.length != 0) {
            queryString.append(" and o.domainId in(" + StringUtils.join(domainIds, ",") + ")");
        }
        if (StringUtils.isNotBlank(hostId)) {
            queryString.append(" and o.hostId='" + hostId + "'");
        }
        if (!StringUtils.isBlank(itObjectIds)) {
            queryString.append(" and o.id in (" + Arrays.asList(itObjectIds.split(",")).stream().map(id -> id)
                .collect(Collectors.joining("','", "'", "'")) + ")");
        }
        if (launchDate != null) {
            queryString.append(" and o.launchDate < '" + TimeUtils.formatTime(launchDate) + "'");
        }
        if (null != baseNeClass) {
            queryString.append(" and o.neClass in (" + (StringUtils.isNotBlank(baseNeClass.getNeClassString())
                    ? baseNeClass.getNeClassString() : "'null'") + ")");
        }
        if (isMonitoring) {
            queryString.append(" and o.monitoring=true");
        }
        if (!StringUtils.isBlank(idsInHomeConf)) {
            queryString.append(" and o.id not in (" + Arrays.asList(idsInHomeConf.split(",")).stream().map(id -> id)
                .collect(Collectors.joining("','", "'", "'")) + ")");
        }

        // 排除掉不查询的ID
        if (!Strings.isNullOrEmpty(this.idNotIn)) {
            List<String> ids = Arrays.asList(this.idNotIn.trim().split(","));
            if (!ids.isEmpty()) {
                queryString.append(" and o.id not in (")
                    .append(ids.stream().collect(Collectors.joining("','", "'", "'"))).append(")");
            }
        }

        // 只查询软件/硬件
        if (this.hardOrSoft != null) {
            queryString.append(" and o.neClass  in (").append(BaseNeClass.getNeClass(this.hardOrSoft).stream()
                .map(Enum::name).collect(Collectors.joining("','", "'", "'"))).append(")");
        }

        if (!Strings.isNullOrEmpty(this.protocols)) {
            queryString.append(" and o.neClass  in (")
                .append(BaseNeClass.getNeClassByProtocols(this.protocols.split(",")).stream().map(Enum::name)
                    .collect(Collectors.joining("','", "'", "'")))
                .append(") ");
        }
        /*if (null != objType) {
            switch (objType) {
            case 1:
                queryString.append(" and o.objectType = 'Host' ");
                break;
            case 2:
                queryString.append(" and o.objectType in ('Firewall', 'AP', 'Switch', 'Router') ");
                break;
            case 3:
                queryString.append(" and o.objectType in ('LoadBalancing', 'Backup', 'Database', 'Rac', 'Server') ");
                break;
            case 4:
                queryString.append(" and o.objectType in ('Storage', 'TapeLibrary') ");
                break;
            }
        }*/

        return queryString.toString();
    }

    /**
     * 模糊Or检索（用于同一个输入框既能按ip也能按name检索）
     * @return
     */
    public String createOrSearchQuery() {
        StringBuffer queryString = new StringBuffer(" from NetworkEntity o where 1=1");
        if (type != null) {
            queryString.append(" and o.neClass='").append(type).append("'");
        }
        if (!StringUtils.isBlank(neClasses)) {
            queryString.append(" and o.neClass  in (").append(neClasses).append(")");
        }
        if (domainIds != null && domainIds.length != 0) {
            queryString.append(" and o.domainId in (").append(StringUtils.join(domainIds, ",")).append(")");
        }
        if (manageStatus != null) {
            queryString.append(" and o.manageStatus='").append(manageStatus.toString()).append("'");
        } else {
            queryString.append(" and o.manageStatus <> '").append(ManageStatus.Delected.toString()).append("'");
        }
        if (!StringUtils.isBlank(name) || !StringUtils.isBlank(ip)) {
            queryString.append(" and ( 1!=1 ");
            if (!StringUtils.isBlank(name)) {
                queryString.append(" or lower(o.name) like '").append(Criteria.escapeLike(name.toLowerCase())).append("'");
            }
            if (!StringUtils.isBlank(ip)) {
                queryString.append(" or lower(o.ip) like '").append(Criteria.escapeLike(ip.toLowerCase())).append("'");
            }
            queryString.append(" )");
        }
        return queryString.toString();
    }



}
