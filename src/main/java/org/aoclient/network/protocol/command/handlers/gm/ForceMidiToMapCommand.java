package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;
import org.aoclient.network.protocol.command.CommandHandler;
import org.aoclient.network.protocol.types.NumericType;

import java.nio.charset.StandardCharsets;

import static org.aoclient.network.protocol.Protocol.writeForceMIDIToMap;

public class ForceMidiToMapCommand implements CommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        if (context.hasArguments()) {
            if (context.getArgumentCount() == 1) {
                if (validator.isValidNumber(context.getArgument(0), NumericType.BYTE))
                    writeForceMIDIToMap(Integer.parseInt(context.getArgument(1)), (short) 0);
                else
                    console.addMsgToConsole(new String("Incorrect MIDI. Usage: /forcemidimap <midi> [map]".getBytes(), StandardCharsets.UTF_8), false, true, new RGBColor());
            } else if (context.getArgumentCount() == 2) {
                if (validator.isValidNumber(context.getArgument(0), NumericType.BYTE) && validator.isValidNumber(context.getArgument(1), NumericType.INTEGER))
                    writeForceMIDIToMap(Integer.parseInt(context.getArgument(0)), Short.parseShort(context.getArgument(1)));
                else
                    console.addMsgToConsole(new String("Incorrect arguments. Usage: /forcemidimap <midi> [map]".getBytes(), StandardCharsets.UTF_8), false, true, new RGBColor());
            } else
                console.addMsgToConsole(new String("Usage: /forcemidimap <midi> [map]".getBytes(), StandardCharsets.UTF_8), false, true, new RGBColor());
        } else
            console.addMsgToConsole(new String("Usage: /forcemidimap <midi> [map]".getBytes(), StandardCharsets.UTF_8), false, true, new RGBColor());
    }

}
