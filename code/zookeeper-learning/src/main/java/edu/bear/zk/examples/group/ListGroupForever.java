package edu.bear.zk.examples.group;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import edu.bear.zk.examples.util.ConnectionHelper;
import edu.bear.zk.examples.util.MoreZKPaths;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** https://github.com/sleberknight/zookeeper-samples
 连接到 ZooKeeper 服务
 zkCli.sh -server 127.0.0.1:2181
 查看当前 ZooKeeper 中所包含的内容:   ls /
 */

public class ListGroupForever {

    private static final Logger LOG = LoggerFactory.getLogger(ListGroupForever.class);

    private final ZooKeeper zooKeeper;
    private final Semaphore semaphore = new Semaphore(1);

    public ListGroupForever(ZooKeeper zooKeeper) {
        this.zooKeeper = zooKeeper;
    }

    public static void main(String[] args) throws Exception {
        ZooKeeper zk = new ConnectionHelper().connect(args[0]);
        new ListGroupForever(zk).listForever(args[1]);
    }

    @SuppressWarnings({"squid:S2189", "InfiniteLoopStatement"})
    public void listForever(String groupName) throws KeeperException, InterruptedException {
        semaphore.acquire();
        while (true) {
            list(groupName);
            semaphore.acquire();
        }
    }

    private void list(String groupName) throws KeeperException, InterruptedException {
        String path = MoreZKPaths.makeAbsolutePath(groupName);

        List<String> children = zooKeeper.getChildren(path, event -> {
            if (event.getType() == Watcher.Event.EventType.NodeChildrenChanged) {
                semaphore.release();
            }
        });
        if (children.isEmpty()) {
            LOG.info("No members in group {}", groupName);
            return;
        }
        Collections.sort(children);
        LOG.info("{}", children);
        LOG.info("--------------------");
    }
}
