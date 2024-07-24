package xyz.cofe.coll.im.htree;

public sealed interface UpdateResult {
    record Updated(Object result) implements UpdateResult {}
    final class NoUpdate implements UpdateResult {
        private NoUpdate() {}
        public final static NoUpdate instance = new NoUpdate();
    }
}
