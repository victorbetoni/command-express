package net.threadly.commandexpress.args;

import net.threadly.commandexpress.args.cast.Caster;
import net.threadly.commandexpress.exception.CastNotPossibleException;
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

    public static CommandElement<String, OfflinePlayer> offlinePlayer(String name) {
        return new CommandElement<>(name, false, new Caster<String, OfflinePlayer>() {
            @Override
            public OfflinePlayer cast(String rawValue) {
                return Bukkit.getOfflinePlayer(rawValue);
            }
        });
    }

    public static CommandElement<String, Player> onlinePlayer(final String nick) {
        return new CommandElement<>(nick, false, new Caster<String, Player>() {
            @Override
            public Player cast(String passedArgument) throws CastNotPossibleException {
                return Bukkit.getPlayer(nick);
            }
        });
    }

    public static CommandElement<String, String> string(String key) {
        return new CommandElement<>(key, false, new Caster<String, String>() {
            @Override
            public String cast(String passedArgument) throws CastNotPossibleException {
                return (String) passedArgument;
            }
        });
    }

    public static CommandElement<String, Integer> integer(String key) {
        return new CommandElement<>(key, false, new Caster<String, Integer>() {
            @Override
            public Integer cast(String passedArgument) throws CastNotPossibleException {
                try {
                    return Integer.valueOf(passedArgument);
                } catch (ClassCastException ex) {
                    throw new CastNotPossibleException();
                }
            }
        });
    }

    public static CommandElement<String, Boolean> bool(String key) {
        return new CommandElement<>(key, false, new Caster<String, Boolean>() {
            @Override
            public Boolean cast(String passedArgument) throws CastNotPossibleException {
                try {
                    return Boolean.valueOf(passedArgument);
                } catch (ClassCastException ex) {
                    throw new CastNotPossibleException();
                }
            }
        });
    }

    public static CommandElement<String[], String> joinString(String key) {
        return new CommandElement<>(key, true, new Caster<String[], String>() {
            @Override
            public String cast(String[] passedArgument) throws CastNotPossibleException {
                String string = "";
                for (String s : passedArgument) {
                    string += s + " ";
                }
                return string.trim();
            }
        });
    }
}
