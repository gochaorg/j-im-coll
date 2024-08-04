package xyz.cofe.coll.im.iter;

import java.util.Iterator;

public class EmptyIterator<A> implements Iterator<A> {
    public static <A> ExtIterable<A> iterate() {
        return EmptyIterator::new;
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public A next() {
        return null;
    }
}
