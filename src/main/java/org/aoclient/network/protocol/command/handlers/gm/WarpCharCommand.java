package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.engine.game.Options;
import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeWarpChar;

/**
 * Comando para teletransportar personajes.
 * <p>
 * Formatos aceptados:
 * <ul>
 *  <li>{@code /telep <x> <y>}               - Teletransporta al usuario actual en el mismo mapa
 *  <li>{@code /telep <nick> <x> <y>}        - Teletransporta al nick al mismo mapa donde esta el usuario
 *  <li>{@code /telep <map> <x> <y>}         - Teletransporta al usuario actual al mapa especificado
 *  <li>{@code /telep <nick> <map> <x> <y>}  - Teletransporta al nick al mapa y coordenadas especificados
 * </ul>
 * <p>
 * NOTA: Para distinguir entre nick y map en el formato de 3 argumentos:
 * <ul>
 *  <li>Si el primer argumento es numerico, se considera un mapa
 *  <li>Si no es numerico, se considera un nick
 * </ul>
 */

public class WarpCharCommand extends BaseCommandHandler {

    private static final String USAGE = "/telep <nick> <map> <x> <y>";
    private static final int MAX_COORDINATE = 100; // Maxima coordenada valida
    private static final int MAX_MAP_ID = 1000; // Maximo ID de mapa valido (ajustar segun tu servidor)

    @Override
    public void handle(CommandContext context) throws CommandException {
        requireArguments(context, 4, USAGE);

        switch (context.getArgumentCount()) {
            case 2 -> handleTwoArguments(context);
            case 3 -> handleThreeArguments(context);
            case 4 -> handleFourArguments(context);
            default -> showError("Missing arguments. Usage: " + USAGE);
        }
    }

    /**
     * Maneja el formato: {@code /telep <x> <y>}.
     * <p>
     * Teletransporta al usuario actual en el mismo mapa.
     */
    private void handleTwoArguments(CommandContext context) throws CommandException {
        requireInteger(context, 0, "x");
        requireInteger(context, 1, "y");

        int x = Integer.parseInt(context.getArgument(0));
        int y = Integer.parseInt(context.getArgument(1));

        validateCoordinate(x);
        validateCoordinate(y);

        writeWarpChar(Options.INSTANCE.getNickName(), user.getUserMap(), x, y);
    }

    /**
     * Maneja el formato: {@code /telep <nick> <x> <y>} o {@code /telep <map> <x> <y>}.
     * <p>
     * Intenta determinar si el primer argumento es un nick o un mapa usando heurísticas.
     */
    private void handleThreeArguments(CommandContext context) throws CommandException {
        requireInteger(context, 1, "x or map");
        requireInteger(context, 2, "y or x");

        String firstArg = context.getArgument(0);

        // Intenta determinar si es un nick o un mapa
        if (isNumeric(firstArg)) handleMapXY(context); // Es numerico, probablemente un mapa: /telep <map> <x> <y>
        else handleNickXY(context); // No es numerico, probablemente un nick: /telep <nick> <x> <y>
    }

    /**
     * Maneja el formato: {@code /telep <map> <x> <y>}.
     */
    private void handleMapXY(CommandContext context) throws CommandException {
        requireInteger(context, 0, "map");

        short map = Short.parseShort(context.getArgument(0));
        int x = Integer.parseInt(context.getArgument(1));
        int y = Integer.parseInt(context.getArgument(2));

        validateMapId(map);
        validateCoordinate(x);
        validateCoordinate(y);

        writeWarpChar(Options.INSTANCE.getNickName(), map, x, y);
    }

    /**
     * Maneja el formato: {@code /telep <nick> <x> <y>}
     */
    private void handleNickXY(CommandContext context) throws CommandException {
        requireString(context, 0, "nick");

        String nick = context.getArgument(0);
        int x = Integer.parseInt(context.getArgument(1));
        int y = Integer.parseInt(context.getArgument(2));

        validateCoordinate(x);
        validateCoordinate(y);

        writeWarpChar(nick, user.getUserMap(), x, y);
    }

    /**
     * Maneja el formato: {@code /telep <nick> <map> <x> <y>}.
     * <p>
     * Formato completo con todos los parametros.
     */
    private void handleFourArguments(CommandContext context) throws CommandException {
        requireString(context, 0, "nick");
        requireInteger(context, 1, "map");
        requireInteger(context, 2, "x");
        requireInteger(context, 3, "y");

        String nick = context.getArgument(0);
        short map = Short.parseShort(context.getArgument(1));
        int x = Integer.parseInt(context.getArgument(2));
        int y = Integer.parseInt(context.getArgument(3));

        validateMapId(map);
        validateCoordinate(x);
        validateCoordinate(y);

        writeWarpChar(nick, map, x, y);
    }

    /**
     * Verifica si un string es numerico.
     */
    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Valida que una coordenada este en el rango valido.
     */
    private void validateCoordinate(int coordinate) throws CommandException {
        // TODO o menor a 0?
        if (coordinate < 1 || coordinate > MAX_COORDINATE)
            showError("Invalid " + coordinate + " coordinate, must be between 1 and " + MAX_COORDINATE);
    }

    /**
     * Valida que un ID de mapa este en el rango valido.
     */
    private void validateMapId(short mapId) throws CommandException {
        if (mapId < 1 || mapId > MAX_MAP_ID) showError("Invalid map ID, must be between 1 and " + MAX_MAP_ID);
    }

}

