package org.aoclient.network.protocol.command.metadata;

import java.util.List;

/**
 * Representa un comando disponible en el juego. Cada comando contiene un nombre, una descripcion y una lista de argumentos. Estos
 * comandos pueden ser ejecutados por jugadores o administradores dependiendo de la configuracion.
 * <p>
 * La convencion estandar de los argumentos es: <> = requerido, [] = opcional
 * <p>
 * Junto a cada comando se muestra el comando viejo como comentario.
 */

public enum GameCommand {

    // ==================== USER COMMANDS ====================
    BALANCE("/balance", "Check your account balance", List.of()), // /balance
    BMSG("/bmsg", "Send message to council members", List.of("<message>")), // /bmsg
    BET("/bet", "Gamble gold with gambler NPC", List.of("<amount>")), // /apostar
    BUG("/bug", "Report a bug to administrators", List.of("<description>")), // /bug
    CENTINEL_CODE("/centinelcode", "Submit centinel verification code", List.of("<code>")), // /centinela
    CLAIM_REWARD("/reward", "Claim daily reward", List.of()), // /recompensa
    CLEAR("/clear", "Clean the console", List.of()), // NUEVO COMANDO
    CONSULTATION("/consultation", "Make a consultation request", List.of()), // /consulta
    DEPOSIT("/deposit", "Deposit gold in the bank", List.of("<quantity>")), // /depositar
    DESC("/desc", "Change your character description", List.of("<description>")), // /desc
    ENLIST("/enlist", "Enlist in faction army", List.of()), // /enlistar
    EST("/est", "Show your character statistics", List.of()), // /est
    EXIT("/exit", "Exit the game safely", List.of()), // /salir
    EXTRACT_GOLD("/extractgold", "Withdraw gold from the bank", List.of("<quantity>")), // /retirar
    GM("/gm", "Request help from Game Master", List.of()), // /gm
    GUILD_FOUND("/guildfound", "Found a new guild", List.of("<guild_name>")), // /fundarclan
    GUILD_LEAVE("/guildleave", "Leave the guild", List.of()), // /salirclan
    GUILD_MSG("/guildmsg", "Send message to guild members", List.of("<message>")), // /cmsg
    GUILD_ONLINE("/guildonline", "Show online guild members", List.of()), // /onlineclan
    GUILD_VOTE("/guildvote", "Vote in guild elections", List.of("<nick>")), // /voto
    HEAL("/heal", "Heal other players", List.of()), // /curar
    HELP("/?", "Show available commands", List.of()), // NUEVO COMANDO
    INFORMATION("/information", "Get server information", List.of()), // /informacion
    LEAVE_FACTION("/leavefaction", "Leave current faction", List.of()), // /retirarfaccion
    MEDITATE("/meditate", "Meditate to recover mana", List.of()), // /meditar
    MOTD("/motd", "Show message of the day", List.of()), // /motd
    ONLINE("/online", "Show online players count", List.of()), // /online
    OPEN_VAULT("/openvault", "Open bank interface", List.of()), // /boveda
    PARTY_ACCEPT("/partyaccept", "Accept player into party", List.of("<nick>")), // /acceptparty
    PARTY_CREATE("/partycreate", "Create a party", List.of()), // /crearparty
    PARTY_JOIN("/partyjoin", "Join or request to join a party", List.of()), // /party
    PARTY_KICK("/partykick", "Kick member from party", List.of("<nick>")), // /echarparty
    PARTY_LEAVE("/partyleave", "Leave the party", List.of()), // /salirparty
    PARTY_MSG("/partymsg", "Send message to party members", List.of("<message>")), // /pmsg
    PARTY_ONLINE("/partyonline", "Show online party members", List.of()), // /onlineparty
    PARTY_SET_LEADER("/partysetleader", "Transfer party leadership", List.of("<nick>")), // /partylider
    PET_FOLLOW("/petfollow", "Make your pet follow you", List.of()), // /acompañar
    PET_RELEASE("/release", "Release your pet", List.of()), // /liberar
    PET_STAY("/quieto", "Make your pet stay in place", List.of()), // /quieto
    POLL("/poll", "Participate in server polls", List.of("<option>")), // /encuesta
    REPORT("/report", "Report a player for misconduct", List.of("<nick>", "<reason>")), // /denunciar
    REST("/rest", "Rest to recover stamina", List.of()), // /descansar
    RESURRECT("/resurrect", "Resurrect other players", List.of()), // /resucitar
    ROL("/rol", "Request role master assistance", List.of("<message>")), // /rol
    SHARE_NPC("/sharenpc", "Share NPC with other players", List.of()), // /compartirnpc
    STOP_SHARING_NPC("/stopsharingnpc", "Stop sharing NPC", List.of()), // /nocompartirnpc
    TRADE("/trade", "Start trading with NPC", List.of()), // //comerciar
    TRAIN("/train", "Show available creatures to train", List.of()), // /entrenar
    UPTIME("/uptime", "Show server uptime", List.of()), // /uptime

