package com.chinabear.hdfs;

import com.chinabear.hdfs.impl.HdfsUtilImpl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;


/*You can change the replication factor of a file using command:

hadoop fs –setrep –w 3 /user/hdfs/file.txt
You can also change the replication factor of a directory using command:
hadoop fs -setrep -R 2 /user/hdfs/test
https://www.iteye.com/blog/heipark-1356063
hadoop  archive -archiveName NAME -p <parent path> <src>* <dest>
hadoop fs -ls har:///user/zoo/foo.har/
*/




//https://github.com/Sunshine888999/JavaCallPythonDemo/blob/master/src/main/java/cn/diana/algorithm/JavaCallPythonDemo.java
/**
 * Created by bear on 2020/7/10
 */
public class testMain {
    public static void main(String[] args) {



        HdfsUtilImpl hdfsUtil = new HdfsUtilImpl();

        try {
            hdfsUtil.mkdir("/home/hdp-ads-audit/user/xiongyouguo/hadoopapi");
            hdfsUtil.reName("/home/hdp-ads-audit/user/xiongyouguo/hadoopapi","/home/hdp-ads-audit/user/xiongyouguo/hadoopapinew");

            hdfsUtil.setReplication("/home/hdp-ads-audit/user/xiongyouguo/word/a", (short) 2);
            /*目录不生效*/
            hdfsUtil.setReplication("/home/hdp-ads-audit/user/xiongyouguo/userview/*", (short) 1);
            /*目录不生效*/
            hdfsUtil.setReplication("/home/hdp-ads-audit/user/xiongyouguo/test2/", (short) 1);

            String[] env = {"PATH=/bin:/usr/bin/:/usr/bin/hadoop/software/hadoop/bin/"};
            String cmd = "hadoop fs  -ls /home/hdp-ads-audit/user/xiongyouguo/word/";

            Process proc = Runtime.getRuntime().exec(cmd, env);

            String line = null;

            BufferedReader br1 = new BufferedReader(new InputStreamReader(proc.getInputStream(), "UTF-8"));
            while ((line = br1.readLine()) != null) {
                System.out.println(line);
            }

            BufferedReader br2 = new BufferedReader(new InputStreamReader(proc.getErrorStream(), "UTF-8"));
            while ((line = br2.readLine()) != null) {
                System.out.println(line);
            }

            br1.close();
            br2.close();

            int exitVal = proc.waitFor();

            System.out.println(exitVal);
            System.out.println(proc.exitValue());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
