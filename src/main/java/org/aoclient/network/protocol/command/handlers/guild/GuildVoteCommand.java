package org.aoclient.network.protocol.command.handlers.guild;

import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;
import org.aoclient.network.protocol.command.CommandHandler;

import java.nio.charset.StandardCharsets;

import static org.aoclient.network.protocol.Protocol.writeGuildVote;

public class GuildVoteCommand implements CommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        if (context.hasArguments()) writeGuildVote(context.getArgumentsRaw());
        else
            console.addMsgToConsole(new String("Missing parameters. Use /VOTO NICKNAME.".getBytes(), StandardCharsets.UTF_8), false, true, new RGBColor());
    }

}
