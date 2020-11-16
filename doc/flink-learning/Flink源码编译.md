## 打包 https://www.cnblogs.com/felixzh/p/9685529.html
mvn clean install -Drat.skip=true -DskipTests -Dfast
cd flink-dist
mvn clean install

## 编译和运行例子的问题
错误1: java: 程序包org.junit不存在 (问题依然存在)
File -> Project Struct... -> Libraies -> 点击绿色的加号 -> Java -> 找到 IDEA 安装路径下的 Lib 中的junit-4.12 -> 确定完就行了，点击OK

错误2：Could not find artifact io.confluent:kafka-schema-registry-client:jar:3.3.1 in aliyunmaven (https://maven.aliyun.com/repository/public)
解决：手动下载kafka-schema-registry-client-3.3.1.jar包，下载地址如下：http://packages.confluent.io/maven/io/confluent/kafka-schema-registry-client/3.3.1/kafka-schema-registry-client-3.3.1.jar
mvn install:install-file -DgroupId=io.confluent -DartifactId=kafka-schema-registry-client -Dversion=3.3.1 -Dpackaging=jar  -Dfile=d:\kafka-schema-registry-client-3.3.1.jar

错误3： Error:java: 无效的标记: --add-exports=java.base/sun.ne
解决：  "Intellij" -> "View" -> "Tool Windows" -> "Maven" -> "Profiles" -> uncheck the "java11" -> reimport maven project

切记： idea配置的settings.xml 和 终端运行mvn 的settings.xml 保持一致，要不引起本地仓库运行和依赖不一致