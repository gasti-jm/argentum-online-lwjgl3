package org.aoclient.network.protocol.command.handlers.basic;

import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;
import org.aoclient.network.protocol.command.CommandHandler;

import java.nio.charset.StandardCharsets;

import static org.aoclient.network.protocol.Protocol.writeCouncilMessage;

public class CouncilMessageCommand implements CommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        if (context.hasArguments()) writeCouncilMessage(context.getArgumentsRaw());
        else
            console.addMsgToConsole(new String("Write a message.".getBytes(), StandardCharsets.UTF_8), false, true, new RGBColor());
    }

}
