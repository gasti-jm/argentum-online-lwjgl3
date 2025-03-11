package org.aoclient.network.packets;

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
