package xyz.cofe.coll.im.htree;

import xyz.cofe.coll.im.HTreeUpdate;
import xyz.cofe.coll.im.ImList;

import java.lang.reflect.Field;
import java.lang.reflect.RecordComponent;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Consumer;

public class Updater implements HTreeUpdate,
                                Visitor
{
    private Consumer<ImList<Object>> consumer;
    private final ArrayList<StackRecordNode> nodeStack = new ArrayList<>();
    private ImList<Object> objectStack = ImList.of();

    public void setConsumer(Consumer<ImList<Object>> consumer){
        this.consumer = consumer;
    }

    private void push(StackRecordNode node) {
        nodeStack.add(node);
    }

    private StackRecordNode pop() {
        return nodeStack.remove(nodeStack.size() - 1);
    }

    private Optional<StackRecordNode> peek() {
        if (nodeStack.isEmpty()) return Optional.empty();
        return Optional.of(nodeStack.get(nodeStack.size() - 1));
    }

    public void enterRecord(Object node, RecordComponent[] recordComponents) {
        objectStack = objectStack.prepend(node);

        peek().ifPresentOrElse(parent -> {
            push(new StackRecordNode(parent, node, recordComponents));
        }, () -> {
            push(new StackRecordNode(node, recordComponents));
        });
    }

    public Object exitRecord() {
        var nodeStack = peek().get();
        var resObj = nodeStack.exit();

        var consumer = this.consumer;
        if (consumer != null) {
            consumer.accept(objectStack.tail().prepend(resObj));
        }

        var nodeStack2 = pop();

        objectStack = objectStack.tail();
        resObj = nodeStack2.exit();
        return resObj;
    }

    public void enterField(RecordComponent recordComponent, Field fld, Object value) {
        peek().ifPresent(sn -> sn.current(recordComponent, fld, value));
    }

    public void exitField() {
    }

    @Override
    public void update(Object newValue) {
        peek().ifPresent(sn -> sn.update(newValue));
    }
}
