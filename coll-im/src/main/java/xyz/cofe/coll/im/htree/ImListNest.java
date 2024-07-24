package xyz.cofe.coll.im.htree;

import xyz.cofe.coll.im.ImList;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public final class ImListNest implements Nest {
    @Override
    public NestIter enter(Object value) {
        if( !(value instanceof ImList<?>) ) throw new IllegalArgumentException("!(value instanceof ImList<?>) ");
        var imLst = (ImList)value;
        return new ImListIter(new ArrayList<>(), imLst);
    }

    public final class ImListIter implements NestIter, NestFinish {
        private int updateCount;
        private final List<Object> list;
        private final ImList imList;

        public ImListIter(List<Object> list, ImList imList) {
            this.list = list;
            this.imList = imList;
            this.updateCount = 0;
            for( var v : imList ){
                list.add(v);
            }
        }

        private int ptr = 0;

        @Override
        public NestIt next() {
            if( ptr>=list.size() )return this;
            var p = ptr;
            ptr++;
            return new ImListItValue(list.get(p), p, (idx,newValue) -> {
                list.set(idx,newValue);
                updateCount++;
            });
        }

        @Override
        public Object exit() {
            if( updateCount<1 )return imList;
            return ImList.from(list);
        }
    }

    public final class ImListItValue implements NestItValue {
        private final Object value;
        private final int index;
        private final BiConsumer<Integer,Object> update;
        private boolean updated;
        private Object newValue;

        public ImListItValue(Object value, int index, BiConsumer<Integer,Object> update){
            this.value = value;
            this.index = index;
            this.update = update;
            this.updated = false;
            this.newValue = null;
        }

        public int getIndex() {
            return index;
        }

        @Override
        public Object value() {
            return value;
        }

        @Override
        public void update(Object newValue) {
            update.accept(index, newValue);
        }

        @Override
        public PathNode withPathValue(Object value) {
            return new ImListItValue(value,index,(_skip1,_skip2)->{});
        }

        @Override
        public String toString() {
            if( updated ){
                return "["+index+"]={"+value+" -> "+newValue+"}";
            }
            return "["+index+"]="+value;
        }
    }
}
