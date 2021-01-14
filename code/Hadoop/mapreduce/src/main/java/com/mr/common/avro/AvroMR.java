package com.mr.common.avro;

import org.apache.avro.Schema;
import org.apache.hadoop.io.compress.SnappyCodec;
import org.apache.avro.generic.GenericData;
import org.apache.avro.mapred.AvroKey;
import org.apache.avro.mapred.AvroValue;
import org.apache.avro.mapreduce.AvroJob;
import org.apache.avro.mapreduce.AvroKeyInputFormat;
import org.apache.avro.mapreduce.AvroKeyOutputFormat;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;

import java.io.IOException;

public class AvroMR {

    private static class Mapper extends org.apache.hadoop.mapreduce.Mapper<AvroKey<GenericData.Record>, NullWritable, AvroKey<GenericData.Record>, AvroValue<Long>> {
        @Override
        protected void map(AvroKey<GenericData.Record> key, NullWritable value, Context context) throws IOException, InterruptedException {
            Integer id = (Integer)key.datum().get("id");
            String name = (String)key.datum().get("name");
            System.out.printf("%d -> %s", id, name);

            Long val = new Long(1);
            context.write(key, new AvroValue<Long>(val));   // avro record -> 1次
        }
    }

    private static class Reducer extends org.apache.hadoop.mapreduce.Reducer<AvroKey<GenericData.Record>, AvroValue<Long>, AvroKey<GenericData.Record>, NullWritable> {
        @Override
        protected void reduce(AvroKey<GenericData.Record> key, Iterable<AvroValue<Long>> values, Context context) throws IOException, InterruptedException {
            context.write(key, NullWritable.get()); // 原样输出avro record
        }
    }

    public void run(Configuration conf) throws Exception {
        Path input = new Path("/avro.txt");
        Path output = new Path("/avro-mr");

        // 删除之前的结果
        FileSystem.get(conf).delete(output, true);

        // 加载avro记录描述
        Schema.Parser parser = new Schema.Parser();
        Schema schema = parser.parse(getClass().getClassLoader().getResourceAsStream("user.avsc"));

        // 创建Job
        Job job = new Job(conf, "AvroMR");
        job.setJarByClass(AvroMR.class); // mapper/reducer实现在该类内部，需要设置这个

        /// 输入
        AvroKeyInputFormat.addInputPath(job, input);   // 文件路径
        job.setInputFormatClass(AvroKeyInputFormat.class); // 文件格式
        job.setMapperClass(AvroMR.Mapper.class);   // mapper实现
        AvroJob.setMapOutputKeySchema(job, schema); // 中间输出key依旧是user结构
        AvroJob.setMapOutputValueSchema(job, Schema.create(Schema.Type.LONG));   // 中间输出value是avro序列化的long

        // 输出
        AvroKeyOutputFormat.setOutputPath(job, output);    // 文件路径
        AvroKeyOutputFormat.setCompressOutput(job, true);   // 输出压缩
        AvroKeyOutputFormat.setOutputCompressorClass(job, SnappyCodec.class);   //  snappy压缩
        AvroJob.setOutputKeySchema(job, schema);    // 输出avro key的schema
        job.setOutputFormatClass(AvroKeyOutputFormat.class);   // 文件格式仅输出key
        job.setReducerClass(AvroMR.Reducer.class); // reducer实现

        // 等待任务执行，打印详情
        job.waitForCompletion(true);
    }
}
