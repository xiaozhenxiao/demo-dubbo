package com.wz.java.kafka;

import com.wz.java.kafka.ConfigureAPI.KafkaProperties;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStreamBuilder;

import java.text.MessageFormat;
import java.util.*;

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

    //Kafka Streams Configs
    private ConsumerConfig consumerConfig() {
        Properties props = new Properties();
        props.put("zookeeper.connect", KafkaProperties.ZK);
        props.put("group.id", KafkaProperties.GROUP_ID);
        props.put("auto.commit.enable", "true");// 默认为true，让consumer定期commit offset，zookeeper会将offset持久化，否则只在内存，若故障则再消费时会从最后一次保存的offset开始
        props.put("auto.commit.interval.ms", KafkaProperties.INTERVAL);// 经过INTERVAL时间提交一次offset
        props.put("auto.offset.reset", "largest");// What to do when there is no initial offset in ZooKeeper or if an offset is out of range
        props.put("zookeeper.session.timeout.ms", KafkaProperties.TIMEOUT);
        props.put("zookeeper.connection.timeout.ms", "100000");
        props.put("rebalance.backoff.ms", "20000");
        props.put("rebalance.max.retries", "5");
        props.put("consumer.timeout.ms", "-1");
        props.put("fetch.min.bytes", "1");
        return new ConsumerConfig(props);
    }

    @Override
    public void run() {
        /*Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
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
        }*/
        automaticOffsetCommitting();
    }

    public void streams(){
        Map<String, Object> props = new HashMap<>();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "my-stream-processing-application");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, Serdes.StringSerde.class);
        props.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG, Serdes.StringSerde.class);
        StreamsConfig config = new StreamsConfig(props);

        KStreamBuilder builder = new KStreamBuilder();
        builder.stream("my-input-topic").mapValues((Object value) -> ((String)value).length()).to("my-output-topic");

        KafkaStreams streams = new KafkaStreams(builder, config);
        streams.start();
    }

    /**
     * 自动提交Offset
     */
    public void automaticOffsetCommitting() {
        Properties props = new Properties();
        props.put("bootstrap.servers", ConfigureAPI.KafkaProperties.BROKER_LIST);
        props.put("group.id", "test");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
//        consumer.subscribe(Arrays.asList("foo", "bar"));
        consumer.subscribe(Arrays.asList(KafkaProperties.TOPIC));
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records)
                System.out.printf("offset = %d, key = %s, value = %s ", record.offset(), record.key(), record.value());
        }
    }

    public void manualOffsetControl() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "test");
        props.put("enable.auto.commit", "false");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList("foo", "bar"));
        final int minBatchSize = 200;
        List<ConsumerRecord<String, String>> buffer = new ArrayList<>();
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                buffer.add(record);
            }
            if (buffer.size() >= minBatchSize) {
//                insertIntoDb(buffer);
                consumer.commitSync();
                buffer.clear();
            }
        }
        /**
         * The above example uses commitSync to mark all received messages as committed.
         * In some cases you may wish to have even finer control over which messages have been committed by specifying an offset explicitly.
         * In the example below we commit offset after we finish handling the messages in each partition.
         */
        /*boolean running = true;
        try {
            while (running) {
                ConsumerRecords<String, String> records = consumer.poll(Long.MAX_VALUE);
                for (TopicPartition partition : records.partitions()) {
                    List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);
                    for (ConsumerRecord<String, String> record : partitionRecords) {
                        System.out.println(record.offset() + ": " + record.value());
                    }
                    long lastOffset = partitionRecords.get(partitionRecords.size() - 1).offset();
                    consumer.commitSync(Collections.singletonMap(partition, new OffsetAndMetadata(lastOffset + 1)));
                }
            }
        } finally {
            consumer.close();
        }*/

    }

    public void MultiThreadedProcessing(){
        /**
         * MultiThreadedProcessing类处理
        */
    }

    public static void main(String[] args) {
        JConsumer con = new JConsumer(KafkaProperties.TOPIC);
        con.start();
    }
}