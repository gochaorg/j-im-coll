Манипуляции с record (java)
=================================

`record` (java) - это иммутабльные классы с упрощенным синтаксисом.

```java
record SomeRec<A>(String field1, A field2) {}
record OtherRec(SomeRec<Integer> other1, SomeRec<Integer> other2) {}
```

Такие структуры образуют гетерогенные, иммутабельные деревья

    OtherRec
     |
     +- other1
     |  |
     |  +-- SomeRec
     |      |
     |      +-- field1
     |      +-- field2
     |   
     +- other2
        |
        +-- SomeRec
            |
            +-- field1
            +-- field2

С ними все хорошо, за исключением, что обновление вложенного поля, влечет пересозданием дерева
И код пересоздания весьма типичен, но не масштабируемый (нельзя написать функцию, не прибегая к ухищрениям)

Для этой цели служит данная библиотека - она использует рефлексию java

Подготовим иммутабельные классы

```java
public sealed interface Node {
}

public record NodeA() implements Node {
}

public record NodeB(String a) implements Node {
}

public record NodeC(int b, Node c) implements Node {
}

public record NodeD(int b, Node c, Node d) implements Node {
}

public record NodeE(ImList<Node> nodes) implements Node {
}

public record NodeF(Optional<Node> node) implements Node {
}
```

Выполним обход и обновление

```java
public class HTreeTest {
    @Test
    public void update2() {
        // Подготавливаем данные
        var tree = new NodeE(
            ImList.of(
                new NodeF(Optional.empty()),
                new NodeF(Optional.of(
                    new NodeD(1,
                        new NodeD(2, new NodeA(), new NodeB("3")),
                        new NodeC(4, new NodeB("5"))
                    )
                )),
                new NodeF(Optional.of(new NodeB("8")))
            )
        );

        // Выполняем обход и пересоздание дерево при необходимости
        tree = HTree.visit(
            // Обновляемое дерево
            tree,
            // Класс, который содержит логику обновления/обхода 
            new Object() {
                // Метод с именем enter вызывается до входа в узел дерева
                // Данный метод ничего не возвращает, соответственно ничего не обновляет
                void enter(ImList<Nest.PathNode> path) {
                    System.out.println("enter " + ">>> ".repeat(path.size()) + path.head().get());
                }
                
                // Любые другие методы вызываются когда происходит выход из узла
                void exit(ImList<Nest.PathNode> path) {
                    System.out.println("exit  " + ">>> ".repeat(path.size()) + path.head().get());
                }
                
                // Метод, который принимает 1 аргумент с типом T (некий тип)
                // И возвращает этот же тип
                // То этот метод будет использоваться для обновления
                NodeB update(NodeB n) {
                    return new NodeB(n.a + n.a);
                }

                NodeC update(NodeC n) {
                    return new NodeC(n.b * n.b, n.c);
                }

                NodeD update(NodeD n) {
                    return new NodeD(n.b + 100, n.c, n.d);
                }

                NodeE update(NodeE n) {
                    return new NodeE(n.nodes.prepend(new NodeB("543")));
                }

                // Если обновлять не требуется, то можно вернуть этот же аргумент
                NodeF update(NodeF n) {
                    if (n.node.isEmpty()) return new NodeF(Optional.of(new NodeB("o2d")));
                    // Обновлять не требуется
                    return n;
                }

                // Обновить можно без указания конкретного типа
                UpdateResult updateByPath(ImList<Nest.PathNode> path){
                    var recOpt = path.head().flatMap( pn -> 
                        pn instanceof RecordNest.RecordIt r 
                            ? Optional.of(r) : 
                            Optional.empty() 
                    );

                    if( recOpt.isEmpty() )return UpdateResult.NoUpdate.instance;

                    var recIt = recOpt.get();
                    if( recIt.getRecordClass() == NodeC.class ){
                        if( recIt.getRecordComponent().getName().equals("b") ){
                            // Новое значение
                            return new UpdateResult.Updated( 2 );
                        }
                    }

                    // Обновление не требуется
                    return UpdateResult.NoUpdate.instance;
                }
            }
        );
    }    
}
```

Принцип работы
------------------------

`HTree.visit` - осуществляет рекурсивный обход - сверху в низ и обратно.

При обходе

- Вниз:
  - Вызывает enter методы
- Вверх
  - Пересоздает (под) дерево, при необходимости
    - Необходимость - если возвращаемое значение отличается по ссылке (по значению, в случае примитивных типов)