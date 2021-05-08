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


import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * http://zhongmingmao.me/2019/03/26/kafka-docker-schema-registry/
 * https://github.com/confluentinc/cp-all-in-one
 */

public class AvroNoGenPojoProducer {
    private static final Logger logger = LoggerFactory.getLogger(AvroNoGenPojoProducer.class);
    private static final String applicationID = "AvroConfluentProducer";

    private static final String USER_SCHEMA = "{\"type\": \"record\", \"name\": \"User\", " +
            "\"fields\": [{\"name\": \"id\", \"type\": \"int\"}, " +
            "{\"name\": \"name\",  \"type\": \"string\"}, {\"name\": \"age\", \"type\": \"int\"}]}";

    public static void main(String[] args) throws InterruptedException {


        logger.info("Creating Kafka Producer...");
        Properties props = new Properties();
        props.put(ProducerConfig.CLIENT_ID_CONFIG, applicationID);
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "10.172.175.222:9092");

        // 使用Confluent实现的KafkaAvroSerializer
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
        /**
         * 查看配置      curl  http://10.172.175.222:8081/config
         * 将模式设置为NONE, 这样再改变schema就不会报错
         * curl -X PUT -H "Content-Type:application/json" http://localhost:8081/config -d '{"compatibility": "NONE"}'
         */
        // 添加Schema服务的地址，用于获取Schema
        props.put("schema.registry.url", "http://10.172.175.222:8081");

        // 因为没有使用Avro生成的对象，因此需要提供Avro Schema
        Schema.Parser parser = new Schema.Parser();
        Schema schema = parser.parse(USER_SCHEMA);

        // 对象类型为Avro GenericRecord
        Producer<String, GenericRecord> producer = new KafkaProducer<>(props);

        logger.info("Start sending messages...");
        Random rand = new Random();
        int id = 0;

        try {
            while (id < 100) {
                id++;
                String name = "name" + id;
                int age = rand.nextInt(40) + 1;
                // ProducerRecord.value是GenericRecord类型，包含了Schema和数据
                // 序列化器知道如何从记录获取Schema，把它保存到注册表里，并用它序列化对象数据
                GenericRecord user = new GenericData.Record(schema);
                user.put("id", id);
                user.put("name", name);
                user.put("age", age);

                ProducerRecord<String, GenericRecord> record = new ProducerRecord<>("hello-topic", user);
                producer.send(record);
                TimeUnit.SECONDS.sleep(1);
            }
        } finally {
            logger.info("Finished - Closing Kafka Producer.");
            producer.close();
        }
    }
}

