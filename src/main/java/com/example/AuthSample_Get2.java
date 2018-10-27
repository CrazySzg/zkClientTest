package com.example;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

/**
 * 使用错误权限信息的ZooKeeper会话访问权限信息的数据节点
 *
 * @author Administrator
 * @create 2018-10-27 17:57
 */
public class AuthSample_Get2 {

    final static String PATH = "/zk-book-auth_test";
    public static void main(String[] args) throws Exception{
        ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181",
                5000,null);

        zooKeeper.addAuthInfo("digest","foo:true".getBytes());
        zooKeeper.create(PATH,"init".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL,
                CreateMode.EPHEMERAL);

        ZooKeeper zooKeeper2 = new ZooKeeper("127.0.0.1:2181",
                5000,null);
        zooKeeper2.addAuthInfo("digest","foo:true".getBytes());
        System.out.println(zooKeeper2.getData(PATH,false,null));

        ZooKeeper zooKeeper3 = new ZooKeeper("127.0.0.1:2181",
                5000,null);
        zooKeeper3.addAuthInfo("digest","foo:false".getBytes());
        System.out.println(zooKeeper3.getData(PATH,false,null));

        Thread.sleep(Integer.MAX_VALUE);
    }
}
