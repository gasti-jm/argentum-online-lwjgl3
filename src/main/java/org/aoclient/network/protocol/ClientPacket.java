package org.aoclient.network.protocol;

/**
 * <p>
 * Representa los distintos comandos y solicitudes que el cliente puede enviar al servidor. Estos identificadores se utilizan en
 * los metodos <b>write</b> de la clase {@link org.aoclient.network.protocol.Protocol} para construir los paquetes
 * correspondientes que seran enviados al servidor. La estructura del enumerador refleja todas las posibles interacciones
 * iniciadas por el cliente, como movimientos, acciones de combate, uso de objetos, comunicacion y comandos administrativos.
 * <p>
 * Los comandos estan referenciados por una descripcion corta que corresponde a una abreviatura o representacion textual del
 * comando desde el punto de vista del cliente (por ejemplo, {@code /SALIR} para {@code QUIT}).
 * <p>
 * Funcionalidades principales:
 * <ul>
 * <li>Definir comandos relacionados con la interaccion basica del jugador, como movimiento, combate y comunicacion.
 * <li>Manejar solicitudes hacia el servidor, como actualizaciones de estado, informacion de clanes, comercio y banca.
 * <li>Controlar funcionalidades específicas del juego como fabricacion, capacidades de mascotas y configuraciones de guilds.
 * <li>Admitir comandos administrativos y de consulta hacia el servidor.
 * </ul>
 * <p>
 * <b>IMPORTANTE</b>: Los IDs de los enums se asignan para que coincidan <b>exactamente</b> con la posicion del paquete
 * correspondiente en el {@code ClientPacketID} del servidor VB6 dentro de {@code Protocol.bas}. Esta coincidencia es fundamental
 * para la correcta comunicacion cliente-servidor.
 * <p>
 * FIXME Actualizar comentarios de comandos
 */

public enum ClientPacket {

