package org.aoclient.network.protocol;

/**
 * <p>
 * Esta enumeracion representa los distintos comandos y solicitudes que un cliente puede enviar a un servidor en un contexto de
 * juego en linea. Estos identificadores se utilizan en los metodos <b>write</b> de la clase
 * {@link org.aoclient.network.protocol.Protocol} para construir los paquetes correspondientes que seran enviados al servidor. La
 * estructura del enumerador refleja todas las posibles interacciones iniciadas por el cliente, como movimientos, acciones de
 * combate, uso de objetos, comunicacion y comandos administrativos.
 * <p>
 * Los comandos estan referenciados por una descripcion corta que corresponde a una abreviatura o representacion textual del
 * comando desde el punto de vista del cliente (por ejemplo, {@code /SALIR} para {@code QUIT}).
 * <p>
 * Funcionalidades principales:
 * <ul>
 * <li>Definir comandos relacionados con la interaccion basica del jugador, como movimiento, combate y comunicacion.
 * <li>Manejar solicitudes hacia el servidor, como actualizaciones de estado, informacion de clanes, comercio y banca.
 * <li>Controlar funcionalidades especificas del juego como fabricacion, capacidades de mascotas y configuraciones de guilds.
 * <li>Admitir comandos administrativos y de consulta hacia el servidor.
 * </ul>
 * <p>
 * Es importante mencionar que cada constante posee un papel bien definido dentro de las interacciones cliente-servidor. La
 * funcionalidad debe ser correctamente implementada en ambos extremos (cliente y servidor) para garantizar una respuesta adecuada.
 */

public enum ClientPacket {

