package xyz.cofe.coll.im;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * Обход элементов списка
 * @param <A> элемент списка/коллекции
 */
public interface ForEach<A> extends Iterable<A> {
    /**
     * Обход списка
     * @param consumer функция принимающая элемент
     */
    void each(Consumer<A> consumer);

    default ImList<A> toImList(){
        var lst = new ArrayList<A>();
        each(lst::add);
        return ImList.from(lst);
    }
}
