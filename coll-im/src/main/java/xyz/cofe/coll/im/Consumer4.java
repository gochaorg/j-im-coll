package xyz.cofe.coll.im;

/**
 * Функция с 4 аргументами и без результата
 * @param <A> аргумент функции
 * @param <B> аргумент функции
 * @param <C> аргумент функции
 * @param <D> аргумент функции
 */
public interface Consumer4<A,B,C,D> extends Fn4<A,B,C,D,Result.NoValue> {
    /**
     * Переда аргументов в функцию
     * @param a 1ый аргумент функции
     * @param b 2ой аргумент функции
     * @param c 3ий аргумент функции
     * @param d 4ый аргумент функции
     */
    void accept(A a, B b, C c, D d);

    @Override
    default Result.NoValue apply(A a, B b, C c, D d) {
        accept(a, b, c, d);
        return Result.NoValue.instance;
    }
}
