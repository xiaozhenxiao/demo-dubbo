package com.jd.web.dao;

import com.jd.web.domain.ScheduleTask;
import org.springframework.stereotype.Repository;

/**
 * mapper
 * wangzhen23
 * 2017/8/23.
 */
@Repository
public interface ScheduleTaskMapper {
    ScheduleTask select(Long id);
    Integer deleteByPrimaryKey(Long id);
    Integer insert(ScheduleTask task);
    Integer updateByPrimaryKeySelective(ScheduleTask task);
}
