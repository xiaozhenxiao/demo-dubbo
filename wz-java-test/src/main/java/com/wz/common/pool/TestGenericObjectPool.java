package com.wz.common.pool;

import org.apache.commons.pool.KeyedObjectPool;
import org.apache.commons.pool.KeyedPoolableObjectFactory;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericKeyedObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO
 * wangzhen23
 * 2017/5/23.
 */
public class TestGenericObjectPool {
    private static Logger logger = LoggerFactory.getLogger(TestGenericObjectPool.class);

    public static void main(String[] args) {
        //test1();
        //test2();
        //test3();
    }

    private static void test1() {
        PoolableObjectFactory factory = new MyConnectionPoolableObjectFactory();
        Config config = new Config();
        config.lifo = false;
        config.maxActive = 5;
        config.maxIdle = 5;
        config.minIdle = 1;
        config.maxWait = 5 * 1000;

        ObjectPool pool = new GenericObjectPool(factory, config);
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(new MyTask(pool));
            thread.start();
        }
        closePool(pool);
    }

    private static void test2() {
        PoolableObjectFactory factory = new MyConnectionPoolableObjectFactory();
        Config config = new Config();
        config.lifo = false;
        config.maxActive = 5;
        config.maxIdle = 5;
        config.minIdle = 1;
        config.maxWait = 20 * 1000;

        ObjectPool pool = new GenericObjectPool(factory, config);
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(new MyTask(pool));
            thread.start();
        }
        closePool(pool);
    }

    private static void test3() {
        PoolableObjectFactory factory = new MyConnectionPoolableObjectFactory();
        Config config = new Config();
        config.lifo = false;
        config.maxActive = 5;
        config.maxIdle = 0;
        config.minIdle = 0;
        config.maxWait = -1;

        ObjectPool pool = new GenericObjectPool(factory, config);
        Thread thread = new Thread(new MyTask(pool));
        thread.start();

        try {
            Thread.sleep(60L * 1000L);
        } catch (Exception e) {
            e.printStackTrace();
        }

        closePool(pool);
    }

    private static void test4(){
        KeyedPoolableObjectFactory factory = new MyConnectionKeyedPoolableObjectFactory();
        GenericKeyedObjectPool.Config config = new GenericKeyedObjectPool.Config();
        config.lifo = false;
        config.maxActive = 5;
        config.maxIdle = 0;
        config.minIdle = 0;
        config.maxWait = -1;
        KeyedObjectPool pool = new GenericKeyedObjectPool(factory, config);
        Thread thread = new Thread(new MyTask1(pool));
        thread.start();

        try {
            Thread.sleep(60L * 1000L);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        closePool(pool);
    }

    private static void closePool(ObjectPool pool) {
        try {
            pool.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class MyTask implements Runnable {
        private ObjectPool pool;

        public MyTask(ObjectPool pool) {
            this.pool = pool;
        }

        public void run() {
            MyConnection myConn = null;
            try {
                myConn = (MyConnection)pool.borrowObject();
                try {
                    myConn.print();
                } catch(Exception ex) {
                    pool.invalidateObject(myConn);
                    myConn = null;
                }
                Thread.sleep(10L * 1000L);
            } catch(Exception ex) {
                logger.error("Cannot borrow connection from pool.", ex);
            } finally {
                if (myConn != null) {
                    try {
                        pool.returnObject(myConn);
                    } catch (Exception ex) {
                        logger.error("Cannot return connection from pool.", ex);
                    }
                }
            }
        }
    }
    private static class MyTask1 implements Runnable {
        private KeyedObjectPool pool;

        public MyTask1(KeyedObjectPool pool) {
            this.pool = pool;
        }

        public void run() {
            MyConnection myConn = null;
            try {
                myConn = (MyConnection)pool.borrowObject("key");
                try {
                    myConn.print();
                } catch(Exception ex) {
                    pool.invalidateObject("key", myConn);
                    myConn = null;
                }
                Thread.sleep(10L * 1000L);
            } catch(Exception ex) {
                logger.error("Cannot borrow connection from pool.", ex);
            } finally {
                if (myConn != null) {
                    try {
                        pool.returnObject("key", myConn);
                    } catch (Exception ex) {
                        logger.error("Cannot return connection from pool.", ex);
                    }
                }
            }
        }
    }
}
