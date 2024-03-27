package xyz.cofe.coll.im;

import xyz.cofe.coll.im.ImList;

import java.util.Iterator;

public interface Product<A> extends Iterable<A> {
    record Prod<A, B>(A left, B right) {}
    class ProdIterator<A, B> implements Iterator<Prod<A, B>> {
        private final Iterator<A> leftIterator;
        private A leftCurrent;

        private final Iterable<B> rightSource;
        private Iterator<B> rightIterator;

        private boolean finish;

        public ProdIterator(Iterable<A> leftSource, Iterable<B> rightSource) {
            if (leftSource == null) throw new IllegalArgumentException("leftSource==null");
            if (rightSource == null) throw new IllegalArgumentException("rightSource==null");

            finish = false;
            this.rightSource = rightSource;

            leftIterator = leftSource.iterator();
            if (!leftIterator.hasNext()) {
                finish = true;
                return;
            }

            leftCurrent = leftIterator.next();

            rightIterator = rightSource.iterator();
            if (!rightIterator.hasNext()) {
                finish = true;
            }
        }

        @Override
        public boolean hasNext() {
            return !finish;
        }

        @Override
        public Prod<A, B> next() {
            if (finish) return null;

            var rValue = rightIterator.next();
            var lValue = leftCurrent;

            if (!rightIterator.hasNext()) {
                if (leftIterator.hasNext()) {
                    leftCurrent = leftIterator.next();
                    rightIterator = rightSource.iterator();
                    if (!rightIterator.hasNext()) {
                        finish = true;
                    }
                } else {
                    finish = true;
                }
            }

            return new Prod<>(lValue, rValue);
        }
    }

    static <A, B> Iterable<Prod<A, B>> product(Iterable<A> left, Iterable<B> right) {
        if (left == null) throw new IllegalArgumentException("left==null");
        if (right == null) throw new IllegalArgumentException("right==null");
        return () -> new ProdIterator<>(left, right);
    }

    default <B> ImList<Prod<A,B>> product(Iterable<B> right){
        if( right==null ) throw new IllegalArgumentException("right==null");
        var result = ImList.<Prod<A,B>>of();
        for( var prod : product(this, right)){
            result = result.prepend(prod);
        }
        return result.reverse();
    }
}
