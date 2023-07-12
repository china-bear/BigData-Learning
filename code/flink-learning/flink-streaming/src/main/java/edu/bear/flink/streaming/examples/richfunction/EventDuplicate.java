package edu.bear.flink.streaming.examples.richfunction;

import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

import java.sql.Types;

public class EventDuplicate {

    private static class Event {
        public final String key;
        public final long timestamp;

        public Event(String key, long timestamp) {
            this.key = key;
            this.timestamp = timestamp;
        }
    }

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

/*        env.addSource(new MyEventSource())
                .keyBy(e -> e.key)
                .flatMap(new Deduplicator())
                .print();*/

        env.execute();

    }



    public static class Deduplicator extends RichFlatMapFunction<Event, Event> {
        ValueState<Boolean> keyHasBeenSeen;

        @Override
        public void open(Configuration conf) {
            ValueStateDescriptor<Boolean> desc = new ValueStateDescriptor<>("keyHasBeenSeen", Types.BOOLEAN);
            keyHasBeenSeen = getRuntimeContext().getState(desc);
        }

        @Override
        public void flatMap(Event event, Collector<Event> out) throws Exception {
            if (keyHasBeenSeen.value() == null) {
                out.collect(event);
                keyHasBeenSeen.update(true);
            }
        }
    }
}
