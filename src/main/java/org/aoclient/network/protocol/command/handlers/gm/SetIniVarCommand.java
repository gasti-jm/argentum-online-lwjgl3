package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;
import org.aoclient.network.protocol.command.CommandHandler;

import java.nio.charset.StandardCharsets;

import static org.aoclient.network.protocol.Protocol.writeSetIniVar;

public class SetIniVarCommand implements CommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        if (context.getArgumentCount() == 3) {
            context.setArgument(2, context.getArgument(2).replace("+", " ")); // ?
            // argumentosAll[2] = context.getArgument(2).replace("+", " ");
            writeSetIniVar(context.getArgument(0), context.getArgument(1), context.getArgument(2));
        } else
            console.addMsgToConsole(new String("Incorrect parameters. Usage: /setinivar <llave> <clave> <valor>".getBytes(), StandardCharsets.UTF_8), false, true, new RGBColor());
    }

}
