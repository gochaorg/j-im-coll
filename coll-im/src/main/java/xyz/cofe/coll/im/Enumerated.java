package xyz.cofe.coll.im;

/**
 * Перечисление
 *
 * @param index индекс элемента
 * @param value значение
 * @param <A>   тип значения
 */
public record Enumerated<A>(long index, A value) implements Tuple2<Long, A> {
    @Override
    public Long _1() {
        return index;
    }

    @Override
    public A _2() {
        return value;
    }
}
