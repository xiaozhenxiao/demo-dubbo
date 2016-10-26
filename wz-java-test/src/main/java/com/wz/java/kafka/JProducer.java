package com.wz.java.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

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
    private final int msgNum = 100;

    public JProducer(String topic) {
        Properties props = new Properties();
        /**
         * The number of acknowledgments the producer requires the leader to have received
         * before considering a request complete. This controls the durability(耐久性) of records that are sent.
         * The following settings are common:
         * acks=0 If set to zero then the producer will not wait for any acknowledgment from the server at all.
         * The record will be immediately added to the socket buffer and considered(考虑) sent. No guarantee can be made that the server has received the record in this case,
         * and the retries configuration will not take effect (as the client won't generally know of any failures). The offset given back for each record will always be set to -1.
         * acks=1 This will mean the leader will write the record to its local log but will respond without awaiting full acknowledgement from all followers.
         * In this case should the leader fail immediately after acknowledging the record but before the followers have replicated it then the record will be lost.
         * acks=all This means the leader will wait for the full set of in-sync replicas to acknowledge the record.
         * This guarantees that the record will not be lost as long as at least one in-sync replica remains alive.
         * This is the strongest available guarantee.
         */
        props.put("acks", "all");
        /**
         * A list of host/port pairs to use for establishing the initial connection to the Kafka cluster.
         * The client will make use of all servers irrespective of which servers are specified here for
         * bootstrapping—this list only impacts the initial hosts used to discover the full set of servers.
         * This list should be in the form host1:port1,host2:port2,....
         * Since these servers are just used for the initial connection to discover the full cluster membership
         * (which may change dynamically), this list need not contain the full set of servers
         * (you may want more than one, though, in case a server is down).
         */
        props.put("bootstrap.servers", ConfigureAPI.KafkaProperties.BROKER_LIST);
        /**
         * Setting a value greater than zero will cause the client to resend any record whose send fails with a potentially transient error.
         * Note that this retry is no different than if the client resent the record upon receiving the error.
         * Allowing retries without setting max.in.flight.requests.per.connection to 1 will potentially change the ordering of records
         * because if two batches are sent to a single partition, and the first fails and is retried but the second succeeds,
         * then the records in the second batch may appear first.
         */
        props.put("retries", 0);
        /**
         * The producer will attempt to batch records together into fewer requests whenever multiple records are being sent to the same partition.
         * This helps performance on both the client and the server. This configuration controls the default batch size in bytes.
         * No attempt will be made to batch records larger than this size. Requests sent to brokers will contain multiple batches, one for each partition with data available to be sent.
         * A small batch size will make batching less common and may reduce throughput (a batch size of zero will disable batching entirely).
         * A very large batch size may use memory a bit more wastefully as we will always allocate a buffer of the specified batch size in anticipation of additional records.
         */
        props.put("batch.size", 16384);
        /**
         * The producer groups together any records that arrive in between request transmissions into a single batched request.
         * Normally this occurs only under load when records arrive faster than they can be sent out.
         * However in some circumstances(情况) the client may want to reduce the number of requests even under moderate(中等) load.
         * This setting accomplishes this by adding a small amount of artificial delay—that is,
         * rather than immediately sending out a record the producer will wait for up to the given delay to allow other records
         * to be sent so that the sends can be batched together. This can be thought of as analogous to Nagle's algorithm in TCP.
         * This setting gives the upper bound on the delay for batching: once we get batch.size worth of records for a partition
         * it will be sent immediately regardless of this setting, however if we have fewer than this many bytes accumulated for
         * this partition we will 'linger' for the specified time waiting for more records to show up.
         * This setting defaults to 0 (i.e. no delay). Setting linger.ms=5, for example,
         * would have the effect of reducing the number of requests sent but would add up to 5ms of latency to records
         * sent in the absense of load.
         */
        props.put("linger.ms", 1);
        /**
         * The total bytes of memory the producer can use to buffer records waiting to be sent to the server.
         * If records are sent faster than they can be delivered to the server the producer will block for max.block.ms after
         * which it will throw an exception. This setting should correspond roughly to the total memory the producer will use,
         * but is not a hard bound since not all memory the producer uses is used for buffering.
         * Some additional memory will be used for compression (if compression is enabled) as well as for maintaining in-flight requests.
         */
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        // 提供自定义的分区函数将消息写到分区上，未指定的话Kafka根据hash(messageKey)%partitionNum确定写到哪个分区
//        props.put("partitioner.class", "com.zsm.kfkdemo.MyPartitioner");
        producer = new KafkaProducer<>(props);
        this.topic = topic;
    }

    @Override
    public void run() {
        for (int i = 0; i < msgNum; i++) {
            String msg = "===============================================Message_" + i;
            // KeyedMessage<String, String> msg = new KeyedMessage<String, String>(topic, "Message_" + i);//未指定key，Kafka会自动选择一个分区
            producer.send(new ProducerRecord<>(topic, Integer.toString(i), msg));
            System.out.println("Send->[" + msg + "]");
            try {
                sleep(SLEEP);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        System.out.println("send done");
    }

    public static void main(String[] args) {
        JProducer pro = new JProducer(ConfigureAPI.KafkaProperties.TOPIC);
        pro.start();
    }
}
