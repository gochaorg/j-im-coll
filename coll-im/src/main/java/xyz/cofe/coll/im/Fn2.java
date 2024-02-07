package xyz.cofe.coll.im;

import java.io.Serializable;
import java.util.function.BiFunction;

/**
 * Функция с двумя элементами
 * @param <A> аргумент функции
 * @param <B> аргумент функции
 * @param <RES> результат функции
 */
public interface Fn2<A,B,RES> extends Serializable, BiFunction<A,B,RES> {
    RES apply(A a, B b);
}
