package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeCreateNPC;

@Command("/acc")
@SuppressWarnings("unused")
public class CreateNpcCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, "/acc <npc>");
        requireInteger(commandContext, 0, "npc");
        short npc = Short.parseShort(commandContext.getArgument(0));
        writeCreateNPC(npc);
    }

}
