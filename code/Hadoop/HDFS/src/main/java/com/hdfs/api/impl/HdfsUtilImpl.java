package com.hdfs.api.impl;


import com.hdfs.api.HdfsUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.hdfs.protocol.DatanodeInfo;

import java.io.IOException;


/**
 * Created by bear on 2020/7/9
 */
public class HdfsUtilImpl implements HdfsUtil {

    private static final String HDFS_PATH = "hdfs://xxx.xxx.lycc.qihoo.net:9000/";
    private static FileSystem FS;


/*    private Logger logger  = Logger.getLogger(HdfsUtilImpl.class);
http://hadoop.apache.org/docs/r2.8.0/hadoop-project-dist/hadoop-common/api/org/apache/hadoop/fs/HarFileSystem.html  */

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

    HarFileSystem HarFS = new HarFileSystem();


    @Override
    public Boolean mkdir(String path) throws IOException {
        return FS.mkdirs(new Path(path));
    }


    @Override
    public Boolean rmDir(String path, boolean recursive) throws IllegalArgumentException, IOException {
        return FS.delete(new Path(path), recursive);
    }

    @Override
    public Boolean reName(String srcPath, String dstPath) throws IOException {
        return FS.rename(new Path(srcPath), new Path(dstPath));
    }

    @Override
    public Boolean isExistDir(String path, Boolean mk) throws IOException {
        boolean flag = false;
        if (!FS.exists(new Path(path))) {
            if(mk) {
                FS.mkdirs(new Path(path));
            }
        } else {
            flag = true;
        }
        return  flag;
    }

    @Override
    public void copyFromLocal(Boolean delSrc, Boolean overwrite, String srcFile, String destPath) throws IOException {
       FS.copyFromLocalFile(delSrc,overwrite,new Path(srcFile), new Path(destPath));
    }

    @Override
    public void copyToLocal(String srcFile, String destFile) throws IOException {
        FS.copyToLocalFile(false, new Path(srcFile), new Path(destFile), true);
    }

    @Override
    public RemoteIterator<LocatedFileStatus> listFiles(String path, boolean recursive) throws IOException {
        return FS.listFiles(new Path(path), recursive);
    }

    @Override
    public FileStatus[] listFileStatus(String path) throws IOException {
        return FS.listStatus(new Path(path));
    }

    @Override
    public boolean setReplication(String path, short replication) throws IOException {
        return  FS.setReplication(new Path(path), replication);
    }

    @Override
    public DatanodeInfo[] getHDFSNodes() throws IOException {
        DistributedFileSystem DFS = (DistributedFileSystem)FS;
        return DFS.getDataNodeStats();
    }

    @Override
    public BlockLocation[] getFileBlockLocations(String filePath) throws IOException {
        FileStatus filestatus = FS.getFileStatus(new Path(filePath));
        return FS.getFileBlockLocations(filestatus, 0, filestatus.getLen());
    }

    @Override
    public int getDirDepth(String path) throws IOException {
        return path.substring(path.indexOf("/home"))
                .split("/")
                .length;
    }


    public void close(FileSystem FS) throws IOException {
        FS.close();
    }

}
