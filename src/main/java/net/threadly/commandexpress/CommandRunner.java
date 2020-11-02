package net.threadly.commandexpress;

import net.threadly.commandexpress.args.CommandContext;
import net.threadly.commandexpress.result.CommandResult;

public interface CommandRunner {
    CommandResult execute(CommandContext context);
}
