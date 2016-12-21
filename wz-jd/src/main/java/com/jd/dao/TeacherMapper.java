package com.jd.dao;

import com.jd.domain.Teacher;
import org.springframework.stereotype.Repository;

/**
 * Created by wangzhen23 on 2016/12/20.
 */
@Repository
public interface TeacherMapper {
    Teacher getTeacherById(Long id);

    Integer insertTeacher(Teacher teacher);
}
