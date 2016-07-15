package com.wz.web.service.impl;

import com.wz.web.dao.DemoDao;
import com.wz.web.domain.Demo;
import com.wz.web.service.DemoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by wangzhen on 2016-07-15.
 */
@Service
public class DemoServiceImpl implements DemoService {
    @Resource
    private DemoDao demoDao;


    @Override
    public int deleteById(Long id) {
        return demoDao.deleteByPrimaryKey(id);
    }

    @Override
    public int addDemo(Demo record) {
        return demoDao.insert(record);
    }

    @Override
    public int addDemoSelective(Demo record) {
        return demoDao.insertSelective(record);
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
