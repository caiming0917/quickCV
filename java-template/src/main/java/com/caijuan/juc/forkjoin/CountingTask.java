package com.caijuan.juc.forkjoin;

import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class CountingTask extends RecursiveTask<Integer> {

    private final TreeNode node;

    public CountingTask(TreeNode node) {
        this.node = node;
    }

    @Override
    protected Integer compute() {
        return node.value + node.children.stream()
                // 子节点集合包装成任务并返回
                .map(childNode -> new CountingTask(childNode).fork())
                // 执行
                .mapToInt(ForkJoinTask::join)
                .sum();
    }
}