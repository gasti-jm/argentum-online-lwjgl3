package org.aoclient.engine.game.models;

/**
 * <p>
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

public enum E_KeyType {

    mKeyUp,
    mKeyDown,
    mKeyLeft,
    mKeyRight,
    mKeyToggleMusic,
    mKeyToggleSound,
    mKeyToggleFxs,
    mKeyRequestRefresh,
    mKeyToggleNames,
    mKeyGetObject,
    mKeyEquipObject,
    mKeyTamAnimal,
    mKeySteal,
    mKeyToggleSafeMode,
    mKeyToggleResuscitationSafe,
    mKeyHide,
    mKeyDropObject,
    mKeyUseObject,
    mKeyAttack,
    mKeyTalk,
    mKeyTalkWithGuild,
    mKeyTakeScreenShot,
    mKeyShowOptions,
    mKeyMeditate,
    mKeyCastSpellMacro,
    mKeyWorkMacro,
    mKeyAutoMove,
    mKeyExitGame

}
