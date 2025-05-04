package org.aoclient.network.protocol.types;

/**
 * <p>
 * Representa una enumeracion que contiene una lista de comandos disponibles para el Game Master (GM). Estos comandos permiten
 * realizar diversas acciones administrativas, de moderacion y de gestion dentro del entorno del juego.
 * <p>
 * Un Game Master (GM) utiliza estos comandos para interactuar con el servidor, los jugadores y el mundo del juego de manera
 * eficiente. La mayoria de estos comandos tienen funciones especializadas que facilitan el mantenimiento, la supervision y la
 * administracion del servidor y su comunidad.
 * <p>
 * Caracteristicas principales:
 * <ul>
 *   <li>Incluye comandos para enviar mensajes en diferentes canales, como mensajes generales, mensajes dirigidos a facciones o
 *   gremios.
 *   <li>Herramientas para gestionar jugadores: teletransportar, enviar advertencias, aplicar sanciones, gestionar inventarios.
 *   <li>Capacidades para interactuar con PNJ (personajes no jugables), como crear, eliminar o modificar PNJ en el mundo del juego.
 *   <li>Manejo de configuraciones del servidor y mapas, actualizacion de datos y ejecucion de acciones avanzadas.
 *   <li>Soporte para tareas relacionadas con el sistema, como reiniciar el servidor, realizar respaldos y cargar datos modificados.
 * </ul>
 */

public enum GMCommand {

