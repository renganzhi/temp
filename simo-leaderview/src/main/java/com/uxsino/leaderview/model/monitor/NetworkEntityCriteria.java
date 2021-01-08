package com.uxsino.leaderview.model.monitor;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.uxsino.commons.db.criteria.Criteria;
import com.uxsino.commons.model.BaseNeClass;
import com.uxsino.commons.model.NeClass;
import com.uxsino.commons.model.RunStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 网元检索
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "资源记录查询类")
public class NetworkEntityCriteria extends Criteria<NetworkEntity> {

    @ApiModelProperty(value = "查询条件-关键字")
    private String keyword;// 匹配关键字

    @ApiModelProperty(value = "查询条件-资源名称")
    private String name; // 模糊查询

    @ApiModelProperty(value = "查询条件-资源类型")
    private NeClass neClass;// 资源类型

    @ApiModelProperty(value = "查询条件-类型包含")
    private List<NeClass> neClasses;// 资源类型

    @ApiModelProperty(value = "查询条件-IP")
    private String ip;

    @ApiModelProperty(value = "查询条件-IP包含")
    private List<String> ips;

    @ApiModelProperty(value = "查询条件-主机ID")
    private String hostId;// 主机ID

    @ApiModelProperty(value = "查询条件-主机ID包含")
    private List<String> hostIdIn;// 主机ID包含

    @ApiModelProperty(value = "查询条件-域ID")
    private Long domainId;// 安全域ID

    @ApiModelProperty(value = "查询条件-域范围包含")
    private List<Long> domainIds;// 多个安全域

    @ApiModelProperty(value = "查询条件-资源ID")
    private String id;// 查询某一个数据

    @ApiModelProperty(value = "查询条件-资源ID包含")
    private List<String> ids;// 包含的ID

    @ApiModelProperty(value = "查询条件-不包含的ID")
    private List<String> idNotIn;// 不包含的ID

    @ApiModelProperty(value = "查询条件-协议")
    private String protocol;// 某一种协议

    @ApiModelProperty(value = "查询条件-协议包含")
    private List<String> protocols;// 多个协议

    @ApiModelProperty(value = "查询条件-资源类型的分组ID")
    private Long groupTypeId;// 资源类型的分组

    @ApiModelProperty(value = "查询条件-资源类型的分组ID包含")
    private List<Long> groupTypeIds;// 资源类型的分组

    @ApiModelProperty(value = "查询条件-匹配自定义资源类型分组 groupType 字段")
    private String groupType;// 匹配自定义资源类型分组 groupType 字段

    @ApiModelProperty(value = "查询条件-是否被监控")
    private Boolean monitoring;// 是否被监控

    @ApiModelProperty(value = "查询条件-是否由资源同一管理")
    private Boolean sourceManage = Boolean.TRUE;// 是否由资源统一管理

    @ApiModelProperty(value = "查询条件-管理状态")
    private ManageStatus manageStatus;// // 对象的管理状态

    @ApiModelProperty(value = "查询条件-管理状态包含")
    private List<ManageStatus> manageStatusIn;// 对象的管理状态

    @ApiModelProperty(value = "查询条件-管理状态,默认不显示删除的资源")
    private List<ManageStatus> manageStatusNotIn = Lists.newArrayList(ManageStatus.Delected);// 对象的管理状态,默认不显示删除的资源

    @ApiModelProperty(value = "查询条件-运行状态")
    private RunStatus runStatus;// 运行状态

    @ApiModelProperty(value = "查询条件-运行状态包含")
    private List<RunStatus> runStatusIn;// 运行状态包含

    @ApiModelProperty(value = "查询条件-特殊的查询条件")
    private String querySpec;

    @ApiModelProperty(value = "查询条件-资源分组ID")
    private Long groupId;

    @ApiModelProperty(value = "查询条件-是否限定为登录用户的权限，默认为true")
    private boolean limit = true;

    @ApiModelProperty(value = "查询条件-是否查询资源健康度，默认为false")
    private boolean healthReturn;

    public NetworkEntityCriteria() {
        super(NetworkEntity.class);
        setNativeQuery(true);
    }

