package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeForceWAVEToMap;

/**
 * Comando para forzar la reproduccion de un archivo WAV en un mapa especifico.
 * <p>
 * Uso:
 * <ul>
 *  <li>{@code /forcewavmap <wav_id>}                    - Reproducir WAV en el mapa actual
 *  <li>{@code /forcewavmap <wav_id> <map_id> <x> <y>}   - Reproducir WAV en posicion especifica
 * </ul>
 * <p>
 * Parametros:
 * <ul>
 *  <li>wav_id: ID del archivo WAV a reproducir
 *  <li>map_id: ID del mapa donde reproducir
 *  <li>x: Coordenada X en el mapa
 *  <li>y: Coordenada Y en el mapa
 * </ul>
 * <p>
 * Ejemplos:
 * <ul>
 *  <li>{@code /forcewavmap 15}                    - Reproducir WAV #15 en el mapa actual
 *  <li>{@code /forcewavmap 20 100 50 75}          - Reproducir WAV #20 en mapa #100 en posicion (50,75)
 * </ul>
 */

public class ForceWaveToMapCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        requireArguments(context, 1, "/forcewavmap <wav_id> [map_id] [x] [y]");
        if (context.getArgumentCount() == 1) handleCurrentMap(context);
        else if (context.getArgumentCount() == 4) handleSpecificLocation(context);
        else showError("Missing arguments. Usage: /forcewavmap <wav_id> [map_id] [x [y]");
    }

    /**
     * Maneja el caso de reproducir WAV en el mapa actual.
     */
    private void handleCurrentMap(CommandContext context) throws CommandException {
        requireInteger(context, 0, "wav_id");
        int wavId = Integer.parseInt(context.getArgument(0));
        writeForceWAVEToMap(wavId, (short) 0, 0, 0); // Mapa actual, posicion por defecto
    }

    /**
     * Maneja el caso de reproducir WAV en ubicacion especifica.
     */
    private void handleSpecificLocation(CommandContext context) throws CommandException {
        requireInteger(context, 0, "wav_id");
        requireInteger(context, 1, "map_id");
        requireInteger(context, 2, "x");
        requireInteger(context, 3, "y");

        int wavId = Integer.parseInt(context.getArgument(0));
        int mapId = Integer.parseInt(context.getArgument(1));
        int x = Integer.parseInt(context.getArgument(2));
        int y = Integer.parseInt(context.getArgument(3));

        writeForceWAVEToMap(wavId, (short) mapId, x, y);
    }

}
