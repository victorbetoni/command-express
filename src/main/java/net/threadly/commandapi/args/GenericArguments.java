package net.threadly.commandapi.args;

import net.threadly.commandapi.args.cast.Caster;
import net.threadly.commandapi.exception.CastNotPossibleException;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public final class GenericArguments {

    public static class Builder<T,U> {
        private String key;
        private Caster<T,U> caster;
        private boolean isJoinString = false;

        public Builder<T,U> joinString() {
            this.isJoinString = !isJoinString;
            return this;
        }

        public Builder<T,U> caster(Caster<T,U> caster) {
            this.caster = caster;
            return this;
        }

        public Builder<T,U> key(String key){
            this.key = key;
            return this;
        }

        public CommandElement<T,U> build() {
            return new CommandElement<>(key, isJoinString, caster);
        }
    }

    public static <T,U> Builder<T,U> builder() {
        return new Builder<>();
    }

    public static CommandElement<String, OfflinePlayer> offlinePlayer(String key) {
        return new CommandElement<>(key, false, (passedArgument) -> {
            return Bukkit.getOfflinePlayer(UUID.fromString(passedArgument));
        });
    }

    public static CommandElement<String, Player> onlinePlayer(String key) {
        return new CommandElement<>(key, false, (passedArgument) -> {
            Optional<Player> player = Optional.ofNullable(Bukkit.getPlayer(UUID.fromString(passedArgument)));
            player.orElseThrow(CastNotPossibleException::new);
            return player.get();
        });
    }

    public static CommandElement<String, String> string(String key) {
        return new CommandElement<>(key, false, (passedArgument) -> (String) passedArgument);
    }

    public static CommandElement<String, Integer> integer(String key) {
        return new CommandElement<>(key, false, (passedArgument) -> {
            try {
                return Integer.valueOf(passedArgument);
            } catch (ClassCastException ex) {
                throw new CastNotPossibleException();
            }
        });
    }

    public static CommandElement<String, Boolean> bool(String key) {
        return new CommandElement<>(key, false, (passedArgument) -> {
            try{
                return Boolean.valueOf(passedArgument);
            }catch (ClassCastException ex) {
                throw new CastNotPossibleException();
            }
        });
    }

    public static CommandElement<String[], String> joinString(String key) {
        return new CommandElement<>(key, true, (passedArgument) -> {
            return Arrays.stream(passedArgument).map(String::valueOf).collect(Collectors.joining(" ")).trim();
        });
    }
}
