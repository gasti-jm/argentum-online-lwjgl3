package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.TextContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writePlaySound;
import static org.aoclient.network.protocol.Protocol.writePlaySoundAtTheSpecifiedLocation;

/**
 * FIXME Si especifico la posicion [x] [y] del sonido, este se reproduce en todo el mapa y no en la posicion especificada
 */

@Command("/playsound")
@SuppressWarnings("unused")
public class PlaySoundCommand extends BaseCommandHandler {

    @Override
    public void handle(TextContext textContext) throws CommandException {
        if (textContext.getArgumentCount() == 1) playSound(textContext);
        else if (textContext.getArgumentCount() == 4) playSoundAtTheSpecifiedLocation(textContext);
        else showError("Missing arguments. Usage: /playsound <sound_id> [map] [x] [y]");
    }

    private void playSound(TextContext context) throws CommandException {
        requireInteger(context, 0, "sound_id");
        int soundId = Integer.parseInt(context.getArgument(0));
        writePlaySound(soundId);
    }

    private void playSoundAtTheSpecifiedLocation(TextContext context) throws CommandException {
        requireInteger(context, 0, "sound_id");
        requireInteger(context, 1, "map");
        requireInteger(context, 2, "x");
        requireInteger(context, 3, "y");
        int soundId = Integer.parseInt(context.getArgument(0));
        short map = Short.parseShort(context.getArgument(1));
        int x = Integer.parseInt(context.getArgument(2));
        int y = Integer.parseInt(context.getArgument(3));
        writePlaySoundAtTheSpecifiedLocation(soundId, map, x, y);
    }

}
