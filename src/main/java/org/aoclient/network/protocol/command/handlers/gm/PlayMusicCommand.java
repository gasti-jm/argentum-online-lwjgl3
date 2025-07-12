package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.Protocol;
import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;

import static org.aoclient.network.protocol.Protocol.playMusicOnMap;
import static org.aoclient.network.protocol.command.metadata.GameCommand.PLAY_MUSIC;

public class PlayMusicCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        if (commandContext.getArgumentCount() == 1) playMusic(commandContext, false);
        else if (commandContext.getArgumentCount() == 2) playMusic(commandContext, true);
        else showError("Missing arguments. Usage: " + getCommandUsage(PLAY_MUSIC));
    }

    private void playMusic(CommandContext context, boolean onMap) throws CommandException {
        requireInteger(context, 0, "music_id");
        int musicId = Integer.parseInt(context.getArgument(0));
        if (!onMap) {
            Protocol.playMusic(musicId);
            return;
        }
        requireInteger(context, 1, "map");
        short map = Short.parseShort(context.getArgument(1));
        playMusicOnMap(musicId, map); // FIXME No funciona al especificar el mapa
    }

}

