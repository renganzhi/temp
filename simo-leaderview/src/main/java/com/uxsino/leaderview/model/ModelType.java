package com.uxsino.leaderview.model;

import com.uxsino.commons.model.BaseNeClass;

import java.util.ArrayList;
import java.util.List;


public enum ModelType {

    server(1, "服务器", BaseModelType.room, AssetModelType.contents,BaseNeClass.server),

    cabinet(2, "机柜", BaseModelType.room,AssetModelType.container,BaseNeClass.other),

    switches(3,"交换机",BaseModelType.room,AssetModelType.contents,BaseNeClass.network),

    firewall(4,"防火墙",BaseModelType.room,AssetModelType.contents,BaseNeClass.network),

    router(5,"路由器",BaseModelType.room,AssetModelType.contents,BaseNeClass.network),

    ac(6,"无线控制器",BaseModelType.room, AssetModelType.contents,BaseNeClass.network),

    vpn(7,"VPN",BaseModelType.room, AssetModelType.contents,BaseNeClass.network),

    netsBrake(8,"网闸",BaseModelType.room, AssetModelType.contents,BaseNeClass.network),

    gateway(9,"网关",BaseModelType.room, AssetModelType.contents,BaseNeClass.network),

    diskArray(10,"磁盘阵列",BaseModelType.room,AssetModelType.contents,BaseNeClass.storage),

    wall(11,"墙",BaseModelType.draw, null,BaseNeClass.other),

    pipeline(12,"管线",BaseModelType.draw, null,BaseNeClass.other),

    floor(13,"地板",BaseModelType.draw, null,BaseNeClass.other),

    pillar(14,"柱子",BaseModelType.draw, null,BaseNeClass.other),

    furniture(15,"家具",BaseModelType.general, null,BaseNeClass.other),

    supplies(16,"日常用品",BaseModelType.general, null,BaseNeClass.other),

    plant(17,"植物",BaseModelType.general, null,BaseNeClass.other),

    office(18,"办公",BaseModelType.general, null,BaseNeClass.other),

    others(19,"其他",BaseModelType.general, null,BaseNeClass.other),

    door(19,"门",BaseModelType.draw, null,BaseNeClass.other),

    window(19,"窗",BaseModelType.draw, null,BaseNeClass.other),

    voltameter(19,"三相电量仪",BaseModelType.general, null,BaseNeClass.other),

    videoDevices(19,"视频设备",BaseModelType.general, null,BaseNeClass.other),

    //ups(19,"UPS",BaseModelType.pe, AssetModelType.pe,BaseNeClass.other),

    trash(19,"垃圾桶",BaseModelType.general, null,BaseNeClass.other),

    //thDetector(19,"温湿度检测仪",BaseModelType.pe, AssetModelType.pe,BaseNeClass.other),

    sw(19,"开关",BaseModelType.general, null,BaseNeClass.other),

    sofa(19,"沙发",BaseModelType.general, null,BaseNeClass.other),

    //smokeDetector(19,"烟雾检测仪",BaseModelType.pe, AssetModelType.pe,BaseNeClass.other),

    projector(19,"投影仪",BaseModelType.general, null,BaseNeClass.other),

    printer(19,"打印机",BaseModelType.general, null,BaseNeClass.other),

    pot(19,"盆栽",BaseModelType.general, null,BaseNeClass.other),

    //pdc(19,"配电柜",BaseModelType.pe, AssetModelType.pe,BaseNeClass.other),

    pau(19,"新风机",BaseModelType.general, null,BaseNeClass.other),

    patchPanel(19,"插线板",BaseModelType.general, null,BaseNeClass.other),

    outlet(19,"插座",BaseModelType.general, null,BaseNeClass.other),

    lamp(19,"台灯",BaseModelType.general, null,BaseNeClass.other),

    host(19,"主机",BaseModelType.room, AssetModelType.contents,BaseNeClass.host),

    gap(19,"网闸",BaseModelType.general, AssetModelType.contents,BaseNeClass.network),

    fiberSwitch(19,"光纤交换机",BaseModelType.room, AssetModelType.contents,BaseNeClass.network),

    duplicator(19,"复印机",BaseModelType.general, null,BaseNeClass.other),

    //doorControl(19,"门禁",BaseModelType.pe, AssetModelType.pe,BaseNeClass.other),

    desk(19,"桌子",BaseModelType.general, null,BaseNeClass.other),

    clock(19,"时钟",BaseModelType.general, null,BaseNeClass.other),

    chair(19,"椅子",BaseModelType.general, null,BaseNeClass.other),

    ap(19,"无线接入器",BaseModelType.general, AssetModelType.contents,BaseNeClass.network);

    //airConditioner(19,"空调",BaseModelType.pe,AssetModelType.pe,BaseNeClass.other);

    private Integer value;

    private String text;

    private BaseModelType baseType;

    private AssetModelType assetModelType;

    private BaseNeClass baseNeClass;
    private ModelType(Integer value, String text, BaseModelType baseType, AssetModelType assetModelType,
            BaseNeClass baseNeClass) {
        this.value = value;
        this.text = text;
        this.baseType = baseType;
        this.assetModelType = assetModelType;
        this.baseNeClass = baseNeClass;
    }

    public static ModelType fromText(String text) {
        for (ModelType ModelType : ModelType.values()) {
            if (ModelType.text.equals(text)) {
                return ModelType;
            }
        }
        throw new IllegalArgumentException("无此枚举项:" + text);
    }

    public static ModelType fromName(String str) {
        for (ModelType modelType : ModelType.values()) {
            if (modelType.name().equals(str)) {
                return modelType;
            }
        }
        throw new IllegalArgumentException("无此枚举项:" + str);
    }

    /**
     * 获取跟资产相关的类型（机柜/柜内设备）
     * @return
     */
    public static List<ModelType> aboutAsset() {
        List<ModelType> ret = new ArrayList<>();
        for (ModelType modelType : ModelType.values()) {
            if (modelType.getAssetModelType() != null) {
                ret.add(modelType);
            }
        }
        return ret;
    }

    public static boolean contains(String str) {
        for (ModelType baseClass : ModelType.values()) {
            if (baseClass.name().equals(str)) {
                return true;
            }
        }
        return false;
    }

    public Integer getValue() {
        return this.value;
    }

    public String getText() {
        return this.text;
    }

    public BaseModelType getBaseType() {
        return this.baseType;
    }

    public AssetModelType getAssetModelType(){
        return this.assetModelType;
    }

    public BaseNeClass getBaseNeClass() {
        return this.baseNeClass;
    }
}
