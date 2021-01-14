package com.hdfs.fs;

import org.apache.avro.Schema;
import org.apache.avro.file.CodecFactory;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;

import org.apache.avro.mapred.FsInput;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.conf.Configuration;

import java.io.IOException;

public class AvroFileTest {
    public void run(Configuration conf) throws  IOException {
        // HDFS
        FileSystem dfs = FileSystem.get(conf);

        // 加载avro记录描述
        Schema.Parser parser = new Schema.Parser();
        Schema schema = parser.parse(getClass().getClassLoader().getResourceAsStream("user.avsc"));

        // 文件路径
        Path path = new Path("/avro.txt");

        // 打开文件
        FSDataOutputStream out = dfs.create(path);

        // avro对象序列化
        DatumWriter ser = new GenericDatumWriter<GenericRecord>();
        // avro文件流编码
        DataFileWriter codecOut = new DataFileWriter<GenericRecord>(ser);
        codecOut.setCodec(CodecFactory.snappyCodec());  // 开启snappy压缩
        codecOut.setSyncInterval(10 * 1024 * 1024); // 10MB打一个sync marker，方便MR分片

        // 初始化avro文件
        codecOut.create(schema, out);

        // 写入1000行记录
        for (int i = 0; i < 1000; i++) {
            GenericData.Record record = new GenericData.Record(schema);
            record.put("id", i);
            record.put("name", String.format("name-%d", i));
            codecOut.append(record);
        }
        codecOut.close();

        ////////////////

        // 读打开, 必须用avro这个api

        FsInput in = new FsInput(path, conf);
        // avro对象反序列化
        DatumReader de = new GenericDatumReader<GenericRecord>();
        // avro文件流解码
        DataFileReader codecIn = new DataFileReader<GenericRecord>(in, de);
        // 读取记录
        while (codecIn.hasNext()) {
            GenericRecord record = (GenericRecord) codecIn.next();
            System.out.printf("[Avro] id=%d name=%s\n", (Integer) record.get("id"), record.get("name"));
        }
        codecIn.close();
    }
}
