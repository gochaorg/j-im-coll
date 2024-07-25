package xyz.cofe.coll.im;

import java.io.Serializable;
import java.util.Objects;

/**
 * Кортеж из 9 элементов
 * @param <A> тип 1‑го элемента
 * @param <B> тип 2‑го элемента
 * @param <C> тип 3‑го элемента
 * @param <D> тип 4‑го элемента
 * @param <E> тип 5‑го элемента
 * @param <F> тип 6‑го элемента
 * @param <H> тип 7‑го элемента
 * @param <G> тип 8‑го элемента
 * @param <I> тип 9‑го элемента
 */
public sealed interface Tuple9<A,B,C,D,E,F,H,G,I> extends Serializable {
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
     * Отображение кортежа
     * @param f функция отображения
     * @return результат отображения
     * @param <RES> результат отображения
     */
    default <RES> RES map( Fn9<A,B,C,D,E,F,H,G,I,RES> f ){
        if( f==null )throw new IllegalArgumentException("f==null");
        return f.apply(_1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9());
    }

    /**
     * Добавление элемента
     * @param j элемент
     * @return кортеж
     * @param <J> тип элемента
     */
    default <J> Tuple10<A,B,C,D,E,F,H,G,I,J> append(J j){
        return Tuple10.of(_1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), _9(), j);
    }

    final class Tuple9Impl<A,B,C,D,E,F,H,G,I> implements Tuple9<A,B,C,D,E,F,H,G,I> {
        public final A a;
        public final B b;
        public final C c;
        public final D d;
        public final E e;
        public final F f;
        public final H h;
        public final G g;
        public final I i;

        public Tuple9Impl(A a, B b, C c, D d, E e, F f,H h, G g,I i) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.e = e;
            this.f = f;
            this.h = h;
            this.g = g;
            this.i = i;
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
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Tuple9Impl<?, ?, ?, ?, ?, ?, ?, ?, ?> tuple9 = (Tuple9Impl<?, ?, ?, ?, ?, ?, ?, ?, ?>) o;
            return Objects.equals(a, tuple9.a)
                && Objects.equals(b, tuple9.b)
                && Objects.equals(c, tuple9.c)
                && Objects.equals(d, tuple9.d)
                && Objects.equals(e, tuple9.e)
                && Objects.equals(f, tuple9.f)
                && Objects.equals(h, tuple9.h)
                && Objects.equals(g, tuple9.g)
                && Objects.equals(i, tuple9.i)
                ;
        }

        @Override
        public int hashCode() {
            return Objects.hash(a, b, c, d, e, f);
        }

        @Override
        public String toString() {
            return "("+a+", "+b+",  "+c+", "+d+", "+e+", "+f+", "+h+", "+g+", "+i+')';
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
     * @return кортеж
     * @param <A> тип 1го элемента
     * @param <B> тип 2го элемента
     * @param <C> тип 3го элемента
     * @param <D> тип 4го элемента
     * @param <E> тип 5го элемента
     * @param <F> тип 6го элемента
     * @param <H> тип 7‑го элемента
     * @param <G> тип 8‑го элемента
     * @param <I> тип 9‑го элемента
     */
    static <A,B,C,D,E,F,H,G,I> Tuple9<A,B,C,D,E,F,H,G,I> of(A a, B b, C c, D d, E e, F f, H h, G g,I i) {
        return new Tuple9Impl<>(a, b, c, d, e, f, h, g, i);
    }
}
