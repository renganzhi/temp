package com.uxsino.leaderview.model.business;

/**
 * @description 排序方式
 * @date 2019年1月22日
 */
public enum SortWay {
                     createDate_asc("按创建时间排序","create_date","asc",true),
                     health_asc("按健康度排序","health","asc",true),
                     alertCount_desc("按告警数排序","alertCount","desc",false),
                     availableRate_asc("按可用率排序","availableRate","asc",false),
                     custom("自定义排序","priority","asc",false);
    private String text;

    private String field;

    private String sort;

    private boolean bySql;

    private SortWay(String text, String field, String sort, boolean bySql) {
        this.text = text;
        this.field = field;
        this.sort = sort;
        this.bySql = bySql;
    }

    public String getText() {
        return this.text;
    }

    public String getField() {
        return this.field;
    }

    public String getSort() {
        return this.sort;
    }

    public boolean bySql() {
        return this.bySql;
    }

}
