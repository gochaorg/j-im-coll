package xyz.cofe.coll.im;

import java.io.Serializable;
import java.util.Objects;

public sealed interface Tuple4<A,B,C,D> extends Serializable {
    A _1();
    B _2();
    C _3();
    D _4();

    default <RES> RES map( Fn4<A,B,C,D,RES> f ){
        if( f==null )throw new IllegalArgumentException("f==null");
        return f.apply(_1(), _2(), _3(), _4());
    }

    default <E> Tuple5<A,B,C,D,E> append(E e){
        return Tuple5.of(_1(), _2(), _3(), _4(), e);
    }

    final class Tuple4Impl<A,B,C,D> implements Tuple4<A,B,C,D> {
        public final A a;
        public final B b;
        public final C c;
        public final D d;

        public Tuple4Impl(A a, B b, C c, D d) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
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
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Tuple4Impl<?, ?, ?, ?> tuple4 = (Tuple4Impl<?, ?, ?, ?>) o;
            return Objects.equals(a, tuple4.a) && Objects.equals(b, tuple4.b) && Objects.equals(c, tuple4.c) && Objects.equals(d, tuple4.d);
        }

        @Override
        public int hashCode() {
            return Objects.hash(a, b, c, d);
        }

        @Override
        public String toString() {
            return "("+a+", "+b+",  "+c+", "+d+')';
        }
    }

    static <A,B,C,D> Tuple4<A,B,C,D> of(A a, B b, C c, D d) {
        return new Tuple4Impl<>(a,b,c,d);
    }
}
