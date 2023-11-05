Иммутабельные коллекции
=============================

Мотивация
-------------

Иммутабельные коллекции - в данном случае те, что даже не содержат мутирующие методы.
В данном случае это имеет принципиальное значение.

Наличие мутирующих методов, таких как `add` / `remove` в коллекциях как `java.util.List` или Google guava - 
вносят сумятицу в использование. 

В частности, если коллекция была создана `List.of` - по факту являются
иммутабельной, но тип `List` не сообщает об этом факте, а даже на оборот,
наличие мутирующих методов (`add` , `set`, ... и д.р.).

Наличие таких молчаливых типов дает предпосылки к написанию плохого кода, 
а так же компилятор теряет возможность проверить на наличие ошибок.

Иммутабельный список - Быстрое ознакомление
===========================================

Импортировать классы

```java
import xyz.cofe.coll.im.ImList;
```

Базовые операции

Создание
---------------------

```java
// Создание списка
// Получение размера списка
@Test
public void size_test(){
    // Создание списка из массива
    var lst = ImList.of(1,2,3);

    // Получение размера списка
    assertTrue( lst.size()==3 );
}

// Создание списка из Iterable
@Test
public void createFromIter_test(){
    // Создание списка
    var lst = ImList.of(List.of(1,2,3));

    // Получение размера списка
    assertTrue( lst.size()==3 );
}

// Создание списка ImList
@Test
public void createFromImList_test(){
    // Создание списка
    var lst = ImList.of(ImList.of(1,2,3));

    // Получение размера списка
    assertTrue( lst.size()==3 );
}
```

Чтение отдельных элементов
--------------------------

