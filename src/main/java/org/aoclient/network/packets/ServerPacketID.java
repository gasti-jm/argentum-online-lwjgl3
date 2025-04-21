package org.aoclient.network.packets;

import org.aoclient.network.protocol.Protocol;

/**
 * <p>
 * Enumeracion que define todos los identificadores de paquetes enviados por el servidor al cliente en el protocolo de
 * comunicacion.
 * <p>
 * {@code ServerPacketID} contiene constantes para cada tipo de mensaje que el servidor puede enviar al cliente, permitiendo una
 * identificacion clara y tipada de los paquetes durante el procesamiento de datos recibidos. Cada valor del enumerador
 * corresponde a un tipo especifico de operacion o informacion que el servidor transmite.
 * <p>
 * Estos identificadores se utilizan en el metodo {@link Protocol#handleIncomingData() handleIncomingData()} para determinar el
 * tipo de paquete recibido y dirigirlo al manejador correspondiente. La estructura del enumerador refleja todas las posibles
 * interacciones iniciadas por el servidor, como actualizaciones de estado, notificaciones de eventos, respuestas a solicitudes
 * del cliente y cambios en el entorno de juego.
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

public enum ServerPacketID {

    logged,                  // LOGGED
    RemoveDialogs,           // QTDL
    RemoveCharDialog,        // QDL
    NavigateToggle,          // NAVEG
    Disconnect,              // FINOK
    CommerceEnd,             // FINCOMOK
    BankEnd,                 // FINBANOK
    CommerceInit,            // INITCOM
    BankInit,                // INITBANCO
    UserCommerceInit,        // INITCOMUSU
    UserCommerceEnd,         // FINCOMUSUOK
    UserOfferConfirm,
    CommerceChat,

    ShowBlacksmithForm,      // SFH
    ShowCarpenterForm,       // SFC
    UpdateSta,               // ASS
    UpdateMana,              // ASM
    UpdateHP,                // ASH
    UpdateGold,              // ASG
    UpdateBankGold,
    UpdateExp,               // ASE
    ChangeMap,               // CM
    PosUpdate,               // PU
    ChatOverHead,            // ||
    ConsoleMsg,              // || - Beware!! its the same as above, but it was properly splitted
    GuildChat,               // |+
    ShowMessageBox,          // !!
    UserIndexInServer,       // IU
    UserCharIndexInServer,   // IP
    CharacterCreate,         // CC
    CharacterRemove,         // BP
    CharacterChangeNick,
    CharacterMove,           // MP, +, * and _ //
    ForceCharMove,
    CharacterChange,         // CP
    ObjectCreate,            // HO
    ObjectDelete,            // BO
    BlockPosition,           // BQ
    PlayMIDI,                // TM
    PlayWave,                // TW
    guildList,               // GL
    AreaChanged,             // CA
    PauseToggle,             // BKW
    RainToggle,              // LLU
    CreateFX,                // CFX
    UpdateUserStats,         // EST
    WorkRequestTarget,       // T01
    ChangeInventorySlot,     // CSI
    ChangeBankSlot,          // SBO
    ChangeSpellSlot,         // SHS
    Atributes,               // ATR
    BlacksmithWeapons,       // LAH
    BlacksmithArmors,        // LAR
    CarpenterObjects,        // OBR
    RestOK,                  // DOK
    ErrorMsg,                // ERR
    Blind,                   // CEGU
    Dumb,                    // DUMB
    ShowSignal,              // MCAR
    ChangeNPCInventorySlot,  // NPCI
    UpdateHungerAndThirst,   // EHYS
    Fame,                    // FAMA
    MiniStats,               // MEST
    LevelUp,                 // SUNI
    AddForumMsg,             // FMSG
    ShowForumForm,           // MFOR
    SetInvisible,            // NOVER
    DiceRoll,                // DADOS
    MeditateToggle,          // MEDOK
    BlindNoMore,             // NSEGUE
    DumbNoMore,              // NESTUP
    SendSkills,              // SKILLS
    TrainerCreatureList,     // LSTCRI
    guildNews,               // GUILDNE
    OfferDetails,            // PEACEDE & ALLIEDE
    AlianceProposalsList,    // ALLIEPR
    PeaceProposalsList,      // PEACEPR
    CharacterInfo,           // CHRINFO
    GuildLeaderInfo,         // LEADERI
    GuildMemberInfo,
    GuildDetails,            // CLANDET
    ShowGuildFundationForm,  // SHOWFUN
    ParalizeOK,              // PARADOK
    ShowUserRequest,         // PETICIO
    TradeOK,                 // TRANSOK
    BankOK,                  // BANCOOK
    ChangeUserTradeSlot,     // COMUSUINV
    SendNight,               // NOC
    Pong,
    UpdateTagAndStatus,

    //GM messages
    SpawnList,               // SPL
    ShowSOSForm,             // MSOS
    ShowMOTDEditionForm,     // ZMOTD
    ShowGMPanelForm,         // ABPANEL
    UserNameList,            // LISTUSU

    ShowGuildAlign,
    ShowPartyForm,
    UpdateStrenghtAndDexterity,
    UpdateStrenght,
    UpdateDexterity,
    AddSlots,
    MultiMessage,
    StopWorking,
    CancelOfferItem

}
