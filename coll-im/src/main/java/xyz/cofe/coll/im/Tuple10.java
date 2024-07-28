package xyz.cofe.coll.im;

import java.io.Serializable;
import java.util.Objects;

/**
 * Кортеж из 10 элементов
 * @param <A> тип 1‑го элемента
 * @param <B> тип 2‑го элемента
 * @param <C> тип 3‑го элемента
 * @param <D> тип 4‑го элемента
 * @param <E> тип 5‑го элемента
 * @param <F> тип 6‑го элемента
 * @param <H> тип 7‑го элемента
 * @param <G> тип 8‑го элемента
 * @param <I> тип 9‑го элемента
 * @param <J> тип 10-го элемента
 */
public interface Tuple10<A,B,C,D,E,F,H,G,I,J> extends Serializable {
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
     * Возвращает 8ой элемент
     * @return 8ой элемент
     */
    G _8();

    /**
     * Возвращает 9ый элемент
     * @return 9ый элемент
     */
    I _9();

    /**
     * Возвращает 10ый элемент
     * @return 10ый элемент
     */
    J _10();

    /**
     * Отображение кортежа
     * @param f функция отображения
     * @return результат отображения
     * @param <RES> результат отображения
     */
    default <RES> RES map( Fn10<A,B,C,D,E,F,H,G,I,J,RES> f ){
        if( f==null )throw new IllegalArgumentException("f==null");
        return f.apply(_1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), _10());
    }

    /**
     * Кортеж из 10 элементов
     * @param <A> тип 1‑го элемента
     * @param <B> тип 2‑го элемента
     * @param <C> тип 3‑го элемента
     * @param <D> тип 4‑го элемента
     * @param <E> тип 5‑го элемента
     * @param <F> тип 6‑го элемента
     * @param <H> тип 7‑го элемента
     * @param <G> тип 8‑го элемента
     * @param <I> тип 9‑го элемента
     * @param <J>> тип 10‑го элемента
     */
    final class Tuple10Impl<A,B,C,D,E,F,H,G,I,J> implements Tuple10<A,B,C,D,E,F,H,G,I,J> {
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
         * 8ой элемент
         */
        public final G g;

        /**
         * 9ый элемент
         */
        public final I i;

        /**
         * 10ый элемент
         */
        public final J j;

        /**
         * Конструктор
         * @param a 1ый элемент
         * @param b 2ой элемент
         * @param c 3ий элемент
         * @param d 4ый элемент
         * @param e 5ый элемент
         * @param f 6ой элемент
         * @param h 7ой элемент
         * @param g 8ой элемент
         * @param i 9ый элемент
         * @param j 10ый элемент
         */
        public Tuple10Impl(A a, B b, C c, D d, E e, F f,H h, G g,I i,J j) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.e = e;
            this.f = f;
            this.h = h;
            this.g = g;
            this.i = i;
            this.j = j;
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
        public G _8() {
            return g;
        }

        @Override
        public I _9() {
            return i;
        }

        @Override
        public J _10() {
            return j;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Tuple10Impl<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> tuple10 = (Tuple10Impl<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>) o;
            return Objects.equals(a, tuple10.a)
                && Objects.equals(b, tuple10.b)
                && Objects.equals(c, tuple10.c)
                && Objects.equals(d, tuple10.d)
                && Objects.equals(e, tuple10.e)
                && Objects.equals(f, tuple10.f)
                && Objects.equals(h, tuple10.h)
                && Objects.equals(g, tuple10.g)
                && Objects.equals(i, tuple10.i)
                && Objects.equals(j, tuple10.j)
                ;
        }

        @Override
        public int hashCode() {
            return Objects.hash(a, b, c, d, e, f);
        }

        @Override
        public String toString() {
            return "("+a+", "+b+",  "+c+", "+d+", "+e+", "+f+", "+h+", "+g+", "+i+", "+j+')';
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
     * @param g 8ой элемент
     * @param i 9ый элемент
     * @param j 10ый элемент
     * @return кортеж
     * @param <A> тип 1‑го элемента
     * @param <B> тип 2‑го элемента
     * @param <C> тип 3‑го элемента
     * @param <D> тип 4‑го элемента
     * @param <E> тип 5‑го элемента
     * @param <F> тип 6‑го элемента
     * @param <H> тип 7‑го элемента
     * @param <G> тип 8‑го элемента
     * @param <I> тип 9‑го элемента
     * @param <J> тип 10го элемента
     */
    static <A,B,C,D,E,F,H,G,I,J> Tuple10<A,B,C,D,E,F,H,G,I,J> of(A a, B b, C c, D d, E e, F f, H h, G g, I i, J j) {
        return new Tuple10Impl<>(a, b, c, d, e, f, h, g, i, j);
    }
}
