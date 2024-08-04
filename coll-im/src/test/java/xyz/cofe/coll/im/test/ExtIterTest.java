package xyz.cofe.coll.im.test;

import org.junit.jupiter.api.Test;
import xyz.cofe.coll.im.Either;
import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.iter.EmptyIterator;
import xyz.cofe.coll.im.iter.ExtIterable;
import xyz.cofe.coll.im.iter.SingleIterator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("SimplifiableAssertion")
public class ExtIterTest {
    @Test
    public void construct1empty() {
        int cnt = 0;
        for (var e : ExtIterable.of()) {
            cnt++;
        }
        assertTrue(cnt == 0);
    }

    @Test
    public void construct2one() {
        int cnt = 0;
        for (var e : ExtIterable.of(1)) {
            cnt++;
            assertTrue(Objects.equals(e, (Integer) 1));
        }
        assertTrue(cnt == 1);
    }

    @Test
    public void construct3many() {
        int cnt = 0;
        for (var e : ExtIterable.of(1, 2)) {
            cnt++;
            if (cnt == 1) assertTrue(Objects.equals(e, 1));
            if (cnt == 2) assertTrue(Objects.equals(e, 2));
        }
        assertTrue(cnt == 2);
    }

    @Test
    public void filter() {
        int cnt = 0;
        for (var e : ExtIterable.of(1, 2, 3, 4, 5).filter(v -> v > 2)) {
            cnt++;
            if (cnt == 1) assertTrue(Objects.equals(e, 3));
            if (cnt == 2) assertTrue(Objects.equals(e, 4));
            if (cnt == 3) assertTrue(Objects.equals(e, 5));
        }
        assertTrue(cnt == 3);
    }

    @Test
    public void toList() {
        var lst1 = ExtIterable.of(1, 2, 3).toList();
        assertTrue(lst1.size() == 3);
        assertTrue(Objects.equals(lst1.get(0), 1));
        assertTrue(Objects.equals(lst1.get(1), 2));
        assertTrue(Objects.equals(lst1.get(2), 3));

        var lst2 = ExtIterable.of(1, 2, 3).toImList();
        assertTrue(lst2.size() == 3);
        assertTrue(lst2.get(0).map(v -> v == 1).orElse(false));
        assertTrue(lst2.get(1).map(v -> v == 2).orElse(false));
        assertTrue(lst2.get(2).map(v -> v == 3).orElse(false));
    }

    @Test
    public void appendTest() {
        var lst = ExtIterable.of(1, 2).append(ExtIterable.of(3, 4)).toList();
        assertTrue(Objects.equals(lst, List.of(1, 2, 3, 4)));
    }

    @Test
    public void prependTest() {
        var lst = ExtIterable.of(1, 2).prepend(ExtIterable.of(3, 4)).toList();
        assertTrue(Objects.equals(lst, List.of(3, 4, 1, 2)));
    }

    @Test
    public void mapTest() {
        var lst = ExtIterable.of(1, 2, 3).map(Object::toString).toList();
        assertTrue(Objects.equals(lst, List.of("1", "2", "3")));
    }

    @Test
    public void fmapTest() {
        var lst = ExtIterable.of(1, 2, 3).fmap(v ->
            v > 1
                ? v == 2
                ? // Заменяем на 1 элемент
                SingleIterator.iterate(v.toString()).iterator()
                : // Заменяем на 2 элемента
                ImList.of("4", "5").iterator()
                : // Заменяем на 0 элементов, вырезаем
                EmptyIterator.<String>iterate().iterator()
        ).toList();
        assertTrue(Objects.equals(lst, List.of("2", "4", "5")));
    }

    @Test
    public void enumerate() {
        int cnt = 0;
        for (var e : ExtIterable.of(1, 2, 3).enumerate()) {
            cnt++;
            if (cnt == 1) {
                assertTrue(e.index() == 0);
                assertTrue(Objects.equals(e.value(), 1));
            }
            if (cnt == 2) {
                assertTrue(e.index() == 1);
                assertTrue(Objects.equals(e.value(), 2));
            }
            if (cnt == 3) {
                assertTrue(e.index() == 2);
                assertTrue(Objects.equals(e.value(), 3));
            }
        }
        assertTrue(cnt == 3);
    }

    @Test
    public void take() {
        var cnt = 0;
        for (var e : ExtIterable.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 0).take(3)) {
            cnt++;
        }
        assertTrue(cnt == 3);
    }

    @Test
    public void skip() {
        var cnt = 0;
        for (var e : ExtIterable.of(1, 2, 3, 4, 5, 6).skip(3)) {
            cnt++;
            if (cnt == 1) assertTrue(Objects.equals(e, 4));
            if (cnt == 2) assertTrue(Objects.equals(e, 5));
            if (cnt == 3) assertTrue(Objects.equals(e, 6));
        }
        assertTrue(cnt == 3);
    }

    @SuppressWarnings("Convert2MethodRef")
    @Test
    public void induction() {
        var expect = new ArrayList<>(List.of(1, 1, 2, 3, 5, 8, 13, 21, 34, 55));
        for (var n : ExtIterable.induction(1, 1, (a, b) -> a + b).take(10)) {
            System.out.println(n);
            var e = expect.remove(0);
            assertTrue(Objects.equals(e, n));
        }
    }


    @Test
    public void finitiveInduction() {
        var expect = new ArrayList<>(List.of(1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89));
        for (var n : ExtIterable.finitiveInduction(1, 1, (a, b) -> a + b > 100 ? Optional.empty() : Optional.of(a + b))) {
            System.out.println(n);
            var e = expect.remove(0);
            assertTrue(Objects.equals(e, n));
        }
    }

    @Test
    public void production() {
        var cnt = 0;
        for (var e : ExtIterable.of(1, 2).product(ExtIterable.of(3, 4))) {
            cnt++;
            if (cnt == 1) assertTrue(e.left() == 1 && e.right() == 3);
            if (cnt == 2) assertTrue(e.left() == 1 && e.right() == 4);
            if (cnt == 3) assertTrue(e.left() == 2 && e.right() == 3);
            if (cnt == 4) assertTrue(e.left() == 2 && e.right() == 4);
        }
        assertTrue(cnt == 4);
    }

    @Test
    public void unzip() {
        var src = ExtIterable.induction(1, a -> a + 1).take(10);
        var split = src.unzipIterators(a -> a % 2 == 0 ? Either.left(a) : Either.right(a));

        var leftExpect = new ArrayList<>(List.of(2, 4, 6, 8, 10));
        while (split.left().hasNext()) {
            var l = split.left().next();
            System.out.println("left: " + l);
            assertTrue(Objects.equals(leftExpect.remove(0), l));
        }

        var rightExpect = new ArrayList<>(List.of(1, 3, 5, 7, 9));
        while (split.right().hasNext()) {
            var r = split.right().next();
            System.out.println("right: " + r);
            assertTrue(Objects.equals(rightExpect.remove(0), r));
        }
    }

    @Test
    public void zip() {
        int cnt = 0;
        for (var p : ExtIterable.of(1, 2, 3).zip(ExtIterable.of("a", "b"))) {
            cnt++;
            if (cnt == 1) assertTrue(p.left() == 1 && Objects.equals(p.right(), "a"));
            if (cnt == 2) assertTrue(p.left() == 2 && Objects.equals(p.right(), "b"));
        }
        assertTrue(cnt == 2);
    }
}
