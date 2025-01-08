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

        /**
         * Переход к следующему элементу
         *
         * @return значение типа NestItValue - очередной элемент из итератора
         * <br>
         * значение типа NestFinish - обход завершен
         */
        NestIt next();
    }

    /**
     * Элемент пути в дереве
     */
    sealed interface PathNode {
        /**
         * Значение узла дерева
         *
         * @return значение
         */
        Object pathValue();

        /**
         * Создает клон элемента, с указанным значением
         *
         * @param value значение
         * @return клон
         */
        PathNode withPathValue(Object value);
    }

    /**
     * Начало пути
     *
     * @param pathValue значение
     */
    record RootPathNode(Object pathValue) implements PathNode {
        @Override
        public RootPathNode withPathValue(Object value) {
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
        default Object pathValue() {
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
