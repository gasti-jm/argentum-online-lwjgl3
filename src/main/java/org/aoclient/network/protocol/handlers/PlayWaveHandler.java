package org.aoclient.network.protocol.handlers;

import org.aoclient.network.PacketBuffer;

import static org.aoclient.engine.Sound.playSound;

public class PlayWaveHandler implements PacketHandler {
    
    @Override
    public void handle(PacketBuffer data) {
        if (data.checkBytes(3)) return;

        data.readByte();

        int wave = data.readByte();
        int srcX = data.readByte();
        int srcY = data.readByte();

        // Call Audio.PlayWave(CStr(wave) & ".wav", srcX, srcY)
        playSound(String.valueOf(wave) + ".ogg");
    }
    
}
