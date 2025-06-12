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
            switch (context.getArgument(0)) {
                case "pk":
                    writeChangeMapInfoPK(context.getArgument(1).equals("1"));
                    break;
                case "backup":
                    writeChangeMapInfoBackup(context.getArgument(1).equals("1"));
                    break;
                case "restringir":
                    writeChangeMapInfoRestricted(context.getArgument(1));
                    break;
                case "magiasinefecto":
                    writeChangeMapInfoNoMagic(Boolean.parseBoolean(context.getArgument(1)));
                    break;
                case "invisinefecto":
                    writeChangeMapInfoNoInvi(Boolean.parseBoolean(context.getArgument(1)));
                    break;
                case "resusinefecto":
                    writeChangeMapInfoNoResu(Boolean.parseBoolean(context.getArgument(1)));
                    break;
                case "terreno":
                    writeChangeMapInfoLand(context.getArgument(1));
                    break;
                case "zona":
                    writeChangeMapInfoZone(context.getArgument(1));
                    break;
            }
        } else
            console.addMsgToConsole(new String("Missing arguments. Arguments: <pk>, <backup>, <restringir>, <magiasinefecto>, <invisinefecto>, <resusinefecto>, <terreno>, <zona>".getBytes(), StandardCharsets.UTF_8), false, true, new RGBColor());
    }

}
