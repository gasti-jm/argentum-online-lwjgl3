package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;

import static org.aoclient.network.protocol.Protocol.requestCharSkills;
import static org.aoclient.network.protocol.command.metadata.GameCommand.SHOW_PLAYER_SKILLS;

public class RequestCharSkillsCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, getCommandUsage(SHOW_PLAYER_SKILLS));
        String nick = commandContext.getArgument(0);
        requestCharSkills(nick);
    }

}
