package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;

import static org.aoclient.network.protocol.Protocol.writeSOSShowList;
import static org.aoclient.network.protocol.Protocol.writeShowServerForm;
import static org.aoclient.network.protocol.command.metadata.GameCommand.SHOW;

public class ShowCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, SHOW.getCommand() + " <SOS|INT>");
        requireString(commandContext, 0, "option");
        String option = commandContext.getArgument(0);
        switch (option.toUpperCase()) {
            case "SOS" -> writeSOSShowList();
            case "INT" -> writeShowServerForm();
            default -> showError("Valid options: SOS, INT");
        }
    }

}
