package org.aoclient.engine.game.bindkeys;

import org.aoclient.engine.game.bindkeys.actions.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Represents keys associated with game actions with customizable configuration. Keys can be loaded from file, saved and reset to
 * default values.
 */

public enum Key {

    // Movimiento
    UP(GLFW_KEY_W, new UpMovement()),
    DOWN(GLFW_KEY_S, new DownMovement()),
    LEFT(GLFW_KEY_A, new LeftMovement()),
    RIGHT(GLFW_KEY_D, new RightMovement()),
    // Audio
    TOGGLE_MUSIC(GLFW_KEY_M, new ToggleMusic()),
    TOGGLE_SOUND(GLFW_KEY_F1, new ToggleSound()),
    TOGGLE_FXS(GLFW_KEY_F, new ToggleFXs()),
    // Acciones del juego
    REQUEST_REFRESH(GLFW_KEY_L, new RequestRefresh()),
    TOGGLE_NAMES(GLFW_KEY_N, new ToggleNames()),
    GET_OBJECT(GLFW_KEY_Q, new GetObject()),
    EQUIP_OBJECT(GLFW_KEY_E, new EquipObject()),
    TAME_ANIMAL(GLFW_KEY_Y, new TameAnimal()),
    STEAL(GLFW_KEY_R, new Steal()),
    TOGGLE_SAFE_MODE(GLFW_KEY_KP_MULTIPLY, new ToggleSafeMode()),
    TOGGLE_RESUSCITATION_SAFE(GLFW_KEY_END, new ToggleResuscitationSafe()),
    HIDE(GLFW_KEY_O, new Hide()),
    DROP_OBJECT(GLFW_KEY_T, new DropObject()),
    USE_OBJECT(GLFW_KEY_SPACE, new UseObject()),
    ATTACK(GLFW_KEY_LEFT_CONTROL, new Attack()),
    TALK(GLFW_KEY_ENTER, new Talk()),
    TALK_WITH_GUILD(GLFW_KEY_DELETE, new TalkWithGuild()),
    // Sistemas
    TAKE_SCREENSHOT(GLFW_KEY_F2, new TakeScreenshot()),
    SHOW_OPTIONS(GLFW_KEY_F5, new ShowOptions()),
    MEDITATE(GLFW_KEY_F6, new Meditate()),
    CAST_SPELL_MACRO(GLFW_KEY_F7, new CastSpellMacro()),
    WORK_MACRO(GLFW_KEY_F8, new WorkMacro()),
    AUTO_MOVE(GLFW_KEY_TAB, new AutoMove()),
    EXIT_GAME(GLFW_KEY_ESCAPE, new ExitGameKey());

    private static final String KEYS_CONFIG_FILE = "resources/keys.properties";
    private static final Logger LOGGER = Logger.getLogger(Key.class.getName());

    /** Mapa que almacena la correspondencia entre las teclas del enum y sus codigos asociados. */
    private static final Map<Key, Integer> keyCodeMap = new EnumMap<>(Key.class);
    /** Mapa que almacena la correspondencia entre codigos de tecla y sus acciones asociadas. */
    private static final Map<Integer, Key> codeToKeyMap = new HashMap<>();

    static {
        // Inicializa los valores predeterminados despues de que se crean las constantes de enum
        for (Key key : values())
            keyCodeMap.put(key, key.defaultKeyCode);
        loadKeys();
    }

    /** Valor de keyCode predeterminado para esta asignacion de tecla. */
    private final int defaultKeyCode;
    private boolean preparedToBind;
    private KeyAction a;

    Key(int defaultKeyCode, KeyAction action) {
        this.defaultKeyCode = defaultKeyCode;
        this.preparedToBind = false;
        this.a = action;
    }

    public static Key getKey(int keyCode) {
        return codeToKeyMap.get(keyCode);
    }

    public static boolean containsKey(int keyCode) {
        return codeToKeyMap.containsKey(keyCode);
    }


