package com.shangde.datacenter;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.utils.EnsurePath;
import org.apache.hadoop.hbase.zookeeper.ZkAclReset;

import java.io.Serializable;

public class CuratorTest implements Serializable {



    /**
     * Zookeeper info
     */
    private static final String ZK_ADDRESS = "192.168.0.80:2181";
    private static final String ZK_PATH = "/lx_test_node";
    static CuratorFramework client = CuratorFrameworkFactory.builder().
            connectString(ZK_ADDRESS)
            .retryPolicy(new ExponentialBackoffRetry(1000,3)).build();

    public static void main(String[] args) throws InterruptedException {
         client.start();
         LeaderSelector selector = new LeaderSelector(client, ZK_PATH, new LeaderSelectorListener() {
             @Override
             public void takeLeadership(CuratorFramework curatorFramework) throws Exception {
                 System.out.println("成为leader");
                 int  i = 0;
                 while(true){
                     System.out.println("Running ..............");
                     i ++;
                     Thread.sleep(1500);
                     if(i == 20)break;
                 }
             }

             @Override
             public void stateChanged(CuratorFramework curatorFramework, ConnectionState connectionState) {
                 System.out.println("ConnectionState changed now is " + connectionState);
             }
         });
         selector.autoRequeue();
         selector.start();
         while (true){

         }

    }

    private static void registerListener(LeaderSelectorListener listener) {
        // 1.Connect to zk
        CuratorFramework client = CuratorFrameworkFactory.newClient(
                ZK_ADDRESS,
                new RetryNTimes(10, 5000)
        );
        client.start();

        // 2.Ensure path
        try {
            new EnsurePath(ZK_PATH).ensure(client.getZookeeperClient());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 3.Register listener
        LeaderSelector selector = new LeaderSelector(client, ZK_PATH, listener);
        selector.autoRequeue();
        selector.start();
    }


}
