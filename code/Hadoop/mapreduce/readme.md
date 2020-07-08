mvn clean  package -DskipTests

hadoop jar mapreduce-1.0.jar  com.chinabear.mr.WordCount  in  out