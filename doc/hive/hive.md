使用MSCK命令修复Hive分区
MSCK REPAIR TABLE table_name
分区的目录结构必遵循 /partition_name=partition_value/结构，否则msck无法自动添加分区，只能使用add partition命令。


https://www.simpledw.com/articles/2020/01/13/1578918761163.html  ORC文件谨慎ALTER
https://zhuanlan.zhihu.com/p/141908285   再来聊一聊 Parquet 列式存储格式
