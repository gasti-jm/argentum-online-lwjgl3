package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeRequestCharSkills;
import static org.aoclient.network.protocol.command.GameCommand.SHOW_PLAYER_SKILLS;

public class RequestCharSkillsCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, SHOW_PLAYER_SKILLS.getCommand() + " <nick>");
        String nick = commandContext.getArgument(0);
        writeRequestCharSkills(nick);
    }

}
