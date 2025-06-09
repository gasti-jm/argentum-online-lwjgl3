package org.aoclient.network.protocol.command.handlers.party;

import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;
import org.aoclient.network.protocol.command.CommandHandler;

import java.nio.charset.StandardCharsets;

import static org.aoclient.network.protocol.Protocol.writePartyMessage;

public class PartyMessageCommand implements CommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        if (context.getArgumentCount() == 0) writePartyMessage(context.getArgumentsRaw());
        else
            console.addMsgToConsole(new String("Write a message.".getBytes(), StandardCharsets.UTF_8), false, true, new RGBColor());
    }

}
