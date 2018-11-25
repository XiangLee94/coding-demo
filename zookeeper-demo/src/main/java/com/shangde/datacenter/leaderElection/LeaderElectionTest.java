package com.shangde.datacenter.leaderElection;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.Random;

public class LeaderElectionTest {

    private static boolean isMaster = false;
    private static String path;
    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        MyWatch myWatch = new MyWatch();

        ZooKeeper zk = new ZooKeeper("192.168.0.80:" + 2181,
                1000, myWatch);

        myWatch.setZooKeeper(zk);


        //先创建一个节点存储创建过的机器节点
        try {
            zk.create("/lx_test_node", ("主机").getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        } catch (KeeperException.NodeExistsException e) {
            System.out.println("主机节点已创建");
        }
        //模拟新加入一台机器
        Random r = new Random();
        String num = r.nextInt(100000) + "";

        String sequentialPath = zk.create("/lx_test_node/children", ("运行server的ip:" + num).getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        path = sequentialPath;
        System.out.println("同步创建临时顺序节点成功：" + sequentialPath);

        //注册监听事件
        zk.getChildren("/lx_test_node", true);

        //此处模拟机器持续运行着
        while (true) {
            //zk.close();
        }



    }

    public static void callback(String currentPath) {
        if(path.equals("/lx_test_node/"+currentPath))
            isMaster =true;
        else isMaster = false;
        while(isMaster){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(path+"  running");
        }
    }
}