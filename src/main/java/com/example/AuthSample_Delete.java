package com.example;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

/**
 * 删除节点接口的权限控制
 *
 * @author Administrator
 * @create 2018-10-27 18:00
 */
public class AuthSample_Delete {
    final static String PATH = "/zk-book-auth_test1";
    final static String PATH2 = "/zk-book-auth_test1/child";
    public static void main(String[] args) throws Exception{
        ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181",
                5000,null);

        zooKeeper.addAuthInfo("digest","foo:true".getBytes());
        zooKeeper.create(PATH,"init".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL,
                CreateMode.PERSISTENT);
        zooKeeper.create(PATH2,"init".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL,
                CreateMode.EPHEMERAL);

        try {
            ZooKeeper zooKeeper2 = new ZooKeeper("127.0.0.1:2181",
                    5000,null);
            zooKeeper2.delete(PATH2,-1);
        } catch (Exception e) {
            System.out.println("删除结点失败：" + e.getMessage());
        }

        ZooKeeper zooKeeper3 = new ZooKeeper("127.0.0.1:2181",
                5000,null);
        zooKeeper3.addAuthInfo("digest","foo:true".getBytes());
        zooKeeper3.delete(PATH2,-1);
        System.out.println("成功删除节点：" + PATH2);

        /**
         * 删除节点接口的权限控制比较特殊，当客户端对一个数据节点添加了权限信息后，对于删除
         * 操作而言，其作用范围是其子节点，也就是说，当我们对一个数据节点添加权限信息后，依然可以自由地
         * 删除这个节点，但是对于这个节点的子节点，就必须使用相应的权限信息才能够删除它
         */
        ZooKeeper zooKeeper4 = new ZooKeeper("127.0.0.1:2181",
                5000,null);
    //    zooKeeper4.addAuthInfo("digest","foo:true".getBytes());
        zooKeeper4.delete(PATH,-1);
        System.out.println("成功删除节点：" + PATH);
        Thread.sleep(Integer.MAX_VALUE);
    }
}
