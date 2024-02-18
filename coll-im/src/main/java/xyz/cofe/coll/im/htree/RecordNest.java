package xyz.cofe.coll.im.htree;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.RecordComponent;
import java.util.HashMap;
import java.util.Map;

public final class RecordNest implements Nest {
    @Override
    public RecordIter enter(Object value) {
        return new RecordIter(value);
    }

    public static final class RecordIter implements Nest.NestIter {
        private final RecordIt[] recordValues;
        private final Class<?> klass;
        private final Object root;
        private int ptr = 0;

        public RecordIter(Object root) {
            if (root == null) throw new IllegalArgumentException("root==null");
            if (!root.getClass().isRecord()) throw new IllegalArgumentException("!root.getClass().isRecord()");

            this.root = root;
            this.klass = root.getClass();

            var cmts = klass.getRecordComponents();

            recordValues = new RecordIt[cmts.length];
            for( var i=0;i< recordValues.length;i++ ){
                var rc = cmts[i];
                try {
                    var fld = klass.getDeclaredField(rc.getName());
                    if( !fld.trySetAccessible() ){
                        throw new RuntimeException("can't set accessible "+fld);
                    }

                    recordValues[i] = new RecordIt(root, klass, fld.get(root), rc, fld);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        public RecordIt[] getRecordValues() {
            return recordValues;
        }

        public Class<?> getKlass() {
            return klass;
        }

        public Object getRoot() {
            return root;
        }

        @Override
        public NestIt next() {
            if( ptr>=recordValues.length )return new RecordFinish(this);
            var r = recordValues[ptr];
            ptr += 1;
            return r;
        }

        public Map<String, RecordIt> getUpdates(){
            var m = new HashMap<String, RecordIt>();
            for( var r : recordValues ){
                if( r.isUpdated() ){
                    m.put(r.recordComponent.getName(), r);
                }
            }
            return m;
        }
    }

    public static final class RecordIt implements NestItValue {
        private final Class<?> recordClass;
        private final RecordComponent recordComponent;
        private final Object fieldValue;
        private final Object recordValue;
        private final Field recordField;

        public RecordIt(Object recordValue, Class<?> recordClass, Object value, RecordComponent recordComponent, Field recordField) {
            this.recordClass = recordClass;
            this.recordComponent = recordComponent;
            this.fieldValue = value;
            this.updated = false;
            this.recordValue = recordValue;
            this.recordField = recordField;
        }

        private Object newValue;
        private boolean updated;

        public boolean isUpdated(){ return updated; }

        public Object newValue() {
            return newValue;
        }

        @Override
        public Object value() {
            return fieldValue;
        }

        @Override
        public void update(Object newValue) {
            this.newValue = newValue;
            this.updated = true;
        }

        public Class<?> getRecordClass() {
            return recordClass;
        }

        public RecordComponent getRecordComponent() {
            return recordComponent;
        }

        public Object getFieldValue() {
            return fieldValue;
        }

        public Field getRecordField() {
            return recordField;
        }

        public Object getRecordValue() {
            return recordValue;
        }

        @Override
        public PathNode withPathValue(Object value) {
            return new RecordIt(recordValue,recordClass,value,recordComponent,recordField);
        }
    }

    public static final class RecordFinish implements Nest.NestFinish {
        private final Object exitValue;

        public RecordFinish(RecordIter iter){
            if( iter==null ) throw new IllegalArgumentException("iter==null");

            var updates = iter.getUpdates();

            if( updates.isEmpty() ){
                exitValue = iter.getRoot();
            }else{
                Object[] recordValues = new Object[iter.recordValues.length];
                for( var i=0;i<recordValues.length;i++ ){
                    var r = iter.recordValues[i];
                    if( r.isUpdated() ){
                        recordValues[i] = r.newValue();
                    }else{
                        recordValues[i] = r.value();
                    }
                }

                var ctrs = iter.klass.getDeclaredConstructors();
                if( ctrs.length!=1 ){
                    throw new RuntimeException("!!");
                }

                var ctr = ctrs[0];
                try {
                    exitValue = ctr.newInstance(recordValues);
                } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        @Override
        public Object exit() {
            return exitValue;
        }
    }
}
