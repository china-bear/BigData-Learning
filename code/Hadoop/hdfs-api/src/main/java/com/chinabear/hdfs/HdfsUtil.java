package com.chinabear.hdfs;

import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.RemoteIterator;
import org.apache.hadoop.hdfs.protocol.DatanodeInfo;

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
     * 目录文件 重命名(mv)
     * @param srcPath HDFS源路径
     * @param dstPath HDFS目标路径
     * @return
     * @throws IOException IOException
     */
    public Boolean reName(String srcPath, String dstPath) throws IOException;

    /**
     * 判断目录是否存在
     * @param filePath
     * @param mk: 是否自动创建该目录
     * @return
     * @throws IOException IOException
     */
    public Boolean isExistDir(String filePath, Boolean mk) throws IOException;

    /**
     * 文件上传
     * @param delSrc 是否删除源文件
     * @param overwrite 是否覆盖原文件
     * @param srcFile   源文件
     * @param dstPath  目的路径
     */
    public void copyFromLocal(Boolean delSrc, Boolean overwrite, String srcFile, String dstPath) throws IOException;

    /**
     * 从HDFS下载文件
     * @param srcFile
     * @param dstFile
     */
    public void copyToLocal(String srcFile,String dstFile) throws IOException;


    /**
     * 递归查看目录下所有文件
     * @param path  HDFS 路径
     * @param recursive 递归标志
     * @return RemoteIterator<LocatedFileStatus>
     * @throws IOException IOException
     */
    public  RemoteIterator<LocatedFileStatus> listFiles(String path, boolean recursive) throws IOException;

    /**
     * 查看目录下文件及目录
     * @param path  HDFS 路径
     * @return FileStatus[]
     * @throws IOException IOException
     */
    public  FileStatus[] listFileStatus(String path)throws IOException ;


    /**
     * 设置
     * @param path  HDFS 路径
     * @param replication  副本数
     * @return
     * @throws IOException IOException
     */
    public  boolean setReplication(String path, short replication) throws IOException;

    /**
     * 获取HDFS集群节点信息
     * @return
     */
    public  DatanodeInfo[] getHDFSNodes() throws IOException;

    /**
     * 查找某个文件在 HDFS集群的位置
     * @param path
     * @return
     */
    public BlockLocation[] getFileBlockLocations(String path) throws IOException;

}
