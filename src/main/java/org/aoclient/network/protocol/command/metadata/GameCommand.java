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
    BET("/bet", "Gamble gold with gambler NPC", List.of("<amount>")), // /apostar
    BUG("/bug", "Report a bug to administrators", List.of("<description>")), // /bug
    CLEAR("/clear", "Clean the console", List.of()), // NUEVO COMANDO
    CONSULTATION("/consultation", "Make a consultation request", List.of()), // /consulta
    COUNCIL_MSG("/councilmsg", "Send message to council members", List.of("<message>")), // /bmsg
    DEPOSIT_GOLD("/depositgold", "Deposit gold in the bank", List.of("<amount>")), // /depositar
    DESC("/desc", "Change your character description", List.of("<description>")), // /desc
    ENLIST("/enlist", "Enlist in faction army", List.of()), // /enlistar
    EXTRACT_GOLD("/extractgold", "Extract gold from the bank", List.of("<amount>")), // /retirar
    GM("/gm", "Request help from Game Master", List.of()), // /gm
    GUILD_FOUND("/guildfound", "Found a new guild", List.of("<guild_name>")), // /fundarclan
    GUILD_LEAVE("/guildleave", "Leave the guild", List.of()), // /salirclan
    GUILD_MSG("/guildmsg", "Send message to guild members", List.of("<message>")), // /cmsg
    GUILD_ONLINE("/guildonline", "Show online guild members", List.of()), // /onlineclan
    GUILD_VOTE("/guildvote", "Vote in guild elections", List.of("<player>")), // /voto
    HEAL("/heal", "Heal other players", List.of()), // /curar
    HELP("/?", "Show available commands", List.of()), // NUEVO COMANDO
    INFORMATION("/information", "Get server information", List.of()), // /informacion
    LEAVE_FACTION("/leavefaction", "Leave current faction", List.of()), // /retirarfaccion
    MEDITATE("/meditate", "Meditate to recover mana", List.of()), // /meditar
    MOTD("/motd", "Show message of the day", List.of()), // /motd
    ONLINE("/online", "Show online players count", List.of()), // /online
    OPEN_BANK("/openbank", "Open bank interface", List.of()), // /boveda
    PARTY_ACCEPT("/partyaccept", "Accept player into party", List.of("<player>")), // /acceptparty
    PARTY_CREATE("/partycreate", "Create a party", List.of()), // /crearparty
    PARTY_JOIN("/partyjoin", "Join or request to join a party", List.of()), // /party
    PARTY_KICK("/partykick", "Kick member from party", List.of("<player>")), // /echarparty
    PARTY_LEAVE("/partyleave", "Leave the party", List.of()), // /salirparty
    PARTY_MSG("/partymsg", "Send message to party members", List.of("<message>")), // /pmsg
    PARTY_ONLINE("/partyonline", "Show online party members", List.of()), // /onlineparty
    PARTY_SET_LEADER("/partysetleader", "Transfer party leadership", List.of("<player>")), // /partylider
    PET_FOLLOW("/petfollow", "Make your pet follow you", List.of()), // /acompañar
    PET_RELEASE("/petrelease", "Release your pet", List.of()), // /liberar
    PET_STAY("/petstay", "Make your pet stay in place", List.of()), // /quieto
    POLL("/poll", "Participate in server polls", List.of("<option>")), // /encuesta
    QUIT("/quit", "Quit the game safely", List.of()), // /salir
    REPORT("/report", "Report a player for misconduct", List.of("<player>", "<reason>")), // /denunciar
    REST("/rest", "Rest to recover stamina", List.of()), // /descansar
    RESURRECT("/resurrect", "Resurrect other players", List.of()), // /resucitar
    REWARD("/reward", "Claim daily reward", List.of()), // claim
    ROL("/rol", "Request role master assistance", List.of("<message>")), // /rol
    SENTINEL_CODE("/sentinelcode", "Submit sentinel verification code", List.of("<code>")), // /centinela
    SHARE_NPC("/sharenpc", "Share NPC with other players", List.of()), // /compartirnpc
    STATS("/stats", "Show your character statistics", List.of()), // /est
    STOP_SHARING_NPC("/stopsharingnpc", "Stop sharing NPC", List.of()), // /nocompartirnpc
    TRADE("/trade", "Start trading with NPC", List.of()), // //comerciar
    TRAIN_LIST("/train", "Show available creatures to train", List.of()), // /entrenar
    UPTIME("/uptime", "Show server uptime", List.of()), // /uptime
    // ==================== GM COMMANDS ====================
    ACCEPT_CHAOS_COUNCIL("/acceptchaoscouncil", "Accept chaos council member", List.of("<player>")), // /aceptconsecaos
    ACCEPT_ROYAL_COUNCIL("/acceptroyalcouncil", "Accept royal council member", List.of("<player>")), // /aceptconse
    ADMIN_SERVER("/adminserver", "Toggle server for administrators", List.of()), // /habilitar
    ALTERNATE_PASSWORD("/alternatepassword", "Copy password between players", List.of("<player_without_password>", "<player_with_password>")), // /apass
    BACKUP("/backup", "Create server backup", List.of()), // /dobackup
    BAN("/ban", "Ban a player", List.of("<player>", "<reason>")), // /ban
    BAN_IP("/banip", "Ban IP address or player", List.of("<ip|nick>", "<reason>")), // /banip
    BAN_IP_LIST("/baniplist", "Show list of banned IP addresses", List.of()), // /baniplist
    BAN_IP_RELOAD("/banipreload", "Reload banned IP list", List.of()), // /banipreload
    BLOCK_TILE("/blocktile", "Block the tile of the current position", List.of()), // /bloq
    CHANGE_EMAIL("/changeemail", "Change player's email address", List.of("<player>", "<email>")), // /aemail
    CHANGE_MOTD("/motdchange", "Change message of the day", List.of()), // /motdcambia
    CHANGE_NICK("/changenick", "Change player's name", List.of("<nick>", "<new_nick>")), // /aname
    CHAOS_ARMOUR("/chaosarmour", "Give chaos faction armor", List.of("<armor>", "<object>")), // /ac
    CHAOS_MSG("/chaosmsg", "Send message to chaos legion", List.of("<message>")), // /caosmsg
    CHAT_COLOR("/chatcolor", "Change chat text color", List.of("<r>", "<g>", "<b>")), // /chatcolor
    CITIZEN_MSG("/citizenmsg", "Send message to all citizens", List.of("<message>")), // /ciumsg
    CLEAN_NPC_INV("/cleannpcinv", "Clean NPC inventory", List.of()), // /resetinv
    CLEAN_SOS("/cleansos", "Clear SOS message list", List.of()), // /borrar
    CLEAN_WORLD("/clean", "Clean world objects", List.of()), // /limpiar
    COMMENT_SERVER_LOG("/rem", "Add comment to server log", List.of("<comment>")), // /rem
    CREATE_NPC("/createnpc", "Create NPC at current location", List.of("<npc_id>")), // /acc
    CREATE_NPC_RESPAWN("/createnpcrespawn", "Create respawning NPC", List.of("<npc_id>")), // /racc
    CREATE_OBJ("/createobj", "Create object at current location", List.of("<obj_id>")), // /co
    CREATE_TELEPORT("/createteleport", "Create teleport at location", List.of("<map>", "<x>", "<y>", "[radiu]")), // /ct
    CRIMINAL_MSG("/criminalsmsg", "Send message to all criminals", List.of("<message>")), // /crimsg
    DUMP_SECURITY("/dumpsecurity", "Dump security IP tables", List.of()), // /dumpsecurity
    DUMB("/dumb", "Make player unable to speak", List.of("<player>")), // /estupido
    FORGIVE("/forgive", "Forgive player's crimes", List.of("<player>")), // /perdon
    GM_MSG("/gmmsg", "Send global GM message", List.of("<message>")), // /gmsg
    GM_PANEL("/gmpanel", "Open GM panel", List.of()), // /panelgm
    GUILD_BAN("/guildban", "Ban a guild", List.of("<guild>")), // /banclan
    GUILD_KICK("/guildkick", "Kick member from guild", List.of("<player>")), // /rajarclan
    GUILD_MEMBERS("/guildmembers", "Show guild member list", List.of("<guild>")), // /miembrosclan
    GUILD_MESSAGES("/guildmessages", "Show guild message history", List.of("<guild>")), // /showcmsg
    GUILD_ONLINE_SPECIFIC("/guildonlinespecific", "Show online members of specific guild", List.of("<guild>")), // /onclan
    HOME("/home", "Return to home city", List.of()), // /hogar
    IMPERIAL_ARMOUR("/imperialarmour", "Give imperial faction armor", List.of("<armor_id>", "<object_id>")), // /ai
    INVISIBILITY("/invisibility", "Toggle invisibility", List.of()), // /invisible
    IP_TO_NICK("/iptonick", "Get nick from IP address", List.of("<ip>")), // /ip2nick
    JAIL("/jail", "Send player to jail", List.of("<player>", "<reason>", "<minutes>")), // /carcel
    KICK("/kick", "Kick player from server", List.of("<player>")), // /echar
    KICK_ALL("/kickall", "Kick all players from server", List.of()), // /echartodospjs
    KICK_COUNCIL("/kickcouncil", "Remove player from council", List.of("<player>")), // /kickconse
    KILL("/kill", "Mata a un player", List.of("<player>")), // /ejecutar
    LAST_IP("/lastip", "Show player's last IP address", List.of("<player>")), // /lastip
    MOB_PANEL("/mobpanel", "Open Mob panel", List.of()), // /cc
    MOD_MAP("/modmap", "Modify map properties", List.of("<option>", "<value>")), // /modmapinfo
    MOD_PLAYER("/modplayer", "Modify player properties", List.of("<player>", "<property>", "<value>", "[extra_param]")), // /mod
    NAVIGATION("/navigation", "Toggle navigation", List.of()), // /nave
    NICK_TO_IP("/nicktoip", "Get IP address from nickname", List.of("<nick>")), // /nick2ip
    NIGHT("/night", "Change time to night", List.of()), // /noche
    NO_DUMB("/nodumb", "Restore player's ability to speak", List.of("<player>")), // /noestupido
    NPC_FOLLOW("/npcfollow", "Make NPC follow you", List.of()), // /seguir
    ONLINE_CHAOS("/onlinechaos", "Show online chaos legion members", List.of()), // /onlinecaos
    ONLINE_GM("/onlinegm", "Show online game masters", List.of()), // /onlinegm
    ONLINE_MAP("/onlinemap", "Show players online in specific map", List.of("<map>")), // /onlinemap
    ONLINE_ROYAL("/onlineroyal", "Show online royal army members", List.of()), // /onlinereal
    PING("/ping", "Check server connection latency", List.of()), // /ping
    PLAYER_BANK("/playerbank", "Show player bank", List.of("<player>")), // /bov
    PLAYER_EMAIL("/playeremail", "Show player email", List.of("<player>")), // /lastemail
    PLAYER_GOLD("/playergold", "Show player gold", List.of("<player>")), // /bal
    PLAYER_INFO("/playerinfo", "Show player info", List.of("<player>")), // /info
    PLAYER_INV("/playerinv", "Show player inventory", List.of("<player>")), // /inv
    PLAYER_LOCATION("/playerlocation", "Show player location", List.of("<player>")), // /donde
    PLAYER_SKILLS("/playerskills", "Show player skills", List.of("<player>")), // /skills
    PLAYER_SLOT("/playerslot", "Show player inventory slot", List.of("<player>", "<slot>")), // /slot
    PLAYER_STATS("/playerstats", "Show player stats", List.of("<player>")), // /stat
    PLAY_MUSIC("/playmusic", "Play music", List.of("<music_id>", "[map]")), // /playmusic
    PLAY_SOUND("/playsound", "Play sound", List.of("<sound_id>", "[map]", "[x]", "[y]")), // /playsound
    RAIN("/rain", "Toggle rain", List.of()), // /lluvia
    RELOAD_NPC("/reloadnpc", "Reload npcs", List.of()), // /reloadnpcs
    RELOAD_OBJ("/reloadobj", "Reload objects", List.of()), // /reloadobj
    RELOAD_SERVER_INI("/reloadserverini", "Reload server ini settings", List.of()), // /reloadsini
    RELOAD_SPELL("/reloadspell", "Reload spells", List.of()), // /reloadhechizos
    REMOVE_ARMY("/removearmy", "Remove player from royal army", List.of("<player>")), // /noreal
    REMOVE_CHAOS("/removechaos", "Remove player from chaos legion", List.of("<player>")), // /nocaos
    REMOVE_FACTIONS("/removefactions", "Remove player from all factions", List.of("<player>")), // /rajar
    REMOVE_ITEM("/removeitem", "Remove item in current location", List.of()), // /dest
    REMOVE_ITEM_AREA("/removeitemarea", "Remove items in area", List.of()), // /masdest
    REMOVE_NPC("/removenpc", "Remove npc", List.of()), // /rmata
    REMOVE_NPC_AREA("/removenpcarea", "Remove npcs in area", List.of()), // /masskill
    REMOVE_NPC_NO_RESPAWN("/removenpcnorespawn", "Remove npc without respawn", List.of()), // /mata
    REMOVE_PUNISHMENT("/removepunishment", "Remove player punishment", List.of("<player>")), // /borrarpena
    REMOVE_TELEPORT("/removeteleport", "Remove selected teleport", List.of()), // /dt
    AUTO_UPDATE("/autoupdate", "Reset autoupdate system", List.of()), // /autoupdate
    RESTART("/restart", "Restart the server", List.of()), // /reiniciar
    REVIVE("/revive", "Revive a player", List.of("<player>")), // /revivir
    RMSG("/rmsg", "Send message to all players", List.of("<message>")),
    ROYAL_ARMY_MSG("/royalarmymsg", "Send message to royal army", List.of("<message>")), // /realmsg
    SAVE_CHAR("/savechar", "Save all character data", List.of()), // /grabar
    SAVE_MAP("/savemap", "Save current map", List.of()), // /guardamapa
    SENTINEL("/sentinel", "Toggle sentinel", List.of()), // /centinelaactivado
    SET_DESC("/setdesc", "Set player description", List.of("<player>", "<description>")), // /setdesc
    SETINIVAR("/setinivar", "Set server configuration variable", List.of("<llave>", "<clave>", "<valor>")), // /setinivar
    SHOW("/show", "Show server information panels", List.of("<SOS|INT>")), // /show
    SHOW_HIDDEN("/showhidden", "Show hidden players", List.of()), // /hiding
    SHOW_IGNORED("/showignored", "Show ignored players list", List.of()), // /ignorado
    SHOW_MOBS("/showmobs", "Show mobs in specific map", List.of("<map>")), // /nene
    SHOW_NAME("/showname", "Toggle name display above characters", List.of()), // /showname
    SHOW_OBJ_MAP("/showobjmap", "Show objets in current map", List.of()), // /piso
    SHOW_WORKERS("/showworkers", "Show working players", List.of()), // /trabajando
    SHUTDOWN("/shutdown", "Shutdown server", List.of()), // /apagar
    SILENCE("/silent", "Silence a player", List.of("<player>")), // /silenciar
    SUMMON("/summon", "Summon player to your location", List.of("<player>")), // /sum
    SYSTEM_MESSAGE("/systemmsg", "Send system message", List.of("<message>")), // /smsg
    TALK_AS_NPC("/talkas", "Speak as npc character", List.of("<message>")), // /talkas
    TELEPORT("/teleport", "Teleport player to location", List.of("[nick]", "[map]", "<x>", "<y>")), // /telep
    TELEPORT_NEAR_TO_PLAYER("/teleportneartoplayer", "Teleport near specified player", List.of("<player>")), // /ircerca
    TELEPORT_TO_PLAYER("/teleporttoplayer", "Teleport to player", List.of("<player>")), // /ira
    TELEPORT_TO_TARGET("/teleporttotarget", "Teleport to target", List.of()), // /teleploc
    TIME("/time", "Show current server time", List.of()), // /hora
    TRIGGER("/trigger", "Set or check map trigger", List.of("<trigger>")), // /trigger
    TURN_CRIMINAL("/turncriminal", "Turn player into criminal", List.of("<player>")), // /conden
    UNBAN("/unban", "Unban a player", List.of("<player>")), // /unban
    UNBAN_IP("/unbanip", "Unban an IP address", List.of("<ip>")), // /unbanip
    WARNING("/warning", "Send warning to player", List.of("<player>", "<reason>")); // /advertencia

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
