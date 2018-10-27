package com.example;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

/**
 * @author Administrator
 * @create 2018-10-27 13:54
 * @desc 使用sessionId和sessionPwd复用会话
 */
public class ZooKeeper_Constructor_Usage_With_SID_PASSWD implements Watcher {

    private static CountDownLatch latch = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {
        ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181",5000,
                new ZooKeeper_Constructor_Usage_With_SID_PASSWD());

        latch.await();
        long sessionId = zooKeeper.getSessionId();
        byte[] passwd = zooKeeper.getSessionPasswd();

        zooKeeper = new ZooKeeper("127.0.0.1:2181",
                5000,
                new ZooKeeper_Constructor_Usage_With_SID_PASSWD(),
                1l,
                "test".getBytes());

        zooKeeper = new ZooKeeper("127.0.0.1:2181",
                5000,
                new ZooKeeper_Constructor_Usage_With_SID_PASSWD(),
                sessionId,
                passwd);

        Thread.sleep(Integer.MAX_VALUE);

    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println("Reveive watched event : " + watchedEvent);
        if(Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            latch.countDown();
        }
    }
}
