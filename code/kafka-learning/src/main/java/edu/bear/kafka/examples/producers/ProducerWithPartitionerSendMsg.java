package edu.bear.kafka.examples.producers;

import edu.bear.kafka.examples.common.AppConfigs;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/*
 * Kafka生产者示例——指定分区器 发送消息
 */
public class ProducerWithPartitionerSendMsg {

    private static final Logger logger = LoggerFactory.getLogger(HelloProducer.class);
    private static final String applicationID = "ProducerWithPartitionerSendMsg";

    public static void main(String[] args) {


        logger.info("Creating Kafka Producer...");
        Properties props = new Properties();
        props.put(ProducerConfig.CLIENT_ID_CONFIG, applicationID);
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, AppConfigs.bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        /*传递自定义分区器*/
        props.put("partitioner.class", "edu.bear.kafka.examples.producers.partitioner.CustomPartitioner");
        /*传递分区器所需的参数*/
        //props.put("pass.line", 6);

        logger.info("Start sending messages...");
        Producer<Integer, String> producer = new KafkaProducer<>(props);

        try {
            for (int i = 1; i <= AppConfigs.numEvents; i++) {
                RecordMetadata metadata = producer.send(new ProducerRecord<>(AppConfigs.topicName, i, "Simple Message-" + i)).get();
                logger.info("Message " + i + " persisted with offset " + metadata.offset()
                        + " in partition " + metadata.partition());
            }
        } catch (Exception e) {
            logger.error("Exception occurred ");
            throw new RuntimeException(e);
        } finally {
            logger.info("Finished Application - Closing Kafka Producer.");
            producer.close();
        }

        producer.close();
    }
}