package com.uxsino.leaderview.model;

import com.google.common.base.Strings;
import com.uxsino.commons.utils.StringUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections4.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.uxsino.leaderview.model.asset.Criteria.escapeLike;

/**
 * 扩展查询接口
 *
 */
@Data
@ApiModel(description = "部门查询类")
@EqualsAndHashCode(callSuper = false)
public class SiteOrganizationCriteria {

    private String keyword; // 根据关键字查询，机构名

    private String id;

    private List<String> ids;

    private List<String> idNotIn;

    @ApiModelProperty(value = "机构名称")
    private String name;

    @ApiModelProperty(value = "机构性质Id")
    private Long property;

    @ApiModelProperty(value = "机构性质Id,多个")
    private List<Long> propertyIn;

    @ApiModelProperty(value = "机构主管")
    private Long supervisor;

    @ApiModelProperty(value = "机构主管名称")
    private String supervisorName;

    @ApiModelProperty(value = "上级机构id")
    private String parentId;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "是否为顶级机构，只能存在一个顶级机构")
    private Boolean isTop;

    @ApiModelProperty(value = "机构状态")
    private EnableFlagEnum status = EnableFlagEnum.OK;

    @ApiModelProperty(value = "机构状态不包括")
    private EnableFlagEnum statusNot;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "返回父级（每个机构只有一个父级），id和父级全路径")
    private boolean returnParent;

    @ApiModelProperty(value = "返回所有子级，id和子级路径")
    private boolean returnAllChild;

    @ApiModelProperty(value = "是否返回机构的成员数量")
    private boolean returnMemberAmount;

    @ApiModelProperty(value = "是否包含机构所有下级,可以配合成员数量使用")
    private boolean hasDownLevel;

    @ApiModelProperty(value = "机构全路径")
    private String organPath;

    public Map<String, Object> param() {
        Map<String, Object> params = new HashMap<>();

        if (!Strings.isNullOrEmpty(keyword)) {
            params.put("keyword", escapeLike(keyword.toLowerCase()));
        }

        if (null != id) {
            params.put("id", id);
        }
        if (ids != null && !ids.isEmpty()) {
            params.put("ids", ids);
        }
        if (CollectionUtils.isNotEmpty(idNotIn)) {
            params.put("idNotIn", idNotIn);
        }

        if (StringUtils.isStrictNotEmpty(name)) {
            params.put("name", escapeLike(name.toLowerCase()));
        }
        if (null != property) {
            params.put("property", property);
        }
        if (CollectionUtils.isNotEmpty(propertyIn)) {
            params.put("propertyIn", propertyIn);
        }

        if (null != supervisor) {
            params.put("supervisor", supervisor);
        }

        if (null != parentId) {
            params.put("parentId", parentId);
        }
        if (null != isTop) {
            params.put("isTop", isTop);
        }
        if (null != status) {
            params.put("status", status.toString());
        }

        if (null != statusNot) {
            params.put("statusNot",statusNot.toString());
        }
        if (null != userId) {
            params.put("userId",userId);
        }
        return params;
    }

    public String createQuery() {
        return "select DISTINCT o.* " + jpql() + " order by o.id asc";
    }

    public String jpql() {
        String jpql = " from simo_useradmin_organization o";

        if (null != userId) {
            jpql += " INNER JOIN simo_useradmin_user_organization uo ON o.id = uo.organization_id ";
        }

        jpql += " where 1=1 ";

        if (null != userId) {
            jpql += " and uo.user_id = :userId ";
        }

        if (StringUtils.isStrictNotEmpty(keyword)) {
            jpql += " and (" +
                    " lower(o.name) like :keyword " +
                    " )";
        }

        if (null != ids && !ids.isEmpty()) {
            jpql += " and o.id IN (:ids) ";
        }
        if (CollectionUtils.isNotEmpty(idNotIn)) {
            jpql += " and o.id NOT IN (:idNotIn) ";
        }
        if (null != id) {
            jpql += " and o.id = :id ";
        }
        if (StringUtils.isStrictNotEmpty(name)) {
            jpql += " and o.name ilike :name ";
        }
        if (null != property) {
            jpql += " and o.property = :property ";
        }
        if (CollectionUtils.isNotEmpty(propertyIn)) {
            jpql += " and o.property in (:propertyIn) ";
        }

        if (null != supervisor) {
            jpql += " and o.supervisor = :supervisor ";
        }

        if (null != parentId) {
            jpql += " and o.parentId = :parentId ";
        }
        if (null != isTop) {
            jpql += " and o.is_top = :isTop ";
        }
        if (null != status) {
            jpql += " and o.status = :status ";
        }

        if (null != statusNot) {
            jpql += " and o.status != :statusNot ";
        }
        return jpql;
    }

    public String createCount() {
        return "select count(DISTINCT o.id) " + jpql();
    }

}
