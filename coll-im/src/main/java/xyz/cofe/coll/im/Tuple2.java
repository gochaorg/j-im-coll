package xyz.cofe.coll.im;

import java.io.Serializable;
import java.util.Objects;

public sealed interface Tuple2<A, B> extends Serializable {
    A _1();

    B _2();

    default <RES> RES map(Fn2<A, B, RES> f) {
        if (f == null) throw new IllegalArgumentException("f==null");
        return f.apply(_1(), _2());
    }

    default <C> Tuple3<A, B, C> append(C c) {
        return Tuple3.of(_1(), _2(), c);
    }

    final class Tuple2Impl<A, B> implements Tuple2<A, B> {
        public final A a;
        public final B b;

        public Tuple2Impl(A a, B b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public A _1() {
            return a;
        }

        @Override
        public B _2() {
            return b;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Tuple2Impl<?, ?> tuple2 = (Tuple2Impl<?, ?>) o;
            return Objects.equals(a, tuple2.a) && Objects.equals(b, tuple2.b);
        }

        @Override
        public int hashCode() {
            return Objects.hash(a, b);
        }

        @Override
        public String toString() {
            return "("+a+", "+b+')';
        }
    }

    static <A, B> Tuple2<A, B> of(A a, B b) {
        return new Tuple2Impl<>(a, b);
    }
}
