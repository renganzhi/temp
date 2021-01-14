package com.uxsino.leaderview.model.monitor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NeHealthHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 资源ID
     */
    private String neId;

    /**
     * 健康度得分
     */
    private int health;

    /**
     * 最近更新时间
     */
    private Date updateDate;

}
