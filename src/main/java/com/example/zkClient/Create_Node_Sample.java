package com.example.zkClient;

import org.I0Itec.zkclient.ZkClient;

/**
 * @author Administrator
 * @create 2018-10-27 21:34
 */
public class Create_Node_Sample {
    public static void main(String[] args) {
        ZkClient zkClient = new ZkClient("127.0.0.1:2181",500);
        String path = "/zk-book5/c1";
        zkClient.createPersistent(path,true);
    }
}
