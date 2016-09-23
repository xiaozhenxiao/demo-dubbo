package com.wz.dubbo;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import scala.Tuple2;

import java.util.Arrays;

/**
 * Hello world!
 */
public class WordCountApp {
    public static void main(String[] args) {
        String inputPath = "file:///home/wangzhen/spark-project/input";
//        String inputPath = "hdfs://soy1:9000/mapreduces/word.txt";
//        String outputPath = "hdfs://soy1:9000/spark/wordCount";
        String outputPath = "file:///home/wangzhen/spark-project/output";
        if(args != null){
            if(args.length > 0)
                inputPath = args[0];
            if(args.length > 1)
                outputPath = args[1];
        }
        /**
         * 1、创建SparkConf对象，设置Spark应用程序的配置信息
         */
        SparkConf conf = new SparkConf()
                //设置Spark应用程序的名称
                .setAppName(WordCountApp.class.getSimpleName());
        /**
         * 2、创建SparkContext对象，Java开发使用JavaSparkContext；Scala开发使用SparkContext
         * 在Spark中，SparkContext负责连接Spark集群，创建RDD、累积量和广播量等。
         * Master参数是为了创建TaskSchedule（较低级的调度器，高层次的调度器为DAGSchedule），如下：
         *         如果setMaster("local")则创建LocalSchedule；
         *         如果setMaster("spark")则创建SparkDeploySchedulerBackend。在SparkDeploySchedulerBackend的start函数，会启动一个Client对象，连接到Spark集群。
         */
        JavaSparkContext sc = new JavaSparkContext(conf);

        /**
         * 3、sc中提供了textFile方法是SparkContext中定义的，如下：
         *         def textFile(path: String): JavaRDD[String] = sc.textFile(path)
         * 用来读取HDFS上的文本文件、集群中节点的本地文本文件或任何支持Hadoop的文件系统上的文本文件，它的返回值是JavaRDD[String]，是文本文件每一行
         */
        JavaRDD<String> lines = sc.textFile(inputPath);

        /**
         * 4、将行文本内容拆分为多个单词
         * lines调用flatMap这个transformation算子（参数类型是FlatMapFunction接口实现类）返回每一行的每个单词
         */
        JavaRDD<String> words = lines.flatMap(new FlatMapFunction<String, String>() {
            private static final long serialVersionUID = -3243665984299496473L;

            @Override
            public Iterable<String> call(String line) throws Exception {
                return Arrays.asList(line.split("\t"));
            }
        });

        /**
         * 5、将每个单词的初始数量都标记为1个
         * words调用mapToPair这个transformation算子（参数类型是PairFunction接口实现类，PairFunction<String, String, Integer>的三个参数是<输入单词, Tuple2的key, Tuple2的value>），返回一个新的RDD，即JavaPairRDD
         */
        JavaPairRDD<String, Integer> pairs = words.mapToPair(new PairFunction<String, String, Integer>() {
            private static final long serialVersionUID = -7879847028195817507L;

            @Override
            public Tuple2<String, Integer> call(String word) throws Exception {
                return new Tuple2<String, Integer>(word, 1);
            }
        });

        /**
         * 6、计算每个相同单词出现的次数
         * pairs调用reduceByKey这个transformation算子（参数是Function2接口实现类）对每个key的value进行reduce操作，返回一个JavaPairRDD，这个JavaPairRDD中的每一个Tuple的key是单词、value则是相同单词次数的和
         */
        JavaPairRDD<String, Integer> wordCount = pairs.reduceByKey(new Function2<Integer, Integer, Integer>() {
            private static final long serialVersionUID = -4171349401750495688L;

            @Override
            public Integer call(Integer v1, Integer v2) throws Exception {
                return v1 + v2;
            }
        });

        /**
         * 7、使用foreach这个action算子提交Spark应用程序
         * 在Spark中，每个应用程序都需要transformation算子计算，最终由action算子触发作业提交
         */
        wordCount.foreach(new VoidFunction<Tuple2<String, Integer>>() {
            private static final long serialVersionUID = -5926812153234798612L;

            @Override
            public void call(Tuple2<String, Integer> wordCount) throws Exception {
                System.out.println(wordCount._1 + ":" + wordCount._2);
            }
        });

        /**
         * 8、将计算结果文件输出到文件系统
         *         HDFS：
         *             使用新版API（org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;）
         *                 wordCount.saveAsNewAPIHadoopFile("hdfs://ns1/spark/wordcount", Text.class, IntWritable.class, TextOutputFormat.class, new Configuration());
         *             使用旧版API（org.apache.hadoop.mapred.JobConf;org.apache.hadoop.mapred.OutputFormat;）
         *                 wordCount.saveAsHadoopFile("hdfs://ns1/spark/wordcount", Text.class, IntWritable.class, OutputFormat.class, new JobConf(new Configuration()));
         *             使用默认TextOutputFile写入到HDFS(注意写入HDFS权限，如无权限则执行：hdfs dfs -chmod -R 777 /spark)
         *                 wordCount.saveAsTextFile("hdfs://soy1:9000/spark/wordCount");
         */
        wordCount.saveAsTextFile(outputPath);

        /**
         * 9、关闭SparkContext容器，结束本次作业
         */
        sc.close();
    }
}
