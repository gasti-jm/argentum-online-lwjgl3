package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeCreateNPCWithRespawn;
import static org.aoclient.network.protocol.command.GameCommand.RACC;

public class CreateNPCWithRespawnCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, RACC.getCommand() + " <npc>");
        requireInteger(commandContext, 0, "npc");
        short npc = Short.parseShort(commandContext.getArgument(0));
        writeCreateNPCWithRespawn(npc);
    }

}
