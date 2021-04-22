package edu.bear.kafka.examples.consumers;

import edu.bear.kafka.examples.common.AppConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Kafka消费者—— 消费优雅的退出
 * https://github.com/heibaiying/BigData-Notes/tree/master/code/Kafka/kafka-basis
 */
public class HelloConsumerGracefulExit {

    private static final Logger logger = LoggerFactory.getLogger(HelloConsumerGracefulExit.class);
    private static final String applicationID = "HelloConsumerWithPartitions";

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

        consumer.subscribe(Collections.singletonList(AppConfigs.topicName));


        /*调用wakeup优雅的退出*/
        final Thread mainThread = Thread.currentThread();
        new Thread(() -> {
            Scanner sc = new Scanner(System.in);
            while (sc.hasNext()) {
                if ("exit".equals(sc.next())) {
                    consumer.wakeup();
                    try {
                        /*等待主线程完成提交偏移量、关闭消费者等操作*/
                        mainThread.join();
                        break;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        try {
            while (true) {
                ConsumerRecords<Integer, String> records = consumer.poll(Duration.of(100, ChronoUnit.MILLIS));
                for (ConsumerRecord<Integer, String> record : records) {
                    System.out.printf("topic = %s,partition = %d, key = %s, value = %s, offset = %d,\n",
                            record.topic(), record.partition(), record.key(), record.value(), record.offset());
                }
            }
        } catch (WakeupException e) {
            //对于wakeup()调用引起的WakeupException异常可以不必处理
        } finally {
            consumer.close();
            System.out.println("consumer关闭");
        }

    }
}
