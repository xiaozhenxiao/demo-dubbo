package com.wz.rpc;

import com.wz.dubbo.api.IDemoService;
import com.wz.dubbo.api.Person;

/**
 * wangzhen23
 * 2018/3/20.
 */
public class NormalDemoService implements IDemoService {
    public Person get(String id) {
        System.out.println("recive id:" + id);
        return new Person(id, "charles`son", 4);
    }
}
