package org.aoclient.network.protocol.command;

import org.aoclient.engine.game.Console;
import org.aoclient.engine.game.User;

public interface CommandHandler {

    User user = User.INSTANCE;
    Console console = Console.INSTANCE;
    CommandValidator validator = CommandProcessor.INSTANCE.validator;

    void handle(CommandContext context) throws CommandException;

}
