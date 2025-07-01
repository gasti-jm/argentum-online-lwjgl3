package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeChaosLegionMessage;

@Command("/caosmsg")
@SuppressWarnings("unused")
public class ChaosLegionMessageCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        requireArguments(context, -1, "/caosmsg <message>");
        requireValidString(context, "message", REGEX);
        String message = context.argumentsRaw().trim();
        writeChaosLegionMessage(message);
    }

}
