# 新版Flink Java环境开发快速搭建(基于 Flink 1.12.2 版本)

## Maven创建FLINK项目(三种方式选一种即可)
1. 基于 Maven Archetype 构建，直接使用下面的 mvn 语句来进行构建
直接使用下面的 mvn 语句来进行构建，然后根据交互信息的提示，依次输入 groupId , artifactId 以及包名等信息后等待初始化的完成
    mvn archetype:generate \
    -DarchetypeGroupId=org.apache.flink \
    -DarchetypeArtifactId=flink-quickstart-java \
    -DarchetypeVersion=1.10.2 \
    -DgroupId=org.myorg.quickstart \
    -DartifactId=quickstart	\
    -Dversion=0.1 \
    -Dpackage=org.myorg.quickstart \
    -DinteractiveMode=false

2. 使用官方脚本快速构建
为了更方便的初始化项目，官方提供了快速构建脚本，可以直接通过以下命令来进行调用：

curl https://flink.apache.org/q/quickstart.sh | bash -s 1.10.2

该方式其实也是通过执行 maven archetype 命令来进行初始化，相比于第一种方式，该种方式只是直接指定好了 groupId ，artifactId ，version 等信息而已, 把生成的项目文件导入IDEA即可

3. 使用 IDEA 构建
直接在项目创建页面选择 Maven Flink Archetype 进行项目初始化：

如果你的 IDEA 没有上述 Archetype， 可以通过点击右上角的 ADD ARCHETYPE ，来进行添加，依次填入所需信息，这些信息都可以从上述的 archetype:generate 语句中获取。
点击  OK 保存后，该 Archetype 就会一直存在于你的 IDEA 中，之后每次创建项目时，只需要直接选择该 Archetype 即可：

选中 Flink Archetype ，然后点击 NEXT 按钮，之后的所有步骤都和正常的 Maven 工程相同。

## IDEA运行环境设置(二种方式选一种即可)

1. 使用 IDEA 创建项目pom.xml  添加profile 配置后在 id 为 add-dependencies-for-IDEA 的 profile 中，
所有的核心依赖都被标识为 compile，此时你可以无需改动任何代码，只需要在 IDEA 的 Maven 面板中勾选该 profile后再Reload ALL MAVEN PROJECTS，
即可直接在 IDEA 中运行 Flink 项目,或者会导致IDEA中启动项目时会抛出 ClassNotFoundException 异常。
   
        <!-- This profile helps to make things run out of the box in IntelliJ -->
        <!-- Its adds Flink's core classes to the runtime class path. -->
        <!-- Otherwise they are missing in IntelliJ, because the dependency is 'provided' -->
        <profile>
            <id>add-dependencies-for-IDEA</id>

            <activation>
                <property>
                    <name>idea.version</name>
                </property>
            </activation>

            <dependencies>
                <dependency>
                    <groupId>org.apache.flink</groupId>
                    <artifactId>flink-scala_${scala.binary.version}</artifactId>
                    <scope>compile</scope>
                </dependency>
                <dependency>
                    <groupId>org.apache.flink</groupId>
                    <artifactId>flink-streaming-scala_${scala.binary.version}</artifactId>
                    <scope>compile</scope>
                </dependency>
                <dependency>
                    <groupId>org.scala-lang</groupId>
                    <artifactId>scala-library</artifactId>
                    <scope>compile</scope>
                </dependency>
            </dependencies>
        </profile>

2. 在IntelliJ IDEA中做相关设置 Include dependencies with “Provided” scope
RUN-->RUN/DEBUG  --> "Run/Debug Configurations"


## 外部数据源环境

###安装Docker
yum install docker
yum install docker-compose

docker --version
docker-compose -version

sudo systemctl start docker

###安装Kafka
$ docker search kafka
NAME                                    DESCRIPTION                                     STARS               OFFICIAL            AUTOMATED
wurstmeister/kafka                      Multi-Broker Apache Kafka Image                 1259                                    [OK]
spotify/kafka                           A simple docker image with both Kafka and Zo…   403                                     [OK]
sheepkiller/kafka-manager               kafka-manager                                   196                                     [OK]
bitnami/kafka                           Apache Kafka is a distributed streaming plat…   181                                     [OK]
ches/kafka                              Apache Kafka. Tagged versions. JMX. Cluster-…   117                                     [OK]

$ docker pull wurstmeister/kafka:2.12-2.5.0

安装完成之后，我们可以查看一下已经安装的镜像
$ docker images

###zookeeper
$ docker search zookeeper
NAME                               DESCRIPTION                                     STARS               OFFICIAL            AUTOMATED
zookeeper                          Apache ZooKeeper is an open-source server wh…   956                 [OK]                
jplock/zookeeper                   Builds a docker image for Zookeeper version …   166                                     [OK]
wurstmeister/zookeeper                                                             132                                     [OK]
mesoscloud/zookeeper               ZooKeeper                                       73                                      [OK]
bitnami/zookeeper                  ZooKeeper is a centralized service for distr…   45                                      [OK]
mbabineau/zookeeper-exhibitor                                                      24                                      [OK]

