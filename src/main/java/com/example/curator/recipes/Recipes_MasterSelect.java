package com.example.curator.recipes;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author Administrator
 * @create 2018-10-29 23:32
 */
public class Recipes_MasterSelect {

    static String master_path = "/curator_recipes_master_path";
    static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString("127.0.0.1:2181")
            .sessionTimeoutMs(5000)
            .retryPolicy(new ExponentialBackoffRetry(1000,3))
            .build();

    public static void main(String[] args) throws Exception {
        client.start();
        LeaderSelector leaderSelector = new LeaderSelector(client,
                master_path,
                new LeaderSelectorListenerAdapter() {

                    //curator会在竞争到Master后自动调用该方法，执行完后，会立即释放Master权利，然后重新开始新一轮的Master选举
                    @Override
                    public void takeLeadership(CuratorFramework client) throws Exception {
                        System.out.println("成为master角色");
                        Thread.sleep(3000);
                        System.out.println("完成master角色，释放Master权利");
                    }
                });
        leaderSelector.autoRequeue();
        leaderSelector.start();
        Thread.sleep(Integer.MAX_VALUE);
    }
}
