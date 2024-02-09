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
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Обход и обновление гетерогенных деревьев
 */
public class HTree {
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

    private interface Visitor {
        void enter(Object root, RecordComponent[] recordComponents);

        void enterField(RecordComponent rc, Field field, Object value);

        void exitField();

        Object exit();

        static Visitor dummy() {
            return new Visitor() {
                @Override
                public void enter(Object root, RecordComponent[] recordComponents) {
                }

                @Override
                public void enterField(RecordComponent rc, Field field, Object value) {

                }

                @Override
                public void exitField() {

                }

                @Override
                public Object exit() {
                    return null;
                }
            };
        }
    }

    private void callConsumers(ImList<Object> stack) {
        stack.head().ifPresent(node -> {
            var klass = node.getClass();

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
            visitor.enter(root, recs);

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

            return visitor.exit();
        } else {
            return root;
        }
    }

    private Visitor readVisitor() {
        return new Visitor() {
            private ImList<Object> stack = ImList.of();

            @Override
            public void enter(Object root, RecordComponent[] recordComponents) {
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
            public Object exit() {
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

    public interface Update {
        void update(Object newValue);
    }

    private static class StackNode {
        private final Object node;
        private final RecordComponent[] recordComponents;
        private final Map<String, Object> newValues = new HashMap<>();
        private final Map<String, Supplier<Object>> newValuesSuppliers = new HashMap<>();
        private String currentField;
        private final StackNode parent;
        private Object exitValue;
        private boolean updated = false;

        public StackNode(Object node, RecordComponent[] components) {
            this.node = node;
            this.recordComponents = components;
            this.currentField = null;
            this.parent = null;
            this.exitValue = node;
        }

        public StackNode(StackNode parent, Object node, RecordComponent[] components) {
            this.node = node;
            this.recordComponents = components;
            this.currentField = null;
            this.parent = parent;
            this.exitValue = node;
        }

        public void current(RecordComponent recordComponent, Field field, Object value) {
            currentField = recordComponent.getName();
        }

        private void delayUpdate(Supplier<Object> delayedValue) {
            updated = false;
            if (currentField != null) {
                newValuesSuppliers.put(currentField, delayedValue);
            }
            if (parent != null) {
                parent.delayUpdate(() -> exitValue);
            }
        }

        private void fieldUpdate(Object newValue) {
            updated = false;
            if (currentField != null) {
                newValues.put(currentField, newValue);
            }
            if (parent != null) {
                parent.delayUpdate(() -> exitValue);
            }
        }

        public void update(Object newValue) {
            this.exitValue = newValue;
            this.updated = true;
            if (parent != null) {
                parent.fieldUpdate(newValue);
            }
        }

        private Object build() {
            if (newValues.isEmpty() && newValuesSuppliers.isEmpty()) return node;

            var ctrs = node.getClass().getDeclaredConstructors();
            if (ctrs.length != 1) throw new RuntimeException("constructors!!");

            var ctor = ctrs[0];
            var values = new Object[recordComponents.length];

            var valueIndex = -1;
            for (var rc : recordComponents) {
                valueIndex++;
                try {
                    var field = node.getClass().getDeclaredField(rc.getName());
                    var acc = field.trySetAccessible();
                    try {
                        var oldValue = field.get(node);
                        var trtValue = oldValue;
                        if (newValues.containsKey(rc.getName())) {
                            trtValue = newValues.get(rc.getName());
                        } else if (newValuesSuppliers.containsKey(rc.getName())) {
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
                if (parent != null) {
                    parent.fieldUpdate(newObj);
                }
                return newObj;
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        public Object exit() {
            if (updated) return exitValue;

            exitValue = build();
            if (exitValue != node) {
                updated = true;
            }
            return exitValue;
        }
    }

    private static class Updater implements Update,
                                            Visitor {
        private final ArrayList<StackNode> nodeStack = new ArrayList<>();
        private ImList<Object> objectStack = ImList.of();
        private HTree hTree;

        public void sethTree(HTree hTree) {
            this.hTree = hTree;
        }

        private void push(StackNode node) {
            nodeStack.add(node);
        }

        private StackNode pop() {
            return nodeStack.remove(nodeStack.size() - 1);
        }

        private Optional<StackNode> peek() {
            if (nodeStack.isEmpty()) return Optional.empty();
            return Optional.of(nodeStack.get(nodeStack.size() - 1));
        }

        public void enter(Object node, RecordComponent[] recordComponents) {
            objectStack = objectStack.prepend(node);

            peek().ifPresentOrElse(parent -> {
                push(new StackNode(parent, node, recordComponents));
            }, () -> {
                push(new StackNode(node, recordComponents));
            });
        }

        public void enterField(RecordComponent recordComponent, Field fld, Object value) {
            peek().ifPresent(sn -> sn.current(recordComponent, fld, value));
        }

        @Override
        public void update(Object newValue) {
            peek().ifPresent(sn -> sn.update(newValue));
        }

        public void exitField() {
        }

        public Object exit() {
            var ht = hTree;
            var nodeStack = peek().get();
            var resObj = nodeStack.exit();

            if (ht != null) {
                ht.callConsumers(objectStack.tail().prepend(resObj));
            }

            var nodeStack2 = pop();

            objectStack = objectStack.tail();
            resObj = nodeStack2.exit();
            return resObj;
        }
    }

    public static <A> A update(A root, Function<Update, Object> updater) {
        if (root == null) throw new IllegalArgumentException("root==null");
        if (updater == null) throw new IllegalArgumentException("updater==null");

        Updater updaterImpl = new Updater();
        var visitor = updater.apply(updaterImpl);

        var hVisitor = new HTree(visitor);
        updaterImpl.sethTree(hVisitor);

        var res = hVisitor.run(root, updaterImpl);

        //noinspection unchecked
        return (A) res;
    }
}
