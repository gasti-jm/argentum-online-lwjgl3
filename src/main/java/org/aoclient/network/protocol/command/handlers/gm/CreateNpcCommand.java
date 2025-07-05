package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeCreateNPC;
import static org.aoclient.network.protocol.command.GameCommand.CREATE_NPC;

public class CreateNpcCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, CREATE_NPC.getCommand() + " <npc>");
        requireInteger(commandContext, 0, "npc");
        short npc = Short.parseShort(commandContext.getArgument(0));
        writeCreateNPC(npc);
    }

}
