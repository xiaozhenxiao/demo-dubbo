package com.jd.dao;

import com.alibaba.fastjson.JSON;
import com.jd.domain.Student;
import com.jd.service.StudentService;
import com.jd.service.TeacherService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by wangzhen23 on 2016/12/20.
 */
@ContextConfiguration("classpath:spring-config.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class StudentTest {

    @Resource
    private StudentService studentService;
    @Resource
    private TeacherService teacherService;

    @Test
    public void testGetStudentById(){
        System.out.println(JSON.toJSONString(studentService.getStudentById(2l)));
        System.out.println(JSON.toJSONString(studentService.getStudentById(1l)));
    }

    @Test
    public void testInsertStudent(){
        Student student = new Student();
        student.setSname("测试");
        student.setSno("1111");
        student.setSage(22);
        student.setSsex("男");
        studentService.addStudent(student);
    }

    @Test
    public void testMixOperator(){
        System.out.println(studentService.mixOperator(2l));
    }
}
