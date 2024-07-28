package xyz.cofe.coll.im;

/**
 * Реализация списка в виде linked list
 * @param <A> тип элемента списка
 */
public class ImListLinked<A> extends ImListLinkedBase<A> {
    /**
     * Конструктор
     * @param value голова списка
     * @param next следующий список, допускается null
     */
    public ImListLinked(A value, ImListLinkedBase<A> next) {
        super(value, next);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    protected <A1> ImListLinked<A1> selfConstructor(A1 value, ImListLinkedBase<A1> next) {
        return new ImListLinked(value,next);
    }

    @Override
    public ImListLinked<A> empty() {
        return (ImListLinked<A>) super.empty();
    }

    @Override
    public ImListLinked<A> one(A a) {
        return (ImListLinked<A>) super.one(a);
    }

    @Override
    public ImListLinked<A> prepend(A a) {
        return (ImListLinked<A>)super.prepend(a);
    }

    @Override
    public ImList<A> prepend(PositionalRead<? extends A> values) {
        return (ImList<A>)super.prepend(values);
    }

    @Override
    public ImListLinked<A> reverse() {
        return (ImListLinked<A>)super.reverse();
    }

    @Override
    public ImListLinked<A> append(A a) {
        return (ImListLinked<A>)super.append(a);
    }

    @Override
    public ImListLinked<A> append(PositionalRead<? extends A> values) {
        return (ImListLinked<A>)super.append(values);
    }

    /**
     * Создание списка из указанных значений
     * @param values значения списка
     * @return список
     * @param <A> тип элемента списка
     */
    @SafeVarargs
    public static <A> ImListLinked<A> of(A ... values ){
        var lst = new ImListLinked<A>( null, null );
        for( var a : values ){
            lst = lst.prepend(a);
        }
        return lst.reverse();
    }

    /**
     * Создание списка из указанных значений
     * @param values значения списка
     * @return список
     * @param <A> тип элемента списка
     */
    public static <A> ImListLinked<A> from( Iterable<A> values ) {
        if( values==null )throw new IllegalArgumentException("values==null");
        var lst = new ImListLinked<A>( null, null );
        for( var a : values ){
            lst = lst.prepend(a);
        }
        return lst.reverse();
    }

    /**
     * Создание списка из указанных значений
     * @param values значения списка
     * @return список
     * @param <A> тип элемента списка
     */
    public static <A> ImListLinked<A> from( PositionalRead<A> values ) {
        if( values==null )throw new IllegalArgumentException("values==null");
        var lst = new ImListLinked<A>( null, null );
        lst = values.foldLeft(lst, ImListLinked::prepend);
        return lst.reverse();
    }

    @Override
    public String toString(){
        var sb = new StringBuilder();
        each(a -> {
            if(!sb.isEmpty()) sb.append(", ");
            sb.append(a!=null ? a.toString() : "null");
        });
        sb.insert(0,"[");
        sb.append("]");
        return sb.toString();
    }
}
