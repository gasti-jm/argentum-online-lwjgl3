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
 * Gestiona el registro y acceso a los comandos disponibles.
 * <p>
 * Los comandos permiten realizar diferentes acciones y acceder a funciones especificas dentro del sistema. Cada comando esta
 * configurado con su correspondiente manejador que define su comportamiento.
 * <p>
 * Los comandos estan organizados de la siguiente manera:
 * <ul>
 * <li><b>Comandos de usuario</b>: Estos comandos son accesibles para jugadores regulares y proporcionan acciones como consultar
 * el estado de la cuenta, unirse a eventos, interactuar con NPCs, manejar mascotas, acceder a cofres bancarios, entre otros.
 * <li><b>Comandos de Game Master (GM)</b>: Estos comandos son exclusivos para administradores del juego. Permiten realizar tareas
 * de mantenimiento, control y gestion del mundo del juego, como expulsar jugadores, realizar modificaciones en los mapas, manejar
 * NPCs, habilitar o deshabilitar caracteristicas globales del juego, y mas.
 * </ul>
 * <p>
 * TODO Los nombres de los handlers tienen que coincidir con los nombres de los comandos
 */

public final class CommandRegistry {

    /**
     * Lista inmutable de todos los comandos disponibles en el sistema, dividida en comandos para usuarios y comandos exclusivos
     * para Game Masters (GMs).
     * <p>
     * La lista incluye tanto comandos directos (que utilizan metodos predefinidos del protocolo) como comandos gestionados por
     * handlers personalizados que encapsulan logicas mas complejas.
     */
    private static final List<Command> commands = List.of(

            // ==================== USER COMMANDS ====================
            Command.user(BALANCE, Protocol::balance),
            Command.user(CONSULTATION, Protocol::consultation),
            Command.user(ENLIST, Protocol::enlist),
            Command.user(GM, Protocol::GM),
            Command.user(GUILD_LEAVE, Protocol::guildLeave),
            Command.user(GUILD_ONLINE, Protocol::guildOnline),
            Command.user(HEAL, Protocol::heal),
            Command.user(INFORMATION, Protocol::information),
            Command.user(LEAVE_FACTION, Protocol::leaveFaction),
            Command.user(MEDITATE, Protocol::meditate),
            Command.user(MOTD, Protocol::MOTD),
            Command.user(ONLINE, Protocol::online),
            Command.user(OPEN_BANK, Protocol::openBank),
            Command.user(PARTY_CREATE, Protocol::partyCreate),
            Command.user(PARTY_JOIN, Protocol::partyJoin),
            Command.user(PARTY_LEAVE, Protocol::partyLeave),
            Command.user(PARTY_ONLINE, Protocol::partyOnline),
            Command.user(PET_FOLLOW, Protocol::petFollow),
            Command.user(PET_RELEASE, Protocol::petRelease),
            Command.user(PET_STAY, Protocol::petStay),
            Command.user(QUIT, Protocol::quit),
            Command.user(REST, Protocol::rest),
            Command.user(RESURRECT, Protocol::resurrect),
            Command.user(REWARD, Protocol::reward),
            Command.user(SHARE_NPC, Protocol::shareNpc),
            Command.user(STATS, Protocol::stats),
            Command.user(STOP_SHARING_NPC, Protocol::stopSharingNpc),
            Command.user(TRADE, Protocol::trade),
            Command.user(TRAIN_LIST, Protocol::trainList),
            Command.user(UPTIME, Protocol::uptime),
            // Comandos con handler
            Command.user(BET, BetCommand::new),
            Command.user(BUG, BugCommand::new),
            Command.user(CLEAR, ClearCommand::new),
            Command.user(COUNCIL_MSG, CouncilMessageCommand::new),
            Command.user(DEPOSIT_GOLD, DepositGoldCommand::new),
            Command.user(DESC, DescCommand::new),
            Command.user(EXTRACT_GOLD, ExtractGoldCommand::new),
            Command.user(GUILD_FOUND, GuildFoundCommand::new),
            Command.user(GUILD_MSG, GuildMessageCommand::new),
            Command.user(GUILD_VOTE, GuildVoteCommand::new),
            Command.user(HELP, HelpCommand::new),
            Command.user(PARTY_ACCEPT, PartyAcceptCommand::new),
            Command.user(PARTY_KICK, PartyKickCommand::new),
            Command.user(PARTY_MSG, PartyMessageCommand::new),
            Command.user(PARTY_SET_LEADER, PartySetLeaderCommand::new),
            Command.user(POLL, PollCommand::new),
            Command.user(REPORT, ReportCommand::new),
            Command.user(ROL, RolCommand::new),
            Command.user(SENTINEL_CODE, SentinelCodeCommand::new), // FIXME Rompe el cliente

            // ==================== GM COMMANDS ====================
            Command.gm(ADMIN_SERVER, Protocol::adminServer),
            Command.gm(AUTO_UPDATE, Protocol::autoUpdate), // FIXME Rompe el cliente
            Command.gm(BACKUP, Protocol::backup), // FIXME Rompe el cliente
            Command.gm(BAN_IP_LIST, Protocol::banIpList),
            Command.gm(BAN_IP_RELOAD, Protocol::banIpReload),
            Command.gm(BLOCK_TILE, Protocol::blockTile),
            Command.gm(CHANGE_MOTD, Protocol::changeMOTD),
            Command.gm(CLEAN_NPC_INV, Protocol::cleanNpcInventory),
            Command.gm(CLEAN_SOS, Protocol::cleanSOS),
            Command.gm(CLEAN_WORLD, Protocol::cleanWorld), // FIXME No hace nada 
            Command.gm(DUMP_SECURITY, Protocol::dumpSecurity), // FIXME No hace nada
            Command.gm(GM_PANEL, Protocol::GMPanel),
            Command.gm(HOME, Protocol::home),
            Command.gm(INVISIBILITY, Protocol::invisibility),
            Command.gm(KICK_ALL, Protocol::kickAll),
            Command.gm(MOB_PANEL, Protocol::mobPanel),
            Command.gm(NAVIGATION, Protocol::navigation),
            Command.gm(NIGHT, Protocol::night),
            Command.gm(NPC_FOLLOW, Protocol::NpcFollow),
            Command.gm(ONLINE_CHAOS, Protocol::onlineChaos),
            Command.gm(ONLINE_GM, Protocol::onlineGM),
            Command.gm(ONLINE_ROYAL, Protocol::onlineRoyal),
            Command.gm(PING, Protocol::ping),
            Command.gm(RAIN, Protocol::rain),
            Command.gm(RELOAD_NPC, Protocol::reloadNPC),
            Command.gm(RELOAD_OBJ, Protocol::reloadObj),
            Command.gm(RELOAD_SERVER_INI, Protocol::reloadServerIni),
            Command.gm(RELOAD_SPELL, Protocol::reloadSpell),
            Command.gm(REMOVE_ITEM, Protocol::removeItem),
            Command.gm(REMOVE_ITEM_AREA, Protocol::removeItemArea),
            Command.gm(REMOVE_NPC, Protocol::removeNpc),
            Command.gm(REMOVE_NPC_AREA, Protocol::removeNpcArea),
            Command.gm(REMOVE_NPC_NO_RESPAWN, Protocol::removeNpcNoRespawn),
            Command.gm(REMOVE_TELEPORT, Protocol::removeTeleport),
            Command.gm(RESTART, Protocol::restart), // FIXME Los mobs me atacan al reinciar
            Command.gm(SAVE_CHAR, Protocol::saveChar),
            Command.gm(SAVE_MAP, Protocol::saveMap),
            Command.gm(SENTINEL, Protocol::sentinel),
            Command.gm(SHOW_HIDDEN, Protocol::showHidden),
            Command.gm(SHOW_IGNORED, Protocol::showIgnored), // FIXME Rompe el cliente
            Command.gm(SHOW_NAME, Protocol::showName),
            Command.gm(SHOW_OBJ_MAP, Protocol::showObjMap), // FIXME Rompe el cliente
            Command.gm(SHOW_WORKERS, Protocol::showWorkers),
            Command.gm(SHUTDOWN, Protocol::shutdown),
            Command.gm(TELEPORT_TO_TARGET, Protocol::teleportToTarget),
            Command.gm(TIME, Protocol::time),
            // Comandos con handler
            Command.gm(ACCEPT_CHAOS_COUNCIL, AcceptChaosCouncilCommand::new),
            Command.gm(ACCEPT_ROYAL_COUNCIL, AcceptRoyalCouncilCommand::new),
            Command.gm(ALTERNATE_PASSWORD, AlternatePasswordCommand::new),
            Command.gm(BAN, BanCommand::new),
            Command.gm(BAN_IP, BanIpCommand::new),
            Command.gm(CHANGE_EMAIL, ChangeEmailCommand::new),
            Command.gm(CHANGE_NICK, ChangeNickCommand::new),
            Command.gm(CHAOS_ARMOUR, ChaosArmourCommand::new),
            Command.gm(CHAOS_MSG, ChaosMessageCommand::new),
            Command.gm(CHAT_COLOR, ChatColorCommand::new), // FIXME Rompe el cliente
            Command.gm(CITIZEN_MSG, CitizenMessageCommand::new),
            Command.gm(COMMENT_SERVER_LOG, CommentServerLogCommand::new),
            Command.gm(CREATE_NPC, CreateNpcCommand::new),
            Command.gm(CREATE_NPC_RESPAWN, CreateNpcRespawnCommand::new),
            Command.gm(CREATE_OBJ, CreateObjCommand::new),
            Command.gm(CREATE_TELEPORT, CreateTeleportCommand::new),
            Command.gm(CRIMINAL_MSG, CriminalMessageCommand::new),
            Command.gm(DUMB, DumbCommand::new), // TODO Que hace?
            Command.gm(FORGIVE, ForgiveCommand::new),
            Command.gm(GM_MSG, GmMessageCommand::new),
            Command.gm(GUILD_BAN, GuildBanCommand::new),
            Command.gm(GUILD_KICK, GuildKickCommand::new),
            Command.gm(GUILD_MEMBERS, GuildMembersCommand::new),
            Command.gm(GUILD_MESSAGES, GuildMessagesCommand::new),
            Command.gm(GUILD_ONLINE_SPECIFIC, GuildOnlineSpecificCommand::new),
            Command.gm(IMPERIAL_ARMOUR, ImperialArmourCommand::new),
            Command.gm(IP_TO_NICK, IpToNickCommand::new),
            Command.gm(JAIL, JailCommand::new),
            Command.gm(KICK, KickCommand::new),
            Command.gm(KICK_COUNCIL, KickCouncilCommand::new),
            Command.gm(KILL, KillCommand::new),
            Command.gm(LAST_IP, LastIpCommand::new),
            Command.gm(MOD_MAP, ModMapCommand::new),
            Command.gm(MOD_PLAYER, ModPlayerCommand::new),
            Command.gm(NICK_TO_IP, NickToIpCommand::new),
            Command.gm(NO_DUMB, NoDumbCommand::new),
            Command.gm(ONLINE_MAP, OnlineMapCommand::new),
            Command.gm(PLAYER_BANK, PlayerBankCommand::new),
            Command.gm(PLAYER_EMAIL, PlayerEmailCommand::new),
            Command.gm(PLAYER_GOLD, PlayerGoldCommand::new),
            Command.gm(PLAYER_INFO, PlayerInfoCommand::new),
            Command.gm(PLAYER_INV, PlayerInvCommand::new),
            Command.gm(PLAYER_LOCATION, PlayerLocationCommand::new),
            Command.gm(PLAYER_SKILLS, PlayerSkillsCommand::new),
            Command.gm(PLAYER_SLOT, PlayerSlotCommand::new), // FIXME Se rompe el cliente
            Command.gm(PLAYER_STATS, PlayerStatsCommand::new),
            Command.gm(PLAY_MUSIC, PlayMusicCommand::new),
            Command.gm(PLAY_SOUND, PlaySoundCommand::new),
            Command.gm(REMOVE_ARMY, RemoveArmyCommand::new),
            Command.gm(REMOVE_CHAOS, RemoveChaosCommand::new),
            Command.gm(REMOVE_FACTIONS, RemoveFactionsCommand::new),
            Command.gm(REMOVE_PUNISHMENT, RemovePunishmentCommand::new),
            Command.gm(REVIVE, ReviveCommand::new), // TODO Cura o revive? Que diferencia hay con /resurrect
            Command.gm(RMSG, RMSGCommand::new),
            Command.gm(ROYAL_ARMY_MSG, RoyalArmyMessageCommand::new),
            Command.gm(SET_DESC, SetDescCommand::new),
            Command.gm(SETINIVAR, SetIniVarCommand::new), // FIXME Rompe el cliente
            Command.gm(SHOW, ShowCommand::new),
            Command.gm(SHOW_MOBS, ShowMobsCommand::new),
            Command.gm(SILENCE, SilenceCommand::new),
            Command.gm(SUMMON, SummonCommand::new),
            Command.gm(SYSTEM_MESSAGE, SystemMessageCommand::new),
            Command.gm(TALK_AS_NPC, TalkAsNpcCommand::new),
            Command.gm(TELEPORT, TeleportCommand::new),
            Command.gm(TELEPORT_NEAR_TO_PLAYER, TeleportNearToPlayerCommand::new),
            Command.gm(TELEPORT_TO_PLAYER, TeleporToPlayerCommand::new),
            Command.gm(TRIGGER, TriggerCommand::new),
            Command.gm(UNBAN, UnbanCommand::new),
            Command.gm(UNBAN_IP, UnbanIpCommand::new),
            Command.gm(WARNING, WarningCommand::new)
    );

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
        return commands.stream()
                .collect(Collectors.toMap(
                        Command::command,
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
        return commands.stream()
                .map(Command::command)
                .sorted()
                .toList();
    }

    /**
     * Obtiene comandos por categoria.
     */
    public static List<Command> getCommandsByCategory(CommandCategory category) {
        return commands.stream()
                .filter(cmd -> cmd.category() == category)
                .toList();
    }

    /**
     * Obtiene informacion de un comando especifico.
     */
    public static Optional<Command> getCommandInfo(String command) {
        return commands.stream()
                .filter(cmd -> cmd.command().equals(command))
                .findFirst();
    }

}
