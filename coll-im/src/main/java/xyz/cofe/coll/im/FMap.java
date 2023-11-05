package xyz.cofe.coll.im;

import java.util.Optional;
import java.util.function.Function;

/**
 * Отображение коллекции
 *
 * @param <A> элемент списка/коллекции
 */
public interface FMap<A> {
    /**
     * Отображение коллекции
     *
     * @param fmapper функция отображения
     * @param <B>     тип элементов в коллекции
     * @return отображенная коллекция
     */
    <B> ImList<B> fmap(Function<A, PositionalRead<B>> fmapper);

    /**
     * Отображение коллекции на две
     *
     * @param left  первая коллекция
     * @param right вторая коллекция
     * @param <A>   элементы первой коллекции
     * @param <B>   элементы второй коллекции
     */
    record Split<A, B>(ImList<A> left, ImList<B> right) {
        static public <A, B> Split<A, B> splitLeft(A a) {
            return new Split<>(ImList.of(a), ImList.of());
        }

        static public <A, B> Split<A, B> splitRight(B b) {
            return new Split<>(ImList.of(), ImList.of(b));
        }

        static public <A, B> Split<A, B> split(A a, B b) {
            return new Split<>(ImList.of(a), ImList.of(b));
        }

//        public Split<A,B> merge(Split<? extends A,? extends B> split){
//            if( split==null ) throw new IllegalArgumentException("split==null");
//            var leftList = left.prepend(split.left);
//            return null;
//        }
    }

    /**
     * Фильтрация коллекции на две: отфильтрованную
     *
     * @param mapper функция фильтрации
     * @param <B>    тип второй коллекции
     * @return две разнотипные коллекции
     */
//    <B> Split<B, A> split(Function<A, Split<B, A>> mapper);

    /**
     * Фильтрует коллекцию и возвращает коллекцию содержащую элементы указанного класса
     *
     * @param cls интересующий класс элементов
     * @param <B> тип интересующих элементов
     * @return коллекция элементов
     */
    @SuppressWarnings("unchecked")
    default <B> ImList<B> fmap(Class<B> cls) {
        if (cls == null) throw new IllegalArgumentException("cls==null");
        return fmap(item -> item != null && cls.isAssignableFrom(item.getClass()) ?
            ImListLinked.of((B) item) : ImListLinked.of()
        );
    }
}