    private String sql() {
        StringBuilder sql = new StringBuilder(" FROM simo_monitor_network_entity o ");
        sql.append(" LEFT JOIN simo_monitor_ne_multi_ip m ON m.ne_id = o.id ");

        sql.append(" WHERE 1=1 ");
        if (StringUtils.isNotBlank(this.keyword)) {
            sql.append(
                " AND ( lower(o.name) LIKE :keyword OR lower(o.ip) LIKE :keyword OR lower(m.ip) LIKE :keyword OR lower(o.series) LIKE :keyword ) ");
        }
        if (StringUtils.isNotBlank(this.name)) {
            sql.append(" And lower(o.name) LIKE :name");
        }
        if (this.neClass != null) {
            sql.append(" AND o.ne_class =:neClass ");
        }
        if (this.neClasses != null && !this.neClasses.isEmpty()) {
            sql.append(" AND o.ne_class IN :neClasses ");
        }

        if (StringUtils.isNotBlank(this.ip)) {
            sql.append(" AND (lower(o.ip) like :ip OR lower(m.ip) LIKE :ip) ");
        }

        if (this.ips != null && !this.ips.isEmpty()) {
            sql.append(" AND (o.ip IN :ips OR m.ip IN :ips ) ");
        }

        if (StringUtils.isNotBlank(this.hostId)) {
            sql.append(" AND o.host_id = :hostId ");
        }

        if (this.hostIdIn != null && !hostIdIn.isEmpty()) {
            sql.append(" AND o.host_id in :hostIdIn ");
        }

        if (this.domainId != null) {
            sql.append(" AND o.domain_id =:domainId ");
        }

        if (this.domainIds != null && !this.domainIds.isEmpty()) {
            sql.append(" AND o.domain_id IN :domainIds ");
        }

        if (StringUtils.isNotBlank(this.id)) {
            sql.append(" AND o.id = :id");
        }

        if (this.ids != null && !this.ids.isEmpty()) {
            sql.append(" AND o.id IN :ids ");
        }

        if (this.idNotIn != null && !this.idNotIn.isEmpty()) {
            sql.append(" AND o.id NOT IN :idNotIn ");
        }

        if (StringUtils.isNotBlank(this.protocol)) {
            sql.append(" AND o.ne_class IN (").append(BaseNeClass.getNeClassByProtocols(this.protocol).stream()
                .map(nc -> nc.name()).collect(Collectors.joining("','", "'", "'"))).append(") ");
        }

        if (this.protocols != null && !this.protocols.isEmpty()) {
            sql.append(" AND o.ne_class IN (")
                .append(BaseNeClass.getNeClassByProtocols(this.protocols.toArray(new String[this.protocols.size()]))
                    .stream().map(nc -> nc.name()).collect(Collectors.joining("','", "'", "'")))
                .append(") ");
        }

        if (this.monitoring != null) {
            sql.append(" AND o.monitoring = :monitoring ");
        }
        if (this.sourceManage != null) {
            sql.append(" And o.source_manage = :sourceManage ");
        }

        if (this.manageStatus != null) {
            sql.append(" AND o.manage_status =:manageStatus ");
        }
        if (this.manageStatusIn != null && !this.manageStatusIn.isEmpty()) {
            sql.append(" AND o.manage_status IN :manageStatusIn ");
        }
        if (this.manageStatusNotIn != null && !this.manageStatusNotIn.isEmpty()) {
            sql.append(" AND o.manage_status NOT IN :manageStatusNotIn ");
        }

        if (this.runStatus != null) {
            sql.append(" AND o.run_status =:runStatus");
        }

        if (this.runStatusIn != null && !this.runStatusIn.isEmpty()) {
            sql.append(" AND o.run_status IN :runStatusIn ");
        }

        if (this.groupTypeId != null) {
            sql.append(
                " AND o.ne_class IN ( SELECT jsonb_object_keys(ng.ne_classes)  FROM simo_monitor_custom_neclass_group ng WHERE ng.id = :groupTypeId ) ");
        }

        if (StringUtils.isNotBlank(this.groupType)) {
            sql.append(
                " AND o.ne_class IN ( SELECT jsonb_object_keys(ng.ne_classes)  FROM simo_monitor_custom_neclass_group ng WHERE ng.group_type = :groupType ) ");
        }

        if (this.groupTypeIds != null && !this.groupTypeIds.isEmpty()) {
            sql.append(
                " AND o.ne_class IN ( SELECT jsonb_object_keys(ng.ne_classes)  FROM simo_monitor_custom_neclass_group ng WHERE ng.id IN :groupTypeIds ) ");
        }

        if (StringUtils.isNotBlank(querySpec)) {
            sql.append(" AND ").append(this.querySpec).append(" ");
        }

        return sql.toString();
    }

    @Override
    public String createQuery() {
        String hql = "SELECT DISTINCT o.* " + sql() + " ORDER BY ";
        if (Strings.isNullOrEmpty(this.getSortField())) {
            hql += " o.launch_date ";
        } else {
            hql += " o." + humpToUnderline(this.getSortField().trim()) + " ";

        }
        return hql + (Boolean.TRUE.equals(this.getAsc()) ? " ASC " : " DESC ");
    }

