package edu.bear.kafka.examples.consumers.multithread;

import edu.bear.kafka.examples.common.AppConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

/**
* https://github.com/LearningJournal/Kafka-Streams-Real-time-Stream-Processing/tree/master/json-consumer/src/main/java/guru/learningjournal/kafka/examples
*/

public class KafkaConsumerRunner implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerRunner.class);
    private final AtomicBoolean closed = new AtomicBoolean(false);
    public static final String applicationID = "KafkaConsumerRunner";
    private final KafkaConsumer<String, String> consumer;
    private final List<String> topics;
    private int consumerID;

    public KafkaConsumerRunner(int id, String groupID, List<String> topics) {
        this.topics = topics;
        this.consumerID = id;
        Properties props = new Properties();

        props.put(ConsumerConfig.CLIENT_ID_CONFIG, applicationID + "-" + id);
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, AppConfigs.bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupID);
        //Set autocommit to false so you can execute it again for the same set of messages
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        this.consumer = new KafkaConsumer<>(props);
    }

    @Override
    public void run() {
        try {
            logger.info("Staring consumer " + consumerID);
            consumer.subscribe(topics);
            while (!closed.get()) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                // Handle new records
                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf("topic = %s,partition = %d, key = %s, value = %s, offset = %d,\n",
                            record.topic(), record.partition(), record.key(), record.value(), record.offset());
                }

            }
        } catch (WakeupException ex) {
            // Ignore exception if closing
            if (!closed.get()) throw new RuntimeException(ex);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            logger.info("Closing consumer " + consumerID);
            consumer.close();
        }
    }

    // Shutdown hook which can be called from a separate thread
    public void shutdown() {
        closed.set(true);
        consumer.wakeup();
    }
}