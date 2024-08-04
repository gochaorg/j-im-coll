package xyz.cofe.coll.im.iter;

import java.util.Iterator;

public class TakeIterator<A> implements Iterator<A> {
    private final Iterator<A> iterator;
    private long count;

    public TakeIterator(Iterator<A> iterator, long count) {
        this.iterator = iterator;
        this.count = count;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext() && count > 0;
    }

    @Override
    public A next() {
        if (!iterator.hasNext()) return null;
        if (count <= 0) return null;
        var r = iterator.next();
        count--;
        return r;
    }
}
