package com.jd.service.impl;

import com.jd.annotation.DataSource;
import com.jd.dao.StudentMapper;
import com.jd.domain.Student;
import com.jd.domain.Teacher;
import com.jd.service.StudentService;
import com.jd.service.TeacherService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by wangzhen23 on 2016/12/20.
 */
@Service
public class StudentServiceImpl implements StudentService {
    @Resource
    private StudentMapper studentMapper;
    @Resource
    private TeacherService teacherService;

    @Override
    @DataSource(name = DataSource.slave)
    public Student getStudentById(Long id) {
        return studentMapper.getStudentById(id);
    }

    @Override
//    @DataSource(name=DataSource.slave)
    public int addStudent(Student student) {
        return studentMapper.insertStudent(student);
    }

    @Override
    @DataSource(name = DataSource.master)
    public int mixOperator(Long id) {
        /*Student student = studentMapper.getStudentById(id);
        if (student == null) {
            student = new Student();
        }*/
        Teacher teacher = teacherService.getTeacherById(id);
        Student student = new Student();
        if(teacher != null)
            student.setSname(teacher.getTname());
        student.setId(null);
        return studentMapper.insertStudent(student);
    }
}
