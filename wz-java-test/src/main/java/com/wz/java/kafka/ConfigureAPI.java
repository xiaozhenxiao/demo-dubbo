package com.wz.java.kafka;

/**
 * @author wangzhen
 * @version 1.0
 * @date 2016-10-17 17:13
 **/

public class ConfigureAPI {


    public static class KafkaProperties{

        public static final String ZK = "";
        public static final String BROKER_LIST = "";
        public static final String TOPIC = "";
        public static final String GROUP_ID = "";
        //经过INTERVAL时间提交一次offset,整数
        public static final String INTERVAL = "";
        public static final String TIMEOUT = "";
    }
}
