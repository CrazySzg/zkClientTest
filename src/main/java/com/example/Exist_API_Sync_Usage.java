package com.example;

import org.apache.zookeeper.*;

import java.util.concurrent.CountDownLatch;

/**
 * 同步检测节点是否存在
 *
 * @author Administrator
 * @create 2018-10-27 17:37
 */
public class Exist_API_Sync_Usage implements Watcher {
    private static CountDownLatch latch = new CountDownLatch(1);
    private static ZooKeeper zooKeeper = null;
    public static void main(String[] args) throws Exception {
        String path = "/zk-book4";
        zooKeeper = new ZooKeeper("127.0.0.1:2181",5000,
                new Exist_API_Sync_Usage());
        latch.await();

        zooKeeper.exists(path,true);

        zooKeeper.create(path,
                "".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT);

        zooKeeper.setData(path,"".getBytes(),-1);
        zooKeeper.create(path + "/c1",
                "".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL);

        zooKeeper.delete(path+"/c1",-1);
        zooKeeper.delete(path,-1);

        Thread.sleep(Integer.MAX_VALUE);
    }
    @Override
    public void process(WatchedEvent watchedEvent) {
        if(Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            try {
                if(Event.EventType.None == watchedEvent.getType() && null == watchedEvent.getPath()) {
                    latch.countDown();
                } else if(Event.EventType.NodeCreated == watchedEvent.getType()) {
                    System.out.println("Node(" + watchedEvent.getPath() + ")Created");
                    zooKeeper.exists(watchedEvent.getPath(),true);
                } else if(Event.EventType.NodeDeleted == watchedEvent.getType()) {
                    System.out.println("Node(" + watchedEvent.getPath() + ")Deleted");
                    zooKeeper.exists(watchedEvent.getPath(),true);
                } else if(Event.EventType.NodeDataChanged == watchedEvent.getType()) {
                    System.out.println("Node(" + watchedEvent.getPath() + ")DataChanged");
                    zooKeeper.exists(watchedEvent.getPath(),true);
                }
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
