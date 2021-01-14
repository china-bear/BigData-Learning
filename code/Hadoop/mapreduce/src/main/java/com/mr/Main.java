package com.mr;

import com.mr.common.avro.AvroMR;
import org.apache.hadoop.conf.Configuration;

public class Main {
    // https://github.com/owenliang/mr-demo

    public static void main(String[] args) {
        Configuration conf = new Configuration();

        try {
            // Avro的MR
            AvroMR avroMR = new AvroMR();
            avroMR.run(conf);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
