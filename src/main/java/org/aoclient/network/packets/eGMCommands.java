package org.aoclient.network.packets;

public enum eGMCommands {

    StrawBundle,
    GMMessage,                  // '/GMSG
    showName,                   // '/SHOWNAME
    OnlineRoyalArmy,            // '/ONLINEREAL
    OnlineChaosLegion,          // '/ONLINECAOS
    GoNearby,                   // '/IRCERCA
    Comment,                    // '/REM
    serverTime,                 // '/HORA
    Where,                      // '/DONDE
    CreaturesInMap,             // '/NENE
    WarpMeToTarget,             // '/TELEPLOC
    WarpChar,                   // '/TELEP
    Silence,                    // '/SILENCIAR
    SOSShowList,                // '/SHOW SOS
    SOSRemove,                  // 'SOSDONE
    GoToChar,                   // '/IRA
    Invisible,                  // '/INVISIBLE
    GMPanel,                    // '/PANELGM
    RequestUserList,            // 'LISTUSU
    Working,                    // '/TRABAJANDO
    Hiding,                     // '/OCULTANDO
    Jail,                       // '/CARCEL
    KillNPC,                    // '/RMATA
    WarnUser,                   // '/ADVERTENCIA
    EditChar,                   // '/MOD
    RequestCharInfo,            // '/INFO
    RequestCharStats,           // '/STAT
    RequestCharGold,            // '/BAL
    RequestCharInventory,       // '/INV
    RequestCharBank,            // '/BOV
    RequestCharSkills,          // '/SKILLS
    ReviveChar,                 // '/REVIVIR
    OnlineGM,                   // '/ONLINEGM
    OnlineMap,                  // '/ONLINEMAP
    Forgive,                    // '/PERDON
    Kick,                       // '/ECHAR
    Execute,                    // '/EJECUTAR
    banChar,                    // '/BAN
    UnbanChar,                  // '/UNBAN
    NPCFollow,                  // '/SEGUIR
    SummonChar,                 // '/SUM
    SpawnListRequest,           // '/CC
    SpawnCreature,              // 'SPA
    ResetNPCInventory,          // '/RESETINV
    CleanWorld,                 // '/LIMPIAR
    ServerMessage,              // '/RMSG
    nickToIP,                   // '/NICK2IP
    IPToNick,                   // '/IP2NICK
    GuildOnlineMembers,         // '/ONCLAN
    TeleportCreate,             // '/CT
    TeleportDestroy,            // '/DT
    RainToggle,                 // '/LLUVIA
    SetCharDescription,         // '/SETDESC
    ForceMIDIToMap,             // '/FORCEMIDIMAP
    ForceWAVEToMap,             // '/FORCEWAVMAP
    RoyalArmyMessage,           // '/REALMSG
    ChaosLegionMessage,         // '/CAOSMSG
    CitizenMessage,             // '/CIUMSG
    CriminalMessage,            // '/CRIMSG
    TalkAsNPC,                  // '/TALKAS
    DestroyAllItemsInArea,      // '/MASSDEST
    AcceptRoyalCouncilMember,   // '/ACEPTCONSE
    AcceptChaosCouncilMember,   // '/ACEPTCONSECAOS
    ItemsInTheFloor,            // '/PISO
    MakeDumb,                   // '/ESTUPIDO
    MakeDumbNoMore,             // '/NOESTUPIDO
    dumpIPTables,               // '/DUMPSECURITY
    CouncilKick,                // '/KICKCONSE
    SetTrigger,                 // '/TRIGGER
    AskTrigger,                 // '/TRIGGER with no args
    BannedIPList,               // '/BANIPLIST
    BannedIPReload,             // '/BANIPRELOAD
    GuildMemberList,            // '/MIEMBROSCLAN
    GuildBan,                   // '/BANCLAN
    BanIP,                      // '/BANIP
    UnbanIP,                    // '/UNBANIP
    CreateItem,                 // '/CI
    DestroyItems,               // '/DEST
    ChaosLegionKick,            // '/NOCAOS
    RoyalArmyKick,              // '/NOREAL
    ForceMIDIAll,               // '/FORCEMIDI
    ForceWAVEAll,               // '/FORCEWAV
    RemovePunishment,           // '/BORRARPENA
    TileBlockedToggle,          // '/BLOQ
    KillNPCNoRespawn,           // '/MATA
    KillAllNearbyNPCs,          // '/MASSKILL
    LastIP,                     // '/LASTIP
    ChangeMOTD,                 // '/MOTDCAMBIA
    SetMOTD,                    // 'ZMOTD
    SystemMessage,              // '/SMSG
    CreateNPC,                  // '/ACC
    CreateNPCWithRespawn,       // '/RACC
    ImperialArmour,             // '/AI1 - 4
    ChaosArmour,                // '/AC1 - 4
    NavigateToggle,             // '/NAVE
    ServerOpenToUsersToggle,    // '/HABILITAR
    TurnOffServer,              // '/APAGAR
    TurnCriminal,               // '/CONDEN
    ResetFactions,               // '/RAJAR
    RemoveCharFromGuild,        // '/RAJARCLAN
    RequestCharMail,            // '/LASTEMAIL
    AlterPassword,              // '/APASS
    AlterMail,                  // '/AEMAIL
    AlterName,                  // '/ANAME
    ToggleCentinelActivated,    // '/CENTINELAACTIVADO
    DoBackUp,                   // '/DOBACKUP
    ShowGuildMessages,          // '/SHOWCMSG
    SaveMap,                    // '/GUARDAMAPA
    ChangeMapInfoPK,            // '/MODMAPINFO PK
    ChangeMapInfoBackup,        // '/MODMAPINFO BACKUP
    ChangeMapInfoRestricted,    // '/MODMAPINFO RESTRINGIR
    ChangeMapInfoNoMagic,       // '/MODMAPINFO MAGIASINEFECTO
    ChangeMapInfoNoInvi,        // '/MODMAPINFO INVISINEFECTO
    ChangeMapInfoNoResu,        // '/MODMAPINFO RESUSINEFECTO
    ChangeMapInfoLand,          // '/MODMAPINFO TERRENO
    ChangeMapInfoZone,          // '/MODMAPINFO ZONA
    ChangeMapInfoStealNpc,     // '/MODMAPINFO ROBONPCm
    ChangeMapInfoNoOcultar,    // '/MODMAPINFO OCULTARSINEFECTO
    ChangeMapInfoNoInvocar,    // '/MODMAPINFO INVOCARSINEFECTO
    SaveChars,                  // '/GRABAR
    CleanSOS,                   // '/BORRAR SOS
    ShowServerForm,             // '/SHOW INT
    night,                      // '/NOCHE
    KickAllChars,               // '/ECHARTODOSPJS
    ReloadNPCs,               // '/RELOADNPCS
    ReloadServerIni,          // '/RELOADSINI
    ReloadSpells,             // '/RELOADHECHIZOS
    ReloadObjects,            // '/RELOADOBJ
    Restart,                  // '/REINICIAR
    ResetAutoUpdate,          // '/AUTOUPDATE
    ChatColor,                // '/CHATCOLOR
    Ignored,                  // '/IGNORADO
    CheckSlot,                // '/SLOT
    SetIniVar,                // '/SETINIVAR LLAVE CLAVE VALOR
    CreatePretorianClan,      // '/CREARPRETORIANOS
    RemovePretorianClan,      // '/ELIMINARPRETORIANOS
    EnableDenounces,          // '/DENUNCIAS
    ShowDenouncesList,        // '/SHOW DENUNCIAS
    MapMessage,               // '/MAPMSG
    SetDialog,                // '/SETDIALOG
    Impersonate,              // '/IMPERSONAR
    Imitate,                  // '/MIMETIZAR
    RecordAdd,
    RecordRemove,
    RecordAddObs,
    RecordListRequest,
    RecordDetailsRequest;

}
