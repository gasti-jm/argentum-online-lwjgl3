package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeForceWAVEAll;

public class ForceWavAllCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        requireArguments(context, 1, "/forcewav <wav>");
        requireInteger(context, 0, "wav");
        int wav = Integer.parseInt(context.getArgument(0));
        writeForceWAVEAll(wav);
    }

}
