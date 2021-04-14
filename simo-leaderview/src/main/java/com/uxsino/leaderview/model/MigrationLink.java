package com.uxsino.leaderview.model;

import lombok.*;

import java.util.ArrayList;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//表示元件"地图-迁徙图"中迁徙路径上面的起点和终点，其中终点实体后三者一律为null或0
public class MigrationLink {
    /*
        coord即坐标，确定起点或者终点在地图上的位置，将影响迁徙线的两头
        严格按照先后顺序定义为：
            coord.get(0): 经度
            coord.get(1): 维度
     */
    private ArrayList<Float> coord;

    /*
        name同样表示该条链路上显示的标题信息，仅在起点实体中有值，终点实体的name没有作用
     */
    private String name;

    /*
        value对应的是标题信息的值，这个值同样将会影响到迁徙线在地图上的颜色，同理终点实体的value没有作用
     */
    private double value;

    /*
        迁徙线上除了name一个值外，可能额外还有其他值也要展示，这些值与value不同，将按照 信息标题:对应值
        的格式以字符串的形式传回前端，前端将直接展示这些字符串，因此这些值不能像value一样对迁徙线的颜色
        产生任何影响。同理终点实体的obj没有作用。
     */
    private ArrayList<String> obj;
}
