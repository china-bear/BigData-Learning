package edu.bear.kafka.examples.consumers;

import edu.bear.kafka.examples.common.AppConfigs;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Kafka消费者——异步提交
 * https://github.com/heibaiying/BigData-Notes/tree/master/code/Kafka/kafka-basis
 */
public class HelloConsumerAsyncWithOffsetCommit {

    private static final Logger logger = LoggerFactory.getLogger(HelloConsumerAsyncWithOffsetCommit.class);
    public static final String applicationID = "HelloConsumerAsyncWithOffsetCommit";

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
        consumer.subscribe(Collections.singletonList(AppConfigs.topicName));

        //consumer.subscribe(Arrays.asList(AppConfigs.topicName));

        logger.info("Start consuming messages...");

        Map<TopicPartition, OffsetAndMetadata> offsets = new HashMap<>();

        try {
            while (true) {
                ConsumerRecords<Integer, String> records = consumer.poll(Duration.of(100, ChronoUnit.MILLIS));
                for (ConsumerRecord<Integer, String> record : records) {
                    System.out.println(record);
                    /*记录每个主题的每个分区的偏移量*/
                    TopicPartition topicPartition = new TopicPartition(record.topic(), record.partition());
                    OffsetAndMetadata offsetAndMetadata = new OffsetAndMetadata(record.offset()+1, "no metaData");
                    /*TopicPartition重写过hashCode和equals方法，所以能够保证同一主题和分区的实例不会被重复添加*/
                    offsets.put(topicPartition, offsetAndMetadata);
                }
                /*提交特定偏移量*/
                consumer.commitAsync(offsets, null);
            }
        } finally {
            consumer.close();
        }
    }
}
