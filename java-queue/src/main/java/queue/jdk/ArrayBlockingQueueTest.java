package queue.jdk;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ArrayBlockingQueueTest extends BlockQueueDemo {

    public ArrayBlockingQueueTest(BlockingQueue<String> blockingQueue) {
        super(blockingQueue);
    }

    public static void main(String[] args) {
        ArrayBlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(2);
        ArrayBlockingQueueTest blockingQueueTest = new ArrayBlockingQueueTest(blockingQueue);
        blockingQueueTest.testBlockQueueMethod();
    }
}
