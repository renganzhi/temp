package com.uxsino.leaderview.model.alert;

import com.google.common.collect.Maps;
import com.uxsino.commons.qr.ZxQr;
import com.uxsino.commons.utils.TimeUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * 
 * @description 通用告警信息
 * @date 2018年6月21日
 */
@Data
@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "通用告警信息")
public class CurrencyAlert {

    /**
     * 告警信息编号
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "ID", hidden = true)
    private Long id;

    /**
     * 告警编码，由相关告警规则生成的加密串
     */
    @ApiModelProperty(value = "告警编码", hidden = true)
    private String alertCode;

    /**
     * 告警等级值（取值范围1~10，值越大表示告警越严重）
     */
    @Column(nullable = false)
    @ApiModelProperty(value = "告警等级值")
    @NotNull(message = "告警等级值不能为空")
    private Integer level;

    /**
     * 告警处理状态
     */
    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(value = "告警处理状态", hidden = true)
    private AlertHandleStatus handleStatus;

    /**
     * 处理告警的用户编号
     */
    @ApiModelProperty(value = "处理告警的用户编号", hidden = true)
    private Long handleUserId;

    /**
     * 告警处理时间
     */
    @ApiModelProperty(value = "告警处理时间", hidden = true)
    private Date handleDate;

    /**
     * 告警处理方式
     */
    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(value = "告警处理方式", hidden = true)
    private AlertHandleWay handleWay;

    /**
     * 告警处理描述
     */
    @Column(length = 500)
    @ApiModelProperty(value = "告警处理描述", hidden = true)
    private String description;

    /**
     * 告警合并条数（告警累计次数）
     */
    @ApiModelProperty(value = "告警合并条数", hidden = true)
    private Integer number = 0;

    /**
     * 告警持续次数（告警恢复时清零）
     */
    @ApiModelProperty(value = "告警持续次数", hidden = true)
    private Integer times = 0;

    /**
     * 首次告警时间
     */
    @ApiModelProperty(value = "首次告警时间", hidden = true)
    private Date firstAlertDate;

    /**
     * 最近告警时间
     */
    @ApiModelProperty(value = "最近告警时间", hidden = true)
    private Date recentAlertDate;

    /**
     * 最近告警信息描述
     */
    @Column(columnDefinition = "TEXT")
    @ApiModelProperty(value = "最近告警信息描述", hidden = true)
    private String recentAlertBrief;

    /**
     * 告警工单的流程实例ID
     */
    @ApiModelProperty(value = "告警工单的流程实例ID", hidden = true)
    private String processInstanceId;

    /**
     * 是否被删除
     */
    @Column(columnDefinition = "boolean default false")
    @ApiModelProperty(value = "是否被删除", hidden = true)
    private Boolean isDeleted = false;

    /**
     * 获取首次告警时间字符串 yyyy-MM-dd HH:mm:ss
     * @return
     */
    public String getFirstAlertDateStr() {
        if (firstAlertDate != null) {
            return TimeUtils.formatTime(firstAlertDate);
        }
        return null;
    }

    /**
     * 获取最近告警时间字符串 yyyy-MM-dd HH:mm:ss
     * @return
     */
    public String getRecentAlertDateStr() {
        if (recentAlertDate != null) {
            return TimeUtils.formatTime(recentAlertDate);
        }
        return null;
    }

    /**
     * 获取告警处理时间字符串 yyyy-MM-dd HH:mm:ss
     * @return
     */
    public String getHandleDateStr() {
        if (handleDate != null) {
            return TimeUtils.formatTime(handleDate);
        }
        return null;
    }

    /**
     * 【实时告警】左侧时间显示
     * @return
     */
    public String getRealAlertDateStr() {
        if (recentAlertDate != null) {
            Calendar sysDate = Calendar.getInstance();
            Calendar realDate = Calendar.getInstance();
            realDate.setTime(recentAlertDate);
            String formatStr = "HH:mm";
            if (sysDate.get(Calendar.YEAR) != realDate.get(Calendar.YEAR)) {
                formatStr = "yyyy-MM-dd " + formatStr;
            } else if (sysDate.get(Calendar.MONTH) != realDate.get(Calendar.MONTH)) {
                formatStr = "MM-dd " + formatStr;
            } else if (sysDate.get(Calendar.DATE) != realDate.get(Calendar.DATE)) {
                formatStr = "MM-dd " + formatStr;
            }
            return new SimpleDateFormat(formatStr).format(recentAlertDate);
        }
        return null;
    }

//    /**
//     * 获取包含告警内容的二维码数据
//     * @return
//     */
//    public Map<String, Object> getAlertQRCode() {
//        Map<String, Object> tags = Maps.newHashMap();
//        StringBuffer qrContent = new StringBuffer();
//        // 添加告警前缀
//        qrContent.append("[").append(getRecentAlertDateStr()).append("]-");
//        String alertBrief = recentAlertBrief;
//        qrContent.append(AlertLevelService.levelMapName.get(level)).append(" : ")
//
//            .append(alertBrief.length() > 180 ? alertBrief.substring(0, 180) + "...(部分内容已省略)" : alertBrief);
//        tags.put("props", qrContent.toString());
//        Integer size = 180;
//        tags.put("qrImg", ZxQr.of(qrContent.toString()).size(size, size).color(0xFFFFFFFF, 0x00000000).margin(4)
//            .charset("UTF-8").toBase64ImageData());
//        return tags;
//    }

}