    LOGIN_EXISTING_CHAR,         // OLOGIN
    THROW_DICES,                 // TIRDAD
    LOGIN_NEW_CHAR,              // NLOGIN
    TALK,                        // ;
    YELL,                        // -
    WHISPER,                     // \
    WALK,                        // M
    REQUEST_POSITION_UPDATE,     // RPU
    ATTACK,                      // AT
    PICK_UP,                     // AG
    SAFE_TOGGLE,                 //  SEG & SEG (SEG//s el comportamiento tiene que ser codificado en el cliente)
    RESUSCITATION_SAFE_TOGGLE,
    REQUEST_GUILD_LEADER_INFO,   // GLINFO
    REQUEST_ATTRIBUTES,          // ATR
    REQUEST_FAME,                // FAMA
    REQUEST_SKILLS,              // ESKI
    REQUEST_MINI_STATS,          // FEST
    COMMERCE_END,                // FINCOM
    USER_COMMERCE_END,           // FINCOMUSU
    USER_COMMERCE_CONFIRM,
    COMMERCE_CHAT,
    BANK_END,                    // FINBAN
    USER_COMMERCE_OK,            // COMUSUOK
    USER_COMMERCE_REJECT,        // COMUSUNO
    DROP,                        // TI
    CAST_SPELL,                  // LH
    LEFT_CLICK,                  // LC
    DOUBLE_CLICK,                // RC
    WORK,                        // UK
    USE_SPELL_MACRO,             // UMH
    USE_ITEM,                    // USA
    CRAFT_BLACKSMITH,            // CNS
    CRAFT_CARPENTER,             // CNC
    WORK_LEFT_CLICK,             // WLC
    CREATE_NEW_GUILD,            // CIG
    SPELL_INFO,                  // INFS
    EQUIP_ITEM,                  // EQUI
    CHANGE_HEADING,              // CHEA
    MODIFY_SKILLS,               // SKSE
    TRAIN,                       // ENTR
    COMMERCE_BUY,                // COMP
    BANK_EXTRACT_ITEM,           // RETI
    COMMERCE_SELL,               // VEND
    BANK_DEPOSIT,                // DEPO
    FORUM_POST,                  // DEMSG
    MOVE_SPELL,                  // DESPHE
    MOVE_BANK,
    CLAN_CODEX_UPDATE,           // DESCOD
    USER_COMMERCE_OFFER,         // OFRECER
    GUILD_ACCEPT_PEACE,          // ACEPPEAT
    GUILD_REJECT_ALLIANCE,       // RECPALIA
    GUILD_REJECT_PEACE,          // RECPPEAT
    GUILD_ACCEPT_ALLIANCE,       // ACEPALIA
    GUILD_OFFER_PEACE,           // PEACEOFF
    GUILD_OFFER_ALLIANCE,        // ALLIEOFF
    GUILD_ALLIANCE_DETAILS,      // ALLIEDET
    GUILD_PEACE_DETAILS,         // PEACEDET
    GUILD_REQUEST_JOINER_INFO,   // ENVCOMEN
    GUILD_ALLIANCE_PROP_LIST,    // ENVALPRO
    GUILD_PEACE_PROP_LIST,       // ENVPROPP
    GUILD_DECLARE_WAR,           // DECGUERR
    GUILD_NEW_WEBSITE,           // NEWWEBSI
    GUILD_ACCEPT_NEW_MEMBER,     // ACEPTARI
    GUILD_REJECT_NEW_MEMBER,     // RECHAZAR
    GUILD_KICK_MEMBER,           // ECHARCLA
    GUILD_UPDATE_NEWS,           // ACTGNEWS
    GUILD_MEMBER_INFO,           // 1HRINFO<
    GUILD_OPEN_ELECTIONS,        // ABREELEC
    GUILD_REQUEST_MEMBERSHIP,    // SOLICITUD
    GUILD_REQUEST_DETAILS,       // CLANDETAILS
    ONLINE,                      // /ONLINE
    QUIT,                        // /SALIR
    GUILD_LEAVE,                 // /SALIRCLAN
    REQUEST_ACCOUNT_STATE,       // /BALANCE
    PET_STAND,                   // /QUIETO
    PET_FOLLOW,                  // /ACOMPAÑAR
    RELEASE_PET,                 // /LIBERAR
    TRAIN_LIST,                  // /ENTRENAR
    REST,                        // /DESCANSAR
    MEDITATE,                    // /MEDITAR
    RESUCITATE,                  // /RESUCITAR
    HEAL,                        // /CURAR
    HELP,                        // /AYUDA
    REQUEST_STATS,               // /EST
    COMMERCE_START,              // /COMERCIAR
    BANK_START,                  // /BOVEDA
    ENLIST,                      // /ENLISTAR
    INFORMATION,                 // /INFORMACION
    REWARD,                      // /RECOMPENSA
    REQUEST_MOTD,                // /MOTD
    UPTIME,                      // /UPTIME
    PARTY_LEAVE,                 // /SALIRPARTY
    PARTY_CREATE,                // /CREARPARTY
    PARTY_JOIN,                  // /PARTY
    INQUIRY,                     // /ENCUESTA ( with no params )
    GUILD_MESSAGE,               // /CMSG
    PARTY_MESSAGE,               // /PMSG
    CENTINEL_REPORT,             // /CENTINELA
    GUILD_ONLINE,                // /ONLINECLAN
    PARTY_ONLINE,                // /ONLINEPARTY
    COUNCIL_MESSAGE,             // /BMSG
    ROLE_MASTER_REQUEST,         // /ROL
    GM_REQUEST,                  // /GM
    BUG_REPORT,                  // /_BUG
    CHANGE_DESCRIPTION,          // /DESC
    GUILD_VOTE,                  // /VOTO
    PUNISHMENTS,                 // /PENAS
    CHANGE_PASSWORD,             // /CONTRASEA
    GAMBLE,                      // /APOSTAR
    INQUIRY_VOTE,                // /ENCUESTA ( with parameters )
    LEAVE_FACTION,               // /RETIRAR ( with no arguments )
    BANK_EXTRACT_GOLD,           // /RETIRAR ( with arguments )
    BANK_DEPOSIT_GOLD,           // /DEPOSITAR
    DENOUNCE,                    // /DENUNCIAR
    GUILD_FUNDATE,               // /FUNDARCLAN
    GUILD_FUNDATION,
    PARTY_KICK,                  // /ECHARPARTY
    PARTY_SET_LEADER,            // /PARTYLIDER
    PARTY_ACCEPT_MEMBER,         // /ACCEPTPARTY
    PING,                        // /PING
    REQUEST_PARTY_FORM,
    ITEM_UPGRADE,
    GM_COMMANDS,
    INIT_CRAFTING,
    HOME,
    SHOW_GUILD_NEWS,
    SHARE_NPC,                  // /COMPARTIRNPC
    STOP_SHARING_NPC,           // /NOCOMPARTIRNPC
    CONSULTA

}
