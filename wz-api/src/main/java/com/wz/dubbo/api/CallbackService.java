package com.wz.dubbo.api;

/**
 * wangzhen23
 * 2018/3/20.
 */
public interface CallbackService {

    void addListener(String key, CallbackListener listener);

}
