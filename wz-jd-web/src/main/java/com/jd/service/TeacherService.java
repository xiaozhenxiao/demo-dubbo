package com.jd.service;

import com.jd.domain.Teacher;

/**
 * Created by wangzhen23 on 2016/12/20.
 */
public interface TeacherService {
    Teacher getTeacherById(Long id);

    Integer insertTeacher(Teacher teacher);
}
