package edu.bear.kafka.examples.common;


import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;


/**
 Print number of messages sent and messages acknowledged every N milliseconds
 This is a ProducerInterceptor that every N milliseconds prints how many records were produced and how many were
 acknowledged in last interval.

 To try:

 * Build it with `mvn clean package`
 * Add jar to classpath: `export CLASSPATH=$CLASSPATH:~./target/CountingProducerInterceptor-1.0-SNAPSHOT.jar`
 * Create a config file that includes:
 interceptor.classes=edu.bear.kafka.examples.common.CountingProducerInterceptor
 counting.interceptor.window.size.ms=10000

 * Try with the console producer:
 ` bin/kafka-console-producer.sh --broker-list localhost:9092 --topic interceptor-test --producer.config /tmp/producer.config`

 */
public class CountingProducerInterceptor implements ProducerInterceptor {

    ScheduledExecutorService executorService =
            Executors.newSingleThreadScheduledExecutor();
    static AtomicLong numSent = new AtomicLong(0);
    static AtomicLong numAcked = new AtomicLong(0);

    public ProducerRecord onSend(ProducerRecord producerRecord) {
        numSent.incrementAndGet();
        return producerRecord;
    }

    public void onAcknowledgement(RecordMetadata recordMetadata, Exception e) {
        numAcked.incrementAndGet();
    }

    public void close() {
        executorService.shutdownNow();
    }

    public void configure(Map<String, ?> map) {
        Long windowSize = Long.valueOf(
                (String) map.get("counting.interceptor.window.size.ms"));
        executorService.scheduleAtFixedRate(CountingProducerInterceptor::run,
                windowSize, windowSize, TimeUnit.MILLISECONDS);
    }

    public static void run() {
        System.out.println(numSent.getAndSet(0));
        System.out.println(numAcked.getAndSet(0));
    }
}
