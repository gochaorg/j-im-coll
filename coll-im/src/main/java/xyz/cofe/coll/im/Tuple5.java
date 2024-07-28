package xyz.cofe.coll.im;

import java.io.Serializable;
import java.util.Objects;

/**
 * Кортеж из 5 элементов
 * @param <A> тип 1‑го элемента
 * @param <B> тип 2‑го элемента
 * @param <C> тип 3‑го элемента
 * @param <D> тип 4‑го элемента
 * @param <E> тип 5‑го элемента
 */
public interface Tuple5<A,B,C,D,E> extends Serializable {
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
     * Возвращает 4ый элемент
     * @return 4ый элемент
     */
    D _4();

    /**
     * Возвращает 5ый элемент
     * @return 5ый элемент
     */
    E _5();

    /**
     * Отображение кортежа
     * @param f функция отображения
     * @return результат отображения
     * @param <RES> результат отображения
     */
    default <RES> RES map( Fn5<A,B,C,D,E,RES> f ){
        if( f==null )throw new IllegalArgumentException("f==null");
        return f.apply(_1(), _2(), _3(), _4(), _5());
    }

    /**
     * Добавление элемента
     * @param f элемент
     * @return кортеж
     * @param <F> тип элемента
     */
    default <F> Tuple6<A,B,C,D,E,F> append(F f){
        return Tuple6.of(_1(), _2(), _3(), _4(), _5(), f);
    }

    /**
     * Кортеж из 5 элементов
     * @param <A> тип 1‑го элемента
     * @param <B> тип 2‑го элемента
     * @param <C> тип 3‑го элемента
     * @param <D> тип 4‑го элемента
     * @param <E> тип 5‑го элемента
     */
    final class Tuple5Impl<A,B,C,D,E> implements Tuple5<A,B,C,D,E> {
        /**
         * 1ый элемент
         */
        public final A a;

        /**
         * 2ой элемент
         */
        public final B b;

        /**
         * 3ий элемент
         */
        public final C c;

        /**
         * 4ый элемент
         */
        public final D d;

        /**
         * 5ый элемент
         */
        public final E e;

        /**
         * Конструктор
         * @param a 1ый элемент
         * @param b 2ой элемент
         * @param c 3ий элемент
         * @param d 4ый элемент
         * @param e 5ый элемент
         */
        public Tuple5Impl(A a, B b, C c, D d, E e) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.e = e;
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
        public D _4() {
            return d;
        }

        @Override
        public E _5() {
            return e;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Tuple5Impl<?, ?, ?, ?, ?> tuple5 = (Tuple5Impl<?, ?, ?, ?, ?>) o;
            return Objects.equals(a, tuple5.a) && Objects.equals(b, tuple5.b) && Objects.equals(c, tuple5.c) && Objects.equals(d, tuple5.d) && Objects.equals(e, tuple5.e);
        }

        @Override
        public int hashCode() {
            return Objects.hash(a, b, c, d, e);
        }

        @Override
        public String toString() {
            return "("+a+", "+b+",  "+c+", "+d+", "+e+')';
        }
    }

    /**
     * Создание кортежа
     * @param a 1ый элемент
     * @param b 2ой элемент
     * @param c 3ий элемент
     * @param d 4ый элемент
     * @param e 5ый элемент
     * @return кортеж
     * @param <A> тип 1‑го элемента
     * @param <B> тип 2‑го элемента
     * @param <C> тип 3‑го элемента
     * @param <D> тип 4‑го элемента
     * @param <E> тип 5‑го элемента
     */
    static <A,B,C,D,E> Tuple5<A,B,C,D,E> of(A a, B b, C c, D d,E e) {
        return new Tuple5Impl<>(a,b,c,d,e);
    }
}
