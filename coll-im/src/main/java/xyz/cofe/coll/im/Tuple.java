package xyz.cofe.coll.im;

/**
 * Кортеж
 */
public interface Tuple {
    /**
     * Пустой кортеж
     *
     * @return Пустой кортеж
     */
    static Tuple of() {
        return new Tuple() {
        };
    }

    /**
     * Создание кортежа из одного элемента
     *
     * @param a   значение
     * @param <A> тип значения
     * @return Кортеж
     */
    default <A> Tuple1<A> append(A a) {
        return of(a);
    }

    /**
     * Создание кортежа из одного элемента
     *
     * @param a   значение
     * @param <A> тип элемента
     * @return кортеж
     */
    static <A> Tuple1<A> of(A a) {
        return Tuple1.of(a);
    }

    /**
     * Создание кортежа из 2-х элементов
     *
     * @param a   значение 1-го элемента
     * @param b   значение 2-го элемента
     * @param <A> Тип 1-го элемента
     * @param <B> Тип 2-го элемента
     * @return кортеж
     */
    static <A, B> Tuple2<A, B> of(A a, B b) {
        return Tuple2.of(a, b);
    }

    /**
     * Создание кортежа из 3-и элементов
     *
     * @param a   значение 1-го элемента
     * @param b   значение 2-го элемента
     * @param c   значение 3-го элемента
     * @param <A> Тип 1-го элемента
     * @param <B> Тип 2-го элемента
     * @param <C> Тип 3-го элемента
     * @return кортеж
     */
    static <A, B, C> Tuple3<A, B, C> of(A a, B b, C c) {
        return Tuple3.of(a, b, c);
    }

    /**
     * Создание кортежа из 4-и элементов
     *
     * @param a   значение 1-го элемента
     * @param b   значение 2-го элемента
     * @param c   значение 3-го элемента
     * @param d   значение 4-го элемента
     * @param <A> Тип 1-го элемента
     * @param <B> Тип 2-го элемента
     * @param <C> Тип 3-го элемента
     * @param <D> Тип 4-го элемента
     * @return кортеж
     */
    static <A, B, C, D> Tuple4<A, B, C, D> of(A a, B b, C c, D d) {
        return Tuple4.of(a, b, c, d);
    }

    /**
     * Создание кортежа из 5-и элементов
     *
     * @param a   значение 1-го элемента
     * @param b   значение 2-го элемента
     * @param c   значение 3-го элемента
     * @param d   значение 4-го элемента
     * @param e   значение 5-го элемента
     * @param <A> Тип 1-го элемента
     * @param <B> Тип 2-го элемента
     * @param <C> Тип 3-го элемента
     * @param <D> Тип 4-го элемента
     * @param <E> Тип 5-го элемента
     * @return кортеж
     */
    static <A, B, C, D, E> Tuple5<A, B, C, D, E> of(A a, B b, C c, D d, E e) {
        return Tuple5.of(a, b, c, d, e);
    }

    /**
     * Создание кортежа из 6-и элементов
     *
     * @param a   значение 1-го элемента
     * @param b   значение 2-го элемента
     * @param c   значение 3-го элемента
     * @param d   значение 4-го элемента
     * @param e   значение 5-го элемента
     * @param f   значение 6-го элемента
     * @param <A> Тип 1-го элемента
     * @param <B> Тип 2-го элемента
     * @param <C> Тип 3-го элемента
     * @param <D> Тип 4-го элемента
     * @param <E> Тип 5-го элемента
     * @param <F> Тип 6-го элемента
     * @return кортеж
     */
    static <A, B, C, D, E, F> Tuple6<A, B, C, D, E, F> of(A a, B b, C c, D d, E e, F f) {
        return Tuple6.of(a, b, c, d, e, f);
    }

