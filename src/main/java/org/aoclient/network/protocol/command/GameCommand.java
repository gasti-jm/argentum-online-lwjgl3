package org.aoclient.network.protocol.command;

/**
 * Enumeracion que contiene todos los comandos disponibles en el juego junto con sus descripciones. Esta clase centraliza la
 * definicion de los comandos, eliminando referencias hardcodeadas y facilitando el mantenimiento.
 * <p>
 * Cada comando esta asociado con su nombre (incluyendo el prefijo '/') y una descripcion que explica su proposito o funcion
 * dentro del juego.
 * <p>
 * Junto a cada comando se muestra el comando viejo.
 */

public enum GameCommand {

    // ==================== BASIC COMMANDS ====================
    BALANCE("/balance", "Check your account balance"), // /balance
    OPEN_VAULT("/openvault", "Open bank interface"), // /boveda
    TRADE("/trade", "Start trading with NPC"), // //comerciar
    SHARE_NPC("/sharenpc", "Share NPC with other players"), // /compartirnpc
    CONSULTATION("/consultation", "Make a consultation request"), // /consulta
    HEAL("/heal", "Heal other players"), // /curar
    REST("/rest", "Rest to recover stamina"), // /descansar
    ENLIST("/enlist", "Enlist in faction army"), // /enlistar
    TRAIN("/train", "Show available creatures to train"), // /entrenar
    EST("/est", "Show your character statistics"), // /est
    GM("/gm", "Request help from Game Master"), // /gm
    HELP("/?", "Show available commands"), // NUEVO COMANDO
    INFORMATION("/information", "Get server information"), // /informacion
    PET_RELEASE("/release", "Release your pet"), // /liberar
    PET_FOLLOW("/petfollow", "Make your pet follow you"), // /acompañar
    PET_STAY("/quieto", "Make your pet stay in place"), // /quieto
    MEDITATE("/meditate", "Meditate to recover mana"), // /meditar
    MOTD("/motd", "Show message of the day"), // /motd
    STOP_SHARING_NPC("/stopsharingnpc", "Stop sharing NPC"), // /nocompartirnpc
    ONLINE("/online", "Show online players count"), // /online
    CLAIM_REWARD("/reward", "Claim daily reward"), // /recompensa
    RESURRECT("/resurrect", "Resurrect other players"), // /resucitar
    LEAVE_FACTION("/leavefaction", "Leave current faction"), // /retirarfaccion
    EXIT("/exit", "Exit the game safely"), // /salir
    UPTIME("/uptime", "Show server uptime"), // /uptime
    BET("/bet", "Gamble gold with gambler NPC"), // /apostar
    BMSG("/bmsg", "Send message to council members"), // /bmsg
    BUG("/bug", "Report a bug to administrators"), // /bug
    CENTINEL_CODE("/centinelcode", "Submit centinel verification code"), // /centinela
    REPORT("/report", "Report a player for misconduct"), // /denunciar
    DEPOSIT("/deposit", "Deposit gold in the bank"), // /depositar
    DESC("/desc", "Change your character description"), // /desc
    POLL("/poll", "Participate in server polls"), // /encuesta
    EXTRACT_GOLD("/extractgold", "Withdraw gold from the bank"), // /retirar
    ROL("/rol", "Request role master assistance"), // /rol

    // ==================== PARTY COMMANDS ====================
    PARTY_ACCEPT("/partyaccept", "Accept player into party"), // /acceptparty
    PARTY_CREATE("/partycreate", "Create a party"), // /crearparty
    PARTY_JOIN("/partyjoin", "Join or request to join a party"), // /party
    PARTY_KICK("/partykick", "Kick member from party"), // /echarparty
    PARTY_LEAVE("/partyleave", "Leave the party"), // /salirparty
    PARTY_MSG("/partymsg", "Send message to party members"), // /pmsg
    PARTY_ONLINE("/partyonline", "Show online party members"), // /onlineparty
    PARTY_SET_LEADER("/partysetleader", "Transfer party leadership"), // /partylider

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

