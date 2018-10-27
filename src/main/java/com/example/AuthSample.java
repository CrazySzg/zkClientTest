package com.example;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

/**
 * 权限控制
 *
 * @author Administrator
 * @create 2018-10-27 17:51
 */
public class AuthSample {

    /**
     * 权限控制分为world，auth,digest,ip和super
     */
    final static String PATH = "/zk-book-auth_test";
    public static void main(String[] args) throws Exception {
        ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181",
                5000,null);

        zooKeeper.addAuthInfo("digest","foo:true".getBytes());
        zooKeeper.create(PATH,"init".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL,
                CreateMode.EPHEMERAL);

        Thread.sleep(Integer.MAX_VALUE);
    }
}
