package org.aoclient.network.protocol.command.handlers.user;

import org.aoclient.engine.game.console.Console;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;
import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;

/**
 * Limpia la consola.
 */

public class ClearCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        Console.INSTANCE.clearConsole();
    }

}
