package com.example.zkClient;

import org.I0Itec.zkclient.ZkClient;

/**
 * @author Administrator
 * @create 2018-10-27 21:30
 */
public class Create_Session_Sample {

    public static void main(String[] args) {
        ZkClient zkClient = new ZkClient("127.0.0.1:2181",500);
        System.out.println("ZooKeeper session established");
    }
}