```java
// Получение первого элемента
@Test
public void first_test(){
    var lst = ImList.of(1,2,3);
    assertTrue( lst.head().map(v->v==1).orElse(false) );
}

// Получение последнего элемента
@Test
public void last_test(){
    var lst = ImList.of(1,2,3);
    assertTrue( lst.last().map(v->v==3).orElse(false) );
}

// Получение элементов по индексу
@Test
public void get_test(){
    var lst = ImList.of(1,2,3);
    assertTrue(lst.get(-1).isEmpty());
    assertTrue(lst.get(0).isPresent());
    assertTrue(lst.get(1).isPresent());
    assertTrue(lst.get(2).isPresent());
    assertTrue(lst.get(3).isEmpty());
}

// Получение хвоста списка
@Test
public void tail_test(){
    var lst = ImList.of(1,2,3);
    var res = lst.tail();
    assertTrue(res.size()==2);
    assertTrue(res.get(0).map(v->v==2).orElse(false));
    assertTrue(res.get(1).map(v->v==3).orElse(false));
}

// Нумерование элементов списка
@Test
public void enum_test(){
    var res = ImList.of("a","b","c").enumerate();
    assertTrue(res.size()==3);
    assertTrue(res.get(0).map(v->v.index()==0 && v.value().equals("a")).orElse(false));
    assertTrue(res.get(1).map(v->v.index()==1 && v.value().equals("b")).orElse(false));
    assertTrue(res.get(2).map(v->v.index()==2 && v.value().equals("c")).orElse(false));
}


// Обход списка лямбдой
@Test
public void each_test(){
    var lst = ImList.of(1,2,3);
    var sum = new AtomicInteger(0);
    lst.each(sum::addAndGet);
    assertTrue(sum.get()==6);
}

// Обход списка итератором
@Test
public void for_test(){
    var lst = ImList.of(1,2,3);
    var sum = 0;
    for( var itm : lst ){
    sum += itm;
    }
    assertTrue(sum==6);
}

// Добавление элемента в конец к клонированному списку
@Test
public void append_test(){
    var lst = ImList.of(1);
    lst = lst.append(2); // тут будет создан клон, и в конец к нему будет добавлен элемент
    assertTrue(lst.size() == 2);
    assertTrue(lst.get(0).map(v -> v==1).orElse(false));
    assertTrue(lst.get(1).map(v -> v==2).orElse(false));
}

// Добавление элементов в конец к клонированному списку
@Test
public void append_test2(){
    var lst = ImList.of(1);
    lst = lst.append(2);
    lst = lst.append(3);
    assertTrue(lst.size() == 3);
    assertTrue(lst.get(0).map(v -> v==1).orElse(false));
    assertTrue(lst.get(1).map(v -> v==2).orElse(false));
    assertTrue(lst.get(2).map(v -> v==3).orElse(false));
}

// Добавление элементов в конец к клонированному списку
@Test
public void append_test3(){
    var lst = ImList.of(1);
    lst = lst.append(2);
    lst = lst.append(3);
    lst = lst.append(4);
    assertTrue(lst.size() == 4);
    assertTrue(lst.get(0).map(v -> v==1).orElse(false));
    assertTrue(lst.get(1).map(v -> v==2).orElse(false));
    assertTrue(lst.get(2).map(v -> v==3).orElse(false));
    assertTrue(lst.get(3).map(v -> v==4).orElse(false));
}

// Добавление элементов в конец к клонированному списку
@Test
public void append_test4(){
    var lst = ImList.of(1).append(ImList.of(2,3,4));
    assertTrue(lst.size() == 4);
    assertTrue(lst.get(0).map(v -> v==1).orElse(false));
    assertTrue(lst.get(1).map(v -> v==2).orElse(false));
    assertTrue(lst.get(2).map(v -> v==3).orElse(false));
    assertTrue(lst.get(3).map(v -> v==4).orElse(false));
}

// Добавление элементов в начало к клонированному списку
@Test
public void prepend_test(){
    var lst = ImList.of(1);
    lst = lst.prepend(2);
    assertTrue(lst.size() == 2);
    assertTrue(lst.get(0).map(v -> v==2).orElse(false));
    assertTrue(lst.get(1).map(v -> v==1).orElse(false));
    }

// Добавление элементов в начало к клонированному списку
@Test
public void prepend_test2(){
    var lst = ImList.of(1).prepend(ImList.of(2,3));
    assertTrue(lst.size() == 3);
    assertTrue(lst.get(0).map(v -> v==2).orElse(false));
    assertTrue(lst.get(1).map(v -> v==3).orElse(false));
    assertTrue(lst.get(2).map(v -> v==1).orElse(false));
}

// Отображение элемента на другой элемент
@Test
public void map_test(){
    var lst = ImList.of(1,2,3);
    var ls2 = lst.map(Object::toString);
    assertTrue(lst.size() == ls2.size());
    assertTrue(ls2.get(0).map(v -> v.equals("1")).orElse(false));
    assertTrue(ls2.get(1).map(v -> v.equals("2")).orElse(false));
    assertTrue(ls2.get(2).map(v -> v.equals("3")).orElse(false));
}

// Отображение элемента на список и затем разглаживание списка
@Test
public void fmap_test(){
    var lst = ImList.of(1,2);
    var ls2 = lst.fmap(v -> ImList.of(v).append(v));
    assertTrue(ls2.size()==4);
    assertTrue(ls2.get(0).map(v->v==1).orElse(false));
    assertTrue(ls2.get(1).map(v->v==1).orElse(false));
    assertTrue(ls2.get(2).map(v->v==2).orElse(false));
    assertTrue(ls2.get(3).map(v->v==2).orElse(false));
}

sealed interface A {
    record B(int v) implements A {}
    record C(boolean v) implements A {}
}

// Отображение элемента на список и затем разглаживание списка
// В данном случае удобно, если список не является однородным по элементам
@Test
public void fmap_test_cls(){
    var lst = ImList.<A>of(new A.B(1), new A.B(2), new A.C(true));
    var res = lst.fmap(A.B.class).filter(b->b.v>1);
    assertTrue(res.size()==1);
    assertTrue(res.get(0).map(b->b.v==2).orElse(false));
}

// Свертка списка с начала к концу
@Test
public void foldLeft_test(){
    var lst = ImList.of(1,2);
    var res = lst.foldLeft(0, Integer::sum);
    assertTrue(res==3);
}

// Свертка списка с конца к началу
@Test
public void foldRight_test(){
    var l1 = ImList.of(1,2,3,4);
    var res = l1.foldRight(ImListLinked.<Integer>of(), ImListLinked::prepend);
    assertTrue(res.size()==4);
    assertTrue(res.get(0).map(v->v==1).orElse(false));
    assertTrue(res.get(1).map(v->v==2).orElse(false));
    assertTrue(res.get(2).map(v->v==3).orElse(false));
    assertTrue(res.get(3).map(v->v==4).orElse(false));
}

// Проверка наличия элемента
@Test
public void contains_test1(){
    var lst = ImList.of(1,2,3);
    var res = lst.containsAll(1);
    assertTrue(res == true);
}

// Проверка наличия элемента
@Test
public void contains_test2(){
    var lst = ImList.of(1,2,3);
    var res = lst.containsAll(1,2);
    assertTrue(res == true);
}

// Проверка наличия элемента
@Test
public void contains_test3(){
    var lst = ImList.of(1,2,3);
    var res = lst.containsAll(1,2,4);
    assertTrue(res == false);
}

// Подсчет кол-ва вхождений/совпадения элементов
@Test
public void contains_test4(){
    var lst = ImList.of(1,2,2,3,3,3);
    var res = lst.containsCount(Integer::equals,ImList.of(3));
    assertTrue(res == 3);
}

// Подсчет кол-ва вхождений/совпадения элементов
@Test
public void contains_test5(){
    var lst = ImList.of(1,2,2,3,3,3);
    var res = lst.containsCount(Integer::equals,ImList.of(2,3));
    assertTrue(res == 5);
}

// Фильтрация элементов списка
@Test
public void filter_test(){
    var lst = ImList.of(1,2,3);
    var res = lst.filter( v -> v > 2 );
    assertTrue(res.size()==1);
    assertTrue(res.get(0).map(v->v==3).orElse(false));
}

// Поиск элементов списка
@Test
public void find_test(){
    var res = ImList.of(1,2,3).find(v -> v>2);
    assertTrue(res.get() == 3);
}

// Разворот списка
@Test
public void reverse_test(){
    var res = ImList.of(1,2,3).reverse();
    assertTrue(res.size()==3);
    assertTrue(res.get(0).map(v->v==3).orElse(false));
    assertTrue(res.get(1).map(v->v==2).orElse(false));
    assertTrue(res.get(2).map(v->v==1).orElse(false));
}
```