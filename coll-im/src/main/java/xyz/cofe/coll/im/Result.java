package xyz.cofe.coll.im;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Контейнер для успешного выполнения функции или наличия ошибки
 * @param <VALUE> Результат успешного выполнения
 * @param <ERROR> Результат выполнения с ошибкой
 */
public final class Result<VALUE,ERROR> {
    private final VALUE value;
    private final boolean hasValue;
    private final ERROR error;

    private Result(VALUE value, ERROR error, boolean hasValue){
        this.value = value;
        this.error = error;
        this.hasValue = hasValue;
    }

    public static <V,E> Result<V,E> ok(V v){
        return new Result<>(v, null, true);
    }

    public static <V,E> Result<V,E> ok(V v,Class<E> errType){
        return new Result<>(v, null, true);
    }

    public static <V,E> Result<V,E> error(E e){
        return new Result<>(null, e, false);
    }

    @SuppressWarnings({"OptionalAssignedToNull", "OptionalUsedAsFieldOrParameterType", "OptionalIsPresent"})
    public static <V,E> Result<V,E> from(Optional<V> value, Supplier<E> error){
        if( value==null ) throw new IllegalArgumentException("value==null");
        if( error==null ) throw new IllegalArgumentException("error==null");

        return value.isPresent() ? ok(value.get()) : error(error.get());
    }

    public Optional<VALUE> getOk(){
        if( hasValue )return Optional.of(value);
        return Optional.empty();
    }

    public Optional<ERROR> getError(){
        if( !hasValue )return Optional.of(error);
        return Optional.empty();
    }

    public boolean isOk(){ return hasValue; }
    public boolean isError(){ return !hasValue; }

    public VALUE unwrap(Fn1<ERROR,VALUE> errUnwrap){
        if( errUnwrap==null ) throw new IllegalArgumentException("errUnwrap==null");
        if( hasValue )return value;
        return errUnwrap.apply(error);
    }

    public <V> Result<V,ERROR> map(Fn1<VALUE,V> mapper){
        if( mapper==null ) throw new IllegalArgumentException("mapper==null");
        if( hasValue )return new Result<>(mapper.apply(value),null,true);
        return new Result<>(null,error,false);
    }

    public <V> Result<V,ERROR> fmap(Fn1<VALUE,Result<V,ERROR>> mapper){
        if( mapper==null ) throw new IllegalArgumentException("mapper==null");
        if( hasValue )return mapper.apply(value);
        return new Result<>(null,error,false);
    }

    public <E> Result<VALUE,E> mapErr(Fn1<ERROR,E> mapper){
        if( mapper==null ) throw new IllegalArgumentException("mapper==null");
        if( hasValue )return new Result<>(value,null,true);
        return new Result<>(null,mapper.apply(error),false);
    }

    public Optional<VALUE> toOptional(){
        return getOk();
    }

    public <R> R fold(Function<VALUE,R> succFold, Function<ERROR,R> errFold){
        if( succFold==null ) throw new IllegalArgumentException("succFold==null");
        if( errFold==null ) throw new IllegalArgumentException("errFold==null");
        return isOk() ? succFold.apply(value) : errFold.apply(error);
    }
}
