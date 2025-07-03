package org.aoclient.network.protocol.command;

import org.aoclient.network.protocol.Protocol;
import org.aoclient.network.protocol.command.handlers.basic.*;
import org.aoclient.network.protocol.command.handlers.gm.*;
import org.aoclient.network.protocol.command.handlers.guild.*;
import org.aoclient.network.protocol.command.handlers.party.PartyAcceptMemberCommand;
import org.aoclient.network.protocol.command.handlers.party.PartyKickCommand;
import org.aoclient.network.protocol.command.handlers.party.PartyMessageCommand;
import org.aoclient.network.protocol.command.handlers.party.PartySetLeaderCommand;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Gestiona el registro y acceso a los comandos disponibles dentro del sistema. Ofrece metodos para definir, organizar y recuperar
 * comandos, permitiendo una administracion centralizada de la logica que implementan.
 * <p>
 * Esta clase tambien se encarga de verificar que no existan duplicados al registrar comandos y organiza los mismos por
 * categorias, facilitando su busqueda y utilizacion.
 */

public final class CommandRegistry {

    /** Lista de comandos registrados en el sistema con pre-carga en tiempo de compilacion. */
    private static final List<CommandDefinition> COMMANDS = loadCommands();

    /**
     * Construye un mapa que asocia nombres de comandos con sus respectivos handlers ({@link CommandHandler}). Este mapa permite
     * acceder rapidamente al handler correspondiente para cada nombre de comando.
     * <p>
     * En caso de nombres duplicados entre los comandos registrados, se lanza una excepcion para evitar conflictos en la
     * asociacion.
     *
     * @return un {@code Map<String, CommandHandler>} donde la clave es el nombre del comando y el valor es la instancia del
     * {@link CommandHandler} correspondiente
     * @throws IllegalStateException si se encuentran comandos con nombres duplicados
     */
    public static Map<String, CommandHandler> buildCommandMap() {
        return COMMANDS.stream()
                .collect(Collectors.toMap(
                        CommandDefinition::name,
                        def -> def.factory().get(),
                        (existing, replacement) -> {
                            throw new IllegalStateException("Duplicate command: " + existing);
                        }
                ));
    }

    /**
     * Obtiene todos los comandos disponibles (util para auto-completado).
     */
    public static List<String> getAllCommandNames() {
        return COMMANDS.stream()
                .map(CommandDefinition::name)
                .sorted()
                .toList();
    }

    /**
     * Obtiene comandos por categoria.
     */
    public static List<CommandDefinition> getCommandsByCategory(CommandCategory category) {
        return COMMANDS.stream()
                .filter(cmd -> cmd.category() == category)
                .toList();
    }

    /**
     * Obtiene informacion de un comando especifico.
     */
    public static Optional<CommandDefinition> getCommandInfo(String command) {
        return COMMANDS.stream()
                .filter(cmd -> cmd.name().equals(command))
                .findFirst();
    }

