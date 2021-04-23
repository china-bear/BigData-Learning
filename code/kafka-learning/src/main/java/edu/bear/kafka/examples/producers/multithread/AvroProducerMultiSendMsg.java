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

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import edu.bear.kafka.examples.common.AppConfigs;
import edu.bear.kafka.examples.pojo.StockDataAvro;
import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class AvroProducerMultiSendMsg {
    private static final Logger logger = LoggerFactory.getLogger(AvroProducerMultiSendMsg.class);
    private static final String applicationID = "AvroProducerMultiSendMsg";

    /**
     * private static method to read data from given dataFile
     *
     * @param dataFile data file name in resource folder
     * @return List of StockData Instance
     * @throws IOException, NullPointerException
     */
    private static List<StockDataAvro> getStocks(String dataFile) throws IOException {
        File file = new File(dataFile);
        CsvSchema schema = CsvSchema.builder()
                .addColumn("symbol", CsvSchema.ColumnType.STRING)
                .addColumn("series", CsvSchema.ColumnType.STRING)
                .addColumn("open", CsvSchema.ColumnType.NUMBER)
                .addColumn("high", CsvSchema.ColumnType.NUMBER)
                .addColumn("low", CsvSchema.ColumnType.NUMBER)
                .addColumn("close", CsvSchema.ColumnType.NUMBER)
                .addColumn("last", CsvSchema.ColumnType.NUMBER)
                .addColumn("previousClose", CsvSchema.ColumnType.NUMBER)
                .addColumn("totalTradedQty", CsvSchema.ColumnType.NUMBER)
                .addColumn("totalTradedVal", CsvSchema.ColumnType.NUMBER)
                .addColumn("tradeDate", CsvSchema.ColumnType.STRING)
                .addColumn("totalTrades", CsvSchema.ColumnType.NUMBER)
                .addColumn("isinCode", CsvSchema.ColumnType.STRING)
                .build();
        MappingIterator<StockDataAvro> stockDataIterator = new CsvMapper().readerFor(StockDataAvro.class).with(schema).readValues(file);
        return stockDataIterator.readAll();
    }

    /**
     * Application entry point
     * you must provide the topic name and at least one event file
     *
     * @param args topicName (Name of the Kafka topic) list of files (list of files in the classpath)
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Please provide command line arguments: topicName EventFiles");
            System.exit(-1);
        }
        System.out.println(args);
        String topicName = args[0];
        String[] eventFiles = Arrays.copyOfRange(args, 1, args.length);
        List[] stockArrayOfList = new List[eventFiles.length];
        for (int i = 0; i < stockArrayOfList.length; i++) {
            stockArrayOfList[i] = new ArrayList<>();
        }

        logger.trace("Creating Kafka producer...");
        Properties props = new Properties();
        props.put(ProducerConfig.CLIENT_ID_CONFIG, applicationID);
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, AppConfigs.bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
        props.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");

        KafkaProducer<String, Object> producer = new KafkaProducer<>(props);

        //For each data file
        List<Thread> dispatchers = new ArrayList<>();
        try {
            for (int i = 0; i < eventFiles.length; i++) {
                logger.info("Preparing data for " + eventFiles[i]);
                for (StockDataAvro s : getStocks(eventFiles[i])) {
                    stockArrayOfList[i].add(s);
                }
                dispatchers.add(new Thread(new AvroProducerRunner(producer, topicName, eventFiles[i], stockArrayOfList[i]), eventFiles[i]));
                dispatchers.get(i).start();
            }
        } catch (Exception e) {
            logger.error("Can't read data files");
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

