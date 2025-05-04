package org.aoclient.network.protocol;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Define todos los identificadores de paquetes enviados por el servidor al cliente.
 * <p>
 * Contiene constantes para cada tipo de mensaje que el servidor puede enviar al cliente, permitiendo una identificacion clara y
 * tipada de los paquetes durante el procesamiento de datos recibidos. Cada valor del enumerador corresponde a un tipo especifico
 * de operacion o informacion que el servidor transmite.
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

    LOGGED(0),                   // LOGGED
    REMOVE_DIALOGS(1),           // QTDL
    REMOVE_CHAR_DIALOG(2),       // QDL
    NAVIGATE_TOGGLE(3),          // NAVEG
    DISCONNECT(4),               // FINOK
    COMMERCE_END(5),             // FINCOMOK
    BANK_END(6),                 // FINBANOK
    COMMERCE_INIT(7),            // INITCOM
    BANK_INIT(8),                // INITBANCO
    USER_COMMERCE_INIT(9),       // INITCOMUSU
    USER_COMMERCE_END(10),       // FINCOMUSUOK
    USER_OFFER_CONFIRM(11),
    COMMERCE_CHAT(12),
    SHOW_BLACKSMITH_FORM(13),     // SFH
    SHOW_CARPENTER_FORM(14),      // SFC
    UPDATE_STA(15),               // ASS
    UPDATE_MANA(16),              // ASM
    UPDATE_HP(17),                // ASH
    UPDATE_GOLD(18),              // ASG
    UPDATE_BANK_GOLD(19),
    UPDATE_EXP(20),               // ASE
    CHANGE_MAP(21),               // CM
    POS_UPDATE(22),               // PU
    CHAT_OVER_HEAD(23),           // ||
    CONSOLE_MSG(24),              // || ¡Cuidado! Es lo mismo que el anterior, pero esta correctamente dividido
    GUILD_CHAT(25),               // |+
    SHOW_MESSAGE_BOX(26),         // !!
    USER_INDEX_IN_SERVER(27),     // IU
    USER_CHAR_INDEX_IN_SERVER(28),// IP
    CHARACTER_CREATE(29),         // CC
    CHARACTER_REMOVE(30),         // BP
    CHARACTER_CHANGE_NICK(31),
    CHARACTER_MOVE(32),           // MP, +, * and _ //
    FORCE_CHAR_MOVE(33),
    CHARACTER_CHANGE(34),         // CP
    OBJECT_CREATE(35),            // HO
    OBJECT_DELETE(36),            // BO
    BLOCK_POSITION(37),           // BQ
    PLAY_MIDI(38),                // TM
    PLAY_WAVE(39),                // TW
    GUILD_LIST(40),               // GL
    AREA_CHANGED(41),             // CA
    PAUSE_TOGGLE(42),             // BKW
    RAIN_TOGGLE(43),              // LLU
    CREATE_FX(44),                // CFX
    UPDATE_USER_STATS(45),        // EST
    WORK_REQUEST_TARGET(46),      // T01
    CHANGE_INVENTORY_SLOT(47),    // CSI
    CHANGE_BANK_SLOT(48),         // SBO
    CHANGE_SPELL_SLOT(49),        // SHS
    ATTRIBUTES(50),               // ATR
    BLACKSMITH_WEAPONS(51),       // LAH
    BLACKSMITH_ARMORS(52),        // LAR
    CARPENTER_OBJECTS(53),        // OBR
    REST_OK(54),                  // DOK
    ERROR_MSG(55),                // ERR
    BLIND(56),                    // CEGU
    DUMB(57),                     // DUMB
    SHOW_SIGNAL(58),              // MCAR
    CHANGE_NPC_INVENTORY_SLOT(59),// NPCI
    UPDATE_HUNGER_AND_THIRST(60), // EHYS
    FAME(61),                     // FAMA
    MINI_STATS(62),               // MEST
    LEVEL_UP(63),                 // SUNI
    ADD_FORUM_MSG(64),            // FMSG
    SHOW_FORUM_FORM(65),          // MFOR
    SET_INVISIBLE(66),            // NOVER
    DICE_ROLL(67),                // DADOS
    MEDITATE_TOGGLE(68),          // MEDOK
    BLIND_NO_MORE(69),            // NSEGUE
    DUMB_NO_MORE(70),             // NESTUP
    SEND_SKILLS(71),              // SKILLS
    TRAINER_CREATURE_LIST(72),    // LSTCRI
    GUILD_NEWS(73),               // GUILDNE
    OFFER_DETAILS(74),            // PEACEDE & ALLIEDE
    ALIANCE_PROPOSALS_LIST(75),   // ALLIEPR
    PEACE_PROPOSALS_LIST(76),     // PEACEPR
    CHARACTER_INFO(77),           // CHRINFO
    GUILD_LEADER_INFO(78),        // LEADERI
    GUILD_MEMBER_INFO(79),
    GUILD_DETAILS(80),            // CLANDET
    SHOW_GUILD_FUNDATION_FORM(81),// SHOWFUN
    PARALIZE_OK(82),              // PARADOK
    SHOW_USER_REQUEST(83),        // PETICIO
    TRADE_OK(84),                 // TRANSOK
    BANK_OK(85),                  // BANCOOK
    CHANGE_USER_TRADE_SLOT(86),   // COMUSUINV
    SEND_NIGHT(87),               // NOC
    PONG(88),
    UPDATE_TAG_AND_STATUS(89),
    //GM messages
    SPAWN_LIST(90),               // SPL
    SHOW_SOS_FORM(91),            // MSOS
    SHOW_MOTD_EDITION_FORM(92),   // ZMOTD
    SHOW_GM_PANEL_FORM(93),       // ABPANEL
    USER_NAME_LIST(94),           // LISTUSU
    SHOW_GUILD_ALIGN(95),
    SHOW_PARTY_FORM(96),
    UPDATE_STRENGHT_AND_DEXTERITY(97),
    UPDATE_STRENGHT(98),
    UPDATE_DEXTERITY(99),
    ADD_SLOTS(100),
    MULTI_MESSAGE(101),
    STOP_WORKING(102),
    CANCEL_OFFER_ITEM(103);

    /** Utiliza un HashMap que proporciona acceso en tiempo constante (complejidad O(1)). */
    private static final Map<Integer, ServerPacket> idToPacket = new HashMap<>();

    // Inicializa el mapa una sola vez cuando la clase se carga
    static {
        for (ServerPacket packet : values())
            idToPacket.put(packet.getId(), packet);
    }

    private final int id;

    ServerPacket(int id) {
        this.id = id;
    }

    public static ServerPacket fromId(int id) {
        ServerPacket packet = idToPacket.get(id);
        if (packet == null) throw new IllegalArgumentException("No ServerPacket found with ID: " + id);
        return packet;
    }

    // TODO Deberia ser privado?
    public int getId() {
        return id;
    }

}
