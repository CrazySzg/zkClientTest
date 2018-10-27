package com.example;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

/**
 * 使用无权限信息的ZooKeeper会话访问含权限信息的数据节点
 *
 * @author Administrator
 * @create 2018-10-27 17:54
 */
public class AuthSample_Get {

    final static String PATH = "/zk-book-auth_test";
    public static void main(String[] args) throws Exception{
        ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181",
                5000,null);

        zooKeeper.addAuthInfo("digest","foo:true".getBytes());
        zooKeeper.create(PATH,"init".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL,
                CreateMode.EPHEMERAL);

        ZooKeeper zooKeeper2 = new ZooKeeper("127.0.0.1:2181",
                5000,null);

        zooKeeper2.getData(PATH,false,null);
        Thread.sleep(Integer.MAX_VALUE);
    }
}
