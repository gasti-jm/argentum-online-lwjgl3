package org.aoclient.network.protocol.command.execution;

import org.aoclient.network.protocol.Protocol;
import org.aoclient.network.protocol.command.core.Command;
import org.aoclient.network.protocol.command.core.CommandHandler;
import org.aoclient.network.protocol.command.handlers.gm.*;
import org.aoclient.network.protocol.command.handlers.user.*;
import org.aoclient.network.protocol.command.metadata.CommandCategory;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.aoclient.network.protocol.command.metadata.GameCommand.*;

/**
 * Gestiona el registro y acceso a los comandos disponibles dentro del sistema. Ofrece metodos para definir, organizar y recuperar
 * comandos, permitiendo una administracion centralizada de la logica que implementan.
 * <p>
 * Esta clase tambien se encarga de verificar que no existan duplicados al registrar comandos y organiza los mismos por
 * categorias, facilitando su busqueda y utilizacion.
 * <p>
 * TODO Los nombres de los handlers tienen que coincidir con los nombres de los comandos
 */

public final class CommandRegistry {

    /** Lista de comandos registrados en el sistema con pre-carga en tiempo de compilacion. */
    private static final List<Command> COMMANDS = loadCommands();

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
                        Command::name,
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
                .map(Command::name)
                .sorted()
                .toList();
    }

    /**
     * Obtiene comandos por categoria.
     */
    public static List<Command> getCommandsByCategory(CommandCategory category) {
        return COMMANDS.stream()
                .filter(cmd -> cmd.category() == category)
                .toList();
    }

    /**
     * Obtiene informacion de un comando especifico.
     */
    public static Optional<Command> getCommandInfo(String command) {
        return COMMANDS.stream()
                .filter(cmd -> cmd.name().equals(command))
                .findFirst();
    }

    /**
     * Carga una lista de comandos predefinidos disponibles en el juego.
     * <p>
     * Los comandos se dividen en comandos para usuarios y comandos para gms, en donde cada comando tiene asociado su
     * funcionalidad especifica.
     *
     * @return una lista de objetos de tipo {@code Command}, que representan los comandos del sistema junto con su configuracion y
     * proposito
     */
    private static List<Command> loadCommands() {
        return List.of(

                // ==================== USER COMMANDS ====================
                Command.user(BALANCE, Protocol::requestAccountState),
                Command.user(CLAIM_REWARD, Protocol::reward),
                Command.user(CONSULTATION, Protocol::consultation),
                Command.user(ENLIST, Protocol::enlist),
                Command.user(EST, Protocol::requestStats),
                Command.user(EXIT, Protocol::quit),
                Command.user(GUILD_LEAVE, Protocol::guildLeave),
                Command.user(GUILD_ONLINE, Protocol::guildOnline),
                Command.user(GM, Protocol::GMRequest),
                Command.user(HEAL, Protocol::heal),
                Command.user(INFORMATION, Protocol::information),
                Command.user(LEAVE_FACTION, Protocol::leaveFaction),
                Command.user(MEDITATE, Protocol::meditate),
                Command.user(MOTD, Protocol::requestMOTD),
                Command.user(ONLINE, Protocol::online),
                Command.user(OPEN_VAULT, Protocol::bankStart),
                Command.user(PARTY_CREATE, Protocol::partyCreate),
                Command.user(PARTY_JOIN, Protocol::partyJoin),
                Command.user(PARTY_LEAVE, Protocol::partyLeave),
                Command.user(PARTY_ONLINE, Protocol::partyOnline),
                Command.user(PET_FOLLOW, Protocol::petFollow),
                Command.user(PET_RELEASE, Protocol::releasePet),
                Command.user(PET_STAY, Protocol::petStand),
                Command.user(REST, Protocol::rest),
                Command.user(RESURRECT, Protocol::resucitate),
                Command.user(SHARE_NPC, Protocol::shareNpc),
                Command.user(STOP_SHARING_NPC, Protocol::stopSharingNpc),
                Command.user(TRADE, Protocol::commerceStart),
                Command.user(TRAIN, Protocol::trainList),
                Command.user(UPTIME, Protocol::upTime),
                // Comandos con handler
                Command.user(BET, GambleCommand::new),
                Command.user(BMSG, CouncilMessageCommand::new),
                Command.user(BUG, BugReportCommand::new),
                Command.user(CENTINEL_CODE, CentinelReportCommand::new),
                Command.user(DEPOSIT, BankDepositGoldCommand::new),
                Command.user(DESC, ChangeDescriptionCommand::new),
                Command.user(EXTRACT_GOLD, BankExtractGoldCommand::new),
                Command.user(GUILD_FOUND, GuildFundateCommand::new),
                Command.user(GUILD_MSG, GuildMessageCommand::new),
                Command.user(GUILD_VOTE, GuildVoteCommand::new),
                Command.user(HELP, HelpCommand::new),
                Command.user(PARTY_ACCEPT, PartyAcceptMemberCommand::new),
                Command.user(PARTY_KICK, PartyKickCommand::new),
                Command.user(PARTY_MSG, PartyMessageCommand::new),
                Command.user(PARTY_SET_LEADER, PartySetLeaderCommand::new),
                Command.user(POLL, InquiryCommand::new),
                Command.user(REPORT, DenounceCommand::new),
                Command.user(ROL, RoleMasterRequestCommand::new),

                // ==================== GM COMMANDS ====================
                Command.gm(ADMINSERVER, Protocol::serverOpenToUsersToggle),
                Command.gm(AUTOUPDATE, Protocol::resetAutoUpdate),
                Command.gm(BANIPLIST, Protocol::bannedIPList),
                Command.gm(BANIPRELOAD, Protocol::bannedIPReload),
                Command.gm(BLOQ, Protocol::tileBlockedToggle),
                Command.gm(CC, Protocol::spawnListRequest),
                Command.gm(CENTINEL, Protocol::toggleCentinelActivated),
                Command.gm(CLEAN, Protocol::cleanWorld),
                Command.gm(CLEAN_NPC_INV, Protocol::resetNPCInventory),
                Command.gm(CLEAR, Protocol::cleanSOS),
                Command.gm(DESTROYITEMS, Protocol::destroyItems),
                Command.gm(DESTROYTELEPORT, Protocol::teleportDestroy),
                Command.gm(DOBACKUP, Protocol::doBackup),
                Command.gm(DUMPSECURITY, Protocol::dumpIPTables),
                Command.gm(HIDING, Protocol::hiding),
                Command.gm(HOME, Protocol::home),
                Command.gm(INVISIBILITY, Protocol::invisible),
                Command.gm(KICKALL, Protocol::kickAllChars),
                Command.gm(MOTD_CHANGE, Protocol::changeMOTD),
                Command.gm(NAVE, Protocol::navigateToggle),
                Command.gm(NIGHT, Protocol::night),
                Command.gm(NPC_FOLLOW, Protocol::NPCFollow),
                Command.gm(ONLINE_CHAOS, Protocol::onlineChaosLegion),
                Command.gm(ONLINE_GM, Protocol::onlineGM),
                Command.gm(ONLINE_ROYAL, Protocol::onlineRoyalArmy),
                Command.gm(PANEL_GM, Protocol::GMPanel),
                Command.gm(PING, Protocol::ping),
                Command.gm(RAIN, Protocol::rainToggle),
                Command.gm(RELOAD_NPCS, Protocol::reloadNPCs),
                Command.gm(RELOAD_OBJ, Protocol::reloadObjects),
                Command.gm(RELOAD_SERVER_INI, Protocol::reloadServerIni),
                Command.gm(RELOAD_SPELLS, Protocol::reloadSpells),
                Command.gm(REMOVE_ITEM_AREA, Protocol::destroyAllItemsInArea),
                Command.gm(REMOVE_NPC, Protocol::killNPCNoRespawn),
                Command.gm(REMOVE_NPC_AREA, Protocol::killAllNearbyNPCs),
                Command.gm(REMOVE_NPC_RESPAWN, Protocol::killNPC),
                Command.gm(RESTART, Protocol::restart),
                Command.gm(SAVE, Protocol::saveChars),
                Command.gm(SAVEMAP, Protocol::saveMap),
                Command.gm(SEE_WORKERS, Protocol::working),
                Command.gm(SHOW_HIDDEN_PLAYERS, Protocol::hiding),
                Command.gm(SHOW_NAME, Protocol::showName),
                Command.gm(SHOW_OBJ_MAP, Protocol::itemsInTheFloor),
                Command.gm(SHOWIGNORED, Protocol::ignored),
                Command.gm(SHUTDOWN, Protocol::turnOffServer),
                Command.gm(TELEPORT_TARGET, Protocol::warpMeToTarget),
                Command.gm(TIME, Protocol::serverTime),
                // Comandos con handler
                Command.gm(AC, ChaosArmourCommand::new),
                Command.gm(ACEPT_CONSE, AcceptRoyalCouncilMemberCommand::new),
                Command.gm(ACEPT_CONSE_CHAOS, AcceptChaosCouncilMemberCommand::new),
                Command.gm(AEMAIL, AlterMailCommand::new),
                Command.gm(AI, ImperialArmourCommand::new),
                Command.gm(ANAME, AlterNameCommand::new),
                Command.gm(APASS, AlterPasswordCommand::new),
                Command.gm(BAN, BanCharCommand::new),
                Command.gm(BAN_IP, BanIpCommand::new),
                Command.gm(CAOSMSG, ChaosLegionMessageCommand::new),
                Command.gm(CHAT_COLOR, ChatColorCommand::new),
                Command.gm(CIUMSG, CitizenMessageCommand::new),
                Command.gm(CONDEN, TurnCriminalCommand::new),
                Command.gm(CREATE_NPC, CreateNpcCommand::new),
                Command.gm(CREATE_OBJ, CreateObjectCommand::new),
                Command.gm(CREATE_TELEPORT, TeleportCreateCommand::new),
                Command.gm(CRIMSG, CriminalMessageCommand::new),
                Command.gm(ESTUPID, MakeDumbCommand::new),
                Command.gm(EXECUTE, ExecuteCommand::new),
                Command.gm(FORGIVE, ForgiveCommand::new),
                Command.gm(GMSG, GmMessageCommand::new),
                Command.gm(GUILD_BAN, GuildBanCommand::new),
                Command.gm(GUILD_KICK, RemoveCharFromGuildCommand::new),
                Command.gm(GUILD_MEMBER_LIST, GuildMemberListCommand::new),
                Command.gm(GUILD_MSG_HISTORY, ShowGuildMessagesCommand::new),
                Command.gm(GUILD_ONLINE_SPECIFIC, GuildOnlineMembersCommand::new),
                Command.gm(IP2NICK, IpToNickCommand::new),
                Command.gm(JAIL, JailCommand::new),
                Command.gm(KICK, KickCommand::new),
                Command.gm(KICK_CONSE, CouncilKickCommand::new),
                Command.gm(LAST_EMAIL, RequestCharMailCommand::new),
                Command.gm(LAST_IP, LastIpCommand::new),
                Command.gm(MOD_MAP, MapInfoCommand::new),
                Command.gm(MOD_PLAYER, EditCharCommand::new),
                Command.gm(NICK2IP, NickToIpCommand::new),
                Command.gm(NO_REAL, RoyalArmyKickCommand::new),
                Command.gm(NO_STUPID, MakeDumbNoMoreCommand::new),
                Command.gm(ONLINE_MAP, OnlineMapCommand::new),
                Command.gm(PLAY_MUSIC, PlayMusicCommand::new),
                Command.gm(PLAY_SOUND, PlaySoundCommand::new),
                Command.gm(PLAYER_INFO, RequestCharInfoCommand::new),
                Command.gm(PLAYER_INV, RequestCharInventoryCommand::new),
                Command.gm(RACC, CreateNPCWithRespawnCommand::new),
                Command.gm(RAJAR, ResetFactionsCommand::new),
                Command.gm(REALMSG, RoyaArmyMessageCommand::new),
                Command.gm(REM, CommentCommand::new),
                Command.gm(REMOVE_PLAYER_FROM_CHAOS, ChaosLegionKickCommand::new),
                Command.gm(REMOVE_PUNISHMENT, RemovePunishmentCommand::new),
                Command.gm(REQUEST_CHAR_BANK, RequestCharBankCommand::new),
                Command.gm(REQUEST_CHAR_GOLD, RequestCharGoldCommand::new),
                Command.gm(REVIVIR, ReviveCharCommand::new),
                Command.gm(RMSG, ServerMessageCommand::new),
                Command.gm(SETDESC, SetCharDescriptionCommand::new),
                Command.gm(SETINIVAR, SetIniVarCommand::new),
                Command.gm(SHOW, ShowCommand::new),
                Command.gm(SHOW_LOCATION, WhereCommand::new),
                Command.gm(SHOW_MOBS, CreaturesInMapCommand::new),
                Command.gm(SHOW_PLAYER_SKILLS, RequestCharSkillsCommand::new),
                Command.gm(SHOW_PLAYER_SLOT, CheckSlotCommand::new),
                Command.gm(SILENT, SilenceCommand::new),
                Command.gm(SMSG, SystemMessageCommand::new),
                Command.gm(STAT, RequestCharStatsCommand::new),
                Command.gm(SUM, SummonCharCommand::new),
                Command.gm(TALKAS, TalkAsNpcCommand::new),
                Command.gm(TELEPORT, WarpCharCommand::new),
                Command.gm(TELEPORT_NEAR_TO_PLAYER, GoNearbyCommand::new),
                Command.gm(TELEPORT_TO_PLAYER, GoToCharCommand::new),
                Command.gm(TRIGGER, SetTriggerCommand::new),
                Command.gm(UNBAN, UnbanCharCommand::new),
                Command.gm(UNBANIP, UnbanIpCommand::new),
                Command.gm(WARNING, WarnUserCommand::new)
        );
    }

}
