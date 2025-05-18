package org.aoclient.engine;

import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL10.AL_FORMAT_MONO16;
import static org.lwjgl.openal.AL10.AL_FORMAT_STEREO16;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_decode_filename;
import static org.lwjgl.system.MemoryStack.stackMallocInt;
import static org.lwjgl.system.MemoryStack.stackPush;

/**
 * Esta clase nos permite precargar los datos de algun sonido en .ogg
 * lo vamos a utilizar para que en un hilo podamos cargar un sonido
 * y evitar lag a la hora de pasar de mapa o de cargar algun sonido con mucho peso.
 */
public final class DecodedSoundData {
    public final ShortBuffer pcm;
    public final String filepath;
    public final int format;
    public final int sampleRate;

    public DecodedSoundData(String filepath, ShortBuffer pcm, int format, int sampleRate) {
        this.pcm = pcm;
        this.format = format;
        this.sampleRate = sampleRate;
        this.filepath = filepath;
    }

    public static DecodedSoundData decodeOgg(String filepath) {
        try (MemoryStack stack = stackPush()) {
            IntBuffer channelsBuffer = stackMallocInt(1);
            IntBuffer sampleRateBuffer = stackMallocInt(1);

            ShortBuffer pcm = stb_vorbis_decode_filename(filepath, channelsBuffer, sampleRateBuffer);
            if (pcm == null) throw new RuntimeException("Error decoding: " + filepath);

            int channels = channelsBuffer.get(0);
            int sampleRate = sampleRateBuffer.get(0);

            int format = channels == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16;
            return new DecodedSoundData(filepath, pcm, format, sampleRate);
        }
    }

}

