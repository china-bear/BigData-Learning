package com.chinabear.hdfs.impl;


import com.chinabear.hdfs.HdfsUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


/**
 * Created by bear on 2020/7/9
 */
public class HdfsUtilImpl implements HdfsUtil {

    private static final String HDFS_PATH = "hdfs://XXX:9000/";
    private static FileSystem FS;

    private Logger logger  = Logger.getLogger(HdfsUtilImpl.class);

/*    @Value("${hdfsUrl}")*/
    private String hdfsUrl;

    static {
        try {
            Configuration conf = new Configuration();
            conf.set("fs.defaultFS",HDFS_PATH);
            conf.set("dfs.replication", "3");
            FS = FileSystem.get(conf);
        }  catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Boolean mkdir(String path) throws IOException {
        return FS.mkdirs(new Path(path));
    }


    @Override
    public Boolean rmDir(String path, boolean recursive) throws IllegalArgumentException, IOException {
        return FS.delete(new Path(path), recursive);
    }

    @Override
    public Boolean isExistDir(String path, Boolean mk){
        return  true;
    }

    public void close(FileSystem FS) throws IOException {
        FS.close();
    }





}
