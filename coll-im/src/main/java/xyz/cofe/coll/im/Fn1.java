package xyz.cofe.coll.im;

import java.io.Serializable;
import java.util.function.Function;

/**
 * Функция с одним аргументами
 * @param <A> аргумент функции
 * @param <RES> результат функции
 */
public interface Fn1<A,RES> extends Serializable, Function<A,RES> {
    RES apply(A a);
}
