package com.wz.prodect.callback;

import com.wz.dubbo.api.Person;

import java.util.HashMap;
import java.util.Map;

/**
 * wangzhen23
 * 2018/3/20.
 */
public class NotifyImpl implements Notify {
    public Map<String, Person> ret    = new HashMap<String, Person>();
    public Map<String, Throwable> errors = new HashMap<String, Throwable>();

    public void oninvoke(String id){
        System.out.println("oninvoke:+++++++++++++++++++++++=================" + id);
    }
    public void onreturn(Person msg, String id) {
        System.out.println("onreturn:" + msg);
        ret.put(id, msg);
    }

    public void onthrow(Throwable ex, String id) {
        System.out.println("onthrow:"+ex.getMessage());
        errors.put(id, ex);
    }
}
