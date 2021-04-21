package edu.bear.kafka.examples.consumers;

import edu.bear.kafka.examples.common.AppConfigs;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Kafka消费者——指定分区消费
 * https://github.com/heibaiying/BigData-Notes/tree/master/code/Kafka/kafka-basis
 */
public class HelloConsumerWithPartitions {

    private static final Logger logger = LoggerFactory.getLogger(HelloConsumerWithPartitions.class);
    public static final String applicationID = "HelloConsumerWithPartitions";

    public static void main(String[] args) {
        Properties props = new Properties();
        logger.info("Creating Kafka Consumer...");
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, applicationID);
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, AppConfigs.bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, AppConfigs.groupName);  //指定分组ID
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        //props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, AppConfigs.autoCommitInterval);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.IntegerDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<Integer, String> consumer = new KafkaConsumer<>(props);

        /*订阅主题(s)*/
        List<TopicPartition> partitions = new ArrayList<>();
        List<PartitionInfo> partitionInfos = consumer.partitionsFor(AppConfigs.topicName);

        logger.info("Start consuming messages...");


        /*可以指定读取哪些分区 如这里假设只读取主题的0分区*/
        for (PartitionInfo partition : partitionInfos) {
            if (partition.partition()==0){
                partitions.add(new TopicPartition(partition.topic(), partition.partition()));
            }
        }

        // 为消费者指定分区
        consumer.assign(partitions);

        try {
            while (true) {
                ConsumerRecords<Integer, String> records = consumer.poll(Duration.of(100, ChronoUnit.MILLIS));
                for (ConsumerRecord<Integer, String> record : records)
                    System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
                /*同步提交*/
                consumer.commitSync();
            }
        } finally {
            consumer.close();
        }
    }
}
