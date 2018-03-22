package com.wz.prodect.callback;

import com.wz.dubbo.api.Person;

/**
 * wangzhen23
 * 2018/3/20.
 */
public interface Notify {
    void oninvoke(String id);
    void onreturn(Person msg, String id);
    void onthrow(Throwable ex, String id);
}
