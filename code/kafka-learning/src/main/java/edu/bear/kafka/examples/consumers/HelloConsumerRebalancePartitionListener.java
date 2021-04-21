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
 * Kafka消费者——动态检测分区变化
 * https://github.com/heibaiying/BigData-Notes/tree/master/code/Kafka/kafka-basis
 */
public class HelloConsumerRebalancePartitionListener {

    private static final Logger logger = LoggerFactory.getLogger(HelloConsumerRebalancePartitionListener.class);
    public static final String applicationID = "HelloConsumerRebalancePartitionListener";

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

        Map<TopicPartition, OffsetAndMetadata> offsets = new HashMap<>();

        consumer.subscribe(Collections.singletonList(AppConfigs.topicName), new ConsumerRebalanceListener() {

            /*该方法会在消费者停止读取消息之后，再均衡开始之前就调用*/
            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                System.out.println("再均衡即将触发");
                // 提交已经处理的偏移量
                consumer.commitSync(offsets);
            }

            /*该方法会在重新分配分区之后，消费者开始读取消息之前被调用*/
            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {

            }
        });

        logger.info("Start consuming messages...");


        try {
            while (true) {
                ConsumerRecords<Integer, String> records = consumer.poll(Duration.of(100, ChronoUnit.MILLIS));
                for (ConsumerRecord<Integer, String> record : records) {
                    System.out.println(record);
                    TopicPartition topicPartition = new TopicPartition(record.topic(), record.partition());
                    OffsetAndMetadata offsetAndMetadata = new OffsetAndMetadata(record.offset() + 1, "no metaData");
                    /*TopicPartition重写过hashCode和equals方法，所以能够保证同一主题和分区的实例不会被重复添加*/
                    offsets.put(topicPartition, offsetAndMetadata);
                }
                consumer.commitAsync(offsets, null);
            }
        } finally {
            consumer.close();
        }
    }
}
