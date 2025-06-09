package org.aoclient.network.protocol.command.handlers.guild;

import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;
import org.aoclient.network.protocol.command.CommandHandler;

import java.nio.charset.StandardCharsets;

import static org.aoclient.network.protocol.Protocol.writeShowGuildMessages;

public class ShowGuildMessagesCommand implements CommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        if (context.hasArguments()) writeShowGuildMessages(context.getArgumentsRaw());
        else
            console.addMsgToConsole(new String("Missing parameters. \"Use /SHOWCMSG guildname\".".getBytes(), StandardCharsets.UTF_8), false, true, new RGBColor());
    }

}
