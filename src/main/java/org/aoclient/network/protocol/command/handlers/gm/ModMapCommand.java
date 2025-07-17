package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;
import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;

import static org.aoclient.network.protocol.Protocol.*;
import static org.aoclient.network.protocol.command.metadata.GameCommand.MOD_MAP;

public class ModMapCommand extends BaseCommandHandler {

    private static final String VALID_OPTIONS = "pk, backup, restringir, magiasinefecto, invisinefecto, resusinefecto, terreno, zona";
    private static final String USAGE = getCommandUsage(MOD_MAP) + " Options: " + VALID_OPTIONS;

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 2, USAGE);

        String option = commandContext.getArgument(0);
        String value = commandContext.getArgument(1);

        switch (option.toLowerCase()) {
            case "pk" -> changeMapInfoPK(value.equals("1"));
            case "backup" -> changeMapInfoBackup(value.equals("1"));
            case "restringir" -> changeMapInfoRestricted(value); // FIXME No funciona la opcion
            case "magiasinefecto" -> changeMapInfoNoMagic(Boolean.parseBoolean(value));
            case "invisinefecto" -> changeMapInfoNoInvi(Boolean.parseBoolean(value));
            case "resusinefecto" -> changeMapInfoNoResu(Boolean.parseBoolean(value));
            case "terreno" -> changeMapInfoLand(value);
            case "zona" -> changeMapInfoZone(value);
            default -> showError("Incorrect option, must be one of: " + VALID_OPTIONS);
        }

    }

}
