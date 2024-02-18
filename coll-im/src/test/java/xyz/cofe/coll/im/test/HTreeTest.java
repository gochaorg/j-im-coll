package xyz.cofe.coll.im.test;

import org.junit.jupiter.api.Test;
import xyz.cofe.coll.im.HTree;
import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.htree.Nest;
import xyz.cofe.coll.im.htree.RecordNest;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class HTreeTest {
    public sealed interface Node {
    }

    public record NodeA() implements Node {
    }

    public record NodeB(String a) implements Node {
    }

    public record NodeC(int b, Node c) implements Node {
    }

    public record NodeD(int b, Node c, Node d) implements Node {
    }

    public record NodeE(ImList<Node> nodes) implements Node {
    }

    @Test
    public void recordNest() {
        var nest = new RecordNest().enter(new NodeB("aa"));
        var a = nest.next();
        if (a instanceof Nest.NestItValue nv) {
            System.out.println(nv.value());
            nv.update("bb");
        }

        var b = nest.next();
        if (b instanceof Nest.NestFinish nf) {
            System.out.println(nf.exit());
        }
    }

    @Test
    public void update() {
        var n2 = new NodeA();
        var n3 = new NodeC(4, n2);
        var n4 = new NodeB("3");
        var n5 = new NodeC(2, n4);
        var n1 = new NodeD(1,
            n5,
            n3
        );
        var n7 = new NodeB("7");
        var n6 = new NodeE(
            ImList.of(n7, n1)
        );

        var n66 = HTree.visit(n6, new Object() {
            NodeB node(NodeB n) {
                return new NodeB(n.a + n.a);
            }

            Node node2(Node n) {
                if (n instanceof NodeC nc && nc.b==4) {
                    return new NodeC(44, nc.c);
                }
                return n;
            }
        });

        System.out.println("------------------------");
        HTree.visit(n66, new Object() {
            void show(ImList<Nest.PathNode> path) {
                System.out.println("path " + path);
            }
        });

        assertTrue(n66.nodes.size()>1);
        assertTrue(n66.nodes.get(0).get().equals(new NodeB("77")) );

        assertTrue(n66.nodes.get(1).get() instanceof NodeD u1 );
        var u1 = (NodeD)n66.nodes.get(1).get();

        assertTrue(u1.c instanceof NodeC);
        var u2 = (NodeC)u1.c;

        assertTrue(u2.c instanceof NodeB nb && nb.a.equalsIgnoreCase("33"));
        assertTrue(u1.d instanceof NodeC nc && nc.b == 44);
    }

    @Test
    public void visit() {
        var n2 = new NodeA();
        var n3 = new NodeC(4, n2);
        var n4 = new NodeB("3");
        var n5 = new NodeC(2, n4);
        var n1 = new NodeD(1,
            n5,
            n3
        );
        var n7 = new NodeB("7");
        var n6 = new NodeE(
            ImList.of(n7, n1)
        );

        HTree.visit(n6, new Object() {
            void enter(ImList<Nest.PathNode> path) {
                System.out.println("enter " + ">>> ".repeat(path.size()) + path.head().get());
            }
            void show(ImList<Nest.PathNode> path) {
                System.out.println("exit  " + ">>> ".repeat(path.size()) + path.head().get());
            }
        });
    }
}
