package org.aoclient.network.protocol.command;

/**
 * Comandos disponibles del juego junto con sus descripciones. Esta clase centraliza la definicion de los comandos, eliminando
 * referencias hardcodeadas y facilitando el mantenimiento.
 * <p>
 * Cada comando esta asociado con su nombre (incluyendo el prefijo '/') y una descripcion que explica su proposito o funcion
 * dentro del juego.
 * <p>
 * Junto a cada comando se muestra el comando viejo como comentario.
 */

public enum GameCommand {

    // ==================== BASIC COMMANDS ====================
    BALANCE("/balance", "Check your account balance"), // /balance
    BET("/bet", "Gamble gold with gambler NPC"), // /apostar
    BMSG("/bmsg", "Send message to council members"), // /bmsg
    BUG("/bug", "Report a bug to administrators"), // /bug
    CENTINEL_CODE("/centinelcode", "Submit centinel verification code"), // /centinela
    CONSULTATION("/consultation", "Make a consultation request"), // /consulta
    CLAIM_REWARD("/reward", "Claim daily reward"), // /recompensa
    DEPOSIT("/deposit", "Deposit gold in the bank"), // /depositar
    DESC("/desc", "Change your character description"), // /desc
    ENLIST("/enlist", "Enlist in faction army"), // /enlistar
    EST("/est", "Show your character statistics"), // /est
    EXIT("/exit", "Exit the game safely"), // /salir
    EXTRACT_GOLD("/extractgold", "Withdraw gold from the bank"), // /retirar
    GM("/gm", "Request help from Game Master"), // /gm
    HEAL("/heal", "Heal other players"), // /curar
    HELP("/?", "Show available commands"), // NUEVO COMANDO
    INFORMATION("/information", "Get server information"), // /informacion
    LEAVE_FACTION("/leavefaction", "Leave current faction"), // /retirarfaccion
    MEDITATE("/meditate", "Meditate to recover mana"), // /meditar
    MOTD("/motd", "Show message of the day"), // /motd
    ONLINE("/online", "Show online players count"), // /online
    OPEN_VAULT("/openvault", "Open bank interface"), // /boveda
    PET_FOLLOW("/petfollow", "Make your pet follow you"), // /acompañar
    PET_RELEASE("/release", "Release your pet"), // /liberar
    PET_STAY("/quieto", "Make your pet stay in place"), // /quieto
    POLL("/poll", "Participate in server polls"), // /encuesta
    REPORT("/report", "Report a player for misconduct"), // /denunciar
    REST("/rest", "Rest to recover stamina"), // /descansar
    RESURRECT("/resurrect", "Resurrect other players"), // /resucitar
    ROL("/rol", "Request role master assistance"), // /rol
    SHARE_NPC("/sharenpc", "Share NPC with other players"), // /compartirnpc
    STOP_SHARING_NPC("/stopsharingnpc", "Stop sharing NPC"), // /nocompartirnpc
    TRADE("/trade", "Start trading with NPC"), // //comerciar
    TRAIN("/train", "Show available creatures to train"), // /entrenar
    UPTIME("/uptime", "Show server uptime"), // /uptime

    // ==================== GUILD COMMANDS ====================
    GUILD_BAN("/guildban", "Ban a guild"), // /banclan
    GUILD_FOUND("/guildfound", "Found a new guild"), // /fundarclan
    GUILD_KICK("/guildkick", "Kick member from guild"), // /rajarclan
    GUILD_LEAVE("/guildleave", "Leave the guild"), // /salirclan
    GUILD_MEMBER_LIST("/guildmemberlist", "Show guild member list"), // /miembrosclan
    GUILD_MSG("/guildmsg", "Send message to guild members"), // /cmsg
    GUILD_MSG_HISTORY("/guildmsghistory", "Show guild message history"), // /showcmsg
    GUILD_ONLINE("/guildonline", "Show online guild members"), // /onlineclan
    GUILD_ONLINE_SPECIFIC("/guildonlinespecific", "Show online members of specific guild"), // /onclan
    GUILD_VOTE("/guildvote", "Vote in guild elections"), // /voto

    // ==================== PARTY COMMANDS ====================
    PARTY_ACCEPT("/partyaccept", "Accept player into party"), // /acceptparty
    PARTY_CREATE("/partycreate", "Create a party"), // /crearparty
    PARTY_JOIN("/partyjoin", "Join or request to join a party"), // /party
    PARTY_KICK("/partykick", "Kick member from party"), // /echarparty
    PARTY_LEAVE("/partyleave", "Leave the party"), // /salirparty
    PARTY_MSG("/partymsg", "Send message to party members"), // /pmsg
    PARTY_ONLINE("/partyonline", "Show online party members"), // /onlineparty
    PARTY_SET_LEADER("/partysetleader", "Transfer party leadership"), // /partylider

