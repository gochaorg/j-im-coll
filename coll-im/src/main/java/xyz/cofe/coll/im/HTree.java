package xyz.cofe.coll.im;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.RecordComponent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class HTree {
    private final Map<Class<?>, List<Consumer<Object>>> nodeConsumers = new HashMap<>();

    private HTree(Object visitor){
        var vcls = visitor.getClass();
        var visited = new HashSet<Method>();
        Stream.concat(
            Arrays.stream(vcls.getDeclaredMethods()),
            Arrays.stream(vcls.getMethods())
        ).forEach( method -> {
            if(visited.contains(method))return;
            visited.add(method);

            if( (method.getModifiers() & Modifier.PRIVATE)>0 )return;

            var params = method.getParameters();
            if( params.length==1 ){
                var param = params[0];
                Consumer<Object> consumer = value -> {
                    try {
                        if( method.trySetAccessible() ) {
                            method.invoke(visitor, value);
                        }else{
                            throw new RuntimeException("!!");
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                };
                nodeConsumers.computeIfAbsent(param.getType(), _1 -> new ArrayList<>()).add(consumer);
            }
        });
    }

    private void doRead(Object root){
        var rcls = root.getClass();
        if( rcls.isRecord() ){
            var nc = nodeConsumers.get(rcls);
            if( nc!=null ){
                nc.forEach(c -> c.accept(root));
            }

            for( var rc : rcls.getRecordComponents() ){
                try {
                    Field fld = rcls.getDeclaredField(rc.getName());
                    if( fld.trySetAccessible() ){
                        try {
                            var value = fld.get(root);
                            if( value!=null ) {
                                doRead(value);
                            }
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } catch (NoSuchFieldException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static void read(Object root, Object visitor){
        if( root==null ) throw new IllegalArgumentException("root==null");
        if( visitor==null ) throw new IllegalArgumentException("visitor==null");

        var hvisitor = new HTree(visitor);
        hvisitor.doRead(root);
    }

    public interface Update {
        void update(Object newValue);
    }

    private static class StackNode {
        private final Object node;
        private final RecordComponent[] recordComponents;
        private final Map<String,Object> newValues = new HashMap<>();
        private final Map<String, Supplier<Object>> newValuesSuppliers = new HashMap<>();
        private String currentField;
        private final StackNode parent;
        private Object exitValue;

        public StackNode(Object node, RecordComponent[] components){
            this.node = node;
            this.recordComponents = components;
            this.currentField = null;
            this.parent = null;
            this.exitValue = this;
        }

        public StackNode(StackNode parent, Object node, RecordComponent[] components){
            this.node = node;
            this.recordComponents = components;
            this.currentField = null;
            this.parent = parent;
            this.exitValue = this;
        }

        public void current(RecordComponent recordComponent, Field field, Object value){
            currentField = recordComponent.getName();
        }

        private void delayUpdate(Supplier<Object> delayedValue){
            if( currentField!=null ){
                newValuesSuppliers.put(currentField, delayedValue);
            }
            if( parent!=null ){
                parent.delayUpdate(()->exitValue);
            }
        }

        private void fieldUpdate(Object newValue){
            if( currentField!=null ){
                newValues.put(currentField, newValue);
            }
            if( parent!=null ){
                parent.delayUpdate(()->exitValue);
            }
        }

        public void update(Object newValue){
            if( parent!=null ){
                parent.fieldUpdate(newValue);
            }
        }

        public Object exit(){
            if(newValues.isEmpty() && newValuesSuppliers.isEmpty())return node;

            var ctrs = node.getClass().getDeclaredConstructors();
            if( ctrs.length!=1 )throw new RuntimeException("constructors!!");

            var ctor = ctrs[0];
            var values = new Object[recordComponents.length];

            var valueIndex = -1;
            for( var rc : recordComponents ){
                valueIndex++;
                try {
                    var field = node.getClass().getDeclaredField(rc.getName());
                    var acc = field.trySetAccessible();
                    try {
                        var oldValue = field.get(node);
                        var trtValue = oldValue;
                        if( newValues.containsKey(rc.getName()) ){
                            trtValue = newValues.get(rc.getName());
                        }else if( newValuesSuppliers.containsKey(rc.getName()) ){
                            trtValue = newValuesSuppliers.get(rc.getName()).get();
                        }
                        values[valueIndex] = trtValue;
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                } catch (NoSuchFieldException e) {
                    throw new RuntimeException(e);
                }
            }

            try {
                var newObj = ctor.newInstance(values);
                this.exitValue = newObj;
                if( parent!=null ){
                    parent.update(newObj);
                }
                return newObj;
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class Updater implements Update {
        private final ArrayList<StackNode> stack = new ArrayList<>();

        private void push(StackNode node){
            stack.add(node);
        }

        private StackNode pop(){
            return stack.remove(stack.size()-1);
        }

        private Optional<StackNode> peek(){
            if( stack.isEmpty() )return Optional.empty();
            return Optional.of(stack.get(stack.size()-1));
        }

        public void enter(Object node, RecordComponent[] recordComponents){
            peek().ifPresentOrElse(parent -> {
                push(new StackNode(parent,node,recordComponents));
            }, () -> {
                push(new StackNode(node,recordComponents));
            });
        }

        public void enterField(RecordComponent recordComponent, Field fld, Object value){
            peek().ifPresent(sn -> sn.current(recordComponent,fld,value));
        }

        @Override
        public void update(Object newValue) {
            peek().ifPresent(sn -> sn.update(newValue));
        }

        public void exitField(){
        }

        public Object exit(){
            return pop().exit();
        }
    }

    private Object doUpdate(Object root, Updater updater){
        var rcls = root.getClass();
        if( rcls.isRecord() ){
            var recs = rcls.getRecordComponents();
            updater.enter(root,recs);

            var nc = nodeConsumers.get(rcls);
            if( nc!=null ){
                nc.forEach(c -> c.accept(root));
            }

            for( var rc : recs ){
                try {
                    Field fld = rcls.getDeclaredField(rc.getName());
                    if( fld.trySetAccessible() ){
                        try {
                            var value = fld.get(root);
                            if( value!=null ) {
                                updater.enterField(rc, fld, value);
                                doUpdate(value, updater);
                                updater.exitField();
                            }
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } catch (NoSuchFieldException e) {
                    throw new RuntimeException(e);
                }
            }

            return updater.exit();
        }else{
            return root;
        }
    }

    public static <A> A update(A root, Function<Update,Object> updater){
        if( root==null ) throw new IllegalArgumentException("root==null");
        if( updater==null ) throw new IllegalArgumentException("updater==null");

        Updater updaterImpl = new Updater();
        var visitor = updater.apply(updaterImpl);
        var hVisitor = new HTree(visitor);

        //noinspection unchecked
        return (A)hVisitor.doUpdate(root, updaterImpl);
    }
}
