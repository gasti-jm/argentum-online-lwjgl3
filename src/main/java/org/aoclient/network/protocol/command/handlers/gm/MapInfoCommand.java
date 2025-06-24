package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.*;

public class MapInfoCommand extends BaseCommandHandler {

    private static final String USAGE = "/mapinfo <option> <value>\n" +
            "Options: pk, backup, restringir, magiasinefecto, invisinefecto, resusinefecto, terreno, zona";

    @Override
    public void handle(CommandContext context) throws CommandException {
        requireArguments(context, 2, USAGE);

        String option = context.getArgument(0);
        String value = context.getArgument(1);

        switch (option.toLowerCase()) {
            case "pk":
                requireString(context, 1, "pk value");
                writeChangeMapInfoPK(value.equals("1"));
                break;
            case "backup":
                requireString(context, 1, "backup value");
                writeChangeMapInfoBackup(value.equals("1"));
                break;
            case "restringir":
                requireString(context, 1, "restringir value");
                writeChangeMapInfoRestricted(value);
                break;
            case "magiasinefecto":
                requireString(context, 1, "magiasinefecto value");
                writeChangeMapInfoNoMagic(Boolean.parseBoolean(value));
                break;
            case "invisinefecto":
                requireString(context, 1, "invisinefecto value");
                writeChangeMapInfoNoInvi(Boolean.parseBoolean(value));
                break;
            case "resusinefecto":
                requireString(context, 1, "resusinefecto value");
                writeChangeMapInfoNoResu(Boolean.parseBoolean(value));
                break;
            case "terreno":
                requireString(context, 1, "terreno value");
                writeChangeMapInfoLand(value);
                break;
            case "zona":
                requireString(context, 1, "zona value");
                writeChangeMapInfoZone(value);
                break;
            default:
                showError("Incorrect option, must be one of: pk, backup, restringir, magiasinefecto, invisinefecto, resusinefecto, terreno, zona");
        }
    }

}
