package com.example;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

/**
 * 同步API更新节点数据内容
 *
 * @author Administrator
 * @create 2018-10-27 17:23
 */
public class SetData_API_Sync_Usage implements Watcher {

    private static CountDownLatch latch = new CountDownLatch(1);
    private static ZooKeeper zooKeeper = null;
    public static void main(String[] args) throws Exception {
        String path = "/zk-book4";
        zooKeeper = new ZooKeeper("127.0.0.1:2181",5000,
                new SetData_API_Sync_Usage());
        latch.await();

        zooKeeper.create(path,
                "123".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL);

        zooKeeper.getData(path,true,null);

        Stat stat = zooKeeper.setData(path, "456".getBytes(), -1);
        System.out.println(stat.getCzxid() + "," + stat.getMzxid() + "," +
                stat.getVersion());

        Stat stat2 = zooKeeper.setData(path, "456".getBytes(), stat.getVersion());
        System.out.println(stat2.getCzxid() + "," + stat2.getMzxid() + "," +
                stat2.getVersion());

        try {
            zooKeeper.setData(path,"456".getBytes(),stat.getVersion());
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Thread.sleep(Integer.MAX_VALUE);
    }
    @Override
    public void process(WatchedEvent watchedEvent) {
        if(Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            latch.countDown();
        }
    }
}
