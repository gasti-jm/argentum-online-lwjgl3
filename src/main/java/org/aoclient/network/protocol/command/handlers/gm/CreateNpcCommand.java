package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.TextContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeCreateNPC;

@Command("/acc")
@SuppressWarnings("unused")
public class CreateNpcCommand extends BaseCommandHandler {

    @Override
    public void handle(TextContext textContext) throws CommandException {
        requireArguments(textContext, 1, "/acc <npc>");
        requireInteger(textContext, 0, "npc");
        short npc = Short.parseShort(textContext.getArgument(0));
        writeCreateNPC(npc);
    }

}
