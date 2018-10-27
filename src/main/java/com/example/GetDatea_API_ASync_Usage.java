package com.example;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

/**
 * 异步获取节点数据内容
 *
 * @author Administrator
 * @create 2018-10-27 15:00
 */
public class GetDatea_API_ASync_Usage implements Watcher {

    private static CountDownLatch latch = new CountDownLatch(1);
    private static ZooKeeper zooKeeper = null;

    public static void main(String[] args) throws Exception {
        String path = "/zk-book4";
        zooKeeper = new ZooKeeper("127.0.0.1:2181",5000,
                new GetDatea_API_ASync_Usage());
        latch.await();
        zooKeeper.create(path,
                "123".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL);

        zooKeeper.getData(path,true,new IDataCallback(),null);

        zooKeeper.setData(path,"123".getBytes(),-1);


        Thread.sleep(Integer.MAX_VALUE);
    }
    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println("Reveive watched event : " + watchedEvent);
        if(Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            if(Event.EventType.None == watchedEvent.getType() && null ==  watchedEvent.getPath()) {
                latch.countDown();
            } else if(watchedEvent.getType() == Event.EventType.NodeDataChanged) {
                try {
                    zooKeeper.getData(watchedEvent.getPath(),true,new IDataCallback(),null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

class IDataCallback implements AsyncCallback.DataCallback {

    @Override
    public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
        System.out.println(rc + ", " + path + ", " + new String(data));
        System.out.println(stat.getCzxid() + "," + stat.getMzxid() + "," +
                stat.getVersion());
    }
}
