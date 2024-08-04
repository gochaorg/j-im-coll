package xyz.cofe.coll.im.iter;

import java.util.Iterator;

public class SingleIterator<A> implements Iterator<A> {
    public static <A> ExtIterable<A> iterate(A value){
        return () -> new SingleIterator<>(value);
    }

    private final A value;
    private boolean consumed;

    public SingleIterator( A value ){
        this.value = value;
        this.consumed = false;
    }

    @Override
    public boolean hasNext() {
        return !consumed;
    }

    @Override
    public A next() {
        if( consumed )return null;
        consumed = true;
        return value;
    }
}
