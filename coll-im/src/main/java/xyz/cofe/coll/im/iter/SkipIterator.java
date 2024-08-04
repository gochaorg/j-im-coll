package xyz.cofe.coll.im.iter;

import java.util.Iterator;

public class SkipIterator<A> implements Iterator<A> {
    private final Iterator<A> iterator;
    private long skipCount;

    public SkipIterator(Iterator<A> iterator, long skipCount) {
        if( iterator==null ) throw new IllegalArgumentException("iterator==null");
        this.iterator = iterator;
        this.skipCount = skipCount;
    }

    private boolean skipped = false;
    private void skip(){
        if( skipped ) return;

        var cnt = skipCount;
        while (cnt>0){
            if( !iterator.hasNext() )break;
            iterator.next();
            cnt--;
        }
        skipped = true;
    }

    @Override
    public boolean hasNext() {
        skip();
        return iterator.hasNext();
    }

    @Override
    public A next() {
        skip();
        return iterator.next();
    }
}
