package com.example.curator.async;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Administrator
 * @create 2018-10-29 23:00
 */
public class Create_Node_Background_Sample {
    static String path = "/zk-book/c1";
    static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString("127.0.0.1:2181")
            .sessionTimeoutMs(5000)
            .retryPolicy(new ExponentialBackoffRetry(1000,3))
            .build();
    static CountDownLatch latch = new CountDownLatch(2);
    static ExecutorService tp = Executors.newFixedThreadPool(2);


    public static void main(String[] args) throws Exception {
        client.start();
        System.out.println("Main Thread: " + Thread.currentThread().getName());

        client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL)
                .inBackground(new BackgroundCallback() {
                    @Override
                    public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
                        System.out.println("event[code: " + event.getResultCode() + ", type:" + event.getType() + "]");
                        System.out.println("Thread of processResult:  " + Thread.currentThread().getName());
                        latch.countDown();
                    }
                },tp).forPath(path,"init".getBytes());

        client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL)
                .inBackground(new BackgroundCallback() {
                    @Override
                    public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
                        System.out.println("event[code: " + event.getResultCode() + ", type:" + event.getType() + "]");
                        System.out.println("Thread of processResult:  " + Thread.currentThread().getName());
                        latch.countDown();
                    }
                }).forPath(path,"init".getBytes());

        latch.await();
        tp.shutdown();
    }
}
