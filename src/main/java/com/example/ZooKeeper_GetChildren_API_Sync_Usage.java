package com.example;

import org.apache.zookeeper.*;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 同步获取结点列表
 *
 * @author Administrator
 * @create 2018-10-27 14:23
 */
public class ZooKeeper_GetChildren_API_Sync_Usage implements Watcher {
    private static CountDownLatch latch = new CountDownLatch(1);
    private static ZooKeeper zooKeeper = null;
    public static void main(String[] args) throws Exception {
        String path = "/zk-book";
        zooKeeper = new ZooKeeper("127.0.0.1:2181",5000,
                new ZooKeeper_GetChildren_API_Sync_Usage());
        latch.await();

        zooKeeper.create(path,
                "".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT);
        zooKeeper.create(path + "/c1",
                "".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL);

        List<String> childrenList = zooKeeper.getChildren(path,true);
        System.out.println(childrenList);

        //有子节点创建，ZooKeeper服务端就会向客户端发出一个"子节点变更"的事件通知
        zooKeeper.create(path + "/c2",
                "".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL);

        Thread.sleep(Integer.MAX_VALUE);
    }


    /**
     * ZooKeeper服务端在向客户端发送Watcher "NodeChildrenChanged"事件通知的时候，仅仅只会发出一个通知，
     * 而不会把节点的变化情况发送给客户端，需要客户端自己重新获取。
     * 另外，由于Watcher通知是一次性的，即一旦触发一次通知后，该Watcher就失效了，
     * 因此客户端需要反复注册Watcher
     * @param watchedEvent
     */
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
