package queue.disruptor.meituan;

import com.lmax.disruptor.EventFactory;

public class ElementEventFactory implements EventFactory<Element> {

    @Override
    public Element newInstance() {
        return new Element();
    }
}
