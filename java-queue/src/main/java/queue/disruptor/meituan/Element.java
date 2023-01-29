package queue.disruptor.meituan;

public class Element {

    private long value;

    public long get() {
        return value;
    }

    public void set(long value) {
        this.value = value;
    }

}