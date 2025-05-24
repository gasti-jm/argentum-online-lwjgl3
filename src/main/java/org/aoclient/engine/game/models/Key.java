package org.aoclient.engine.game.models;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Representa las diferentes teclas que pueden ser mapeadas para realizar acciones en el juego.
 * <p>
 * Cada constante del enumerado contiene un codigo de tecla predeterminado que puede ser personalizado por el usuario.
 * <p>
 * Proporciona metodos para obtener, modificar y restablecer el mapeo de cada tecla a su configuracion original.
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

    private final int defaultKeyCode;
    private int keyCode;

    Key(int keyCode) {
        this.defaultKeyCode = keyCode;
        this.keyCode = keyCode;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }

    /**
     * Restaura el valor de la tecla asociada a su configuracion predeterminada.
     * <p>
     * Este metodo asigna el codigo de tecla por defecto {@code defaultKeyCode} al atributo {@code keyCode} para que la
     * accion vuelva a usar el mapeo original definido al iniciar el juego. Es util para restablecer configuraciones en
     * caso de que el jugador desee revertir cambios personalizados.
     */
    public void resetToDefault() {
        keyCode = defaultKeyCode;
    }

}

