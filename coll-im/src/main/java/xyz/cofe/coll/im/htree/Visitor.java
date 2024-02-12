package xyz.cofe.coll.im.htree;

import java.lang.reflect.Field;
import java.lang.reflect.RecordComponent;

public interface Visitor {
    default void enterIterable(Iterable root) {}
    default Object exitIterable() {return null;}
    default void enterRecord(Object root, RecordComponent[] recordComponents){}
    default Object exitRecord(){return  null;}
    default void enterField(RecordComponent rc, Field field, Object value){}
    default void exitField(){}
    default void enterIndexedValue(int index,Object value) {}
    default void exitIndexedValue(){}

    static Visitor dummy() {
        return new Visitor() {
        };
    }
}
