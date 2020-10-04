package net.threadly.commandapi;

import net.threadly.commandapi.args.CommandContext;
import net.threadly.commandapi.result.CommandResult;
import org.bukkit.command.CommandSender;

import java.util.Map;

public interface CommandRunner {
    CommandResult execute(CommandContext context);
}
