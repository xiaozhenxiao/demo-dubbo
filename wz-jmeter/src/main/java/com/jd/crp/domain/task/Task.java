package com.jd.crp.domain.task;

import java.io.Serializable;
import java.util.Date;

/**
 * 订单任务表
 */
public class Task implements Serializable {
    private static final long serialVersionUID = 9169386461272264015L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 虚拟业务类型
     */
    private Integer businessType;
    /**
     * 父任务id
     */
    private Long parentId;
    /**
     * 业务唯一约束
     */
    private String uuid;
    /**
     * 任务类型
     */
    private Integer type;
    /**
     * 关联业务对象id
     */
    private Long rfId;
    /**
     * 关联业务对象类型
     */
    private Integer rfType;
    /**
     * 状态【1,等待处理;0,已锁定;-1,无效记录;2,已完成】
     */
    private Integer status;
    /**
     * 序列化任务数据
     */
    private String taskData;
    /**
     * 失败次数
     */
    private Integer failCount;
    /**
     * 创建时间
     */
    private Date created;
    /**
     * 修改时间
     */
    private Date modified;
    /**
     * 操作人
     */
    private String modifiedPerson;


    public Task() {
    }

    private Task(Task target){
        this.id = target.id;
        this.businessType = target.businessType;
        this.uuid = target.uuid;
        this.type = target.type;
        this.rfId = target.rfId;
        this.rfType = target.rfType;
        this.status = target.status;
        this.failCount = target.failCount;
        this.taskData = target.taskData;
        this.modifiedPerson = target.modifiedPerson;
    }

    public Long getId() {
        return id;
    }

    public Integer getBusinessType() {
        return businessType;
    }

    public Long getParentId() {
        return parentId;
    }

    public String getUuid() {
        return uuid;
    }

    public Long getRfId() {
        return rfId;
    }

    public String getTaskData() {
        return taskData;
    }

    public Date getCreated() {
        return created;
    }

    public Date getModified() {
        return modified;
    }

    public String getModifiedPerson() {
        return modifiedPerson;
    }

    public Integer getType() {
        return type;
    }

    public Integer getRfType() {
        return rfType;
    }

    public Integer getStatus() {
        return status;
    }

    public Integer getFailCount() {
        return failCount;
    }

    public static class Builder {
        private Task target;
        public Builder() {
            target = new Task();
        }
        public Builder id(Long id) {
            target.id = id;
            return this;
        }
        public Builder businessType(Integer businessType) {
            target.businessType = businessType;
            return this;
        }
        public Builder parentId(Long parentId) {
            target.parentId = parentId;
            return this;
        }
        public Builder uuid(String uuid) {
            target.uuid = uuid;
            return this;
        }
        public Builder type(Integer type) {
            target.type = type;
            return this;
        }
        public Builder rfId(Long rfId) {
            target.rfId = rfId;
            return this;
        }
        public Builder rfType(Integer rfType) {
            target.rfType = rfType;
            return this;
        }
        public Builder status(Integer status) {
            target.status = status;
            return this;
        }
        public Builder taskData(String taskData) {
            target.taskData = taskData;
            return this;
        }
        public Builder failCount(Integer failCount) {
            target.failCount = failCount;
            return this;
        }
        public Builder created(Date created) {
            target.created = created;
            return this;
        }
        public Builder modified(Date modified) {
            target.modified = modified;
            return this;
        }
        public Builder modifiedPerson(String modifiedPerson) {
            target.modifiedPerson = modifiedPerson;
            return this;
        }
        public Task build() {
            return new Task(target);
        }
    }
}