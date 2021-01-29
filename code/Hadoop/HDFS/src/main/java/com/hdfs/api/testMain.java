package com.hdfs.api;

import com.hdfs.api.impl.HdfsUtilImpl;
import org.apache.hadoop.fs.Path;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;


/*You can change the replication factor of a file using command:
hadoop fs –setrep –w 3 /user/hdfs/file.txt
You can also change the replication factor of a directory using command:
hadoop fs -setrep -R 2 /user/hdfs/test
https://www.iteye.com/blog/heipark-1356063
https://www.geek-book.com/src/docs/hadoop3.2.1/hadoop.apache.org/docs/r3.2.1/hadoop-archives/HadoopArchives.html
hadoop  archive -archiveName NAME -p <parent path> <src>*  <har_dest_dir>
hadoop fs -ls har:///user/zoo/foo.har/
*/


/**
 * Created by bear on 2020/7/10
 */
public class testMain {
    public static void main(String[] args) {

        HdfsUtilImpl hdfsUtil = new HdfsUtilImpl();
        try {
            boolean rs = hdfsUtil.isExistDir("hdfs://namenode.safe.lycc.qihoo.net:9000/home/hdp-ads-dw/hive/warehouse/hdp_ads_dw.db/ods/ods_ka_customer_belong/dt=2020-08-07", false);
            System.out.println(rs);

            hdfsUtil.isExistDir("/home/hdp-ads-audit/user/xiongyouguo/hadoopapi",true);
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
