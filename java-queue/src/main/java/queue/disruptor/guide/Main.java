package queue.disruptor.guide;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static void main(String[] args) {
        //获取 Disruptor 对象
        Disruptor<LogEvent> disruptor = getLogEventDisruptor();
        //绑定处理事件的Handler对象
        disruptor.handleEventsWith(new LogEventHandler());
        //启动 Disruptor
        disruptor.start();
        //获取保存事件的环形数组（RingBuffer）
        RingBuffer<LogEvent> ringBuffer = disruptor.getRingBuffer();
        //发布 10w 个事件
        for (int i = 1; i <= 100000; i++) {
            // 通过调用 RingBuffer 的 next() 方法获取下一个空闲事件槽的序号
            long sequence = ringBuffer.next();
            try {
                LogEvent logEvent = ringBuffer.get(sequence);
                // 初始化 Event，对其赋值
                logEvent.setMessage(String.format("这是第%d条日志消息", i));
            } finally {
                // 发布事件
                ringBuffer.publish(sequence);
            }
        }
        // 关闭 Disruptor
        disruptor.shutdown();
    }

    private static Disruptor<LogEvent> getLogEventDisruptor() {
        // 创建 LogEvent 的工厂
        LogEventFactory logEventFactory = new LogEventFactory();
        // Disruptor 的 RingBuffer 缓存大小
        int bufferSize = 1024 * 1024;
        // 生产者的线程工厂
        ThreadFactory threadFactory = new ThreadFactory() {
            final AtomicInteger threadNum = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "LogEventThread" + " [#" + threadNum.incrementAndGet() + "]");
            }
        };


        //实例化 Disruptor
        return new Disruptor<>(
                logEventFactory,
                bufferSize,
                threadFactory,
                // 单生产者
                ProducerType.SINGLE,
                // 阻塞等待策略
                new BlockingWaitStrategy());
    }

}
