package xyz.cofe.coll.im;

import java.util.Optional;
import java.util.function.Function;

/**
 * Тип "Или", может содержать только одно из двух значений: {@link Left} или {@link Right}
 * @param <A> Тип левого значения
 * @param <B> Тип правого значения
 */
public sealed interface Either<A, B> {
    /**
     * Левое значение
     * @param value значение
     * @param <A> Тип значения
     * @param <B> Тип правого значения
     */
    record Left<A, B>(A value) implements Either<A, B> {}

    /**
     * Правое значение
     * @param value значение
     * @param <A> левое значение
     * @param <B> значение
     */
    record Right<A, B>(B value) implements Either<A, B> {}

    /**
     * Создание "левого" значения
     * @param a значение
     * @return левое значение
     * @param <A> Тип левого значения
     * @param <B> Тип правого значения
     */
    public static <A, B> Either<A, B> left(A a) {
        return new Left<>(a);
    }

    /**
     * Создание "правое" значения
     * @param b значение
     * @return правое значение
     * @param <A> Тип левого значения
     * @param <B> Тип правого значения
     */
    public static <A, B> Either<A, B> right(B b) {
        return new Right<>(b);
    }

    /**
     * Свертка значения
     * @param leftMap свертка левого значения
     * @param rightMap свертка правого значения
     * @return значение
     * @param <R> тип значения
     */
    public default <R> R fold(Function<A, R> leftMap, Function<B, R> rightMap) {
        if (leftMap == null) throw new IllegalArgumentException("leftMap==null");
        if (rightMap == null) throw new IllegalArgumentException("rightMap==null");
        if (this instanceof Either.Left) {
            return leftMap.apply(((Left<A, B>) this).value());
        }
        return rightMap.apply(((Right<A, B>) this).value());
    }

    /**
     * Отображение левого значения
     * @param map функция отображения
     * @return значение
     * @param <R> тип левого значения
     */
    @SuppressWarnings("unchecked")
    public default <R> Either<R, B> leftMap(Function<A, R> map) {
        if (map == null) throw new IllegalArgumentException("map==null");
        if (this instanceof Either.Right) return (Either<R, B>) this;
        return new Left<>(map.apply(((Left<A, B>) this).value()));
    }

    /**
     * Отображение правого значения
     * @param map функция отображения
     * @return значение
     * @param <R> тип правого значения
     */
    public default <R> Either<A, R> rightMap(Function<B, R> map) {
        if (map == null) throw new IllegalArgumentException("map==null");
        if (this instanceof Either.Left<A, B> o) {
            return Either.<A, R>left(o.value);
        }
        var e = ((Right<A, B>) this);
        return Either.<A, R>right(map.apply(e.value()));
    }

    /**
     * Отображение правого значения
     * @param map функция отображения
     * @return значение
     * @param <R> тип правого значения
     */
    public default <R> Either<A, R> rightFlatMap(Function<B, Either<A,R>> map){
        if( map==null ) throw new IllegalArgumentException("map==null");
        return this.fold(Either::left, map);
    }

    /**
     * Отображение левого значения
     * @param map функция отображения
     * @return значение
     * @param <R> тип левого значения
     */
    @SuppressWarnings("unchecked")
    public default <R> Either<R, B> leftFlatMap(Function<A, Either<R, B>> map) {
        if (map == null) throw new IllegalArgumentException("map==null");
        if (this instanceof Either.Right) return (Either<R, B>) this;
        return map.apply(((Left<A, B>) this).value());
    }

    /**
     * Содержит "левое" значение
     * @return true - левое
     */
    public default boolean isLeft() {return this instanceof Either.Left;}

    /**
     * Содержит "правое" значение
     * @return true - правое
     */
    public default boolean isRight() {return this instanceof Either.Right;}

    /**
     * Получение опционального левого значения
     * @return левое значение
     */
    public default Optional<A> toLeftOptional() {return fold(Optional::ofNullable, b -> Optional.empty());}

    /**
     * Получение опционального правого значения
     * @return правое значение
     */
    public default Optional<B> toRightOptional() {
        return fold(a -> Optional.empty(), Optional::ofNullable);
    }

    /**
     * Преобразование в {@link Result}
     * @return значение
     */
    public default Result<A,B> toResult(){
        return fold(Result::ok, Result::error);
    }
}
