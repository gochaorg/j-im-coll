package xyz.cofe.coll.im;

import xyz.cofe.coll.im.Countable;
import xyz.cofe.coll.im.ForEach;
import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.PositionalRead;

import java.util.Comparator;
import java.util.function.Function;

/**
 * Поддержка сортировки
 * @param <A> тип элементов в коллекции
 */
public interface Sort<A> extends PositionalRead<A> {
    /**
     * Сортировка списка
     * @param comparator функция сравнения
     * @return сортированный список
     */
    public default ImList<A> sort(Comparator<A> comparator) {
        if( comparator==null ) throw new IllegalArgumentException("comparator==null");
        var lst = toList();
        lst.sort(comparator);
        return ImList.from(lst);
    }

    /**
     * Сортировка списка
     * @param compareKey функция получения ключа сортировки
     * @return сортированный список
     * @param <B> ключ сортировки (тип)
     */
    public default <B extends Comparable<B>> ImList<A> sort(Function<A,B> compareKey){
        if( compareKey==null ) throw new IllegalArgumentException("compareKey==null");
        return sort(new Comparator<A>() {
            @Override
            public int compare(A o1, A o2) {
                var k1 = compareKey.apply(o1);
                var k2 = compareKey.apply(o2);
                return k1.compareTo(k2);
            }
        });
    }
}
