package com.wz.spark.api;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wangzhen on 2016-06-16.
 */
public class MsgInfo implements Serializable{
    private static final long serialVersionUID = -2814022769568306965L;
    int id;
    String name;
    List<String> msgs;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id= id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name= name;
    }
    public List<String> getMsgs() {
        return msgs;
    }
    public void setMsgs(List<String> msgs) {
        this.msgs= msgs;
    }
}
