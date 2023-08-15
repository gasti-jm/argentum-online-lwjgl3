package org.aoclient.engine;

import java.io.File;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.Collection;

import static org.aoclient.engine.utils.GameData.music;
import static org.aoclient.engine.utils.GameData.sounds;
import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_decode_filename;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.libc.LibCStdlib.free;

public final class Sound {
    // Constantes
    public static final String SND_CLICK = "click.ogg";
    public static final String SND_PASOS1 = "23.ogg";
    public static final String SND_PASOS2 = "24.ogg";
    public static final String SND_NAVEGANDO = "50.ogg";
    public static final String SND_OVER = "click2.ogg";
    public static final String SND_DICE = "cupdice.ogg";
    public static final String SND_LLUVIAINEND = "lluviainend.ogg";
    public static final String SND_LLUVIAOUTEND = "lluviaoutend.ogg";


    // Atributos
    private int bufferId;
    private int sourceId;
    private String filepath;

    private boolean isPlaying = false;

    public Sound(String filepath, boolean loops) {
        this.filepath = filepath;

        // Allocate space to store the return information from stb
        stackPush();
        IntBuffer channelsBuffer = stackMallocInt(1);
        stackPush();
        IntBuffer sampleRateBuffer = stackMallocInt(1);

        ShortBuffer rawAudioBuffer =
                stb_vorbis_decode_filename(filepath, channelsBuffer, sampleRateBuffer);

        if (rawAudioBuffer == null) {
            System.out.println("Could not load sound '" + filepath + "'");
            stackPop();
            stackPop();
            return;
        }

        // Retrieve the extra information that was stored in the buffers by stb
        int channels = channelsBuffer.get();
        int sampleRate = sampleRateBuffer.get();

        // Free
        stackPop();
        stackPop();

        // Find the correct openAL format
        int format = -1;
        if (channels == 1) {
            format = AL_FORMAT_MONO16;
        } else if (channels == 2) {
            format = AL_FORMAT_STEREO16;
        }

        bufferId = alGenBuffers();
        alBufferData(bufferId, format, rawAudioBuffer, sampleRate);

        // Generate the source
        sourceId = alGenSources();

        alSourcei(sourceId, AL_BUFFER, bufferId);
        alSourcei(sourceId, AL_LOOPING, loops ? 1 : 0);
        alSourcei(sourceId, AL_POSITION, 0);
        alSourcef(sourceId, AL_GAIN, 1f);

        // Free stb raw audio buffer
        free(rawAudioBuffer);
    }

    public void delete() {
        alDeleteSources(sourceId);
        alDeleteBuffers(bufferId);
    }

    public void play() {
        int state = alGetSourcei(sourceId, AL_SOURCE_STATE);
        if (state == AL_STOPPED) {
            isPlaying = false;
            alSourcei(sourceId, AL_POSITION, 0);
        }

        if (!isPlaying) {
            alSourcePlay(sourceId);
            isPlaying = true;
        }
    }


    public void stop() {
        if (isPlaying) {
            alSourceStop(sourceId);
            isPlaying = false;
        }
    }

    public String getFilepath() {
        return this.filepath;
    }

    public boolean isPlaying() {
        int state = alGetSourcei(sourceId, AL_SOURCE_STATE);
        if (state == AL_STOPPED) {
            isPlaying = false;
        }
        return isPlaying;
    }

    /**
     *
     * @desc: Devuelve todos los sonidos
     */
    public static Collection<Sound> getAllSounds() {
        return sounds.values();
    }

    /**
     *
     * @desc: Nos devuelve un sonido cargado a nuestro mapa.
     */
    public static Sound getSound(String soundFile) {
        File file = new File(soundFile);
        if (sounds.containsKey(file.getAbsolutePath())) {
            return sounds.get(file.getAbsolutePath());
        } else {
            System.out.println("Sound file not added '" + soundFile + "'");
        }

        return null;
    }

    /**
     *
     * @desc: Agregamos un sonido a nuestro mapa.
     */
    public static Sound addSound(String soundFile, boolean loops) {
        final File file = new File(soundFile);
        if (sounds.containsKey(file.getAbsolutePath())) {
            return sounds.get(file.getAbsolutePath());
        } else {
            Sound sound = new Sound(file.getAbsolutePath(), loops);
            sounds.put(file.getAbsolutePath(), sound);
            return sound;
        }
    }

    public static void playSound(String soundName) {
        final File file = new File("resources/sounds/" + soundName);

        // existe?
        if (sounds.containsKey(file.getAbsolutePath())) {
            sounds.get(file.getAbsolutePath()).play();
        } else {
            addSound("resources/sounds/" + soundName, false).play();
        }
    }

    /**
     *
     * @desc: Agregamos musica a nuestro variable de musica.
     */
    public static Sound addMusic(String soundFile) {
        final File file = new File(soundFile);

        if(music != null) {
            music.stop();
            music.delete();
        }

        Sound sound = new Sound(file.getAbsolutePath(), true);
        music = sound;

        return sound;
    }

    public static void clearSounds() {
        sounds.forEach((s, sound) -> sound.delete());
        sounds.clear();
    }
}