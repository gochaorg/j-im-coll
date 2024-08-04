package xyz.cofe.coll.im.iter;

import java.util.Iterator;

public class ArrayIterator<A> implements Iterator<A> {
    public static <A> ExtIterable<A> iterate(A[] values){
        return ()->new ArrayIterator<>(values);
    }

    private final A[] values;
    private int ptr = 0;

    public ArrayIterator(A[] values, int ptr) {
        if( values==null ) throw new IllegalArgumentException("values==null");
        if( ptr<0 ) throw new IllegalArgumentException("ptr<0");
        this.values = values;
        this.ptr = ptr;
    }

    public ArrayIterator(A[] values) {
        if( values==null ) throw new IllegalArgumentException("values==null");
        this.values = values;
        this.ptr = 0;
    }

    @Override
    public boolean hasNext() {
        return ptr < values.length;
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public A next() {
        if( ptr >= values.length )return null;
        var r = values[ptr];
        ptr++;
        return r;
    }
}
