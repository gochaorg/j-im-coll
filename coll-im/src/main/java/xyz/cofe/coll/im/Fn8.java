package xyz.cofe.coll.im;

import java.io.Serializable;

/**
 * Функция с 8 аргументами
 * @param <A> аргумент функции
 * @param <B> аргумент функции
 * @param <C> аргумент функции
 * @param <D> аргумент функции
 * @param <E> аргумент функции
 * @param <F> аргумент функции
 * @param <G> аргумент функции
 * @param <H> аргумент функции
 * @param <RES> результат функции
 */
public interface Fn8<A,B,C,D,E,F,G,H,RES> extends Serializable {
    RES apply(A a, B b, C c,D d,E e,F f,G g,H h);
}
