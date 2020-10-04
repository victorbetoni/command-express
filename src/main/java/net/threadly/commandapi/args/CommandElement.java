package net.threadly.commandapi.args;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.function.Function;

public class CommandElement {
    private String key;
    private Object element;
    private Class<?> type;
    private Function<Object, Boolean> check;

    public CommandElement(String key, Object element, Class<?> type, Function<Object, Boolean> check) {
        this.key = key;
        this.element = element;
        this.type = type;
        this.check = check;
    }

    public boolean check(Object passedElement) {
        return check.apply(passedElement);
    }

    public String getKey() {
        return key;
    }

    public Object getElement() {
        return element;
    }

    public Class<?> getType() {
        return type;
    }
}