    /**
     * Создание кортежа из 7-и элементов
     *
     * @param a   значение 1-го элемента
     * @param b   значение 2-го элемента
     * @param c   значение 3-го элемента
     * @param d   значение 4-го элемента
     * @param e   значение 5-го элемента
     * @param f   значение 6-го элемента
     * @param g   значение 7-го элемента
     * @param <A> Тип 1-го элемента
     * @param <B> Тип 2-го элемента
     * @param <C> Тип 3-го элемента
     * @param <D> Тип 4-го элемента
     * @param <E> Тип 5-го элемента
     * @param <F> Тип 6-го элемента
     * @param <G> Тип 7-го элемента
     * @return кортеж
     */
    static <A, B, C, D, E, F, G> Tuple7<A, B, C, D, E, F, G> of(A a, B b, C c, D d, E e, F f, G g) {
        return Tuple7.of(a, b, c, d, e, f, g);
    }

    /**
     * Создание кортежа из 8-и элементов
     *
     * @param a   значение 1-го элемента
     * @param b   значение 2-го элемента
     * @param c   значение 3-го элемента
     * @param d   значение 4-го элемента
     * @param e   значение 5-го элемента
     * @param f   значение 6-го элемента
     * @param g   значение 7-го элемента
     * @param h   значение 8-го элемента
     * @param <A> Тип 1-го элемента
     * @param <B> Тип 2-го элемента
     * @param <C> Тип 3-го элемента
     * @param <D> Тип 4-го элемента
     * @param <E> Тип 5-го элемента
     * @param <F> Тип 6-го элемента
     * @param <G> Тип 7-го элемента
     * @param <H> Тип 8-го элемента
     * @return кортеж
     */
    static <A, B, C, D, E, F, G, H> Tuple8<A, B, C, D, E, F, G, H> of(A a, B b, C c, D d, E e, F f, G g, H h) {
        return Tuple8.of(a, b, c, d, e, f, g, h);
    }

    /**
     * Создание кортежа из 9-и элементов
     *
     * @param a   значение 1-го элемента
     * @param b   значение 2-го элемента
     * @param c   значение 3-го элемента
     * @param d   значение 4-го элемента
     * @param e   значение 5-го элемента
     * @param f   значение 6-го элемента
     * @param g   значение 7-го элемента
     * @param h   значение 8-го элемента
     * @param i   значение 9-го элемента
     * @param <A> Тип 1-го элемента
     * @param <B> Тип 2-го элемента
     * @param <C> Тип 3-го элемента
     * @param <D> Тип 4-го элемента
     * @param <E> Тип 5-го элемента
     * @param <F> Тип 6-го элемента
     * @param <G> Тип 7-го элемента
     * @param <H> Тип 8-го элемента
     * @param <I> Тип 9-го элемента
     * @return кортеж
     */
    static <A, B, C, D, E, F, G, H, I> Tuple9<A, B, C, D, E, F, G, H, I> of(A a, B b, C c, D d, E e, F f, G g, H h, I i) {
        return Tuple9.of(a, b, c, d, e, f, g, h, i);
    }

    /**
     * Создание кортежа из 10-и элементов
     *
     * @param a   значение 1-го элемента
     * @param b   значение 2-го элемента
     * @param c   значение 3-го элемента
     * @param d   значение 4-го элемента
     * @param e   значение 5-го элемента
     * @param f   значение 6-го элемента
     * @param g   значение 7-го элемента
     * @param h   значение 8-го элемента
     * @param i   значение 9-го элемента
     * @param j   значение 10-го элемента
     * @param <A> Тип 1-го элемента
     * @param <B> Тип 2-го элемента
     * @param <C> Тип 3-го элемента
     * @param <D> Тип 4-го элемента
     * @param <E> Тип 5-го элемента
     * @param <F> Тип 6-го элемента
     * @param <G> Тип 7-го элемента
     * @param <H> Тип 8-го элемента
     * @param <I> Тип 9-го элемента
     * @param <J> Тип 10-го элемента
     * @return кортеж
     */
    static <A, B, C, D, E, F, G, H, I, J> Tuple10<A, B, C, D, E, F, G, H, I, J> of(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j) {
        return Tuple10.of(a, b, c, d, e, f, g, h, i, j);
    }
}
