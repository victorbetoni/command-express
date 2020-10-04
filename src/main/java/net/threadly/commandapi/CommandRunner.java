package net.threadly.commandapi;

import net.threadly.commandapi.result.CommandResult;
import org.bukkit.command.CommandSender;

import java.util.Map;

public interface CommandRunner {
    CommandResult execute(CommandSender sender, Map<String, String> args);
}
