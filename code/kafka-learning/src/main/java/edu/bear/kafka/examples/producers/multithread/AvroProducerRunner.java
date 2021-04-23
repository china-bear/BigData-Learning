package edu.bear.kafka.examples.producers.multithread;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * A runnable class to dispatch a set of messages to Kafka producer
 */

public class AvroProducerRunner implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(AvroProducerRunner.class);

    private final KafkaProducer<String, Object> producer;
    private final String topicName;
    private final String messageKey;
    private final List<Object> dataList;

    /**
     * A dispatcher thread takes a kafka producer and send a batch of messages to the given topic
     *
     * @param producer   A valid producer instance
     * @param topicName  Name of the Kafka Topic
     * @param messageKey Message key for the entire batch
     * @param dataList   List of message Objects
     */
    AvroProducerRunner(KafkaProducer<String, Object> producer, String topicName, String messageKey, List<Object> dataList) {
        this.producer = producer;
        this.topicName = topicName;
        this.messageKey = messageKey;
        this.dataList = dataList;
    }

    @Override
    public void run() {
        int messageCounter = 1;
        String producerName = Thread.currentThread().getName();

        logger.trace("Starting Producer thread" + producerName);
        for (Object data : dataList) {
            producer.send(new ProducerRecord<>(topicName, messageKey, data));
            messageCounter++;
        }
        logger.trace("Finished Producer thread" + producerName + " sent " + messageCounter + " messages");
    }

}
