/*
 * Copyright (c) 2018. Prashant Kumar Pandey
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package edu.bear.kafka.examples.consumers;

import edu.bear.kafka.examples.common.AppConfigs;
import edu.bear.kafka.examples.pojo.LogLine;
import edu.bear.kafka.examples.pojo.SessionState;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;


/**
 *  https://github.com/gwenshap/kafka-examples/tree/master/AvroConsumerExample
 */

public class AvroClicksConsumer {
    private static final Logger logger = LoggerFactory.getLogger(AvroClicksConsumer.class);
    private static final String applicationID = "AvroClicksConsumer";

    private final KafkaConsumer<String, LogLine> consumer;
    private final KafkaProducer<String, LogLine> producer;
    private final String inputTopic;
    private final String outputTopic;
    private Map<String, SessionState> state = new HashMap<String, SessionState>();


    public AvroClicksConsumer(String brokers, String groupId, String inputTopic, String outputTopic, String url) {

        this.consumer = new KafkaConsumer(ConsumerConfig(brokers, groupId, url));
        this.producer = new KafkaProducer<String, LogLine>(ProducerConfig(brokers, url));
        this.inputTopic = inputTopic;
        this.outputTopic = outputTopic;

    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        String groupId = "AvroClicksSessionizer";
        String inputTopic = "clicks";
        String outputTopic = "sessionized_clicks";
        String url = "http://10.172.175.222:8081";
        String brokers = "10.172.175.222:9092" ;
        AvroClicksConsumer sessionizer = new AvroClicksConsumer(brokers, groupId, inputTopic, outputTopic, url);
        sessionizer.run();

    }

    private void run() throws ExecutionException, InterruptedException {

        logger.info("Start consuming messages...");
        consumer.subscribe(Collections.singletonList(inputTopic));
        System.out.println("Reading topic:" + inputTopic);
        try {
            while (true) {
                ConsumerRecords<String, LogLine> records = consumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, LogLine> record: records) {
                    String ip = record.key();
                    LogLine event = record.value();

                    SessionState oldState = state.get(ip);
                    int sessionId = 0;
                    if (oldState == null) {
                        state.put(ip, new SessionState(event.getTimestamp(), 0));
                    } else {
                        sessionId = oldState.getSessionId();
                        // if the old timestamp is more than 30 minutes older than new one, we have a new session
                        if (oldState.getLastConnection() < event.getTimestamp() - (30 * 60 * 1000))
                            sessionId = sessionId + 1;
                        SessionState newState = new SessionState(event.getTimestamp(), sessionId);
                        state.put(ip, newState);
                    }
                    event.setSessionid(sessionId);
                    System.out.println(event.toString());
                    ProducerRecord<String, LogLine> sessionizedEvent = new ProducerRecord<String, LogLine>(outputTopic, event.getIp().toString(), event);
                    RecordMetadata metadata = producer.send(sessionizedEvent).get();
                    logger.info("Message " + ip + " persisted with offset " + metadata.offset()
                            + " and timestamp on " + new Timestamp(metadata.timestamp()));
                }
                consumer.commitSync();
            }
        } finally {
            consumer.close();
            producer.close();
            logger.info("Finished Application - Closing Kafka Producer and Consumer");
        }
    }


    private Properties ConsumerConfig(String brokers, String groupId, String url) {
        Properties props = new Properties();
        props.put("bootstrap.servers", brokers);
        props.put("group.id", groupId);
        props.put("auto.commit.enable", "false");
        props.put("auto.offset.reset", "earliest");
        props.put("schema.registry.url", url);
        props.put("specific.avro.reader", true);
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "io.confluent.kafka.serializers.KafkaAvroDeserializer");

        return props;
    }


    private Properties ProducerConfig(String brokers, String url) {
        Properties props = new Properties();
        props.put("bootstrap.servers", brokers);
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("key.serializer", "io.confluent.kafka.serializers.KafkaAvroSerializer");
        props.put("value.serializer", "io.confluent.kafka.serializers.KafkaAvroSerializer");
        props.put("schema.registry.url", url);

        return props;
    }

}

