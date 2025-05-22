package org.aoclient.engine.game.models;

/**
 * Define todas las acciones posibles que un usuario puede realizar mediante pulsaciones de teclas, constituyendo el sistema de
 * control del personaje y de la interfaz del juego. Cada valor del enum representa una accion especifica que puede ser vinculada
 * a una tecla fisica del teclado.
 * <p>
 * Las acciones incluyen movimientos basicos (arriba, abajo, izquierda, derecha), interacciones de combate (atacar, lanzar
 * hechizos), manejo de inventario (equipar objetos, usar items), comunicacion (hablar, chat de clan) y funciones del sistema
 * (opciones, salir).
 * <p>
 * Este enum trabaja en conjunto con la clase {@link org.aoclient.engine.game.BindKeys BindKeys}, que gestiona la asociacion entre
 * teclas fisicas y estas acciones, permitiendo personalizar los controles y procesar las entradas del usuario durante el juego.
 */

public enum Key {

    UP,
    DOWN,
    LEFT,
    RIGHT,
    TOGGLE_MUSIC,
    TOGGLE_SOUND,
    TOGGLE_FXS,
    REQUEST_REFRESH,
    TOGGLE_NAMES,
    GET_OBJECT,
    EQUIP_OBJECT,
    TAME_ANIMAL,
    STEAL,
    TOGGLE_SAFE_MODE,
    TOGGLE_RESUSCITATION_SAFE,
    HIDE,
    DROP_OBJECT,
    USE_OBJECT,
    ATTACK,
    TALK,
    TALK_WITH_GUILD,
    TAKE_SCREENSHOT,
    SHOW_OPTIONS,
    MEDITATE,
    CAST_SPELL_MACRO,
    WORK_MACRO,
    AUTO_MOVE,
    EXIT_GAME

}
