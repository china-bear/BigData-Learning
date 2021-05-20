### 打包
mvn clean  package -DskipTests

### com.mr.common
wordcount 多种文件格式例子

### com.mr.mos
wordcount 多路输出例子

## join https://github.com/Nana0606/hadoop_example
此文件夹下主要包括6个java文件和2个txt文件，CommonReduce.java、FirstComparator.java、JoinMain.java、KeyPartition.java、PreMapper.java、TextPair.java、action.txt和alipay.txt。主要功能是：

* action是商品和交易的匹配
* alipay是商品和支付的匹配，求出交易和支付的相应记录（这个目前存在bug）

## kmeans https://github.com/Nana0606/hadoop_example

主要包括4个java文件和1个txt文件，Center.java、IntSumReducer.java、Run.java、TokenizerMapper.java和note.txt，其中note.txt含有代码思想和简单分析（very important）


## TopN  https://github.com/andreaiacono/MapReduce/blob/master/src/main/java/samples

求出TopN 


## pagerank  https://github.com/Nana0606/hadoop_example

* pagerank
  主要包括1个java文件和1个txt文件，PageRank.java和pagerank.txt
* pagerank2
  主要包括PageRank2.java，这个pagerank的代码是不适用mapper和reducer的代码
  
## hbase_hdfs
  
  主要包括Hbase.simple、Hbase.wordcount文件夹
  
  * Hbase.simple
    主要包括4个java文件，HbaseBean.java、WordCountHbaseMapRed01.java、WordCountHbaseMapRed02.java和WordCountHbaseMapRed03.java。
    * HbaseBean.java主要功能包括数据表的新建、插入、查询和删除。
    * WordCountHbaseMapRed01.java是将HDFS数据写入Hbase
    * WordCountHbaseMapRed02.java是将Hbase数据写入HDFS
    * WordCountHbaseMapRed03.java是将HBase的数据写入Hbase。
  * Hbase.wordcount
    主要包括3个java文件，WordCountHbaseMapper.java、WordCountHbaseReducer.java和WordCountHbaseTest.java，主要功能是：
    * 根据给定文件内容，统计单词及其出现次数
    * 将结果写入Hbase。


### 运行
hadoop jar mapreduce-1.0.jar  com.mr.WordCount  <in> [<in>...] <out>

