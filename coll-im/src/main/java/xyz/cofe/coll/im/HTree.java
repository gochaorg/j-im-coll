package xyz.cofe.coll.im;

import xyz.cofe.coll.im.htree.ImListNest;
import xyz.cofe.coll.im.htree.Nest;
import xyz.cofe.coll.im.htree.NodeVisitor;
import xyz.cofe.coll.im.htree.OptionalNest;
import xyz.cofe.coll.im.htree.RecordNest;
import xyz.cofe.coll.im.htree.UpdateResult;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Обход и обновление гетерогенных деревьев
 *
 * <p>
 * Системное свойство: <code>HTree.visitorCacheEnabled</code>, по умолчанию <code>false</code> - указывает,
 * кешировать или нет информацию о методах визитера
 * </p>
 *
 * В общем случае выглядит так
 *
 * <pre>
 * HTree.visit(root, new Object() {
 *     // обновление конкретного типа узла
 *     NodeB node(NodeB n) {
 *         return new NodeB(n.a + n.a);
 *     }
 *
 *     // обновление обобщенного типа узла
 *     Node node2(Node n) {
 *         if (n instanceof NodeC nc && nc.b==4) {
 *             return new NodeC(44, nc.c);
 *         }
 *         return n;
 *     }
 *
 *     // вход в узел
 *     void enter(ImList&lt;Nest.PathNode&gt; path) {
 *         System.out.println("enter " + "&gt;&gt;&gt; ".repeat(path.size()) + path.head().get());
 *     }
 *
 *     // выход из узла
 *     void show(ImList&lt;Nest.PathNode&gt; path) {
 *         System.out.println("exit  " + "&gt;&gt;&gt; ".repeat(path.size()) + path.head().get());
 *     }
 * })
 * </pre>
 */
public class HTree {
    private static Optional<Nest> nestOf(Object root) {
        if (root == null) return Optional.empty();

        Class<?> klass = root.getClass();
        if (klass.isRecord()) {
            return Optional.of(new RecordNest());
        }

        if (root instanceof ImList<?>) {
            return Optional.of(new ImListNest());
        }

        if (root instanceof Optional<?>) {
            return Optional.of(new OptionalNest());
        }

        return Optional.empty();
    }

    private final static Map<Class<?>, NodeVisitor> visitorCache = new HashMap<>();

    /**
     * Обход дерева и обновление узлов
     *
     * @param root корень дерева
     * @param visitor объект для посещения и обновления узлов {@link NodeVisitor}
     * @return обновленное или старое дерево
     * @param <A> тип дерева
     */
    public static <A> A visit(A root, Object visitor) {
        if (root == null) throw new IllegalArgumentException("root==null");
        if (visitor == null) throw new IllegalArgumentException("visitor==null");

        boolean cacheEnabled = System.getProperty("HTree.visitorCacheEnabled", "false").equalsIgnoreCase("true");
        NodeVisitor nv = cacheEnabled
            ? visitorCache.computeIfAbsent(visitor.getClass(), x -> new NodeVisitor(visitor))
            : new NodeVisitor(visitor);

        if (cacheEnabled) nv.setVisitor(visitor);

        return visit(root, ImList.of(new Nest.RootPathNode(root)), nv);
    }

    private static UpdateResult update(Object value, ImList<Nest.PathNode> path, NodeVisitor nodeVisitor) {
        return nodeVisitor.update(path);
    }

    private static <A> A visit(A root, ImList<Nest.PathNode> path, NodeVisitor nodeVisitor) {
        if (path == null) throw new IllegalArgumentException("path==null");

        nodeVisitor.enter(path);

        var nestOpt = nestOf(root);
        if (nestOpt.isEmpty()) {
            // случай лист - терминальный узел
            var updateResult = update(root, path, nodeVisitor);
            if (updateResult == UpdateResult.NoUpdate.instance) {
                nodeVisitor.exit(path);
                return root;
            } else if (updateResult instanceof UpdateResult.Updated up) {
                var a = (A) up.result();
                path.head().ifPresent(h -> {
                    nodeVisitor.exit(
                        path.tail().prepend(h.withPathValue(a))
                    );
                });
                return a;
            }
        }

        var nest = nestOpt.get();
        var iter = nest.enter(root);
        var result = root;

        while (true) {
            var itm = iter.next();
            if (itm instanceof Nest.NestItValue nv) {
                var maybeUpdated = visit(nv.value(), path.prepend(nv), nodeVisitor);
                if (maybeUpdated != nv.value()) {
                    // обновлен лист/узел
                    nv.update(maybeUpdated);
                }
            } else if (itm instanceof Nest.NestFinish nf) {
                result = (A) nf.exit();
                break;
            }
        }

        var updateResult = update(result, path, nodeVisitor);
        if (updateResult == UpdateResult.NoUpdate.instance) {
            var aResult = result;
            path.head().ifPresent(h -> {
                nodeVisitor.exit(
                    path.tail().prepend(h.withPathValue(aResult))
                );
            });
            return result;
        } else if (updateResult instanceof UpdateResult.Updated up) {
            var aResult = (A) up.result();
            path.head().ifPresent(h -> {
                nodeVisitor.exit(
                    path.tail().prepend(h.withPathValue(aResult))
                );
            });
            return aResult;
        }

        var aResult = result;
        path.head().ifPresent(h -> {
            nodeVisitor.exit(
                path.tail().prepend(h.withPathValue(aResult))
            );
        });
        return result;
    }
}
