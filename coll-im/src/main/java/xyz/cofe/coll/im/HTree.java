package xyz.cofe.coll.im;

import xyz.cofe.coll.im.htree.Updater;
import xyz.cofe.coll.im.htree.Visitor;

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
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Обход и обновление гетерогенных деревьев
 */
public class HTree {
    // TODO Подумать над кешем
    private final Map<Class<?>, List<Consumer<Object>>> nodeConsumers = new HashMap<>();

    private HTree(Object visitor) {
        var vcls = visitor.getClass();
        var visited = new HashSet<Method>();

        Stream.concat(
            Arrays.stream(vcls.getDeclaredMethods()),
            Arrays.stream(vcls.getMethods())
        ).forEach(method -> {
            if (visited.contains(method)) return;
            visited.add(method);

            if ((method.getModifiers() & Modifier.PRIVATE) > 0) return;

            var params = method.getParameters();
            if (params.length == 1) {
                var param = params[0];
                Consumer<Object> consumer = value -> {
                    try {
                        if (method.trySetAccessible()) {
                            method.invoke(visitor, value);
                        } else {
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

    private void callConsumers(ImList<Object> stack) {
        stack.head().ifPresent(node -> {
            var klass = node.getClass();

            // TODO Подумать над кешем, чтоб не перебирать
            for (var en : nodeConsumers.entrySet()) {
                var acceptKlass = en.getKey();
                if (acceptKlass.isAssignableFrom(klass)) {
                    var consumers = en.getValue();
                    consumers.forEach(c -> c.accept(node));
                }
            }
        });
    }

    private Object run(Object root, Visitor visitor) {
        var rcls = root.getClass();
        if (rcls.isRecord()) {
            var recs = rcls.getRecordComponents();
            visitor.enterRecord(root, recs);

            for (var rc : recs) {
                try {
                    Field fld = rcls.getDeclaredField(rc.getName());
                    if (fld.trySetAccessible()) {
                        try {
                            var value = fld.get(root);
                            if (value != null) {
                                visitor.enterField(rc, fld, value);
                                run(value, visitor);
                                visitor.exitField();
                            }
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } catch (NoSuchFieldException e) {
                    throw new RuntimeException(e);
                }
            }

            return visitor.exitRecord();
        } else if(root instanceof Iterable<?> iterable ){
            var idx = -1;
            for( var itm : iterable ){
                idx++;
                visitor.enterIndexedValue(idx,itm);
                run(itm,visitor);
                visitor.exitIndexedValue();
            }
            return root;
        } else {
            return root;
        }
    }

    private Visitor readVisitor() {
        return new Visitor() {
            private ImList<Object> stack = ImList.of();

            @Override
            public void enterRecord(Object root, RecordComponent[] recordComponents) {
                stack = stack.prepend(root);
                callConsumers(stack);
            }

            @Override
            public void enterField(RecordComponent rc, Field field, Object value) {

            }

            @Override
            public void exitField() {

            }

            @Override
            public Object exitRecord() {
                stack = stack.tail();
                return null;
            }
        };
    }

    public static void read(Object root, Object visitor) {
        if (root == null) throw new IllegalArgumentException("root==null");
        if (visitor == null) throw new IllegalArgumentException("visitor==null");

        var ht = new HTree(visitor);
        ht.run(root, ht.readVisitor());
    }

    public static <A> A update(A root, Function<HTreeUpdate, Object> updater) {
        if (root == null) throw new IllegalArgumentException("root==null");
        if (updater == null) throw new IllegalArgumentException("updater==null");

        Updater updaterImpl = new Updater();
        var visitor = updater.apply(updaterImpl);

        var hVisitor = new HTree(visitor);
        updaterImpl.setConsumer(hVisitor::callConsumers);

        var res = hVisitor.run(root, updaterImpl);

        //noinspection unchecked
        return (A) res;
    }
}
