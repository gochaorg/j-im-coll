package xyz.cofe.coll.im;

/**
 * Функция с 6 аргументами и без результата
 * @param <A> аргумент функции
 * @param <B> аргумент функции
 * @param <C> аргумент функции
 * @param <D> аргумент функции
 * @param <E> аргумент функции
 * @param <F> аргумент функции
 */
public interface Consumer6<A,B,C,D,E,F> extends Fn6<A,B,C,D,E,F,Result.NoValue> {
    void accept(A a, B b, C c,D d,E e,F f);

    @Override
    default Result.NoValue apply(A a, B b, C c, D d, E e, F f) {
        accept(a, b, c, d, e, f);
        return Result.NoValue.instance;
    }
}
