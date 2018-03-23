package com.smile.wz.rpc;

/**
 * wangzhen23
 * 2018/3/23.
 */
public interface ResponseCallback {
    /**
     * done.
     *
     * @param response
     */
    void done(Object response);

    /**
     * caught exception.
     *
     * @param exception
     */
    void caught(Throwable exception);
}
