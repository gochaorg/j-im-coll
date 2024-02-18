package xyz.cofe.coll.im.htree;

import xyz.cofe.coll.im.ImList;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public class NodeVisitor {
    private Map<Class<?>, Function<Object,Object>> oneArgUpdate = new HashMap<>();
    private Map<Class<?>, Consumer<Object>> oneArgRead = new HashMap<>();
    private Set<Consumer<ImList<Nest.PathNode>>> pathConsumers = new HashSet<>();

    public NodeVisitor(Object visitor){
        if( visitor!=null ){
            var vcls = visitor.getClass();
            var visited = new HashSet<Method>();

            Stream.concat(
                Arrays.stream(vcls.getDeclaredMethods()),
                Arrays.stream(vcls.getMethods())
            ).forEach(method -> {
                if (visited.contains(method)) return;
                visited.add(method);

                if ((method.getModifiers() & Modifier.PRIVATE) > 0) return;

                var params = method.getParameters();
                if( params.length==1 ){
                    var paramClass = params[0].getType();
                    if( paramClass.isAssignableFrom(method.getReturnType()) ){
                        oneArgUpdate.put(params[0].getType(), v -> {
                            if( method.trySetAccessible() ){
                                try {
                                    return method.invoke(visitor, v);
                                } catch (IllegalAccessException | InvocationTargetException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            return v;
                        });
                    }



                    var genParamType = params[0].getParameterizedType();
                    if( genParamType instanceof ParameterizedType pt ){
                        var rawType = pt.getRawType();
                        var typeArgs = pt.getActualTypeArguments();
                        if( rawType.getTypeName().equals(ImList.class.getName()) && typeArgs.length==1 && typeArgs[0].getTypeName().equals(Nest.PathNode.class.getName()) ){
                            pathConsumers.add( path -> {
                                if( method.trySetAccessible() ){
                                    try {
                                        method.invoke(visitor, path);
                                    } catch (IllegalAccessException | InvocationTargetException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            });
                        }
                    }
                }
            });
        }
    }

    public void enter(ImList<Nest.PathNode> path){
    }

    public void exit(ImList<Nest.PathNode> path){
        for( var cons : pathConsumers ){
            cons.accept(path);
        }

        path.head().ifPresent( head -> {
            var value = head.pathValue();
            if( value!=null ){
                var handler = oneArgRead.get(value.getClass());
                if( handler!=null ){
                    handler.accept(value);
                }
            }
        });
    }

    public UpdateResult update(ImList<Nest.PathNode> path){
        var h = path.head();
        if( h.isPresent() ){
            var node = h.get();
            var value = node.pathValue();
            if( value!=null ){
                var handler = oneArgUpdate.get(value.getClass());
                if( handler!=null ){
                    var r = handler.apply(value);
                    if( r!=value ){
                        return new UpdateResult.Updated(r);
                    }
                }
            }
        }
        return UpdateResult.NoUpdate.instance;
    }
}
