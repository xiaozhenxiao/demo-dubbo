package com.wz.java.kafka;

/**
 * @author wangzhen
 * @version 1.0
 * @date 2016-10-17 17:13
 **/

public class ConfigureAPI {


    public static class KafkaProperties{

        public static final String ZK = "spark:2181,spark2:2181,spark3:2181";
        public static final String BROKER_LIST = "spark:9092,spark2:9092,spark3:9092";
        public static final String TOPIC = "testTopic";
        public static final String GROUP_ID = "test";
        //经过INTERVAL时间提交一次offset,整数
        public static final String INTERVAL = "1000";
        public static final String TIMEOUT = "5000";
    }
}
