package xyz.cofe.coll.im.htree;

/**
 * Результат обновления
 */
public sealed interface UpdateResult {
    /**
     * Обновленное значение
     * @param result Обновленное значение
     */
    record Updated(Object result) implements UpdateResult {}

    /**
     * Нет обновления
     */
    final class NoUpdate implements UpdateResult {
        private NoUpdate() {}

        /**
         * Нет обновления
         */
        public final static NoUpdate instance = new NoUpdate();
    }
}
