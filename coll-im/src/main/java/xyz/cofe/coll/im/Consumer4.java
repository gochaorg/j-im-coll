package xyz.cofe.coll.im;

/**
 * Функция с 4 аргументами и без результата
 * @param <A> аргумент функции
 * @param <B> аргумент функции
 * @param <C> аргумент функции
 * @param <D> аргумент функции
 */
public interface Consumer4<A,B,C,D> extends Fn4<A,B,C,D,Result.NoValue> {
    void accept(A a, B b, C c, D d);

    @Override
    default Result.NoValue apply(A a, B b, C c, D d) {
        accept(a, b, c, d);
        return Result.NoValue.instance;
    }
}
