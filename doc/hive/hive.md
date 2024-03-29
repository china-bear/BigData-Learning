使用MSCK命令修复Hive分区
MSCK REPAIR TABLE table_name
分区的目录结构必遵循 /partition_name=partition_value/结构，否则msck无法自动添加分区，只能使用add partition命令

# how to run hive in debug mode
hive -hiveconf hive.root.logger=debug,console

#查看hive的所有默认配置
set -v;

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
https://www.jianshu.com/p/ac76c9bed557  Spark 处理小文件
https://developer.aliyun.com/article/1093610  小文件处理方案汇总

https://zhuanlan.zhihu.com/p/511193855  spark数据倾斜解决方案
https://developer.aliyun.com/article/741111  Spark 数据倾斜及其解决方案
http://www.jasongj.com/spark/skew/  解决Spark数据倾斜（Data Skew）的N种姿势

https://zhuanlan.zhihu.com/p/354044983  大厂都在用的Hive优化
https://zhuanlan.zhihu.com/p/90953401 MapReduce 作业内存溢出-分类、原理、如何解决
https://cloud.tencent.com/developer/article/2290331  join-on-and 与 join-on-where的区别

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

### mapreduce.job.maps
set mapreduce.job.maps = 2000
### mapred.reduce.tasks 参数功能一样，该参数新版本被弃用,  简单粗暴的直接指定reduce数量, 这个值是多少reduce task数量就是多少
set mapreduce.job.reduces = 2000
### Job的task map 并发数  The maximum number of simultaneous map tasks per job. There is no limit if this value is 0 or negative
set mapreduce.job.running.map.limit=4000;
set mapred.job.max.map.running=4000 ???
### Job的task reduce 并发数 The maximum number of simultaneous reduce tasks per job. There is no limit if this value is 0 or negative
set mapreduce.job.running.reduce.limit=2000;
set mapred.job.max.reduce.running=2000 ???
### Maximum number of threads allowed for parallel tasks
set hive.exec.parallel.thread.number=8 # default 8
### Task总数的 mapreduce.job.reduce.slowstart.completedmaps (默认为0.05) 后，ApplicationMaster便会开始调度执行Reduce Task任务。
set mapreduce.job.reduce.slowstart.completedmaps=0.05

## 内存调整参数
### 
set mapred.child.java.opts=-Xmx3072m;

### Set the memory size of Map and JVM Heap
set mapreduce.map.memory.mb=8192  # default 1536 The amount of memory to request from the scheduler for each map task
set mapreduce.map.java.opts=-Xms6144m -Xmx6144m -XX:+UseG1GC -XX:MaxMetaspaceSize=256m -XX:+PrintGC -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintGCDateStamps -verbose:gc -server 
# Map 子进程Java参数, 如果设置了该参数, 将会覆盖mapred.child.java.opts参数, If no -Xmx is specified, it is calculated as mapreduce.{map|reduce}.memory.mb * mapreduce.heap.memory-mb.ratio (default 0.8), default: mapreduce.map.java.opts= -Xmx1230m -XX:ParallelGCThreads=4
mapred.child.java.opts
### Set Reduce memory size and JVM Heap
set mapreduce.reduce.memory.mb=4096  # default 2048 The amount of memory to request from the scheduler for each reduce task
set mapreduce.reduce.java.opts=-Xms3072m -Xmx3072m -XX:+UseG1GC -XX:MaxMetaspaceSize=256m -XX:+PrintGC -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintGCDateStamps -verbose:gc -server 
# Map 子进程Java参数, 如果设置了该参数, 将会覆盖mapred.child.java.opts参数, If no -Xmx is specified, it is calculated as mapreduce.{map|reduce}.memory.mb * mapreduce.heap.memory-mb.ratio (default 0.8),  default: mapreduce.map.java.opts= -Xmx1230m -XX:ParallelGCThreads=4


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
> skewjoin原理
> 1.对于skewjoin.key，在执行job时，将它们存入临时的HDFS目录。其它数据正常执行
> 2.对倾斜数据开启map join操作，对非倾斜值采取普通join操作
> 3.将倾斜数据集和非倾斜数据及进行合并操作

###Hive Group By查询中是否在Map端先进行聚合
set hive.map.aggr=true;

##容错相关参数
### 每个Map Task最大重试次数，一旦重试参数超过该值，则认为Map Task运行失败
set mapreduce.map.maxattempts=4  # default 4
### 每个Reduce Task最大重试次数，一旦重试参数超过该值，则认为Map Task运行失败
set mapreduce.reduce.maxattempts = 4  # default 4
### 当失败的Map Task失败比例超过该值为，整个作业则失败,比如5，表示如果有低于5%的Map Task失败（如果一个Map Task重试次数超过mapreduce.map.maxattempts，则认为这个Map Task失败，其对应的输入数据将不会产生任何结果），整个作业扔认为成功
set mapreduce.map.failures.maxpercent = 0 # default: 0
### 当失败的Reduce Task失败比例超过该值为，整个作业则失败，默认值为
set mapreduce.reduce.failures.maxpercent=0  # default 0
###Task超时时间，经常需要设置的一个参数，该参数表达的意思为：如果一个task在一定时间内没有任何进入，即不会读取新的数据，也没有输出数据，则认为该task处于block状态，可能是卡住了，也许永远会卡主，为了防止因为用户程序永远block住不退出，则强制设置了一个该超时时间（单位毫秒）,
### 如果你的程序对每条输入数据的处理时间过长（比如会访问数据库，通过网络拉取数据等），建议将该参数调大，该参数过小常出现的错误提示是“AttemptID:attempt_14267829456721_123456_m_000224_0 Timed out after 300 secsContainer killed by the ApplicationMaster.”
set mapreduce.task.timeout =0  # default 300000

### 允许多列去重，否则报错
set hive.groupby.skewindata = false;
### 关闭 自动转换为mapjoin
set hive.auto.convert.join = false;

### 是否为连接表中的倾斜键创建单独的执行计划。它基于存储在元数据中的倾斜键。在编译时，Hive为倾斜键和其他键值生成各自的查询计划
set hive.optimize.skewjoin=false; (关闭)

## 使用向量化查询执行
要使用向量化查询执行，必须以ORC格式存储数据，并设置以下变量(矢量查询(Vectorized query) 每次处理数据时会将1024行数据组成一个batch进行处理，而不是一行一行进行处理，这样能够显著提高执行速度)
set hive.vectorized.execution.enabled = true;
set hive.vectorized.execution.reduce.enabled = true;
实际BA迁移总发现下面错误： https://www.coder.work/article/733607
INFO [main] org.apache.hadoop.hive.ql.exec.mr.ExecMapper: Initializing dummy operator
ERROR [main] org.apache.hadoop.hive.ql.exec.mr.ExecMapper: java.lang.NullPointerException


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

# ORDER BY, SORT BY, DISTRIBUTE BY, CLUSTER BY的区别 https://juejin.cn/post/7084016673207877645
# 聊聊hive随机采样 https://cloud.tencent.com/developer/article/1346665
# https://www.slideshare.net/joudkhattab/spark-sql-77435155 Spark SQL
# Enhancements on Spark SQL optimizer  https://www.slideshare.net/SparkSummit/enhancements-on-spark-sql-optimizer-by-min-qiu