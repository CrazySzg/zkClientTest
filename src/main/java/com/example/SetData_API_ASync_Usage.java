package com.example;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

/**
 * 异步更新节点数据内容
 *
 * @author Administrator
 * @create 2018-10-27 17:32
 */
public class SetData_API_ASync_Usage implements Watcher {

    private static CountDownLatch latch = new CountDownLatch(1);
    private static ZooKeeper zooKeeper = null;
    public static void main(String[] args) throws Exception {
        String path = "/zk-book4";
        zooKeeper = new ZooKeeper("127.0.0.1:2181",5000,
                new SetData_API_ASync_Usage());
        latch.await();

        zooKeeper.create(path,
                "123".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL);

        zooKeeper.setData(path,"456".getBytes(),-1,new IStatCallback(),null);

        Thread.sleep(Integer.MAX_VALUE);
    }


    @Override
    public void process(WatchedEvent watchedEvent) {
        if(Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            latch.countDown();
        }
    }
}

class IStatCallback implements AsyncCallback.StatCallback {
    @Override
    public void processResult(int rc, String path, Object ctx, Stat stat) {
        if(rc == 0) {
            System.out.println("SUCCESS");
        }
    }
}
