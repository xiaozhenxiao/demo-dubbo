package com.wz.java.kafka;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;

/**
 * 璇︾粏鍙互鍙傝�锛歨ttps://cwiki.apache.org/confluence/display/KAFKA/Consumer+Group+ Example
 * 
 * @author Fung
 *
 */
public class ConsumerDemo {
    private final ConsumerConnector consumer;

    private final String topic;

    private ExecutorService executor;

    public ConsumerDemo(String zkip, String groupId, String topic) {
        consumer = Consumer.createJavaConsumerConnector(createConsumerConfig(zkip, groupId));
        this.topic = topic;
    }

    public void shutdown() {
        if (consumer != null)
            consumer.shutdown();
        if (executor != null)
            executor.shutdown();
    }

    @SuppressWarnings("rawtypes")
    public void run(int numThreads) {
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, new Integer(numThreads));
        // 返回的Map包含所有的Topic以及对应的KafkaStream
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);

        // 创建Java线程池
        executor = Executors.newFixedThreadPool(numThreads);
        // now create an object to consume the messages
        // 创建 consume 线程消费messages
        int threadNumber = 0;
        for (final KafkaStream stream : streams) {
//            executor.submit(new ConsumerMsgTask(stream, threadNumber));
            ConsumerIterator<byte[], byte[]> it = stream.iterator();
            MessageAndMetadata<byte[], byte[]> messageAndMetaData = null;
            while (it.hasNext()) {
                messageAndMetaData = it.next();
                System.out.println(MessageFormat.format("Receive->[ message:{0} , key:{1} , partition:{2} , offset:{3} ]",
                        new String(messageAndMetaData.message()), new String(messageAndMetaData.key()),
                        messageAndMetaData.partition() + "", messageAndMetaData.offset() + ""));
                try {
                    Thread.sleep(200);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            threadNumber++;
        }
    }

    private static ConsumerConfig createConsumerConfig(String a_zookeeper, String a_groupId) {
        Properties props = new Properties();
        // 指定连接的Zookeeper集群，通过该集群来存储连接到某个Partition的Consumer的Offerset
        props.put("zookeeper.connect", a_zookeeper);
        // consumer group 的ID
        props.put("group.id", a_groupId);
        // Kafka等待Zookeeper的响应时间（毫秒）
        props.put("zookeeper.session.timeout.ms", "400");
        // ZooKeeper 的‘follower’可以落后Master多少毫秒
        props.put("zookeeper.sync.time.ms", "2000");
        // consumer更新offerset到Zookeeper的时间
        props.put("auto.commit.interval.ms", "1000");
        props.put("rebalance.backoff.ms", "2000");
        return new ConsumerConfig(props);
    }

    public static void main(String[] args) {
        /*String zooKeeper = args[0];
        String groupId = args[1];
        String topic = args[2];
        int threads = Integer.parseInt(args[3]);*/
        String zooKeeper = "spark:2181,spark2:2181,spark3:2181";
        String groupId = "test";
        String topic = "testTopic";
        int threads = 1;

        ConsumerDemo example = new ConsumerDemo(zooKeeper, groupId, topic);
        example.run(threads);
         //因为consumer的offerset并不是实时的传送到zookeeper（通过配置来制定更新周期），所以shutdown Consumer的线程，有可能会读取重复的信息
        //增加sleep时间，让consumer把offset同步到zookeeper
        try {
            Thread.sleep(10000);
        } catch (InterruptedException ie) {

        }
        example.shutdown();
    }
}
