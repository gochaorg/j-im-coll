package xyz.cofe.coll.im;

/**
 * Функция с 3 аргументами и без результата
 * @param <A> аргумент функции
 * @param <B> аргумент функции
 * @param <C> аргумент функции
 */
public interface Consumer3<A,B,C> extends Fn3<A,B,C,Result.NoValue> {
    /**
     * Переда аргументов в функцию
     * @param a 1ый аргумент функции
     * @param b 2ой аргумент функции
     * @param c 3ий аргумент функции
     */
    void accept(A a, B b, C c);

    @Override
    default Result.NoValue apply(A a, B b, C c) {
        accept(a, b, c);
        return Result.NoValue.instance;
    }
}
