package xyz.cofe.coll.im;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Контейнер для успешного выполнения функции или наличия ошибки
 * @param <VALUE> Результат успешного выполнения
 * @param <ERROR> Результат выполнения с ошибкой
 */
public final class Result<VALUE,ERROR> implements ForEach<VALUE> {
    public static final class NoValue {
        private NoValue(){}
        public final static NoValue instance = new NoValue();

        @Override
        public String toString(){
            return "NoValue";
        }
    }

    private final VALUE value;
    private final boolean hasValue;
    private final ERROR error;

    private Result(VALUE value, ERROR error, boolean hasValue){
        this.value = value;
        this.error = error;
        this.hasValue = hasValue;
    }

    /**
     * Возвращает успешный результат
     * @return успешный результат
     * @param <B> тип ошибки
     */
    public static <B> Result<NoValue, B> ok() {
        return ok(NoValue.instance);
    }

    /**
     * Возвращает успешный результат
     * @param v результат
     * @return результат
     * @param <V> тип результата
     * @param <E> тип ошибки
     */
    public static <V,E> Result<V,E> ok(V v){
        return new Result<>(v, null, true);
    }

    /**
     * Возвращает успешный результат
     * @param v результат
     * @param errType тип ошибки
     * @return результат
     * @param <V> тип результата
     * @param <E> тип ошибки
     */
    public static <V,E> Result<V,E> ok(V v,Class<E> errType){
        return new Result<>(v, null, true);
    }

    /**
     * Возвращает ошибку
     * @param e ошибка
     * @return результат
     * @param <V> тип результата
     * @param <E> тип ошибки
     */
    public static <V,E> Result<V,E> error(E e){
        return new Result<>(null, e, false);
    }

    /**
     * Возвращает ошибку
     * @param e ошибка
     * @param errType тип ошибки
     * @return результат
     * @param <V> тип результата
     * @param <E> тип ошибки
     */
    public static <V,E> Result<V,E> error(E e, Class<E> errType){
        return new Result<>(null, e, false);
    }

    /**
     * Преобразование опционального значения
     * @param value опциональное значения
     * @param error ошибка
     * @return результат
     * @param <V> тип результата
     * @param <E> тип ошибки
     */
    @SuppressWarnings({"OptionalAssignedToNull", "OptionalUsedAsFieldOrParameterType", "OptionalIsPresent"})
    public static <V,E> Result<V,E> from(Optional<V> value, Supplier<E> error){
        if( value==null ) throw new IllegalArgumentException("value==null");
        if( error==null ) throw new IllegalArgumentException("error==null");

        return value.isPresent() ? ok(value.get()) : error(error.get());
    }

    /**
     * Возвращает опциональный результат
     * @return опциональный результат
     */
    public Optional<VALUE> getOk(){
        if( hasValue )return Optional.of(value);
        return Optional.empty();
    }

    /**
     * Возвращает опциональную ошибку
     * @return опциональная ошибка
     */
    public Optional<ERROR> getError(){
        if( !hasValue )return Optional.of(error);
        return Optional.empty();
    }

    /**
     * Проверка наличия успешного значения
     * @return true - существует
     */
    public boolean isOk(){ return hasValue; }

    /**
     * Проверка наличия ошибки
     * @return true - ошибка существует
     */
    public boolean isError(){ return !hasValue; }

    /**
     * Чтение значения
     * @param errUnwrap преобразование ошибки
     * @return значение
     */
    public VALUE unwrap(Fn1<ERROR,VALUE> errUnwrap){
        if( errUnwrap==null ) throw new IllegalArgumentException("errUnwrap==null");
        if( hasValue )return value;
        return errUnwrap.apply(error);
    }

    /**
     * Итерация по значению
     * @param consumer функция принимающая элемент
     */
    @Override
    public void each(Consumer<VALUE> consumer) {
        if( consumer==null ) throw new IllegalArgumentException("consumer==null");
        if( hasValue )consumer.accept(value);
    }

    /**
     * Итерация по значению
     * @return итератор
     */
    @Override
    public Iterator<VALUE> iterator() {
        if( hasValue )return ImList.of(value).iterator();
        return ImList.<VALUE>of().iterator();
    }

    /**
     * Преобразование успешного значения
     * @param mapper преобразователь
     * @return результат
     * @param <V> тип результата
     */
    public <V> Result<V,ERROR> map(Fn1<VALUE,V> mapper){
        if( mapper==null ) throw new IllegalArgumentException("mapper==null");
        if( hasValue )return new Result<>(mapper.apply(value),null,true);
        return new Result<>(null,error,false);
    }

    /**
     * Преобразование успешного значения
     * @param mapper преобразователь
     * @return результат
     * @param <V> тип результата
     */
    public <V> Result<V,ERROR> fmap(Fn1<VALUE,Result<V,ERROR>> mapper){
        if( mapper==null ) throw new IllegalArgumentException("mapper==null");
        if( hasValue )return mapper.apply(value);
        return new Result<>(null,error,false);
    }

    /**
     * Преобразование ошибки
     * @param mapper преобразователь
     * @return результат
     * @param <E> тип ошибки
     */
    public <E> Result<VALUE,E> mapErr(Fn1<ERROR,E> mapper){
        if( mapper==null ) throw new IllegalArgumentException("mapper==null");
        if( hasValue )return new Result<>(value,null,true);
        return new Result<>(null,mapper.apply(error),false);
    }

    /**
     * Преобразование ошибки
     * @param map преобразователь
     * @return результат
     * @param <R> тип ошибки
     */
    public <R> Result<VALUE, R> fmapErr(Function<ERROR, Result<VALUE,R>> map){
        if( map==null ) throw new IllegalArgumentException("map==null");
        return this.fold(Result::ok, map);
    }

    /**
     * Синоним {@link #getOk()}
     * @return Опциональное значение
     */
    @Deprecated
    public Optional<VALUE> toOptional(){
        return getOk();
    }

    /**
     * Свертка значения
     * @param succFold успешное значение
     * @param errFold ошибка
     * @return результат
     * @param <R> результат
     */
    public <R> R fold(Function<VALUE,R> succFold, Function<ERROR,R> errFold){
        if( succFold==null ) throw new IllegalArgumentException("succFold==null");
        if( errFold==null ) throw new IllegalArgumentException("errFold==null");
        return isOk() ? succFold.apply(value) : errFold.apply(error);
    }

    @Override
    public String toString() {
        if( hasValue )return "ok{"+value+"}";
        return "err{"+error+"}";
    }
}
