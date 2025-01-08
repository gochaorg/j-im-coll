package xyz.cofe.coll.im.htree;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.Tuple2;

import java.util.Objects;

/**
 * Идентификатор пути
 */
public class PathId {
    private final ImList<Nest.PathNode> path;

    public PathId(ImList<Nest.PathNode> path){
        if( path==null ) throw new IllegalArgumentException("path==null");
        this.path = path;
    }

    public static String nameOf(Nest.PathNode pathNode){
        if( pathNode==null ) throw new IllegalArgumentException("pathNode==null");
        if (pathNode instanceof Nest.RootPathNode) {
            return "/";
        } else if (pathNode instanceof RecordNest.RecordIt r) {
            return r.getRecordComponent().getName();
        } else if (pathNode instanceof ImListNest.ImListItValue iv) {
            return "" + iv.getIndex();
        } else if (pathNode instanceof OptionalNest.OptionalValue) {
            return ".";
        }
        throw new RuntimeException("Bug!!");
    }

    public static PathId from(ImList<Nest.PathNode> path) {
        if (path == null) throw new IllegalArgumentException("path==null");
        return new PathId(path);
    }

    private ImList<String> names;
    public ImList<String> names(){
        if( names!=null )return names;
        names = path.map(PathId::nameOf).reverse();
        return names;
    }

    private String string;

    @Override
    public String toString() {
        if( string!=null )return string;

        string = names().foldLeft(
            Tuple2.of("",0),
            (sum, it) ->
                sum._2() <= 0
                    ? Tuple2.of(it, sum._2()+1)
                    : Tuple2.of(sum._1() + "/" + it,sum._2()+1)
        )._1();

        return string;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return toString().equals( o.toString() );
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(toString());
    }
}
