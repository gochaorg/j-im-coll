package xyz.cofe.coll.im;

import java.io.Serializable;
import java.util.Objects;

public sealed interface Tuple6<A,B,C,D,E,F> extends Serializable {
    A _1();
    B _2();
    C _3();
    D _4();
    E _5();
    F _6();

    default <RES> RES map( Fn6<RES,A,B,C,D,E,F> f ){
        if( f==null )throw new IllegalArgumentException("f==null");
        return f.apply(_1(), _2(), _3(), _4(), _5(), _6());
    }

    final class Tuple6Impl<A,B,C,D,E,F> implements Tuple6<A,B,C,D,E,F> {
        public final A a;
        public final B b;
        public final C c;
        public final D d;
        public final E e;
        public final F f;

        public Tuple6Impl(A a, B b, C c, D d, E e, F f) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.e = e;
            this.f = f;
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
        public D _4() {
            return d;
        }

        @Override
        public E _5() {
            return e;
        }

        @Override
        public F _6() {
            return f;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Tuple6Impl<?, ?, ?, ?, ?, ?> tuple6 = (Tuple6Impl<?, ?, ?, ?, ?, ?>) o;
            return Objects.equals(a, tuple6.a) && Objects.equals(b, tuple6.b) && Objects.equals(c, tuple6.c) && Objects.equals(d, tuple6.d) && Objects.equals(e, tuple6.e) && Objects.equals(f, tuple6.f);
        }

        @Override
        public int hashCode() {
            return Objects.hash(a, b, c, d, e, f);
        }

        @Override
        public String toString() {
            return "("+a+", "+b+",  "+c+", "+d+", "+e+", "+f+')';
        }
    }

    static <A,B,C,D,E,F> Tuple6<A,B,C,D,E,F> of(A a, B b, C c, D d, E e, F f) {
        return new Tuple6Impl<>(a, b, c, d, e, f);
    }
}
