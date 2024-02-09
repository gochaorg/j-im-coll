package xyz.cofe.coll.im.test;

import org.junit.jupiter.api.Test;
import xyz.cofe.coll.im.HTree;

import java.util.concurrent.atomic.AtomicInteger;

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

    @Test
    public void visit() {
        var na = new NodeA();
        var nb = new NodeB("3");
        var nc = new NodeC(2, nb);

        Node root = new NodeD(1,
            nc,
            na
        );

        AtomicInteger matchCount = new AtomicInteger(0);

        HTree.read(root, new Object() {
            public void accept1(NodeA n) {
                if (n == na) matchCount.incrementAndGet();
                System.out.println("accept1 " + n);
            }

            void accept2(NodeB n) {
                if (n == nb) matchCount.incrementAndGet();
                System.out.println("accept2 " + n);
            }

            public void accept3(NodeC n) {
                if (n == nc) matchCount.incrementAndGet();
                System.out.println("accept3 " + n);
            }

            public void accept4(NodeD n) {
                if (root == n) matchCount.incrementAndGet();
                System.out.println("accept4 " + n);
            }
        });

        assertTrue(matchCount.get() >= 4);
    }

    @Test
    public void update() {
        Node root = new NodeD(1,
            new NodeC(2, new NodeB("3")),
            new NodeC(4,
                new NodeC(5,
                    new NodeC(8,
                        new NodeB("6"))))
        );

        System.out.println("before");
        System.out.println(root);

        System.out.println("update");
        var newRoot = HTree.update(root, up -> new Object() {
            public void accept(NodeB nb) {
                if (nb.a.equalsIgnoreCase("3")) {
                    var n = new NodeB("3-updated");
                    System.out.println("updating " + nb + " -> " + n);
                    up.update(n);
                }
                if (nb.a.equalsIgnoreCase("6")) {
                    var n = new NodeB("6-updated");
                    System.out.println("updating " + nb + " -> " + n);
                    up.update(n);
                }
            }

            public void accept(NodeC nc) {
                var n = new NodeC(55, nc.c);
                if (nc.b == 5) {
                    System.out.println("updating " + nc + " -> " + n);
                    up.update(n);
                }
            }

            public void accept(Node n){
                System.out.println("visit "+n);
            }
        });

        System.out.println("after");
        System.out.println(newRoot);

        var matchCnt = new AtomicInteger(0);

        HTree.read(newRoot, new Object() {
            void node(NodeB nb) {
                if (nb.a.equalsIgnoreCase("3-updated")) matchCnt.incrementAndGet();
                if (nb.a.equalsIgnoreCase("6-updated")) matchCnt.incrementAndGet();
            }

            void node(NodeC nc) {
                if (nc.b == 55) matchCnt.incrementAndGet();
            }
        });

        assertTrue(matchCnt.get() >= 3);
    }
}
