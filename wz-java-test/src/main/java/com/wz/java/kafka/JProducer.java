package com.wz.java.kafka;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 可以指定规则(key和分区函数)以让消息写到特定分区：
 * <p>
 * 1、若发送的消息没有指定key则Kafka会随机选择一个分区
 * </p>
 * <p>
 * 2、否则，若指定了分区函数(通过partitioner.class)则该函数以key为参数确定写到哪个分区
 * </p>
 * <p>
 * 3、否则，Kafka根据hash(key)%partitionNum确定写到哪个分区
 * </p>
 *
 * @author zsm
 * @version 1.0
 * @date 2016年9月27日 上午10:26:42
 * @parameter
 * @return
 */
public class JProducer extends Thread {
    private Producer<String, String> producer;
    private String topic;
    private final int SLEEP = 10;
    private final int msgNum = 1000;

    public JProducer(String topic) {
        Properties props = new Properties();
        props.put("metadata.broker.list", ConfigureAPI.KafkaProperties.BROKER_LIST);// 如192.168.6.127:9092,192.168.6.128:9092
        // request.required.acks
        // 0, which means that the producer never waits for an acknowledgement from the broker (the same behavior as 0.7). This option provides the lowest latency but the weakest durability guarantees
        // (some data will be lost when a server fails).
        // 1, which means that the producer gets an acknowledgement after the leader replica has received the data. This option provides better durability as the client waits until the server
        // acknowledges the request as successful (only messages that were written to the now-dead leader but not yet replicated will be lost).
        // -1, which means that the producer gets an acknowledgement after all in-sync replicas have received the data. This option provides the best durability, we guarantee that no messages will be
        // lost as long as at least one in sync replica remains.
        props.put("request.required.acks", "-1");
        // 配置value的序列化类
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        // 配置key的序列化类
        props.put("key.serializer.class", "kafka.serializer.StringEncoder");
        // 提供自定义的分区函数将消息写到分区上，未指定的话Kafka根据hash(messageKey)%partitionNum确定写到哪个分区
        props.put("partitioner.class", "com.zsm.kfkdemo.MyPartitioner");
        producer = new Producer<String, String>(new ProducerConfig(props));
        this.topic = topic;
    }

    @Override
    public void run() {
        boolean isBatchWriteMode = true;
        System.out.println("isBatchWriteMode: " + isBatchWriteMode);
        if (isBatchWriteMode) {
            // 批量发送
            int batchSize = 100;
            List<KeyedMessage<String, String>> msgList = new ArrayList<KeyedMessage<String, String>>(batchSize);
            for (int i = 0; i < msgNum; i++) {
                String msg = "Message_" + i;
                msgList.add(new KeyedMessage<String, String>(topic, i + "", msg));
                // msgList.add(new KeyedMessage<String, String>(topic, msg));//未指定key，Kafka会自动选择一个分区
                if (i % batchSize == 0) {
                    producer.send(msgList);
                    System.out.println("Send->[" + msgList + "]");
                    msgList.clear();
                    try {
                        sleep(SLEEP);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
            producer.send(msgList);
        } else {
            // 单个发送
            for (int i = 0; i < msgNum; i++) {
                KeyedMessage<String, String> msg = new KeyedMessage<String, String>(topic, i + "", "Message_" + i);
                // KeyedMessage<String, String> msg = new KeyedMessage<String, String>(topic, "Message_" + i);//未指定key，Kafka会自动选择一个分区
                producer.send(msg);
                System.out.println("Send->[" + msg + "]");
                try {
                    sleep(SLEEP);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        System.out.println("send done");
    }

    public static void main(String[] args) {
        JProducer pro = new JProducer(ConfigureAPI.KafkaProperties.TOPIC);
        pro.start();
    }
}
