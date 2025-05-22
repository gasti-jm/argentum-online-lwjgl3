package org.aoclient.engine.game;

import org.aoclient.engine.game.models.Key;

import java.io.IOException;
import java.io.RandomAccessFile;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Clase encargada de la gestion y configuracion de las teclas de control.
 * <p>
 * {@code BindKeys} mantiene un mapeo entre las distintas acciones posibles (definidas en el enumerador {@code E_KeyType}) y las
 * teclas asignadas a estas acciones. Este mapeo puede cargarse desde un archivo de configuracion, permitiendo a los jugadores
 * personalizar los controles segun sus preferencias.
 * <p>
 * Principales funcionalidades:
 * <ul>
 * <li>Cargar configuraciones de teclas por defecto
 * <li>Cargar configuraciones personalizadas desde archivos
 * <li>Guardar configuraciones de teclas modificadas
 * <li>Asignar nuevas teclas a acciones especificas
 * <li>Traducir entre codigos de teclas y acciones del juego
 * </ul>
 */

public enum BindKeys {

    INSTANCE;

    private final int[] mappedKeys;

    /**
     * @desc Constructor privado por singleton.
     */
    private BindKeys() {
        mappedKeys = new int[Key.values().length];
        try {
            loadBindKeys();
        } catch (IOException ex) {
            loadDefaultKeys();
            saveBindKeys();
        }
    }

    /**
     * @desc Carga una configuracion por defecto.
     */
    public void loadDefaultKeys() {
        mappedKeys[Key.UP.ordinal()] = GLFW_KEY_W;
        mappedKeys[Key.DOWN.ordinal()] = GLFW_KEY_S;
        mappedKeys[Key.LEFT.ordinal()] = GLFW_KEY_A;
        mappedKeys[Key.RIGHT.ordinal()] = GLFW_KEY_D;

        mappedKeys[Key.TOGGLE_MUSIC.ordinal()] = GLFW_KEY_M;
        mappedKeys[Key.TOGGLE_SOUND.ordinal()] = GLFW_KEY_S;
        mappedKeys[Key.TOGGLE_FXS.ordinal()] = GLFW_KEY_F;

        mappedKeys[Key.REQUEST_REFRESH.ordinal()] = GLFW_KEY_L;

        mappedKeys[Key.TOGGLE_NAMES.ordinal()] = GLFW_KEY_N;

        mappedKeys[Key.GET_OBJECT.ordinal()] = GLFW_KEY_Q;
        mappedKeys[Key.EQUIP_OBJECT.ordinal()] = GLFW_KEY_E;

        mappedKeys[Key.TAME_ANIMAL.ordinal()] = GLFW_KEY_D;
        mappedKeys[Key.STEAL.ordinal()] = GLFW_KEY_R;
        mappedKeys[Key.TOGGLE_SAFE_MODE.ordinal()] = GLFW_KEY_KP_MULTIPLY;
        mappedKeys[Key.TOGGLE_RESUSCITATION_SAFE.ordinal()] = GLFW_KEY_END;

        mappedKeys[Key.HIDE.ordinal()] = GLFW_KEY_O;
        mappedKeys[Key.DROP_OBJECT.ordinal()] = GLFW_KEY_T;
        mappedKeys[Key.USE_OBJECT.ordinal()] = GLFW_KEY_SPACE;
        mappedKeys[Key.ATTACK.ordinal()] = GLFW_KEY_LEFT_CONTROL;

        mappedKeys[Key.TALK.ordinal()] = GLFW_KEY_ENTER;
        mappedKeys[Key.TALK_WITH_GUILD.ordinal()] = GLFW_KEY_DELETE;
        mappedKeys[Key.TAKE_SCREENSHOT.ordinal()] = GLFW_KEY_F2;

        mappedKeys[Key.SHOW_OPTIONS.ordinal()] = GLFW_KEY_F5;
        mappedKeys[Key.MEDITATE.ordinal()] = GLFW_KEY_F6;
        mappedKeys[Key.CAST_SPELL_MACRO.ordinal()] = GLFW_KEY_F7;
        mappedKeys[Key.WORK_MACRO.ordinal()] = GLFW_KEY_F8;

        mappedKeys[Key.AUTO_MOVE.ordinal()] = GLFW_KEY_TAB;
        mappedKeys[Key.EXIT_GAME.ordinal()] = GLFW_KEY_ESCAPE;
    }

    /**
     * @throws IOException: Puede suceder que no encuentre el archivo o que tenga menos informacion de lo que se esperaba desde el
     *                      programa (en caso de que agregemos una tecla nueva).
     * @desc: Carga todas las teclas guardadas por el usuario.
     */
    public void loadBindKeys() throws IOException {
        RandomAccessFile f = new RandomAccessFile("resources/keys.bin", "rw");
        f.seek(0);
        for (int i = 0; i < Key.values().length; i++) mappedKeys[i] = f.readInt();
        f.close();
    }

    /**
     * @desc Guarda la configuracion de teclas.
     */
    public void saveBindKeys() {
        RandomAccessFile f;
        try {
            f = new RandomAccessFile("resources/keys.bin", "rw");
            f.seek(0);
            for (int i = 0; i < Key.values().length; i++)
                f.writeInt(mappedKeys[i]);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @desc: Permite bindear una nueva tecla.
     */
    public void changeBindedKey(Key key, int newKey) {
        mappedKeys[key.ordinal()] = newKey;
    }

    /**
     * @param key Tecla del enumerador E_KeyType
     * @return Entero que representa a una tecla de nuestra ventana GLFW.
     */
    public int getBindedKey(Key key) {
        return mappedKeys[key.ordinal()];
    }

    /**
     * @param keyCode entero que representa a una tecla de nuestra ventana GLFW.
     * @return Devuelve un E_KeyType segun la tecla que tenga bindeada ese entero pasado por parametro. Si no existe devuelve
     * null.
     */
    public Key getKeyPressed(int keyCode) {
        for (int i = 0; i < mappedKeys.length; i++)
            if (keyCode == mappedKeys[i]) return Key.values()[i];
        return null;
    }

}
