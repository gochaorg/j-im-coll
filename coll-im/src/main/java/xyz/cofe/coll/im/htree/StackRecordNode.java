package xyz.cofe.coll.im.htree;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.RecordComponent;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class StackRecordNode implements StackNode {
    private final Object node;
    private final RecordComponent[] recordComponents;
    private final Map<String, Object> newValues = new HashMap<>();
    private final Map<String, Supplier<Object>> newValuesSuppliers = new HashMap<>();
    private String currentField;
    private final StackRecordNode parent;
    private Object exitValue;
    private boolean updated = false;

    public StackRecordNode(Object node, RecordComponent[] components) {
        this.node = node;
        this.recordComponents = components;
        this.currentField = null;
        this.parent = null;
        this.exitValue = node;
    }

    public StackRecordNode(StackRecordNode parent, Object node, RecordComponent[] components) {
        this.node = node;
        this.recordComponents = components;
        this.currentField = null;
        this.parent = parent;
        this.exitValue = node;
    }

    public void current(RecordComponent recordComponent, Field field, Object value) {
        currentField = recordComponent.getName();
    }

    private void delayUpdate(Supplier<Object> delayedValue) {
        updated = false;
        if (currentField != null) {
            newValuesSuppliers.put(currentField, delayedValue);
        }
        if (parent != null) {
            parent.delayUpdate(() -> exitValue);
        }
    }

    private void fieldUpdate(Object newValue) {
        updated = false;
        if (currentField != null) {
            newValues.put(currentField, newValue);
        }
        if (parent != null) {
            parent.delayUpdate(() -> exitValue);
        }
    }

    public void update(Object newValue) {
        this.exitValue = newValue;
        this.updated = true;
        if (parent != null) {
            parent.fieldUpdate(newValue);
        }
    }

    private Object build() {
        if (newValues.isEmpty() && newValuesSuppliers.isEmpty()) return node;

        var ctrs = node.getClass().getDeclaredConstructors();
        if (ctrs.length != 1) throw new RuntimeException("constructors!!");

        var ctor = ctrs[0];
        var values = new Object[recordComponents.length];

        var valueIndex = -1;
        for (var rc : recordComponents) {
            valueIndex++;
            try {
                var field = node.getClass().getDeclaredField(rc.getName());
                var acc = field.trySetAccessible();
                try {
                    var oldValue = field.get(node);
                    var trtValue = oldValue;
                    if (newValues.containsKey(rc.getName())) {
                        trtValue = newValues.get(rc.getName());
                    } else if (newValuesSuppliers.containsKey(rc.getName())) {
                        trtValue = newValuesSuppliers.get(rc.getName()).get();
                    }
                    values[valueIndex] = trtValue;
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            var newObj = ctor.newInstance(values);
            if (parent != null) {
                parent.fieldUpdate(newObj);
            }
            return newObj;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public Object exit() {
        if (updated) return exitValue;

        exitValue = build();
        if (exitValue != node) {
            updated = true;
        }
        return exitValue;
    }
}
