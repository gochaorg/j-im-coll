package xyz.cofe.coll.im;

/**
 * Функция с 8 аргументами и без результата
 * @param <A> аргумент функции
 * @param <B> аргумент функции
 * @param <C> аргумент функции
 * @param <D> аргумент функции
 * @param <E> аргумент функции
 * @param <F> аргумент функции
 * @param <G> аргумент функции
 * @param <H> аргумент функции
 * @param <I> аргумент функции
 * @param <J> аргумент функции
 */
public interface Consumer10<A,B,C,D,E,F,G,H,I,J> extends Fn10<A,B,C,D,E,F,G,H,I,J,Result.NoValue> {
    /**
     * Переда аргументов в функцию
     * @param a 1ый аргумент функции
     * @param b 2ой аргумент функции
     * @param c 3ий аргумент функции
     * @param d 4ый аргумент функции
     * @param e 5ый аргумент функции
     * @param f 6ой аргумент функции
     * @param g 7ой аргумент функции
     * @param h 8ой аргумент функции
     * @param i 9ый аргумент функции
     * @param j 10ый аргумент функции
     */
    void accept(A a, B b, C c,D d,E e,F f,G g,H h,I i,J j);

    @Override
    default Result.NoValue apply(A a, B b, C c, D d, E e, F f,G g,H h,I i,J j) {
        accept(a, b, c, d, e, f, g, h, i, j);
        return Result.NoValue.instance;
    }
}
