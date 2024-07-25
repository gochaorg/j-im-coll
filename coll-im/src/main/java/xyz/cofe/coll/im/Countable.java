package xyz.cofe.coll.im;

/**
 * Счетное множество/список
 */
public interface Countable {
    /**
     * Возвращает кол-во элементов
     * @return кол-во элементов
     */
    int size();

    /**
     * Возвращает true - если список пустой
     * @return true - если список пустой
     */
    default boolean isEmpty(){ return size()==0; }

    /**
     * Возвращает true - если список не пустой
     * @return true - если список не пустой
     */
    default boolean isNonEmpty() {return size() > 0;}
}
