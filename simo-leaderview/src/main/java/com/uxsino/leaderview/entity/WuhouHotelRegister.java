package com.uxsino.leaderview.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 住店登记(WuhouHotelRegister)表实体类
 *
 * @author huanglei
 * @since 2022-01-27 15:22:34
 */
@Data
@Entity
@Table(name = "wuhou_hotel_register")
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class WuhouHotelRegister{

    @Id
    @ApiModelProperty(value = "主键id")
    @GeneratedValue(generator = "uuidGenerator")
    @GenericGenerator(name = "uuidGenerator", strategy = "uuid")
    private Long id;

    @ApiModelProperty(value = "场所id")
    private Long hotelId;

    @ApiModelProperty(value = "场所名称")
    private String hotelName;

    @ApiModelProperty(value = "住客名称")
    private String guestName;

    @ApiModelProperty(value = "住客身份证号")
    private String guestIdentity;

    @ApiModelProperty(value = "住客手机")
    private String guestPhone;

    @ApiModelProperty(value = "入住日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date checkInDate;

    @ApiModelProperty(value = "离店日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date checkOutDate;

    @ApiModelProperty(value = "记录创建人id")
    private Long createUser;

    @ApiModelProperty(value = "记录创建时间")
    private Date createTime;

    @ApiModelProperty(value = "记录修改人id")
    private Long updateUser;

    @ApiModelProperty(value = "记录修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "是否已删除（0：正常；1：已删除）")
    private Integer isDeleted;

    /**
     * 获取主键值
     *
     * @return 主键值
     */
    /*@Override
    protected Serializable pkVal() {
        return this.id;
    }*/

}
