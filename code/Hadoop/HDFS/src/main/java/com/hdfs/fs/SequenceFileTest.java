package com.hdfs.fs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;

import java.io.IOException;

public class SequenceFileTest {

    public void run(Configuration conf) throws IOException {
        // 路径
        Path path = new Path("/sequence.txt");

        // GZIP编码器
        CompressionCodec codec = new CompressionCodecFactory(conf).getCodecByName("gzip");

        // 写打开
        SequenceFile.Writer out = SequenceFile.createWriter(conf,
                SequenceFile.Writer.file(path),
                SequenceFile.Writer.keyClass(Text.class),   // key类型
                SequenceFile.Writer.valueClass(Text.class), // value类型
                SequenceFile.Writer.compression(SequenceFile.CompressionType.BLOCK, codec),  // 块级gzip压缩
                SequenceFile.Writer.syncInterval(10 * 1024 * 1024));   // 10MB打一个sync marker，方便MR分片

        // 写入数据
        for (int i = 0; i < 1000; i++) {
            out.append(new Text(String.format("key-%d", i)), new Text(String.format("value-%d", i)));
        }
        out.close();

        // 读打开
        SequenceFile.Reader in = new SequenceFile.Reader(conf,
                SequenceFile.Reader.file(path));

        // 循环读取数据
        Text key = new Text();
        Text value = new Text();
        while (in.next(key, value)) {
            System.out.printf("[Sequence] %s -> %s\n", key.toString(), value.toString());
        }
        in.close();
    }
}
