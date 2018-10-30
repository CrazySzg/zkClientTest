package com.example.curator.recipes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * @author xuezhiqiang@sensetime.com
 * @create 2018-10-30 20:42
 */
public class Recipes_NoLock {

    public static void main(String[] args) {
        final CountDownLatch latch = new CountDownLatch(1);
        for(int i = 0;i < 10;i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        latch.await();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss | SSS");
                        String orderNo = dateFormat.format(new Date());
                        System.err.println("生成的订单号是：" + orderNo);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        latch.countDown();
    }
}
