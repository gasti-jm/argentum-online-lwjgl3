package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.TextContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.*;

@Command("/modmapinfo")
@SuppressWarnings("unused")
public class MapInfoCommand extends BaseCommandHandler {

    private static final String VALID_OPTIONS = "pk, backup, restringir, magiasinefecto, invisinefecto, resusinefecto, terreno, zona";
    private static final String USAGE = "/modmapinfo <option> <value>\nOptions: " + VALID_OPTIONS;

    @Override
    public void handle(TextContext textContext) throws CommandException {
        requireArguments(textContext, 2, USAGE);

        String option = textContext.getArgument(0);
        String value = textContext.getArgument(1);

        switch (option.toLowerCase()) {
            case "pk" -> writeChangeMapInfoPK(value.equals("1"));
            case "backup" -> writeChangeMapInfoBackup(value.equals("1"));
            case "restringir" -> writeChangeMapInfoRestricted(value);
            case "magiasinefecto" -> writeChangeMapInfoNoMagic(Boolean.parseBoolean(value));
            case "invisinefecto" -> writeChangeMapInfoNoInvi(Boolean.parseBoolean(value));
            case "resusinefecto" -> writeChangeMapInfoNoResu(Boolean.parseBoolean(value));
            case "terreno" -> writeChangeMapInfoLand(value);
            case "zona" -> writeChangeMapInfoZone(value);
            default -> showError("Incorrect option, must be one of: " + VALID_OPTIONS);
        }

    }

}
