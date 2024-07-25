package xyz.cofe.coll.im;

import java.io.Serializable;
import java.util.Objects;

/**
 * Кортеж из 8 элементов
 * @param <A> тип 1‑го элемента
 * @param <B> тип 2‑го элемента
 * @param <C> тип 3‑го элемента
 * @param <D> тип 4‑го элемента
 * @param <E> тип 5‑го элемента
 * @param <F> тип 6‑го элемента
 * @param <H> тип 7‑го элемента
 * @param <G> тип 8‑го элемента
 */
public sealed interface Tuple8<A,B,C,D,E,F,H,G> extends Serializable {
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
     * Отображение кортежа
     * @param f функция отображения
     * @return результат отображения
     * @param <RES> результат отображения
     */
    default <RES> RES map( Fn8<A,B,C,D,E,F,H,G,RES> f ){
        if( f==null )throw new IllegalArgumentException("f==null");
        return f.apply(_1(), _2(), _3(), _4(), _5(), _6(), _7(), _8());
    }

    /**
     * Добавление элемента
     * @param i элемент
     * @return кортеж
     * @param <I> тип элемента
     */
    default <I> Tuple9<A,B,C,D,E,F,H,G,I> append(I i){
        return Tuple9.of(_1(), _2(), _3(), _4(), _5(), _6(), _7(), _8(), i);
    }

    final class Tuple8Impl<A,B,C,D,E,F,H,G> implements Tuple8<A,B,C,D,E,F,H,G> {
        public final A a;
        public final B b;
        public final C c;
        public final D d;
        public final E e;
        public final F f;
        public final H h;
        public final G g;

        public Tuple8Impl(A a, B b, C c, D d, E e, F f,H h, G g) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.e = e;
            this.f = f;
            this.h = h;
            this.g = g;
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
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Tuple8Impl<?, ?, ?, ?, ?, ?, ?, ?> tuple8 = (Tuple8Impl<?, ?, ?, ?, ?, ?, ?, ?>) o;
            return Objects.equals(a, tuple8.a)
                && Objects.equals(b, tuple8.b)
                && Objects.equals(c, tuple8.c)
                && Objects.equals(d, tuple8.d)
                && Objects.equals(e, tuple8.e)
                && Objects.equals(f, tuple8.f)
                && Objects.equals(h, tuple8.h)
                && Objects.equals(g, tuple8.g)
                ;
        }

        @Override
        public int hashCode() {
            return Objects.hash(a, b, c, d, e, f);
        }

        @Override
        public String toString() {
            return "("+a+", "+b+",  "+c+", "+d+", "+e+", "+f+", "+h+", "+g+')';
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
     * @return кортеж
     * @param <A> тип 1‑го элемента
     * @param <B> тип 2‑го элемента
     * @param <C> тип 3‑го элемента
     * @param <D> тип 4‑го элемента
     * @param <E> тип 5‑го элемента
     * @param <F> тип 6‑го элемента
     * @param <H> тип 7‑го элемента
     * @param <G> тип 8‑го элемента
     */
    static <A,B,C,D,E,F,H,G> Tuple8<A,B,C,D,E,F,H,G> of(A a, B b, C c, D d, E e, F f, H h, G g) {
        return new Tuple8Impl<>(a, b, c, d, e, f, h, g);
    }
}
