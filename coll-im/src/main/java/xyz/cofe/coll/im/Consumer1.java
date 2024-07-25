package xyz.cofe.coll.im;

import java.util.function.Consumer;

/**
 * Функция с одним аргументом и без результата
 * @param <A> аргумент функции
 */
public interface Consumer1<A> extends Consumer<A>, Fn1<A,Result.NoValue> {
    @Override
    default Result.NoValue apply(A a) {
        accept(a);
        return Result.NoValue.instance;
    }
}

