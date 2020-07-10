package com.chinabear.hdfs.impl;

/*import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;*/
import com.chinabear.hdfs.HdfsUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


/**
 * Created by bear on 2020/7/9
 */
public class HdfsUtilImpl implements HdfsUtil {

    private static final String HDFS_PATH = "hdfs://XXX:9000/";
    private static FileSystem FS;

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

    public void close(FileSystem FS) throws IOException {
        FS.close();
    }

}
