package com.jd.web.domain;

/**
 * 任务类
 * wangzhen23
 * 2017/8/23.
 */
public class ScheduleTask {
    private Long id;
    private Integer dealCount;
    private String sts;
    private String ownSign;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDealCount() {
        return dealCount;
    }

    public void setDealCount(Integer dealCount) {
        this.dealCount = dealCount;
    }

    public String getSts() {
        return sts;
    }

    public void setSts(String sts) {
        this.sts = sts;
    }

    public String getOwnSign() {
        return ownSign;
    }

    public void setOwnSign(String ownSign) {
        this.ownSign = ownSign;
    }
}
