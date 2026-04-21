package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.utils.Log;
import org.aoclient.network.PacketBuffer;
import org.tinylog.Logger;

import static org.aoclient.engine.audio.Sound.playMusic;

public class PlayMIDIHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        if (buffer.checkBytes(4)) return;
        buffer.readByte();

        int currentMusic = buffer.readByte();

        buffer.readInteger(); // Remove the bytes to prevent errors
        if (currentMusic > 0) playMusic(currentMusic + ".ogg"); // play music

        Log.debug("handlePlayMIDI Cargado! - FALTA TERMINAR!");
    }

}
