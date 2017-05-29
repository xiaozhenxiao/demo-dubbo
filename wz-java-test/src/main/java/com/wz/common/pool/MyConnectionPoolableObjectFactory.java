package com.wz.common.pool;

import org.apache.commons.pool.PoolableObjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO
 * wangzhen23
 * 2017/5/23.
 */
public class MyConnectionPoolableObjectFactory implements PoolableObjectFactory {
    private static Logger logger = LoggerFactory.getLogger(MyConnectionPoolableObjectFactory.class);

    private static int count = 0;

    public Object makeObject() throws Exception {
        MyConnection myConn = new MyConnection("conn_" + (++count));
        myConn.connect();
        logger.info("make object: " + myConn.getName());
        return myConn;
    }

    public void activateObject(Object obj) throws Exception {
        MyConnection myConn = (MyConnection)obj;
        logger.info("activateObject: " + myConn.getName());
    }

    public void passivateObject(Object obj) throws Exception {
        MyConnection myConn = (MyConnection)obj;
        logger.info("passivateObject: " + myConn.getName());
    }

    public boolean validateObject(Object obj) {
        MyConnection myConn = (MyConnection)obj;
        logger.info("validateObject:" + myConn.getName());
        return myConn.isConnected();
    }

    public void destroyObject(Object obj) throws Exception {
        MyConnection myConn = (MyConnection)obj;
        logger.info("destroyObject:" + myConn.getName());
        myConn.close();
    }
}
