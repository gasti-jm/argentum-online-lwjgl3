package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.Protocol;
import org.aoclient.network.protocol.command.core.CommandContext;
import org.aoclient.network.protocol.command.core.CommandException;
import org.aoclient.network.protocol.command.handlers.BaseCommandHandler;

import static org.aoclient.network.protocol.command.metadata.GameCommand.PLAY_SOUND;

/**
 * FIXME Si especifico la posicion xy del sonido, este se reproduce en todo el mapa y no en la posicion especificada
 */

public class PlaySoundCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        if (commandContext.getArgumentCount() == 1) playSound(commandContext);
        else if (commandContext.getArgumentCount() == 4) playSoundAtLocation(commandContext);
        else showError("Missing arguments. Usage: " + getCommandUsage(PLAY_SOUND));
    }

    private void playSound(CommandContext context) throws CommandException {
        requireInteger(context, 0, "sound_id");
        int sound = Integer.parseInt(context.getArgument(0));
        Protocol.playSound(sound);
    }

    private void playSoundAtLocation(CommandContext context) throws CommandException {
        requireInteger(context, 0, "sound_id");
        requireInteger(context, 1, "map");
        requireInteger(context, 2, "x");
        requireInteger(context, 3, "y");
        int sound = Integer.parseInt(context.getArgument(0));
        short map = Short.parseShort(context.getArgument(1));
        int x = Integer.parseInt(context.getArgument(2));
        int y = Integer.parseInt(context.getArgument(3));
        Protocol.playSoundAtLocation(sound, map, x, y);
    }

}
