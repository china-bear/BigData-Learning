package com.chinabear.hdfs;

import com.chinabear.hdfs.impl.HdfsUtilImpl;

import java.io.IOException;

/**
 * Created by bear on 2020/7/10
 */
public class testMain {
    public static void main(String[] args) {
        HdfsUtilImpl hdfsUtil = new HdfsUtilImpl();
        try {
            hdfsUtil.mkdir("/home/XXX/user/xiongyouguo/hadoopapi");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
