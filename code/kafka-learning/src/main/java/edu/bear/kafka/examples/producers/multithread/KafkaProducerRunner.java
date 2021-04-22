package edu.bear.kafka.examples.producers.multithread;

import edu.bear.kafka.examples.common.AppConfigs;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Runnable class send a batch of Json messages to Kafka Producer
 */
public class KafkaProducerRunner implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerRunner.class);

    private final KafkaProducer<Integer, String> producer;
    private final String topicName;
    private final Integer messageKey;
    private final Integer numEvents;

    /**
     * A dispatcher thread takes a kafka producer and send a batch of messages to the given topic
     *
     * @param producer   A valid producer instance
     * @param topicName  Name of the Kafka Topic
     * @param messageKey Message key for the entire batch
     */
    KafkaProducerRunner(KafkaProducer<Integer, String> producer, String topicName, Integer messageKey, Integer numEvents) {
        this.producer = producer;
        this.topicName = topicName;
        this.messageKey = messageKey;
        this.numEvents = numEvents;
    }

    @Override
    public void run() {
        int messageCounter = 1;
        String producerName = Thread.currentThread().getName();

        logger.trace("Starting Producer thread" + producerName);

        for (int i = 1; i <= numEvents; i++) {
            producer.send(new ProducerRecord<Integer, String>(topicName, messageKey, "Simple Message-" + i));
            messageCounter++;
        }
        logger.trace("Finished Producer thread" + producerName + " sent " + messageCounter + " messages");
    }

}
