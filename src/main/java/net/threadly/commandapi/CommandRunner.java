package net.threadly.commandapi;

import net.threadly.commandapi.args.CommandContext;
import net.threadly.commandapi.result.CommandResult;

public interface CommandRunner {
    CommandResult execute(CommandContext context);
}
