package edu.bear.zk.examples.group;


import edu.bear.zk.examples.util.ConnectionWatcher;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** https://github.com/sleberknight/zookeeper-samples
 连接到 ZooKeeper 服务
 zkCli.sh -server 127.0.0.1:2181
 查看当前 ZooKeeper 中所包含的内容:   ls /
 */

public class JoinGroup extends ConnectionWatcher {

    private static final Logger LOG = LoggerFactory.getLogger(JoinGroup.class);

    public void join(String groupName, String memberName) throws KeeperException, InterruptedException {
        String path = ZKPaths.makePath(groupName, memberName);
        String createdPath = zk.create(path,
                null/*data*/,
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL);
        LOG.info("Created {}", createdPath);
    }

    public static void main(String[] args) throws Exception {
        JoinGroup joinGroup = new JoinGroup();
        joinGroup.connect(args[0]);
        joinGroup.join(args[1], args[2]);

        // stay alive until process is killed or thread is interrupted
        Thread.sleep(Long.MAX_VALUE);
    }

}
