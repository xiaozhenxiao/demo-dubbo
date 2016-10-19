package com.wz.jdk.java.jdk8;

/**
 * @author wangzhen
 * @version 1.0
 * @date 2016-10-19 11:55
 **/
public interface PersonFactory<P extends Person> {
    P create(String firstName, String lastName);
}
