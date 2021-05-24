package com.hdfs.api.impl;


import com.hdfs.api.HdfsUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.hdfs.protocol.DatanodeInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by bear on 2020/7/9
 * https://github.com/Nana0606/hadoop_example/blob/master/hdfs_operations/
 */
public class HdfsUtilImplNoThrows{

    private static final Logger LOGGER = LoggerFactory.getLogger(HdfsUtil.class);
    private static final String HDFS_PATH = "hdfs://namenode.safe.lycc.qihoo.net:9000/";
    private static FileSystem FS;
    private static FileSystem localFS;


    static {
        try {
            Configuration conf = new Configuration();
            conf.set("fs.defaultFS",HDFS_PATH);
            conf.set("dfs.replication", "3");
            FS = FileSystem.get(conf);
            localFS = FileSystem.getLocal(conf);
        }  catch (IOException e) {
            e.printStackTrace();
        }
    }

    HarFileSystem HarFS = new HarFileSystem();

    public void list(String srcPath){
        try {
            RemoteIterator<LocatedFileStatus> rmIterator = FS.listLocatedStatus(new Path(srcPath));
            while(rmIterator.hasNext()){
                Path path = rmIterator.next().getPath();
                if(FS.isDirectory(path)){
                    LOGGER.info("------------DirectoryName: " + path.getName());
                }
                else if (FS.isFile(path)){
                    LOGGER.info("-----------FileName: " + path.getName());
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            LOGGER.info("list fileSystem object stream.: ", e);
            e.printStackTrace();
        }
    }

    public void mkdir(String dir){
        try {
            Path path = new Path(dir);
            if(!FS.exists(path)){
                FS.mkdirs(path);
                LOGGER.info("create directory " + dir + " successfully!");
            }else{
                LOGGER.info("directory " + dir + " exists!");
            }
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void readFile(String file){

        try {
            Path path = new Path(file);
            if(FS.exists(path)){
                LOGGER.info("file " + " doesn't exist!");
                return;
            }
            FSDataInputStream  in= FS.open(path);
            String filename = file.substring( file.lastIndexOf('/') + 1, file.length());
            OutputStream out = new BufferedOutputStream(new FileOutputStream(new File(filename)));
            byte[] b = new byte[1024];
            int numBytes = 0;
            while((numBytes = in.read(b)) > 0){
                out.write(b, 0, numBytes);
            }
            in.close();
            out.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public boolean ifExists(String source){
        if(source == null || source.length() == 0){
            return false;
        }
        try {
            return FS.exists(new Path(source));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    /*
     * Recursively copies the source path directories or files to the destination file of HDFS,
     * that is merging multiple files of local path into a file of HDFS.
     * It is the same functionality as the following command:
     *  hadoop fs -putMerge <local fs><hadoop fs>
     */
    public void putMerge(String source, String dest){
        try {
            Path localPath = new Path(source);
            Path hdfsPath = new Path(dest);
            FSDataOutputStream out = FS.create(hdfsPath);
            if(!localFS.exists(localPath)){
                LOGGER.info("file " + " doesn't exist!");
                return;
            }
            FileStatus[] status = localFS.listStatus(localPath);
            for (FileStatus fileStatus: status){
                Path local = fileStatus.getPath();
                FSDataInputStream  in = localFS.open(local);
                byte[] buffer = new byte[1024];
                int numBytes = 0;
                while((numBytes = in.read(buffer)) > 0){
                    out.write(buffer, 0, numBytes);
                }
                LOGGER.info(local + " has been copoed!");
                in.close();
            }
            out.close();
            localFS.close();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /*
     * Recursively copies the source path directories or files to the destination path of HDFS
     * It is the same functionality as the following command:
     *      hadoop fs -copyFromLocal <local fs><hadoop fs>
     * Briefly, copy folder into folder
     * @param source
     * @param dest
     */
    public void copyFromLocal (String source, String dest){

        try {
            Path srcPath = new Path(source);
            Path dstPath = new Path(dest);

            //check if the file already exists
            if(!(FS.exists(dstPath))){
                LOGGER.info("dstPath doesn't exist");
                LOGGER.info("No such destination " + dstPath);
                return;
            }

            //Get the filename out of the file path
            String filename = source.substring(source.lastIndexOf('/') + 1, source.length());

            try {
                //if the file exists in the destination path, it will throw exception.fs.copyFromLocalFile(srcPath, dstPath)
                //remove and overwrite files with the method
                //copyFromLocalFile(boolean delSrc, boolean overwrite, Path src, Path dst)
                FS.copyFromLocalFile(false, true, srcPath, dstPath);
                System.out.println("File " + filename + " copies to " + dest);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public void renameFile(String fromthis, String tothis){
        try {
            Path fromPath = new Path(fromthis);
            Path toPath = new Path(tothis);

            if(!(FS.exists(fromPath))){
                LOGGER.info("No such destination " + fromPath);
                return;
            }

            if(FS.exists(toPath)){
                LOGGER.info("Already exists: " + toPath);
                return;
            }

            try {
                boolean isRenamed = FS.rename(fromPath, toPath); //rename file name indeed.
                if(isRenamed){
                    LOGGER.info("Renamed from " + fromthis + " to " + tothis);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /*
     * Uploads or adds a file to HDFS
     */
    public void addFile(String source, String dest){
        try {
            //Get the filename out of the file path
            String filename = source.substring(source.lastIndexOf('/') + 1, source.length());

            //Create the destination path including the filename.
            if (dest.charAt(dest.length()-1) != '/'){
                dest = dest + "/" + filename;
            }else{
                dest = dest + filename;
            }

            //Check if the file already exists
            Path path = new Path(dest);
            if(FS.exists(path)){
                LOGGER.info("file " + dest + " already exists");
                return ;
            }

            //Create a new file and write data to it
            FSDataOutputStream out = FS.create(path);
            InputStream in = new BufferedInputStream(new FileInputStream(new File(source)));
            byte[] buffer = new byte[1024];
            int numBytes = 0;
            //In this way, read and write data to destination file.
            while((numBytes = in.read(buffer)) > 0){
                out.write(buffer, 0, numBytes);
            }
            in.close();
            out.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /*
     * Deletes the files if it is a directory
     */
    public void deleteFile(String file){
        try {
            Path filePath = new Path(file);
            if(!(FS.exists(filePath))){
                System.out.println("file " + file + "does not exist");
                return;
            }
            //delete(Path f, boolean recursive), the second parameter is to set whether we delete files recursively.
            FS.delete(filePath, true);
            System.out.println("files have been deleted");
            FS.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    /*
     * Gets the information about the file modified time.
     */
    public void getModificationTime(String source) throws IOException{

        Path srcPath = new Path(source);

        //Check if the file already exists
        if(!(FS.exists(srcPath))){
            System.out.println("file " + srcPath + " does not exist");
        }

        //Get the filename out of the file path
        String filename = source.substring(source.lastIndexOf('/') + 1, source.length());
        FileStatus status = FS.getFileStatus(srcPath);
        long modificationTime = status.getModificationTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(modificationTime);
        LOGGER.info( filename + "is modified in : " + simpleDateFormat.format(date));

    }

    /*
     * Gets the file block locations info
     */
    public void getBlockLocations(String source) {
        try {
            Path srcPath = new Path(source);

            //Check if the file already exists
            if(!(FS.exists(srcPath))){
                System.out.println( srcPath + " does not exist");
            }

            //Get the filename out of the file path
            String filename = source.substring(source.lastIndexOf('/') + 1, source.length());

            FileStatus status = FS.getFileStatus(srcPath);

            BlockLocation[]  blocks = FS.getFileBlockLocations(srcPath, 0, status.getLen());

            for (BlockLocation block: blocks){
                String[] hosts = block.getHosts();
                for (int i = 0; i < hosts.length; i++){
                    LOGGER.info("filename is: " + filename + ", block is: " + block + ", host ip is: " + hosts);
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    public void getHostnames() throws IOException{

        DistributedFileSystem hdfs = (DistributedFileSystem)FS;
        DatanodeInfo[] datanodeInfos = hdfs.getDataNodeStats();

        String[] names = new String[datanodeInfos.length];
        for (int i=0; i < datanodeInfos.length; i++){
            names[i] = datanodeInfos[i].getHostName();
            LOGGER.info(names[i]);
        }
    }


    public void close(FileSystem FS) {
        if(FS !=null ){
            try {
                FS.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
