package com.jd.dao;

import com.jd.domain.Student;
import org.springframework.stereotype.Repository;

/**
 * Created by wangzhen23 on 2016/12/20.
 */
@Repository
public interface StudentMapper {
    Student getStudentById(Long id);

    Integer insertStudent(Student student);
}
