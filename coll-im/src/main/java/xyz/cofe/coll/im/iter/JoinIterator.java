package xyz.cofe.coll.im.iter;

import java.util.Iterator;

public class JoinIterator<A> implements Iterator<A> {
    private final Iterator<A> left;
    private final Iterator<A> right;

    public JoinIterator(Iterator<A> left, Iterator<A> right) {
        if( left==null ) throw new IllegalArgumentException("left==null");
        if( right==null ) throw new IllegalArgumentException("right==null");
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean hasNext() {
        if(left.hasNext())return true;
        if(right.hasNext())return true;
        return false;
    }

    @Override
    public A next() {
        if(left.hasNext()) return left.next();
        if(right.hasNext()) return right.next();
        return null;
    }
}
