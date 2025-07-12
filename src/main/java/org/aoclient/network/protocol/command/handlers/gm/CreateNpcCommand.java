package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;

import static org.aoclient.network.protocol.Protocol.createNPC;
import static org.aoclient.network.protocol.command.metadata.GameCommand.CREATE_NPC;

public class CreateNpcCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, getCommandUsage(CREATE_NPC));
        requireInteger(commandContext, 0, "npc");
        short npc = Short.parseShort(commandContext.getArgument(0));
        createNPC(npc);
    }

}
