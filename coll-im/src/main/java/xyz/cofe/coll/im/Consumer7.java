package xyz.cofe.coll.im;

/**
 * Функция с 7 аргументами и без результата
 * @param <A> аргумент функции
 * @param <B> аргумент функции
 * @param <C> аргумент функции
 * @param <D> аргумент функции
 * @param <E> аргумент функции
 * @param <F> аргумент функции
 * @param <G> аргумент функции
 */
public interface Consumer7<A,B,C,D,E,F,G> extends Fn7<A,B,C,D,E,F,G,Result.NoValue> {
    /**
     * Переда аргументов в функцию
     * @param a 1ый аргумент функции
     * @param b 2ой аргумент функции
     * @param c 3ий аргумент функции
     * @param d 4ый аргумент функции
     * @param e 5ый аргумент функции
     * @param f 6ой аргумент функции
     * @param g 7ой аргумент функции
     */
    void accept(A a, B b, C c,D d,E e,F f,G g);

    @Override
    default Result.NoValue apply(A a, B b, C c, D d, E e, F f,G g) {
        accept(a, b, c, d, e, f, g);
        return Result.NoValue.instance;
    }
}
