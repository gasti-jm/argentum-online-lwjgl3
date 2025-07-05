package org.aoclient.network.protocol.command;

import org.aoclient.network.protocol.Protocol;
import org.aoclient.network.protocol.command.handlers.basic.*;
import org.aoclient.network.protocol.command.handlers.gm.*;
import org.aoclient.network.protocol.command.handlers.guild.*;
import org.aoclient.network.protocol.command.handlers.party.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.aoclient.network.protocol.command.GameCommand.*;

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
                CommandDefinition.simple(PET_FOLLOW.getCommand(), Protocol::writePetFollow, PET_FOLLOW.getDescription()),
                CommandDefinition.simple(BALANCE.getCommand(), Protocol::writeRequestAccountState, BALANCE.getDescription()),
                CommandDefinition.simple(OPEN_VAULT.getCommand(), Protocol::writeBankStart, OPEN_VAULT.getDescription()),
                CommandDefinition.simple(TRADE.getCommand(), Protocol::writeCommerceStart, TRADE.getDescription()),
                CommandDefinition.simple(SHARE_NPC.getCommand(), Protocol::writeShareNpc, SHARE_NPC.getDescription()),
                CommandDefinition.simple(CONSULTATION.getCommand(), Protocol::writeConsultation, CONSULTATION.getDescription()),
                CommandDefinition.simple(HEAL.getCommand(), Protocol::writeHeal, HEAL.getDescription()),
                CommandDefinition.simple(REST.getCommand(), Protocol::writeRest, REST.getDescription()),
                CommandDefinition.simple(ENLIST.getCommand(), Protocol::writeEnlist, ENLIST.getDescription()),
                CommandDefinition.simple(TRAIN.getCommand(), Protocol::writeTrainList, TRAIN.getDescription()),
                CommandDefinition.simple(EST.getCommand(), Protocol::writeRequestStats, EST.getDescription()),
                CommandDefinition.simple(GM.getCommand(), Protocol::writeGMRequest, GM.getDescription()),
                CommandDefinition.simple(INFORMATION.getCommand(), Protocol::writeInformation, INFORMATION.getDescription()),
                CommandDefinition.simple(PET_RELEASE.getCommand(), Protocol::writeReleasePet, PET_RELEASE.getDescription()),
                CommandDefinition.simple(MEDITATE.getCommand(), Protocol::writeMeditate, MEDITATE.getDescription()),
                CommandDefinition.simple(MOTD.getCommand(), Protocol::writeRequestMOTD, MOTD.getDescription()),
                CommandDefinition.simple(STOP_SHARING_NPC.getCommand(), Protocol::writeStopSharingNpc, STOP_SHARING_NPC.getDescription()),
                CommandDefinition.simple(ONLINE.getCommand(), Protocol::writeOnline, ONLINE.getDescription()),
                CommandDefinition.simple(PET_STAY.getCommand(), Protocol::writePetStand, PET_STAY.getDescription()),
                CommandDefinition.simple(CLAIM_REWARD.getCommand(), Protocol::writeReward, CLAIM_REWARD.getDescription()),
                CommandDefinition.simple(RESURRECT.getCommand(), Protocol::writeResucitate, RESURRECT.getDescription()),
                CommandDefinition.simple(LEAVE_FACTION.getCommand(), Protocol::writeLeaveFaction, LEAVE_FACTION.getDescription()),
                CommandDefinition.simple(EXIT.getCommand(), Protocol::writeQuit, EXIT.getDescription()),
                CommandDefinition.simple(UPTIME.getCommand(), Protocol::writeUpTime, UPTIME.getDescription()),

                // Comandos basicos con argumentos/validacion
                CommandDefinition.basic(BET.getCommand(), GambleCommand::new, BET.getDescription()),
                CommandDefinition.basic(BMSG.getCommand(), CouncilMessageCommand::new, BMSG.getDescription()),
                CommandDefinition.basic(BUG.getCommand(), BugReportCommand::new, BUG.getDescription()),
                CommandDefinition.basic(CENTINEL_CODE.getCommand(), CentinelReportCommand::new, CENTINEL_CODE.getDescription()),
                CommandDefinition.basic(REPORT.getCommand(), DenounceCommand::new, REPORT.getDescription()),
                CommandDefinition.basic(DEPOSIT.getCommand(), BankDepositGoldCommand::new, DEPOSIT.getDescription()),
                CommandDefinition.basic(DESC.getCommand(), ChangeDescriptionCommand::new, DESC.getDescription()),
                CommandDefinition.basic(POLL.getCommand(), InquiryCommand::new, POLL.getDescription()),
                CommandDefinition.basic(HELP.getCommand(), HelpCommand::new, HELP.getDescription()),
                CommandDefinition.basic(EXTRACT_GOLD.getCommand(), BankExtractGoldCommand::new, EXTRACT_GOLD.getDescription()),
                CommandDefinition.basic(ROL.getCommand(), RoleMasterRequestCommand::new, ROL.getDescription()),

                // ==================== PARTY COMMANDS ====================

                // Comandos de party sin argumentos
                CommandDefinition.simple(PARTY_CREATE.getCommand(), Protocol::writePartyCreate, PARTY_CREATE.getDescription()),
                CommandDefinition.simple(PARTY_ONLINE.getCommand(), Protocol::writePartyOnline, PARTY_ONLINE.getDescription()),
                CommandDefinition.simple(PARTY_JOIN.getCommand(), Protocol::writePartyJoin, PARTY_JOIN.getDescription()),
                CommandDefinition.simple(PARTY_LEAVE.getCommand(), Protocol::writePartyLeave, PARTY_LEAVE.getDescription()),

                // Comandos de party con argumentos/validacion
                CommandDefinition.party(PARTY_ACCEPT.getCommand(), PartyAcceptMemberCommand::new, PARTY_ACCEPT.getDescription()),
                CommandDefinition.party(PARTY_KICK.getCommand(), PartyKickCommand::new, PARTY_KICK.getDescription()),
                CommandDefinition.party(PARTY_SET_LEADER.getCommand(), PartySetLeaderCommand::new, PARTY_SET_LEADER.getDescription()),
                CommandDefinition.party(PARTY_MSG.getCommand(), PartyMessageCommand::new, PARTY_MSG.getDescription()),

                // ==================== GUILD COMMANDS ====================

                // Comandos de guild sin argumentos
                CommandDefinition.simple(GUILD_ONLINE.getCommand(), Protocol::writeGuildOnline, GUILD_ONLINE.getDescription()),
                CommandDefinition.simple(GUILD_LEAVE.getCommand(), Protocol::writeGuildLeave, GUILD_LEAVE.getDescription()),

                // Comandos de guild con argumentos/validacion
                CommandDefinition.guild(GUILD_BAN.getCommand(), GuildBanCommand::new, GUILD_BAN.getDescription()),
                CommandDefinition.guild(GUILD_MSG.getCommand(), GuildMessageCommand::new, GUILD_MSG.getDescription()),
                CommandDefinition.guild(GUILD_FOUND.getCommand(), GuildFundateCommand::new, GUILD_FOUND.getDescription()),
                CommandDefinition.guild(GUILD_MEMBER_LIST.getCommand(), GuildMemberListCommand::new, GUILD_MEMBER_LIST.getDescription()),
                CommandDefinition.guild(GUILD_ONLINE_SPECIFIC.getCommand(), GuildOnlineMembersCommand::new, GUILD_ONLINE_SPECIFIC.getDescription()),
                CommandDefinition.guild(GUILD_KICK.getCommand(), RemoveCharFromGuildCommand::new, GUILD_KICK.getDescription()),
                CommandDefinition.guild(GUILD_MSG_HISTORY.getCommand(), ShowGuildMessagesCommand::new, GUILD_MSG_HISTORY.getDescription()),
                CommandDefinition.guild(GUILD_VOTE.getCommand(), GuildVoteCommand::new, GUILD_VOTE.getDescription()),

                // ==================== GM COMMANDS ====================

                // Comandos de GM sin argumentos
                CommandDefinition.simple(SHUTDOWN.getCommand(), Protocol::writeTurnOffServer, SHUTDOWN.getDescription()),
                CommandDefinition.simple(AUTOUPDATE.getCommand(), Protocol::writeResetAutoUpdate, AUTOUPDATE.getDescription()),
                CommandDefinition.simple(BANIPLIST.getCommand(), Protocol::writeBannedIPList, BANIPLIST.getDescription()),
                CommandDefinition.simple(BANIPRELOAD.getCommand(), Protocol::writeBannedIPReload, BANIPRELOAD.getDescription()),
                CommandDefinition.simple(BLOQ.getCommand(), Protocol::writeTileBlockedToggle, BLOQ.getDescription()),
                CommandDefinition.simple(CLEAR.getCommand(), Protocol::writeCleanSOS, CLEAR.getDescription()),
                CommandDefinition.simple(CC.getCommand(), Protocol::writeSpawnListRequest, CC.getDescription()),
                CommandDefinition.simple(CENTINEL.getCommand(), Protocol::writeToggleCentinelActivated, CENTINEL.getDescription()),
                CommandDefinition.simple(DESTROYITEMS.getCommand(), Protocol::writeDestroyItems, DESTROYITEMS.getDescription()),
                CommandDefinition.simple(DOBACKUP.getCommand(), Protocol::writeDoBackup, DOBACKUP.getDescription()),
                CommandDefinition.simple(DESTROYTELEPORT.getCommand(), Protocol::writeTeleportDestroy, DESTROYTELEPORT.getDescription()),
                CommandDefinition.simple(DUMPSECURITY.getCommand(), Protocol::writeDumpIPTables, DUMPSECURITY.getDescription()),
                CommandDefinition.simple(KICKALL.getCommand(), Protocol::writeKickAllChars, KICKALL.getDescription()),
                CommandDefinition.simple(SAVE.getCommand(), Protocol::writeSaveChars, SAVE.getDescription()),
                CommandDefinition.simple(SAVEMAP.getCommand(), Protocol::writeSaveMap, SAVEMAP.getDescription()),
                CommandDefinition.simple(ADMINSERVER.getCommand(), Protocol::writeServerOpenToUsersToggle, ADMINSERVER.getDescription()),
                CommandDefinition.simple(HIDING.getCommand(), Protocol::writeHiding, HIDING.getDescription()),
                CommandDefinition.simple(HOME.getCommand(), Protocol::writeHome, HOME.getDescription()),
                CommandDefinition.simple(TIME.getCommand(), Protocol::writeServerTime, TIME.getDescription()),
                CommandDefinition.simple(SHOWIGNORED.getCommand(), Protocol::writeIgnored, SHOWIGNORED.getDescription()),
                CommandDefinition.simple(INVISIBILITY.getCommand(), Protocol::writeInvisible, INVISIBILITY.getDescription()),
                CommandDefinition.simple(CLEAN.getCommand(), Protocol::writeCleanWorld, CLEAN.getDescription()),
                CommandDefinition.simple(RAIN.getCommand(), Protocol::writeRainToggle, RAIN.getDescription()),
                CommandDefinition.simple(REMOVE_ITEM_AREA.getCommand(), Protocol::writeDestroyAllItemsInArea, REMOVE_ITEM_AREA.getDescription()),
                CommandDefinition.simple(REMOVE_NPC_AREA.getCommand(), Protocol::writeKillAllNearbyNPCs, REMOVE_NPC_AREA.getDescription()),
                CommandDefinition.simple(REMOVE_NPC.getCommand(), Protocol::writeKillNPCNoRespawn, REMOVE_NPC.getDescription()),
                CommandDefinition.simple(MOTD_CHANGE.getCommand(), Protocol::writeChangeMOTD, MOTD_CHANGE.getDescription()),
                CommandDefinition.simple(NAVE.getCommand(), Protocol::writeNavigateToggle, NAVE.getDescription()),
                CommandDefinition.simple(NIGHT.getCommand(), Protocol::writeNight, NIGHT.getDescription()),
                CommandDefinition.simple(SHOW_HIDDEN_PLAYERS.getCommand(), Protocol::writeHiding, SHOW_HIDDEN_PLAYERS.getDescription()),
                CommandDefinition.simple(ONLINE_CHAOS.getCommand(), Protocol::writeOnlineChaosLegion, ONLINE_CHAOS.getDescription()),
                CommandDefinition.simple(ONLINE_GM.getCommand(), Protocol::writeOnlineGM, ONLINE_GM.getDescription()),
                CommandDefinition.simple(ONLINE_ROYAL.getCommand(), Protocol::writeOnlineRoyalArmy, ONLINE_ROYAL.getDescription()),
                CommandDefinition.simple(PANEL_GM.getCommand(), Protocol::writeGMPanel, PANEL_GM.getDescription()),
                CommandDefinition.simple(PING.getCommand(), Protocol::writePing, PING.getDescription()),
                CommandDefinition.simple(SHOW_OBJ_MAP.getCommand(), Protocol::writeItemsInTheFloor, SHOW_OBJ_MAP.getDescription()),
                CommandDefinition.simple(RESTART.getCommand(), Protocol::writeRestart, RESTART.getDescription()),
                CommandDefinition.simple(RELOAD_SPELLS.getCommand(), Protocol::writeReloadSpells, RELOAD_SPELLS.getDescription()),
                CommandDefinition.simple(RELOAD_NPCS.getCommand(), Protocol::writeReloadNPCs, RELOAD_NPCS.getDescription()),
                CommandDefinition.simple(RELOAD_OBJ.getCommand(), Protocol::writeReloadObjects, RELOAD_OBJ.getDescription()),
                CommandDefinition.simple(RELOAD_SERVER_INI.getCommand(), Protocol::writeReloadServerIni, RELOAD_SERVER_INI.getDescription()),
                CommandDefinition.simple(CLEAN_NPC_INV.getCommand(), Protocol::writeResetNPCInventory, CLEAN_NPC_INV.getDescription()),
                CommandDefinition.simple(REMOVE_NPC_RESPAWN.getCommand(), Protocol::writeKillNPC, REMOVE_NPC_RESPAWN.getDescription()),
                CommandDefinition.simple(NPC_FOLLOW.getCommand(), Protocol::writeNPCFollow, NPC_FOLLOW.getDescription()),
                CommandDefinition.simple(SHOW_NAME.getCommand(), Protocol::writeShowName, SHOW_NAME.getDescription()),
                CommandDefinition.simple(TELEPORT_TARGET.getCommand(), Protocol::writeWarpMeToTarget, TELEPORT_TARGET.getDescription()),
                CommandDefinition.simple(SEE_WORKERS.getCommand(), Protocol::writeWorking, SEE_WORKERS.getDescription()),

                // Comandos de GM con argumentos/validacion
                CommandDefinition.gm(AC.getCommand(), ChaosArmourCommand::new, AC.getDescription()),
                CommandDefinition.gm(CREATE_NPC.getCommand(), CreateNpcCommand::new, CREATE_NPC.getDescription()),
                CommandDefinition.gm(ACEPT_CONSE.getCommand(), AcceptRoyalCouncilMemberCommand::new, ACEPT_CONSE.getDescription()),
                CommandDefinition.gm(ACEPT_CONSE_CHAOS.getCommand(), AcceptChaosCouncilMemberCommand::new, ACEPT_CONSE_CHAOS.getDescription()),
                CommandDefinition.gm(WARNING.getCommand(), WarnUserCommand::new, WARNING.getDescription()),
                CommandDefinition.gm(AEMAIL.getCommand(), AlterMailCommand::new, AEMAIL.getDescription()),
                CommandDefinition.gm(AI.getCommand(), ImperialArmourCommand::new, AI.getDescription()),
                CommandDefinition.gm(ANAME.getCommand(), AlterNameCommand::new, ANAME.getDescription()),
                CommandDefinition.gm(APASS.getCommand(), AlterPasswordCommand::new, APASS.getDescription()),
                CommandDefinition.gm(REQUEST_CHAR_GOLD.getCommand(), RequestCharGoldCommand::new, REQUEST_CHAR_GOLD.getDescription()),
                CommandDefinition.gm(BAN.getCommand(), BanCharCommand::new, BAN.getDescription()),
                CommandDefinition.gm(BAN_IP.getCommand(), BanIpCommand::new, BAN_IP.getDescription()),
                CommandDefinition.gm(REMOVE_PUNISHMENT.getCommand(), RemovePunishmentCommand::new, REMOVE_PUNISHMENT.getDescription()),
                CommandDefinition.gm(REQUEST_CHAR_BANK.getCommand(), RequestCharBankCommand::new, REQUEST_CHAR_BANK.getDescription()),
                CommandDefinition.gm(CAOSMSG.getCommand(), ChaosLegionMessageCommand::new, CAOSMSG.getDescription()),
                CommandDefinition.gm(JAIL.getCommand(), JailCommand::new, JAIL.getDescription()),
                CommandDefinition.gm(CHAT_COLOR.getCommand(), ChatColorCommand::new, CHAT_COLOR.getDescription()),
                CommandDefinition.gm(CIUMSG.getCommand(), CitizenMessageCommand::new, CIUMSG.getDescription()),
                CommandDefinition.gm(CREATE_OBJ.getCommand(), CreateObjectCommand::new, CREATE_OBJ.getDescription()),
                CommandDefinition.gm(CONDEN.getCommand(), TurnCriminalCommand::new, CONDEN.getDescription()),
                CommandDefinition.gm(CRIMSG.getCommand(), CriminalMessageCommand::new, CRIMSG.getDescription()),
                CommandDefinition.gm(CREATE_TELEPORT.getCommand(), TeleportCreateCommand::new, CREATE_TELEPORT.getDescription()),
                CommandDefinition.gm(SHOW_LOCATION.getCommand(), WhereCommand::new, SHOW_LOCATION.getDescription()),
                CommandDefinition.gm(KICK.getCommand(), KickCommand::new, KICK.getDescription()),
                CommandDefinition.gm(EXECUTE.getCommand(), ExecuteCommand::new, EXECUTE.getDescription()),
                CommandDefinition.gm(ESTUPID.getCommand(), MakeDumbCommand::new, ESTUPID.getDescription()),
                CommandDefinition.gm(GMSG.getCommand(), GmMessageCommand::new, GMSG.getDescription()),
                CommandDefinition.gm(PLAYER_INFO.getCommand(), RequestCharInfoCommand::new, PLAYER_INFO.getDescription()),
                CommandDefinition.gm(PLAYER_INV.getCommand(), RequestCharInventoryCommand::new, PLAYER_INV.getDescription()),
                CommandDefinition.gm(IP2NICK.getCommand(), IpToNickCommand::new, IP2NICK.getDescription()),
                CommandDefinition.gm(TELEPORT_TO_PLAYER.getCommand(), GoToCharCommand::new, TELEPORT_TO_PLAYER.getDescription()),
                CommandDefinition.gm(TELEPORT_NEAR_TO_PLAYER.getCommand(), GoNearbyCommand::new, TELEPORT_NEAR_TO_PLAYER.getDescription()),
                CommandDefinition.gm(KICK_CONSE.getCommand(), CouncilKickCommand::new, KICK_CONSE.getDescription()),
                CommandDefinition.gm(LAST_IP.getCommand(), LastIpCommand::new, LAST_IP.getDescription()),
                CommandDefinition.gm(LAST_EMAIL.getCommand(), RequestCharMailCommand::new, LAST_EMAIL.getDescription()),
                CommandDefinition.gm(MOD_PLAYER.getCommand(), EditCharCommand::new, MOD_PLAYER.getDescription()),
                CommandDefinition.gm(MOD_MAP.getCommand(), MapInfoCommand::new, MOD_MAP.getDescription()),
                CommandDefinition.gm(SHOW_MOBS.getCommand(), CreaturesInMapCommand::new, SHOW_MOBS.getDescription()),
                CommandDefinition.gm(NICK2IP.getCommand(), NickToIpCommand::new, NICK2IP.getDescription()),
                CommandDefinition.gm(REMOVE_PLAYER_FROM_CHAOS.getCommand(), ChaosLegionKickCommand::new, REMOVE_PLAYER_FROM_CHAOS.getDescription()),
                CommandDefinition.gm(NO_STUPID.getCommand(), MakeDumbNoMoreCommand::new, NO_STUPID.getDescription()),
                CommandDefinition.gm(NO_REAL.getCommand(), RoyalArmyKickCommand::new, NO_REAL.getDescription()),
                CommandDefinition.gm(ONLINE_MAP.getCommand(), OnlineMapCommand::new, ONLINE_MAP.getDescription()),
                CommandDefinition.gm(FORGIVE.getCommand(), ForgiveCommand::new, FORGIVE.getDescription()),
                CommandDefinition.gm(PLAY_MUSIC.getCommand(), PlayMusicCommand::new, PLAY_MUSIC.getDescription()),
                CommandDefinition.gm(PLAY_SOUND.getCommand(), PlaySoundCommand::new, PLAY_SOUND.getDescription()),
                CommandDefinition.gm(RACC.getCommand(), CreateNPCWithRespawnCommand::new, RACC.getDescription()),
                CommandDefinition.gm(RAJAR.getCommand(), ResetFactionsCommand::new, RAJAR.getDescription()),
                CommandDefinition.gm(REALMSG.getCommand(), RoyaleArmyMessageCommand::new, REALMSG.getDescription()),
                CommandDefinition.gm(REM.getCommand(), CommentCommand::new, REM.getDescription()),
                CommandDefinition.gm(REVIVIR.getCommand(), ReviveCharCommand::new, REVIVIR.getDescription()),
                CommandDefinition.gm(RMSG.getCommand(), ServerMessageCommand::new, RMSG.getDescription()),
                CommandDefinition.gm(SETDESC.getCommand(), SetCharDescriptionCommand::new, SETDESC.getDescription()),
                CommandDefinition.gm(SETINIVAR.getCommand(), SetIniVarCommand::new, SETINIVAR.getDescription()),
                CommandDefinition.gm(SHOW.getCommand(), ShowCommand::new, SHOW.getDescription()),
                CommandDefinition.gm(SILENT.getCommand(), SilenceCommand::new, SILENT.getDescription()),
                CommandDefinition.gm(SHOW_PLAYER_SKILLS.getCommand(), RequestCharSkillsCommand::new, SHOW_PLAYER_SKILLS.getDescription()),
                CommandDefinition.gm(SHOW_PLAYER_SLOT.getCommand(), CheckSlotCommand::new, SHOW_PLAYER_SLOT.getDescription()),
                CommandDefinition.gm(SMSG.getCommand(), SystemMessageCommand::new, SMSG.getDescription()),
                CommandDefinition.gm(STAT.getCommand(), RequestCharStatsCommand::new, STAT.getDescription()),
                CommandDefinition.gm(SUM.getCommand(), SummonCharCommand::new, SUM.getDescription()),
                CommandDefinition.gm(TALKAS.getCommand(), TalkAsNpcCommand::new, TALKAS.getDescription()),
                CommandDefinition.gm(TELEPORT.getCommand(), WarpCharCommand::new, TELEPORT.getDescription()),
                CommandDefinition.gm(TRIGGER.getCommand(), SetTriggerCommand::new, TRIGGER.getDescription()),
                CommandDefinition.gm(UNBAN.getCommand(), UnbanCharCommand::new, UNBAN.getDescription()),
                CommandDefinition.gm(UNBANIP.getCommand(), UnbanIpCommand::new, UNBANIP.getDescription())
        );
    }

}
