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

public class DeleteGroup extends ConnectionWatcher {

    private static final Logger LOG = LoggerFactory.getLogger(DeleteGroup.class);

    public void delete(String groupName) throws KeeperException, InterruptedException {
        String path = MoreZKPaths.makeAbsolutePath(groupName);

        try {
            List<String> children = zk.getChildren(path, false);
            for (String child : children) {
                zk.delete(path + "/" + child, -1);
            }
            zk.delete(path, -1);
            LOG.info("Deleted group {} at path {}", groupName, path);
        } catch (KeeperException.NoNodeException e) {
            LOG.error("Group {} does not exist", groupName, e);
        }
    }


    public static void main(String[] args) throws Exception {
        DeleteGroup deleteGroup = new DeleteGroup();
        deleteGroup.connect(args[0]);
        deleteGroup.delete(args[1]);
        deleteGroup.close();
    }

}
