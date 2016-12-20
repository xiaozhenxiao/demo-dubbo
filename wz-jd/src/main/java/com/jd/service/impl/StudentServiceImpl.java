package com.jd.service.impl;

import com.jd.annotation.DataSource;
import com.jd.dao.StudentMapper;
import com.jd.domain.Student;
import com.jd.service.StudentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by wangzhen23 on 2016/12/20.
 */
@Service
public class StudentServiceImpl implements StudentService{
    @Resource
    private StudentMapper studentMapper;
    @Override
    @DataSource(name=DataSource.slave)
    public Student getStudentById(Long id) {
        return studentMapper.getStudentById(id);
    }

    @Override
//    @DataSource(name=DataSource.slave)
    public int addStudent(Student student) {
        return studentMapper.insertStudent(student);
    }
}
