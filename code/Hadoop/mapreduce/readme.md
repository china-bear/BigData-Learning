### 打包
mvn clean  package -DskipTests

### 运行
hadoop jar mapreduce-1.0.jar  com.mr.WordCount  <in> [<in>...] <out>