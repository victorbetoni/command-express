package net.threadly.commandapi.args;

import net.threadly.commandapi.args.cast.Caster;
import net.threadly.commandapi.exception.CastNotPossibleException;

public class CommandElement<T> {
    private String key;
    private Caster<T> caster;

    public CommandElement(String key, Caster<T> caster) {
        this.key = key;
        this.caster = caster;
    }

    public T cast(String rawValue) throws CastNotPossibleException {
        return caster.cast(rawValue);
    }

    public String getKey() {
        return key;
    }
}
