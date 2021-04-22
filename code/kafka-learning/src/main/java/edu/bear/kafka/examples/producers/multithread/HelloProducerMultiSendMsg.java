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

package edu.bear.kafka.examples.producers.multithread;


import edu.bear.kafka.examples.common.AppConfigs;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;

import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * A Kafka producer that sends numEvents (# of messages) to a given topicName
 *
 * @author prashant
 * @author www.learningjournal.guru
 * https://github.com/LearningJournal/Kafka-Streams-Real-time-Stream-Processing/tree/master/hello-producerHelloProducer
 */

public class HelloProducerMultiSendMsg {
    private static final Logger logger = LoggerFactory.getLogger(HelloProducerMultiSendMsg.class);
    private static final String applicationID = "HelloProducerMultiSendMsg";

    public static void main(String[] args) {
        List<Thread> dispatchers = new ArrayList<>();
        logger.info("Creating Kafka Producer...");
        Properties props = new Properties();
        props.put(ProducerConfig.CLIENT_ID_CONFIG, applicationID);
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, AppConfigs.bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        KafkaProducer<Integer, String> producer = new KafkaProducer<>(props);

        try {
            for (int i = 0; i < 5; i++) {
                dispatchers.add(new Thread(new KafkaProducerRunner(producer, AppConfigs.topicName, i, AppConfigs.numEvents), "Simple Message-" + i));
                dispatchers.get(i).start();
            }
        } catch (Exception e) {
            logger.error("Exception");
            producer.close();
            throw new RuntimeException(e);
        }

        //Wait for threads
        try {
            for (Thread t : dispatchers) {
                t.join();
            }
        } catch (InterruptedException e) {
            logger.error("Thread Interrupted ");
            throw new RuntimeException(e);
        } finally {
            producer.close();
            logger.info("Finished Application - Closing Kafka Producer.");
        }

    }
}
