package edu.bear.kafka.examples.consumers;

import edu.bear.kafka.examples.common.AppConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Kafka消费者—— 人工控制提交
 */

public class HelloConsumerManualOffsetControl {

    private static final Logger logger = LoggerFactory.getLogger(HelloConsumerManualOffsetControl.class);
    private static final String applicationID = "HelloConsumerManualOffsetControl";

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

        final int minBatchSize = 15;
        List<ConsumerRecord<Integer, String>> buffer = new ArrayList<>();

        logger.info("Start consuming messages...");

        try {
            while (true) {
                ConsumerRecords<Integer, String> records = consumer.poll(Duration.of(100, ChronoUnit.MILLIS));
                for (ConsumerRecord<Integer, String> record : records) {
                    System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
                    buffer.add(record);
                }
                if (buffer.size() >= minBatchSize) {
                    logger.info("buffer fill up,  Start consuming buffer messages");
                    //insertIntoDb(buffer);
                    consumer.commitSync();
                    buffer.clear();
                }
        } }finally {
            consumer.close();
        }
    }
}
