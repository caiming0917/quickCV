package queue.jdk;

import java.util.concurrent.BlockingQueue;

public class BlockQueueDemo {
    private BlockingQueue<String> blockingQueue;

    public BlockQueueDemo(BlockingQueue<String> blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    public void testBlockQueueMethod() {
        // 所有子类都支持的正常操作
        blockingQueue.add("a");
        blockingQueue.add("b");
        System.out.println("peek:" + blockingQueue.peek());
        System.out.println("poll a:" + blockingQueue.poll());
        System.out.println("poll b:" + blockingQueue.poll());
        System.out.println("peek:" + blockingQueue.peek());

//        blockingQueue.put("a");
        blockingQueue.add("b");
        System.out.println("peek:" + blockingQueue.peek());
        System.out.println("poll a:" + blockingQueue.poll());
        System.out.println("poll b:" + blockingQueue.poll());
        System.out.println("peek:" + blockingQueue.peek());
    }
}
