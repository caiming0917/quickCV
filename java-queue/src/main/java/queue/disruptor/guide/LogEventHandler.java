package queue.disruptor.guide;

import com.lmax.disruptor.EventHandler;

/**
 * event ：待消费/处理的事件
 * sequence ：正在处理的事件在环形数组（RingBuffer）中的位置
 * endOfBatch : 表示这是否是来自环形数组（RingBuffer）中一个批次的最后一个事件（批量处理事件）
 */
public class LogEventHandler implements EventHandler<LogEvent> {
    @Override
    public void onEvent(LogEvent logEvent, long sequence, boolean endOfBatch) throws Exception {
        System.out.println(logEvent.getMessage());
    }
}