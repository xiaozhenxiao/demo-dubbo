package com.jd.service;

import com.jd.domain.Student;

/**
 * Created by wangzhen23 on 2016/12/20.
 */
public interface StudentService {
    public Student getStudentById(Long id);

    public int addStudent(Student student);
}
