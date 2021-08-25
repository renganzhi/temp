package com.uxsino.leaderview.model.asset;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public enum CompareCondEnum {

    GT("gt","大于"),
    LT("lt","小于"),
    GE("ge","大于等于"),
    LE("le","小于等于"),
    NotContains("notcontains","不包含"),
    EQ("eq","等于"),
    NotEQ("noteq","不等于"),
    Contains("contains","包含"),
	AllContains("AllContains","含所有选项"),
	Between("between","两者之间"),
	IN("in","在集合中"),
	AnyOne("AnyOne","含任意一个");

    private String value;

    private String text;

    private CompareCondEnum(String value, String text) {
        this.value = value;
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }



    public static CompareCondEnum getCompareCondEnum(String value) {
        for (CompareCondEnum compareCondEnum : CompareCondEnum.values()) {
            if (value.equals(compareCondEnum.getValue())) {
                return compareCondEnum;
            }
        }
        return null;
    }

    public static CompareCondEnum getCompareCondEnumByName(String name) {
        for (CompareCondEnum compareCondEnum : CompareCondEnum.values()) {
            if (compareCondEnum.toString().equals(name)) {
                return compareCondEnum;
            }
        }
        return null;
    }

    public static Pair<String,String> toPair(CompareCondEnum compareCondEnum){
        return  new ImmutablePair<>(compareCondEnum.getValue(),compareCondEnum.getText());
    }
}
