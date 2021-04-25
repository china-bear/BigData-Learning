package edu.bear.kafka.examples.consumers;

import edu.bear.kafka.examples.common.AppConfigs;
import edu.bear.kafka.examples.pojo.StockDataAvro;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

/**
 * Kafka消费者——同步提交
 */
public class AvroConsumerSyncOffsetCommit {

    private static final Logger logger = LoggerFactory.getLogger(AvroConsumerSyncOffsetCommit.class);
    private static final String applicationID = "AvroConsumerSyncOffsetCommit";

    public static void main(String[] args) {
        Properties props = new Properties();
        logger.info("Creating Kafka Consumer...");
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, applicationID);
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, AppConfigs.bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, AppConfigs.groupName);  //指定分组ID
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        //props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, AppConfigs.autoCommitInterval);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);

        KafkaConsumer<String, StockDataAvro> consumer = new KafkaConsumer<>(props);

        /*订阅主题(s)*/
        consumer.subscribe(Collections.singletonList(AppConfigs.topicName));

        //consumer.subscribe(Arrays.asList(AppConfigs.topicName));

        logger.info("Start consuming messages...");
        try {
            while (true) {
                ConsumerRecords<String, StockDataAvro> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, StockDataAvro> record : records) {
                    System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
                    /*同步提交*/
                    consumer.commitSync();
                }
        } } finally {
                consumer.close();
            }
        }
}
