package com.jd.service.impl;

import com.jd.annotation.DataSource;
import com.jd.dao.TeacherMapper;
import com.jd.domain.Teacher;
import com.jd.service.TeacherService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by wangzhen23 on 2016/12/20.
 */
@Service
public class TeacherServiceImpl implements TeacherService{
    @Resource
    private TeacherMapper teacherMapper;

    @Override
    public Teacher getTeacherById(Long id) {
        return teacherMapper.getTeacherById(id);
    }

    @Override
    public Integer insertTeacher(Teacher teacher) {
        return teacherMapper.insertTeacher(teacher);
    }
}
