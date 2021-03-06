package com.mr.mos.orc;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.LazyOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.orc.TypeDescription;
import org.apache.orc.mapred.OrcList;
import org.apache.orc.mapred.OrcStruct;
import org.apache.orc.mapreduce.OrcOutputFormat;

import java.io.IOException;
import java.util.StringTokenizer;

/**
 * Created by pipe on 3/15/17.
 */
public class WordCount {
    public static class TokenizerMapper extends Mapper<Object, Text, Text, IntWritable> {
        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();

        public void map(Object key, Text value, Mapper.Context context) throws IOException, InterruptedException {
            StringTokenizer itr = new StringTokenizer(value.toString());
            while (itr.hasMoreTokens()) {
                word.set(itr.nextToken());
                context.write(word, one);
            }
        }
    }

    public static class combinerReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        private IntWritable result = new IntWritable();

        public void reduce(Text key, Iterable<IntWritable> values, Reducer.Context context) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }
            result.set(sum);
            context.write(key, result);
        }
    }

    public static class IntSumReducer extends Reducer<Text, IntWritable, NullWritable, OrcStruct> {

        private MultipleOutputs mos;

        @Override
        protected void setup(Reducer.Context context) throws IOException, InterruptedException {
            mos = new MultipleOutputs(context);
        }

        public void reduce(Text key, Iterable<IntWritable> values, Reducer.Context context) throws IOException, InterruptedException {
            TypeDescription schema = TypeDescription.fromString("struct<key:string,ints:array<int>>");
            // createValue creates the correct value type for the schema
            OrcStruct pair = (OrcStruct) OrcStruct.createValue(schema);
            // get a handle to the list of ints
            OrcList<IntWritable> valueList = (OrcList<IntWritable>) pair.getFieldValue(1);
            final NullWritable nada = NullWritable.get();

            context.getConfiguration().set("orc.mapred.output.schema",schema.toString());

            pair.setFieldValue(0, key);
            valueList.clear();
            for(IntWritable val: values) {
                valueList.add(new IntWritable(val.get()));
            }
            String namedOutput = "mos" + valueList.get(0);
            mos.write(namedOutput, nada, pair, valueList.get(0).toString());
        }

        @Override
        protected void cleanup(Reducer.Context context) throws IOException, InterruptedException {
            mos.close();
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = new Job(conf, "word count");
        job.setJarByClass(WordCount.class);
        job.setMapperClass(TokenizerMapper.class);
        job.setCombinerClass(combinerReducer.class);
        job.setReducerClass(IntSumReducer.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        LazyOutputFormat.setOutputFormatClass(job, TextOutputFormat.class);

        for (int i = 1; i <= 3; i ++) {
            String namedOutput = "mos" + i;
            MultipleOutputs.addNamedOutput(job, namedOutput, OrcOutputFormat.class, Text.class, IntWritable.class);
        }
        MultipleOutputs.setCountersEnabled(job, true);

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}