package com.uxsino.leaderview.model.asset;

/**
 * 
 * 资产状态枚举，对应数据树simo_asset_status
 *
 */
public enum AssetStatusEnum {
	
    waitstorage("waitstorage","待入库"),
    reserve("reserve","库存"),
    taking("taking","领用中"),
    onSecondment("onSecondment","借调中"),
    lost("lost","已丢失"),
    repair("repair","维修中"),
    scrapped("scrapped","已报废");

    private String value;

    private String text;
    
    private AssetStatusEnum(String value, String text) {
        this.value = value;
        this.text = text;
    }
    
    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }


    
    public static AssetStatusEnum getAssetStatusEnum(String value) {
        for (AssetStatusEnum statusEnum : AssetStatusEnum.values()) {
            if (value.equals(statusEnum.getValue())) {
                return statusEnum;
            }
        }
        return null;
    }

    public static AssetStatusEnum getAssetStatusEnumByName(String name) {
        for (AssetStatusEnum assetStatusEnum : AssetStatusEnum.values()) {
            if (assetStatusEnum.toString().equals(name)) {
                return assetStatusEnum;
            }
        }
        return null;
    }

}
