package com.wz.java.kafka;

import com.wz.java.kafka.ConfigureAPI.KafkaProperties;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 同一consumer group的多线程消费可以两种方法实现：
 * <p>
 * 1、实现单线程客户端，启动多个去消费
 * </p>
 * <p>
 * 2、在客户端的createMessageStreams里为topic指定大于1的线程数，再启动多个线程处理每个stream
 * </p>
 *
 * @author zsm
 * @version 1.0
 * @date 2016年9月27日 上午10:26:42
 * @parameter
 * @return
 */
public class JConsumer extends Thread {

    private ConsumerConnector consumer;
    private String topic;
    private final int SLEEP = 20;

    public JConsumer(String topic) {
        consumer = Consumer.createJavaConsumerConnector(this.consumerConfig());
        this.topic = topic;
    }

    private ConsumerConfig consumerConfig() {
        Properties props = new Properties();
        props.put("zookeeper.connect", KafkaProperties.ZK);
        props.put("group.id", KafkaProperties.GROUP_ID);
        props.put("auto.commit.enable", "true");// 默认为true，让consumer定期commit offset，zookeeper会将offset持久化，否则只在内存，若故障则再消费时会从最后一次保存的offset开始
        props.put("auto.commit.interval.ms", KafkaProperties.INTERVAL);// 经过INTERVAL时间提交一次offset
        props.put("auto.offset.reset", "largest");// What to do when there is no initial offset in ZooKeeper or if an offset is out of range
        props.put("zookeeper.session.timeout.ms", KafkaProperties.TIMEOUT);
        props.put("zookeeper.sync.time.ms", "200");
        return new ConsumerConfig(props);
    }

    @Override
    public void run() {
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, new Integer(1));// 线程数
        Map<String, List<KafkaStream<byte[], byte[]>>> streams = consumer.createMessageStreams(topicCountMap);
        KafkaStream<byte[], byte[]> stream = streams.get(topic).get(0);// 若上面设了多个线程去消费，则这里需为每个stream开个线程做如下的处理

        ConsumerIterator<byte[], byte[]> it = stream.iterator();
        MessageAndMetadata<byte[], byte[]> messageAndMetaData = null;
        while (it.hasNext()) {
            messageAndMetaData = it.next();
            System.out.println(MessageFormat.format("Receive->[ message:{0} , key:{1} , partition:{2} , offset:{3} ]",
                    new String(messageAndMetaData.message()), new String(messageAndMetaData.key()),
                    messageAndMetaData.partition() + "", messageAndMetaData.offset() + ""));
            try {
                sleep(SLEEP);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        JConsumer con = new JConsumer(KafkaProperties.TOPIC);
        con.start();
    }
}