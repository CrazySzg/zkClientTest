package com.example;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 异步获取子节点列表
 *
 * @author Administrator
 * @create 2018-10-27 14:41
 */
public class ZooKeeper_Get_Children_API_ASync_Usage implements Watcher {

    private static CountDownLatch latch = new CountDownLatch(1);
    private static ZooKeeper zooKeeper = null;

    public static void main(String[] args) throws Exception {
        String path = "/zk-book3";
        zooKeeper = new ZooKeeper("127.0.0.1:2181",5000,
                new ZooKeeper_Get_Children_API_ASync_Usage());
        latch.await();
        zooKeeper.create(path,
                "".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT);

        zooKeeper.create(path + "/c1",
                "".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT);

        zooKeeper.getChildren(path,true,new IChildren2Callback(),null);

        zooKeeper.create(path + "/c2",
                "".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL);

        Thread.sleep(Integer.MAX_VALUE);
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println("Reveive watched event : " + watchedEvent);
        if(Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            if(Event.EventType.None == watchedEvent.getType() && null ==  watchedEvent.getPath()) {
                latch.countDown();
            } else if(watchedEvent.getType() == Event.EventType.NodeChildrenChanged) {
                try {
                    System.out.println("ReGetChild: " + zooKeeper.getChildren(watchedEvent.getPath(),true));
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

class IChildren2Callback implements AsyncCallback.Children2Callback {

    @Override
    public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {
        System.out.println("Get Children znode result: [ response code:" + rc +
        ", param path: " + path + ", ctx: " + ctx + ", children list: " + children +
        ", stat: " + stat);
    }
}
