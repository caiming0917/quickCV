package queue.disruptor.meituan;


import com.lmax.disruptor.EventHandler;

public class ElementEventHandler implements EventHandler<Element> {

    @Override
    public void onEvent(Element element, long sequence, boolean endOfBatch) throws Exception {
        System.out.println("Element: " + element.get());
    }
}
