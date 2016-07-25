package com.wz.web.service;

import com.wz.web.domain.User;

/**
 * Created by wangzhen on 2016-07-15.
 */
public interface UserService {
    public int deleteById(Long id);

    public int addUser(User record);

    public int addUserSelective(User record) throws Exception;

    public User getUserById(Long id);

    public int modifyByIdSelective(User record);

    public int modifyById(User record);
}
