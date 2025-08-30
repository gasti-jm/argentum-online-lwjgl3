package org.aoclient.engine.audio;

import org.aoclient.engine.Window;

import java.io.File;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.aoclient.engine.utils.GameData.options;
import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_decode_filename;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.libc.LibCStdlib.free;

/**
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
    public static final int MAX_SOUNDS = 30;
    public static final int MAX_MUSIC = 2;

    private static final Map<String, Sound> sounds = new HashMap<>();
    private static final Map<String, Sound> musics = new HashMap<>();
    public static DecodedSoundData preloadedMusic = null;
    private final String filepath;
    private int bufferId = -1, sourceId = -1;
    private boolean isPlaying = false;
    /** Indica si el sonido se cargo y esta disponible para ser utilizado. */
    private boolean validSound = false;

    /**
     * Inicializa un objeto de sonido a partir de un archivo especificado. Este constructor carga y procesa el archivo de audio,
     * configurando las propiedades de reproduccion segun los parametros proporcionados. Si el sistema de audio no esta
     * disponible, se omite la carga.
     *
     * @param filepath ruta del archivo de audio que se desea cargar
     * @param loops    indica si el sonido debe reproducirse en bucle (true para bucle, false de lo contrario)
     */
    public Sound(String filepath, boolean loops) {
        // Verifica si el audio esta disponible antes de cargar el sonido
        if (!Window.INSTANCE.isAudioAvailable()) {
            System.out.println("Audio not available, skipping sound loading: " + filepath);
            this.filepath = filepath;
            return;
        }

        this.filepath = filepath;

        // Allocate space to store the return information from stb
        stackPush();
        IntBuffer channelsBuffer = stackMallocInt(1);
        stackPush();
        IntBuffer sampleRateBuffer = stackMallocInt(1);

        ShortBuffer rawAudioBuffer = stb_vorbis_decode_filename(filepath, channelsBuffer, sampleRateBuffer);

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

        try {
            bufferId = alGenBuffers();
            alBufferData(bufferId, format, rawAudioBuffer, sampleRate);

            // Generate the source
            sourceId = alGenSources();

            alSourcei(sourceId, AL_BUFFER, bufferId);
            alSourcei(sourceId, AL_LOOPING, loops ? 1 : 0); // loop
            alSourcei(sourceId, AL_POSITION, 0); // posicion
            alSourcef(sourceId, AL_GAIN, 1f); // configuracion de volumen

            validSound = true;
        } catch (Exception e) {
            System.err.println("Error initializing OpenAL for sound: " + filepath + " - " + e.getMessage());
            validSound = false;
        }

        // Free stb raw audio buffer
        free(rawAudioBuffer);
    }

    /**
     * Constructor de la clase Sound que inicializa un objeto de sonido con los datos de un archivo previamente decodificado.
     * Verifica si el sistema tiene audio disponible antes de cargar el sonido.
     *
     * @param filepath ruta al archivo de sonido
     * @param data     datos del sonido previamente decodificados, incluyendo formato, datos PCM y frecuencia de muestreo
     */
    public Sound(String filepath, DecodedSoundData data) {
        // Verifica si el audio esta disponible antes de cargar el sonido
        if (!Window.INSTANCE.isAudioAvailable()) {
            System.out.println("Audio not available, skipping sound loading: " + filepath);
            this.filepath = filepath;
            return;
        }

        this.filepath = filepath;

        try {
            bufferId = alGenBuffers();
            alBufferData(bufferId, data.format, data.pcm, data.sampleRate);
            sourceId = alGenSources();

            alSourcei(sourceId, AL_BUFFER, bufferId);
            alSourcei(sourceId, AL_LOOPING, 1);
            alSourcei(sourceId, AL_POSITION, 0);
            alSourcef(sourceId, AL_GAIN, 1f);

            validSound = true;
        } catch (Exception e) {
            System.err.println("Error initializing OpenAL for pre-decoded sound: " + filepath + " - " + e.getMessage());
            validSound = false;
        }

        free(data.pcm);
    }

    public static Sound getSound(String soundFile) {
        File file = new File(soundFile);
        if (sounds.containsKey(file.getAbsolutePath())) return sounds.get(file.getAbsolutePath());
        else System.out.println("Sound file not added '" + soundFile + "'");
        return null;
    }

    /**
     * Reproduce un sonido especificado por su nombre de archivo.
     * <p>
     * Este metodo verifica si el sistema de sonido esta habilitado y si el audio esta disponible antes de intentar reproducir el
     * sonido. Si el sonido ya esta cargado en memoria, lo reproduce directamente. En caso contrario, lo carga y luego lo
     * reproduce.
     *
     * @param soundName el nombre del archivo de sonido que se desea reproducir, ubicado en la carpeta de recursos7
     *                  {@code resources/sounds/}.
     */
    public static void playSound(String soundName) {
        if (options.isSound() && Window.INSTANCE.isAudioAvailable()) {
            File file = new File("resources/sounds/" + soundName);
            if (sounds.containsKey(file.getAbsolutePath())) sounds.get(file.getAbsolutePath()).play();
            else addSound("resources/sounds/" + soundName).play();
        }
    }

    /**
     * Reproduce musica desde un archivo especificado en la ruta de recursos del sistema.
     * <p>
     * Este metodo verifica inicialmente si el sistema de audio esta disponible. Si no lo esta, genera un mensaje en consola
     * indicando que el audio no esta disponible y cancela la reproduccion. En caso de que el audio este habilitado, detiene
     * cualquier reproduccion actualmente activa mediante el metodo {@code stopMusic()} y prepara el archivo de musica
     * especificado. Si la musica ya se encuentra cargada previamente en memoria, esta es reproducida directamente. Si no lo esta,
     * se intenta cargar y reproducir en un hilo separado.
     * <p>
     * Este metodo maneja automaticamente errores en el proceso de carga y reproduccion, mostrando mensajes en consola en caso de
     * fallos.
     *
     * @param musicName el nombre del archivo de musica que se desea reproducir, ubicado en la carpeta de recursos
     *                  {@code resources/music/}.
     */
    public static void playMusic(String musicName) {
        if (!Window.INSTANCE.isAudioAvailable()) {
            System.out.println("Audio not available, skipping music playback: " + musicName);
            return;
        }

        stopMusic();

        File file = new File("resources/music/" + musicName);

        if (musics.containsKey(file.getAbsolutePath())) {
            if (options.isMusic()) musics.get(file.getAbsolutePath()).play();
        } else {
            if (options.isMusic()) {
                // Lee el ogg en un hilo aparte
                new Thread(() -> {
                    try {
                        preloadedMusic = DecodedSoundData.decodeOgg(file.getAbsolutePath());
                    } catch (Exception e) {
                        System.err.println("Error decoding music: " + file.getAbsolutePath() + " - " + e.getMessage());
                        preloadedMusic = null;
                    }
                }).start();
            }
        }
    }

    /**
     * Renderiza y controla la reproduccion de la musica precargada en el sistema.
     * <p>
     * Este metodo verifica si existe una musica previamente cargada y si el sistema de audio esta disponible. En caso de ser
     * posible, se detiene cualquier reproduccion actual antes de proceder con la nueva musica. Si la musica ya se encuentra
     * registrada en la lista de musicas, esta se reproduce. De lo contrario, se agrega a la lista y empieza su reproduccion.
     * Finalmente, el objeto {@code preloadedMusic} se limpia para prepararse para una nueva carga.
     * <p>
     * Si el audio no esta disponible, la musica precargada se limpia sin ejecutarla.
     * <p>
     * Este metodo asiste en la gestion dinamica de la musica en la aplicacion, asegurando que solo se reproduzca la musica
     * adecuada y gestionando su estado conforme a las condiciones actuales del sistema.
     */
    public static void renderMusic() {
        if (preloadedMusic != null && Window.INSTANCE.isAudioAvailable()) {
            stopMusic();
            if (musics.containsKey(preloadedMusic.filepath)) {
                if (options.isMusic()) musics.get(preloadedMusic.filepath).play();
            } else if (options.isMusic()) addMusic(preloadedMusic.filepath).play();
            preloadedMusic = null; // Establece como null para que se prepare para cargar otro archivo
        } else if (preloadedMusic != null && !Window.INSTANCE.isAudioAvailable())
            preloadedMusic = null; // Si no hay audio disponible, limpia la musica precargada
    }

    /**
     * Elimina todos los sonidos actualmente cargados en la aplicacion.
     * <p>
     * Este metodo recorre la coleccion de sonidos almacenados en memoria, asegurando que cada instancia de sonido libere
     * correctamente sus recursos mediante el metodo {@code delete()}. Posteriormente, limpia la coleccion, dejandola vacia para
     * su reutilizacion futura.
     * <p>
     * Es util para liberar recursos de audio y memoria antes de cerrar la aplicacion o cuando los sonidos ya no son necesarios,
     * reduciendo el consumo de memoria y evitando posibles fugas de recursos.
     */
    public static void clearSounds() {
        sounds.forEach((s, sound) -> sound.delete());
        sounds.clear();
    }

    /**
     * Elimina todos los recursos de las musicas actualmente cargadas.
     * <p>
     * Este metodo recorre la coleccion de musicas almacenadas, asegurando que cada instancia de musica libere sus recursos
     * mediante el metodo {@code delete()}. Posteriormente, limpia la coleccion de musicas, quedando vacia para futuros usos.
     * <p>
     * Es util para liberar memoria y recursos de audio cuando ya no se requiere el uso de las musicas previamente cargadas.
     */
    public static void clearMusics() {
        musics.forEach((s, sound) -> sound.delete());
        musics.clear();
    }

    /**
     * Reproduce un sonido. Primero checkea si se esta reproduciendo el mismo sonido, si es asi crea el mismo sonido y lo
     * reproduce, ya que en el AO cuando se reproduce el mismo sonido, se reproduce varias veces por encima del otro. En caso
     * contrario, se comienza a reproducir el sonido 1 sola vez.
     */
    public void play() {
        // Si el sonido no es valido o no hay audio disponible
        if (!validSound || !Window.INSTANCE.isAudioAvailable()) return;
        try {
            if (alGetSourcei(sourceId, AL_SOURCE_STATE) == AL_PLAYING) {
                new Sound(filepath, false).play();
            } else {
                if (!isPlaying) isPlaying = true;
                alSourcePlay(sourceId);
            }
        } catch (Exception e) {
            System.err.println("Error playing sound: " + filepath + " - " + e.getMessage());
        }
    }

    /**
     * Detiene la reproduccion del sonido actual.
     * <p>
     * Este metodo verifica si el sonido es valido y si el sistema de audio esta disponible antes de intentar detener la
     * reproduccion. Si el sonido se encuentra reproduciendose, se detiene utilizando la fuente de audio asociada y actualiza la
     * bandera interna que indica que el sonido ya no se esta reproduciendo.
     * <p>
     * En caso de que ocurra un error durante el proceso, la excepcion es capturada y un mensaje de error es mostrado por consola,
     * incluyendo informacion sobre el archivo de sonido involucrado.
     */
    public void stop() {
        if (!validSound || !Window.INSTANCE.isAudioAvailable()) return;
        try {
            if (isPlaying) {
                alSourceStop(sourceId);
                isPlaying = false;
            }
        } catch (Exception e) {
            System.err.println("Error stopping sound: " + filepath + " - " + e.getMessage());
        }
    }

    /**
     * Verifica si el sonido actualmente se esta reproduciendo.
     * <p>
     * Este metodo comprueba si el sonido es valido y si el sistema de audio esta disponible. En caso afirmativo, consulta el
     * estado del sonido. Si el estado es "AL_STOPPED", actualiza la bandera interna que indica si el sonido esta reproduciendose.
     * En caso de que ocurra una excepcion, se maneja el error y se informa en consola.
     *
     * @return {@code true} si el sonido esta reproduciendose, {@code false} en caso contrario o si ocurre un error.
     */
    public boolean isPlaying() {
        if (!validSound || !Window.INSTANCE.isAudioAvailable()) return false;
        try {
            if (alGetSourcei(sourceId, AL_SOURCE_STATE) == AL_STOPPED) isPlaying = false;
            return isPlaying;
        } catch (Exception e) {
            System.err.println("Error checking sound status: " + filepath + " - " + e.getMessage());
            return false;
        }
    }

    public boolean isValid() {
        return validSound;
    }

    /**
     * Crea un objeto de tipo {@code Sound} a partir de los datos de sonido decodificados.
     *
     * @param filepath ruta al archivo del sonido que se esta creando
     * @param data     datos de sonido decodificados que seran utilizados para crear el sonido
     * @return el objeto {@code Sound} creado utilizando la ruta del archivo y los datos decodificados proporcionados
     */
    private static Sound createSoundFromDecoded(String filepath, DecodedSoundData data) {
        return new Sound(filepath, data);
    }

    /**
     * Agrega un sonido a la lista de sonidos disponibles. Si el sonido ya existe, lo recupera del registro. En caso contrario,
     * crea un nuevo sonido y lo agrega al mapa de sonidos. Si el limite de sonidos permitidos (MAX_SOUNDS) se alcanza, limpia los
     * sonidos existentes antes de agregar el nuevo.
     *
     * @param soundFile ruta del archivo de sonido que se desea cargar
     * @return el objeto {@code Sound} asociado al archivo de sonido proporcionado
     */
    private static Sound addSound(String soundFile) {
        File file = new File(soundFile);
        if (sounds.containsKey(file.getAbsolutePath())) return sounds.get(file.getAbsolutePath());
        else {
            if (sounds.size() == MAX_SOUNDS) clearSounds();
            Sound sound = new Sound(file.getAbsolutePath(), false);
            sounds.put(file.getAbsolutePath(), sound);
            return sound;
        }
    }

    /**
     * Agrega una musica a la lista de musicas disponibles. Si la musica ya existe, la recupera del registro. En caso contrario,
     * crea una nueva musica y la agrega al mapa de musicas. Si el limite de musicas permitidas (MAX_MUSIC) se alcanza, limpia las
     * musicas existentes antes de agregar la nueva.
     *
     * @param soundFile ruta del archivo de musica que se desea cargar
     * @return el objeto {@code Sound} asociado al archivo de musica proporcionado
     */
    private static Sound addMusic(String soundFile) {
        if (musics.containsKey(soundFile)) return musics.get(soundFile);
        else {
            if (musics.size() == MAX_MUSIC) clearMusics();
            Sound sound = createSoundFromDecoded(soundFile, preloadedMusic);
            musics.put(soundFile, sound);
            return sound;
        }
    }

    /**
     * Detiene la reproduccion de todas las musicas actualmente activas en el sistema.
     * <p>
     * Comprueba si el audio esta disponible. En caso afirmativo, se recorre la coleccion de musicas y se detienen todas las que
     * se estan reproduciendo.
     */
    public static void stopMusic() {
        if (Window.INSTANCE.isAudioAvailable())
            musics.forEach((m, musicReproducing) -> musicReproducing.stop());
    }

    /**
     * Elimina los recursos de audio asociados a este objeto de sonido.
     * <p>
     * Este metodo verifica si el sonido es valido y si el sistema de audio esta disponible. Si ambas condiciones se cumplen,
     * intenta eliminar las fuentes de audio y los buffers asociados. En caso de que ocurra un error durante la eliminacion, se
     * captura la excepcion y se imprime un mensaje de error en la consola.
     * <p>
     * Tras la operacion, los identificadores de fuente y buffer se reinician a -1, y el estado del sonido se marca como
     * invalido.
     */
    private void delete() {
        if (validSound && Window.INSTANCE.isAudioAvailable()) {
            try {
                if (sourceId != -1) alDeleteSources(sourceId);
                if (bufferId != -1) alDeleteBuffers(bufferId);
            } catch (Exception e) {
                System.err.println("Error deleting audio resources: " + e.getMessage());
            }
        }
        sourceId = -1;
        bufferId = -1;
        validSound = false;
    }

}