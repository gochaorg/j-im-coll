package xyz.cofe.coll.im;

import java.io.Serializable;

/**
 * Функция с тремя элементами
 * @param <A> аргумент функции
 * @param <B> аргумент функции
 * @param <C> аргумент функции
 * @param <RES> результат функции
 */
public interface Fn3<A,B,C,RES> extends Serializable {
    RES apply(A a, B b, C c);
}
