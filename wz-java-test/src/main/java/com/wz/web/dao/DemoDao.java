package com.wz.web.dao;


import com.wz.web.domain.Demo;
import org.springframework.stereotype.Repository;

@SuppressWarnings("UnnecessaryInterfaceModifier")
@Repository
public interface DemoDao {

    public int deleteByPrimaryKey(Long id);

    public int insert(Demo record);

    public int insertSelective(Demo record);

    public Demo selectByPrimaryKey(Long id);

    public int updateByPrimaryKeySelective(Demo record);

    public int updateByPrimaryKey(Demo record);
}