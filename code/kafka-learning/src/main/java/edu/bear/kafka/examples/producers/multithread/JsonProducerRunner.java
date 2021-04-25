package edu.bear.kafka.examples.producers.multithread;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * A runnable class to dispatch a set of messages to Kafka producer
 */

public class JsonProducerRunner implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(JsonProducerRunner.class);

    private final KafkaProducer<String, JsonNode> producer;
    private final String topicName;
    private final String messageKey;
    private final List<JsonNode> dataList;

    /**
     * A dispatcher thread takes a kafka producer and send a batch of messages to the given topic
     *
     * @param producer   A valid producer instance
     * @param topicName  Name of the Kafka Topic
     * @param messageKey Message key for the entire batch
     * @param dataList   List of message json msg
     */
    JsonProducerRunner(KafkaProducer<String, JsonNode> producer, String topicName, String messageKey, List<JsonNode> dataList) {
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
        for (JsonNode data : dataList) {
            producer.send(new ProducerRecord<>(topicName, messageKey, data));
            messageCounter++;
        }
        logger.trace("Finished Producer thread" + producerName + " sent " + messageCounter + " messages");
    }

}
