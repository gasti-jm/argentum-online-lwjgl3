package org.aoclient.engine.game;

import org.aoclient.engine.game.models.E_KeyType;

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
        mappedKeys = new int[E_KeyType.values().length];
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
        mappedKeys[E_KeyType.mKeyUp.ordinal()] = GLFW_KEY_W;
        mappedKeys[E_KeyType.mKeyDown.ordinal()] = GLFW_KEY_S;
        mappedKeys[E_KeyType.mKeyLeft.ordinal()] = GLFW_KEY_A;
        mappedKeys[E_KeyType.mKeyRight.ordinal()] = GLFW_KEY_D;

        mappedKeys[E_KeyType.mKeyToggleMusic.ordinal()] = GLFW_KEY_M;
        mappedKeys[E_KeyType.mKeyToggleSound.ordinal()] = GLFW_KEY_S;
        mappedKeys[E_KeyType.mKeyToggleFxs.ordinal()] = GLFW_KEY_F;

        mappedKeys[E_KeyType.mKeyRequestRefresh.ordinal()] = GLFW_KEY_L;

        mappedKeys[E_KeyType.mKeyToggleNames.ordinal()] = GLFW_KEY_N;

        mappedKeys[E_KeyType.mKeyGetObject.ordinal()] = GLFW_KEY_Q;
        mappedKeys[E_KeyType.mKeyEquipObject.ordinal()] = GLFW_KEY_E;

        mappedKeys[E_KeyType.mKeyTamAnimal.ordinal()] = GLFW_KEY_D;
        mappedKeys[E_KeyType.mKeySteal.ordinal()] = GLFW_KEY_R;
        mappedKeys[E_KeyType.mKeyToggleSafeMode.ordinal()] = GLFW_KEY_KP_MULTIPLY;
        mappedKeys[E_KeyType.mKeyToggleResuscitationSafe.ordinal()] = GLFW_KEY_END;

        mappedKeys[E_KeyType.mKeyHide.ordinal()] = GLFW_KEY_O;
        mappedKeys[E_KeyType.mKeyDropObject.ordinal()] = GLFW_KEY_T;
        mappedKeys[E_KeyType.mKeyUseObject.ordinal()] = GLFW_KEY_SPACE;
        mappedKeys[E_KeyType.mKeyAttack.ordinal()] = GLFW_KEY_LEFT_CONTROL;

        mappedKeys[E_KeyType.mKeyTalk.ordinal()] = GLFW_KEY_ENTER;
        mappedKeys[E_KeyType.mKeyTalkWithGuild.ordinal()] = GLFW_KEY_DELETE;
        mappedKeys[E_KeyType.mKeyTakeScreenShot.ordinal()] = GLFW_KEY_F2;

        mappedKeys[E_KeyType.mKeyShowOptions.ordinal()] = GLFW_KEY_F5;
        mappedKeys[E_KeyType.mKeyMeditate.ordinal()] = GLFW_KEY_F6;
        mappedKeys[E_KeyType.mKeyCastSpellMacro.ordinal()] = GLFW_KEY_F7;
        mappedKeys[E_KeyType.mKeyWorkMacro.ordinal()] = GLFW_KEY_F8;

        mappedKeys[E_KeyType.mKeyAutoMove.ordinal()] = GLFW_KEY_TAB;
        mappedKeys[E_KeyType.mKeyExitGame.ordinal()] = GLFW_KEY_ESCAPE;
    }

    /**
     * @throws IOException: Puede suceder que no encuentre el archivo o que tenga menos informacion de lo que se esperaba desde el
     *                      programa (en caso de que agregemos una tecla nueva).
     * @desc: Carga todas las teclas guardadas por el usuario.
     */
    public void loadBindKeys() throws IOException {
        RandomAccessFile f = new RandomAccessFile("resources/keys.bin", "rw");
        f.seek(0);
        for (int i = 0; i < E_KeyType.values().length; i++) mappedKeys[i] = f.readInt();
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
            for (int i = 0; i < E_KeyType.values().length; i++)
                f.writeInt(mappedKeys[i]);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @desc: Permite bindear una nueva tecla.
     */
    public void changeBindedKey(E_KeyType key, int newKey) {
        mappedKeys[key.ordinal()] = newKey;
    }

    /**
     * @param key Tecla del enumerador E_KeyType
     * @return Entero que representa a una tecla de nuestra ventana GLFW.
     */
    public int getBindedKey(E_KeyType key) {
        return mappedKeys[key.ordinal()];
    }

    /**
     * @param keyCode entero que representa a una tecla de nuestra ventana GLFW.
     * @return Devuelve un E_KeyType segun la tecla que tenga bindeada ese entero pasado por parametro. Si no existe devuelve
     * null.
     */
    public E_KeyType getKeyPressed(int keyCode) {
        for (int i = 0; i < mappedKeys.length; i++)
            if (keyCode == mappedKeys[i]) return E_KeyType.values()[i];
        return null;
    }

}
