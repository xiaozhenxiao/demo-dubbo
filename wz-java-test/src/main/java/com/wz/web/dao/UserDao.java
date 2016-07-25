package com.wz.web.dao;

import com.wz.web.domain.User;
import org.springframework.stereotype.Repository;

@SuppressWarnings("UnnecessaryInterfaceModifier")
@Repository
public interface UserDao {

    public int deleteByPrimaryKey(Long id);

    public int insert(User record);

    public int insertSelective(User record);

    public User selectByPrimaryKey(Long id);

    public int updateByPrimaryKeySelective(User record);

    public int updateByPrimaryKey(User record);
}