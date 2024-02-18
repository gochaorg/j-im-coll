package xyz.cofe.coll.im.htree;

/**
 * Обход элементов
 */
public sealed interface Nest permits ImListNest,
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
                                                       RecordNest.RecordFinish {
        Object exit();
    }
}
