package xyz.cofe.coll.im;

import java.util.function.BiConsumer;

/**
 * Функция с двумя аргументами и без результата
 * @param <A> аргумент функции
 * @param <B> аргумент функции
 */
public interface Consumer2<A,B> extends BiConsumer<A,B>, Fn2<A,B,Result.NoValue> {
    @Override
    default Result.NoValue apply(A a, B b) {
        accept(a,b);
        return Result.NoValue.instance;
    }
}
