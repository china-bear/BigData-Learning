package edu.bear.kafka.examples.producers;

import java.util.concurrent.ExecutionException;

/**
 * https://github.com/gwenshap/kafka-examples/blob/master/SimpleCounter/src/main/java/com/shapira/examples/producer/simplecounter/SimpleCounter.java
 */

public class SimpleCounterMainProducer {

    // private static DemoProducer producer;

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        if (args.length == 0) {
            System.out.println("SimpleCounter {broker-list} {topic} {type old/new} {type sync/async} {delay (ms)} {count}");
            return;
        }


        /** get arguments
         * broker-list : list of Kafka broker(s)
         * topic : Kafka topic to write message(s)

         * sync/async : specify synchronous or asynchronous mode
         * delay : delay in milliseconds
         * count : numbers to generate */

        String brokerList = args[0];
        String topic = args[1];
        String sync = args[2];
        int delay = Integer.parseInt(args[3]);
        int count = Integer.parseInt(args[4]);

    /*  if (age.equals("old"))
            producer = new DemoProducerOld(topic);
        else if (age.equals("new"))
            producer = new DemoProducerNewJava(topic);
        else {
            System.out.println("Third argument should be old or new, got " + age);
            System.exit(-1);
        } */

        SimpleCounterProducer producer = new SimpleCounterProducer(topic);

        /* start a producer */
        producer.configure(brokerList, sync);
        producer.start();

        long startTime = System.currentTimeMillis();
        System.out.println("Starting...");
        producer.produce("Starting...");

        /* produce the numbers */
        for (int i=0; i < count; i++ ) {
            producer.produce(Integer.toString(i));
            Thread.sleep(delay);
        }

        long endTime = System.currentTimeMillis();
        System.out.println("... and we are done. This took " + (endTime - startTime) + " ms.");
        producer.produce("... and we are done. This took " + (endTime - startTime) + " ms.");

        /* close shop and leave */
        producer.close();
        System.exit(0);
    }
}


