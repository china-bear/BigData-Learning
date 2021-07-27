使用MSCK命令修复Hive分区
MSCK REPAIR TABLE table_name
分区的目录结构必遵循 /partition_name=partition_value/结构，否则msck无法自动添加分区，只能使用add partition命令。

# 查看 HIVE分区数据位置
describe formatted tbl_name partition (dt='20131023');
show table extended like <your table name here> partition(<your partition spec here>);
DESC EXTENDED tablename PARTITION (yr_no='y2019');

https://www.simpledw.com/articles/2020/01/13/1578918761163.html  ORC文件谨慎ALTER
https://zhuanlan.zhihu.com/p/141908285   再来聊一聊 Parquet 列式存储格式

https://zhuanlan.zhihu.com/p/103740807  Hive压缩与存储详解
https://zhuanlan.zhihu.com/p/151064814  hive 压缩和存储
https://cloud.tencent.com/developer/article/1644638  Hive进阶篇

https://zhuanlan.zhihu.com/p/46981953  Hadoop小文件的3类常见情况的处理

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

# HIVE ETL JOB 参数调优 (集群参数缺省值 set 参数名; 查询)

## 通用参数
set mapreduce.job.queuename=root.etl  # default undefined  mapred.fairscheduler.pool 功能一样

set mapred.job.priority=VERY_HIGH  # default undefined, LOW、VERY_LOW、NORMAL、HIGH、VERY_HIGH 五种类型

set mapred.job.name=Hive:[etl][tbname][owner]  # default undefined


## 并行相关参数
### Turn on task parallel execution
set hive.exec.parallel=true; # default false

### mapred.reduce.tasks 参数功能一样，改参数新版本被弃用,  简单粗暴的直接指定reduce数量, 这个值是多少reduce task数量就是多少
set mapreduce.job.reduces = 4000
### map 数量
set mapred.job.max.map.running=4000
### reduce 数量
set mapred.job.max.reduce.running=2000
### Maximum number of threads allowed for parallel tasks
set hive.exec.parallel.thread.number=8 # default 8
### Task总数的 mapreduce.job.reduce.slowstart.completedmaps (默认为0.05) 后，ApplicationMaster便会开始调度执行Reduce Task任务。
set mapreduce.job.reduce.slowstart.completedmaps=0.05

## 内存调整参数
### Set the memory size of Map and JVM Heap
set mapreduce.map.memory.mb=3072  # default 1536
set mapreduce.map.java.opts=-Xms2400m -Xmx2400m -XX:+UseG1GC -XX:MaxMetaspaceSize=256m -XX:+PrintGC -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintGCDateStamps -verbose:gc -server  # default: mapreduce.map.java.opts= -Xmx1230m -XX:ParallelGCThreads=4

### Set Reduce memory size and JVM Heap
set mapreduce.reduce.memory.mb=4096  # default 2048
set mapreduce.reduce.java.opts=-Xms3276m -Xmx3276m -XX:+UseG1GC -XX:MaxMetaspaceSize=256m -XX:+PrintGC -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintGCDateStamps -verbose:gc -server # default: mapreduce.map.java.opts= -Xmx1230m -XX:ParallelGCThreads=4

## 文件压缩参数
### 
set hive.exec.compress.output=true;

###  mapred.output.compress 参数功能一样，改参数新版本被弃用
set mapreduce.output.fileoutputformat.compress=true  # default false
  
### mapred.output.compression.codec 参数功能一样，改参数新版本被弃用
set mapreduce.output.fileoutputformat.compress.codec=org.apache.hadoop.io.compress.SnappyCodec  # default org.apache.hadoop.io.compress.DefaultCodec

### mapred.output.compression.type 参数功能一样，改参数新版本被弃用
set mapreduce.output.fileoutputformat.compress.type=BLOCK  # default BLOCK

## 小文件合并参数
### Input merge(输入合并)
set hive.input.format=org.apache.hadoop.hive.ql.io.CombineHiveInputFormat;  # default: org.apache.hadoop.hive.ql.io.CombineHiveInputFormat

### Merge small files at the end of the Map-only task
set hive.merge.mapfiles=true; # default: true

### Merge small files at the end of Map-reduce, (Note: If the file compression format is inconsistent, it must be set to false)
set hive.merge.mapredfiles=true; # default: true

### When the average size of the output file is less than this value, start an independent map-reduce task to merge the files (default)  当输出文件的平均大小小于该值时，启动一个独立的map-reduce任务进行文件merge（默认）
set hive.merge.smallfiles.avgsize=256000000; # default: 16000000

###  合并文件的大小 Combined file size (default)
set hive.merge.size.per.task=256000000; # default: 256000000

### mapred.min.split.size 参数功能一样，改参数新版本被弃用, 当设置了set hive.input.format=org.apache.hadoop.hive.ql.io.HiveInputFormat 时,会影响map数量
set mapreduce.input.fileinputformat.split.minsize=268435456; # default 512000000
### 文件分割大小 mapred.max.split.size 参数功能一样，改参数新版本被弃用    影响map数量的参数
set mapreduce.input.fileinputformat.split.maxsize=268435456; # default 512000000

### 节点文件分割大小 mapred.min.split.size.per.node 参数功能一样，改参数新版本被弃用  影响map数量的参数
set mapreduce.input.fileinputformat.split.minsize.per.node=268435456  # default 512000000
### 机架文件分割大小 mapred.min.split.size.per.rack 参数功能一样，改参数新版本被弃用  影响map数量的参数
set mapreduce.input.fileinputformat.split.minsize.per.rack=268435456  # default 512000000
### Reduce 文件分割大小  影响reduce数量的参数
set hive.exec.reducers.bytes.per.reducer=268435456;

##数据倾斜

###是否启用倾斜连接优化
set hive.optimize.skewjoin=true;

###Hive Group By查询中是否在Map端先进行聚合
set hive.map.aggr=true;


## 生成环境配置
set hive.input.format=org.apache.hadoop.hive.ql.io.CombineHiveInputFormat;
set hive.hadoop.supports.splittable.combineinputformat=true;
set mapreduce.map.memory.mb=2048;
set mapreduce.map.java.opts=-Xmx1536m -XX:ParallelGCThreads=4;
set mapreduce.reduce.memory.mb=2048;
set mapreduce.reduce.java.opts=-Xmx1536m -XX:ParallelGCThreads=4;
set mapred.min.split.size=268435456;
set mapred.max.split.size=268435456;
set mapred.min.split.size.per.node=268435456;
set mapred.min.split.size.per.rack=268435456;
set hive.exec.reducers.bytes.per.reducer=268435456;
set hive.merge.mapfiles=true;
set hive.merge.mapredfiles=true;
set hive.merge.size.per.task=256000000;
set hive.merge.smallfiles.avgsize=256000000;

# [Deprecated Properties] https://hadoop.apache.org/docs/r2.7.7/hadoop-project-dist/hadoop-common/DeprecatedProperties.html
# [数据倾斜解决方案]  https://juejin.cn/post/6844904165752176648
# 优化Hive SQL参数 https://zhuanlan.zhihu.com/p/102274518
# Map & Reduce数量调整 https://www.jianshu.com/p/007ce9991292