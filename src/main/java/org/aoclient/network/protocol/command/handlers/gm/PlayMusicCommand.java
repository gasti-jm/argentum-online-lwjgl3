package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.Protocol;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;
import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;

import static org.aoclient.network.protocol.Protocol.playMusicMap;
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
        int music = Integer.parseInt(context.getArgument(0));
        if (!onMap) {
            Protocol.playMusic(music);
            return;
        }
        requireInteger(context, 1, "map");
        short map = Short.parseShort(context.getArgument(1));
        playMusicMap(music, map); // FIXME No funciona al especificar el mapa
    }

}

