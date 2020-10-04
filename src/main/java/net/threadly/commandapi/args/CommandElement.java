package net.threadly.commandapi.args;

import net.threadly.commandapi.args.cast.Caster;
import net.threadly.commandapi.exception.CastNotPossibleException;


public class CommandElement<T,U> {
    private String key;
    private Caster<T,U> caster;
    private boolean isJoinString = false;

    public CommandElement(String key, boolean isJoinString, Caster<T, U> caster) {
        this.key = key;
        this.isJoinString = isJoinString;
        this.caster = caster;
    }

    public U cast(T rawValue) throws CastNotPossibleException {
        return caster.cast(rawValue);
    }

    public String getKey() {
        return key;
    }

    public boolean isJoinString() { return isJoinString; }
}
