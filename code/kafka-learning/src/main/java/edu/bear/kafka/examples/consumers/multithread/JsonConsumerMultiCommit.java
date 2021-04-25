package edu.bear.kafka.examples.consumers.multithread;

import edu.bear.kafka.examples.common.AppConfigs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Kafka消费者——多线程消费 JSON
 */

public class JsonConsumerMultiCommit {

    private static final Logger logger = LoggerFactory.getLogger(JsonConsumerMultiCommit.class);
    private static final String applicationID = "JsonConsumerMultiCommit";
    private static final int consumerNum = 10;

    public static void main(String[] args) {

        logger.info("Starting Kafka Json consumer...");

        ExecutorService executor = Executors.newFixedThreadPool(3);
        final List<JsonConsumerRunner> consumers = new ArrayList<>();
        for (int i = 0; i < consumerNum; i++) {
            JsonConsumerRunner consumer = new JsonConsumerRunner(i, AppConfigs.groupName, Arrays.asList(AppConfigs.topicName));
            consumers.add(consumer);
            executor.submit(consumer);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Stopping Consumers...");
            for (JsonConsumerRunner c : consumers) {
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
