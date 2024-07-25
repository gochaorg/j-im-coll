package xyz.cofe.coll.im;

/**
 * Функция с 8 аргументами и без результата
 * @param <A> аргумент функции
 * @param <B> аргумент функции
 * @param <C> аргумент функции
 * @param <D> аргумент функции
 * @param <E> аргумент функции
 * @param <F> аргумент функции
 * @param <G> аргумент функции
 * @param <H> аргумент функции
 */
public interface Consumer8<A,B,C,D,E,F,G,H> extends Fn8<A,B,C,D,E,F,G,H,Result.NoValue> {
    void accept(A a, B b, C c,D d,E e,F f,G g,H h);

    @Override
    default Result.NoValue apply(A a, B b, C c, D d, E e, F f,G g,H h) {
        accept(a, b, c, d, e, f, g, h);
        return Result.NoValue.instance;
    }
}
