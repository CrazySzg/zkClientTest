package com.example;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

/**
 * @author Administrator
 * @create 2018-10-27 11:52
 * @description Zookeeper 客户端和服务端会话的建立是一个异步的过程，也就是说在程序中构造
 * 方法会在处理客户端初始化工作后立即返回，在大多数情况下，此时并没有真正建立好一个可用的会话，在会话
 * 的生命周期中处于 "CONNECTING" 的状态。
 *
 */
public class ZooKeeper_Constructor_Usage_Simple implements Watcher {

    private static CountDownLatch latch = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {
        ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181",5000,
                new ZooKeeper_Constructor_Usage_Simple());
        System.out.println(zooKeeper.getState());

        latch.await();

        System.out.println("Zookeeper session established.");
    }


    /**
     * 当该会话真正创建完毕后，ZooKeeper服务端会向会话对应的客户端发送一个事件通知，已告知客户端
     * ，客户端只有字获取这个通知之后，才算真正建立了会话
     * @param watchedEvent
     */
    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println("Receive watched event: " + watchedEvent);
        if(watchedEvent.getState() == Event.KeeperState.SyncConnected) {
            latch.countDown();
        }
    }
}