    // ==================== GM COMMANDS ====================
    AC("/ac", "Give chaos faction armor"), // /ac
    ACEPT_CONSE("/aceptconse", "Accept royal council member"), // /aceptconse
    ACEPT_CONSE_CHAOS("/aceptconsechaos", "Accept chaos council member"), // /aceptconsecaos
    ADMINSERVER("/adminserver", "Toggle server for administrators"), // /habilitar
    AEMAIL("/aemail", "Change player's email address"), // /aemail
    AI("/ai", "Give imperial faction armor"), // /ai
    ANAME("/aname", "Change player's name"), // /aname
    APASS("/apass", "Copy password between characters"), // /apass
    AUTOUPDATE("/autoupdate", "Reset auto-update system"), // /autoupdate
    BAN("/ban", "Ban a player from server"), // /ban
    BANIPLIST("/baniplist", "Show list of banned IP addresses"), // /baniplist
    BANIPRELOAD("/banipreload", "Reload banned IP list"), // /banipreload
    BAN_IP("/banip", "Ban IP address or player"), // /banip
    BLOQ("/bloq", "Toggle tile blocking"), // /bloq
    CAOSMSG("/caosmsg", "Send message to chaos legion"), // /caosmsg
    CC("/cc", "Show creature spawn list"), // /cc
    CENTINEL("/centinel", "Toggle centinel system"), // /centinelaactivado
    CHAT_COLOR("/chatcolor", "Change chat text color"), // /chatcolor
    CIUMSG("/ciumsg", "Send message to all citizens"), // /ciumsg
    CLEAN("/clean", "Clean world objects"), // /limpiar
    CLEAN_NPC_INV("/cleannpcinv", "Clean NPC inventory"), // /resetinv
    CLEAR("/clear", "Clear SOS message list"), // /borrar
    CONDEN("/conden", "Turn player into criminal"), // /conden
    CREATE_NPC("/createnpc", "Create NPC at current location"), // /acc
    CREATE_OBJ("/createobject", "Create object at current location"), // /co
    CREATE_TELEPORT("/createteleport", "Create teleport at location"), // /ct
    CRIMSG("/crimsg", "Send message to all criminals"), // /crimsg
    DESTROYITEMS("/destroyitems", "Destroy items"), // /dest
    DESTROYTELEPORT("/destroyteleport", "Destroy teleport at current location"), // /dt
    DOBACKUP("/dobackup", "Create server backup"), // /dobackup
    DUMPSECURITY("/dumpsecurity", "Dump security IP tables"), // /dumpsecurity
    ESTUPID("/estupid", "Make player unable to speak"), // /estupido
    EXECUTE("/execute", "Execute a player"), // /ejecutar
    FORGIVE("/forgive", "Forgive player's crimes"), // /perdon
    GMSG("/gmsg", "Send global GM message"), // /gmsg
    HIDING("/hiding", "Show hidden players"), // /hiding
    HOME("/home", "Return to home city"), // /hogar
    INVISIBILITY("/invisible", "Toggle invisibility"), // /invisible
    IP2NICK("/ip2nick", "Get nickname from IP address"), // /ip2nick
    JAIL("/jail", "Send player to jail"), // /carcel
    KICK("/kick", "Kick player from server"), // /echar
    KICKALL("/kickall", "Kick all players from server"), // /echartodospjs
    KICK_CONSE("/kickconse", "Remove player from council"), // /kickconse
    LAST_EMAIL("/lastemail", "Show player's last email"), // /lastemail
    LAST_IP("/lastip", "Show player's last IP address"), // /lastip
    MOD_MAP("/modmap", "Modify map properties"), // /modmapinfo
    MOD_PLAYER("/modplayer", "Modify player's character properties"), // /mod
    MOTD_CHANGE("/motdchange", "Change message of the day"), // /motdcambia
    NAVE("/nave", "Toggle navigation mode"), // /nave
    NICK2IP("/nick2ip", "Get IP address from nickname"), // /nick2ip
    NIGHT("/night", "Change time to night"), // /noche
    NO_REAL("/noreal", "Remove player from royal army"), // /noreal
    NO_STUPID("/noestupido", "Restore player's ability to speak"), // /noestupido
    NPC_FOLLOW("/npcfollow", "Make NPC follow you"), // /seguir
    ONLINE_CHAOS("/onlinechaos", "Show online chaos legion members"), // /onlinecaos
    ONLINE_GM("/onlinegm", "Show online game masters"), // /onlinegm
    ONLINE_MAP("/onlinemap", "Show players online in specific map"), // /onlinemap
    ONLINE_ROYAL("/onlineroyal", "Show online royal army members"), // /onlinereal
    PANEL_GM("/panelgm", "Open GM panel"), // /panelgm
    PING("/ping", "Check server connection latency"), // /ping
    PLAY_MUSIC("/playmusic", "Play music"), // /playmusic
    PLAY_SOUND("/playsound", "Play sound"), // /playsound
    PLAYER_INFO("/playerinfo", "Show detailed player information"), // /info
    PLAYER_INV("/playerinv", "Show player's inventory"), // /inv
    RACC("/racc", "Create respawning NPC"), // /racc
    RAIN("/rain", "Toggle rain"), // /lluvia
    RAJAR("/rajar", "Remove player from all factions"), // /rajar
    REALMSG("/realmsg", "Send message to royal army"), // /realmsg
    RELOAD_NPCS("/reloadnpcs", "Reload NPCs"), // /reloadnpcs
    RELOAD_OBJ("/reloadobj", "Reload object"), // /reloadobj
    RELOAD_SERVER_INI("/reloadserverini", "Reload server INI settings"), // /reloadsini
    RELOAD_SPELLS("/reloadspells", "Reload spells"), // /reloadhechizos
    REM("/rem", "Add comment to server log"), // /rem
    REMOVE_ITEM_AREA("/removeitemarea", "Remove items in area"), // /masdest
    REMOVE_NPC("/removenpc", "Remove npc without respawn"), // /mata
    REMOVE_NPC_AREA("/removenpcarea", "Remove npcs in area"), // /masskill
    REMOVE_NPC_RESPAWN("/removenpcrespawn", "Remove NPC with respawn"), // /rmata
    REMOVE_PLAYER_FROM_CHAOS("/nocaos", "Remove player from chaos legion"), // /nocaos
    REMOVE_PUNISHMENT("/borrarpena", "Remove player punishment"), // /borrarpena
    REQUEST_CHAR_BANK("/charbank", "Show player's bank contents"), // /bov
    REQUEST_CHAR_GOLD("/chargold", "Show player's gold amount"), // /bal
    RESTART("/restart", "Restart the server"), // /reiniciar
    REVIVIR("/revivir", "Resurrect a player"), // /revivir
    RMSG("/rmsg", "Send server-wide message"), // /rmsg
    SAVE("/save", "Save all character data"), // /grabar
    SAVEMAP("/savemap", "Save current map"), // /guardamapa
    SEE_WORKERS("/seeworkers", "Show working players"), // /trabajando
    SETDESC("/setdesc", "Set player's description"), // /setdesc
    SETINIVAR("/setinivar", "Set server configuration variable"), // /setinivar
    SHOW("/show", "Show server information panels"), // /show
    SHOW_HIDDEN_PLAYERS("/showhiddenplayers", "Show hidden players"), // /ocultando
    SHOWIGNORED("/showignored", "Show ignored players list"), // /ignorado
    SHOW_LOCATION("/showlocation", "Show player's location"), // /donde
    SHOW_MOBS("/showmobs", "Show mobs in specific map"), // /nene
    SHOW_NAME("/showname", "Toggle name display above characters"), // /showname
    SHOW_OBJ_MAP("/showobjmap", "Show objets in map"), // /piso
    SHOW_PLAYER_SKILLS("/showplayerskills", "Show player's skills"), // /skills
    SHOW_PLAYER_SLOT("/showplayerslot", "Check player's inventory slot"), // /slot
    SHUTDOWN("/shutdown", "Shutdown the server"), // /apagar
    SILENT("/silent", "Silence a player"), // /silenciar
    SMSG("/smsg", "Send system message"), // /smsg
    STAT("/stat", "Show player's statistics"), // /stat
    SUM("/sum", "Summon player to your location"), // /sum
    TALKAS("/talkas", "Speak as NPC character"), // /talkas
    TELEPORT("/teleport", "Teleport player to location"), // /telep
    TELEPORT_NEAR_TO_PLAYER("/teleportneartoplayer", "Teleport near specified player"), // /ircerca
    TELEPORT_TARGET("/teleporttarget", "Teleport to target location"), // /teleploc
    TELEPORT_TO_PLAYER("/teleporttoplayer", "Teleport to specific player"), // /ira
    TIME("/time", "Show current server time"), // /hora
    TRIGGER("/trigger", "Set or check map trigger"), // /trigger
    UNBAN("/unban", "Unban a player"), // /unban
    UNBANIP("/unbanip", "Unban an IP address"), // /unbanip
    WARNING("/warning", "Send warning to player"); // /advertencia

    private final String command;
    private final String description;

    GameCommand(String command, String description) {
        this.command = command;
        this.description = description;
    }

    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }

}
