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
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * Kafka生产者示例——同步发送消息
 * https://github.com/heibaiying/BigData-Notes/tree/master/code/Kafka/kafka-basis
 */

public class HelloProducerSyncSendMsg {
    private static final Logger logger = LoggerFactory.getLogger(HelloProducerSyncSendMsg.class);
    private static final String applicationID = "HelloProducerSyncSendMsg";

    public static void main(String[] args) {

        //System.out.println(HelloProducer.class.getSimpleName());
        //System.out.println(HelloProducer.class.getName());
        logger.info("Creating Kafka Producer...");
        Properties props = new Properties();
        props.put(ProducerConfig.CLIENT_ID_CONFIG, applicationID);
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, AppConfigs.bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        KafkaProducer<Integer, String> producer = new KafkaProducer<>(props);

        logger.info("Start sending messages...");

        try {
            for (int i = 1; i <= AppConfigs.numEvents; i++) {
                RecordMetadata metadata = producer.send(new ProducerRecord<>(AppConfigs.topicName, i, "Simple Message-" + i)).get();
                logger.info("Message " + i + " persisted with offset " + metadata.offset()
                        + " and timestamp on " + new Timestamp(metadata.timestamp()));
            }
        } catch (Exception ex) {
            logger.error("Exception occurred.");
            throw new RuntimeException(ex);
        } finally {
            producer.close();
            logger.info("Finished Application - Closing Kafka Producer.");
        }
    }
}
