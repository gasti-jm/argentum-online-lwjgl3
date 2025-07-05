package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.Protocol;
import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;

import static org.aoclient.network.protocol.command.metadata.GameCommand.PLAY_SOUND;

/**
 * FIXME Si especifico la posicion [x] [y] del sonido, este se reproduce en todo el mapa y no en la posicion especificada
 */

public class PlaySoundCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        if (commandContext.getArgumentCount() == 1) playSound(commandContext);
        else if (commandContext.getArgumentCount() == 4) playSoundAtTheSpecifiedLocation(commandContext);
        else showError("Missing arguments. Usage: " + PLAY_SOUND.getCommand() + " <sound_id> [map] [x] [y]");
    }

    private void playSound(CommandContext context) throws CommandException {
        requireInteger(context, 0, "sound_id");
        int soundId = Integer.parseInt(context.getArgument(0));
        Protocol.playSound(soundId);
    }

    private void playSoundAtTheSpecifiedLocation(CommandContext context) throws CommandException {
        requireInteger(context, 0, "sound_id");
        requireInteger(context, 1, "map");
        requireInteger(context, 2, "x");
        requireInteger(context, 3, "y");
        int soundId = Integer.parseInt(context.getArgument(0));
        short map = Short.parseShort(context.getArgument(1));
        int x = Integer.parseInt(context.getArgument(2));
        int y = Integer.parseInt(context.getArgument(3));
        Protocol.playSoundAtTheSpecifiedLocation(soundId, map, x, y);
    }

}