    @Override
    public String createCount() {
        return "SELECT COUNT(DISTINCT o.id) " + sql();
    }

    @Override
    public Map<String, Object> param() {
        Map<String, Object> param = Maps.newHashMap();

        if (StringUtils.isNotBlank(this.keyword)) {
            param.put("keyword", escapeLike(this.keyword.toLowerCase()));
        }
        if (StringUtils.isNotBlank(this.name)) {
            param.put("name", escapeLike(this.name.toLowerCase()));
        }
        if (this.neClass != null) {
            param.put("neClass", this.neClass.name());
        }
        if (this.neClasses != null && !this.neClasses.isEmpty()) {
            param.put("neClasses", this.neClasses.stream().map(status -> status.name()).collect(Collectors.toList()));
        }

        if (StringUtils.isNotBlank(this.ip)) {
            param.put("ip", escapeLike(ip.toLowerCase()));
        }

        if (this.ips != null && !this.ips.isEmpty()) {
            param.put("ips", ips);
        }

        if (StringUtils.isNotBlank(this.hostId)) {
            param.put("hostId", hostId);
        }

        if (this.hostIdIn != null && !hostIdIn.isEmpty()) {
            param.put("hostIdIn", hostIdIn);
        }

        if (this.domainId != null) {
            param.put("domainId", domainId);
        }

        if (this.domainIds != null && !this.domainIds.isEmpty()) {
            param.put("domainIds", domainIds);
        }

        if (StringUtils.isNotBlank(this.id)) {
            param.put("id", id);
        }

        if (this.ids != null && !this.ids.isEmpty()) {
            param.put("ids", ids);
        }

        if (this.idNotIn != null && !this.idNotIn.isEmpty()) {
            param.put("idNotIn", idNotIn);
        }

        /*if(!Strings.isNullOrEmpty(this.protocol)){
            sql.append(" AND o.neClass IN (").append(BaseNeClass.getNeClassByProtocols(this.protocol).stream().map(nc->nc.name()).collect(Collectors.joining("','", "'", "'"))).append(") ");
        }
        
        if(this.protocols != null && !this.protocols.isEmpty()){
            sql.append(" AND o.neClass IN (").append(BaseNeClass.getNeClassByProtocols(this.protocols.toArray(new String[this.protocols.size()])).stream().map(nc->nc.name()).collect(Collectors.joining("','", "'", "'"))).append(") ");
        }*/

        if (this.monitoring != null) {
            param.put("monitoring", monitoring);
        }

        if (this.sourceManage != null) {
            param.put("sourceManage", sourceManage);
        }

        if (this.manageStatus != null) {
            param.put("manageStatus", this.manageStatus.name());
        }
        if (this.manageStatusIn != null && !this.manageStatusIn.isEmpty()) {
            param.put("manageStatusIn",
                manageStatusIn.stream().map(status -> status.name()).collect(Collectors.toList()));
        }
        if (this.manageStatusNotIn != null && !this.manageStatusNotIn.isEmpty()) {
            param.put("manageStatusNotIn",
                manageStatusNotIn.stream().map(status -> status.name()).collect(Collectors.toList()));
        }

        if (this.runStatus != null) {
            param.put("runStatus", runStatus.name());
        }

        if (this.runStatusIn != null && !this.runStatusIn.isEmpty()) {
            param.put("runStatusIn", runStatusIn.stream().map(status -> status.name()).collect(Collectors.toList()));
        }

        if (this.groupTypeId != null) {
            param.put("groupTypeId", groupTypeId);
        }

        if (StringUtils.isNotBlank(this.groupType)) {
            param.put("groupType", groupType);
        }

        if (this.groupTypeIds != null && !this.groupTypeIds.isEmpty()) {
            param.put("groupTypeIds", groupTypeIds);
        }

        return param;
    }

    public void limit(boolean isSuper, List<Long> domains) {
        if (isSuper) {
            return;
        }
        domains.add(-1L);
        if (getDomainIds() == null) {
            setDomainIds(Lists.newArrayList());
        }
        List<Long> legal = getDomainIds().stream().filter(id -> domains.contains(id)).collect(Collectors.toList());
        if (getDomainIds().isEmpty()) {// 条件中没有查询条件
            setDomainIds(domains);
        } else {// 存在则做校验
            if (legal.isEmpty()) {// 表示用户查询的所有的域都不属于自己的
                // setDomainIds(Lists.newArrayList(-1L));//屏蔽查询结果，返回空
                // 直接返回，不存在占位条件
                setDomainId(-1L);
            } else {// 查询权限范围的Domain的数据
                setDomainIds(legal);
            }
        }
    }

}
