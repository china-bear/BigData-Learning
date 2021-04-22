package edu.bear.kafka.examples.producers.partitioner;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.InvalidRecordException;
import org.apache.kafka.common.errors.InvalidTopicException;

import java.util.Map;

/**
 * 自定义分区器
 */
public class CustomPartitioner implements Partitioner {

    private int passLine;

    @Override
    public void configure(Map<String, ?> configs) {

        //Nothing to configure
        // passLine = (Integer) configs.get("pass.line");
    }

    /**
     * Simple partitioning on the message key
     * Odd keys to partition 0
     * Even keys to partition 1
     *
     * @param topic topic Name
     * @param key Message Key
     * @param keyBytes Key Bytes
     * @param value Message Value
     * @param valueBytes Value Bytes
     * @param cluster Cluster Object
     * @return Partition Id
     */
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        if ((keyBytes == null) || (!(key instanceof Integer)))
            throw new InvalidRecordException("Topic Key must have a valid Integer value.");

        if (cluster.partitionsForTopic(topic).size() != 2)
            throw new InvalidTopicException("Topic must have exactly two partitions");

        return (Integer) key % 2;
    }


    @Override
    public void close() {
        //Nothing to close
        System.out.println("分区器关闭");
    }


}
