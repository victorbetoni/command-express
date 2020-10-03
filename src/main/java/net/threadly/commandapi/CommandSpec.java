package net.threadly.commandapi;

import javax.annotation.Nonnull;
import java.util.*;

public class CommandSpec {
    private final String alias;
    private final CommandRunner executor;
    private Set<CommandSpec> childs = new HashSet<>();
    private List<String> arguments = new ArrayList<>();
    private boolean playerOnly;
    private Optional<CommandSpec> belongsTo = Optional.empty();
    private Optional<String> permission = Optional.empty();

    public static class Builder {
        private String alias;
        private Set<CommandSpec> childs = new HashSet<>();
        private CommandRunner executor;
        private List<String> arguments = new ArrayList<>();
        private boolean playerOnly = false;
        private Optional<String> permission;

        public Builder alias(String alias){
            this.alias = alias;
            return this;
        }

        public Builder playerOnly() {
            this.playerOnly = true;
            return this;
        }

        public Builder permission(String permission){
            this.permission = Optional.of(permission);
            return this;
        }

        public Builder child(CommandSpec spec){
            childs.add(spec);
            return this;
        }

        public Builder executor(CommandRunner runner){
            this.executor = runner;
            return this;
        }

        public Builder arguments(String... arguments){
            this.arguments = Arrays.asList(arguments);
            return this;
        }

        public CommandSpec build() {
            CommandSpec spec = new CommandSpec(alias, childs, executor, arguments, playerOnly, permission);
            for(CommandSpec cmd : childs){
                spec.setBelongsTo(spec);
            }
            return spec;
        }

    }

    public CommandSpec(String alias, Set<CommandSpec> childs, CommandRunner executor, List<String> arguments, boolean playerOnly, Optional<String> permission) {
        this.alias = alias;
        this.childs = childs;
        this.executor = executor;
        this.arguments = arguments;
        this.permission = permission;
        this.playerOnly = playerOnly;
    }

    public static Builder builder(){
        return new Builder();
    }

    public String getAlias() {
        return alias;
    }

    public Set<CommandSpec> getChilds() {
        return childs;
    }

    public CommandRunner getExecutor() {
        return executor;
    }

    public List<String> getArguments() {
        return arguments;
    }

    public Optional<CommandSpec> getBelongsTo(){
        return belongsTo;
    }

    public void setBelongsTo(CommandSpec spec){
        this.belongsTo = Optional.of(spec);
    }

    public boolean isPlayerOnly() {
        return playerOnly;
    }

    public Optional<String> getPermission() {
        return permission;
    }
}
