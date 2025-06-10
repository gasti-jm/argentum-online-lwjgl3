package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;
import org.aoclient.network.protocol.command.CommandHandler;

import java.nio.charset.StandardCharsets;

import static org.aoclient.network.protocol.Protocol.*;

public class MapInfoCommand implements CommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        if (context.getArgumentCount() > 1) {
            switch (context.getArgument(0).toUpperCase()) {
                case "PK":
                    writeChangeMapInfoPK(context.getArgument(1).equals("1"));
                    break;
                case "BACKUP":
                    writeChangeMapInfoBackup(context.getArgument(1).equals("1"));
                    break;
                case "RESTRINGIR":
                    writeChangeMapInfoRestricted(context.getArgument(1));
                    break;
                case "MAGIASINEFECTO":
                    writeChangeMapInfoNoMagic(Boolean.parseBoolean(context.getArgument(1)));
                    break;
                case "INVISINEFECTO":
                    writeChangeMapInfoNoInvi(Boolean.parseBoolean(context.getArgument(1)));
                    break;
                case "RESUSINEFECTO":
                    writeChangeMapInfoNoResu(Boolean.parseBoolean(context.getArgument(1)));
                    break;
                case "TERRENO":
                    writeChangeMapInfoLand(context.getArgument(1));
                    break;
                case "ZONA":
                    writeChangeMapInfoZone(context.getArgument(1));
                    break;
            }
        } else
            console.addMsgToConsole(new String("Missing parameters. Options: PK, BACKUP, RESTRINGIR, MAGIASINEFECTO, INVISINEFECTO, RESUSINEFECTO, TERRENO, ZONA".getBytes(), StandardCharsets.UTF_8), false, true, new RGBColor());
    }

}
