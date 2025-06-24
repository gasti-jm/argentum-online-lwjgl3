package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeSOSShowList;
import static org.aoclient.network.protocol.Protocol.writeShowServerForm;

public class ShowCommand extends BaseCommandHandler {

    private static final String USAGE = "/show <SOS|INT>";

    @Override
    public void handle(CommandContext context) throws CommandException {
        requireArguments(context, 1, USAGE);

        String option = context.getArgument(0);
        requireString(context, 0, "option");

        switch (option.toUpperCase()) {
            case "SOS":
                writeSOSShowList();
                break;
            case "INT":
                writeShowServerForm();
                break;
            default:
                showError("Valid options: SOS, INT");
                break;
        }
    }

}
