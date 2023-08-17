package org.aoclient.engine.game;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import static org.lwjgl.glfw.GLFW.*;


public final class BindKeys {
    private final int mappedKeys[];
    private static BindKeys instance;

    private BindKeys()  {
        mappedKeys = new int[eKeyType.values().length];

        try {
            loadBindKeys();
        } catch (IOException ex) {
            System.err.println("No se pudo leer el archivo de configuracion de teclas");
            loadDefaultKeys();
            saveBindKeys();
        }
    }

    public static BindKeys get() {
        if(instance == null) {
            instance = new BindKeys();
        }

        return instance;
    }

    public void loadDefaultKeys () {
        mappedKeys[eKeyType.mKeyUp.ordinal()] = GLFW_KEY_W;
        mappedKeys[eKeyType.mKeyDown.ordinal()] = GLFW_KEY_S;
        mappedKeys[eKeyType.mKeyLeft.ordinal()] = GLFW_KEY_A;
        mappedKeys[eKeyType.mKeyRight.ordinal()] = GLFW_KEY_D;

        mappedKeys[eKeyType.mKeyToggleMusic.ordinal()] = GLFW_KEY_M;
        mappedKeys[eKeyType.mKeyToggleSound.ordinal()] = GLFW_KEY_S;
        mappedKeys[eKeyType.mKeyToggleFxs.ordinal()] = GLFW_KEY_F;

        mappedKeys[eKeyType.mKeyRequestRefresh.ordinal()] = GLFW_KEY_L;

        mappedKeys[eKeyType.mKeyToggleNames.ordinal()] = GLFW_KEY_N;

        mappedKeys[eKeyType.mKeyGetObject.ordinal()] = GLFW_KEY_Q;
        mappedKeys[eKeyType.mKeyEquipObject.ordinal()] = GLFW_KEY_E;

        mappedKeys[eKeyType.mKeyTamAnimal.ordinal()] = GLFW_KEY_D;
        mappedKeys[eKeyType.mKeySteal.ordinal()] = GLFW_KEY_R;
        mappedKeys[eKeyType.mKeyToggleSafeMode.ordinal()] = GLFW_KEY_KP_MULTIPLY;
        mappedKeys[eKeyType.mKeyToggleResuscitationSafe.ordinal()] = GLFW_KEY_END;

        mappedKeys[eKeyType.mKeyHide.ordinal()] = GLFW_KEY_O;
        mappedKeys[eKeyType.mKeyDropObject.ordinal()] = GLFW_KEY_T;
        mappedKeys[eKeyType.mKeyUseObject.ordinal()] = GLFW_KEY_SPACE;
        mappedKeys[eKeyType.mKeyAttack.ordinal()] = GLFW_KEY_LEFT_CONTROL;

        mappedKeys[eKeyType.mKeyTalk.ordinal()] = GLFW_KEY_ENTER;
        mappedKeys[eKeyType.mKeyTalkWithGuild.ordinal()] = GLFW_KEY_DELETE;
        mappedKeys[eKeyType.mKeyTakeScreenShot.ordinal()] = GLFW_KEY_F2;

        mappedKeys[eKeyType.mKeyShowOptions.ordinal()] = GLFW_KEY_F5;
        mappedKeys[eKeyType.mKeyMeditate.ordinal()] = GLFW_KEY_F6;
        mappedKeys[eKeyType.mKeyCastSpellMacro.ordinal()] = GLFW_KEY_F7;
        mappedKeys[eKeyType.mKeyWorkMacro.ordinal()] = GLFW_KEY_F8;

        mappedKeys[eKeyType.mKeyExitGame.ordinal()] = GLFW_KEY_ESCAPE;
    }

    /**
     *
     * @throws IOException: Puede suceder que no encuentre el archivo o que tenga menos informacion
     *                      de lo que se esperaba desde el programa.
     *
     * @desc: Carga todas las teclas guardadas por el usuario.
     */
    public void loadBindKeys() throws IOException {
        RandomAccessFile f = new RandomAccessFile("resources/inits/keys.bin", "rw");
        f.seek(0);

        for (int i = 0; i < eKeyType.values().length; i++) {
            mappedKeys[i] = f.readInt();
        }

        f.close();
    }

    /**
     * @desc: Guarda la configuracion de teclas.
     */
    public void saveBindKeys() {
        RandomAccessFile f = null;

        try {
            f = new RandomAccessFile("resources/inits/keys.bin", "rw");
            f.seek(0);

            for (int i = 0; i < eKeyType.values().length; i++) {
                f.writeInt(mappedKeys[i]);
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @desc: Permite bindear una nueva tecla.
     */
    public void changeBindedKey(eKeyType key, int newKey) {

    }

    public int getBindedKey(eKeyType key) {
        return mappedKeys[key.ordinal()];
    }

}
