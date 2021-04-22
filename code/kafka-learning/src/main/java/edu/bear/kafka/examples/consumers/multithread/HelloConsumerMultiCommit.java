package edu.bear.kafka.examples.consumers.multithread;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import edu.bear.kafka.examples.common.AppConfigs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Kafka消费者——多线程消费
 */

public class HelloConsumerMultiCommit {

    private static final Logger logger = LoggerFactory.getLogger(HelloConsumerMultiCommit.class);
    private static final String applicationID = "HelloConsumerMultiCommit";
    private static final int consumerNum = 10;

    public static void main(String[] args) {

        logger.info("Starting Kafka Json consumer...");

        ExecutorService executor = Executors.newFixedThreadPool(3);
        final List<KafkaConsumerRunner> consumers = new ArrayList<>();
        for (int i = 0; i < consumerNum; i++) {
            KafkaConsumerRunner consumer = new KafkaConsumerRunner(i, AppConfigs.groupName, Arrays.asList(AppConfigs.topicName));
            consumers.add(consumer);
            executor.submit(consumer);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Stopping Consumers...");
            for (KafkaConsumerRunner c : consumers) {
                c.shutdown();
            }
            logger.info("Closing Application");
            executor.shutdown();
            try {
                executor.awaitTermination(5000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }));
    }
}
