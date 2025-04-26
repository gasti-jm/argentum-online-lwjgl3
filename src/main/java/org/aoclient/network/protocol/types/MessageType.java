package org.aoclient.network.protocol.types;

/**
 * <p>
 * Representa diferentes tipos de mensajes o eventos que pueden ocurrir dentro de un sistema, como por ejemplo en un juego en
 * linea.
 * <p>
 * Cada constante de la enumeracion describe un tipo de evento especifico, ya sea relacionado con interacciones de usuarios,
 * acciones de NPCs, cambios de estado del jugador o el sistema, entre otros. Esto permite clasificar y manejar de forma
 * organizada los diferentes casos que pueden presentarse.
 * <p>
 * Algunos de los tipos de mensaje incluidos tratan sobre:
 * <ul>
 * <li>Ataques entre usuarios y NPCs.
 * <li>Uso de elementos defensivos como escudos.
 * <li>Cambios en modos seguros o resurrecciones.
 * <li>Notificaciones sobre ganancias, experiencia o acciones completadas.
 * <li>Situaciones particulares como trabajar, moverse a casa o eventos sociales.
 * </ul>
 */

public enum MessageType {

    DONT_SEE_ANYTHING,
    NPC_SWING,
    NPC_KILL_USER,
    BLOCKED_WITH_SHIELD_USER,
    BLOCKED_WITH_SHIELD_OTHER,
    USER_SWING,
    SAFE_MODE_ON,
    SAFE_MODE_OFF,
    RESUSCITATION_SAFE_OFF,
    RESUSCITATION_SAFE_ON,
    NOBILITY_LOST,
    CANT_USE_WHILE_MEDITATING,
    NPC_HIT_USER,
    USER_HIT_NPC,
    USER_ATTACKED_SWING,
    USER_HITTED_BY_USER,
    USER_HITTED_USER,
    WORK_REQUEST_TARGET,
    HAVE_KILLED_USER,
    USER_KILL,
    EARN_EXP,
    GO_HOME,
    CANCEL_GO_HOME,
    FINISH_HOME

}