    /**
     * Carga una lista de comandos predefinidos disponibles en el juego. Estos comandos cubren una variedad de acciones, desde
     * funciones basicas hasta interacciones mas complejas como participar en gremios, grupos (parties) y administracion de
     * servidores para Game Masters. Los comandos estan divididos en categorias segun su funcionalidad.
     *
     * @return una lista de instancias de {@link CommandDefinition} que representan todos los comandos disponibles y sus
     * configuraciones correspondientes
     */
    private static List<CommandDefinition> loadCommands() {
        return List.of(

                // ==================== BASIC COMMANDS ====================

                // Comandos basicos sin argumentos
                CommandDefinition.simple("/acompañar", Protocol::writePetFollow, "Make your pet follow you"),
                CommandDefinition.simple("/ayuda", Protocol::writeHelp, "Show available commands and help"),
                CommandDefinition.simple("/balance", Protocol::writeRequestAccountState, "Check your account balance"),
                CommandDefinition.simple("/boveda", Protocol::writeBankStart, "Open bank interface"),
                CommandDefinition.simple("/comerciar", Protocol::writeCommerceStart, "Start trading with NPC"),
                CommandDefinition.simple("/compartirnpc", Protocol::writeShareNpc, "Share NPC with other players"),
                CommandDefinition.simple("/consulta", Protocol::writeConsultation, "Make a consultation request"),
                CommandDefinition.simple("/curar", Protocol::writeHeal, "Heal other players"),
                CommandDefinition.simple("/descansar", Protocol::writeRest, "Rest to recover stamina"),
                CommandDefinition.simple("/enlistar", Protocol::writeEnlist, "Enlist in faction army"),
                CommandDefinition.simple("/entrenar", Protocol::writeTrainList, "Show available creatures to train"),
                CommandDefinition.simple("/est", Protocol::writeRequestStats, "Show your character statistics"),
                CommandDefinition.simple("/gm", Protocol::writeGMRequest, "Request help from Game Master"),
                CommandDefinition.simple("/informacion", Protocol::writeInformation, "Get server information"),
                CommandDefinition.simple("/liberar", Protocol::writeReleasePet, "Release your pet"),
                CommandDefinition.simple("/meditar", Protocol::writeMeditate, "Meditate to recover mana"),
                CommandDefinition.simple("/motd", Protocol::writeRequestMOTD, "Show message of the day"),
                CommandDefinition.simple("/nocompartirnpc", Protocol::writeStopSharingNpc, "Stop sharing NPC"),
                CommandDefinition.simple("/online", Protocol::writeOnline, "Show online players count"),
                CommandDefinition.simple("/quieto", Protocol::writePetStand, "Make your pet stay in place"),
                CommandDefinition.simple("/recompensa", Protocol::writeReward, "Claim daily reward"),
                CommandDefinition.simple("/resucitar", Protocol::writeResucitate, "Resurrect other players"),
                CommandDefinition.simple("/retirarfaccion", Protocol::writeLeaveFaction, "Leave current faction"),
                CommandDefinition.simple("/salir", Protocol::writeQuit, "Exit the game safely"),
                CommandDefinition.simple("/uptime", Protocol::writeUpTime, "Show server uptime"),

                // Comandos basicos con argumentos/validacion
                CommandDefinition.basic("/apostar", GambleCommand::new, "Gamble gold with gambler NPC"),
                CommandDefinition.basic("/bmsg", CouncilMessageCommand::new, "Send message to council members"),
                CommandDefinition.basic("/bug", BugReportCommand::new, "Report a bug to administrators"),
                CommandDefinition.basic("/centinela", CentinelReportCommand::new, "Submit centinel verification code"),
                CommandDefinition.basic("/denunciar", DenounceCommand::new, "Report a player for misconduct"),
                CommandDefinition.basic("/depositar", BankDepositGoldCommand::new, "Deposit gold in the bank"),
                CommandDefinition.basic("/desc", ChangeDescriptionCommand::new, "Change your character description"),
                CommandDefinition.basic("/encuesta", InquiryCommand::new, "Participate in server polls"),
                CommandDefinition.basic("/retirar", BankExtractGoldCommand::new, "Withdraw gold from the bank"),
                CommandDefinition.basic("/rol", RoleMasterRequestCommand::new, "Request role master assistance"),

                // ==================== PARTY COMMANDS ====================

                // Comandos de party sin argumentos
                CommandDefinition.simple("/crearparty", Protocol::writePartyCreate, "Create a new party"),
                CommandDefinition.simple("/onlineparty", Protocol::writePartyOnline, "Show online party members"),
                CommandDefinition.simple("/party", Protocol::writePartyJoin, "Join or request to join a party"),
                CommandDefinition.simple("/salirparty", Protocol::writePartyLeave, "Leave current party"),

                // Comandos de party con argumentos/validacion
                CommandDefinition.party("/acceptparty", PartyAcceptMemberCommand::new, "Accept player into party"),
                CommandDefinition.party("/echarparty", PartyKickCommand::new, "Kick member from party"),
                CommandDefinition.party("/partylider", PartySetLeaderCommand::new, "Transfer party leadership"),
                CommandDefinition.party("/pmsg", PartyMessageCommand::new, "Send message to party members"),

                // ==================== GUILD COMMANDS ====================

                // Comandos de guild sin argumentos
                CommandDefinition.simple("/onlineclan", Protocol::writeGuildOnline, "Show online guild members"),
                CommandDefinition.simple("/salirclan", Protocol::writeGuildLeave, "Leave current guild"),

                // Comandos de guild con argumentos/validacion
                CommandDefinition.guild("/banclan", GuildBanCommand::new, "Ban a guild from server"),
                CommandDefinition.guild("/cmsg", GuildMessageCommand::new, "Send message to guild members"),
                CommandDefinition.guild("/fundarclan", GuildFundateCommand::new, "Found a new guild"),
                CommandDefinition.guild("/miembrosclan", GuildMemberListCommand::new, "Show guild member list"),
                CommandDefinition.guild("/onclan", GuildOnlineMembersCommand::new, "Show online members of specific guild"),
                CommandDefinition.guild("/rajarclan", RemoveCharFromGuildCommand::new, "Remove character from guild"),
                CommandDefinition.guild("/showcmsg", ShowGuildMessagesCommand::new, "Show guild message history"),
                CommandDefinition.guild("/voto", GuildVoteCommand::new, "Vote in guild elections"),

                // ==================== GM COMMANDS ====================

                // Comandos de GM sin argumentos
                CommandDefinition.simple("/apagar", Protocol::writeTurnOffServer, "Shutdown the server"),
                CommandDefinition.simple("/autoupdate", Protocol::writeResetAutoUpdate, "Reset auto-update system"),
                CommandDefinition.simple("/baniplist", Protocol::writeBannedIPList, "Show list of banned IP addresses"),
                CommandDefinition.simple("/banipreload", Protocol::writeBannedIPReload, "Reload banned IP list"),
                CommandDefinition.simple("/bloq", Protocol::writeTileBlockedToggle, "Toggle tile blocking"),
                CommandDefinition.simple("/borrar", Protocol::writeCleanSOS, "Clear SOS message list"),
                CommandDefinition.simple("/cc", Protocol::writeSpawnListRequest, "Show creature spawn list"),
                CommandDefinition.simple("/centinelaactivado", Protocol::writeToggleCentinelActivated, "Toggle centinel system"),
                CommandDefinition.simple("/dest", Protocol::writeDestroyItems, "Destroy items in area"),
                CommandDefinition.simple("/dobackup", Protocol::writeDoBackup, "Create server backup"),
                CommandDefinition.simple("/dt", Protocol::writeTeleportDestroy, "Destroy teleport at current location"),
                CommandDefinition.simple("/dumpsecurity", Protocol::writeDumpIPTables, "Dump security IP tables"),
                CommandDefinition.simple("/echartodospjs", Protocol::writeKickAllChars, "Kick all players from server"),
                CommandDefinition.simple("/grabar", Protocol::writeSaveChars, "Save all character data"),
                CommandDefinition.simple("/guardamapa", Protocol::writeSaveMap, "Save current map"),
                CommandDefinition.simple("/habilitar", Protocol::writeServerOpenToUsersToggle, "Toggle server access for users"),
                CommandDefinition.simple("/hiding", Protocol::writeHiding, "Show hidden players"),
                CommandDefinition.simple("/hogar", Protocol::writeHome, "Return to home city"),
                CommandDefinition.simple("/hora", Protocol::writeServerTime, "Show current server time"),
                CommandDefinition.simple("/ignorado", Protocol::writeIgnored, "Show ignored players list"),
                CommandDefinition.simple("/invisible", Protocol::writeInvisible, "Toggle GM invisibility"),
                CommandDefinition.simple("/limpiar", Protocol::writeCleanWorld, "Clean world objects"),
                CommandDefinition.simple("/lluvia", Protocol::writeRainToggle, "Toggle rain weather"),
                CommandDefinition.simple("/masdest", Protocol::writeDestroyAllItemsInArea, "Destroy all items in area"),
                CommandDefinition.simple("/masskill", Protocol::writeKillAllNearbyNPCs, "Kill all nearby NPCs"),
                CommandDefinition.simple("/mata", Protocol::writeKillNPCNoRespawn, "Kill NPC without respawn"),
                CommandDefinition.simple("/motdcambia", Protocol::writeChangeMOTD, "Change message of the day"),
                CommandDefinition.simple("/nave", Protocol::writeNavigateToggle, "Toggle navigation mode"),
                CommandDefinition.simple("/noche", Protocol::writeNight, "Change time to night"),
                CommandDefinition.simple("/ocultando", Protocol::writeHiding, "Show hidden players"),
                CommandDefinition.simple("/onlinecaos", Protocol::writeOnlineChaosLegion, "Show online chaos legion members"),
                CommandDefinition.simple("/onlinegm", Protocol::writeOnlineGM, "Show online game masters"),
                CommandDefinition.simple("/onlinereal", Protocol::writeOnlineRoyalArmy, "Show online royal army members"),
                CommandDefinition.simple("/panelgm", Protocol::writeGMPanel, "Open GM control panel"),
                CommandDefinition.simple("/ping", Protocol::writePing, "Check server connection latency"),
                CommandDefinition.simple("/piso", Protocol::writeItemsInTheFloor, "Show items on ground"),
                CommandDefinition.simple("/reiniciar", Protocol::writeRestart, "Restart the server"),
                CommandDefinition.simple("/reloadhechizos", Protocol::writeReloadSpells, "Reload spell configurations"),
                CommandDefinition.simple("/reloadnpcs", Protocol::writeReloadNPCs, "Reload NPC configurations"),
                CommandDefinition.simple("/reloadobj", Protocol::writeReloadObjects, "Reload object configurations"),
                CommandDefinition.simple("/reloadsini", Protocol::writeReloadServerIni, "Reload server INI settings"),
                CommandDefinition.simple("/resetinv", Protocol::writeResetNPCInventory, "Reset NPC inventory"),
                CommandDefinition.simple("/rmata", Protocol::writeKillNPC, "Kill targeted NPC"),
                CommandDefinition.simple("/seguir", Protocol::writeNPCFollow, "Make NPC follow you"),
                CommandDefinition.simple("/showname", Protocol::writeShowName, "Toggle name display above characters"),
                CommandDefinition.simple("/teleploc", Protocol::writeWarpMeToTarget, "Teleport to target location"),
                CommandDefinition.simple("/trabajando", Protocol::writeWorking, "Show working players"),

                // Comandos de GM con argumentos/validacion
                CommandDefinition.gm("/ac", ChaosArmourCommand::new, "Give chaos faction armor"),
                CommandDefinition.gm("/acc", CreateNpcCommand::new, "Create NPC at current location"),
                CommandDefinition.gm("/aceptconse", AcceptRoyalCouncilMemberCommand::new, "Accept royal council member"),
                CommandDefinition.gm("/aceptconsecaos", AcceptChaosCouncilMemberCommand::new, "Accept chaos council member"),
                CommandDefinition.gm("/advertencia", WarnUserCommand::new, "Send warning to player"),
                CommandDefinition.gm("/aemail", AlterMailCommand::new, "Change player's email address"),
                CommandDefinition.gm("/ai", ImperialArmourCommand::new, "Give imperial faction armor"),
                CommandDefinition.gm("/aname", AlterNameCommand::new, "Change player's name"),
                CommandDefinition.gm("/apass", AlterPasswordCommand::new, "Copy password between characters"),
                CommandDefinition.gm("/bal", RequestCharGoldCommand::new, "Show player's gold amount"),
                CommandDefinition.gm("/ban", BanCharCommand::new, "Ban a player from server"),
                CommandDefinition.gm("/banip", BanIpCommand::new, "Ban IP address or player"),
                CommandDefinition.gm("/borrarpena", RemovePunishmentCommand::new, "Remove player punishment"),
                CommandDefinition.gm("/bov", RequestCharBankCommand::new, "Show player's bank contents"),
                CommandDefinition.gm("/caosmsg", ChaosLegionMessageCommand::new, "Send message to chaos legion"),
                CommandDefinition.gm("/carcel", JailCommand::new, "Send player to jail"),
                CommandDefinition.gm("/chatcolor", ChatColorCommand::new, "Change chat text color"),
                CommandDefinition.gm("/ciumsg", CitizenMessageCommand::new, "Send message to all citizens"),
                CommandDefinition.gm("/co", CreateObjectCommand::new, "Create object at current location"),
                CommandDefinition.gm("/conden", TurnCriminalCommand::new, "Turn player into criminal"),
                CommandDefinition.gm("/crimsg", CriminalMessageCommand::new, "Send message to all criminals"),
                CommandDefinition.gm("/ct", TeleportCreateCommand::new, "Create teleport at location"),
                CommandDefinition.gm("/donde", WhereCommand::new, "Show player's location"),
                CommandDefinition.gm("/echar", KickCommand::new, "Kick player from server"),
                CommandDefinition.gm("/ejecutar", ExecuteCommand::new, "Execute a player"),
                CommandDefinition.gm("/estupido", MakeDumbCommand::new, "Make player unable to speak"),
                CommandDefinition.gm("/gmsg", GmMessageCommand::new, "Send global GM message"),
                CommandDefinition.gm("/info", RequestCharInfoCommand::new, "Show detailed player information"),
                CommandDefinition.gm("/inv", RequestCharInventoryCommand::new, "Show player's inventory"),
                CommandDefinition.gm("/ip2nick", IpToNickCommand::new, "Get nickname from IP address"),
                CommandDefinition.gm("/ira", GoToCharCommand::new, "Teleport to specific player"),
                CommandDefinition.gm("/ircerca", GoNearbyCommand::new, "Teleport near specified player"),
                CommandDefinition.gm("/kickconse", CouncilKickCommand::new, "Remove player from council"),
                CommandDefinition.gm("/lastip", LastIpCommand::new, "Show player's last IP address"),
                CommandDefinition.gm("/lastemail", RequestCharMailCommand::new, "Show player's last email"),
                CommandDefinition.gm("/mod", EditCharCommand::new, "Modify player's character properties"),
                CommandDefinition.gm("/modmapinfo", MapInfoCommand::new, "Modify map properties"),
                CommandDefinition.gm("/nene", CreaturesInMapCommand::new, "Show creatures in specific map"),
                CommandDefinition.gm("/nick2ip", NickToIpCommand::new, "Get IP address from nickname"),
                CommandDefinition.gm("/nocaos", ChaosLegionKickCommand::new, "Remove player from chaos legion"),
                CommandDefinition.gm("/noestupido", MakeDumbNoMoreCommand::new, "Restore player's ability to speak"),
                CommandDefinition.gm("/noreal", RoyalArmyKickCommand::new, "Remove player from royal army"),
                CommandDefinition.gm("/onlinemap", OnlineMapCommand::new, "Show players online in specific map"),
                CommandDefinition.gm("/perdon", ForgiveCommand::new, "Forgive player's crimes"),
                CommandDefinition.gm("/playmusic", PlayMusicCommand::new, "Play music on server or map"),
                CommandDefinition.gm("/playsound", PlaySoundCommand::new, "Play sound effect"),
                CommandDefinition.gm("/racc", CreateNPCWithRespawnCommand::new, "Create respawning NPC"),
                CommandDefinition.gm("/rajar", ResetFactionsCommand::new, "Remove player from all factions"),
                CommandDefinition.gm("/realmsg", RoyaleArmyMessageCommand::new, "Send message to royal army"),
                CommandDefinition.gm("/rem", CommentCommand::new, "Add comment to server log"),
                CommandDefinition.gm("/revivir", ReviveCharCommand::new, "Resurrect a player"),
                CommandDefinition.gm("/rmsg", ServerMessageCommand::new, "Send server-wide message"),
                CommandDefinition.gm("/setdesc", SetCharDescriptionCommand::new, "Set player's description"),
                CommandDefinition.gm("/setinivar", SetIniVarCommand::new, "Set server configuration variable"),
                CommandDefinition.gm("/show", ShowCommand::new, "Show server information panels"),
                CommandDefinition.gm("/silenciar", SilenceCommand::new, "Silence a player"),
                CommandDefinition.gm("/skills", RequestCharSkillsCommand::new, "Show player's skills"),
                CommandDefinition.gm("/slot", CheckSlotCommand::new, "Check player's inventory slot"),
                CommandDefinition.gm("/smsg", SystemMessageCommand::new, "Send system message"),
                CommandDefinition.gm("/stat", RequestCharStatsCommand::new, "Show player's statistics"),
                CommandDefinition.gm("/sum", SummonCharCommand::new, "Summon player to your location"),
                CommandDefinition.gm("/talkas", TalkAsNpcCommand::new, "Speak as NPC character"),
                CommandDefinition.gm("/telep", WarpCharCommand::new, "Teleport player to location"),
                CommandDefinition.gm("/trigger", SetTriggerCommand::new, "Set or check map trigger"),
                CommandDefinition.gm("/unban", UnbanCharCommand::new, "Unban a player"),
                CommandDefinition.gm("/unbanip", UnbanIpCommand::new, "Unban an IP address")
        );
    }

}
