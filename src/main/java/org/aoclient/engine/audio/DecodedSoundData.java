package org.aoclient.engine.audio;

import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import static org.lwjgl.openal.AL10.AL_FORMAT_MONO16;
import static org.lwjgl.openal.AL10.AL_FORMAT_STEREO16;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_decode_memory;
import static org.lwjgl.system.MemoryStack.*;

/**
 * Esta clase nos permite precargar los datos de algun sonido en {@code .ogg }
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

            ByteBuffer vorbisData = ioResourceToByteBuffer(filepath);
            if (vorbisData == null) throw new RuntimeException("Error loading resource: " + filepath);

            ShortBuffer pcm = stb_vorbis_decode_memory(vorbisData, channelsBuffer, sampleRateBuffer);
            if (pcm == null) throw new RuntimeException("Error decoding: " + filepath);

            int channels = channelsBuffer.get(0);
            int sampleRate = sampleRateBuffer.get(0);

            int format = channels == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16;
            return new DecodedSoundData(filepath, pcm, format, sampleRate);
        }
    }

    private static ByteBuffer ioResourceToByteBuffer(String resource) {
        String path = resource.startsWith("/") ? resource : "/" + resource;
        try (InputStream source = DecodedSoundData.class.getResourceAsStream(path)) {
            if (source == null) return null;
            byte[] bytes = source.readAllBytes();
            ByteBuffer buffer = BufferUtils.createByteBuffer(bytes.length);
            buffer.put(bytes);
            buffer.flip();
            return buffer;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}

