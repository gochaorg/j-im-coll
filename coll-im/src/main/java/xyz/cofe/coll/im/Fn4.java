package xyz.cofe.coll.im;

import java.io.Serializable;

/**
 * Функция с 4 аргументами
 * @param <A> аргумент функции
 * @param <B> аргумент функции
 * @param <C> аргумент функции
 * @param <D> аргумент функции
 * @param <RES> результат функции
 */
public interface Fn4<A,B,C,D,RES> extends Serializable {
    /**
     * Вызов функции
     * @param a 1ый аргумент функции
     * @param b 2ой аргумент функции
     * @param c 3ий аргумент функции
     * @param d 4ый аргумент функции
     * @return результат вызова функции
     */
    RES apply(A a, B b, C c,D d);
}
