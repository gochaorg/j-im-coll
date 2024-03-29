package xyz.cofe.coll.im;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import xyz.cofe.coll.im.json.jakson.ImListSerializer;

import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

@JsonSerialize(using = ImListSerializer.class)
public interface ImList<A>
    extends
    PositionalRead<A>,
    Countable,
    One<ImList<A>, A>,
    Append<ImList<A>, A>,
    Prepend<ImList<A>, A>,
    Tail<ImList<A>, A>,
    Filter<ImList<A>, A>,
    MAP<A>,
    FMap<A> {
    @SafeVarargs
    static <T> ImList<T> of(T... values) {
        return ImListLinked.of(values);
    }

    static <T> ImList<T> of(Iterable<T> values) {
        return ImListLinked.of(values);
    }

    static <T> ImList<T> of(PositionalRead<T> values) {
        return ImListLinked.of(values);
    }

    /**
     * Проверка наличия указанных элементов
     *
     * @param equals функция проверки на равенство
     * @param items  искомые элементы
     * @return кол-во совпавших
     */
    default int containsCount(Fn2<A, A, Boolean> equals, PositionalRead<A> items) {
        if (equals == null) throw new IllegalArgumentException("equals==null");
        if (items == null) throw new IllegalArgumentException("items==null");
        int[] cnt = new int[]{0};
        items.each(itm -> {
            foldLeft(0, (acc, it) -> {
                if (equals.apply(it, itm)) {
                    cnt[0]++;
                }
                return acc;
            });
        });
        return cnt[0];
    }

    /**
     * Проверка наличия указанных элементов
     *
     * @param items искомые элементы
     * @return true - указанные элементы есть в списке / false - указанные элементы отсутствуют или присутствуют в не полном объеме
     */
    default boolean containsAll(PositionalRead<A> items) {
        if (items == null) throw new IllegalArgumentException("items==null");
        var cnt = containsCount(Objects::equals, items);
        return items.size() == cnt;
    }

    /**
     * Проверка наличия указанных элементов
     *
     * @param items искомые элементы
     * @return true - указанные элементы есть в списке / false - указанные элементы отсутствуют или присутствуют в не полном объеме
     */
    @SuppressWarnings("unchecked")
    default boolean containsAll(A... items) {
        if (items == null) throw new IllegalArgumentException("items==null");
        var cnt = containsCount(Objects::equals, ImListLinked.of(items));
        return items.length == cnt;
    }

    /**
     * Проверка наличия указанных элементов
     *
     * @param items искомые элементы
     * @return true - указанные элементы есть в списке / false - указанные элементы отсутствуют или присутствуют в не полном объеме
     */
    default boolean containsAll(Iterable<A> items) {
        if (items == null) throw new IllegalArgumentException("items==null");
        var itms = ImListLinked.of(items);
        var cnt = containsCount(Objects::equals, itms);
        return itms.size() == cnt;
    }

    /**
     * Поиск элемента
     *
     * @param predicate искомый элемент
     * @return искомый элемент или None
     */
    default Optional<A> find(Function<A, Boolean> predicate) {
        if (predicate == null) throw new IllegalArgumentException("predicate==null");
        return filter(predicate).head();
    }

    /**
     * Разворот списка
     *
     * @return список в обратном порядке
     */
    default ImList<A> reverse() {
        return foldLeft(empty(), Prepend::prepend);
    }

    /**
     * Перечисление
     *
     * @param index индекс элемента
     * @param value значение
     * @param <A>   тип значения
     */
    record Enumerated<A>(long index, A value) {
    }

    /**
     * Перечислить/нумеровать элементы списка
     *
     * @return список
     */
    default ImList<Enumerated<A>> enumerate() {
        return foldLeft(
            Tuple.of(0L, ImList.<Enumerated<A>>of()),
            (res, it) -> Tuple.of(res._1() + 1, res._2().prepend(new Enumerated<>(res._1(), it)))
        )._2().reverse();
    }

    default Stream<A> stream() {
        return toList().stream();
    }

    /**
     * Сопоставление двух списков в пары.
     *
     * <pre>
     * var res = ImList.of(1,2,3).zip(ImList.of(4,5,6,7));
     * assertTrue(res.size()==3);
     * assertTrue(res.get(0).map(v->v.equals(Tuple2.of(1,4))).orElse(false));
     * assertTrue(res.get(1).map(v->v.equals(Tuple2.of(2,5))).orElse(false));
     * assertTrue(res.get(2).map(v->v.equals(Tuple2.of(3,6))).orElse(false));
     * </pre>
     *
     * @param other второй список
     * @return список пар
     * @param <B> тип элементов второй пары
     */
    default <B> ImList<Tuple2<A, B>> zip(ImList<B> other) {
        if (other == null) throw new IllegalArgumentException("other==null");

        Iterator<A> it_a = iterator();
        Iterator<B> it_b = other.iterator();

        ImList<Tuple2<A, B>> list = ImList.of();
        while (true) {
            if (!it_a.hasNext()) break;
            if (!it_b.hasNext()) break;
            list = list.prepend(Tuple2.of(it_a.next(), it_b.next()));
        }

        return list.reverse();
    }

    /**
     * Возвращает указанное кол-во элементов или меньше (сколько есть)
     * @param count кол-во элементов
     * @return список
     */
    default ImList<A> take(long count) {
        if (count < 0) throw new IllegalArgumentException("count<0");
        if (count == 0) return empty();

        long size = size();
        if (count >= size) return this;

        long skipCnt = size - count;
        return foldRight(Tuple2.of(skipCnt, empty()),
            (tup, it) -> tup._1() > 0
                ? Tuple2.of(tup._1() - 1, tup._2())
                : Tuple2.of(tup._1(), tup._2().prepend(it))
        )._2();
    }

    /**
     * Возвращает элементы, пропуская указанное кол-во с начала
     * @param count кол-во пропускаемых элементов
     * @return список
     */
    default ImList<A> skip(long count){
        if( count<0 )throw new IllegalArgumentException("count<0");
        if( count==0 )return this;

        long size = size();
        if( count>=size )return empty();

        long takeCount = size - count;

        return foldRight(
            Tuple2.of(takeCount, empty()),
            (tup, it) ->
                tup._1() > 0
                    ? Tuple2.of(tup._1()-1, tup._2().prepend(it))
                    : tup
        )._2();
    }
}
