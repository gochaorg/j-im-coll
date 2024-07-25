package xyz.cofe.coll.im;

import java.io.Serializable;
import java.util.Objects;

/**
 * Кортеж из 3 элементов
 * @param <A> тип 1‑го элемента
 * @param <B> тип 2‑го элемента
 * @param <C> тип 3‑го элемента
 */
public sealed interface Tuple3<A,B,C> extends Serializable {
    /**
     * Возвращает 1ый элемент
     * @return 1ый элемент
     */
    A _1();

    /**
     * Возвращает 2ой элемент
     * @return 2ой элемент
     */
    B _2();

    /**
     * Возвращает 3ий элемент
     * @return 3ий элемент
     */
    C _3();

    /**
     * Отображение кортежа
     * @param f функция отображения
     * @return результат отображения
     * @param <RES> результат отображения
     */
    default <RES> RES map( Fn3<A,B,C,RES> f ){
        if( f==null )throw new IllegalArgumentException("f==null");
        return f.apply(_1(), _2(), _3());
    }

    /**
     * Добавление элемента
     * @param d элемент
     * @return кортеж
     * @param <D> тип элемента
     */
    default <D> Tuple4<A,B,C,D> append(D d){
        return Tuple4.of(_1(), _2(), _3(), d);
    }

    final class Tuple3Impl<A,B,C> implements Tuple3<A,B,C> {
        public final A a;
        public final B b;
        public final C c;

        public Tuple3Impl(A a, B b, C c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }

        @Override
        public A _1() {
            return a;
        }

        @Override
        public B _2() {
            return b;
        }

        @Override
        public C _3() {
            return c;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Tuple3Impl<?, ?, ?> tuple3 = (Tuple3Impl<?, ?, ?>) o;
            return Objects.equals(a, tuple3.a) && Objects.equals(b, tuple3.b) && Objects.equals(c, tuple3.c);
        }

        @Override
        public int hashCode() {
            return Objects.hash(a, b, c);
        }

        @Override
        public String toString() {
            return "("+a+", "+b+",  "+c+')';
        }
    }

    /**
     * Создание кортежа
     * @param a 1ый элемент
     * @param b 2ой элемент
     * @param c 3ий элемент
     * @return кортеж
     * @param <A> тип 1‑го элемента
     * @param <B> тип 2‑го элемента
     * @param <C> тип 3‑го элемента
     */
    static <A,B,C> Tuple3<A,B,C> of(A a, B b, C c) {
        return new Tuple3Impl<>(a,b,c);
    }
}
