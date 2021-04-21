package edu.bear.kafka.examples.consumers;

import edu.bear.kafka.examples.common.AppConfigs;
import org.apache.kafka.clients.consumer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Properties;

/**
 * Kafka消费者——同步加异步提交
 * https://github.com/heibaiying/BigData-Notes/tree/master/code/Kafka/kafka-basis
 */
public class HelloConsumerSyncAndAsyncOffsetCommit {

    private static final Logger logger = LoggerFactory.getLogger(HelloConsumerSyncAndAsyncOffsetCommit.class);
    public static final String applicationID = "HelloConsumerSyncAndAsyncOffsetCommit";  //System.out.println(HelloConsumerSyncAndAsyncOffsetCommit.class.getSimpleName());

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
        //consumer.subscribe(Collections.singletonList(AppConfigs.topicName));

        consumer.subscribe(Arrays.asList(AppConfigs.topicName, "page.views"));

        logger.info("Start consuming messages...");

        try {
            while (true) {
                ConsumerRecords<Integer, String> records = consumer.poll(Duration.of(100, ChronoUnit.MILLIS));
                for (ConsumerRecord<Integer, String> record : records)
                    System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());

                /*异步提交并定义回调*/
                consumer.commitAsync();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            try {
                // 因为即将要关闭消费者，所以要用同步提交保证提交成功
                consumer.commitSync();
            } finally {
                consumer.close();
            }
        }
    }
}
