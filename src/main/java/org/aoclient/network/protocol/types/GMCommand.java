package org.aoclient.network.protocol.types;

/**
 * Representa una enumeracion que contiene una lista de comandos disponibles para el Game Master (GM). Estos comandos permiten
 * realizar diversas acciones administrativas, de moderacion y de gestion dentro del entorno del juego.
 * <p>
 * Un Game Master (GM) utiliza estos comandos para interactuar con el servidor, los jugadores y el mundo del juego de manera
 * eficiente. La mayoria de estos comandos tienen funciones especializadas que facilitan el mantenimiento, la supervision y la
 * administracion del servidor y su comunidad.
 * <p>
 * Los comentarios representan los comandos viejos.
 */

public enum GMCommand {

    STRAW_BUNDLE(0),
    GM_MESSAGE(1),                               // /GMSG
    SHOW_NAME(2),                                // /SHOWNAME
    ONLINE_ROYAL(3),                             // /ONLINEREAL
    ONLINE_CHAOS(4),                             // /ONLINECAOS
    TELEPORT_NEAR_TO_PLAYER(5),                  // /IRCERCA
    COMMENT_SERVER_LOG(6),                       // /REM
    TIME(7),                                     // /HORA
    PLAYER_LOCATION(8),                          // /DONDE
    SHOW_MOBS(9),                                // /NENE
    TELEPORT_TO_TARGET(10),                      // /TELEPLOC
    TELEPORT(11),                                // /TELEP
    SILENCE(12),                                 // /SILENCIAR
    SOS_SHOW_LIST(13),                           // /SHOW SOS
    SOS_REMOVE(14),                              // SOSDONE
    TELEPORT_TO_PLAYER(15),                      // /IRA
    INVISIBILITY(16),                            // /INVISIBLE
    GM_PANEL(17),                                // /PANELGM
    REQUEST_USER_LIST(18),                       // LISTUSU
    SHOW_WORKERS(19),                            // /TRABAJANDO
    SHOW_HIDDEN(20),                             // /OCULTANDO
    JAIL(21),                                    // /CARCEL
    REMOVE_NPC(22),                              // /RMATA
    WARNING(23),                                 // /ADVERTENCIA
    MOD_PLAYER(24),                              // /MOD
    PLAYER_INFO(25),                             // /INFO
    STATS(26),                                   // /STAT
    PLAYER_GOLD(27),                             // /BAL
    PLAYER_INVENTORY(28),                        // /INV
    PLAYER_BANK(29),                             // /BOV
    PLAYER_SKILLS(30),                           // /SKILLS
    REVIVE(31),                                  // /REVIVIR
    ONLINE_GM(32),                               // /ONLINEGM
    ONLINE_MAP(33),                              // /ONLINEMAP
    FORGIVE(34),                                 // /PERDON
    KICK(35),                                    // /ECHAR
    KILL(36),                                    // /EJECUTAR
    BAN(37),                                     // /BAN
    UNBAN(38),                                   // /UNBAN
    NPC_FOLLOW(39),                              // /SEGUIR
    SUMMON(40),                                  // /SUM
    MOB_PANEL(41),                               // /CC
    SPAWN_CREATURE(42),                          // SPA
    CLEAN_NPC_INVENTORY(43),                     // /RESETINV
    CLEAN_WORLD(44),                             // /LIMPIAR
    RMSG(45),                                    // /RMSG
    NICK_TO_IP(46),                              // /NICK2IP
    IP_TO_NICK(47),                              // /IP2NICK
    GUILD_ONLINE_MEMBERS(48),                    // /ONCLAN
    CREATE_TELEPORT(49),                         // /CT
    REMOVE_TELEPORT(50),                         // /DT
    RAIN(51),                                    // /LLUVIA
    SET_DESC(52),                                // /SETDESC
    PLAY_MUSIC_MAP(53),                          // /FORCEMIDIMAP
    PLAY_SOUND_AT_LOCATION(54),                  // /FORCEWAVMAP
    ROYAL_ARMY_MESSAGE(55),                      // /REALMSG
    CHAOS_MESSAGE(56),                           // /CAOSMSG
    CITIZEN_MESSAGE(57),                         // /CIUMSG
    CRIMINAL_MESSAGE(58),                        // /CRIMSG
    TALK_AS_NPC(59),                             // /TALKAS
    REMOVE_ITEM_AREA(60),                        // /MASSDEST
    ACCEPT_ROYAL_COUNCIL(61),                    // /ACEPTCONSE
    ACCEPT_CHAOS_COUNCIL(62),                    // /ACEPTCONSECAOS
    SHOW_OBJ_MAP(63),                            // /PISO
    DUMB(64),                                    // /ESTUPIDO
    NO_DUMB(65),                                 // /NOESTUPIDO
    DUMP_SECURITY(66),                           // /DUMPSECURITY
    KICK_COUNCIL(67),                            // /KICKCONSE
    SET_TRIGGER(68),                             // /TRIGGER
    SHOW_TRIGGER(69),                            // /TRIGGER with no args
    BAN_IP_LIST(70),                             // /BANIPLIST
    BAN_IP_RELOAD(71),                           // /BANIPRELOAD
    GUILD_MEMBERS(72),                           // /MIEMBROSCLAN
    GUILD_BAN(73),                               // /BANCLAN
    BAN_IP(74),                                  // /BANIP
    UNBAN_IP(75),                                // /UNBANIP
    CREATE_OBJ(76),                              // /CI
    REMOVE_ITEM(77),                             // /DEST
    REMOVE_CHAOS(78),                            // /NOCAOS
    REMOVE_ARMY(79),                             // /NOREAL
    PLAY_MUSIC(80),                              // /FORCEMIDI
    PLAY_SOUND(81),                              // /FORCEWAV
    REMOVE_PUNISHMENT(82),                       // /BORRARPENA
    BLOCK_TILE(83),                              // /BLOQ
    REMOVE_NPC_NO_RESPAWN(84),                   // /MATA
    REMOVE_NPC_AREA(85),                         // /MASSKILL
    LAST_IP(86),                                 // /LASTIP
    CHANGE_MOTD(87),                             // /MOTDCAMBIA
    SET_MOTD(88),                                // ZMOTD
    SYSTEM_MESSAGE(89),                          // /SMSG
    CREATE_NPC(90),                              // /ACC
    CREATE_NPC_WITH_RESPAWN(91),                 // /RACC
    IMPERIAL_ARMOUR(92),                         // /AI1 - 4
    CHAOS_ARMOUR(93),                            // /AC1 - 4
    NAVIGATION(94),                              // /NAVE
    ADMIN_SERVER(95),                            // /HABILITAR
    SHUTDOWN(96),                                // /APAGAR
    TURN_CRIMINAL(97),                           // /CONDEN
    REMOVE_FACTIONS(98),                         // /RAJAR
    GUILD_KICK(99),                              // /RAJARCLAN
    PLAYER_EMAIL(100),                           // /LASTEMAIL
    ALTERNATE_PASSWORD(101),                     // /APASS
    CHANGE_EMAIL(102),                           // /AEMAIL
    CHANGE_NICK(103),                            // /ANAME
    SENTINEL(104),                               // /CENTINELAACTIVADO
    BACKUP(105),                                 // /DOBACKUP 
    SHOW_GUILD_MESSAGES(106),                    // /SHOWCMSG
    SAVE_MAP(107),                               // /GUARDAMAPA
    CHANGE_MAP_INFO_PK(108),                     // /MODMAPINFO PK
    CHANGE_MAP_INFO_BACKUP(109),                 // /MODMAPINFO BACKUP
    CHANGE_MAP_INFO_RESTRICTED(110),             // /MODMAPINFO RESTRINGIR
    CHANGE_MAP_INFO_NO_MAGIC(111),               // /MODMAPINFO MAGIASINEFECTO
    CHANGE_MAP_INFO_NO_INVI(112),                // /MODMAPINFO INVISINEFECTO
    CHANGE_MAP_INFO_NO_RESU(113),                // /MODMAPINFO RESUSINEFECTO
    CHANGE_MAP_INFO_LAND(114),                   // /MODMAPINFO TERRENO
    CHANGE_MAP_INFO_ZONE(115),                   // /MODMAPINFO ZONA
    CHANGE_MAP_INFO_STEAL_NPC(116),              // /MODMAPINFO ROBONPCm
    CHANGE_MAP_INFO_NO_OCULTAR(117),             // /MODMAPINFO OCULTARSINEFECTO
    CHANGE_MAP_INFO_NO_INVOCAR(118),             // /MODMAPINFO INVOCARSINEFECTO
    SAVE_CHAR(119),                              // /GRABAR
    CLEAN_SOS(120),                              // /BORRAR SOS
    SHOW_SERVER_FORM(121),                       // /SHOW INT
    NIGHT(122),                                  // /NOCHE
    KICK_ALL(123),                               // /ECHARTODOSPJS
    RELOAD_NPC(124),                             // /RELOADNPCS
    RELOAD_SERVER_INI(125),                      // /RELOADSINI
    RELOAD_SPELL(126),                           // /RELOADHECHIZOS
    RELOAD_OBJ(127),                             // /RELOADOBJ
    RESTART(128),                                // /REINICIAR
    AUTO_UPDATE(129),                            // /AUTOUPDATE
    CHAT_COLOR(130),                             // /CHATCOLOR
    SHOW_IGNORED(131),                           // /IGNORADO
    PLAYER_SLOT(132),                            // /SLOT
    SET_INI_VAR(133),                            // /SETINIVAR LLAVE CLAVE VALOR
    CREATE_PRETORIAN_CLAN(134),                  // /CREARPRETORIANOS
    REMOVE_PRETORIAN_CLAN(135),                  // /ELIMINARPRETORIANOS
    ENABLE_DENOUNCES(136),                       // /DENUNCIAS
    SHOW_DENOUNCES_LIST(137),                    // /SHOW DENUNCIAS
    MAP_MESSAGE(138),                            // /MAPMSG
    SET_DIALOG(139),                             // /SETDIALOG
    IMPERSONATE(140),                            // /IMPERSONAR
    IMITATE(141),                                // /MIMETIZAR
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