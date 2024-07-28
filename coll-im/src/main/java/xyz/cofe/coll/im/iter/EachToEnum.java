package xyz.cofe.coll.im.iter;

import xyz.cofe.coll.im.Enumerated;

import java.util.Iterator;

public interface EachToEnum<A> extends Iterable<A> {
    public static class EnumerateIterator<A> implements Iterator<Enumerated<A>> {
        private final Iterator<A> iterator;
        private long index;

        public EnumerateIterator(Iterator<A> iterator) {
            if( iterator==null ) throw new IllegalArgumentException("iterator==null");
            this.iterator = iterator;
            this.index = 0;
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public Enumerated<A> next() {
            if(iterator.hasNext()){
                A value = iterator.next();
                long idx = index;
                index++;
                return new Enumerated<>(idx, value);
            }
            return null;
        }
    }

    default Iterable<Enumerated<A>> enumerate(){
        var self = this;
        return () -> new EnumerateIterator<>( self.iterator() );
    }
}
