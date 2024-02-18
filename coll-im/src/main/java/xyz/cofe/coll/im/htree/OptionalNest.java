package xyz.cofe.coll.im.htree;

import java.util.Optional;

public final class OptionalNest implements Nest {
    @Override
    public OptionalIter enter(Object value) {
        if( value==null ) throw new IllegalArgumentException("value==null");
        if( !(value instanceof Optional<?>) )throw new IllegalArgumentException("!(value instanceof Optional<?>)");

        //noinspection rawtypes
        return new OptionalIter((Optional) value);
    }

    public static final class OptionalIter implements NestIter, NestFinish {
        @SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "rawtypes"})
        private final Optional optional;

        @SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "rawtypes"})
        public OptionalIter(Optional optional) {
            this.optional = optional;
        }

        private int ptr = 0;

        @SuppressWarnings("rawtypes")
        public Optional getOptional() {
            return optional;
        }

        @Override
        public NestIt next() {
            if( ptr>0 )return this;
            ptr++;
            return new OptionalValue(optional, this);
        }

        private Object newInnerValue = null;
        private boolean updatedValue = false;

        @Override
        public Object exit() {
            if( !updatedValue )return optional;
            return newInnerValue==null ? Optional.empty() : Optional.of(newInnerValue);
        }

        public void updateInnerValue(Object newValue){
            updatedValue = true;
            newInnerValue = newValue;
        }
    }

    public static final class OptionalValue implements NestItValue {
        private final Object value;
        private Object newValue;
        private boolean updated;
        private final OptionalIter source;

        public OptionalValue(Object value, OptionalIter source) {
            this.source = source;
            this.value = value;
            updated = false;
            newValue = null;
        }

        public Object getNewValue() {
            return newValue;
        }

        public boolean isUpdated() {
            return updated;
        }

        @Override
        public PathNode withPathValue(Object value) {
            return new OptionalValue(value, null);
        }

        @Override
        public Object value() {
            return value;
        }

        @Override
        public void update(Object newValue) {
            updated = true;
            this.newValue = newValue;
            if( source!=null )source.updateInnerValue(newValue);
        }

        @Override
        public String toString() {
            if( updated )return "Optional{"+value+" -> "+newValue+"}";
            return "Optional{"+value+"}";
        }
    }
}
