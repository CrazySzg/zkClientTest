package com.example;

import org.apache.zookeeper.*;

import java.util.concurrent.CountDownLatch;

/**
 * @author Administrator
 * @create 2018-10-27 14:13
 * @desc 异步增加节点
 */
public class ZooKeeper_Create_API_ASync_Usage implements Watcher {
    private static CountDownLatch latch = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {
        ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181",5000,
                new ZooKeeper_Create_API_ASync_Usage());
        latch.await();

        zooKeeper.create("/zk-test-ephemeral-","".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL,
                new IStringCallback(),"I am context");

        zooKeeper.create("/zk-test-ephemeral-","".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL,
                new IStringCallback(),"I am context");

        zooKeeper.create("/zk-test-ephemeral-","".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL,
                new IStringCallback(),"I am context");

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
class IStringCallback implements AsyncCallback.StringCallback {

    /**
     *
     * @param rc 服务端响应码：0成功，-4客户端和服务端连接断开，-110节点已存在，-112会话过期
     * @param path
     * @param ctx
     * @param name
     */
    @Override
    public void processResult(int rc, String path, Object ctx, String name) {
        System.out.println("Create path result: [" + rc + ", " + path +
        "," + ctx + ", real path name: " + name);
    }
}