    // ==================== GM COMMANDS ====================
    SHUTDOWN("/shutdown", "Shutdown the server"), // /apagar
    AUTOUPDATE("/autoupdate", "Reset auto-update system"), // /autoupdate
    BANIPLIST("/baniplist", "Show list of banned IP addresses"), // /baniplist
    BANIPRELOAD("/banipreload", "Reload banned IP list"), // /banipreload
    BLOQ("/bloq", "Toggle tile blocking"), // /bloq
    CLEAR("/clear", "Clear SOS message list"), // /borrar
    CC("/cc", "Show creature spawn list"), // /cc
    CENTINEL("/centinel", "Toggle centinel system"), // /centinelaactivado
    DESTROYITEMS("/destroyitems", "Destroy items"), // /dest
    DOBACKUP("/dobackup", "Create server backup"), // /dobackup
    DESTROYTELEPORT("/destroyteleport", "Destroy teleport at current location"), // /dt
    DUMPSECURITY("/dumpsecurity", "Dump security IP tables"), // /dumpsecurity
    KICKALL("/kickall", "Kick all players from server"), // /echartodospjs
    SAVE("/save", "Save all character data"), // /grabar
    SAVEMAP("/savemap", "Save current map"), // /guardamapa
    ADMINSERVER("/adminserver", "Toggle server for administrators"), // /habilitar
    HIDING("/hiding", "Show hidden players"), // /hiding
    HOME("/home", "Return to home city"), // /hogar
    TIME("/time", "Show current server time"), // /hora
    SHOWIGNORED("/showignored", "Show ignored players list"), // /ignorado
    INVISIBILITY("/invisible", "Toggle invisibility"), // /invisible
    CLEAN("/clean", "Clean world objects"), // /limpiar
    RAIN("/rain", "Toggle rain"), // /lluvia
    REMOVE_ITEM_AREA("/removeitemarea", "Remove items in area"), // /masdest
    REMOVE_NPC_AREA("/removenpcarea", "Remove npcs in area"), // /masskill
    REMOVE_NPC("/removenpc", "Remove npc without respawn"), // /mata
    MOTD_CHANGE("/motdchange", "Change message of the day"), // /motdcambia
    NAVE("/nave", "Toggle navigation mode"), // /nave
    NIGHT("/night", "Change time to night"), // /noche
    SHOW_HIDDEN_PLAYERS("/showhiddenplayers", "Show hidden players"), // /ocultando
    ONLINE_CHAOS("/onlinechaos", "Show online chaos legion members"), // /onlinecaos
    ONLINE_GM("/onlinegm", "Show online game masters"), // /onlinegm
    ONLINE_ROYAL("/onlineroyal", "Show online royal army members"), // /onlinereal
    PANEL_GM("/panelgm", "Open GM panel"), // /panelgm
    PING("/ping", "Check server connection latency"), // /ping
    SHOW_OBJ_MAP("/showobjmap", "Show objets in map"), // /piso
    RESTART("/restart", "Restart the server"), // /reiniciar
    RELOAD_SPELLS("/reloadspells", "Reload spells"), // /reloadhechizos
    RELOAD_NPCS("/reloadnpcs", "Reload NPCs"), // /reloadnpcs
    RELOAD_OBJ("/reloadobj", "Reload object"), // /reloadobj
    RELOAD_SERVER_INI("/reloadserverini", "Reload server INI settings"), // /reloadsini
    CLEAN_NPC_INV("/cleannpcinv", "Clean NPC inventory"), // /resetinv
    REMOVE_NPC_RESPAWN("/removenpcrespawn", "Remove NPC with respawn"), // /rmata
    NPC_FOLLOW("/npcfollow", "Make NPC follow you"), // /seguir
    SHOW_NAME("/showname", "Toggle name display above characters"), // /showname
    TELEPORT_TARGET("/teleporttarget", "Teleport to target location"), // /teleploc
    SEE_WORKERS("/seeworkers", "Show working players"), // /trabajando
    AC("/ac", "Give chaos faction armor"), // /ac
    CREATE_NPC("/createnpc", "Create NPC at current location"), // /acc
    ACEPT_CONSE("/aceptconse", "Accept royal council member"), // /aceptconse
    ACEPT_CONSE_CHAOS("/aceptconsechaos", "Accept chaos council member"), // /aceptconsecaos
    WARNING("/warning", "Send warning to player"), // /advertencia
    AEMAIL("/aemail", "Change player's email address"), // /aemail
    AI("/ai", "Give imperial faction armor"), // /ai
    ANAME("/aname", "Change player's name"), // /aname
    APASS("/apass", "Copy password between characters"), // /apass
    REQUEST_CHAR_GOLD("/chargold", "Show player's gold amount"), // /bal
    BAN("/ban", "Ban a player from server"), // /ban
    BAN_IP("/banip", "Ban IP address or player"), // /banip
    REMOVE_PUNISHMENT("/borrarpena", "Remove player punishment"), // /borrarpena
    REQUEST_CHAR_BANK("/charbank", "Show player's bank contents"), // /bov
    CAOSMSG("/caosmsg", "Send message to chaos legion"), // /caosmsg
    JAIL("/jail", "Send player to jail"), // /carcel
    CHAT_COLOR("/chatcolor", "Change chat text color"), // /chatcolor
    CIUMSG("/ciumsg", "Send message to all citizens"), // /ciumsg
    CREATE_OBJ("/createobject", "Create object at current location"), // /co
    CONDEN("/conden", "Turn player into criminal"), // /conden
    CRIMSG("/crimsg", "Send message to all criminals"), // /crimsg
    CREATE_TELEPORT("/createteleport", "Create teleport at location"), // /ct
    SHOW_LOCATION("/showlocation", "Show player's location"), // /donde
    KICK("/kick", "Kick player from server"), // /echar
    EXECUTE("/execute", "Execute a player"), // /ejecutar
    ESTUPID("/estupid", "Make player unable to speak"), // /estupido
    GMSG("/gmsg", "Send global GM message"), // /gmsg
    PLAYER_INFO("/playerinfo", "Show detailed player information"), // /info
    PLAYER_INV("/playerinv", "Show player's inventory"), // /inv
    IP2NICK("/ip2nick", "Get nickname from IP address"), // /ip2nick
    TELEPORT_TO_PLAYER("/teleporttoplayer", "Teleport to specific player"), // /ira
    TELEPORT_NEAR_TO_PLAYER("/teleportneartoplayer", "Teleport near specified player"), // /ircerca
    KICK_CONSE("/kickconse", "Remove player from council"), // /kickconse
    LAST_IP("/lastip", "Show player's last IP address"), // /lastip
    LAST_EMAIL("/lastemail", "Show player's last email"), // /lastemail
    MOD_PLAYER("/modplayer", "Modify player's character properties"), // /mod
    MOD_MAP("/modmap", "Modify map properties"), // /modmapinfo
    SHOW_MOBS("/showmobs", "Show mobs in specific map"), // /nene
    NICK2IP("/nick2ip", "Get IP address from nickname"), // /nick2ip
    REMOVE_PLAYER_FROM_CHAOS("/nocaos", "Remove player from chaos legion"), // /nocaos
    NO_STUPID("/noestupido", "Restore player's ability to speak"), // /noestupido
    NO_REAL("/noreal", "Remove player from royal army"), // /noreal
    ONLINE_MAP("/onlinemap", "Show players online in specific map"), // /onlinemap
    FORGIVE("/forgive", "Forgive player's crimes"), // /perdon
    PLAY_MUSIC("/playmusic", "Play music"), // /playmusic
    PLAY_SOUND("/playsound", "Play sound"), // /playsound
    RACC("/racc", "Create respawning NPC"), // /racc
    RAJAR("/rajar", "Remove player from all factions"), // /rajar
    REALMSG("/realmsg", "Send message to royal army"), // /realmsg
    REM("/rem", "Add comment to server log"), // /rem
    REVIVIR("/revivir", "Resurrect a player"), // /revivir
    RMSG("/rmsg", "Send server-wide message"), // /rmsg
    SETDESC("/setdesc", "Set player's description"), // /setdesc
    SETINIVAR("/setinivar", "Set server configuration variable"), // /setinivar
    SHOW("/show", "Show server information panels"), // /show
    SILENT("/silent", "Silence a player"), // /silenciar
    SHOW_PLAYER_SKILLS("/showplayerskills", "Show player's skills"), // /skills
    SHOW_PLAYER_SLOT("/showplayerslot", "Check player's inventory slot"), // /slot
    SMSG("/smsg", "Send system message"), // /smsg
    STAT("/stat", "Show player's statistics"), // /stat
    SUM("/sum", "Summon player to your location"), // /sum
    TALKAS("/talkas", "Speak as NPC character"), // /talkas
    TELEPORT("/teleport", "Teleport player to location"), // /telep
    TRIGGER("/trigger", "Set or check map trigger"), // /trigger
    UNBAN("/unban", "Unban a player"), // /unban
    UNBANIP("/unbanip", "Unban an IP address"); // /unbanip

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
