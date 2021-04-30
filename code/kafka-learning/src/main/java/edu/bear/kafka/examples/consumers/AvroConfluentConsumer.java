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
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * http://zhongmingmao.me/2019/03/26/kafka-docker-schema-registry/
 * https://github.com/confluentinc/cp-all-in-one
 * https://zhmin.github.io/2019/04/23/kafka-schema-registry/
 */

public class AvroConfluentConsumer {
    private static final Logger logger = LoggerFactory.getLogger(AvroConfluentConsumer.class);
    private static final String applicationID = "AvroConfluentConsumer";

    public static void main(String[] args) throws InterruptedException {


        logger.info("Creating Kafka Producer...");
        Properties props = new Properties();
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, applicationID);
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, AppConfigs.bootstrapServers);

        // 使用Confluent实现的KafkaAvroSerializer
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());
        // 添加Schema服务的地址，用于获取Schema
        props.put("schema.registry.url", "http://localhost:8081");

        Consumer<String, GenericRecord> consumer = new KafkaConsumer<>(props);

        logger.info("Start consuming messages...");

        consumer.subscribe(Collections.singletonList(AppConfigs.topicName));
        try {
            while (true) {
                ConsumerRecords<String, GenericRecord> records = consumer.poll(100);
                for (ConsumerRecord<String, GenericRecord> record : records) {
                    GenericRecord user = record.value();
                    logger.info("value=[id={}, name={}, age={}], partition={}, offset={}",
                            user.get("id"), user.get("name"), user.get("age"), record.partition(), record.offset());
                }
            }
        } finally {
            logger.info("Closing consumer ");
            consumer.close();
        }
    }
}

