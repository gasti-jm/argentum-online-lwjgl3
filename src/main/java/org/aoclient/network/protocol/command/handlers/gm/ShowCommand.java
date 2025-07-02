package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.TextContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeSOSShowList;
import static org.aoclient.network.protocol.Protocol.writeShowServerForm;

@Command("/show")
@SuppressWarnings("unused")
public class ShowCommand extends BaseCommandHandler {

    @Override
    public void handle(TextContext textContext) throws CommandException {
        requireArguments(textContext, 1, "/show <SOS|INT>");
        requireString(textContext, 0, "option");
        String option = textContext.getArgument(0);
        switch (option.toUpperCase()) {
            case "SOS" -> writeSOSShowList();
            case "INT" -> writeShowServerForm();
            default -> showError("Valid options: SOS, INT");
        }
    }

}
