package com.shangde.datacenter.leaderElection;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.List;

public class MyWatch implements Watcher {
    private ZooKeeper zk;
    private List<String> oldSlaveList;
    private static boolean isMaster = false;

    public void setZooKeeper(ZooKeeper zk){
        this.zk = zk;
    }
    public void process(WatchedEvent event) {
        if (Event.EventType.None == event.getType() && null == event.getPath()) {
            System.out.println("Zookeeper session established");
            try {
                List<String> stringList =  zk.getChildren("/lx_test_node",true);
                oldSlaveList = stringList;
                if(stringList.size()==0){
                    System.out.println("您现在是唯一一台机器,您就是领袖");
                }
                else{
                    System.out.println("您的机器被创建,当前领袖为:"+findMaster(stringList));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (Event.EventType.NodeChildrenChanged == event.getType()) {
            try {
                List<String> stringList =  zk.getChildren("/lx_test_node",true);
                //如果当前机器列表大于旧的机器列表说明增加了
                if(stringList.size()>oldSlaveList.size()){
                    System.out.println("一台机器被创建,当前领袖为:"+findMaster(stringList));
                    oldSlaveList = stringList;
                }
                else{
                    System.out.println("一台机器被删除,当前领袖为:"+findMaster(stringList));
                    oldSlaveList = stringList;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String findMaster(List<String> stringList){
        Integer min = Integer.MAX_VALUE;
        Integer index = 0;
        for(int i=0;i<stringList.size();i++){
            Integer num = Integer.parseInt(stringList.get(i).substring(8));
            if(num<=min){
                min = num;
                index = i;
            }
        }
        String master = stringList.get(index);
        LeaderElectionTest.callback(master);
        return master;
    }

}