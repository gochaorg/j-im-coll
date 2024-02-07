package xyz.cofe.coll.im;

import java.io.Serializable;

/**
 * Функция с тремя элементами
 * @param <A> аргумент функции
 * @param <B> аргумент функции
 * @param <C> аргумент функции
 * @param <D> аргумент функции
 * @param <E> аргумент функции
 * @param <RES> результат функции
 */
public interface Fn5<A,B,C,D,E,RES> extends Serializable {
    RES apply(A a, B b, C c,D d,E e);
}
