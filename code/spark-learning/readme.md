
## 查看和确认 SPARK系统运行环境
ll  $SPARK_HOME/jars/spark*  查看系统SPARK版本
ll  $SPARK_HOME/jars/scala*  查看系统scala版本

## https://spark.apache.org/docs/2.4.5/running-on-yarn.html

## 编译打包
mvn clean  package -DskipTests


## To launch a Spark application in cluster mode

> ./bin/spark-submit --class path.to.your.Class --master yarn --deploy-mode cluster [options] <app jar> [app options]
* cluster mode For example:
spark-submit --class examples.JavaSparkPi \
--master yarn \
--deploy-mode cluster \
--driver-memory 4g \
--executor-memory 2g \
--executor-cores 4 \
--queue hdp-ads-audit \
/home/hdp-ads-audit/spark-learning-1.0.jar \
10

* client mode For example:
spark-submit --class examples.JavaWordCount \
--master yarn \
--deploy-mode client \
--driver-memory 4g \
--executor-memory 2g \
--executor-cores 4 \
--queue hdp-ads-audit \
/home/hdp-ads-audit/spark-learning-1.0.jar \
/home/hdp-ads-audit/user/xiongyouguo/word.txt