package net.threadly.commandapi.args;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CommandContext {
    private Map<String, CommandElement> arguments = new HashMap<>();
    private CommandSender executer;
    private long timeExecuted;

    public Map<String, CommandElement> getArguments() {
        return arguments;
    }

    public CommandSender getExecuter() {
        return executer;
    }

    public long getTimeExecuted() {
        return timeExecuted;
    }
}
