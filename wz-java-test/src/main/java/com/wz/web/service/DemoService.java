package com.wz.web.service;

import com.wz.web.domain.Demo;

import java.util.List;

/**
 * Created by wangzhen on 2016-07-15.
 */
public interface DemoService {
    public int deleteById(Long id);

    public int addDemo(Demo record);

    public int addDemoSelective(Demo record) throws Exception;

    public Demo getDemoById(Long id);

    public int modifyByIdSelective(Demo record);

    public int modifyById(Demo record);
}
