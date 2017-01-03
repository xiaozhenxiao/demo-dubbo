package com.wz.web.service.impl;

import com.wz.web.dao.DemoDao;
import com.wz.web.domain.Demo;
import com.wz.web.domain.User;
import com.wz.web.exception.ParseException;
import com.wz.web.service.DemoService;
import com.wz.web.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by wangzhen on 2016-07-15.
 */
@Service("demoService")
public class DemoServiceImpl implements DemoService {
    @Resource
    private DemoDao demoDao;
    @Resource
    private UserService userService;


    @Override
    public int deleteById(Long id) {
        return demoDao.deleteByPrimaryKey(id);
    }

    @Override
    public int addDemo(Demo record) {
        return demoDao.insert(record);
    }

    @Override
    public int addDemoSelective(Demo record) throws Exception{
        int result = demoDao.insertSelective(record);

        User user = new User();
        user.setId(1l);
        user.setUsername("振振");
        user.setPassword("654321");
        //此种情况，user可执行成功，demo回滚
        userService.modifyById(user);
//        userService.addUserSelective(user);

        /*record.setId(2l);
        record.setUsername("振振");
        record.setPassword("654321");
//      在add的事务中执行，发生异常，一起回滚
        modifyById(record);*/
//        if(result > 0){
//            throw new ParseException("测试事务回滚");
//        }
        return result;
    }

    @Override
    public Demo getDemoById(Long id) {
        return demoDao.selectByPrimaryKey(id);
    }

    @Override
    public int modifyByIdSelective(Demo record) {
        return demoDao.updateByPrimaryKeySelective(record);
    }

    @Override
    public int modifyById(Demo record) {
        return demoDao.updateByPrimaryKey(record);
    }
}
