package org.aoclient.network.packets;

import org.aoclient.network.protocol.Protocol;

/**
 * <p>
 * Enumeracion que define todos los identificadores de paquetes enviados por el cliente al servidor en el protocolo de
 * comunicacion.
 * <p>
 * {@code ClientPacketID} contiene constantes para cada tipo de mensaje que el cliente puede enviar al servidor, permitiendo una
 * identificacion clara y tipada de los paquetes durante el proceso de construccion y envio de datos. Cada valor del enumerador
 * corresponde a un tipo especifico de accion, solicitud o comando que el cliente puede iniciar.
 * <p>
 * Estos identificadores se utilizan en los metodos <b>write</b> de la clase {@link Protocol} para construir los paquetes
 * correspondientes que seran enviados al servidor. La estructura del enumerador refleja todas las posibles interacciones
 * iniciadas por el cliente, como movimientos, acciones de combate, uso de objetos, comunicacion y comandos administrativos.
 * <p>
 * Junto a cada constante, se incluye como comentario el comando de chat asociado cuando corresponde (por ejemplo, {@code /SALIR}
 * para {@code Quit}), facilitando la comprension de la relacion entre los comandos ingresados por el usuario y los paquetes
 * enviados.
 * <p>
 * Cada miembro del enum debe mantener la misma posicion ordinal que su correspondiente en el servidor para garantizar la correcta
 * interpretacion de los paquetes en la comunicacion cliente-servidor.
 */

public enum ClientPacketID {

    LoginExistingChar,       //OLOGIN
    ThrowDices,              //TIRDAD
    LoginNewChar,            //NLOGIN
    Talk,                    //;
    Yell,                    //-
    Whisper,                 //\
    Walk,                    //M
    RequestPositionUpdate,   //RPU
    Attack,                  //AT
    PickUp,                  //AG
    SafeToggle,              ///SEG & SEG  (SEG//s behaviour has to be coded in the client)
    ResuscitationSafeToggle,
    RequestGuildLeaderInfo,  //GLINFO
    RequestAttributes,        //ATR
    RequestFame,             //FAMA
    RequestSkills,           //ESKI
    RequestMiniStats,        //FEST
    CommerceEnd,             //FINCOM
    UserCommerceEnd,         //FINCOMUSU
    UserCommerceConfirm,
    CommerceChat,
    BankEnd,                 //FINBAN
    UserCommerceOk,          //COMUSUOK
    UserCommerceReject,      //COMUSUNO
    Drop,                    //TI
    CastSpell,               //LH
    LeftClick,               //LC
    DoubleClick,             //RC
    Work,                    //UK
    UseSpellMacro,           //UMH
    UseItem,                 //USA
    CraftBlacksmith,         //CNS
    CraftCarpenter,          //CNC
    WorkLeftClick,           //WLC
    CreateNewGuild,          //CIG
    SpellInfo,               //INFS
    EquipItem,               //EQUI
    ChangeHeading,           //CHEA
    ModifySkills,            //SKSE
    Train,                   //ENTR
    CommerceBuy,             //COMP
    BankExtractItem,         //RETI
    CommerceSell,            //VEND
    BankDeposit,             //DEPO
    ForumPost,               //DEMSG
    MoveSpell,               //DESPHE
    MoveBank,
    ClanCodexUpdate,         //DESCOD
    UserCommerceOffer,       //OFRECER
    GuildAcceptPeace,        //ACEPPEAT
    GuildRejectAlliance,     //RECPALIA
    GuildRejectPeace,        //RECPPEAT
    GuildAcceptAlliance,     //ACEPALIA
    GuildOfferPeace,         //PEACEOFF
    GuildOfferAlliance,      //ALLIEOFF
    GuildAllianceDetails,    //ALLIEDET
    GuildPeaceDetails,       //PEACEDET
    GuildRequestJoinerInfo,  //ENVCOMEN
    GuildAlliancePropList,   //ENVALPRO
    GuildPeacePropList,      //ENVPROPP
    GuildDeclareWar,         //DECGUERR
    GuildNewWebsite,         //NEWWEBSI
    GuildAcceptNewMember,    //ACEPTARI
    GuildRejectNewMember,    //RECHAZAR
    GuildKickMember,         //ECHARCLA
    GuildUpdateNews,         //ACTGNEWS
    GuildMemberInfo,         //1HRINFO<
    GuildOpenElections,      //ABREELEC
    GuildRequestMembership,  //SOLICITUD
    GuildRequestDetails,     //CLANDETAILS
    Online,                  // /ONLINE
    Quit,                    // /SALIR
    GuildLeave,              // /SALIRCLAN
    RequestAccountState,     // /BALANCE
    PetStand,                // /QUIETO
    PetFollow,               // /ACOMPAÑAR
    ReleasePet,              // /LIBERAR
    TrainList,               // /ENTRENAR
    Rest,                    // /DESCANSAR
    Meditate,                // /MEDITAR
    Resucitate,              // /RESUCITAR
    Heal,                    // /CURAR
    Help,                    // /AYUDA
    RequestStats,            // /EST
    CommerceStart,           // /COMERCIAR
    BankStart,               // /BOVEDA
    Enlist,                  // /ENLISTAR
    Information,             // /INFORMACION
    Reward,                  // /RECOMPENSA
    RequestMOTD,             // /MOTD
    Uptime,                  // /UPTIME
    PartyLeave,              // /SALIRPARTY
    PartyCreate,             // /CREARPARTY
    PartyJoin,               // /PARTY
    Inquiry,                 // /ENCUESTA ( with no params )
    GuildMessage,            // /CMSG
    PartyMessage,            // /PMSG
    CentinelReport,          // /CENTINELA
    GuildOnline,             // /ONLINECLAN
    PartyOnline,             // /ONLINEPARTY
    CouncilMessage,          // /BMSG
    RoleMasterRequest,       // /ROL
    GMRequest,               // /GM
    bugReport,               // /_BUG
    ChangeDescription,       // /DESC
    GuildVote,               // /VOTO
    Punishments,             // /PENAS
    ChangePassword,          // /CONTRASEA
    Gamble,                  // /APOSTAR
    InquiryVote,             // /ENCUESTA ( with parameters )
    LeaveFaction,            // /RETIRAR ( with no arguments )
    BankExtractGold,         // /RETIRAR ( with arguments )
    BankDepositGold,         // /DEPOSITAR
    Denounce,                // /DENUNCIAR
    GuildFundate,            // /FUNDARCLAN
    GuildFundation,
    PartyKick,               // /ECHARPARTY
    PartySetLeader,          // /PARTYLIDER
    PartyAcceptMember,       // /ACCEPTPARTY
    Ping,                    // /PING

    RequestPartyForm,
    ItemUpgrade,
    GMCommands,
    InitCrafting,
    Home,
    ShowGuildNews,
    ShareNpc,                ///COMPARTIRNPC
    StopSharingNpc,          ///NOCOMPARTIRNPC
    Consulta

}