    LOGIN_EXISTING_CHAR(0),         // OLOGIN
    THROW_DICES(1),                 // TIRDAD
    LOGIN_NEW_CHAR(2),              // NLOGIN
    TALK(3),                        // ;
    YELL(4),                        // -
    WHISPER(5),                     // \
    WALK(6),                        // M
    REQUEST_POSITION_UPDATE(7),     // RPU
    ATTACK(8),                      // AT
    PICK_UP(9),                     // AG
    SAFE_TOGGLE(10),                //  SEG & SEG (SEG//s el comportamiento tiene que ser codificado en el cliente)
    RESUSCITATION_SAFE_TOGGLE(11),
    REQUEST_GUILD_LEADER_INFO(12),  // GLINFO
    REQUEST_ATTRIBUTES(13),         // ATR
    REQUEST_FAME(14),               // FAMA
    REQUEST_SKILLS(15),             // ESKI
    REQUEST_MINI_STATS(16),         // FEST
    COMMERCE_END(17),               // FINCOM
    USER_COMMERCE_END(18),          // FINCOMUSU
    USER_COMMERCE_CONFIRM(19),
    COMMERCE_CHAT(20),
    BANK_END(21),                   // FINBAN
    USER_COMMERCE_OK(22),           // COMUSUOK
    USER_COMMERCE_REJECT(23),       // COMUSUNO
    DROP(24),                       // TI
    CAST_SPELL(25),                 // LH
    LEFT_CLICK(26),                 // LC
    DOUBLE_CLICK(27),               // RC
    WORK(28),                       // UK
    USE_SPELL_MACRO(29),            // UMH
    USE_ITEM(30),                   // USA
    CRAFT_BLACKSMITH(31),           // CNS
    CRAFT_CARPENTER(32),            // CNC
    WORK_LEFT_CLICK(33),            // WLC
    CREATE_NEW_GUILD(34),           // CIG
    SPELL_INFO(35),                 // INFS
    EQUIP_ITEM(36),                 // EQUI
    CHANGE_HEADING(37),             // CHEA
    MODIFY_SKILLS(38),              // SKSE
    TRAIN(39),                      // ENTR
    COMMERCE_BUY(40),               // COMP
    BANK_EXTRACT_ITEM(41),          // RETI
    COMMERCE_SELL(42),              // VEND
    BANK_DEPOSIT(43),               // DEPO
    FORUM_POST(44),                 // DEMSG
    MOVE_SPELL(45),                 // DESPHE
    MOVE_BANK(46),
    CLAN_CODEX_UPDATE(47),          // DESCOD
    USER_COMMERCE_OFFER(48),        // OFRECER
    GUILD_ACCEPT_PEACE(49),         // ACEPPEAT
    GUILD_REJECT_ALLIANCE(50),      // RECPALIA
    GUILD_REJECT_PEACE(51),         // RECPPEAT
    GUILD_ACCEPT_ALLIANCE(52),      // ACEPALIA
    GUILD_OFFER_PEACE(53),          // PEACEOFF
    GUILD_OFFER_ALLIANCE(54),       // ALLIEOFF
    GUILD_ALLIANCE_DETAILS(55),     // ALLIEDET
    GUILD_PEACE_DETAILS(56),        // PEACEDET
    GUILD_REQUEST_JOINER_INFO(57),  // ENVCOMEN
    GUILD_ALLIANCE_PROP_LIST(58),   // ENVALPRO
    GUILD_PEACE_PROP_LIST(59),      // ENVPROPP
    GUILD_DECLARE_WAR(60),          // DECGUERR
    GUILD_NEW_WEBSITE(61),          // NEWWEBSI
    GUILD_ACCEPT_NEW_MEMBER(62),    // ACEPTARI
    GUILD_REJECT_NEW_MEMBER(63),    // RECHAZAR
    GUILD_KICK_MEMBER(64),          // ECHARCLA
    GUILD_UPDATE_NEWS(65),          // ACTGNEWS
    GUILD_MEMBER_INFO(66),          // 1HRINFO<
    GUILD_OPEN_ELECTIONS(67),       // ABREELEC
    GUILD_REQUEST_MEMBERSHIP(68),   // SOLICITUD
    GUILD_REQUEST_DETAILS(69),      // CLANDETAILS
    ONLINE(70),                     // /ONLINE
    QUIT(71),                       // /SALIR
    GUILD_LEAVE(72),                // /SALIRCLAN
    REQUEST_ACCOUNT_STATE(73),      // /BALANCE
    PET_STAND(74),                  // /QUIETO
    PET_FOLLOW(75),                 // /ACOMPAÑAR
    RELEASE_PET(76),                // /LIBERAR
    TRAIN_LIST(77),                 // /ENTRENAR
    REST(78),                       // /DESCANSAR
    MEDITATE(79),                   // /MEDITAR
    RESUCITATE(80),                 // /RESUCITAR
    HEAL(81),                       // /CURAR
    HELP(82),                       // /AYUDA
    REQUEST_STATS(83),              // /EST
    COMMERCE_START(84),             // /COMERCIAR
    BANK_START(85),                 // /BOVEDA
    ENLIST(86),                     // /ENLISTAR
    INFORMATION(87),                // /INFORMACION
    REWARD(88),                     // /RECOMPENSA
    REQUEST_MOTD(89),               // /MOTD
    UPTIME(90),                     // /UPTIME
    PARTY_LEAVE(91),                // /SALIRPARTY
    PARTY_CREATE(92),               // /CREARPARTY
    PARTY_JOIN(93),                 // /PARTY
    INQUIRY(94),                    // /ENCUESTA ( with no params )
    GUILD_MESSAGE(95),              // /CMSG
    PARTY_MESSAGE(96),              // /PMSG
    CENTINEL_REPORT(97),            // /CENTINELA
    GUILD_ONLINE(98),               // /ONLINECLAN
    PARTY_ONLINE(99),               // /ONLINEPARTY
    COUNCIL_MESSAGE(100),           // /BMSG
    ROLE_MASTER_REQUEST(101),       // /ROL
    GM_REQUEST(102),                // /GM
    BUG_REPORT(103),                // /_BUG
    CHANGE_DESCRIPTION(104),        // /DESC
    GUILD_VOTE(105),                // /VOTO
    PUNISHMENTS(106),               // /PENAS
    CHANGE_PASSWORD(107),           // /CONTRASEA
    GAMBLE(108),                    // /APOSTAR
    INQUIRY_VOTE(109),              // /ENCUESTA ( with parameters )
    LEAVE_FACTION(110),             // /RETIRAR ( with no arguments )
    BANK_EXTRACT_GOLD(111),         // /RETIRAR ( with arguments )
    BANK_DEPOSIT_GOLD(112),         // /DEPOSITAR
    DENOUNCE(113),                  // /DENUNCIAR
    GUILD_FUNDATE(114),             // /FUNDARCLAN
    GUILD_FUNDATION(115),
    PARTY_KICK(116),                // /ECHARPARTY
    PARTY_SET_LEADER(117),          // /PARTYLIDER
    PARTY_ACCEPT_MEMBER(118),       // /ACCEPTPARTY
    PING(119),                      // /PING
    REQUEST_PARTY_FORM(120),
    ITEM_UPGRADE(121),
    GM_COMMANDS(122),
    INIT_CRAFTING(123),
    HOME(124),
    SHOW_GUILD_NEWS(125),
    SHARE_NPC(126),                 // /COMPARTIRNPC
    STOP_SHARING_NPC(127),          // /NOCOMPARTIRNPC
    CONSULTA(128);

    private final int id;

    ClientPacket(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
