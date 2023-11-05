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

        /**
         * Объединение списков
         *
         * @param split объединяемый список
         * @return объеденные списки
         */
        public Split<A, B> merge(Split<? extends A, ? extends B> split) {
            if (split == null) throw new IllegalArgumentException("split==null");
            var leftList = left.prepend(split.left);
            var rightList = right.prepend(split.right);
            return new Split<>(leftList, rightList);
        }

        /**
         * Разворот списков
         *
         * @return списки
         */
        public Split<A, B> reverse() {
            return new Split<>(left.reverse(), right.reverse());
        }

        public <C,D> Split<C,D> fmap(Function<A, PositionalRead<C>> leftMapper, Function<B, PositionalRead<D>> rightMapper){
            if( leftMapper==null ) throw new IllegalArgumentException("leftMapper==null");
            if( rightMapper==null ) throw new IllegalArgumentException("rightMapper==null");
            return new Split<>( left.fmap(leftMapper), right.fmap(rightMapper) );
        }

        public <D> Split<A,D> rightFmap(Function<B, PositionalRead<D>> rightMapper){
            if( rightMapper==null ) throw new IllegalArgumentException("rightMapper==null");
            return new Split<>( left, right.fmap(rightMapper) );
        }

        public <D> Split<A,D> rightFmap(Class<D> cls){
            if( cls==null ) throw new IllegalArgumentException("cls==null");
            return new Split<>( left, right.fmap(cls) );
        }

        public <C> Split<C,B> leftFmap(Function<A, PositionalRead<C>> leftMapper){
            if( leftMapper==null ) throw new IllegalArgumentException("leftMapper==null");
            return new Split<>( left.fmap(leftMapper), right );
        }

        public <C> Split<C,B> leftFmap(Class<C> cls){
            if( cls==null ) throw new IllegalArgumentException("cls==null");
            return new Split<>( left.fmap(cls), right );
        }

        public <R> R fold( Function<ImList<A>, Function<ImList<B>,R>> folder ){
            if( folder==null ) throw new IllegalArgumentException("folder==null");
            return folder.apply(left).apply(right);
        }
    }

    /**
     * Фильтрация коллекции на две: отфильтрованную
     *
     * @param mapper функция фильтрации
     * @param <B>    тип второй коллекции
     * @return две разнотипные коллекции
     */
    <B> Split<B, A> split(Function<A, Split<B, A>> mapper);

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

    /**
     * Фильтрует коллекцию и возвращает коллекцию содержащую элементы указанного класса
     * @param cls интересующий класс элементов
     * @return коллекции
     * @param <B> тип интересующих элементов
     */
    @SuppressWarnings("unchecked")
    default <B> Split<B, A> split(Class<B> cls) {
        if (cls == null) throw new IllegalArgumentException("cls==null");
        return split(item ->
            item != null && cls.isAssignableFrom(item.getClass())
                ? Split.splitLeft((B) item)
                : Split.splitRight(item)
        ).reverse();
    }


}
