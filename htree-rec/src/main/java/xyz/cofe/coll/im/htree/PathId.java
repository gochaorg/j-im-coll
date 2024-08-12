package xyz.cofe.coll.im.htree;

import xyz.cofe.coll.im.ImList;

/**
 * Идентификатор пути
 * @param path
 */
public record PathId(String path) {
    public static PathId from(ImList<Nest.PathNode> path) {
        if (path == null) throw new IllegalArgumentException("path==null");

        return new PathId(path.map(pathNode -> {
            if (pathNode instanceof Nest.RootPathNode) {
                return "$";
            } else if (pathNode instanceof RecordNest.RecordIt r) {
                return r.getRecordComponent().getName();
            } else if (pathNode instanceof ImListNest.ImListItValue iv) {
                return "" + iv.getIndex();
            } else if (pathNode instanceof OptionalNest.OptionalValue) {
                return "%";
            }
            return "";
        }).reverse().foldLeft("", (sum, it) -> sum.isBlank() ? it : sum + "/" + it));
    }
}
