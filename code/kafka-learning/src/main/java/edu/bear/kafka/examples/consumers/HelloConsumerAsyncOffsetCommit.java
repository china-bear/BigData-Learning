package edu.bear.kafka.examples.consumers;

import edu.bear.kafka.examples.common.AppConfigs;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

/**
 * Kafka消费者——异步提交
 * https://github.com/heibaiying/BigData-Notes/tree/master/code/Kafka/kafka-basis
 */
public class HelloConsumerAsyncOffsetCommit {

    private static final Logger logger = LoggerFactory.getLogger(HelloConsumerAsyncOffsetCommit.class);
    private static final String applicationID = "HelloConsumerAsyncOffsetCommit";

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

        try {
            while (true) {
                ConsumerRecords<Integer, String> records = consumer.poll(Duration.of(100, ChronoUnit.MILLIS));
                for (ConsumerRecord<Integer, String> record : records)
                    System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());

                /*异步提交并定义回调*/
                consumer.commitAsync(new OffsetCommitCallback() {
                    @Override
                    public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
                        if (exception != null) {
                            logger.error("错误处理");
                            offsets.forEach((x, y) -> System.out.printf("topic = %s,partition = %d, offset = %s \n",
                                    x.topic(), x.partition(), y.offset()));
                        }
                    }
                });
            }
        } finally {
            consumer.close();
        }
    }
}
