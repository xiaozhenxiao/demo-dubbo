package com.wz.web.service.impl;

import com.wz.web.domain.User;
import com.wz.web.service.UserService;
import org.springframework.stereotype.Service;

/**
 * 测试autowireByType
 * wangzhen23
 * 2018/8/7.
 */
@Service("otherUserService")
public class OtherUserServiceImpl  implements UserService {

    @Override
    public int deleteById(Long id) {
        return 0;
    }

    @Override
    public int addUser(User record) {
        return 0;
    }

    @Override
    public int addUserSelective(User record) throws Exception {
        return 0;
    }

    @Override
    public User getUserById(Long id) {
        return null;
    }

    @Override
    public int modifyByIdSelective(User record) {
        return 0;
    }

    @Override
    public int modifyById(User record) {
        return 0;
    }
}
