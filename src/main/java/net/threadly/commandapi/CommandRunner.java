package net.threadly.commandapi;

import org.bukkit.command.CommandSender;

import java.util.Map;

public interface CommandRunner {
    CommandResult execute(CommandSender sender, Map<String, String> args);
}
