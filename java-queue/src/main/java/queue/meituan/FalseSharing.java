package queue.meituan;

import sun.misc.Contended;

/**
 * 无法充分使用缓存行特性的现象，称为伪共享。
 * <p>
 * 对于伪共享，一般的解决方案是，增大数组元素的间隔使得由不同线程存取的元素位于不同的缓存行上，以空间换时间。
 */
public class FalseSharing implements Runnable {
    // 迭代
    public final static long ITERATIONS = 500L * 1000L * 100L;
    private int arrayIndex = 0;

    private static ValuePadding[] longs;

    public FalseSharing(final int arrayIndex) {
        this.arrayIndex = arrayIndex;
    }

    public static void main(final String[] args) throws Exception {
        for (int i = 1; i < 10; i++) {
            // GC
            System.gc();
            final long start = System.currentTimeMillis();
            runTest(i);
            System.out.println("Thread num " + i + " duration = " + (System.currentTimeMillis() - start));
        }

    }

    private static void runTest(int NUM_THREADS) throws InterruptedException {
        Thread[] threads = new Thread[NUM_THREADS];
        longs = new ValuePadding[NUM_THREADS];
        // n 个自定义数据
        for (int i = 0; i < longs.length; i++) {
            longs[i] = new ValuePadding();
        }
        // n 个线程拥有 n 个自定义数据
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new FalseSharing(i));
        }
        // 启动 10 个线程
        for (Thread t : threads) {
            t.start();
        }
        // 10 个线程加入到主线程之前
        for (Thread t : threads) {
            t.join();
        }
    }

    public void run() {
        // 循环若干次给同一个数组不同位置赋值
        long i = ITERATIONS + 1;
        while (0 != --i) {
            longs[arrayIndex].value = 0L;
        }
    }

    /**
     * 持有 16 个 long 数据，用来占据缓存行
     * 使得每个线程具有不同的缓冲行，实现充分的缓存利用
     */
    public final static class ValuePadding {
        protected long p1, p2, p3, p4, p5, p6, p7;
        // volatile 保障所有线程对修改可见
        protected volatile long value = 0L;
        protected long p9, p10, p11, p12, p13, p14;
        protected long p15;
    }

    // 使得多个线程使用同一个缓存行，其中一个线程更改数据行缓冲行失效——伪共享
    @Contended
    public final static class ValueNoPadding {
        // protected long p1, p2, p3, p4, p5, p6, p7;
        protected volatile long value = 0L;
        // protected long p9, p10, p11, p12, p13, p14, p15;
    }
}