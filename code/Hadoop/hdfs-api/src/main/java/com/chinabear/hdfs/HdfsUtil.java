package com.chinabear.hdfs;

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

}
