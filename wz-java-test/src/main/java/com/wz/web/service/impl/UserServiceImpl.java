package com.wz.web.service.impl;

import com.wz.web.dao.DemoDao;
import com.wz.web.dao.UserDao;
import com.wz.web.domain.Demo;
import com.wz.web.domain.User;
import com.wz.web.exception.ParseException;
import com.wz.web.service.DemoService;
import com.wz.web.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by wangzhen on 2016-07-15.
 */
@Service("userService")
public class UserServiceImpl implements UserService {
    @Resource
    private UserDao userDao;


    @Override
    public int deleteById(Long id) {
        return userDao.deleteByPrimaryKey(id);
    }

    @Override
    public int addUser(User record) {
        return userDao.insert(record);
    }

    @Override
    public int addUserSelective(User record) throws Exception{
        int result = userDao.insertSelective(record);
        return result;
    }

    @Override
    public User getUserById(Long id) {
        return userDao.selectByPrimaryKey(id);
    }

    @Override
    public int modifyByIdSelective(User record) {
        return userDao.updateByPrimaryKeySelective(record);
    }

    @Override
    public int modifyById(User record) {
        return userDao.updateByPrimaryKey(record);
    }
}
