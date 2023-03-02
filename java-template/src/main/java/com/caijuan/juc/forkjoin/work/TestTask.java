package com.caijuan.juc.forkjoin.work;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class TestTask extends RecursiveTask<Integer> {
    private static final int GOURP_MAX_SIZE = 50;
    private static final int SIZE = 1000;
    private final List<Integer> datas;

    public TestTask(List<Integer> datas) {
        this.datas = datas;
    }

    @Override
    protected Integer compute() {
        if (datas.size() == 0) {
            return 0;
        }
        // 如果任务足够小就计算任务
        boolean canCompute = datas.size() < GOURP_MAX_SIZE;
        if (canCompute) {
            // 直接计算
            return datas.stream().reduce(Integer::sum).get();
        } else {
            int mid = datas.size() / 2;
            // 分裂成多任务进行计算
            TestTask leftTask = new TestTask(datas.subList(0, mid - 1));
            TestTask rightTask = new TestTask(datas.subList(mid, datas.size() - 1));
            // 执行子任务
            leftTask.fork();
            rightTask.fork();
            // 等待子任务执行完，并得到其结果
            Integer leftResult = leftTask.join();
            Integer rightResult = rightTask.join();
            // 合并子任务
            return leftResult + rightResult;
        }
    }
}
