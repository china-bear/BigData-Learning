package com.chinabear.hdfs;

import org.apache.hadoop.conf.Configuration;

import java.io.IOException;

/**
 * Created by bear on 2020/7/8
 */
public interface HdfsUtil {

    /**
     * 创建文件目录
     * @param path
     */
    public Boolean mkdir(String path) throws IOException;

    /**
     * 删除文件或者是某个目录
     * @param path
     * @return
     */
    public Boolean rmDir(String path,boolean recursive) throws IllegalArgumentException,IOException;

    /**
     * 判断目录是否存在
     * @param filePath
     * @param mk: 是否自动创建该目录
     * @return
     */
    public Boolean isExistDir(String filePath, Boolean mk);



}
