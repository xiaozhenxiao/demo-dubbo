package com.wz.spark.streaming;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Iterator;

/**
 * TODO
 *
 * @author wangzhen
 * @version 1.0
 * @date 2016-09-25 18:05
 **/

public class SparkStreamDemo {
    public static void main(String[] args) throws InterruptedException {

        /*
         * 第一步：配置SparkConf:
         * 1，至少2条线程：因为Spark Streaming应用程序在运行的时候，至少有一条
         * 线程用于不断的循环接收数据，并且至少有一条线程用于处理接受的数据(否则的话无法
         * 有线程用于处理数据，随着时间的推移，内存和磁盘都会不堪重负);
         * 2，对于集群而言，每个Executor一般肯定不止一个Thread，那对于处理Spark Streaming的
         * 应用程序而言，每个Executor一般分配多少Core比较合适？根据我们过去的经验，5个左右的
         * Core是最佳的（一个段子分配为奇数个Core表现最佳，例如3个、5个、7个Core等);
         * */
        /*
        SparkConf conf = new SparkConf().setMaster("local[2]"). setAppName("WordCountOnline");
        */
        //spark是Master的hostname
        SparkConf conf = new SparkConf().setMaster("spark://spark:7077").setAppName("WordCountOnline");
        /**
         * 第二步：创建SparkStreamingContext:
         * 1，这个是SparkStreaming应用程序所有功能的起始点和程序调度的核心
         * SparkStreamingContext的构建可以基于SparkConf参数，也可基于持久化的SparkStreamingContext的
         * 内容来恢复过来（典型的场景是Driver崩溃后重新启动，由于Spark Streaming具有连续7*24小时不，
         * 间断运行的特征所有需要在Driver重新启动后继续上衣系的状态，此时的状态恢复需要基于曾经的Checkpoint）
         *2，在一个Spark Streaming应用程序中可以创建若干个SparkStreamingContext对象，使用下一个
         * SparkStreamingContext之前需要把前面正在运行的SparkStreamingContext对象关闭掉，由此，
         * 我们获得一个重大的启发，SparkStreaming框架也只是Spark Core上的一个应用程序而已，
         * 只不过Spark Streaming框架箱运行的话需要Spark工程师写业务逻辑处理代码
         */
        JavaStreamingContext jsc = new JavaStreamingContext(conf, Durations.seconds(5));


        /**
         * 第三步：创建Spark Streaming输入数据来源input Stream:
         * 1，数据输入来源可以基于File、HDFS、Flume、Kafka、Socket等
         * 2, 在这里我们指定数据来源于网络Socket端口，Spark Streaming连接上该端口并在运行的时候一直监听
         * 该端口的数据（当然该端口服务首先必须存在）,并且在后续会根据业务需要不断的有数据产生(当然对于
         * Spark Streaming应用程序的运行而言，有无数据其处理流程都是一样的)
         * 3,如果经常在每间隔5秒钟没有数据的话不断的启动空的Job其实是会造成调度资源的浪费，
         * 因为并没有数据需要发生计算，所以实例的企业级生成环境的代码在具体提交Job前会判断是否有数据，如果没有的话就不再提交Job
         */
        JavaReceiverInputDStream lines = jsc.socketTextStream("Master", 9999);

        /*
        * 第四步：接下来就像对于 RDD 编程一样基于 DStream 进行编程！！！原因是 DStream 是 RDD 产生的模
        * 板（或者说类），在 Spark Streaming 具体发生计算前，其实质是把每个 Batch 的 DStream 的操作翻译成
        * 为对 RDD 的操作！！！
        * 对初始的 DStream 进行 Transformation 级别的处理，例如 map、 filter 等高阶函数等的编程，来进行具体
        * 的数据计算
        * 第 4.1 步：将每一行的字符串拆分成单个的单词
        */
        //如果是 Scala，由于SAM 转换，所以可以写成 val words = lines.flatMap { line => line.split(" ")}
        JavaDStream<String> words = lines.flatMap(new FlatMapFunction<String, String>() {

            @Override
            public Iterator<String> call(String line){
                return Arrays.asList(line.split(" ")).iterator();
            }
        });
        /*
        * 第四步：对初始的 DStream 进行 Transformation 级别的处理，例如 map、 filter 等高阶函数等的编程，来
        进行具体的数据计算
        * 第 4.2 步：在单词拆分的基础上对每个单词实例计数为 1，也就是 word => (word, 1)
        * mapToPair 接收的参数 PairFunction<String, String, Integer>
        */
        JavaPairDStream<String, Integer> pairs = words.mapToPair((String word) -> new Tuple2<>(word, 1));
        /*
        * 第四步：对初始的 DStream 进行 Transformation 级别的处理，例如 map、 filter 等高阶函数等的编程，来
        进行具体的数据计算
        * 第 4.3 步：在每个单词实例计数为 1 基础之上统计每个单词在文件中出现的总次数
        */
        //对相同的 Key，进行 Value 的累计（包括 Local 和 Reducer 级别同时 Reduce）
        //reduceByKey接收的参数Function2<Integer, Integer, Integer>
        JavaPairDStream<String, Integer> wordsCount = pairs.reduceByKey((Integer v1, Integer v2) -> v1 + v2);
        /*
        * 此处的 print 并不会直接触发Job的执行，因为现在的一切都是在 Spark Streaming 框架的控制之下的，对于 Spark Streaming
        * 而言具体是否触发真正的 Job 运行是基于设置的 Duration 时间间隔的
        * 诸位一定要注意的是 Spark Streaming 应用程序要想执行具体的 Job，对 Dstream 就必须有 output Stream 操
        作，
        * output Stream 有很多类型的函数触发，类 print、 saveAsTextFile、 saveAsHadoopFiles 等，最为重要的一个
        * 方法是 foraeachRDD,因为 Spark Streaming 处理的结果一般都会放在 Redis、 DB、 DashBoard 等上面，foreachRDD
        * 主要就是用用来完成这些功能的，而且可以随意的自定义具体数据到底放在哪里！！！
        */
        wordsCount.print();
        /*
        * Spark Streaming 执行引擎也就是 Driver 开始运行， Driver 启动的时候是位于一条新的线程中的，当然其内部有消息循环体，用于
        * 接受应用程序本身或者 Executor 中的消息；
        */
        jsc.start();
        jsc.awaitTermination();
        jsc.close();
    }
}
