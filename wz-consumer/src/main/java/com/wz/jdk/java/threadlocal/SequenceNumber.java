package com.wz.jdk.java.threadlocal;

/**
 * 测试static ThreadLocal
 * @author wangzhen
 * @version 1.0
 * @date 2016-08-15 19:41
 **/
public class SequenceNumber {

    public static void main(String[] args){
        ThreadLocal<Integer> sn = new ThreadLocal<Integer>(){
            public Integer initialValue(){
                return 0;
            }
        };
        ThreadLocal<Integer> sn1 = new ThreadLocal<Integer>(){
            public Integer initialValue(){
                return 0;
            }
        };
        ThreadLocal<Integer> sn2 = new ThreadLocal<Integer>(){
            public Integer initialValue(){
                return 0;
            }
        };
        TestClient t1  = new TestClient(sn);
        TestClient t2  = new TestClient(sn1);
        TestClient t3  = new TestClient(sn2);

        t1.start();
        t2.start();
        t3.start();

        t1.print();
        t2.print();
        t3.print();


    }

    private static class TestClient extends Thread{
        private static ThreadLocal<Integer> seqNum;

        public TestClient(ThreadLocal<Integer> sn ){
            this.seqNum = sn;
        }

        public void run(){
            for(int i=0; i< 3; i++){
                seqNum.set(seqNum.get() + 1);
                System.out.println( Thread.currentThread().getName()  + " --> " + seqNum.get());
            }
        }

        public void print(){
            for(int i=0; i< 3; i++){
                seqNum.set(seqNum.get() + 1);
                System.out.println( Thread.currentThread().getName()  + " --> " + seqNum.get());
            }
        }
    }

}
