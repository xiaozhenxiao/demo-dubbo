package com.wz.common.pool;

import org.apache.commons.pool.KeyedObjectPool;
import org.apache.commons.pool.impl.StackKeyedObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO
 * wangzhen23
 * 2017/5/23.
 */
public class TestKeyedPool {
    private static Logger logger = LoggerFactory.getLogger(TestKeyedPool.class);

    public static void main(String[] args) throws Exception {
        MyConnectionKeyedPoolableObjectFactory factory = new MyConnectionKeyedPoolableObjectFactory();
        KeyedObjectPool pool = new StackKeyedObjectPool(factory);
        try {
            logger.info("================================================");
            for (int i = 0; i < 10; i++) {
                String key = "conn_" + i;
                MyConnection myConn = (MyConnection)pool.borrowObject(key);
                try {
                    myConn.print();
                } catch(Exception ex) {
                    pool.invalidateObject(key, myConn);
                } finally {
                    pool.returnObject(key, myConn);
                }
            }

            logger.info("================================================");
            for (int i = 0; i < 10; i++) {
                String key = "conn_xxx";
                MyConnection myConn = (MyConnection)pool.borrowObject(key);
                try {
                    myConn.print();
                } catch(Exception ex) {
                    pool.invalidateObject(key, myConn);
                } finally {
                    pool.returnObject(key, myConn);
                }
            }
        } finally {
            logger.info("Close Pool");
            try {
                pool.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
