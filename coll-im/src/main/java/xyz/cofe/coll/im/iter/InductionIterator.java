package xyz.cofe.coll.im.iter;

import xyz.cofe.coll.im.Fn1;
import xyz.cofe.coll.im.Fn2;
import xyz.cofe.coll.im.Fn3;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class InductionIterator<A> implements Iterator<A> {
    public static <A> ExtIterable<A> iterable( List<A> initialValues, Fn1<List<A>,A> nextGenerator ){
        if( initialValues==null ) throw new IllegalArgumentException("initialValues==null");
        if( nextGenerator==null ) throw new IllegalArgumentException("nextGenerator==null");
        return () -> new InductionIterator<>(initialValues,0,nextGenerator);
    }

    public static <A> ExtIterable<A> iterate( A initial, Fn1<A,A> nextGenerator ){
        if( nextGenerator==null ) throw new IllegalArgumentException("nextGenerator==null");
        return iterable(
            new ArrayList<>(List.of(initial)),
            values -> nextGenerator.apply(values.get(0))
        );
    }

    public static <A> ExtIterable<A> iterate( A initial0, A initial1, Fn2<A,A,A> nextGenerator ){
        if( nextGenerator==null ) throw new IllegalArgumentException("nextGenerator==null");
        return iterable(
            new ArrayList<>(List.of(initial0, initial1)),
            values -> nextGenerator.apply(values.get(0), values.get(1))
        );
    }

    public static <A> ExtIterable<A> iterate( A initial0, A initial1, A initial2, Fn3<A,A,A,A> nextGenerator ){
        if( nextGenerator==null ) throw new IllegalArgumentException("nextGenerator==null");
        return iterable(
            new ArrayList<>(List.of(initial0, initial1, initial2)),
            values -> nextGenerator.apply(values.get(0), values.get(1), values.get(2))
        );
    }

    private final List<A> values;
    private int ptr;
    private final Fn1<List<A>, A> next;

    public InductionIterator(List<A> values, int ptr, Fn1<List<A>, A> next) {
        if (values == null) throw new IllegalArgumentException("values==null");
        if (next == null) throw new IllegalArgumentException("next==null");
        if (ptr < 0) throw new IllegalArgumentException("ptr<0");

        this.values = values;
        this.ptr = ptr;
        this.next = next;
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public A next() {
        if (ptr < values.size()) {
            var a = values.get(ptr);
            ptr++;
            return a;
        }else{
            var nextValue = next.apply(values);
            values.add(nextValue);
            values.remove(0);
            return nextValue;
        }
    }
}
