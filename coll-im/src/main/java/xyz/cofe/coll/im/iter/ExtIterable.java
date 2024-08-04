package xyz.cofe.coll.im.iter;

import xyz.cofe.coll.im.Either;
import xyz.cofe.coll.im.Fn1;
import xyz.cofe.coll.im.Fn2;
import xyz.cofe.coll.im.Fn3;
import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.Product;
import xyz.cofe.coll.im.Tuple2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Итератор с доп функциями
 * @param <A> тип элемента итератора
 */
public interface ExtIterable<A> extends Iterable<A>,
                                        EachToMap<A>,
                                        EachToEnum<A>,
                                        Product<A> {

    /**
     * Создает итератор по указанным значениям
     * @param values значения
     * @return итератор
     * @param <A> тип элемента итератора
     */
    @SafeVarargs
    static <A> ExtIterable<A> of(A... values) {
        if (values == null) throw new IllegalArgumentException("values==null");
        if (values.length == 0) return () -> EmptyIterator.<A>iterate().iterator();
        if (values.length == 1) return () -> SingleIterator.<A>iterate(values[0]).iterator();
        return ArrayIterator.iterate(values);
    }

    /**
     * Создает итератор по указанным значениям
     * @param iterable значения
     * @return итератор
     * @param <A> тип элемента итератора
     */
    static <A> ExtIterable<A> from(Iterable<A> iterable) {
        if (iterable == null) throw new IllegalArgumentException("iterable==null");
        return iterable::iterator;
    }

    /**
     * Отображение коллекции, с возможностью вырезания и вставки.
     * <pre>
     *  var lst = ExtIterable.of(1, 2, 3).fmap(v -&gt;
     *      v &gt; 1
     *          ? v == 2
     *          ? // Заменяем на 1 элемент
     *          SingleIterator.iterate(v.toString()).iterator()
     *          : // Заменяем на 2 элемента
     *          ImList.of("4", "5").iterator()
     *          : // Заменяем на 0 элементов, вырезаем
     *          EmptyIterator.&lt;String&gt;iterate().iterator()
     *  ).toList();
     *  assertTrue(Objects.equals(lst, List.of("2", "4", "5")));
     * </pre>
     * @param map функция отображения
     * @return новая коллекция
     * @param <B> тип элемента новой коллекции
     */
    default <B> ExtIterable<B> fmap(Function<A, Iterator<B>> map) {
        if (map == null) throw new IllegalArgumentException("map==null");
        return () -> new FMapIterator<A, B>(iterator(), map);
    }

    /**
     * Отображение коллекции
     * <pre>
     * var lst = ExtIterable.of(1, 2, 3).map(Object::toString).toList();
     * assertTrue(Objects.equals(lst, List.of("1", "2", "3")));
     * </pre>
     * @param map функция отображения
     * @return новая коллекция
     * @param <B> тип элемента новой коллекции
     */
    default <B> ExtIterable<B> map(Function<A, B> map) {
        if (map == null) throw new IllegalArgumentException("map==null");
        return fmap(e -> new SingleIterator<>(map.apply(e)));
    }

    /**
     * Фильтрация коллекции
     * <pre>
     * int cnt = 0;
     * for (var e : ExtIterable.of(1, 2, 3, 4, 5).filter(v -&gt; v &gt; 2)) {
     *     cnt++;
     *     if (cnt == 1) assertTrue(Objects.equals(e, 3));
     *     if (cnt == 2) assertTrue(Objects.equals(e, 4));
     *     if (cnt == 3) assertTrue(Objects.equals(e, 5));
     * }
     * assertTrue(cnt == 3);    
     * </pre>
     * @param pred фильтр
     * @return отфильтрованная коллекция
     */
    default ExtIterable<A> filter(Predicate<A> pred) {
        if (pred == null) throw new IllegalArgumentException("pred==null");
        return fmap(e -> {
            if (pred.test(e)) {
                return SingleIterator.iterate(e).iterator();
            } else {
                return EmptyIterator.<A>iterate().iterator();
            }
        });
    }

    /**
     * Добавление в конец
     * <pre>
     * var lst = ExtIterable.of(1, 2).append(ExtIterable.of(3, 4)).toList();
     * assertTrue(Objects.equals(lst, List.of(1, 2, 3, 4)));
     * </pre>
     * @param tail добавляемая часть
     * @return новая коллекция с добавлением
     */
    default ExtIterable<A> append(Iterable<A> tail) {
        if (tail == null) throw new IllegalArgumentException("tail==null");
        return () -> new JoinIterator<>(iterator(), tail.iterator());
    }

    /**
     * Добавление в начало
     * <pre>
     * var lst = ExtIterable.of(1, 2).prepend(ExtIterable.of(3, 4)).toList();
     * assertTrue(Objects.equals(lst, List.of(3, 4, 1, 2)));
     * </pre>
     * @param head добавляемая часть
     * @return новая коллекция с добавлением
     */
    default ExtIterable<A> prepend(Iterable<A> head) {
        if (head == null) throw new IllegalArgumentException("head==null");
        return () -> new JoinIterator<>(head.iterator(), iterator());
    }

    /**
     * Преобразование в {@link Stream}
     * @return stream
     */
    default Stream<A> toStream(){
        return StreamSupport.stream(
            Spliterators.spliteratorUnknownSize(iterator(), Spliterator.ORDERED), false
        );
    }

    /**
     * Преобразование в список
     * @return список
     */
    default List<A> toList() {
        var lst = new ArrayList<A>();
        for (var e : this) {
            lst.add(e);
        }
        return lst;
    }

    /**
     * Преобразование в иммутабельный список
     * @return список
     */
    default ImList<A> toImList() {
        var lst = ImList.<A>of();
        for (var e : this) {
            lst = lst.prepend(e);
        }
        return lst.reverse();
    }

    /**
     * Пропустить указанное кол-во элементов
     * <pre>
     * var cnt = 0;
     * for (var e : ExtIterable.of(1, 2, 3, 4, 5, 6).skip(3)) {
     *     cnt++;
     *     if (cnt == 1) assertTrue(Objects.equals(e, 4));
     *     if (cnt == 2) assertTrue(Objects.equals(e, 5));
     *     if (cnt == 3) assertTrue(Objects.equals(e, 6));
     * }
     * assertTrue(cnt == 3);
     * </pre>
     * @param count кол-во элементов
     * @return Новый итератор
     */
    default ExtIterable<A> skip(long count) {
        return () -> new SkipIterator<>(iterator(), count);
    }

    /**
     * Возвращает не больше указанного кол-ва элементов
     * <pre>
     * var cnt = 0;
     * for (var e : ExtIterable.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 0).take(3)) {
     *     cnt++;
     * }
     * assertTrue(cnt == 3);
     * </pre>
     * @param count макс. кол-во элементов
     * @return Новый итератор
     */
    default ExtIterable<A> take(long count) {
        return () -> new TakeIterator<>(iterator(), count);
    }

    /**
     * Бесконечная индукция
     * @param init0 начальное значение
     * @param next генерация следующего значения, на основании предыдущего
     * @return Бесконечная последовательность
     * @param <A> Тип элементов в последовательности
     * @see #induction(Object, Object, Fn2)
     */
    static <A> ExtIterable<A> induction(A init0, Fn1<A, A> next) {
        if (next == null) throw new IllegalArgumentException("next==null");
        return InductionIterator.iterate(init0, next);
    }

    /**
     * Потенциально конечная индукция
     * @param init0 начальное значение
     * @param next генерация следующего значения, на основании предыдущего
     * @return Потенциально конечная последовательность
     * @param <A> Тип элементов в последовательности
     * @see #finitiveInduction(Object, Object, Fn2)
     */
    static <A> ExtIterable<A> finitiveInduction(A init0, Fn1<A, Optional<A>> next) {
        if (next == null) throw new IllegalArgumentException("next==null");
        return FinitiveInductionIterator.iterate(init0, next);
    }

    /**
     * Бесконечная индукция
     * <pre>
     * var expect = new ArrayList<>(List.of(1, 1, 2, 3, 5, 8, 13, 21, 34, 55));
     * for (var n : ExtIterable.induction(1, 1, (a, b) -> a + b).take(10)) {
     *     System.out.println(n);
     *     var e = expect.remove(0);
     *     assertTrue(Objects.equals(e, n));
     * }
     * </pre>
     * @param init0 начальное значение
     * @param init1 начальное значение
     * @param next генерация следующего значения, на основании предыдущего
     * @return Бесконечная последовательность
     * @param <A> Тип элементов в последовательности
     * @see #induction(Object, Object, Fn2)
     */
    static <A> ExtIterable<A> induction(A init0, A init1, Fn2<A, A, A> next) {
        if (next == null) throw new IllegalArgumentException("next==null");
        return InductionIterator.iterate(init0, init1, next);
    }

    /**
     * Потенциально конечная индукция
     * <pre>
     * var expect = new ArrayList&lt;&gt;(List.of(
     *   1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89));
     *
     * for (var n : ExtIterable.finitiveInduction(
     *   1, 1, (a, b) -&gt; a + b &gt;
     *   100 ? Optional.empty() : Optional.of(a + b)))
     * {
     *     System.out.println(n);
     *     var e = expect.remove(0);
     *     assertTrue(Objects.equals(e, n));
     * }    
     * </pre>
     * @param init0 начальное значение
     * @param init1 начальное значение
     * @param next генерация следующего значения, на основании предыдущего
     * @return Потенциально конечная последовательность
     * @param <A> Тип элементов в последовательности
     */
    static <A> ExtIterable<A> finitiveInduction(A init0, A init1, Fn2<A, A, Optional<A>> next) {
        if (next == null) throw new IllegalArgumentException("next==null");
        return FinitiveInductionIterator.iterate(init0, init1, next);
    }

    /**
     * Бесконечная индукция
     * @param init0 начальное значение
     * @param init1 начальное значение
     * @param init2 начальное значение
     * @param next генерация следующего значения, на основании предыдущего
     * @return Бесконечная последовательность
     * @param <A> Тип элементов в последовательности
     * @see #induction(Object, Object, Fn2)
     */
    static <A> ExtIterable<A> induction(A init0, A init1, A init2, Fn3<A, A, A, A> next) {
        if (next == null) throw new IllegalArgumentException("next==null");
        return InductionIterator.iterate(init0, init1, init2, next);
    }

    /**
     * Потенциально конечная индукция
     * @param init0 начальное значение
     * @param init1 начальное значение
     * @param init2 начальное значение
     * @param next генерация следующего значения, на основании предыдущего
     * @return Потенциально конечная последовательность
     * @param <A> Тип элементов в последовательности
     * @see #finitiveInduction(Object, Object, Fn2)
     */
    static <A> ExtIterable<A> finitiveInduction(A init0, A init1, A init2, Fn3<A, A, A, Optional<A>> next) {
        if (next == null) throw new IllegalArgumentException("next==null");
        return FinitiveInductionIterator.iterate(init0, init1, init2, next);
    }

    /**
     * Декартово множество, оба множества (this, other) должны быть конечными
     * @param other другое множество
     * @return Декартово множество
     * @param <B> Тип элемента второго множества
     */
    default <B> ExtIterable<Prod<A,B>> product(Iterable<B> other){
        if( other==null ) throw new IllegalArgumentException("other==null");
        return () -> new Product.ProdIterator<>(this, other);
    }

    /**
     * Соединение двух последовательность в одну
     * @param other другая последовательность
     * @return последовательность пар
     * @param <B> Тип элемента второй пары
     */
    default <B> ExtIterable<ZipIterator.Zipped<A,B>> zip(Iterable<B> other){
        if( other==null ) throw new IllegalArgumentException("other==null");
        return () -> new ZipIterator<>(iterator(), other.iterator());
    }

    record UnZipIterators<L,R>( Iterator<L> left, Iterator<R> right ) implements Tuple2<Iterator<L>, Iterator<R>> {
        @Override
        public Iterator<L> _1() {
            return left;
        }

        @Override
        public Iterator<R> _2() {
            return right;
        }
    }

    /**
     * Разделение последовательности на два итератора
     * @param split функция распределяющая на левую или правую часть
     * @return пара итераторов
     * @param <L> Тип элемента левого итератора
     * @param <R> Тип элемента правого итератора
     */
    default <L,R> UnZipIterators<L,R> unzipIterators(Fn1<A, Either<L,R>> split){
        if( split==null ) throw new IllegalArgumentException("split==null");
        var unzipper = new UnZipIterator<>( iterator(), split, true );
        return new UnZipIterators<>(unzipper.getLeftIterator(), unzipper.getRightIterator());
    }
}
