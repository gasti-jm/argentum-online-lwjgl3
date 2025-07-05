package org.aoclient.network.protocol.command.handlers.guild;

import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;

import static org.aoclient.network.protocol.Protocol.guildFundate;

public class GuildFundateCommand extends BaseCommandHandler {

    private static final int MIN_LEVEL_REQUIRED = 25;

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        if (user.getUserLvl() < MIN_LEVEL_REQUIRED)
            showError("To found a clan you must be level 25 and have 90 leadership skills.");
        guildFundate();
    }

}
