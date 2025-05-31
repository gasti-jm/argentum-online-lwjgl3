package org.aoclient.engine.game.models;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Representa una enumeracion de teclas asociadas a diferentes acciones dentro del juego. Cada entrada de la enumeracion describe
 * una accion y su tecla por defecto. Las teclas pueden ser personalizadas por el usuario, y los cambios se registran y guardan en
 * el archivo de configuracion en formato Properties {@code keys.properties}.
 * <p>
 * La clase permite cargar teclas desde un archivo, guardar configuraciones personalizadas, y restablecer valores predeterminados.
 * Adicionalmente, valida la asignacion de teclas asegurando que no haya conflictos entre diferentes acciones.
 */

public enum Key {

    UP(GLFW_KEY_W),
    DOWN(GLFW_KEY_S),
    LEFT(GLFW_KEY_A),
    RIGHT(GLFW_KEY_D),
    TOGGLE_MUSIC(GLFW_KEY_M),
    TOGGLE_SOUND(GLFW_KEY_S),
    TOGGLE_FXS(GLFW_KEY_F),
    REQUEST_REFRESH(GLFW_KEY_L),
    TOGGLE_NAMES(GLFW_KEY_N),
    GET_OBJECT(GLFW_KEY_Q),
    EQUIP_OBJECT(GLFW_KEY_E),
    TAME_ANIMAL(GLFW_KEY_D),
    STEAL(GLFW_KEY_R),
    TOGGLE_SAFE_MODE(GLFW_KEY_KP_MULTIPLY),
    TOGGLE_RESUSCITATION_SAFE(GLFW_KEY_END),
    HIDE(GLFW_KEY_O),
    DROP_OBJECT(GLFW_KEY_T),
    USE_OBJECT(GLFW_KEY_SPACE),
    ATTACK(GLFW_KEY_LEFT_CONTROL),
    TALK(GLFW_KEY_ENTER),
    TALK_WITH_GUILD(GLFW_KEY_DELETE),
    TAKE_SCREENSHOT(GLFW_KEY_F2),
    SHOW_OPTIONS(GLFW_KEY_F5),
    MEDITATE(GLFW_KEY_F6),
    CAST_SPELL_MACRO(GLFW_KEY_F7),
    WORK_MACRO(GLFW_KEY_F8),
    AUTO_MOVE(GLFW_KEY_TAB),
    EXIT_GAME(GLFW_KEY_ESCAPE);

    /** Nombre del archivo que almacena la configuracion de las teclas en formato Properties. */
    private static final String KEYS_CONFIG_FILE = "resources/keys.properties";
    /** Mapa que relaciona cada codigo de tecla con su correspondiente instancia de Key. */
    private static final Map<Integer, Key> keys = new HashMap<>();

    static {
        loadKeys();
    }

    private final int defaultKeyCode;
    private int keyCode;

    Key(int keyCode) {
        this.defaultKeyCode = keyCode;
        this.keyCode = keyCode;
    }

    /**
     * Obtiene la tecla asociada a un codigo especifico.
     *
     * @param keyCode codigo de la tecla
     * @return la tecla asociada, o null si no hay ninguna
     */
    public static Key getKey(int keyCode) {
        return keys.get(keyCode);
    }

    /**
     * Verifica si existe una tecla asociada al codigo proporcionado.
     *
     * @param keyCode el codigo de la tecla que se desea comprobar
     * @return {@code true} si el codigo de tecla existe en el mapeo, {@code false} de lo contrario
     */
    public static boolean containsKey(int keyCode) {
        return keys.containsKey(keyCode);
    }

    /**
     * Carga las teclas por defecto segun lo definido en el enum Key.
     */
    public static void loadDefaultKeys() {
        for (Key key : Key.values())
            key.resetToDefault();
        updateKeyMap();
        saveKeys();
    }

    /**
     * Carga las teclas guardadas desde un archivo Properties.
     */
    public static void loadKeys() {
        Properties properties = new Properties();

        try (FileInputStream fis = new FileInputStream(KEYS_CONFIG_FILE)) {
            properties.load(fis);

            for (Key key : Key.values()) {
                String keyName = key.name();
                String keyCodeStr = properties.getProperty(keyName);

                if (keyCodeStr != null) {
                    try {
                        int keyCode = Integer.parseInt(keyCodeStr);
                        // Verifica que el codigo de la tecla leido este en un rango razonable para teclas GLFW
                        if (keyCode >= 0 && keyCode <= 1000) key.keyCode = keyCode;
                        else key.resetToDefault();
                    } catch (NumberFormatException e) {
                        System.err.println("Error al convertir el codigo de tecla para " + keyName + ": " + e.getMessage());
                        key.resetToDefault();
                    }
                }
            }
            updateKeyMap();
        } catch (IOException e) {
            System.err.println("No se pudo cargar la configuracion de teclas: " + e.getMessage());
            loadDefaultKeys();
        }
    }

    /**
     * Guarda la configuracion actual de teclas en un archivo Properties.
     */
    public static void saveKeys() {
        Properties properties = new Properties();
        for (Key key : Key.values())
            properties.setProperty(key.name(), Integer.toString(key.getKeyCode()));
        try (FileOutputStream fos = new FileOutputStream(KEYS_CONFIG_FILE)) {
            properties.store(fos, "Key Configuration for Argentum Online LWJGL3\nMapping actions to GLFW keycodes");
        } catch (IOException e) {
            System.err.println("Could not save key configuration: " + e.getMessage());
        }
    }

    /**
     * Actualiza el mapeo de teclas.
     * <p>
     * Reinicia el mapa de teclas actual eliminando todas las asociaciones previas y posteriormente lo rellena con las teclas
     * actuales basadas en los valores definidos en el enumerado {@code Key}. Se utiliza para sincronizar los codigos de tecla con
     * sus respectivas acciones en el sistema.
     * <p>
     * Es una operacion clave cuando se realizan cambios en la configuracion de teclas o se requiere restablecer los mapeos a
     * partir de los valores definidos en el enumerado.
     */
    private static void updateKeyMap() {
        keys.clear();
        for (Key key : Key.values())
            keys.put(key.getKeyCode(), key);
    }

    public int getKeyCode() {
        return keyCode;
    }

    /**
     * Cambia la tecla asignada a esta accion.
     *
     * @param newKeyCode codigo de la nueva tecla
     * @return true si la asignacion fue exitosa, false si la tecla ya esta asignada a otra accion
     */
    public boolean setKeyCode(int newKeyCode) {
        // Verifica si la tecla ya esta asignada a otra accion
        Key existingKey = keys.get(newKeyCode);
        if (existingKey != null && existingKey != this) return false; // La tecla ya esta asignada a otra accion
        // Actualiza la tecla en el mapa y en el enum
        this.keyCode = newKeyCode;
        updateKeyMap();
        return true;
    }

    /**
     * Restaura el valor de la tecla asociada a su configuracion predeterminada.
     * <p>
     * Este metodo asigna el codigo de tecla por defecto {@code defaultKeyCode} al atributo {@code keyCode} para que la accion
     * vuelva a usar el mapeo original definido al iniciar el juego. Es util para restablecer configuraciones en caso de que el
     * jugador desee revertir cambios personalizados.
     */
    public void resetToDefault() {
        keyCode = defaultKeyCode;
    }

}

