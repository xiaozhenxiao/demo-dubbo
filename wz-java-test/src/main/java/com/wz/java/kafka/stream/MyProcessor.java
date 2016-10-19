package com.wz.java.kafka.stream;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.processor.TopologyBuilder;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.Stores;

/**
 * @author wangzhen
 * @version 1.0
 * @date 2016-10-19 11:27
 **/

public class MyProcessor implements Processor<String, String> {
    private ProcessorContext context;

    private KeyValueStore<String, Integer> kvStore;

    @Override
    @SuppressWarnings("unchecked")
    public void init(ProcessorContext context) {
        this.context = context;
        this.context.schedule(1000);
        this.kvStore = (KeyValueStore) context.getStateStore("Counts");
    }

    @Override
    public void process(String dummy, String line) {
        String[] words = line.toLowerCase().split(" ");

        for (String word : words) {
            Integer oldValue = this.kvStore.get(word);

            if (oldValue == null) {
                this.kvStore.put(word, 1);
            } else {
                this.kvStore.put(word, oldValue + 1);
            }
        }
    }

    @Override
    public void punctuate(long timestamp) {
        KeyValueIterator<String, Integer> iter = this.kvStore.all();

        while (iter.hasNext()) {
            KeyValue entry = iter.next();
            context.forward(entry.key, entry.value);
        }

        iter.close();
        context.commit();
    }

    @Override
    public void close() {
        this.kvStore.close();
    }

    public static void main(String[] args) {
        TopologyBuilder builder = new TopologyBuilder();

        builder.addSource("SOURCE", "src-topic")
                .addProcessor("PROCESS1", MyProcessor::new, "SOURCE")
                .addProcessor("PROCESS2", MyProcessor::new, "PROCESS1")
                .addProcessor("PROCESS3", MyProcessor::new, "PROCESS1")

                .addSink("SINK1", "sink-topic1", "PROCESS1")
                .addSink("SINK2", "sink-topic2", "PROCESS2")
                .addSink("SINK3", "sink-topic3", "PROCESS3");


        /**
         * 为利用本地状态仓库的优势，可使用TopologyBuilder.addStateStore方法以便在创建处理器拓扑时创建一个相应的本地状态仓库；
         * 或将一个已创建的本地状态仓库与现有处理器节点连接，通过TopologyBuilder.connectProcessorAndStateStores方法
         */
//        TopologyBuilder builder = new TopologyBuilder();

        builder.addSource("SOURCE", "src-topic")
                .addProcessor("PROCESS1", MyProcessor::new, "SOURCE")
                // create the in-memory state store "COUNTS" associated with processor "PROCESS1"
                .addStateStore(Stores.create("COUNTS").withStringKeys().withStringValues().inMemory().build(), "PROCESS1")
                .addProcessor("PROCESS2", MyProcessor::new /* the ProcessorSupplier that can generate MyProcessor3 */, "PROCESS1")
                .addProcessor("PROCESS3", MyProcessor::new /* the ProcessorSupplier that can generate MyProcessor3 */, "PROCESS1")

                // connect the state store "COUNTS" with processor "PROCESS2"
                .connectProcessorAndStateStores("PROCESS2", "COUNTS")
                .addSink("SINK1", "sink-topic1", "PROCESS1")
                .addSink("SINK2", "sink-topic2", "PROCESS2")
                .addSink("SINK3", "sink-topic3", "PROCESS3");
    }
}
