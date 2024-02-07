package xyz.cofe.coll.im;

import java.io.Serializable;
import java.util.Objects;

public sealed interface Tuple3<A,B,C> extends Serializable {
    A _1();
    B _2();
    C _3();

    default <RES> RES map( Fn3<A,B,C,RES> f ){
        if( f==null )throw new IllegalArgumentException("f==null");
        return f.apply(_1(), _2(), _3());
    }

    default <D> Tuple4<A,B,C,D> append(D d){
        return Tuple4.of(_1(), _2(), _3(), d);
    }

    final class Tuple3Impl<A,B,C> implements Tuple3<A,B,C> {
        public final A a;
        public final B b;
        public final C c;

        public Tuple3Impl(A a, B b, C c) {
            this.a = a;
            this.b = b;
            this.c = c;
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
        public C _3() {
            return c;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Tuple3Impl<?, ?, ?> tuple3 = (Tuple3Impl<?, ?, ?>) o;
            return Objects.equals(a, tuple3.a) && Objects.equals(b, tuple3.b) && Objects.equals(c, tuple3.c);
        }

        @Override
        public int hashCode() {
            return Objects.hash(a, b, c);
        }

        @Override
        public String toString() {
            return "("+a+", "+b+",  "+c+')';
        }
    }

    static <A,B,C> Tuple3<A,B,C> of(A a, B b, C c) {
        return new Tuple3Impl<>(a,b,c);
    }
}
