package xyz.cofe.coll.im;

import java.io.Serializable;
import java.util.Objects;

public sealed interface Tuple5<A,B,C,D,E> extends Serializable {
    A _1();
    B _2();
    C _3();
    D _4();
    E _5();

    default <RES> RES map( Fn5<RES,A,B,C,D,E> f ){
        if( f==null )throw new IllegalArgumentException("f==null");
        return f.apply(_1(), _2(), _3(), _4(), _5());
    }

    final class Tuple5Impl<A,B,C,D,E> implements Tuple5<A,B,C,D,E> {
        public final A a;
        public final B b;
        public final C c;
        public final D d;
        public final E e;

        public Tuple5Impl(A a, B b, C c, D d, E e) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.e = e;
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
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Tuple5Impl<?, ?, ?, ?, ?> tuple5 = (Tuple5Impl<?, ?, ?, ?, ?>) o;
            return Objects.equals(a, tuple5.a) && Objects.equals(b, tuple5.b) && Objects.equals(c, tuple5.c) && Objects.equals(d, tuple5.d) && Objects.equals(e, tuple5.e);
        }

        @Override
        public int hashCode() {
            return Objects.hash(a, b, c, d, e);
        }

        @Override
        public String toString() {
            return "("+a+", "+b+",  "+c+", "+d+", "+e+')';
        }
    }

    static <A,B,C,D,E> Tuple5<A,B,C,D,E> of(A a, B b, C c, D d,E e) {
        return new Tuple5Impl<>(a,b,c,d,e);
    }
}
