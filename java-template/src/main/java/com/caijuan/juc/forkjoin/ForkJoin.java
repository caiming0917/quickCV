package com.caijuan.juc.forkjoin;

import java.util.concurrent.ForkJoinPool;

/**
 * @author cai juan
 * @date 2023/2/12 14:23
 */
public class ForkJoin {
    public static void main(String[] args) {
        TreeNode tree = new TreeNode(5,
                new TreeNode(3), new TreeNode(2,
                new TreeNode(2), new TreeNode(8)));

        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        int sum = forkJoinPool.invoke(new CountingTask(tree));
        System.out.println("sum =>" + sum);
    }
}
