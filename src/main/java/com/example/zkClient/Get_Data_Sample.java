package com.example.zkClient;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

/**
 * @author Administrator
 * @create 2018-10-28 23:04
 */
public class Get_Data_Sample {

    public static void main(String[] args) throws InterruptedException {
        String path = "/zk-book6";
        ZkClient zkClient = new ZkClient("127.0.0.1:2181",5000);
        zkClient.createEphemeral(path,"123");
        zkClient.subscribeDataChanges(path, new IZkDataListener() {
            @Override
            public void handleDataChange(String path, Object data) throws Exception {
                System.out.println("Node" + path + " changed,new data: " + data);
            }

            @Override
            public void handleDataDeleted(String path) throws Exception {
                System.out.println("Node " + path + " deleted.");
            }
        });

        System.out.println(zkClient.readData(path));
        zkClient.writeData(path,"456");
        Thread.sleep(1000);
        zkClient.delete(path);
        Thread.sleep(Integer.MAX_VALUE);
    }
}
