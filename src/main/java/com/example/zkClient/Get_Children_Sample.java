package com.example.zkClient;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.List;

/**
 * @author Administrator
 * @create 2018-10-28 22:49
 * zkClient中没有watcher的概念，但是引入了Listener，客户端可以通过注册相关的事件监听来实现对
 * ZooKeeper服务端事件的订阅
 */
public class Get_Children_Sample {

    public static void main(String[] args) throws InterruptedException {
        String path = "/zk-book6";
        ZkClient zkClient = new ZkClient("127.0.0.1:2181",5000);
        zkClient.subscribeChildChanges(path, new IZkChildListener() {
            @Override
            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                System.out.println(parentPath + " 's child changed,currentChilds:" + currentChilds);
            }
        });
        zkClient.createPersistent(path);
        Thread.sleep(1000);
        System.out.println(zkClient.getChildren(path));
        Thread.sleep(1000);
        zkClient.createPersistent(path + "/c1");
        Thread.sleep(1000);
        zkClient.delete(path + "/c1");
        Thread.sleep(1000);
        zkClient.delete(path);
        Thread.sleep(Integer.MAX_VALUE);
    }
}
