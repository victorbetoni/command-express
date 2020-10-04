package net.threadly.commandapi.args;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CommandContext {
    private Map<String, Object> arguments = new HashMap<>();
    private CommandSender sender;
    private long timeExecuted;

    public CommandContext(Map<String, Object> arguments, CommandSender sender, long timeExecuted) {
        this.arguments = arguments;
        this.sender = sender;
        this.timeExecuted = timeExecuted;
    }

    public <T> T getArgumentAs(String key, Class<T> type) {
        return (T) arguments.get(key);
    }

    public Map<String, Object> getArguments() {
        return arguments;
    }

    public CommandSender getSender() {
        return sender;
    }

    public long getTimeExecuted() {
        return timeExecuted;
    }
}
