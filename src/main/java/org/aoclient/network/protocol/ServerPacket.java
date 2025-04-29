package org.aoclient.network.protocol;

/**
 * <p>
 * Enumeracion que define todos los identificadores de paquetes enviados por el servidor al cliente en el protocolo de
 * comunicacion del juego.
 * <p>
 * {@code ServerPacket} contiene constantes para cada tipo de mensaje que el servidor puede enviar al cliente, permitiendo una
 * identificacion clara y tipada de los paquetes durante el procesamiento de datos recibidos. Cada valor del enumerador
 * corresponde a un tipo especifico de operacion o informacion que el servidor transmite.
 * <p>
 * Estos identificadores se utilizan en el proceso de manejo de paquetes entrantes para determinar el tipo de paquete recibido y
 * dirigirlo al manejador correspondiente. La estructura del enumerador refleja todas las posibles interacciones iniciadas por el
 * servidor, como actualizaciones de estado, notificaciones de eventos, respuestas a solicitudes del cliente y cambios en el
 * entorno de juego.
 * <p>
 * La enumeracion incluye paquetes para funciones como:
 * <ul>
 * <li>Gestion de conexion y autenticacion
 * <li>Actualizacion de estadisticas del personaje
 * <li>Actualizacion del mundo y sus objetos
 * <li>Sistema de comercio y economia
 * <li>Sistema de combate
 * <li>Comunicacion entre jugadores
 * <li>Funcionalidades de clanes y grupos
 * <li>Comandos administrativos
 * </ul>
 * <p>
 * Cada miembro del enum debe mantener la misma posicion ordinal que su correspondiente en el servidor para garantizar la correcta
 * interpretacion de los paquetes.
 */

public enum ServerPacket {

    LOGGED,                   // LOGGED
    REMOVE_DIALOGS,           // QTDL
    REMOVE_CHAR_DIALOG,       // QDL
    NAVIGATE_TOGGLE,          // NAVEG
    DISCONNECT,               // FINOK
    COMMERCE_END,             // FINCOMOK
    BANK_END,                 // FINBANOK
    COMMERCE_INIT,            // INITCOM
    BANK_INIT,                // INITBANCO
    USER_COMMERCE_INIT,       // INITCOMUSU
    USER_COMMERCE_END,        // FINCOMUSUOK
    USER_OFFER_CONFIRM,
    COMMERCE_CHAT,
    SHOW_BLACKSMITH_FORM,     // SFH
    SHOW_CARPENTER_FORM,      // SFC
    UPDATE_STA,               // ASS
    UPDATE_MANA,              // ASM
    UPDATE_HP,                // ASH
    UPDATE_GOLD,              // ASG
    UPDATE_BANK_GOLD,
    UPDATE_EXP,               // ASE
    CHANGE_MAP,               // CM
    POS_UPDATE,               // PU
    CHAT_OVER_HEAD,           // ||
    CONSOLE_MSG,              // || ¡Cuidado! Es lo mismo que el anterior, pero esta correctamente dividido
    GUILD_CHAT,               // |+
    SHOW_MESSAGE_BOX,         // !!
    USER_INDEX_IN_SERVER,     // IU
    USER_CHAR_INDEX_IN_SERVER,// IP
    CHARACTER_CREATE,         // CC
    CHARACTER_REMOVE,         // BP
    CHARACTER_CHANGE_NICK,
    CHARACTER_MOVE,           // MP, +, * and _ //
    FORCE_CHAR_MOVE,
    CHARACTER_CHANGE,         // CP
    OBJECT_CREATE,            // HO
    OBJECT_DELETE,            // BO
    BLOCK_POSITION,           // BQ
    PLAY_MIDI,                // TM
    PLAY_WAVE,                // TW
    GUILD_LIST,               // GL
    AREA_CHANGED,             // CA
    PAUSE_TOGGLE,             // BKW
    RAIN_TOGGLE,              // LLU
    CREATE_FX,                // CFX
    UPDATE_USER_STATS,        // EST
    WORK_REQUEST_TARGET,      // T01
    CHANGE_INVENTORY_SLOT,    // CSI
    CHANGE_BANK_SLOT,         // SBO
    CHANGE_SPELL_SLOT,        // SHS
    ATTRIBUTES,               // ATR
    BLACKSMITH_WEAPONS,       // LAH
    BLACKSMITH_ARMORS,        // LAR
    CARPENTER_OBJECTS,        // OBR
    REST_OK,                  // DOK
    ERROR_MSG,                // ERR
    BLIND,                    // CEGU
    DUMB,                     // DUMB
    SHOW_SIGNAL,              // MCAR
    CHANGE_NPC_INVENTORY_SLOT,// NPCI
    UPDATE_HUNGER_AND_THIRST, // EHYS
    FAME,                     // FAMA
    MINI_STATS,               // MEST
    LEVEL_UP,                 // SUNI
    ADD_FORUM_MSG,            // FMSG
    SHOW_FORUM_FORM,          // MFOR
    SET_INVISIBLE,            // NOVER
    DICE_ROLL,                // DADOS
    MEDITATE_TOGGLE,          // MEDOK
    BLIND_NO_MORE,            // NSEGUE
    DUMB_NO_MORE,             // NESTUP
    SEND_SKILLS,              // SKILLS
    TRAINER_CREATURE_LIST,    // LSTCRI
    GUILD_NEWS,               // GUILDNE
    OFFER_DETAILS,            // PEACEDE & ALLIEDE
    ALIANCE_PROPOSALS_LIST,   // ALLIEPR
    PEACE_PROPOSALS_LIST,     // PEACEPR
    CHARACTER_INFO,           // CHRINFO
    GUILD_LEADER_INFO,        // LEADERI
    GUILD_MEMBER_INFO,
    GUILD_DETAILS,            // CLANDET
    SHOW_GUILD_FUNDATION_FORM,// SHOWFUN
    PARALIZE_OK,              // PARADOK
    SHOW_USER_REQUEST,        // PETICIO
    TRADE_OK,                 // TRANSOK
    BANK_OK,                  // BANCOOK
    CHANGE_USER_TRADE_SLOT,   // COMUSUINV
    SEND_NIGHT,               // NOC
    PONG,
    UPDATE_TAG_AND_STATUS,
    //GM messages
    SPAWN_LIST,               // SPL
    SHOW_SOS_FORM,            // MSOS
    SHOW_MOTD_EDITION_FORM,   // ZMOTD
    SHOW_GM_PANEL_FORM,       // ABPANEL
    USER_NAME_LIST,           // LISTUSU
    SHOW_GUILD_ALIGN,
    SHOW_PARTY_FORM,
    UPDATE_STRENGHT_AND_DEXTERITY,
    UPDATE_STRENGHT,
    UPDATE_DEXTERITY,
    ADD_SLOTS,
    MULTI_MESSAGE,
    STOP_WORKING,
    CANCEL_OFFER_ITEM

}
