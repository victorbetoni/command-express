package net.threadly.commandexpress.args.cast;

import net.threadly.commandexpress.exception.CastNotPossibleException;

public interface Caster<T, U> {

    /**
     * Cast the raw value type passed by the player (U) to the generic type T.
     *
     * @param rawValue The value passed by the player in command arguments
     **/
    U cast(T rawValue) throws CastNotPossibleException;
}
