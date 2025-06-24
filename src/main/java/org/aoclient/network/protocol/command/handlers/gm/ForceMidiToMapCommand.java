package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeForceMIDIToMap;

/**
 * Comando para forzar la reproduccion de un archivo MIDI en un mapa especifico.
 * <p>
 * Uso: {@code /forcemidimap <midi_id> [map_id]}
 * <p>
 * Parametros:
 * <ul>
 * <li>midi_id: ID del archivo MIDI a reproducir
 * <li>map_id: ID del mapa donde reproducir (opcional, por defecto mapa actual)
 * </ul>
 * <p>
 * Ejemplos:
 * <ul>
 * <li>{@code /forcemidimap 5}        - Reproducir MIDI #5 en el mapa actual
 * <li>{@code /forcemidimap 12 100}   - Reproducir MIDI #12 en el mapa #100
 * </ul>
 */

public class ForceMidiToMapCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        requireArguments(context, 1, "/forcemidimap <midi_id> [map_id]");
        if (context.getArgumentCount() == 1) handleSingleArgument(context);
        else if (context.getArgumentCount() == 2) handleTwoArguments(context);
        else showError("Missing arguments. Usage: /forcemidimap <midi_id> [map_id]");
    }

    /**
     * Maneja el caso con solo MIDI ID (mapa actual).
     */
    private void handleSingleArgument(CommandContext context) throws CommandException {
        requireInteger(context, 0, "midi_id");
        int midiId = Integer.parseInt(context.getArgument(0));
        writeForceMIDIToMap(midiId, (short) 0); // TODO 0 es el mapa actual? No deberia usar user.getUserMap()?
    }

    /**
     * Maneja el caso con MIDI ID + Map ID.
     */
    private void handleTwoArguments(CommandContext context) throws CommandException {
        requireInteger(context, 0, "midi_id");
        requireInteger(context, 1, "map_id");

        int midiId = Integer.parseInt(context.getArgument(0));
        int mapId = Integer.parseInt(context.getArgument(1));

        writeForceMIDIToMap(midiId, (short) mapId);
    }

}

