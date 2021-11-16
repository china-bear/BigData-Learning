# Python 连接 Hive 的方式

> HiveServer2是HiveServer的重写版本, HiveServer2是基于Thrift RPC实现的, 它被设计基于 JDBC或ODBC API客户端提供更好的支持.
> 客户端 连接Hive 都需要服务端启动HiveServer2 服务


1. PyHive

-- [代码库](https://github.com/dropbox/PyHive)

2. impyla

-- [代码库](https://github.com/cloudera/impyla)

3. pyarrow+thrift

-- [代码库](https://github.com/apache/arrow/tree/master/python/pyarrow)

4. pyhs2 

-- [代码库](https://github.com/BradRuderman/pyhs2)  2016年后已经不维护, 不推荐使用



-- [python读取hive方案分析](https://codeantenna.com/a/tiOfO7TaOX)