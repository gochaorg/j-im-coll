package xyz.cofe.coll.im.test;

import org.junit.jupiter.api.Test;
import xyz.cofe.coll.im.ImList;

import java.util.Arrays;
import java.util.Optional;

public class SealedCtrTest {
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

    public record NodeF(Optional<Node> node) implements Node {
    }

    @Test
    public void test1(){
        System.out.println(
            Arrays.toString(Node.class.getNestMembers())
        );

        System.out.println("-------------");

        Arrays.asList(Node.class.getPermittedSubclasses()).forEach(System.out::println);
    }
}
