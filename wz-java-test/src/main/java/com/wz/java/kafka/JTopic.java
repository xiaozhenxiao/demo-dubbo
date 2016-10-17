package com.wz.java.kafka;

import kafka.admin.TopicCommand;
import kafka.utils.ZkUtils;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;

/**
 *
 */
public class JTopic {
    public static void createTopic(String zkAddr, String topicName, int partition, int replication) {
        String[] options = new String[] { "--create", "--zookeeper", zkAddr, "--topic", topicName, "--partitions",
                partition + "", "--replication-factor", replication + "" };
        TopicCommand.main(options);
    }

    public static void listTopic(String zkAddr) {
        String[] options = new String[] { "--list", "--zookeeper", zkAddr };
        TopicCommand.main(options);
    }

    public static void describeTopic(String zkAddr, String topicName) {
        String[] options = new String[] { "--describe", "--zookeeper", zkAddr, "--topic", topicName, };
        TopicCommand.main(options);
    }

    public static void alterTopic(String zkAddr, String topicName) {
        String[] options = new String[] { "--alter", "--zookeeper", zkAddr, "--topic", topicName, "--partitions", "5" };
        TopicCommand.main(options);
    }

    // 通过删除zk里面对应的路径来实现删除topic的功能,只会删除zk里面的信息，Kafka上真实的数据并没有删除
    public static void deleteTopic(String zkAddr, String topicName) {
        String[] options = new String[] { "--zookeeper", zkAddr, "--topic", topicName };
//        DeleteTopicCommand.main(options);
        ZkClient zkClient = ZkUtils.createZkClient("", 1, 1);
        ZkConnection zkConnection = new ZkConnection("");
        ZkUtils zkUtils = new ZkUtils(zkClient, zkConnection, false);
        TopicCommand.TopicCommandOptions topicCommandOptions = new TopicCommand.TopicCommandOptions(options);
        TopicCommand.deleteTopic(zkUtils, topicCommandOptions);
    }

    public static void main(String[] args) {
        String myTestTopic = "ZsmTestTopic";
        int myPartition = 4;
        int myreplication = 1;

        //createTopic(ConfigureAPI.KafkaProperties.ZK, myTestTopic, myPartition, myreplication);
        // listTopic(ConfigureAPI.KafkaProperties.ZK);
        describeTopic(ConfigureAPI.KafkaProperties.ZK, myTestTopic);
        // alterTopic(ConfigureAPI.KafkaProperties.ZK, myTestTopic);
        // deleteTopic(ConfigureAPI.KafkaProperties.ZK, myTestTopic);
    }

}