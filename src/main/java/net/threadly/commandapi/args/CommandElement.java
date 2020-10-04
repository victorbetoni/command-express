package net.threadly.commandapi.args;

import net.threadly.commandapi.args.cast.Caster;
import net.threadly.commandapi.exception.CastNotPossibleException;

public class CommandElement<T,U> {
    private String key;
    private Caster<T,U> caster;

    public CommandElement(String key, Caster<T,U> caster) {
        this.key = key;
        this.caster = caster;
    }

    public U cast(T rawValue) throws CastNotPossibleException {
        return caster.cast(rawValue);
    }

    public String getKey() {
        return key;
    }
}
