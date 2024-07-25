package xyz.cofe.coll.im.iter;

import xyz.cofe.coll.im.ImList;

import java.util.Objects;

public record TreePath<A>(ImList<A> reversePath) {
    public TreePath {
        Objects.requireNonNull(reversePath);
        if( reversePath.size()<=0 ) throw new IllegalArgumentException("reversePath.isEmpty()");
    }

    public static <A> TreePath<A> first(A elem){
        return new TreePath<>(ImList.of(elem));
    }

    public TreePath<A> next(A elem){
        return new TreePath<>( reversePath.prepend(elem) );
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public A node(){ return reversePath.head().get(); }

    public String toString(){
        return
            "["+reversePath
            .map( a -> a!=null ? a.toString() : "null" )
            .foldLeft("", (acc,it)-> !acc.isEmpty() ? acc+", "+it : it )
            +"]";
    }
}
