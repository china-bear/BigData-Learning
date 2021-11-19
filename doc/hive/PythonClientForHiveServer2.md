# Python 连接 Hive 的方式

> HiveServer2是HiveServer的重写版本, HiveServer2是基于Thrift RPC实现的, 它被设计基于 JDBC或ODBC API客户端提供更好的支持.
> $HIVE_HOME/bin/hive --service hiveserver2 &

>Spark Thrift Server是Spark社区基于HiveServer2实现的一个Thrift服务, 旨在无缝兼容HiveServer2, Spark Thrift Server的接口和协议完全兼容HiveServer2, 可以和Hive Metastore进行交互获取hive的元数据。
> $SPARK_HOME/sbin/start-thriftserver.sh --masterspark://master:7077 --executor-memory 1g

> Presto
> Presto is a distributed system that runs on a cluster of machines. It enables analytics on large amounts of data.
> With Presto, access and query data in place on many data different data sources using ANSI SQL (see image below).


1. PyHive + STS(Spark Thrift Server)

-- [代码库](https://github.com/prestodb/presto-python-client)

2. PyHive + STS(Spark Thrift Server)

-- [代码库](https://github.com/dropbox/PyHive)
-- [代码库](https://github.com/prestodb/presto)

3. PyHive + HiveServer2

-- [代码库](https://github.com/dropbox/PyHive)

4. impyla

-- [代码库](https://github.com/cloudera/impyla)

5. pyarrow+thrift

-- [代码库](https://github.com/apache/arrow/tree/master/python/pyarrow)

6. pyhs2 

-- [代码库](https://github.com/BradRuderman/pyhs2)  2016年后已经不维护, 不推荐使用



-- [python读取hive方案分析](https://codeantenna.com/a/tiOfO7TaOX)