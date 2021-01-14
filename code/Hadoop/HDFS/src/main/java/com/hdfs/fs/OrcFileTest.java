package com.hdfs.fs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.ql.exec.vector.BytesColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.LongColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.VectorizedRowBatch;
import org.apache.orc.*;

import java.io.IOException;

public class OrcFileTest {

    public void run(Configuration conf) throws  IOException {
        // 数据结构描述
        TypeDescription schema = TypeDescription.createStruct();
        schema.addField("id", TypeDescription.createInt());
        schema.addField("name", TypeDescription.createString());

        // 文件路径
        Path path = new Path("/orc.txt");

        // 写打开
        Writer out = OrcFile.createWriter(path, OrcFile.writerOptions(conf).
                setSchema(schema).compress(CompressionKind.SNAPPY).overwrite(true));

        // 写入1000行
        VectorizedRowBatch rowBatch = schema.createRowBatch();  // 每个batch内的N行按列存储
        LongColumnVector idColumn = (LongColumnVector)rowBatch.cols[0]; // id 列
        BytesColumnVector nameColumn = (BytesColumnVector)rowBatch.cols[1]; // name列
        for (int i = 0; i < 1000; i++) {
            idColumn.vector[rowBatch.size] = i;
            String name = String.format("name-%d", i);
            nameColumn.setVal(rowBatch.size, name.getBytes());
            if (++rowBatch.size == rowBatch.getMaxSize()) {
                out.addRowBatch(rowBatch);
                rowBatch.reset();
            }
        }
        if (rowBatch.size != 0) {
            out.addRowBatch(rowBatch);
            rowBatch.reset();
        }
        out.close();

        /////////

        // 读打开
        Reader in = OrcFile.createReader(path, OrcFile.readerOptions(conf));
        // 解析schema
        VectorizedRowBatch inBatch = in.getSchema().createRowBatch();
        // 流解析文件
        RecordReader rows = in.rows();
        while (rows.nextBatch(inBatch)) {   // 读1个batch
            // 列式读取
            LongColumnVector idCol = (LongColumnVector)inBatch.cols[0]; // id 列
            BytesColumnVector nameCol = (BytesColumnVector)inBatch.cols[1]; // name列
            for (int i = 0; i < inBatch.size; i++) {
                // 注意：因为是列存储，所以name列是一个大buffer存储的，需要从里面的start偏移量取length长度的才是该行的列值
                System.out.printf("[Orc] id=%d name=%s\n", idCol.vector[i], new String(nameCol.vector[i], nameCol.start[i], nameCol.length[i]));
            }
        }
        rows.close();
    }
}
