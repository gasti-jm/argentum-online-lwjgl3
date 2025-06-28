package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeCreateNPCWithRespawn;

@Command( "/racc")
@SuppressWarnings("unused")
public class CreateNPCWithRespawnCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        requireArguments(context, 1, "/racc <npc>");
        requireInteger(context, 0, "npc");
        short npc = Short.parseShort(context.getArgument(0));
        writeCreateNPCWithRespawn(npc);
    }

}
