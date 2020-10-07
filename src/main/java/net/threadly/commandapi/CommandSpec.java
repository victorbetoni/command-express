package net.threadly.commandapi;

import net.threadly.commandapi.args.CommandElement;

import javax.annotation.Nullable;
import java.util.*;

public class CommandSpec {
    private String alias;
    private CommandRunner executor;
    private boolean playerOnly;
    private Optional<String> permission;
    private Optional<Set<CommandSpec>> childs;
    private Optional<CommandElement[]> arguments;
    private Optional<CommandSpec> belonger = Optional.empty();

    public static class Builder {
        private String alias;
        private CommandRunner executor;
        private boolean playerOnly = false;

        @Nullable
        private String permission;

        @Nullable
        private Set<CommandSpec> childs;

        @Nullable
        private CommandElement[] arguments;

        public Builder alias(String alias){
            this.alias = alias;
            return this;
        }

        public Builder playerOnly() {
            this.playerOnly = !playerOnly;
            return this;
        }

        public Builder permission(String permission){
            this.permission = permission;
            return this;
        }

        public Builder child(CommandSpec spec){
            if(childs == null) childs = new HashSet<>();
            childs.add(spec);
            return this;
        }

        public Builder executor(CommandRunner runner){
            this.executor = runner;
            return this;
        }

        public Builder arguments(CommandElement... arguments){
            this.arguments = arguments;
            return this;
        }

        public CommandSpec build() {
            final CommandSpec spec = new CommandSpec(alias, executor, playerOnly, arguments, permission, childs);
            spec.getChilds().ifPresent(commandSpecs -> commandSpecs.forEach(x -> x.setBelonger(spec)));
            return spec;
        }

    }

    public CommandSpec(String alias, CommandRunner executor, boolean playerOnly, @Nullable CommandElement[] arguments, @Nullable String permission, @Nullable Set<CommandSpec> childs) {
        this.alias = alias;
        this.executor = executor;
        this.childs = Optional.ofNullable(childs);
        this.arguments = Optional.ofNullable(arguments);
        this.permission = Optional.ofNullable(permission);
        this.playerOnly = playerOnly;
    }

    public static Builder builder(){
        return new Builder();
    }

    public String getAlias() {
        return alias;
    }

    public Optional<Set<CommandSpec>> getChilds() {
        return childs;
    }

    public CommandRunner getExecutor() {
        return executor;
    }

    public Optional<CommandElement[]> getArguments() { return arguments; }

    public Optional<CommandSpec> getBelonger(){
        return belonger;
    }

    public void setBelonger(CommandSpec spec){
        this.belonger = Optional.of(spec);
    }

    public boolean isPlayerOnly() {
        return playerOnly;
    }

    public Optional<String> getPermission() {
        return permission;
    }
}