    // ==================== GM COMMANDS ====================
    AC("/ac", "Give chaos faction armor", List.of("<armor>", "<object>")), // /ac
    ACEPT_CONSE("/aceptconse", "Accept royal council member", List.of("<nick>")), // /aceptconse
    ACEPT_CONSE_CHAOS("/aceptconsechaos", "Accept chaos council member", List.of("<nick>")), // /aceptconsecaos
    ADMINSERVER("/adminserver", "Toggle server for administrators", List.of()), // /habilitar
    AEMAIL("/aemail", "Change player's email address", List.of("<nick>", "<email>")), // /aemail
    AI("/ai", "Give imperial faction armor", List.of("<armor_id>", "<object_id>")), // /ai
    ANAME("/aname", "Change player's name", List.of("<origin>", "<dest>")), // /aname
    APASS("/apass", "Copy password between characters", List.of("<pjsinpass>", "<pjconpass>")), // /apass
    AUTOUPDATE("/autoupdate", "Reset auto-update system", List.of()), // /autoupdate
    BAN("/ban", "Ban a player from server", List.of("<nick>", "<reason>")), // /ban
    BANIPLIST("/baniplist", "Show list of banned IP addresses", List.of()), // /baniplist
    BANIPRELOAD("/banipreload", "Reload banned IP list", List.of()), // /banipreload
    BAN_IP("/banip", "Ban IP address or player", List.of("<ip|nick>", "<reason>")), // /banip
    BLOQ("/bloq", "Toggle tile blocking", List.of()), // /bloq
    CAOSMSG("/caosmsg", "Send message to chaos legion", List.of("<message>")), // /caosmsg
    CC("/cc", "Show creature spawn list", List.of()), // /cc
    CENTINEL("/centinel", "Toggle centinel system", List.of()), // /centinelaactivado
    CHAT_COLOR("/chatcolor", "Change chat text color", List.of("<r>", "<g>", "<b>")), // /chatcolor
    CIUMSG("/ciumsg", "Send message to all citizens", List.of("<message>")), // /ciumsg
    CLEAN("/clean", "Clean world objects", List.of()), // /limpiar // TODO Cambiar nombre
    CLEAN_NPC_INV("/cleannpcinv", "Clean NPC inventory", List.of()), // /resetinv
    CLEAN_SOS("/cleansos", "Clear SOS message list", List.of()), // /borrar
    CONDEN("/conden", "Turn player into criminal", List.of("<nick>")), // /conden
    CREATE_NPC("/createnpc", "Create NPC at current location", List.of("<npc_id>")), // /acc
    CREATE_OBJ("/createobject", "Create object at current location", List.of("<obj_id>")), // /co
    CREATE_TELEPORT("/createteleport", "Create teleport at location", List.of("<map>", "<x>", "<y>", "[radiu]")), // /ct
    CRIMSG("/crimsg", "Send message to all criminals", List.of("<message>")), // /crimsg
    DESTROYITEMS("/destroyitems", "Destroy items", List.of()), // /dest
    DESTROYTELEPORT("/destroyteleport", "Destroy teleport at current location", List.of()), // /dt
    DOBACKUP("/dobackup", "Create server backup", List.of()), // /dobackup
    DUMPSECURITY("/dumpsecurity", "Dump security IP tables", List.of()), // /dumpsecurity 
    ESTUPID("/estupid", "Make player unable to speak", List.of("<nick>")), // /estupido
    EXECUTE("/execute", "Execute a player", List.of("<nick>")), // /ejecutar
    FORGIVE("/forgive", "Forgive player's crimes", List.of("<nick>")), // /perdon
    GMSG("/gmsg", "Send global GM message", List.of("<message>")), // /gmsg
    GUILD_BAN("/guildban", "Ban a guild", List.of("<guild>")), // /banclan
    GUILD_KICK("/guildkick", "Kick member from guild", List.of("<nick>")), // /rajarclan
    GUILD_MEMBER_LIST("/guildmemberlist", "Show guild member list", List.of("<guild>")), // /miembrosclan
    GUILD_MSG_HISTORY("/guildmsghistory", "Show guild message history", List.of()), // /showcmsg
    GUILD_ONLINE_SPECIFIC("/guildonlinespecific", "Show online members of specific guild", List.of("<guild>")), // /onclan
    HIDING("/hiding", "Show hidden players", List.of()), // /hiding
    HOME("/home", "Return to home city", List.of()), // /hogar
    INVISIBILITY("/invisible", "Toggle invisibility", List.of()), // /invisible
    IP2NICK("/ip2nick", "Get nickname from IP address", List.of("<ip>")), // /ip2nick
    JAIL("/jail", "Send player to jail", List.of("<nick>", "<reason>", "<minutes>")), // /carcel
    KICK("/kick", "Kick player from server", List.of("<nick>")), // /echar
    KICKALL("/kickall", "Kick all players from server", List.of()), // /echartodospjs
    KICK_CONSE("/kickconse", "Remove player from council", List.of("<nick>")), // /kickconse
    LAST_EMAIL("/lastemail", "Show player's last email", List.of("<nick>")), // /lastemail
    LAST_IP("/lastip", "Show player's last IP address", List.of("<nick>")), // /lastip
    MOD_MAP("/modmap", "Modify map properties", List.of("<option>", "<value>")), // /modmapinfo
    MOD_PLAYER("/modplayer", "Modify player's character properties", List.of("<nick>", "<property>", "<value>")), // /mod
    MOTD_CHANGE("/motdchange", "Change message of the day", List.of()), // /motdcambia
    NAVE("/nave", "Toggle navigation mode", List.of()), // /nave
    NICK2IP("/nick2ip", "Get IP address from nickname", List.of("<nick>")), // /nick2ip
    NIGHT("/night", "Change time to night", List.of()), // /noche
    NO_REAL("/noreal", "Remove player from royal army", List.of("<nick>")), // /noreal
    NO_STUPID("/noestupido", "Restore player's ability to speak", List.of("<nick>")), // /noestupido
    NPC_FOLLOW("/npcfollow", "Make NPC follow you", List.of()), // /seguir
    ONLINE_CHAOS("/onlinechaos", "Show online chaos legion members", List.of()), // /onlinecaos
    ONLINE_GM("/onlinegm", "Show online game masters", List.of()), // /onlinegm
    ONLINE_MAP("/onlinemap", "Show players online in specific map", List.of("<map>")), // /onlinemap
    ONLINE_ROYAL("/onlineroyal", "Show online royal army members", List.of()), // /onlinereal
    PANEL_GM("/panelgm", "Open GM panel", List.of()), // /panelgm
    PING("/ping", "Check server connection latency", List.of()), // /ping
    PLAY_MUSIC("/playmusic", "Play music", List.of("<music_id>", "[map]")), // /playmusic
    PLAY_SOUND("/playsound", "Play sound", List.of("<sound_id>", "[map]", "[x]", "[y]")), // /playsound
    PLAYER_INFO("/playerinfo", "Show detailed player information", List.of("<nick>")), // /info
    PLAYER_INV("/playerinv", "Show player's inventory", List.of("<nick>")), // /inv
    RACC("/racc", "Create respawning NPC", List.of("<npc_id>")), // /racc
    RAIN("/rain", "Toggle rain", List.of()), // /lluvia
    RAJAR("/rajar", "Remove player from all factions", List.of("<nick>")), // /rajar
    REALMSG("/realmsg", "Send message to royal army", List.of("<message>")), // /realmsg
    RELOAD_NPCS("/reloadnpcs", "Reload NPCs", List.of()), // /reloadnpcs
    RELOAD_OBJ("/reloadobj", "Reload object", List.of()), // /reloadobj
    RELOAD_SERVER_INI("/reloadserverini", "Reload server INI settings", List.of()), // /reloadsini
    RELOAD_SPELLS("/reloadspells", "Reload spells", List.of()), // /reloadhechizos
    REM("/rem", "Add comment to server log", List.of("<comment>")), // /rem
    REMOVE_ITEM_AREA("/removeitemarea", "Remove items in area", List.of()), // /masdest
    REMOVE_NPC("/removenpc", "Remove npc without respawn", List.of()), // /mata
    REMOVE_NPC_AREA("/removenpcarea", "Remove npcs in area", List.of()), // /masskill
    REMOVE_NPC_RESPAWN("/removenpcrespawn", "Remove NPC with respawn", List.of()), // /rmata
    REMOVE_PLAYER_FROM_CHAOS("/nocaos", "Remove player from chaos legion", List.of("<nick>")), // /nocaos
    REMOVE_PUNISHMENT("/borrarpena", "Remove player punishment", List.of("<nick>")), // /borrarpena
    REQUEST_CHAR_BANK("/charbank", "Show player's bank contents", List.of("<nick>")), // /bov
    REQUEST_CHAR_GOLD("/chargold", "Show player's gold amount", List.of("<nick>")), // /bal
    RESTART("/restart", "Restart the server", List.of()), // /reiniciar
    REVIVIR("/revivir", "Resurrect a player", List.of("<nick>")), // /revivir
    RMSG("/rmsg", "Send server-wide message", List.of("<message>")), // /rmsg
    SAVE("/save", "Save all character data", List.of()), // /grabar
    SAVEMAP("/savemap", "Save current map", List.of()), // /guardamapa
    SEE_WORKERS("/seeworkers", "Show working players", List.of()), // /trabajando
    SETDESC("/setdesc", "Set player's description", List.of("<nick>", "<description>")), // /setdesc
    SETINIVAR("/setinivar", "Set server configuration variable", List.of("<llave>", "<clave>", "<valor>")), // /setinivar
    SHOW("/show", "Show server information panels", List.of("<SOS|INT>")), // /show
    SHOW_HIDDEN_PLAYERS("/showhiddenplayers", "Show hidden players", List.of()), // /ocultando
    SHOW_LOCATION("/showlocation", "Show player's location", List.of("<nick>")), // /donde
    SHOW_MOBS("/showmobs", "Show mobs in specific map", List.of("<map>")), // /nene
    SHOW_NAME("/showname", "Toggle name display above characters", List.of()), // /showname
    SHOW_OBJ_MAP("/showobjmap", "Show objets in map", List.of()), // /piso
    SHOW_PLAYER_SKILLS("/showplayerskills", "Show player's skills", List.of("<nick>")), // /skills
    SHOW_PLAYER_SLOT("/showplayerslot", "Check player's inventory slot", List.of("<nick>", "<slot>")), // /slot
    SHOWIGNORED("/showignored", "Show ignored players list", List.of()), // /ignorado
    SHUTDOWN("/shutdown", "Shutdown the server", List.of()), // /apagar
    SILENT("/silent", "Silence a player", List.of("<nick>")), // /silenciar
    SMSG("/smsg", "Send system message", List.of("<message>")), // /smsg
    STAT("/stat", "Show player's statistics", List.of("<nick>")), // /stat
    SUM("/sum", "Summon player to your location", List.of("<nick>")), // /sum
    TALKAS("/talkas", "Speak as NPC character", List.of("<message>")), // /talkas
    TELEPORT("/teleport", "Teleport player to location", List.of("[nick]", "[map]", "<x>", "<y>")), // /telep
    TELEPORT_NEAR_TO_PLAYER("/teleportneartoplayer", "Teleport near specified player", List.of("<nick>")), // /ircerca
    TELEPORT_TARGET("/teleporttarget", "Teleport to target location", List.of()), // /teleploc
    TELEPORT_TO_PLAYER("/teleporttoplayer", "Teleport to specific player", List.of("<nick>")), // /ira
    TIME("/time", "Show current server time", List.of()), // /hora
    TRIGGER("/trigger", "Set or check map trigger", List.of("<trigger>")), // /trigger
    UNBAN("/unban", "Unban a player", List.of("<nick>")), // /unban
    UNBANIP("/unbanip", "Unban an IP address", List.of("<ip>")), // /unbanip
    WARNING("/warning", "Send warning to player", List.of("<nick>", "<reason>")); // /advertencia

    private final String command;
    private final String description;
    private final List<String> arguments;

    GameCommand(String command, String description, List<String> arguments) {
        this.command = command;
        this.description = description;
        this.arguments = List.copyOf(arguments);
    }

    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getArguments() {
        return arguments;
    }

}
