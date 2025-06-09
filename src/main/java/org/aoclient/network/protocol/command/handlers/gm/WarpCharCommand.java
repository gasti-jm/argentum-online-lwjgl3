package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;
import org.aoclient.network.protocol.command.CommandHandler;
import org.aoclient.network.protocol.types.NumericType;

import java.nio.charset.StandardCharsets;

import static org.aoclient.network.protocol.Protocol.writeWarpChar;

public class WarpCharCommand implements CommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        if (context.hasArguments() && context.getArgumentCount() >= 4) {
            if (validator.isValidNumber(context.getArgument(1), NumericType.INTEGER) &&
                    validator.isValidNumber(context.getArgument(2), NumericType.BYTE) &&
                    validator.isValidNumber(context.getArgument(3), NumericType.BYTE)) {
                writeWarpChar(context.getArgument(0), Short.parseShort(context.getArgument(1)), Integer.parseInt(context.getArgument(2)), Integer.parseInt(context.getArgument(3)));
            } else
                console.addMsgToConsole(new String("Incorrect value. Use \"/TELEP nickname map x y\".".getBytes(), StandardCharsets.UTF_8), false, true, new RGBColor());
        } else if (context.getArgumentCount() == 3) {
            if (validator.isValidNumber(context.getArgument(0), NumericType.INTEGER) &&
                    validator.isValidNumber(context.getArgument(1), NumericType.BYTE) &&
                    validator.isValidNumber(context.getArgument(2), NumericType.BYTE)) {
                // Por defecto, si no se indica el nombre, se teletransporta el mismo usuario
                writeWarpChar("YO", Short.parseShort(context.getArgument(0)), Integer.parseInt(context.getArgument(1)), Integer.parseInt(context.getArgument(2)));
            } else if (validator.isValidNumber(context.getArgument(1), NumericType.BYTE) &&
                    validator.isValidNumber(context.getArgument(2), NumericType.BYTE)) {
                // Por defecto, si no se indica el mapa, se teletransporta al mismo donde esta el usuario
                writeWarpChar(context.getArgument(0), user.getUserMap(), Integer.parseInt(context.getArgument(1)), Integer.parseInt(context.getArgument(2)));
            } else
                // No uso ningun formato por defecto
                console.addMsgToConsole(new String("Incorrect value. Use \"/TELEP nickname map x y\".".getBytes(), StandardCharsets.UTF_8), false, true, new RGBColor());
        } else if (context.getArgumentCount() == 2) {
            if (validator.isValidNumber(context.getArgument(0), NumericType.BYTE) &&
                    validator.isValidNumber(context.getArgument(1), NumericType.BYTE)) {
                // Por defecto, se considera que se quiere unicamente cambiar las coordenadas del usuario, en el mismo mapa
                writeWarpChar("YO", user.getUserMap(), Integer.parseInt(context.getArgument(0)), Integer.parseInt(context.getArgument(1)));
            } else
                // No uso ningun formato por defecto
                console.addMsgToConsole(new String("Incorrect value. Use \"/TELEP nickname map x y\".".getBytes(), StandardCharsets.UTF_8), false, true, new RGBColor());
        } else
            // Avisa que falta el parametro
            console.addMsgToConsole(new String("Missing parameters. Use \"/TELEP nickname map x y\".".getBytes(), StandardCharsets.UTF_8), false, true, new RGBColor());
    }

}
