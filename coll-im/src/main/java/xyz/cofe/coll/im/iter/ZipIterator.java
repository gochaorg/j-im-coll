package xyz.cofe.coll.im.iter;

import xyz.cofe.coll.im.Tuple2;

import java.util.Iterator;

public class ZipIterator<A,B> implements Iterator<ZipIterator.Zipped<A,B>> {
    public record Zipped<A,B>(A left, B right) implements Tuple2<A,B> {
        @Override
        public A _1() {
            return left;
        }

        @Override
        public B _2() {
            return right;
        }
    }

    private final Iterator<A> leftIter;
    private final Iterator<B> rightIter;

    public ZipIterator(Iterator<A> leftIter, Iterator<B> rightIter) {
        this.leftIter = leftIter;
        this.rightIter = rightIter;
    }

    @Override
    public boolean hasNext() {
        return leftIter.hasNext() && rightIter.hasNext();
    }

    @Override
    public Zipped<A, B> next() {
        if(leftIter.hasNext() && rightIter.hasNext()){
            return new Zipped<>(leftIter.next(), rightIter.next());
        }
        return null;
    }
}
