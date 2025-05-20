package org.aoclient.network.protocol.handlers;

import org.aoclient.network.PacketBuffer;

import static org.aoclient.engine.Sound.playSound;

public class PlayWaveHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        if (buffer.checkBytes(3)) return;
        buffer.readByte();

        int wave = buffer.readByte();
        int srcX = buffer.readByte();
        int srcY = buffer.readByte();

        // Call Audio.PlayWave(CStr(wave) & ".wav", srcX, srcY)
        playSound(String.valueOf(wave) + ".ogg");
    }

}
