package xyz.cofe.coll.im;

/**
 * Функция с 5 аргументами и без результата
 * @param <A> аргумент функции
 * @param <B> аргумент функции
 * @param <C> аргумент функции
 * @param <D> аргумент функции
 * @param <E> аргумент функции
 */
public interface Consumer5<A,B,C,D,E> extends Fn5<A,B,C,D,E,Result.NoValue> {
    /**
     * Переда аргументов в функцию
     * @param a 1ый аргумент функции
     * @param b 2ой аргумент функции
     * @param c 3ий аргумент функции
     * @param d 4ый аргумент функции
     * @param e 5ый аргумент функции
     */
    void accept(A a, B b, C c, D d,E e);

    @Override
    default Result.NoValue apply(A a, B b, C c, D d, E e) {
        accept(a, b, c, d, e);
        return Result.NoValue.instance;
    }
}
