package com.uxsino.leaderview.model.asset;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.uxsino.commons.utils.SessionUtils;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.Date;

/**
 * 实体基类
 *
 * @author echo
 */
@Data
@MappedSuperclass
public class BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
    private String id; // 主键

	/**
     * 创建人id
     */
    @CreatedBy
    private Long createUserId;

    /**
     * 创建时间
     */
    @CreatedDate
    private Date createDate;

    /**
     * 修改人id
     */
    @LastModifiedBy
    private Long updateUserId;

    /**
     * 修改时间
     */
    @LastModifiedDate
    private Date updateDate;

    @JsonIgnore
    public void setUser(JSONObject userInfo) {
        Long userId = userInfo.getLong("id");
        setUser(userId);
    }

    /**
     * 设置创建人创建时间
     */
    @JsonIgnore
    public void setUser(Long userId) {
//        Assert.isTrue(userId != null && StringUtils.isNotEmpty(userName), "用户未登录!");
        Date date = new Date();
        if (createUserId == null) {
            this.createUserId = userId;
            this.createDate = date;
//            this.createUserName = userName;
        }else {
            this.updateUserId = userId;
            this.updateDate = date;
        }

//        this.updateUserName = userName;
    }

    @JsonIgnore
    public void setUser(HttpSession session) {
        Long userId = SessionUtils.getCurrentUserIdFromSession(session);
        if (userId != null) {
//            String userName = SessionUtils.getCurrentUserFromSession(session).getString("userName");
            this.setUser(userId);
        }
    }
}