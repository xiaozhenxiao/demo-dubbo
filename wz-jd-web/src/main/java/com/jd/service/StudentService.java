package com.jd.service;

import com.jd.domain.Student;

/**
 * Created by wangzhen23 on 2016/12/20.
 */
public interface StudentService {
    Student getStudentById(Long id);

    int addStudent(Student student);

    int mixOperator(Long id);
}
