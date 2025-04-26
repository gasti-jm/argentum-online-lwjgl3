package org.aoclient.network.protocol.handlers;

import org.aoclient.network.ByteQueue;
import org.tinylog.Logger;

import static org.aoclient.engine.Sound.playMusic;

public class PlayMIDIHandler implements PacketHandler {

    @Override
    public void handle(ByteQueue data) {
        if (data.checkPacketData(4)) return;

        // Remove packet ID
        data.readByte();

        int currentMusic = data.readByte();

        if (currentMusic > 0) {
            data.readInteger();
            // play music
            playMusic(String.valueOf(currentMusic) + ".ogg");
        } else {
            // Remove the bytes to prevent errors
            data.readInteger();
        }

        Logger.debug("handlePlayMIDI Cargado! - FALTA TERMINAR!");
    }

}
