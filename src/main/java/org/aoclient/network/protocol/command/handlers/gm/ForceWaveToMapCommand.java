package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;
import org.aoclient.network.protocol.command.CommandHandler;
import org.aoclient.network.protocol.types.NumericType;

import java.nio.charset.StandardCharsets;

import static org.aoclient.network.protocol.Protocol.writeForceWAVEToMap;

public class ForceWaveToMapCommand implements CommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        if (context.hasArguments()) {
            if (context.getArgumentCount() == 1) {
                if (validator.isValidNumber(context.getArgument(0), NumericType.BYTE))
                    writeForceWAVEToMap(Integer.parseInt(context.getArgument(0)), (short) 0, 0, 0);
                else
                    console.addMsgToConsole(new String("Use \"/FORCEWAVMAP wav map x y\", last 3 arguments are optional.".getBytes(), StandardCharsets.UTF_8), false, true, new RGBColor());
            } else if (context.getArgumentCount() == 4) {
                if (validator.isValidNumber(context.getArgument(0), NumericType.BYTE) &&
                        validator.isValidNumber(context.getArgument(1), NumericType.INTEGER) &&
                        validator.isValidNumber(context.getArgument(2), NumericType.BYTE) &&
                        validator.isValidNumber(context.getArgument(3), NumericType.BYTE))
                    writeForceWAVEToMap(Integer.parseInt(context.getArgument(0)), Short.parseShort(context.getArgument(1)), Integer.parseInt(context.getArgument(2)), Integer.parseInt(context.getArgument(3)));
                else
                    console.addMsgToConsole(new String("Use \"/FORCEWAVMAP wav map x y\", last 3 arguments are optional.".getBytes(), StandardCharsets.UTF_8), false, true, new RGBColor());
            } else
                console.addMsgToConsole(new String("Use \"/FORCEWAVMAP wav map x y\", last 3 arguments are optional.".getBytes(), StandardCharsets.UTF_8), false, true, new RGBColor());
        } else
            console.addMsgToConsole(new String("Use \"/FORCEWAVMAP wav map x y\", last 3 arguments are optional.".getBytes(), StandardCharsets.UTF_8), false, true, new RGBColor());
    }

}
