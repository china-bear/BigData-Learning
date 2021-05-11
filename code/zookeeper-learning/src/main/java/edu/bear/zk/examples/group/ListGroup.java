package edu.bear.zk.examples.group;

import java.util.List;

import edu.bear.zk.examples.util.ConnectionWatcher;
import edu.bear.zk.examples.util.MoreZKPaths;
import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** https://github.com/sleberknight/zookeeper-samples
 连接到 ZooKeeper 服务
 zkCli.sh -server 127.0.0.1:2181
 查看当前 ZooKeeper 中所包含的内容:   ls /
 */

public class ListGroup extends ConnectionWatcher {

    private static final Logger LOG = LoggerFactory.getLogger(ListGroup.class);

    public void list(String groupName) throws KeeperException, InterruptedException {
        String path = MoreZKPaths.makeAbsolutePath(groupName);

        try {
            List<String> children = zk.getChildren(path, false);
            if (children.isEmpty()) {
                LOG.info("No members in group {}", groupName);
                return;
            }
            for (String child : children) {
                LOG.info(child);
            }
        } catch (KeeperException.NoNodeException e) {
            LOG.error("Group {} does not exist", groupName, e);
        }
    }

    public static void main(String[] args) throws Exception {
        ListGroup listGroup = new ListGroup();
        listGroup.connect(args[0]);
        listGroup.list(args[1]);
        listGroup.close();
    }


}
