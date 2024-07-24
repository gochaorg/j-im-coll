package xyz.cofe.coll.im.htree;

import xyz.cofe.coll.im.ImList;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Выполняя рефлексию подготавливает лямбды на основе методов, которые будут вызваны при посещении очередного узла
 * <br>
 *
 * Ищет следующие методы:
 *
 * <ul>
 *     <li>
 *         <code>B имя_метода( A )</code>,
 *         <i>где A extends B</i> <br>
 *         Данный метод будет использоваться для обновления узла дерева,
 *         где узел дерева имеет тип ? extends A или тип A
 *         <br>
 *
 *     </li>
 *
 *     <li>
 *         <code>void имя_метода( A )</code> <br>
 *         Данный метод используется только для чтения узла.
 *         Принимаемый узел соответствует ? extends A.
 *     </li>
 *
 *     <li>
 *         <code>void enter( A )</code> <br>
 *         Данный метод так же используется для чтения, но
 *         данный метод будет вызван перед посещения узла.
 *     </li>
 *
 *     <li>
 *         <code>? имя_метода( ImList&lt;Nest.PathNode&gt; )</code>
 *         Данный метод для чтения, вызывается при выходе из узла. <br>
 *         В аргументах передается путь от текущего узла к корню
 *     </li>
 *
 *     <li>
 *         <code>? enter( ImList&lt;Nest.PathNode&gt; )</code>
 *         Так же для чтения пути текущего узла, но вызывается перед входом в узел.
 *     </li>
 * </ul>
 */
public class NodeVisitor {
    private Map<Class<?>, Function<Object, Object>> oneArgUpdate = new HashMap<>();
    private Set<Class<?>> oneArgUpdateSkipped = new HashSet<>();

    private Map<Class<?>, Consumer<Object>> oneArgRead = new HashMap<>();
    private Set<Class<?>> oneArgSkipped = new HashSet<>();

    private Map<Class<?>, Consumer<Object>> oneArgReadEnter = new HashMap<>();
    private Set<Class<?>> oneArgSkippedEnter = new HashSet<>();

    private Set<Consumer<ImList<Nest.PathNode>>> pathConsumers = new HashSet<>();
    private Set<Consumer<ImList<Nest.PathNode>>> pathConsumersEnter = new HashSet<>();

    private Object visitor;

    public NodeVisitor(Object visitor0) {
        if (visitor0 == null) throw new IllegalArgumentException("visitor==null");

        this.visitor = visitor0;

        var vcls = visitor0.getClass();
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
                var genParamType = params[0].getParameterizedType();
                if (genParamType instanceof ParameterizedType pt) {
                    var rawType = pt.getRawType();
                    var typeArgs = pt.getActualTypeArguments();
                    if (rawType.getTypeName().equals(ImList.class.getName()) && typeArgs.length == 1 && typeArgs[0].getTypeName().equals(Nest.PathNode.class.getName())) {
                        var consumers = pathConsumers;
                        if (method.getName().equalsIgnoreCase("enter")) {
                            consumers = pathConsumersEnter;
                        }

                        consumers.add(path -> {
                            if (method.trySetAccessible()) {
                                try {
                                    method.invoke(visitor, path);
                                } catch (IllegalAccessException | InvocationTargetException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });

                        return;
                    }
                }

                var paramClass = params[0].getType();
                if (paramClass.isAssignableFrom(method.getReturnType())) {
                    oneArgUpdate.put(params[0].getType(), v -> {
                        if (method.trySetAccessible()) {
                            try {
                                return method.invoke(visitor, v);
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        return v;
                    });
                } else if (method.getReturnType() == void.class || method.getReturnType() == Void.class) {
                    oneArgRead.put(params[0].getType(), v -> {
                        if (method.trySetAccessible()) {
                            try {
                                method.invoke(visitor, v);
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                }
            }
        });
    }

    public void setVisitor(Object visitor){
        if( visitor==null ) throw new IllegalArgumentException("visitor==null");
        this.visitor = visitor;
    }

    public void enter(ImList<Nest.PathNode> path) {
        reading(path, pathConsumersEnter, oneArgReadEnter, oneArgSkippedEnter);
    }

    private void reading(ImList<Nest.PathNode> path, Set<Consumer<ImList<Nest.PathNode>>> pathConsumers, Map<Class<?>, Consumer<Object>> oneArgRead, Set<Class<?>> oneArgSkipped) {
        for (var cons : pathConsumers) {
            cons.accept(path);
        }

        path.head().ifPresent(head -> {
            var value = head.pathValue();
            if (value != null) {
                var handler = oneArgRead.get(value.getClass());
                if (handler != null) {
                    handler.accept(value);
                } else {
                    if (oneArgSkipped.contains(value.getClass())) return;

                    Map<Class<?>, Consumer<Object>> m = new HashMap<>();
                    for (var e : oneArgRead.entrySet()) {
                        if (e.getKey().isAssignableFrom(value.getClass())) {
                            m.put(value.getClass(), e.getValue());
                        }
                    }
                    oneArgRead.putAll(m);
                    for (var v : m.values()) {
                        v.accept(value);
                    }

                    oneArgSkipped.add(value.getClass());
                }
            }
        });
    }

    public void exit(ImList<Nest.PathNode> path) {
        reading(path, pathConsumers, oneArgRead, oneArgSkipped);
    }

    public UpdateResult update(ImList<Nest.PathNode> path) {
        var h = path.head();
        if (h.isPresent()) {
            var node = h.get();
            var value = node.pathValue();
            if (value != null) {
                var handler = oneArgUpdate.get(value.getClass());
                if (handler != null) {
                    var r = handler.apply(value);
                    if (r != value) {
                        return new UpdateResult.Updated(r);
                    }
                } else {
                    if (!oneArgUpdateSkipped.contains(value.getClass())) {
                        Map<Class<?>, Function<Object, Object>> m = new HashMap<>();
                        for (var e : oneArgUpdate.entrySet()) {
                            if (e.getKey().isAssignableFrom(value.getClass())) {
                                var f = e.getValue();
                                m.put(value.getClass(), f);
                            }
                        }
                        oneArgUpdate.putAll(m);
                        for (var v : m.values()) {
                            var r = v.apply(value);
                            if (r != value) {
                                return new UpdateResult.Updated(r);
                            }
                        }
                        oneArgUpdateSkipped.add(value.getClass());
                    }
                }
            }
        }
        return UpdateResult.NoUpdate.instance;
    }
}
