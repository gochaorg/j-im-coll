package xyz.cofe.coll.im;

import java.io.Serializable;
import java.util.function.BiFunction;

/**
 * Функция с двумя аргументами
 * @param <A> аргумент функции
 * @param <B> аргумент функции
 * @param <RES> результат функции
 */
public interface Fn2<A,B,RES> extends Serializable, BiFunction<A,B,RES> {
    /**
     * Вызов функции
     * @param a 1ый аргумент функции
     * @param b 2ой аргумент функции
     * @return результат вызова функции
     */
    RES apply(A a, B b);
}
