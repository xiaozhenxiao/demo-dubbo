package com.wz.common.pool;

import org.apache.commons.pool.KeyedPoolableObjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO
 * wangzhen23
 * 2017/5/23.
 */
public class MyConnectionKeyedPoolableObjectFactory implements KeyedPoolableObjectFactory {
    private static Logger logger = LoggerFactory.getLogger(MyConnectionKeyedPoolableObjectFactory.class);

    private static int count = 0;

    public Object makeObject(Object key) throws Exception {
        MyConnection myConn = new MyConnection(key.toString());
        logger.info(myConn.getName());
        myConn.connect();
        return myConn;
    }

    public void activateObject(Object key, Object obj) throws Exception {
        MyConnection myConn = (MyConnection)obj;
        logger.info(myConn.getName());
    }

    public void passivateObject(Object key, Object obj) throws Exception {
        MyConnection myConn = (MyConnection)obj;
        logger.info(myConn.getName());
    }

    public boolean validateObject(Object key, Object obj) {
        MyConnection myConn = (MyConnection)obj;
        logger.info(myConn.getName());
        return myConn.isConnected();
    }

    public void destroyObject(Object key, Object obj) throws Exception {
        MyConnection myConn = (MyConnection)obj;
        logger.info(myConn.getName());
        myConn.close();
    }
}
