package xyz.cofe.coll.im;

import java.io.Serializable;
import java.util.Objects;

/**
 * Кортеж из 7 элементов
 * @param <A> тип 1‑го элемента
 * @param <B> тип 2‑го элемента
 * @param <C> тип 3‑го элемента
 * @param <D> тип 4‑го элемента
 * @param <E> тип 5‑го элемента
 * @param <F> тип 6‑го элемента
 * @param <H> тип 7‑го элемента
 */
public interface Tuple7<A,B,C,D,E,F,H> extends Serializable {
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
     * Возвращает 6ой элемент
     * @return 6ой элемент
     */
    F _6();

    /**
     * Возвращает 7ой элемент
     * @return 7ой элемент
     */
    H _7();

    /**
     * Отображение кортежа
     * @param f функция отображения
     * @return результат отображения
     * @param <RES> результат отображения
     */
    default <RES> RES map( Fn7<A,B,C,D,E,F,H,RES> f ){
        if( f==null )throw new IllegalArgumentException("f==null");
        return f.apply(_1(), _2(), _3(), _4(), _5(), _6(), _7());
    }

    /**
     * Добавление элемента
     * @param g элемент
     * @return кортеж
     * @param <G> тип элемента
     */
    default <G> Tuple8<A,B,C,D,E,F,H,G> append(G g){
        return Tuple8.of(_1(), _2(), _3(), _4(), _5(), _6(), _7(), g);
    }

    /**
     * Кортеж из 7 элементов
     * @param <A> тип 1‑го элемента
     * @param <B> тип 2‑го элемента
     * @param <C> тип 3‑го элемента
     * @param <D> тип 4‑го элемента
     * @param <E> тип 5‑го элемента
     * @param <F> тип 6‑го элемента
     * @param <H> тип 7‑го элемента
     */
    final class Tuple7Impl<A,B,C,D,E,F,H> implements Tuple7<A,B,C,D,E,F,H> {
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
         * 6ой элемент
         */
        public final F f;

        /**
         * 7ой элемент
         */
        public final H h;

        /**
         * Конструктор
         * @param a 1ый элемент
         * @param b 2ой элемент
         * @param c 3ий элемент
         * @param d 4ый элемент
         * @param e 5ый элемент
         * @param f 6ой элемент
         * @param h 7ой элемент
         */
        public Tuple7Impl(A a, B b, C c, D d, E e, F f,H h) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.e = e;
            this.f = f;
            this.h = h;
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
        public F _6() {
            return f;
        }

        @Override
        public H _7() {
            return h;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Tuple7Impl<?, ?, ?, ?, ?, ?, ?> tuple7 = (Tuple7Impl<?, ?, ?, ?, ?, ?, ?>) o;
            return Objects.equals(a, tuple7.a)
                && Objects.equals(b, tuple7.b)
                && Objects.equals(c, tuple7.c)
                && Objects.equals(d, tuple7.d)
                && Objects.equals(e, tuple7.e)
                && Objects.equals(f, tuple7.f)
                && Objects.equals(h, tuple7.h)
                ;
        }

        @Override
        public int hashCode() {
            return Objects.hash(a, b, c, d, e, f);
        }

        @Override
        public String toString() {
            return "("+a+", "+b+",  "+c+", "+d+", "+e+", "+f+", "+h+')';
        }
    }

    /**
     * Создание кортежа
     * @param a 1ый элемент
     * @param b 2ой элемент
     * @param c 3ий элемент
     * @param d 4ый элемент
     * @param e 5ый элемент
     * @param f 6ой элемент
     * @param h 7ой элемент
     * @return кортеж
     * @param <A> тип 1‑го элемента
     * @param <B> тип 2‑го элемента
     * @param <C> тип 3‑го элемента
     * @param <D> тип 4‑го элемента
     * @param <E> тип 5‑го элемента
     * @param <F> тип 6‑го элемента
     * @param <H> тип 7‑го элемента
     */
    static <A,B,C,D,E,F,H> Tuple7<A,B,C,D,E,F,H> of(A a, B b, C c, D d, E e, F f,H h) {
        return new Tuple7Impl<>(a, b, c, d, e, f, h);
    }
}
