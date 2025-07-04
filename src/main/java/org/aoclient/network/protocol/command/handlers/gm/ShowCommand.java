package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeSOSShowList;
import static org.aoclient.network.protocol.Protocol.writeShowServerForm;

public class ShowCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 1, "/show <SOS|INT>");
        requireString(commandContext, 0, "option");
        String option = commandContext.getArgument(0);
        switch (option.toUpperCase()) {
            case "SOS" -> writeSOSShowList();
            case "INT" -> writeShowServerForm();
            default -> showError("Valid options: SOS, INT");
        }
    }

}