$ docker pull zookeeper:3.6.1

安装完成之后，查看一下已经安装的镜像
$ docker images

查看所有容器（包括停止的）
$ docker ps -a

移除已创建的容器
$ sudo docker rm <container_id>
or
$ sudo docker rm --force <container_id>

在容器的启动 zookeeper
$ docker run -d --name zookeeper  -p 2181:2181 -t zookeeper

启动后用docker ps查看前运行的容器
$ docker ps

进入zookeeper命令行
$ docker exec -it zookeeper /bin/bash

连接到 ZooKeeper 服务
zkCli.sh -server 127.0.0.1:2181 

查看当前 ZooKeeper 中所包含的内容
ls / 

在容器的启动 kafka, KAFKA_ADVERTISED_HOST_NAME=127.0.0.1 只能在本机获取TOPIC 信息
$ docker run -d --name kafka --publish 9092:9092 --link zookeeper --env KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181 --env KAFKA_ADVERTISED_HOST_NAME=127.0.0.1 --env KAFKA_ADVERTISED_PORT=9092 wurstmeister/kafka

启动后用kafka ps查看启动状态
$ docker ps

进入Kafka命令行
$ docker exec -it kafka /bin/bash

尝试创建一个Topic
/opt/kafka/bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --topic page.views  --partitions 3

删除一个Topic
/opt/kafka/bin/kafka-topics.sh --delete  --bootstrap-server localhost:9092  --topic hello-topic

List all kafka topics
/opt/kafka/bin/kafka-topics.sh --list --bootstrap-server localhost:9092

向test Topic 发数据：
/opt/kafka/bin/kafka-console-producer.sh --topic=page.views --broker-list localhost:9092
>hello
>welcome you

启动另一个Kafka的Shell来消费消息，如下：
$ docker exec -it kafka /bin/bash
/opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 -from-beginning --topic page.views --max-messages 10

至此，我们Kafka的环境部署测试完成。

###安装MySQL
先搜索，再安装
$ docker search mysql
NAME                              DESCRIPTION                                     STARS               OFFICIAL            AUTOMATED
mysql                             MySQL is a widely used, open-source relation…   10176               [OK]                
mariadb                           MariaDB is a community-developed fork of MyS…   3749                [OK]                
mysql/mysql-server                Optimized MySQL Server Docker images. Create…   743                                     [OK]


$ docker pull mysql:5.7

安装完成之后，查看一下已经安装的镜像
$ docker images

配置mysql
mkdir -p $PWD/conf $PWD/logs $PWD/data
docker run -p 3306:3306 --name flink_mysql -v $PWD/conf:/etc/mysql/conf.d -v $PWD/logs:/logs -v $PWD/data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=123456 -d mysql:5.7

简单的解释一下命令参数含义如下：
-p 3306:3306：将容器的 3306 端口映射到主机的 3306 端口。
-v $PWD/conf:/etc/mysql/conf.d -v $PWD/logs:/logs -v $PWD/data:/var/lib/mysql: 本机目录和容器目录进行挂载，比如：PWD/logs:/logs：将主机当前目录下的 logs 目录挂载到容器的 /logs。
-e MYSQL_ROOT_PASSWORD=123456：初始化 root 用户的密码。
-d: 后台运行容器，并返回容器ID

查看启动情况：
$ docker ps -a |grep flink_mysql

进入mysql命令行,
docker exec -it flink_mysql bash
mysql -h localhost -u root -p

授权Flink 自带了CDC connector, 可以直接捕获表的changelog
GRANT SELECT, RELOAD, SHOW DATABASES, REPLICATION SLAVE, REPLICATION CLIENT ON *.* TO 'flink' IDENTIFIED BY 'flink360';
操作都成功后MySQL环境已经可用

###安装Flink
FLINK 主要部署方式
1. 本地集群
2. 独立集群

以下部署都有 Per Session 和 Per Job二种方式
3. cluster on YARN
4. cluster on Mesos
5. cluster on Docker
6. cluster on Kubernetes 

开发测试环境目前我们只需要配置方式1： 本地集群

Download and Start Flink
$ cd ~/Downloads        # Go to download directory
$ wget https://mirrors.bfsu.edu.cn/apache/flink/flink-1.12.2/flink-1.12.2-bin-scala_2.11.tgz
$ tar xzf flink-*.tgz   # Unpack the downloaded archive
$ ln -s flink-1.12.2/ flink
$ cd flink/lib

Download Kafka connector 
wget https://repo1.maven.org/maven2/org/apache/flink/flink-sql-connector-kafka_2.11/1.12.2/flink-sql-connector-kafka_2.11-1.12.2.jar

Download mysql-cdc connector jar
wget  https://repo1.maven.org/maven2/com/alibaba/ververica/flink-sql-connector-mysql-cdc/1.2.0/flink-sql-connector-mysql-cdc-1.2.0.jar

