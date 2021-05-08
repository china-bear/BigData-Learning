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

package edu.bear.kafka.examples.producers;


import edu.bear.kafka.examples.common.AppConfigs;
import edu.bear.kafka.examples.pojo.LogLine;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutionException;

/**
 * https://github.com/gwenshap/kafka-examples/tree/master/AvroProducerExample
 */

public class AvroClicksProducer {
    private static final Logger logger = LoggerFactory.getLogger(AvroClicksProducer.class);
    private static final String applicationID = "AvroClicksProducer";

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        logger.info("Creating Kafka Producer...");
        Properties props = new Properties();
        props.put(ProducerConfig.CLIENT_ID_CONFIG, applicationID);
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "10.172.175.222:9092");
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        // 使用Confluent实现的KafkaAvroSerializer
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
        props.put("schema.registry.url", "http://10.172.175.222:8081");

        logger.info("Start sending messages...");
        KafkaProducer<String, LogLine> producer = new KafkaProducer<String, LogLine>(props);

        try {
            for (long nEvents = 0; nEvents < 100; nEvents++) {
                LogLine event = EventGenerator.getNext();

                // Using IP as key, so events from same IP will go to same partition
                ProducerRecord<String, LogLine> record = new ProducerRecord<String, LogLine>("clicks", event.getIp().toString(), event);
                RecordMetadata metadata = producer.send(record).get();

                logger.info("Message " + nEvents + " persisted with offset " + metadata.offset()
                        + " and timestamp on " + new Timestamp(metadata.timestamp()));
        } }finally {
                logger.info("Finished - Closing Kafka Producer.");
                producer.close();
        }

    }
}
