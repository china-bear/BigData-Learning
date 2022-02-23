https://stackoverflow.com/questions/38376307/create-spark-dataframe-from-sql-query
https://segmentfault.com/a/1190000038207701
https://spark.apache.org/docs/latest/sql-data-sources-jdbc.html


## python读取hive方案

1. mysql-PyHive-python
. Officially supported by Oracle
. Pure python
. A little slow
. Not compatible with MySQLdb

2. pymysql
. Pure python
. Faster than mysql-connector
. Almost completely compatible with MySQLdb, after calling pymysql.install_as_MySQLdb()

3. cymysql
. fork of pymysql with optional C speedups

4. mysqlclient
. Django's recommended library.
. Friendly fork of the original MySQLdb, hopes to merge back some day
. The fastest implementation, as it is C based.
. The most compatible with MySQLdb, as it is a fork
. Debian and Ubuntu use it to provide both python-mysqldb andpython3-mysqldb packages.

### 方案一: PyHive

### 方案二: impyla

### 方案三: presto

### 方案四: pyhs2