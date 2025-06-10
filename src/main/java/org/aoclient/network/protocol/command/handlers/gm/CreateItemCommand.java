package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;
import org.aoclient.network.protocol.command.CommandHandler;
import org.aoclient.network.protocol.types.NumericType;

import static org.aoclient.network.protocol.Protocol.writeCreateItem;

public class CreateItemCommand implements CommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        if (context.hasArguments()) {
            if (validator.isValidNumber(context.getArgument(0), NumericType.LONG))
                writeCreateItem(Integer.parseInt(context.getArgument(0)));
            else console.addMsgToConsole("Incorrect object. Use \"/CI object\".", false, true, new RGBColor());
        } else console.addMsgToConsole("Missing parameters. Use \"/CI object\".", false, true, new RGBColor());
    }

}
