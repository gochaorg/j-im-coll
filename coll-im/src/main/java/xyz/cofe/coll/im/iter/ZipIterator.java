package xyz.cofe.coll.im.iter;

import xyz.cofe.coll.im.Tuple2;

import java.util.Iterator;

/**
 * Соединение двух последовательностей в одну из пар
 *
 * @param <A> Первый элемент типа
 * @param <B> Второй элемент типа
 */
public class ZipIterator<A, B> implements Iterator<ZipIterator.Zipped<A, B>> {

    /**
     * Пара значений
     *
     * @param left  первый элемент
     * @param right второй элемент
     * @param <A>   первый элемент 1
     * @param <B>   второй элемент 2
     */
    public record Zipped<A, B>(A left, B right) implements Tuple2<A, B> {
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

    /**
     * Соединение двух последовательностей в одну из пар
     *
     * @param leftIter  первая последовательность
     * @param rightIter вторая последовательность
     */
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
        if (leftIter.hasNext() && rightIter.hasNext()) {
            return new Zipped<>(leftIter.next(), rightIter.next());
        }
        return null;
    }
}
