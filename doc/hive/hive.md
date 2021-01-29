使用MSCK命令修复Hive分区
MSCK REPAIR TABLE table_name
分区的目录结构必遵循 /partition_name=partition_value/结构，否则msck无法自动添加分区，只能使用add partition命令。


https://www.simpledw.com/articles/2020/01/13/1578918761163.html  ORC文件谨慎ALTER
https://zhuanlan.zhihu.com/p/141908285   再来聊一聊 Parquet 列式存储格式

https://zhuanlan.zhihu.com/p/103740807  Hive压缩与存储详解
https://cloud.tencent.com/developer/article/1644638  Hive进阶篇


# hive 分区表 分区归档    **hive 目前只有内表支持分区归档**
set hive.archive.enabled=true;
set hive.archive.har.parentdir.settable=true;
set har.partfile.size=1099511627776;
使用 archive 命令将分区归档 减少文件数量：
alter table dim_mv_ad_location archive PARTITION(dt='2020-12-05');

可以使用 unarchive 命令将分区恢复为原始文件：
ALTER TABLE srcpart UNARCHIVE PARTITION(ds='2008-04-08',)


http://hadoop.apache.org/docs/r1.2.1/streaming.html 



Use the following command to convert an external table to an internal table:

    use <db name>;

    ALTER TABLE <tablename> SET TBLPROPERTIES('EXTERNAL'='FALSE');

Use the following command to convert an internal table to an external table:

    use <db name>;

    ALTER TABLE <tablename> SET TBLPROPERTIES('EXTERNAL'='TRUE');