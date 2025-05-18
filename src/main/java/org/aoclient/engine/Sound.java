package org.aoclient.engine;

import java.io.File;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.Collection;

import static org.aoclient.engine.utils.GameData.*;
import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_decode_filename;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.libc.LibCStdlib.free;

/**
 * <p>
 * Proporciona funcionalidad para cargar, reproducir y administrar sonidos y musica en formato <b>.ogg</b> utilizando
 * {@code OpenAL} como motor de audio. Mantiene colecciones separadas para los efectos de sonido y la musica, permitiendo un
 * control independiente de cada tipo de audio.
 * <p>
 * Define constantes para los sonidos mas comunes del juego (como clicks, pasos, dados, etc.) y ofrece metodos estaticos para
 * acceder facilmente a estas funcionalidades desde cualquier parte del codigo. Limita el numero maximo de sonidos y musicas
 * cargados en memoria para optimizar el uso de recursos.
 * <p>
 * Cada instancia de {@code Sound} representa un archivo de audio individual que puede configurarse para reproducirse en bucle
 * (ideal para musica de fondo) o una sola vez (apropiado para efectos de sonido). La clase tambien gestiona automaticamente la
 * reproduccion simultanea de multiples instancias del mismo sonido cuando es necesario.
 * <p>
 * Incluye funcionalidades para la limpieza de recursos de audio cuando ya no son necesarios, ayudando a prevenir fugas de
 * memoria.
 */

public final class Sound {
    public static final String SND_CLICK = "click.ogg";
    public static final String SND_PASOS1 = "23.ogg";
    public static final String SND_PASOS2 = "24.ogg";
    public static final String SND_NAVEGANDO = "50.ogg";
    public static final String SND_OVER = "click2.ogg";
    public static final String SND_DICE = "cupdice.ogg";

    public static final int MAX_SOUNDS = 30; // cantidad maxima de sondios almacenadas en memoria.
    public static final int MAX_MUSIC = 2; // cantidad maxima de musica almacenada en memoria.
    private String filepath;
    private int bufferId;
    private int sourceId;
    private boolean isPlaying = false;

    public static DecodedSoundData preloadedMusic = null;

    /**
     * Carga nuestro sonido en formato .ogg y activa si es en un loop infinito o no (ya sea para musica o sonido)
     */
    public Sound(final String filepath, final boolean loops) {
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
        final int channels = channelsBuffer.get();
        final int sampleRate = sampleRateBuffer.get();

        // Free
        stackPop();
        stackPop();

        // Find the correct openAL format
        int format = -1;
        if (channels == 1) format = AL_FORMAT_MONO16;
        else if (channels == 2) format = AL_FORMAT_STEREO16;

        bufferId = alGenBuffers();
        alBufferData(bufferId, format, rawAudioBuffer, sampleRate);

        // Generate the source
        sourceId = alGenSources();

        alSourcei(sourceId, AL_BUFFER, bufferId);
        alSourcei(sourceId, AL_LOOPING, loops ? 1 : 0); // loop
        alSourcei(sourceId, AL_POSITION, 0); // posicion
        alSourcef(sourceId, AL_GAIN, 1f); // configuracion de volumen

        // Free stb raw audio buffer
        free(rawAudioBuffer);
    }

    /**
     * Constructor para crear un sonido en un hilo aparte
     */
    public Sound(String filepath, DecodedSoundData data) {
        bufferId = alGenBuffers();
        alBufferData(bufferId, data.format, data.pcm, data.sampleRate);
        sourceId = alGenSources();

        alSourcei(sourceId, AL_BUFFER, bufferId);
        alSourcei(sourceId, AL_LOOPING, 1);
        alSourcei(sourceId, AL_POSITION, 0);
        alSourcef(sourceId, AL_GAIN, 1f);

        free(data.pcm);
    }

    public static Sound createSoundFromDecoded(String filepath, DecodedSoundData data) {
        Sound sound = new Sound(filepath, data);
        return sound;
    }

    /**
     * Nos devuelve un sonido cargado a nuestro mapa.
     */
    public static Sound getSound(String soundFile) {
        File file = new File(soundFile);
        if (sounds.containsKey(file.getAbsolutePath())) return sounds.get(file.getAbsolutePath());
        else System.out.println("Sound file not added '" + soundFile + "'");
        return null;
    }

