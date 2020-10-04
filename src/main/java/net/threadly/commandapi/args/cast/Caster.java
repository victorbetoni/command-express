package net.threadly.commandapi.args.cast;

import net.threadly.commandapi.exception.CastNotPossibleException;

public interface Caster<T, U> {

    /**
     * Cast the raw value passed by the player (String) to the generic type.
     * In this context, the T represents any type passed as parameter to be converted
     * and the U, the returned value type.
     *
     * @param rawValue The value passed by the player in command arguments
     **/
    U cast(T rawValue) throws CastNotPossibleException;
}
