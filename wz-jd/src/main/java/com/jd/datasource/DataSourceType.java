package com.jd.datasource;

/**
 * Created by wangzhen23 on 2016/12/20.
 */
public enum DataSourceType {
    MASTER("MASTER", "主库"),
    SLAVE("SLAVE", "从库");

    String name;
    String desc;

    DataSourceType(String name, String desc){
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
