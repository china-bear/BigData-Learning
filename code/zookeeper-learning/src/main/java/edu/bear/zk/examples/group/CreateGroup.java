package edu.bear.zk.examples.group;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;


import edu.bear.zk.examples.util.MoreZKPaths;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** https://github.com/sleberknight/zookeeper-samples
 连接到 ZooKeeper 服务
 zkCli.sh -server 127.0.0.1:2181
 查看当前 ZooKeeper 中所包含的内容:   ls /
 */

public class CreateGroup implements Watcher{
    private static final Logger LOG = LoggerFactory.getLogger(CreateGroup.class);

    private static final int SESSION_TIMEOUT = 5000;
    private ZooKeeper zk;
    private final CountDownLatch connectedSignal = new CountDownLatch(1);

    public void connect(String hosts) throws IOException, InterruptedException {
        zk = new ZooKeeper(hosts, SESSION_TIMEOUT, this);
        connectedSignal.await();
    }

    @Override
    public void process(WatchedEvent event) { // Watcher interface
        if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
            LOG.info("Connected...");
            connectedSignal.countDown();
        }
    }

    public void create(String groupName) throws KeeperException, InterruptedException {
        String path = MoreZKPaths.makeAbsolutePath(groupName);
        String createdPath = zk.create(path,
                null /*data*/,
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT);
        LOG.info("Created {}", createdPath);
    }

    public void close() throws InterruptedException {
        zk.close();
    }

    public static void main(String[] args) throws Exception {
        CreateGroup createGroup = new CreateGroup();
        createGroup.connect(args[0]);
        createGroup.create(args[1]);
        createGroup.close();
    }
}
