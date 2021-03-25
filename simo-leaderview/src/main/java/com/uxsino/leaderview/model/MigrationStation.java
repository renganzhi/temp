package com.uxsino.leaderview.model;

import lombok.*;

import java.util.ArrayList;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//用于表示元件"地图-迁徙图"的结点
public class MigrationStation {
    /*
        name表示在该节点显示的标题信息
     */
    private String name;
    /*
        value用于定位该点在地图中的准确位置，至少由三个值按顺序组成：
        value.get(0): 表示经度
        value.get(1): 表示维度
        value.get(2): 表示该点标题信息所对应的值，这个值将会决定该点在地图上的颜色
     */
    private ArrayList<Float> value;
}
