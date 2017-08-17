package com.wz.web.service.impl;

import com.wz.web.dao.DemoDao;
import com.wz.web.dao.UserDao;
import com.wz.web.domain.Demo;
import com.wz.web.domain.User;
import com.wz.web.exception.ParseException;
import com.wz.web.service.DemoService;
import com.wz.web.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by wangzhen on 2016-07-15.
 */
@Service("userService")
public class UserServiceImpl implements UserService {
    @Resource
    private UserDao userDao;
    @Resource
    private DemoDao demoDao;


    @Override
    public int deleteById(Long id) {
        return userDao.deleteByPrimaryKey(id);
    }

    @Override
    @Transactional
    public int addUser(User user) {
        Demo demo = new Demo();
        demo.setUsername(user.getUsername());
        demo.setPassword(user.getPassword());
        demoDao.insert(demo);
        return userDao.insert(user);
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
