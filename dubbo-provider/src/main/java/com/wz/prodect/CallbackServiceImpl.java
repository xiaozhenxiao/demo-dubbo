package com.wz.prodect;

/**
 * wangzhen23
 * 2018/3/20.
 */
import com.wz.dubbo.api.CallbackListener;
import com.wz.dubbo.api.CallbackService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * CallbackServiceImpl
 */
public class CallbackServiceImpl implements CallbackService {

    private final Map<String, CallbackListener> listeners = new ConcurrentHashMap<String, CallbackListener>();

    public CallbackServiceImpl() {
        Thread t = new Thread(() -> {
                while (true) {
                    try {
                        for (Map.Entry<String, CallbackListener> entry : listeners.entrySet()) {
                            try {
                                entry.getValue().changed(getChanged(entry.getKey()));
                            } catch (Throwable e) {
                                listeners.remove(entry.getKey());
                            }
                        }
                        Thread.sleep(5000); // timely trigger change event
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
        });
        t.setDaemon(true);
        t.start();
    }

    public void addListener(String key, CallbackListener listener) {
        listeners.put(key, listener);
        listener.changed(getChanged(key)); // send notification for change
    }

    private String getChanged(String key) {
        return key + "-Changed: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

}
