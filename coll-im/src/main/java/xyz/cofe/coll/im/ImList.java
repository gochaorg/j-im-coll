package xyz.cofe.coll.im;

import xyz.cofe.coll.im.iter.EachToMap;

import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Иммутабельный список
 * @param <A> тип элемента в списке
 */
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
    FMap<A>,
    Sort<A>,
    Product<A>,
    EachToMap<A>
{
    /**
     * Создание списка из указанных элементов
     * @param values элементы списка
     * @return Список из указанных элементов
     * @param <T> Тип элемента в списке
     */
    @SafeVarargs
    static <T> ImList<T> of(T... values) {
        return ImListLinked.of(values);
    }

    /**
     * Создание списка из указанных элементов
     * @param values элементы списка
     * @return Список из указанных элементов
     * @param <T> Тип элемента в списке
     */
    static <T> ImList<T> from(Iterable<T> values) {
        return ImListLinked.from(values);
    }

    /**
     * Создание списка из указанных элементов
     * @param values элементы списка
     * @return Список из указанных элементов
     * @param <T> Тип элемента в списке
     */
    static <T> ImList<T> from(PositionalRead<T> values) {
        return ImListLinked.from(values);
    }

    /**
     * Проверка наличия указанных элементов
     * <pre>
     * var lst = ImList.of(1,2,2,3,3,3);
     * var res = lst.containsCount(Integer::equals,ImList.of(2,3));
     * assertTrue(res == 5);
     * </pre>
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
     * <pre>
     * var lst = ImList.of(1,2,3);
     * var res = lst.containsAll(1,2);
     * assertTrue(res == true);
     * </pre>
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
     * <pre>
     * var lst = ImList.of(1,2,3);
     * var res = lst.containsAll(1,2,4);
     * assertTrue(res == false);
     * </pre>
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
     * <pre>
     * var lst = ImList.of(1,2,3);
     * var res = lst.contains(4);
     * assertTrue(res == false);
     * </pre>
     * @param item искомый элемент
     * @return true - указанный элементы есть в списке / false - указанные элементы отсутствуют или присутствуют в не полном объеме
     */
    default boolean contains(A item){
        return containsAll(item);
    }

    /**
     * Проверка наличия указанных элементов
     *
     * @param items искомые элементы
     * @return true - указанные элементы есть в списке / false - указанные элементы отсутствуют или присутствуют в не полном объеме
     * @see #containsAll(Object[])
     */
    default boolean containsAll(Iterable<A> items) {
        if (items == null) throw new IllegalArgumentException("items==null");
        var itms = ImListLinked.from(items);
        var cnt = containsCount(Objects::equals, itms);
        return itms.size() == cnt;
    }

    /**
     * Поиск элемента
     * <pre>
     * var res = ImList.of(1,2,3).find(v -&gt; v&gt;2);
     * assertTrue(res.get() == 3);
     * </pre>
     * @param predicate искомый элемент
     * @return искомый элемент или None
     */
    default Optional<A> find(Function<A, Boolean> predicate) {
        if (predicate == null) throw new IllegalArgumentException("predicate==null");
        return filter(predicate).head();
    }

    /**
     * Проверка наличия элемента
     * @param predicate искомый элемент
     * @return true - искомый элемент найден
     */
    default boolean exists(Function<A, Boolean> predicate) {
        if( predicate==null ) throw new IllegalArgumentException("predicate==null");
        return find(predicate).isPresent();
    }

    /**
     * Разворот списка
     * <pre>
     * var res = ImList.of(1,2,3).reverse();
     * assertTrue(res.size()==3);
     * assertTrue(res.get(0).map(v-&gt;v==3).orElse(false));
     * assertTrue(res.get(1).map(v-&gt;v==2).orElse(false));
     * assertTrue(res.get(2).map(v-&gt;v==1).orElse(false));     
     * </pre>
     * @return список в обратном порядке
     */
    default ImList<A> reverse() {
        return foldLeft(empty(), Prepend::prepend);
    }

    /**
     * Перечислить/нумеровать элементы списка
     * <pre>
     * var res = ImList.of("a","b","c").enumerate();
     * assertTrue(res.size()==3);
     * assertTrue(
     *   res.get(0).map(v-&gt;v.index()==0 &amp;&amp;
     *   v.value().equals("a")).orElse(false));
     * assertTrue(
     *   res.get(1).map(v-&gt;v.index()==1 &amp;&amp;
     *   v.value().equals("b")).orElse(false));
     * assertTrue(
     *   res.get(2).map(v-&gt;v.index()==2 &amp;&amp;
     *   v.value().equals("c")).orElse(false));
     * </pre>
     * @return список
     */
    default ImList<Enumerated<A>> enumerate() {
        return foldLeft(
            Tuple.of(0L, ImList.<Enumerated<A>>of()),
            (res, it) -> Tuple.of(res._1() + 1, res._2().prepend(new Enumerated<>(res._1(), it)))
        )._2().reverse();
    }

    /**
     * Возвращает stream для списка
     * @return stream
     */
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
     * <pre>
     * var res = ImList.of(1,2,3).take(2);
     * assertTrue(res.size()==2);
     * assertTrue(res.get(0).map(v-&gt;v==1).orElse(false));
     * assertTrue(res.get(1).map(v-&gt;v==2).orElse(false));    
     * </pre>
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
     * <pre>
     * var lst = ImList.of(1,2,3);
     * var res = lst.skip(2);
     * assertTrue(res.size()==1);
     * assertTrue(res.get(0).map(v-&gt;v==3).orElse(false));    
     * </pre>
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

    /**
     * Возвращает элементы, кроме последних указанных
     * @param count кол-во пропускаемых элементов
     * @return список кроме указанных последних
     */
    default ImList<A> dropLast(long count){
        if( count<0 ) throw new IllegalArgumentException("count<0");
        if( count==0 )return this;

        long t = size() - count;
        if( t<=0 )return ImList.of();

        return take(t);
    }

    /**
     * Создание декартового произведения
     * @param right правое значение
     * @return Итератор
     * @param <B> Тип правого значения
     */
    default <B> ImList<Prod<A,B>> product(Iterable<B> right){
        if( right==null ) throw new IllegalArgumentException("right==null");
        var result = ImList.<Prod<A,B>>of();
        for( var prod : Product.product(this, right)){
            result = result.prepend(prod);
        }
        return result.reverse();
    }
}
