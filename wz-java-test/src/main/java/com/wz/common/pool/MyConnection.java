package com.wz.common.pool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO
 * wangzhen23
 * 2017/5/23.
 */
public class MyConnection {
    private static Logger logger = LoggerFactory.getLogger(MyConnection.class);

    private String name;
    private boolean connected;

    public MyConnection(String name) {
        this.name = name;
    }

    public void connect() {
        this.connected = true;
        logger.info(name + ": " + connected);
    }

    public void close() {
        this.connected = false;
        logger.info(name + ": " + connected);
    }

    public boolean isConnected() {
        return this.connected;
    }

    public String getName() {
        return this.name;
    }

    public void print() {
        logger.info("=========" + this.name);
    }
}
