package xyz.cofe.coll.im;

import java.io.Serializable;
import java.util.Objects;

/**
 * Кортеж из одного элемента
 * @param <A> Тип элемента
 */
public sealed interface Tuple1<A> extends Serializable {
    /**
     * Возвращает элемент кортежа
     * @return элемент
     */
    A _1();

    default <RES> RES map( Fn1<A,RES> f ) {
        if( f==null )throw new IllegalArgumentException("f==null");
        return f.apply(_1());
    }

    /**
     * Добавление элемента
     * @param b элемент
     * @return кортеж
     * @param <B> тип элемента
     */
    default <B> Tuple2<A,B> append(B b) {
        return Tuple2.of(_1(),b);
    }

    final class Tuple1Impl<A> implements Tuple1<A> {
        public final A a;

        public Tuple1Impl(A a){
            this.a = a;
        }

        @Override
        public A _1() {
            return a;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Tuple1Impl<?> tuple1 = (Tuple1Impl<?>) o;
            return Objects.equals(a, tuple1.a);
        }

        @Override
        public int hashCode() {
            return Objects.hash(a);
        }
    }

    /**
     * Создание кортежа
     * @param a 1ый элемент
     * @return кортеж
     * @param <A> тип 1‑го элемента
     */
    static <A> Tuple1<A> of( A a ) {
        return new Tuple1Impl<>(a);
    }
}
