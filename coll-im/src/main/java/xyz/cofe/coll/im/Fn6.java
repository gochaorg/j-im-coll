package xyz.cofe.coll.im;

import java.io.Serializable;

/**
 * Функция с 6 аргументами
 * @param <A> аргумент функции
 * @param <B> аргумент функции
 * @param <C> аргумент функции
 * @param <D> аргумент функции
 * @param <E> аргумент функции
 * @param <F> аргумент функции
 * @param <RES> результат функции
 */
public interface Fn6<A,B,C,D,E,F,RES> extends Serializable {
    /**
     * Вызов функции
     * @param a 1ый аргумент функции
     * @param b 2ой аргумент функции
     * @param c 3ий аргумент функции
     * @param d 4ый аргумент функции
     * @param e 5ый аргумент функции
     * @param f 6ой аргумент функции
     * @return результат вызова функции
     */
    RES apply(A a, B b, C c,D d,E e,F f);
}
