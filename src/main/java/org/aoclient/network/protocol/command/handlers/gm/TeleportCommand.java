package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.engine.game.Options;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;
import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;

import static org.aoclient.network.protocol.Protocol.teleport;
import static org.aoclient.network.protocol.command.metadata.GameCommand.TELEPORT;

/**
 * Comando para teletransportar personajes.
 * <p>
 * Formatos aceptados:
 * <ul>
 *  <li>{@code /teleport <x> <y>}               - Teletransporta al usuario actual en el mismo mapa
 *  <li>{@code /teleport [nick] <x> <y>}        - Teletransporta a otro jugador al mapa actual
 *  <li>{@code /teleport [map] <x> <y>}         - Teletransporta al usuario actual al mapa especificado
 *  <li>{@code /teleport [nick] [map] <x> <y>}  - Teletransporta a otro jugador al mapa especificado
 * </ul>
 * <p>
 * NOTA: Para distinguir entre nick y map en el formato de 3 argumentos:
 * <ul>
 *  <li>Si el primer argumento es numerico, se considera un mapa
 *  <li>Si no es numerico, se considera un nick
 * </ul>
 * <p>
 * IMPORTANTE: En zonas oscuras (sin tildes, es decir, sin mapear) no se puede teletransportar. Esto no se informa al cliente, ya
 * que se maneja desde el servidor y el cliente nunca sabe cual es el zona sin mapear.
 */

public class TeleportCommand extends BaseCommandHandler {

    private static final int MAX_COORDINATE = 100;
    private static final int MIN_COORDINATE = 1;
    private static final int MAX_MAP_ID = 1000;
    private static final int MIN_MAP_ID = 1;

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        switch (commandContext.getArgumentCount()) {
            case 2 -> handleTwoArguments(commandContext);
            case 3 -> handleThreeArguments(commandContext);
            case 4 -> handleFourArguments(commandContext);
            default -> {
                String usage = getCommandUsage(TELEPORT);
                if (commandContext.getArgumentCount() < 2) showError("Missing arguments. " + usage);
                if (commandContext.getArgumentCount() > 4) showError("Too many arguments. " + usage);
            }
        }
    }

    /**
     * Maneja el formato: {@code /teleport <x> <y>}.
     */
    private void handleTwoArguments(CommandContext context) throws CommandException {
        Map map = parseCoordinates(context, 0, 1);
        teleport(Options.INSTANCE.getNick(), user.getUserMap(), map.x, map.y);
    }

    /**
     * Maneja el formato: {@code /teleport [nick] <x> <y>} o {@code /teleport [map] <x> <y>}.
     */
    private void handleThreeArguments(CommandContext context) throws CommandException {
        String firstArg = context.getArgument(0);
        if (isNumeric(firstArg)) handleMap(context);
        else handleNick(context);
    }

    /**
     * Maneja el formato: {@code /teleport [map] <x> <y>}.
     */
    private void handleMap(CommandContext context) throws CommandException {
        short mapId = parseMap(context, 0);
        Map map = parseCoordinates(context, 1, 2);
        teleport(Options.INSTANCE.getNick(), mapId, map.x, map.y);
    }

    /**
     * Maneja el formato: {@code /teleport [nick] <x> <y>}.
     */
    private void handleNick(CommandContext context) throws CommandException {
        requireString(context, 0, "nick");
        String nick = context.getArgument(0);
        Map map = parseCoordinates(context, 1, 2);
        teleport(nick, user.getUserMap(), map.x, map.y);
    }

    /**
     * Maneja el formato: {@code /teleport [nick] [map] <x> <y>}.
     */
    private void handleFourArguments(CommandContext context) throws CommandException {
        requireString(context, 0, "nick");
        String nick = context.getArgument(0);
        short mapId = parseMap(context, 1);
        Map map = parseCoordinates(context, 2, 3);
        teleport(nick, mapId, map.x, map.y);
    }

    /**
     * Parsea y valida las coordenadas X e Y.
     */
    private Map parseCoordinates(CommandContext context, int xIndex, int yIndex) throws CommandException {
        requireInteger(context, xIndex, "x");
        requireInteger(context, yIndex, "y");
        int x = Integer.parseInt(context.getArgument(xIndex));
        int y = Integer.parseInt(context.getArgument(yIndex));
        validateCoordinate(x, 'x');
        validateCoordinate(y, 'y');
        return new Map(x, y);
    }

    /**
     * Parsea y valida un ID de mapa.
     */
    private short parseMap(CommandContext context, int index) throws CommandException {
        requireInteger(context, index, "map");
        short map = Short.parseShort(context.getArgument(index));
        validateMap(map);
        return map;
    }

    /**
     * Verifica si un string es numerico.
     */
    private boolean isNumeric(String str) {
        if (str == null || str.trim().isEmpty()) return false;
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Valida que la coordenada este en el rango valido del mapa.
     */
    private void validateCoordinate(int coordinate, char coordinateChar) throws CommandException {
        if (coordinate < MIN_COORDINATE || coordinate > MAX_COORDINATE)
            showError("The coordinate [" + coordinateChar + "] must be between " + MIN_COORDINATE + " and " + MAX_COORDINATE + ".");
    }

    /**
     * Valida que el ID del mapa este en el rango valido de mapas.
     */
    private void validateMap(short mapId) throws CommandException {
        if (mapId < MIN_MAP_ID || mapId > MAX_MAP_ID)
            showError("Invalid map ID, must be between " + MIN_MAP_ID + " and " + MAX_MAP_ID);
    }


    private record Map(int x, int y) {
    }

}

