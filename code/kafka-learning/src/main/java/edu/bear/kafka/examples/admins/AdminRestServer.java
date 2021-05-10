package edu.bear.kafka.examples.admins;

import edu.bear.kafka.examples.common.AppConfigs;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServerOptions;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.KafkaFuture;

import java.util.Collections;
import java.util.Properties;

/**
 * Test with:  curl 'localhost:8080?topic=demo-topic'
 * To check that it is indeed async, suspend Kafka with SIGSTOP and then run:
 * curl 'localhost:8080?topic=demo-topic&timeout=60000' on one terminal and
 * curl 'localhost:8080?topic=demo-topic' on another.
 * Even though I only have one Vertx thread, the first command will wait 60s and the second will return immediately
 * Demonstrating that the second command did not block behind the first
 *
 * https://github.com/gwenshap/kafka-examples/tree/master/AdminClientExample
 * https://zhuanlan.zhihu.com/p/33832486   Vert.x 的上手资料
 * https://github.com/gothinkster/realworld
 */
public class AdminRestServer {

    public static Vertx vertx = Vertx.vertx(new VertxOptions().setWorkerPoolSize(1));

    public static void main(String[] args) {
        // initialize admin client
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, AppConfigs.bootstrapServers);
        props.put(AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG, 1000);
        props.put(AdminClientConfig.DEFAULT_API_TIMEOUT_MS_CONFIG, 1000);
        AdminClient admin = AdminClient.create(props);

        // create web server
        HttpServerOptions options = new HttpServerOptions().setLogActivity(true);
        vertx.createHttpServer().requestHandler(request -> {
            String topic = request.getParam("topic");
            String timeout = request.getParam("timeout");
            int timeoutMs = NumberUtils.toInt(timeout, 1000);

            DescribeTopicsResult demoTopic = admin.describeTopics(
                    Collections.singletonList(topic),
                    new DescribeTopicsOptions().timeoutMs(timeoutMs));
            demoTopic.values().get(topic).whenComplete(
                    new KafkaFuture.BiConsumer<TopicDescription, Throwable>() {
                        @Override
                        public void accept(final TopicDescription topicDescription,
                                           final Throwable throwable) {
                            if (throwable != null) {
                                System.out.println("got exception");
                                request.response().end("Error trying to describe topic "
                                        + topic + " due to " + throwable.getMessage());
                            } else {
                                request.response().end(topicDescription.toString());
                            }
                        }
                    });
        }).listen(8080);
    }
}
