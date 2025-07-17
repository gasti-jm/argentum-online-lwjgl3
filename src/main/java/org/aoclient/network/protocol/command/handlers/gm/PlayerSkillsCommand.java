package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;
import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;

import static org.aoclient.network.protocol.Protocol.playerSkills;
import static org.aoclient.network.protocol.command.metadata.GameCommand.PLAYER_SKILLS;

public class PlayerSkillsCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, getCommandUsage(PLAYER_SKILLS));
        String player = commandContext.getArgument(0);
        playerSkills(player);
    }

}
