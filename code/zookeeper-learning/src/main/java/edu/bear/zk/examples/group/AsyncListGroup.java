package edu.bear.zk.examples.group;

import edu.bear.zk.examples.util.ConnectionWatcher;
import edu.bear.zk.examples.util.MoreZKPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/** https://github.com/sleberknight/zookeeper-samples
连接到 ZooKeeper 服务
zkCli.sh -server 127.0.0.1:2181
查看当前 ZooKeeper 中所包含的内容:   ls /
 */

public class AsyncListGroup extends ConnectionWatcher {
    private static final Logger LOG = LoggerFactory.getLogger(AsyncListGroup.class);

    public static void main(String[] args) throws Exception {

        if (args.length == 0) {
            System.out.println("AsyncListGroup 10.146.237.9:2181 zookeeper");
            return;
        }

        AsyncListGroup asyncListGroup = new AsyncListGroup();
        asyncListGroup.connect(args[0]);
        asyncListGroup.list(args[1]);
        asyncListGroup.close();
    }

    public void list(final String groupName) throws InterruptedException {
        String path = MoreZKPaths.makeAbsolutePath(groupName);

        // In real code, you would not use the async API the way it's being used here. You would
        // go off and do other things without blocking like this example does.
        final CountDownLatch latch = new CountDownLatch(1);
        zk.getChildren(path, false,
                (rc, path1, ctx, children) -> {
                    LOG.info("Called back for path {} with return code {}", path1, rc);
                    if (children == null) {
                        LOG.info("Group {} does not exist", groupName);
                    } else {
                        if (children.isEmpty()) {
                            LOG.info("No members in group {}", groupName);
                            return;
                        }
                        for (String child : children) {
                            LOG.info(child);
                        }
                    }
                    latch.countDown();
                }, null /* optional context object */);
        LOG.info("Awaiting latch countdown...");
        latch.await();
    }

}
