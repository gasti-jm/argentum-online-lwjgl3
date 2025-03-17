package org.aoclient.network.packets;

/**
 * <p>
 * Enumeracion que define los tipos de mensajes especiales que pueden ser enviados por el servidor al cliente a traves del paquete
 * <b>MultiMessage</b>.
 * <p>
 * {@code E_Messages} cataloga los diferentes tipos de notificaciones y eventos del juego que son procesados por el metodo
 * {@code handleMultiMessage()}. Estos mensajes representan acciones, resultados de combate, estados y otras notificaciones
 * importantes que requieren un formato especial o comportamiento particular al ser mostrados al usuario.
 * <p>
 * La enumeracion permite identificar de forma tipada y estructurada el tipo de mensaje recibido, facilitando su procesamiento y
 * presentacion adecuada en la interfaz del usuario.
 * <p>
 * Los tipos de mensajes incluyen:
 * <ul>
 * <li><b>DontSeeAnything</b>: Notifica al usuario que no puede ver nada interesante
 * <li><b>NPCSwing</b>: Indica que un NPC ha fallado un golpe
 * <li><b>NPCKillUser</b>: Informa que un NPC ha matado al usuario
 * <li><b>BlockedWithShieldUser</b>: Notifica que el usuario ha bloqueado un ataque con su escudo
 * <li><b>BlockedWithShieldOther</b>: Indica que otro usuario ha bloqueado un ataque con su escudo
 * <li><b>UserSwing</b>: Informa que el usuario ha fallado un golpe
 * <li><b>SafeModeOn/Off</b>: Notifica la activacion/desactivacion del modo seguro
 * <li><b>ResuscitationSafeOn/Off</b>: Notifica el estado del modo de resurreccion segura
 * <li><b>NobilityLost</b>: Informa que se han perdido puntos de nobleza
 * <li><b>CantUseWhileMeditating</b>: Notifica que no se puede realizar una accion mientras se medita
 * <li><b>UserHitNPC/NPCHitUser</b>: Informacion sobre golpes entre usuarios y NPCs
 * <li><b>WorkRequestTarget</b>: Solicitud para seleccionar un objetivo para una habilidad
 * <li><b>HaveKilledUser/UserKill</b>: Notificaciones sobre muertes de usuarios
 * <li><b>EarnExp</b>: Informa sobre experiencia ganada
 * <li><b>GoHome/CancelGoHome/FinishHome</b>: Estados del proceso de viajar al hogar
 * </ul>
 * <p>
 * Cada valor de esta enumeracion corresponde a un formato especifico de mensaje que requiere un tratamiento particular al ser
 * mostrado al usuario.
 */

public enum E_Messages {

    DontSeeAnything,
    NPCSwing,
    NPCKillUser,
    BlockedWithShieldUser,
    BlockedWithShieldOther,
    UserSwing,
    SafeModeOn,
    SafeModeOff,
    ResuscitationSafeOff,
    ResuscitationSafeOn,
    NobilityLost,
    CantUseWhileMeditating,
    NPCHitUser,
    UserHitNPC,
    UserAttackedSwing,
    UserHittedByUser,
    UserHittedUser,
    WorkRequestTarget,
    HaveKilledUser,
    UserKill,
    EarnExp,
    GoHome,
    CancelGoHome,
    FinishHome

}
