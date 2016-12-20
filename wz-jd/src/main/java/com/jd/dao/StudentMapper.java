package com.jd.dao;

import com.jd.domain.Student;
import org.springframework.stereotype.Repository;

/**
 * Created by wangzhen23 on 2016/12/20.
 */
@Repository
public interface StudentMapper {
    public Student getStudentById(Long id);

    public Integer insertStudent(Student student);
}
