package queue.disruptor.meituan;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.ThreadFactory;

public class Main {
    // 生产者的线程工厂
    static ThreadFactory threadFactory = r -> new Thread(r, "simpleThread");

    // 阻塞策略
    static BlockingWaitStrategy strategy = new BlockingWaitStrategy();

    // 指定RingBuffer的大小
    static int bufferSize = 16;

    // RingBuffer生产工厂,初始化RingBuffer的时候使用
    static EventFactory<Element> factory = new ElementEventFactory();

    // 创建disruptor，采用单生产者模式
    static Disruptor<Element> disruptor =
            new Disruptor<>(factory, bufferSize, threadFactory, ProducerType.SINGLE, strategy);

    // 处理Event的handler
    static ElementEventHandler handler = new ElementEventHandler();

    public static void main(String[] args) throws InterruptedException {
        // 设置EventHandler
        disruptor.handleEventsWith(handler);

        // 启动disruptor的线程
        disruptor.start();

        RingBuffer<Element> ringBuffer = disruptor.getRingBuffer();

        long count = 1000 * 1000 * 1000L;

        for (long l = 0; l < count; l++) {
            // 获取下一个可用位置的下标
            long sequence = ringBuffer.next();
            try {
                // 返回可用位置的元素
                Element event = ringBuffer.get(sequence);
                // 设置该位置元素的值
                event.set(l);
            } finally {
                ringBuffer.publish(sequence);
            }
//            Thread.sleep(10);
        }
    }
}
