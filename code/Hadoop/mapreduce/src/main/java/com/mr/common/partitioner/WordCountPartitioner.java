package com.mr.common.partitioner;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @program: BigData-Learning
 * @description: WordCount
 * @author: Mr.Bear
 * @create: 2020-06-27 11:07
 **/

public class WordCountPartitioner {

    public static final List<String> WORD_LIST = Arrays.asList("Spark", "Hadoop", "HBase", "Storm", "Flink", "Hive");

    public static class TokenizerMappePartitioner extends Mapper<Object, Text, Text, IntWritable> {

        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();

        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            StringTokenizer itr = new StringTokenizer(value.toString());
            while (itr.hasMoreTokens()) {
                word.set(itr.nextToken());
                context.write(word, one);
            }
        }
    }


    public static class IntSumReducerPartitioner extends Reducer<Text,IntWritable,Text,IntWritable> {

        private IntWritable result = new IntWritable();

        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }
            result.set(sum);
            context.write(key, result);
        }
    }

    /**
     * 自定义partitioner,按照单词分区
     */
    public class CustomPartitioner extends Partitioner<Text, IntWritable> {

        public int getPartition(Text text, IntWritable intWritable, int numPartitions) {
            return WORD_LIST.indexOf(text.toString());
        }
    }


    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        Configuration conf = new Configuration();
        String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
        if (otherArgs.length < 2) {
            System.err.println("Usage: wordcount <in> [<in>...] <out>");
            System.exit(2);
        }
        // 创建一个Job
        Job job = new Job(conf, "word count partitioner");
        // 创建一个Job
        job.setJarByClass(WordCountPartitioner.class);
        // 设置Mapper和Reducer
        job.setMapperClass(TokenizerMappePartitioner.class);
        //combiner是map运算后的可选操作，它实际上是一个本地化的reduce操作，它主要是在map计算出中间文件后做一个简单的合并重复key值的操作
        job.setCombinerClass(IntSumReducerPartitioner.class);
        job.setReducerClass(IntSumReducerPartitioner.class);

        // 设置自定义分区规则
        job.setPartitionerClass(CustomPartitioner.class);
        // 设置reduce个数
        job.setNumReduceTasks(WORD_LIST.size());

        // 设置Reducer输出key和value的类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        // 设置作业输入文件和输出文件的路径
        for (int i = 0; i < otherArgs.length - 1; ++i) {
            FileInputFormat.addInputPath(job, new Path(otherArgs[i]));
        }
        FileOutputFormat.setOutputPath(job,  new Path(otherArgs[otherArgs.length - 1]));
        // 将作业提交到群集并等待它完成，参数设置为true代表打印显示对应的进度;根据作业结果,终止当前运行的Java虚拟机,退出程序
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

}
