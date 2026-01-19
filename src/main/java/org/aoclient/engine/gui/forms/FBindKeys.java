package org.aoclient.engine.gui.forms;

import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import org.aoclient.engine.game.bindkeys.Key;
import org.aoclient.engine.gui.widgets.ImageButton3State;
import org.aoclient.engine.listeners.KeyHandler;

import java.io.IOException;

import static org.aoclient.engine.audio.Sound.SND_CLICK;
import static org.aoclient.engine.audio.Sound.playSound;
import static org.lwjgl.glfw.GLFW.*;

public class FBindKeys extends Form {
    // apa las papas.
    private ImageButton3State btnSave;
    private ImageButton3State btnDefaultKeys;

    public FBindKeys() {
        try {
            this.backgroundImage = loadTexture("VentanaConfigurarTeclas");


            btnSave = new ImageButton3State(
                    loadTexture("BotonGuardarConfigKey"),
                    loadTexture("BotonGuardarRolloverConfigKey"),
                    loadTexture("BotonGuardarClickConfigKey"),
                    312, 448, 177, 25
            );

            btnDefaultKeys = new ImageButton3State(
                    loadTexture("BotonDefaultKeys"),
                    loadTexture("BotonDefaultKeysRollover"),
                    loadTexture("BotonDefaultKeysClick"),
                    64, 448, 177, 25
            );

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Checkeo de teclas que no se pueden asignar en nuestro guardado. <br>
     * por ejemplo: No se puede bindear la tecla de windows. <br> <br>
     *
     * True = Tecla permitida <br>
     * False = Tecla no asignable
     */
    private boolean checkKeysPermited(int key) {
        return switch (key) {
            case GLFW_KEY_HOME,
                 GLFW_KEY_F12 -> false;
            default -> true;
        };
    }

    /**
     * Segun la tecla que presionemos, nos devolvera el nombre.
     */
    private String getKeyName(int key) {
        int scancode = glfwGetKeyScancode(key);
        String keyName = glfwGetKeyName(key, scancode);

        if (keyName == null) {
            // GLFW no devuelve el nombre de las teclas especiales.

            return switch (key){
                // Teclas especiales.
                case GLFW_KEY_SPACE -> "ESPACIO";
                case GLFW_KEY_ENTER -> "ENTER";
                case GLFW_KEY_LEFT_SHIFT -> "SHIFT IZQ";
                case GLFW_KEY_RIGHT_SHIFT -> "SHIFT DER";
                case GLFW_KEY_ESCAPE -> "ESC";
                case GLFW_KEY_END -> "FIN";
                case GLFW_KEY_TAB -> "TAB";
                case GLFW_KEY_LEFT_CONTROL -> "CTRL IZQ";
                case GLFW_KEY_RIGHT_CONTROL -> "CTRL DER";
                case GLFW_KEY_LEFT_ALT -> "ALT IZQ";
                case GLFW_KEY_RIGHT_ALT -> "ALT DER";
                case GLFW_KEY_DELETE -> "SUPRIMIR";

                // F1-F12
                case GLFW_KEY_F1 -> "F1";
                case GLFW_KEY_F2 -> "F2";
                case GLFW_KEY_F3 -> "F3";
                case GLFW_KEY_F4 -> "F4";
                case GLFW_KEY_F5 -> "F5";
                case GLFW_KEY_F6 -> "F6";
                case GLFW_KEY_F7 -> "F7";
                case GLFW_KEY_F8 -> "F8";
                case GLFW_KEY_F9 -> "F9";
                case GLFW_KEY_F10 -> "F10";
                case GLFW_KEY_F11 -> "F11";
                case GLFW_KEY_F12 -> "F12";

                // tecla inreconocible
                default -> "???";
            };
        }

        // Devuelve el nombre de la tecla presionada gracias a GLFW.
        return keyName;
    }

    @Override
    public void render() {
        ImGui.setNextWindowFocus(); // dale foco solo a este FRM

        ImGui.setNextWindowSize(560, 500, ImGuiCond.Always);
        ImGui.begin(this.getClass().getSimpleName(), ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoDecoration |
                ImGuiWindowFlags.NoBackground | ImGuiWindowFlags.NoMove);

        this.checkMoveFrm();

        ImGui.setCursorPos(5, 0);
        ImGui.image(backgroundImage, 548, 500);

        if(btnDefaultKeys.render()) buttonDefault();
        if(btnSave.render()) buttonSave();

        showKeysBinded();

        ImGui.end();
    }

    /**
     * Muestra todos los botones que existan en nuestro frm para el cambio de tecla.
     */
    private void showKeysBinded() {
        // Movimiento
        buttonToChangeKey(Key.UP, 143, 34);
        buttonToChangeKey(Key.DOWN, 143, 58);
        buttonToChangeKey(Key.LEFT, 143, 82);
        buttonToChangeKey(Key.RIGHT, 143, 106);

        // Acciones
        buttonToChangeKey(Key.GET_OBJECT, 143, 155);
        buttonToChangeKey(Key.EQUIP_OBJECT, 143, 179);
        buttonToChangeKey(Key.TAME_ANIMAL, 143, 203);
        buttonToChangeKey(Key.STEAL, 143, 227);
        buttonToChangeKey(Key.HIDE, 143, 251);
        buttonToChangeKey(Key.DROP_OBJECT, 143, 275);
        buttonToChangeKey(Key.USE_OBJECT, 143, 299);
        buttonToChangeKey(Key.ATTACK, 143, 323);

        // Hablar
        buttonToChangeKey(Key.TALK, 143, 371);
        buttonToChangeKey(Key.TALK_WITH_GUILD, 143, 395);

        // Opciones personales
        buttonToChangeKey(Key.TOGGLE_MUSIC, 423, 35);
        buttonToChangeKey(Key.REQUEST_REFRESH, 423, 59);
        buttonToChangeKey(Key.TOGGLE_NAMES, 423, 83);
        buttonToChangeKey(Key.TOGGLE_SOUND, 423, 108);
        buttonToChangeKey(Key.TOGGLE_FXS, 423, 132);

        // Otras teclas
        buttonToChangeKey(Key.TAKE_SCREENSHOT, 423, 189);
        buttonToChangeKey(Key.SHOW_OPTIONS, 423, 216);
        buttonToChangeKey(Key.MEDITATE, 423, 244);
        buttonToChangeKey(Key.CAST_SPELL_MACRO, 423, 272);
        buttonToChangeKey(Key.WORK_MACRO, 423, 299);
        buttonToChangeKey(Key.EXIT_GAME, 423, 326);
        buttonToChangeKey(Key.TOGGLE_SAFE_MODE, 423, 352);
        buttonToChangeKey(Key.TOGGLE_RESUSCITATION_SAFE, 423, 379);
    }

    /**
     * Dibuja nuestro boton para cambiar su tecla
     */
    private void buttonToChangeKey(Key key, int x, int y) {
        String actual = getKeyName(key.getKeyCode()).toUpperCase();

        // chekeamos que si esta en cambio de tecla se le asigna "Presione una tecla."
        if (key.getPreparedToBind()) {
            actual = "PRES.UNA TECLA";
        }

        ImGui.setCursorPos(x, y);

        // imgui styles
        ImGui.pushStyleVar(ImGuiStyleVar.FramePadding, 0f, 0f);
        ImGui.pushStyleColor(ImGuiCol.Button, 0f, 0f, 0f, 0.6f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0f, 0f, 0f, 0.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0f, 0f, 0f, 0.4f);

        // Lo dibujamos con la tecla que tiene asignada
        if(ImGui.button(actual, 108, 15)) {
            // si hacemos click estamos preparados para cambiar de tecla
            if(key.getPreparedToBind()) {
                key.setPreparedToBind(false); // cancelamos el proceso de bindeado.
            } else {
                if(!Key.checkIsBinding()) { // para que no nos permita hacer lo mismo en varios botones
                    key.setPreparedToBind(true);
                }
            }

        }

        ImGui.popStyleColor();
        ImGui.popStyleColor();
        ImGui.popStyleColor();
        ImGui.popStyleVar();
    }

    private void buttonDefault() {
        playSound(SND_CLICK);

        Key.loadDefaultKeys();
        KeyHandler.updateMovementKeys();

        btnDefaultKeys.delete();
        btnSave.delete();
        close();
    }

    private void buttonSave() {
        playSound(SND_CLICK);

        Key.saveKeys();

        btnDefaultKeys.delete();
        btnSave.delete();
        close();
    }
}
