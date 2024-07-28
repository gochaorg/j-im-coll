package xyz.cofe.coll.im;

import java.io.Serializable;
import java.util.Objects;

/**
 * Кортеж из 4 элементов
 * @param <A> тип 1‑го элемента
 * @param <B> тип 2‑го элемента
 * @param <C> тип 3‑го элемента
 * @param <D> тип 4‑го элемента
 */
public interface Tuple4<A,B,C,D> extends Serializable {
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
     * Отображение кортежа
     * @param f функция отображения
     * @return результат отображения
     * @param <RES> результат отображения
     */
    default <RES> RES map( Fn4<A,B,C,D,RES> f ){
        if( f==null )throw new IllegalArgumentException("f==null");
        return f.apply(_1(), _2(), _3(), _4());
    }

    /**
     * Добавление элемента
     * @param e элемент
     * @return кортеж
     * @param <E> тип элемента
     */
    default <E> Tuple5<A,B,C,D,E> append(E e){
        return Tuple5.of(_1(), _2(), _3(), _4(), e);
    }

    /**
     * Кортеж из 4 элементов
     * @param <A> тип 1‑го элемента
     * @param <B> тип 2‑го элемента
     * @param <C> тип 3‑го элемента
     * @param <D> тип 4‑го элемента
     */
    final class Tuple4Impl<A,B,C,D> implements Tuple4<A,B,C,D> {
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
         * Конструктор
         * @param a 1ый элемент
         * @param b 2ой элемент
         * @param c 3ий элемент
         * @param d 4ый элемент
         */
        public Tuple4Impl(A a, B b, C c, D d) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
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
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Tuple4Impl<?, ?, ?, ?> tuple4 = (Tuple4Impl<?, ?, ?, ?>) o;
            return Objects.equals(a, tuple4.a) && Objects.equals(b, tuple4.b) && Objects.equals(c, tuple4.c) && Objects.equals(d, tuple4.d);
        }

        @Override
        public int hashCode() {
            return Objects.hash(a, b, c, d);
        }

        @Override
        public String toString() {
            return "("+a+", "+b+",  "+c+", "+d+')';
        }
    }

    /**
     * Создание кортежа
     * @param a 1ый элемент
     * @param b 2ой элемент
     * @param c 3ий элемент
     * @param d 4ый элемент
     * @return кортеж
     * @param <A> тип 1‑го элемента
     * @param <B> тип 2‑го элемента
     * @param <C> тип 3‑го элемента
     * @param <D> тип 4‑го элемента
     */
    static <A,B,C,D> Tuple4<A,B,C,D> of(A a, B b, C c, D d) {
        return new Tuple4Impl<>(a,b,c,d);
    }
}
