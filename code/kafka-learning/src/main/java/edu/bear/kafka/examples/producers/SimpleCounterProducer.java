package edu.bear.kafka.examples.producers;

import org.apache.kafka.clients.producer.*;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * https://github.com/gwenshap/kafka-examples/blob/master/SimpleCounter/src/main/java/com/shapira/examples/producer/simplecounter/DemoProducerNewJava.java
 */

public class SimpleCounterProducer {
    String topic;
    String sync;
    private Properties kafkaProps = new Properties();
    private Producer<String, String> producer;

    public SimpleCounterProducer(String topic) {
        this.topic = topic;
    }

    public void configure(String brokerList, String sync) {
        this.sync = sync;
        kafkaProps.put("bootstrap.servers", brokerList);

        // This is mandatory, even though we don't send keys
        kafkaProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kafkaProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kafkaProps.put("acks", "1");

        // how many times to retry when produce request fails?
        kafkaProps.put("retries", "3");
        kafkaProps.put("linger.ms", 5);
    }

    public void start() {
        producer = new KafkaProducer<String, String>(kafkaProps);
    }

    public void produce(String value) throws ExecutionException, InterruptedException {
        if (sync.equals("sync"))
            produceSync(value);
        else if (sync.equals("async"))
            produceAsync(value);
        else throw new IllegalArgumentException("Expected sync or async, got " + sync);

    }

    public void close() {
        producer.close();
    }

    /* Produce a record and wait for server to reply. Throw an exception if something goes wrong */
    private void produceSync(String value) throws ExecutionException, InterruptedException {
        ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, value);
        producer.send(record).get();

    }

    /* Produce a record without waiting for server. This includes a callback that will print an error if something goes wrong */
    private void produceAsync(String value) {
        ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, value);
        producer.send(record, new DemoProducerCallback());
    }

    private class DemoProducerCallback implements Callback {

        @Override
        public void onCompletion(RecordMetadata recordMetadata, Exception e) {
            if (e != null) {
                System.out.println("Error producing to topic " + recordMetadata.topic());
                e.printStackTrace();
            }
        }
    }

}
