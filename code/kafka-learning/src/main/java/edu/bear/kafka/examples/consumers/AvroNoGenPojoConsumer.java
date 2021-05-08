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

import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;


/**
 * http://zhongmingmao.me/2019/03/26/kafka-docker-schema-registry/
 * https://github.com/confluentinc/cp-all-in-one
 * https://zhmin.github.io/2019/04/23/kafka-schema-registry/
 */

public class AvroNoGenPojoConsumer {
    private static final Logger logger = LoggerFactory.getLogger(AvroNoGenPojoConsumer.class);
    private static final String applicationID = "AvroConfluentConsumer";

    public static void main(String[] args) throws InterruptedException {

        logger.info("Creating Kafka Producer...");
        Properties props = new Properties();
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, applicationID);
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "10.172.175.222:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, AppConfigs.groupName);  //指定分组ID
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");  //Automatic Offset Committing
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, AppConfigs.autoCommitInterval);

        // 使用Confluent实现的KafkaAvroSerializer
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());
        // 添加Schema服务的地址，用于获取Schema
        props.put("schema.registry.url", "http://10.172.175.222:8081");  //查看Schema curl -X GET http://10.172.175.222:8081/subjects

        Consumer<String, GenericRecord> consumer = new KafkaConsumer<>(props);

        logger.info("Start consuming messages...");

        consumer.subscribe(Collections.singletonList("hello-topic"));
        try {
            while (true) {
                ConsumerRecords<String, GenericRecord> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, GenericRecord> record : records) {
                    GenericRecord user = record.value();
                    logger.info("key={} value=[id={}, name={}, age={}], partition={}, offset={}",
                            record.key(), user.get("id"), user.get("name"), user.get("age"), record.partition(), record.offset());
                }
            }
        } finally {
            logger.info("Closing consumer ");
            consumer.close();
        }
    }
}

