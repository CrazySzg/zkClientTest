package com.example;

import org.apache.zookeeper.*;

import java.util.concurrent.CountDownLatch;

/**
 * @author Administrator
 * @create 2018-10-27 14:03
 * @desc 同步创建节点
 */
public class ZooKeeper_Create_API_Sync_Usage implements Watcher {
    private static CountDownLatch latch = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {
        ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181",
                5000,
                new ZooKeeper_Create_API_Sync_Usage());
        latch.await();

        String path1 = zooKeeper.create("/zk-test-ephemeral-",
                "".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL);

        System.out.println("Success create znode:" + path1);

        String path2 = zooKeeper.create("/zk-test-ephemeral-",
                "".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL_SEQUENTIAL);

        System.out.println("Success create znode: " + path2);
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        if(Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            latch.countDown();
        }
    }
}