    /**
     * Carga la configuracion de teclas desde un archivo de propiedades.
     * <p>
     * Este metodo verifica si el archivo de configuracion definido por {@code KEYS_CONFIG_FILE} existe. Si no existe, se cargan
     * los valores predeterminados llamando a {@code loadDefaultKeys()} y se registra un mensaje informativo.
     * <p>
     * Si el archivo existe, intenta leerlo utilizando un {@code FileInputStream}, cargando las propiedades definidas en el mismo.
     * Recorre cada tecla definida en el enum y busca su codigo asociado en las propiedades cargadas.
     * <ul>
     * <li>Si encuentra un valor valido para una tecla, lo asigna al mapa de codigos {@code keyCodeMap}.
     * <li>Si el valor es invalido (por ejemplo, formato incorrecto o codigo no permitido), asigna el codigo predeterminado
     * de la tecla y registra una advertencia.
     * </ul>
     * Si ocurre un error durante el proceso de lectura del archivo, se registra el error y se cargan las teclas predeterminadas
     * como resguardo.
     * <p>
     * Al finalizar, actualiza los mapas relacionados llamando al metodo {@code updateMaps()}.
     */
    public static void loadKeys() {
        if (!Files.exists(Paths.get(KEYS_CONFIG_FILE))) {
            LOGGER.info("Configuration file not found, using default values");
            loadDefaultKeys();
            return;
        }

        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(KEYS_CONFIG_FILE)) {
            properties.load(fis);
            for (Key key : values()) {
                String keyCodeStr = properties.getProperty(key.name());
                if (keyCodeStr != null) {
                    try {
                        int keyCode = Integer.parseInt(keyCodeStr);
                        if (isValidKeyCode(keyCode)) keyCodeMap.put(key, keyCode);
                        else {
                            LOGGER.warning("Invalid key code for " + key.name() + ": " + keyCode);
                            keyCodeMap.put(key, key.defaultKeyCode);
                        }
                    } catch (NumberFormatException e) {
                        LOGGER.warning("Error parsing key code for " + key.name() + ": " + keyCodeStr);
                        keyCodeMap.put(key, key.defaultKeyCode);
                    }
                } else keyCodeMap.put(key, key.defaultKeyCode);
            }
            updateMaps();
        } catch (IOException e) {
            LOGGER.severe("Error loading key configuration: " + e.getMessage());
            loadDefaultKeys();
        }
    }

    /**
     * Checkea si ya hay alguna tecla en proceso de bindeado.
     * Esto es para que no creemos varios procesos de bindeado en varias teclas a la vez.
     */
    public static boolean checkIsBinding() {
        for(Key key: values()) {
            if(key.getPreparedToBind()) {
                return true;
            }
        }

        return false;
    }

    public static Key getKeyBinding() {
        for(Key key: values()) {
            if(key.getPreparedToBind()) {
                return key;
            }
        }
        return null;
    }

    /**
     * Guarda la configuracion actual de las teclas en un archivo de propiedades.
     * <p>
     * Este metodo toma las teclas y sus codigos asociados definidos actualmente en {@code keyCodeMap}, los convierte en pares
     * clave-valor y los almacena en un archivo de propiedades definido por la constante {@code KEYS_CONFIG_FILE}.
     * <p>
     * Si ocurre un error durante el proceso de guardado, se registra un mensaje en el logger para notificar al usuario.
     */
    public static void saveKeys() {
        Properties properties = new Properties();
        keyCodeMap.forEach((key, code) ->
                properties.setProperty(key.name(), String.valueOf(code)));
        try (FileOutputStream fos = new FileOutputStream(KEYS_CONFIG_FILE)) {
            properties.store(fos, "Key Configuration for Argentum Online LWJGL3\nMapping actions to GLFW keycodes");
        } catch (IOException e) {
            LOGGER.severe("Error saving key configuration: " + e.getMessage());
        }
    }

    /**
     * Carga las teclas predeterminadas y actualiza los mapas de mapeo.
     * <p>
     * Este metodo asigna a cada tecla definida en el enum su codigo de tecla predeterminado, colocando estas asociaciones en el
     * mapa {@code keyCodeMap}. Posteriormente, actualiza el resto de los mapas relacionados llamando al metodo
     * {@code updateMaps()} y almacena la configuracion actual utilizando {@code saveKeys()}.
     */
    public static void loadDefaultKeys() {
        for (Key key : values())
            keyCodeMap.put(key, key.defaultKeyCode);
        updateMaps();
        saveKeys();
    }

    /**
     * Actualiza los mapas de mapeo de teclas.
     * <p>
     * Este metodo borra completamente el contenido del mapa {@code codeToKeyMap} y lo reconstruye utilizando los valores actuales
     * del mapa {@code keyCodeMap}. Itera sobre las entradas de {@code keyCodeMap}, invirtiendo las claves y valores para
     * almacenarlos en {@code codeToKeyMap}.
     */
    private static void updateMaps() {
        codeToKeyMap.clear();
        keyCodeMap.forEach((key, code) -> codeToKeyMap.put(code, key));
    }

    /**
     * Valida si un keyCode es valido para GLFW.
     */
    private static boolean isValidKeyCode(int keyCode) {
        return keyCode >= GLFW_KEY_SPACE && keyCode <= GLFW_KEY_LAST;
    }

    public int getKeyCode() {
        return keyCodeMap.get(this);
    }

    public boolean setKeyCode(int newKeyCode) {
        if (!isValidKeyCode(newKeyCode)) return false;

        // Verifica conflictos
        Key existingKey = codeToKeyMap.get(newKeyCode);
        if (existingKey != null && existingKey != this) return false;

        keyCodeMap.put(this, newKeyCode);
        updateMaps();
        return true;
    }

    public void resetToDefault() {
        keyCodeMap.put(this, defaultKeyCode);
        updateMaps();
    }

    /**
     * Accion para la key bindeada.
     */
    public void playAction() {
        a.action();
    }

    public int getDefaultKeyCode() {
        return defaultKeyCode;
    }

    public boolean getPreparedToBind() {
        return preparedToBind;
    }

    public void setPreparedToBind(boolean value) {
        this.preparedToBind = value;
    }

}