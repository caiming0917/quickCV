package com.caijuan.juc.limiter.v1;

public class CounterSildeWindowLimiter {

    private int windowSize; //窗口大小，毫秒为单位
    private int limit;//窗口内限流大小
    private int splitNum;//切分小窗口的数目大小
    private int[] counters;//每个小窗口的计数数组
    private int index;//当前小窗口计数器的索引
    private long startTime;//窗口开始时间

    private CounterSildeWindowLimiter() {
    }

    public CounterSildeWindowLimiter(int windowSize, int limit, int splitNum) {
        this.limit = limit;
        this.windowSize = windowSize;
        this.splitNum = splitNum;
        counters = new int[splitNum];
        index = 0;
        startTime = System.currentTimeMillis();
    }

    //请求到达后先调用本方法，若返回true，则请求通过，否则限流
    public synchronized boolean tryAcquire() {
        long curTime = System.currentTimeMillis();
        //计算滑动小窗口的数量(需要往前移动的小窗口个数 = 超出的时间区间 / 小窗口单位大小)
        long windowsNum = Math.max(curTime - startTime - windowSize, 0) / (windowSize / splitNum);
        slideWindow(windowsNum);//滑动窗口
        int count = 0;
        for (int i = 0; i < splitNum; i++) {
            count += counters[i];
        }
        if (count >= limit) {
            return false;
        } else {
            counters[index]++;
            return true;
        }
    }

    private synchronized void slideWindow(long windowsNum) {
        if (windowsNum == 0)
            return;
        // 需要滑动的窗口个数(最多 windowsNum 个)
        long slideNum = Math.min(windowsNum, splitNum);
//        System.out.println("index = " + index + ", counters[" + index + "] = " + counters[index]);
        // 更新窗口通过的流量：滑进来的窗口通过的个数为0
        for (int i = 0; i < slideNum; i++) {
            index = (index + 1) % splitNum;
            counters[index] = 0;
        }
        //更新滑动窗口时间 (windowSize / splitNum) => 每个窗口时间大小
        startTime = startTime + windowsNum * (windowSize / splitNum);
//        System.out.println("startTime = " + startTime + ", windowsNum = " + windowsNum +
//                ", slideNum = " + slideNum + ", splitNum = " + splitNum);
    }

    //测试
    public static void main(String[] args) throws InterruptedException {
        //每秒20个请求
        int limit = 20;
        CounterSildeWindowLimiter counterSildeWindowLimiter = new CounterSildeWindowLimiter(1000, limit, 10);
        int count = 0;

        Thread.sleep(3000);
        //计数器滑动窗口算法模拟100组间隔30ms的50次请求
        System.out.println("计数器滑动窗口算法测试开始");
        System.out.println("开始模拟100组间隔150ms的50次请求");
        int faliCount = 0;
        for (int j = 0; j < 100; j++) {
            count = 0;
            for (int i = 0; i < 50; i++) {
                if (counterSildeWindowLimiter.tryAcquire()) {
                    count++;
                }
            }
            Thread.sleep(150);
            //模拟50次请求，看多少能通过
            for (int i = 0; i < 50; i++) {
                if (counterSildeWindowLimiter.tryAcquire()) {
                    count++;
                }
            }
            if (count > limit) {
                System.out.println("时间窗口内放过的请求超过阈值，放过的请求数" + count + ",限流：" + limit);
                faliCount++;
            }
            Thread.sleep((int) (Math.random() * 100));
        }
        System.out.println("计数器滑动窗口算法测试结束，100组间隔150ms的50次请求模拟完成，限流失败组数：" + faliCount);
        System.out.println("===========================================================================================");


        //计数器固定窗口算法模拟100组间隔30ms的50次请求
        System.out.println("计数器固定窗口算法测试开始");
        //模拟100组间隔30ms的50次请求
        CounterLimiter counterLimiter = new CounterLimiter(1000, limit);
        System.out.println("开始模拟100组间隔150ms的50次请求");
        faliCount = 0;
        for (int j = 0; j < 100; j++) {
            count = 0;
            for (int i = 0; i < 50; i++) {
                if (counterLimiter.tryAcquire()) {
                    count++;
                }
            }
            Thread.sleep(150);
            //模拟50次请求，看多少能通过
            for (int i = 0; i < 50; i++) {
                if (counterLimiter.tryAcquire()) {
                    count++;
                }
            }
            if (count > limit) {
                System.out.println("时间窗口内放过的请求超过阈值，放过的请求数" + count + ",限流：" + limit);
                faliCount++;
            }
            Thread.sleep((int) (Math.random() * 100));
        }
        System.out.println("计数器滑动窗口算法测试结束，100组间隔150ms的50次请求模拟完成，限流失败组数：" + faliCount);
    }
}