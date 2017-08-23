package com.jd.web.domain;

import java.util.List;

/**
 * Task查询条件
 * wangzhen23
 * 2017/8/23.
 */
public class TaskQuery {
    private Long id;
    private int fetchNum;
    private String ownSign;
    private Long taskItemNum;
    private List<Long> condition;

    public TaskQuery(Long id, int fetchNum, String ownSign, Long taskItemNum, List<Long> condition) {
        this.id = id;
        this.fetchNum = fetchNum;
        this.ownSign = ownSign;
        this.taskItemNum = taskItemNum;
        this.condition = condition;
    }

    public TaskQuery() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getFetchNum() {
        return fetchNum;
    }

    public void setFetchNum(int fetchNum) {
        this.fetchNum = fetchNum;
    }

    public String getOwnSign() {
        return ownSign;
    }

    public void setOwnSign(String ownSign) {
        this.ownSign = ownSign;
    }

    public Long getTaskItemNum() {
        return taskItemNum;
    }

    public void setTaskItemNum(Long taskItemNum) {
        this.taskItemNum = taskItemNum;
    }

    public List<Long> getCondition() {
        return condition;
    }

    public void setCondition(List<Long> condition) {
        this.condition = condition;
    }
}
