package com.wz.spring.depends.test.service.impl;

import com.wz.spring.depends.test.dao.DependDao;
import com.wz.spring.depends.test.service.DependService;

import javax.annotation.Resource;

/**
 * Created by wangzhen on 2016-07-15.
 */
public class DependServiceImpl implements DependService {
    private DependDao dependDao;
    @Override
    public String getDomain() {
        return dependDao.getDomain();
    }

    public DependDao getDependDao() {
        return dependDao;
    }

    public void setDependDao(DependDao dependDao) {
        this.dependDao = dependDao;
    }
}
