package com.shangde.datacenter;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by xuyh at 2017/11/30 14:40.
 * <p>
 * **最优方案**
 * <pre>
 *     方案三：子节点监听方式
 *
 *     实现思路：监听子节点状态
 *     1.在父节点(持久化)下创建临时节点，实际创建的节点路径会根据数量进行自增(ZK自编号方式创建节点)。
 *     2.创建节点成功后，首先获取父节点下的子节点列表，判断本线程的路径后缀编号是否是所有子节点中最小的，若是则成为leader，反之监听本节点前一个节点(路径排序为本节点路径数字减一的节点)变动状态(通过getData()方法注册watcher)
 *     3.当监听对象状态变动(节点删除状态)后watcher会接收到通知，这时再次判断父节点下的子节点的排序状态，若满足本线程的路径后缀编号最小则成为leader，反之继续注册watcher监听前一个节点状态
 */
public class ZKLeaderTwo {
    private static ZKLeaderTwo zkLeaderTwo;
    private Logger logger = LoggerFactory.getLogger(ZKLeaderTwo.class);
    private final static String BASE_NODE_PATH = "/lx_test_node";
    private final static String NODE_PATH = "host_process_no_";
    private String finalNodePath;

    //是否是主节点标志位
    private boolean leader = false;

    private String host = "192.168.0.80";
    private String port = "2181";
    private ZooKeeper zooKeeper;
    private PreviousNodeWatcher previousNodeWatcher;

    //是否连接成功标志位
    private boolean connected = false;

    public static ZKLeaderTwo create(String host, String port) {
        ZKLeaderTwo zkLeaderTwo = new ZKLeaderTwo(host, port);
        zkLeaderTwo.connectZookeeper();
        return zkLeaderTwo;
    }

    public boolean leader() {
        return leader;
    }

    public void close() {
        disconnectZooKeeper();
    }

    private ZKLeaderTwo(String host, String port) {
        this.host = host;
        this.port = port;
        this.previousNodeWatcher = new PreviousNodeWatcher(this);
    }

    private boolean connectZookeeper() {
        try {
            zooKeeper = new ZooKeeper(host + ":" + port, 60000, event -> {
                if (event.getState() == Watcher.Event.KeeperState.AuthFailed) {
                    leader = false;
                } else if (event.getState() == Watcher.Event.KeeperState.Disconnected) {
                    leader = false;
                } else if (event.getState() == Watcher.Event.KeeperState.Expired) {
                    leader = false;
                } else {
                    if (event.getType() == Watcher.Event.EventType.None) {//说明连接成功了
                        connected = true;
                    }
                }
            });

            int i = 1;
            while (!connected) {//等待异步连接成功,超过时间30s则退出等待
                if (i == 100)
                    break;
                Thread.sleep(300);
                i++;
            }

            if (connected) {
                if (zooKeeper.exists(BASE_NODE_PATH, false) == null) {//创建父节点
                    zooKeeper.create(BASE_NODE_PATH, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                }
                //创建子节点
                finalNodePath = zooKeeper.create(BASE_NODE_PATH + "/" + NODE_PATH, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);

                //检查一次是否是主节点
                checkLeader();
            } else {
                logger.warn("Connect zookeeper failed. Time consumes 30 s");
                return false;
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return false;
        }
        return true;
    }

    private boolean disconnectZooKeeper() {
        if (zooKeeper == null)
            return false;
        try {
            zooKeeper.close();
            connected = false;
            leader = false;
        } catch (Exception e) {
            logger.warn(String.format("ZK disconnect failed. [%s]", e.getMessage()), e);
        }
        return true;
    }

    private void checkLeader() {
        if (!connected)
            return;
        try {
            //获取子节点列表，若没有成为leader，注册监听，监听对象应当是比本节点路径编号小一(或者排在前面一位)的节点
            List<String> childrenList = zooKeeper.getChildren(BASE_NODE_PATH, false);

            if (judgePathNumMin(childrenList)) {
                leader = true;//成为leader
            } else {
                watchPreviousNode(childrenList);
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
    }

    private boolean judgePathNumMin(List<String> paths) {
        if (paths.isEmpty())
            return true;
        if (paths.size() >= 2) {
            //对无序状态的path列表按照编号升序排序
            paths.sort((str1, str2) -> {
                int num1;
                int num2;
                String string1 = str1.substring(NODE_PATH.length(), str1.length());
                String string2 = str2.substring(NODE_PATH.length(), str2.length());
                num1 = Integer.parseInt(string1);
                num2 = Integer.parseInt(string2);
                if (num1 > num2) {
                    return 1;
                } else if (num1 < num2) {
                    return -1;
                } else {
                    return 0;
                }
            });
        }

        String minId = paths.get(0);
        return finalNodePath.equals(BASE_NODE_PATH + "/" + minId);
    }

    private void watchPreviousNode(List<String> paths) {
        if (paths.isEmpty() || paths.size() == 1) {
            return;
        }
        int currentNodeIndex = paths.indexOf(finalNodePath.substring((BASE_NODE_PATH + "/").length(), finalNodePath.length()));
        String previousNodePath = BASE_NODE_PATH + "/" + paths.get(currentNodeIndex - 1);
        //通过getData方法再次注册watcher
        try {
            zooKeeper.getData(previousNodePath, previousNodeWatcher, new Stat());
        } catch (Exception e) {
            logger.warn(String.format("Previous node watcher register failed! message: [%s]", e.getMessage()), e);
        }
    }

    private class PreviousNodeWatcher implements Watcher {
        private ZKLeaderTwo context;

        PreviousNodeWatcher(ZKLeaderTwo context) {
            this.context = context;
        }

        @Override
        public void process(WatchedEvent event) {
            //节点被删除了，说明这个节点放弃了leader
            if (event.getType() == Event.EventType.NodeDeleted) {
                context.checkLeader();
            }
        }
    }
}