Download JDBC connector jar
wget  https://repo1.maven.org/maven2/org/apache/flink/flink-connector-jdbc_2.11/1.12.2/flink-connector-jdbc_2.11-1.12.2.jar
wget  https://repo.maven.apache.org/maven2/mysql/mysql-connector-java/5.1.48/mysql-connector-java-5.1.48.jar

cd ..
Start a Local Flink Cluster
$ ./bin/start-cluster.sh  # Start Flink

checking the log files in the logs directory:
$ tail log/flink-*-standalonesession-*.log

Check the Dispatcher’s web frontend
http://localhost:8081

SQL Client 运行 SQL作业
$ ./bin/sql-client.sh embedded  # Start Flink SQL Client


### 安装InfluxDB 2.x
1. 安装
$ docker pull influxdb
2. 查看安装的镜像
$ docker images
3. 运行
$ docker run -d --name my_influxdb -p 8083:8083 -p 8086:8086 -v /data/influx/influxdb:/var/lib/influxdb -e INFLUXDB_ADMIN_ENABLED=true influxdb

4. 登陆
$ docker exec -it my_influxdb bash

### 查看 验证influxd 版本
influxd version

5. 设置influxdb信息( UI 或者 CLI)   admin admin360 dw  2400
influx setup   
### 创建admin用户
influx user create -n <username> -p <password> -o <org-name>
influx user create -n app -p app3601234 -o dw
### 产看创建的用户
influx user list

### kill并重启，并指定配置文件
docker kill influxdb
docker rm influxdb
https://docs.influxdata.com/influxdb/v2.0/get-started/?t=Docker

### 第二种方法： Docker Compose 安装
docker-compose -f docker-compose.yml up -d
docker-compose ps
docker-compose -f docker-compose.yml down
### 创建Topic
docker exec -it broker /bin/bash
kafka-topics --create --bootstrap-server localhost:9092 --topic hello-topic  --partitions 3

### 进入Confluent控制中心 
http://localhost:9021 


## 安装REDIS

### 查找镜像
docker search redis
### 下载镜像
docker pull redis
### 创建配置和数据文件夹(docker redis 挂载)
cd  /da2/home/redis
mkdir -p conf
mkdir -p data
### 创建Redis配置文件
touch conf/redis.conf
vim conf/redis.conf 
添加下面内容：
bind 127.0.0.1 
protected-mode no
appendonly yes 
requirepass 123456 

    将bind 127.0.0.1注释掉，保证可以从远程访问到该Redis，不单单是从本地
    appendonly：开启数据持久化到磁盘，由于开启了磁盘映射，数据最终将落到/da2/home/redis/data目录下
    requirepass：设置访问密码为123456

### 创建redis容器并启动
docker  run \
--name  myredis \
-p  6379:6379 \
-v  /da2/home/redis/data:/data \
-v  /da2/home/redis/conf/redis.conf:/etc/redis/redis.conf \
-d  redis redis-server /etc/redis/redis.conf 


    docker run表示运行的意思
    --name myredis表示运行容器的名字叫myredis
    -p 6379:6379表示将服务器的6379(冒号前的6379)端口映射到docker的6379（冒号后的6379）端口，这样就可以通过服务器的端口访问到docker容器的端口了
    -d 表示以后台服务的形式运行redis
    -v /da2/home/redis/data:/data表示将服务器上的/da2/home/redis/data映射为docker容器上的/data ，这样/data中产生的数据就可以持久化到本地的目录下了
    -v /da2/home/redis/conf/redis.conf:/etc/redis/redis.conf表示将本地/da2/home/redis/conf/redis.conf映射为docker容器上的/etc/redis/redis.conf，这样再配合指令末尾的redis redis-server /etc/redis/redis.conf实现让docker容器运行时使用本地配置的Redis配置文件的功能了。


### 查看启动的redis容器启动是否OK
docker ps -a | grep myredis
- 查看myredis 容器日志
docker logs myredis
- 查看 myredis 容器的详细信息
docker inspect myredis
- 查看 myredis 容器的端口映射
docker port myredis

### 容器内部连接进行测试
- docker exec -it myredis redis-cli   
- auth 123456
- set user Mike
- get user

###或shell 登录容器内操作 (二种方式选一种即可)
- docker exec -it myredis bash
- redis-cli  


    
    https://juejin.cn/post/6844903876123066376  zookeeper常用命令
    
    https://www.cnblogs.com/toudoushaofeichang/p/11606255.html
    https://juejin.im/post/6844903999703891976
    https://segmentfault.com/a/1190000022205667
    https://ci.apache.org/projects/flink/flink-docs-release-1.11/zh/ops/deployment/local.html
    https://www.alibabacloud.com/blog/principles-and-practices-of-flink-on-yarn-and-kubernetes-flink-advanced-tutorials_596625
    http://zhongmingmao.me/2019/03/26/kafka-docker-schema-registry/
    