    STRAW_BUNDLE(0),
    GM_MESSAGE(1),                  // '/GMSG
    SHOW_NAME(2),                   // '/SHOWNAME
    ONLINE_ROYAL_ARMY(3),           // '/ONLINEREAL
    ONLINE_CHAOS_LEGION(4),         // '/ONLINECAOS
    GO_NEARBY(5),                   // '/IRCERCA
    COMMENT(6),                     // '/REM
    SERVER_TIME(7),                 // '/HORA
    WHERE(8),                       // '/DONDE
    CREATURES_IN_MAP(9),            // '/NENE
    WARP_ME_TO_TARGET(10),          // '/TELEPLOC
    WARP_CHAR(11),                  // '/TELEP
    SILENCE(12),                    // '/SILENCIAR
    SOS_SHOW_LIST(13),              // '/SHOW SOS
    SOS_REMOVE(14),                 // 'SOSDONE
    GO_TO_CHAR(15),                 // '/IRA
    INVISIBLE(16),                  // '/INVISIBLE
    GM_PANEL(17),                   // '/PANELGM
    REQUEST_USER_LIST(18),          // 'LISTUSU
    WORKING(19),                    // '/TRABAJANDO
    HIDING(20),                     // '/OCULTANDO
    JAIL(21),                       // '/CARCEL
    KILL_NPC(22),                   // '/RMATA
    WARN_USER(23),                  // '/ADVERTENCIA
    EDIT_CHAR(24),                  // '/MOD
    REQUEST_CHAR_INFO(25),          // '/INFO
    REQUEST_CHAR_STATS(26),         // '/STAT
    REQUEST_CHAR_GOLD(27),          // '/BAL
    REQUEST_CHAR_INVENTORY(28),     // '/INV
    REQUEST_CHAR_BANK(29),          // '/BOV
    REQUEST_CHAR_SKILLS(30),        // '/SKILLS
    REVIVE_CHAR(31),                // '/REVIVIR
    ONLINE_GM(32),                  // '/ONLINEGM
    ONLINE_MAP(33),                 // '/ONLINEMAP
    FORGIVE(34),                    // '/PERDON
    KICK(35),                       // '/ECHAR
    EXECUTE(36),                    // '/EJECUTAR
    BAN_CHAR(37),                   // '/BAN
    UNBAN_CHAR(38),                 // '/UNBAN
    NPC_FOLLOW(39),                 // '/SEGUIR
    SUMMON_CHAR(40),                // '/SUM
    SPAWN_LIST_REQUEST(41),         // '/CC
    SPAWN_CREATURE(42),             // 'SPA
    RESET_NPC_INVENTORY(43),        // '/RESETINV
    CLEAN_WORLD(44),                // '/LIMPIAR
    SERVER_MESSAGE(45),             // '/RMSG
    NICK_TO_IP(46),                 // '/NICK2IP
    IP_TO_NICK(47),                 // '/IP2NICK
    GUILD_ONLINE_MEMBERS(48),       // '/ONCLAN
    TELEPORT_CREATE(49),            // '/CT
    TELEPORT_DESTROY(50),           // '/DT
    RAIN_TOGGLE(51),                // '/LLUVIA
    SET_CHAR_DESCRIPTION(52),       // '/SETDESC
    FORCE_MIDI_TO_MAP(53),          // '/FORCEMIDIMAP
    FORCE_WAVE_TO_MAP(54),          // '/FORCEWAVMAP
    ROYAL_ARMY_MESSAGE(55),         // '/REALMSG
    CHAOS_LEGION_MESSAGE(56),       // '/CAOSMSG
    CITIZEN_MESSAGE(57),            // '/CIUMSG
    CRIMINAL_MESSAGE(58),           // '/CRIMSG
    TALK_AS_NPC(59),                // '/TALKAS
    DESTROY_ALL_ITEMS_IN_AREA(60),  // '/MASSDEST
    ACCEPT_ROYAL_COUNCIL_MEMBER(61),// '/ACEPTCONSE
    ACCEPT_CHAOS_COUNCIL_MEMBER(62),// '/ACEPTCONSECAOS
    ITEMS_IN_THE_FLOOR(63),         // '/PISO
    MAKE_DUMB(64),                  // '/ESTUPIDO
    MAKE_DUMB_NO_MORE(65),          // '/NOESTUPIDO
    DUMP_IP_TABLES(66),             // '/DUMPSECURITY
    COUNCIL_KICK(67),               // '/KICKCONSE
    SET_TRIGGER(68),                // '/TRIGGER
    ASK_TRIGGER(69),                // '/TRIGGER with no args
    BANNED_IP_LIST(70),             // '/BANIPLIST
    BANNED_IP_RELOAD(71),           // '/BANIPRELOAD
    GUILD_MEMBER_LIST(72),          // '/MIEMBROSCLAN
    GUILD_BAN(73),                  // '/BANCLAN
    BAN_IP(74),                     // '/BANIP
    UNBAN_IP(75),                   // '/UNBANIP
    CREATE_ITEM(76),                // '/CI
    DESTROY_ITEMS(77),              // '/DEST
    CHAOS_LEGION_KICK(78),          // '/NOCAOS
    ROYAL_ARMY_KICK(79),            // '/NOREAL
    FORCE_MIDI_ALL(80),             // '/FORCEMIDI
    FORCE_WAVE_ALL(81),             // '/FORCEWAV
    REMOVE_PUNISHMENT(82),          // '/BORRARPENA
    TILE_BLOCKED_TOGGLE(83),        // '/BLOQ
    KILL_NPC_NO_RESPAWN(84),        // '/MATA
    KILL_ALL_NEARBY_NPCS(85),       // '/MASSKILL
    LAST_IP(86),                    // '/LASTIP
    CHANGE_MOTD(87),                // '/MOTDCAMBIA
    SET_MOTD(88),                   // 'ZMOTD
    SYSTEM_MESSAGE(89),             // '/SMSG
    CREATE_NPC(90),                 // '/ACC
    CREATE_NPC_WITH_RESPAWN(91),    // '/RACC
    IMPERIAL_ARMOUR(92),            // '/AI1 - 4
    CHAOS_ARMOUR(93),               // '/AC1 - 4
    NAVIGATE_TOGGLE(94),            // '/NAVE
    SERVER_OPEN_TO_USERS_TOGGLE(95),// '/HABILITAR
    TURN_OFF_SERVER(96),            // '/APAGAR
    TURN_CRIMINAL(97),              // '/CONDEN
    RESET_FACTIONS(98),             // '/RAJAR
    REMOVE_CHAR_FROM_GUILD(99),     // '/RAJARCLAN
    REQUEST_CHAR_MAIL(100),         // '/LASTEMAIL
    ALTER_PASSWORD(101),            // '/APASS
    ALTER_MAIL(102),                // '/AEMAIL
    ALTER_NAME(103),                // '/ANAME
    TOGGLE_CENTINEL_ACTIVATED(104), // '/CENTINELAACTIVADO
    DO_BACKUP(105),                 // '/DOBACKUP
    SHOW_GUILD_MESSAGES(106),       // '/SHOWCMSG
    SAVE_MAP(107),                  // '/GUARDAMAPA
    CHANGE_MAP_INFO_PK(108),        // '/MODMAPINFO PK
    CHANGE_MAP_INFO_BACKUP(109),    // '/MODMAPINFO BACKUP
    CHANGE_MAP_INFO_RESTRICTED(110),// '/MODMAPINFO RESTRINGIR
    CHANGE_MAP_INFO_NO_MAGIC(111),  // '/MODMAPINFO MAGIASINEFECTO
    CHANGE_MAP_INFO_NO_INVI(112),   // '/MODMAPINFO INVISINEFECTO
    CHANGE_MAP_INFO_NO_RESU(113),   // '/MODMAPINFO RESUSINEFECTO
    CHANGE_MAP_INFO_LAND(114),      // '/MODMAPINFO TERRENO
    CHANGE_MAP_INFO_ZONE(115),      // '/MODMAPINFO ZONA
    CHANGE_MAP_INFO_STEAL_NPC(116), // '/MODMAPINFO ROBONPCm
    CHANGE_MAP_INFO_NO_OCULTAR(117),// '/MODMAPINFO OCULTARSINEFECTO
    CHANGE_MAP_INFO_NO_INVOCAR(118),// '/MODMAPINFO INVOCARSINEFECTO
    SAVE_CHARS(119),                // '/GRABAR
    CLEAN_SOS(120),                 // '/BORRAR SOS
    SHOW_SERVER_FORM(121),          // '/SHOW INT
    NIGHT(122),                     // '/NOCHE
    KICK_ALL_CHARS(123),            // '/ECHARTODOSPJS
    RELOAD_NPCS(124),               // '/RELOADNPCS
    RELOAD_SERVER_INI(125),         // '/RELOADSINI
    RELOAD_SPELLS(126),             // '/RELOADHECHIZOS
    RELOAD_OBJECTS(127),            // '/RELOADOBJ
    RESTART(128),                   // '/REINICIAR
    RESET_AUTO_UPDATE(129),         // '/AUTOUPDATE
    CHAT_COLOR(130),                // '/CHATCOLOR
    IGNORED(131),                   // '/IGNORADO
    CHECK_SLOT(132),                // '/SLOT
    SET_INI_VAR(133),               // '/SETINIVAR LLAVE CLAVE VALOR
    CREATE_PRETORIAN_CLAN(134),     // '/CREARPRETORIANOS
    REMOVE_PRETORIAN_CLAN(135),     // '/ELIMINARPRETORIANOS
    ENABLE_DENOUNCES(136),          // '/DENUNCIAS
    SHOW_DENOUNCES_LIST(137),       // '/SHOW DENUNCIAS
    MAP_MESSAGE(138),               // '/MAPMSG
    SET_DIALOG(139),                // '/SETDIALOG
    IMPERSONATE(140),               // '/IMPERSONAR
    IMITATE(141),                   // '/MIMETIZAR
    RECORD_ADD(142),
    RECORD_REMOVE(143),
    RECORD_ADD_OBS(144),
    RECORD_LIST_REQUEST(145),
    RECORD_DETAILS_REQUEST(146);

    private final int id;

    GMCommand(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}