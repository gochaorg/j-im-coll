package xyz.cofe.coll.im.htree;

/**
 * Обход элементов
 */
public sealed interface Nest permits ImListNest,
                                     OptionalNest,
                                     RecordNest {
    /**
     * Вход в объект
     *
     * @param value входная точка
     * @return Итератор
     */
    NestIter enter(Object value);

    /**
     * Итератор по элементам
     */
    sealed interface NestIter permits ImListNest.ImListIter,
                                      OptionalNest.OptionalIter,
                                      RecordNest.RecordIter {
        NestIt next();
    }

    sealed interface PathNode {
        Object pathValue();
        PathNode withPathValue(Object value);
    }

    record RootPathNode(Object pathValue) implements PathNode {
        @Override
        public PathNode withPathValue(Object value) {
            return new RootPathNode(value);
        }
    }

    /**
     * Элемент итератора
     */
    sealed interface NestIt {
    }

    /**
     * Есть элемент
     */
    sealed interface NestItValue extends NestIt,
                                         PathNode permits ImListNest.ImListItValue,
                                                          OptionalNest.OptionalValue,
                                                          RecordNest.RecordIt {
        Object value();

        void update(Object newValue);

        @Override
        default Object pathValue(){
            return value();
        }
    }

    /**
     * Обход завершен
     */
    sealed interface NestFinish extends NestIt permits ImListNest.ImListIter,
                                                       OptionalNest.OptionalIter,
                                                       RecordNest.RecordFinish {
        Object exit();
    }
}