    /**
     * Agregamos un sonido a nuestro mapa.
     */
    public static Sound addSound(String soundFile, boolean loops) {
        final File file = new File(soundFile);
        if (sounds.containsKey(file.getAbsolutePath())) return sounds.get(file.getAbsolutePath());
        else {
            if (sounds.size() == MAX_SOUNDS) clearSounds();
            Sound sound = new Sound(file.getAbsolutePath(), loops);
            sounds.put(file.getAbsolutePath(), sound);
            return sound;
        }
    }

    /**
     * Agregamos una musica a nuestro mapa.
     */
    public static Sound addMusic(String soundFile) {
        if (musics.containsKey(soundFile))
            return musics.get(soundFile);
        else {
            if (musics.size() == MAX_MUSIC)
                clearMusics();

            Sound sound = createSoundFromDecoded(soundFile, preloadedMusic);
            musics.put(soundFile, sound);
            return sound;
        }
    }

    /**
     * Reproduce un sonido, primero checkea si existe en nuestro mapa, en caso de que exista lo reproduce, caso contrario:
     * crea uno, lo guarda en el mapa y lo reproduce.
     */
    public static void playSound(String soundName) {
        final File file = new File("resources/sounds/" + soundName);
        // existe?
        if (options.isSound()) {
            if (sounds.containsKey(file.getAbsolutePath())) sounds.get(file.getAbsolutePath()).play();
            else addSound("resources/sounds/" + soundName, false).play();
        }
    }

    /**
     * Agregamos musica a nuestro objeto de musica y lo reproduce.
     */
    public static void playMusic(String musicName) {
        stopMusic();

        final File file = new File("resources/music/" + musicName);

        // existe?
        if (musics.containsKey(file.getAbsolutePath())) {
            if (options.isMusic()) musics.get(file.getAbsolutePath()).play();
        } else {
            if (options.isMusic()) {
                // leemos el ogg en un hilo aparte.
                new Thread(() -> {
                    preloadedMusic = DecodedSoundData.decodeOgg(file.getAbsolutePath());
                }).start();
            }
        }

    }

    /**
     * Va a estar checkenado en nuestro main loop si ya se puede reproducir la musica que se precargo
     * en un hilo externo.
     */
    public static void renderMusic() {
        if (preloadedMusic != null) {
            stopMusic();

            // existe en nuestro array de musica?
            if (musics.containsKey(preloadedMusic.filepath)) {
                if (options.isMusic())
                    musics.get(preloadedMusic.filepath).play();
            } else {
                // agregamos uno nuevo y lo reproducimos.
                if (options.isMusic())
                    addMusic(preloadedMusic.filepath).play();
            }

            // seteamos como null y que se prepare para cargar otro archivo.
            preloadedMusic = null;
        }

    }

    /**
     * Vacia y elimina todos los sonidos de nuestro mapa.
     */
    public static void clearSounds() {
        sounds.forEach((s, sound) -> sound.delete());
        sounds.clear();
    }

    /**
     * Elimina y vacia la musica de nuestra lista
     */
    public static void clearMusics() {
        musics.forEach((s, sound) -> sound.delete());
        musics.clear();
    }

    /**
     * Deja de reproducir la musica, en este caso si hay mas se van a desactivar todas.
     */
    private static void stopMusic() {
        musics.forEach((m, musicReproducing) -> musicReproducing.stop());
    }

    /**
     * Destruye el sonido creado en OpenAL y libera memoria.
     */
    public void delete() {
        alDeleteSources(sourceId);
        alDeleteBuffers(bufferId);
    }

    /**
     * Reproduce un sonido. Primero checkea si se esta reproduciendo el mismo sonido, si es asi crea el mismo sonido y lo
     * reproduce, ya que en el AO cuando se reproduce el mismo sonido, se reproduce varias veces por encima del otro. En caso
     * contrario, se comienza a reproducir el sonido 1 sola vez.
     */
    public void play() {
        if (alGetSourcei(sourceId, AL_SOURCE_STATE) == AL_PLAYING) new Sound(this.filepath, false).play();
        else {
            if (!isPlaying) isPlaying = true;
            alSourcePlay(sourceId);
        }
    }

    /**
     * Detiene el sonido que se esta reproduciendo.
     */
    public void stop() {
        if (isPlaying) {
            alSourceStop(sourceId);
            isPlaying = false;
        }
    }

    /**
     * @return True si esta reproduciendose el sonido, falso en caso contrario.
     */
    public boolean isPlaying() {
        if (alGetSourcei(sourceId, AL_SOURCE_STATE) == AL_STOPPED) isPlaying = false;
        return isPlaying;
    }

}