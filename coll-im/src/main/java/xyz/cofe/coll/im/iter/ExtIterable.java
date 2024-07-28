package xyz.cofe.coll.im.iter;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public interface ExtIterable<E> extends Iterable<E>, EachToMap<E>, EachToEnum<E>,
                                        Product<E> {
    default <B> ExtIterable<B> fmap(Function<E, Iterator<B>> map) {
        return () -> {
            return new FMapIterator<E,B>( iterator(), map );
        };
    }

    default <B> ExtIterable<B> map(Function<E,B> map){
        return fmap( e -> List.of(map.apply(e)).iterator());
    }

    default ExtIterable<E> filter(Predicate<E> pred) {
        return fmap( e -> {
            if(pred.test(e)){
                return List.of(e).iterator();
            }else{
                return Collections.emptyIterator();
            }
        });
    }

    default List<E> toList(){
        var lst = new ArrayList<E>();
        for( var e : this ){
            lst.add(e);
        }
        return lst;
    }

    default ImList<E> toImList(){
        var lst = ImList.<E>of();
        for( var e : this ){
            lst = lst.prepend(e);
        }
        return lst.reverse();
    }
}
