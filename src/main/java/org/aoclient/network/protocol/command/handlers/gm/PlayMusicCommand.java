package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writePlayMusic;
import static org.aoclient.network.protocol.Protocol.writePlayMusicOnMap;

@Command("/playmusic")
@SuppressWarnings("unused")
public class PlayMusicCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        if (context.getArgumentCount() == 1) playMusic(context, false);
        else if (context.getArgumentCount() == 2) playMusic(context, true);
        else showError("Missing arguments. Usage: /playmusic <sound_id> [map]");
    }

    private void playMusic(CommandContext context, boolean onMap) throws CommandException {
        requireInteger(context, 0, "music_id");
        int musicId = Integer.parseInt(context.getArgument(0));
        if (!onMap) {
            writePlayMusic(musicId);
            return;
        }
        requireInteger(context, 1, "map");
        short map = Short.parseShort(context.getArgument(1));
        writePlayMusicOnMap(musicId, map); // FIXME No funciona al especificar el mapa
    }

}

