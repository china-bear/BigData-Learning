
# docker 部署方式 参考   Flink Java环境开发搭建.md





# Start Zookeeper
$ bin/zookeeper-server-start etc/kafka/zookeeper.properties

# Start Kafka
$ bin/kafka-server-start etc/kafka/server.properties

# Start Schema Registry
$ bin/schema-registry-start etc/schema-registry/schema-registry.properties

# Create Topic
bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1  --partitions 1 --topic test_clicks

# List all kafka topics
bin/kafka-topics.sh --list --zookeeper localhost:2181


# Producer to produce
java -cp target/bear-ClickstreamGenerator-1.0-SNAPSHOT.jar com.shapira.examples.producer.avroclicks.AvroClicksProducer 100 http://localhost:8081

# Consumer to consume
bin/kafka-console-consumer.sh --bootstrap-server  test-kf1:9092,test-kf2:9092,test-kf3:9092 --topic test_clicks --from-beginning --max-messages 10


https://docs.confluent.io/platform/current/installation/installing_cp/zip-tar.html#prod-kafka-cli-install