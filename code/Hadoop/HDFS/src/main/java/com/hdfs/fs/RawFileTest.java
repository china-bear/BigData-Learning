package com.hdfs.fs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;
import org.apache.hadoop.io.compress.CompressionInputStream;
import org.apache.hadoop.io.compress.CompressionOutputStream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RawFileTest {

    public void run(Configuration conf) throws IOException {
        // HDFS
        FileSystem dfs = FileSystem.get(conf);

        // 文件路径
        Path path = new Path("/raw.gz");

        // GZIP编码器
        CompressionCodec codec = new CompressionCodecFactory(conf).getCodecByName("gzip");

        // 写打开
        FSDataOutputStream out = dfs.create(path);
        // 压缩编码流
        CompressionOutputStream codecOut = codec.createOutputStream(out);

        // 写入数据
        for (int i = 0; i < 1000; i++) {
            codecOut.write("hello world\n".getBytes());
        }
        codecOut.close();

        // 读打开
        FSDataInputStream in = dfs.open(path);
        // 压缩解码流
        CompressionInputStream codecIn = codec.createInputStream(in);
        // 按行读
        BufferedReader bufIn = new BufferedReader(new InputStreamReader(codecIn));
        while (bufIn.ready()) {
            String line = bufIn.readLine();
            System.out.printf("[Raw] %s\n", line);
        }
        bufIn.close();
    }
}
