package xyz.cofe.coll.im;

import java.io.Serializable;

/**
 * Функция с тремя аргументами
 * @param <A> аргумент функции
 * @param <B> аргумент функции
 * @param <C> аргумент функции
 * @param <RES> результат функции
 */
public interface Fn3<A,B,C,RES> extends Serializable {
    /**
     * Вызов функции
     * @param a 1ый аргумент функции
     * @param b 2ой аргумент функции
     * @param c 3ий аргумент функции
     * @return результат вызова функции
     */
    RES apply(A a, B b, C c);
}
