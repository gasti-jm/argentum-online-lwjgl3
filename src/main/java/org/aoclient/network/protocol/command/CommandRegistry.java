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
     * Los comandos se clasifican en categorias como basicos, de party, de guild y de GM, y cada comando tiene asociado su
     * funcionalidad especifica.
     *
     * @return una lista de objetos de tipo {@code Command}, que representan los comandos del sistema junto con su configuracion y
     * proposito
     */
    private static List<Command> loadCommands() {
        return List.of(

                // ==================== BASIC COMMANDS ====================
                Command.basic(PET_FOLLOW.getCommand(), Protocol::writePetFollow, PET_FOLLOW.getDescription()),
                Command.basic(BALANCE.getCommand(), Protocol::writeRequestAccountState, BALANCE.getDescription()),
                Command.basic(OPEN_VAULT.getCommand(), Protocol::writeBankStart, OPEN_VAULT.getDescription()),
                Command.basic(TRADE.getCommand(), Protocol::writeCommerceStart, TRADE.getDescription()),
                Command.basic(SHARE_NPC.getCommand(), Protocol::writeShareNpc, SHARE_NPC.getDescription()),
                Command.basic(CONSULTATION.getCommand(), Protocol::writeConsultation, CONSULTATION.getDescription()),
                Command.basic(HEAL.getCommand(), Protocol::writeHeal, HEAL.getDescription()),
                Command.basic(REST.getCommand(), Protocol::writeRest, REST.getDescription()),
                Command.basic(ENLIST.getCommand(), Protocol::writeEnlist, ENLIST.getDescription()),
                Command.basic(TRAIN.getCommand(), Protocol::writeTrainList, TRAIN.getDescription()),
                Command.basic(EST.getCommand(), Protocol::writeRequestStats, EST.getDescription()),
                Command.basic(GM.getCommand(), Protocol::writeGMRequest, GM.getDescription()),
                Command.basic(INFORMATION.getCommand(), Protocol::writeInformation, INFORMATION.getDescription()),
                Command.basic(PET_RELEASE.getCommand(), Protocol::writeReleasePet, PET_RELEASE.getDescription()),
                Command.basic(MEDITATE.getCommand(), Protocol::writeMeditate, MEDITATE.getDescription()),
                Command.basic(MOTD.getCommand(), Protocol::writeRequestMOTD, MOTD.getDescription()),
                Command.basic(STOP_SHARING_NPC.getCommand(), Protocol::writeStopSharingNpc, STOP_SHARING_NPC.getDescription()),
                Command.basic(ONLINE.getCommand(), Protocol::writeOnline, ONLINE.getDescription()),
                Command.basic(PET_STAY.getCommand(), Protocol::writePetStand, PET_STAY.getDescription()),
                Command.basic(CLAIM_REWARD.getCommand(), Protocol::writeReward, CLAIM_REWARD.getDescription()),
                Command.basic(RESURRECT.getCommand(), Protocol::writeResucitate, RESURRECT.getDescription()),
                Command.basic(LEAVE_FACTION.getCommand(), Protocol::writeLeaveFaction, LEAVE_FACTION.getDescription()),
                Command.basic(EXIT.getCommand(), Protocol::writeQuit, EXIT.getDescription()),
                Command.basic(UPTIME.getCommand(), Protocol::writeUpTime, UPTIME.getDescription()),
                // Comandos basicos con hanlder
                Command.basic(BET.getCommand(), GambleCommand::new, BET.getDescription()),
                Command.basic(BMSG.getCommand(), CouncilMessageCommand::new, BMSG.getDescription()),
                Command.basic(BUG.getCommand(), BugReportCommand::new, BUG.getDescription()),
                Command.basic(CENTINEL_CODE.getCommand(), CentinelReportCommand::new, CENTINEL_CODE.getDescription()),
                Command.basic(REPORT.getCommand(), DenounceCommand::new, REPORT.getDescription()),
                Command.basic(DEPOSIT.getCommand(), BankDepositGoldCommand::new, DEPOSIT.getDescription()),
                Command.basic(DESC.getCommand(), ChangeDescriptionCommand::new, DESC.getDescription()),
                Command.basic(POLL.getCommand(), InquiryCommand::new, POLL.getDescription()),
                Command.basic(HELP.getCommand(), HelpCommand::new, HELP.getDescription()),
                Command.basic(EXTRACT_GOLD.getCommand(), BankExtractGoldCommand::new, EXTRACT_GOLD.getDescription()),
                Command.basic(ROL.getCommand(), RoleMasterRequestCommand::new, ROL.getDescription()),

                // ==================== PARTY COMMANDS ====================
                Command.party(PARTY_CREATE.getCommand(), Protocol::writePartyCreate, PARTY_CREATE.getDescription()),
                Command.party(PARTY_ONLINE.getCommand(), Protocol::writePartyOnline, PARTY_ONLINE.getDescription()),
                Command.party(PARTY_JOIN.getCommand(), Protocol::writePartyJoin, PARTY_JOIN.getDescription()),
                Command.party(PARTY_LEAVE.getCommand(), Protocol::writePartyLeave, PARTY_LEAVE.getDescription()),
                // Comandos de party con handler
                Command.party(PARTY_ACCEPT.getCommand(), PartyAcceptMemberCommand::new, PARTY_ACCEPT.getDescription()),
                Command.party(PARTY_KICK.getCommand(), PartyKickCommand::new, PARTY_KICK.getDescription()),
                Command.party(PARTY_SET_LEADER.getCommand(), PartySetLeaderCommand::new, PARTY_SET_LEADER.getDescription()),
                Command.party(PARTY_MSG.getCommand(), PartyMessageCommand::new, PARTY_MSG.getDescription()),

                // ==================== GUILD COMMANDS ====================
                Command.guild(GUILD_ONLINE.getCommand(), Protocol::writeGuildOnline, GUILD_ONLINE.getDescription()),
                Command.guild(GUILD_LEAVE.getCommand(), Protocol::writeGuildLeave, GUILD_LEAVE.getDescription()),
                // Comandos de guild con handler
                Command.guild(GUILD_BAN.getCommand(), GuildBanCommand::new, GUILD_BAN.getDescription()),
                Command.guild(GUILD_MSG.getCommand(), GuildMessageCommand::new, GUILD_MSG.getDescription()),
                Command.guild(GUILD_FOUND.getCommand(), GuildFundateCommand::new, GUILD_FOUND.getDescription()),
                Command.guild(GUILD_MEMBER_LIST.getCommand(), GuildMemberListCommand::new, GUILD_MEMBER_LIST.getDescription()),
                Command.guild(GUILD_ONLINE_SPECIFIC.getCommand(), GuildOnlineMembersCommand::new, GUILD_ONLINE_SPECIFIC.getDescription()),
                Command.guild(GUILD_KICK.getCommand(), RemoveCharFromGuildCommand::new, GUILD_KICK.getDescription()),
                Command.guild(GUILD_MSG_HISTORY.getCommand(), ShowGuildMessagesCommand::new, GUILD_MSG_HISTORY.getDescription()),
                Command.guild(GUILD_VOTE.getCommand(), GuildVoteCommand::new, GUILD_VOTE.getDescription()),

                // ==================== GM COMMANDS ====================
                Command.gm(SHUTDOWN.getCommand(), Protocol::writeTurnOffServer, SHUTDOWN.getDescription()),
                Command.gm(AUTOUPDATE.getCommand(), Protocol::writeResetAutoUpdate, AUTOUPDATE.getDescription()),
                Command.gm(BANIPLIST.getCommand(), Protocol::writeBannedIPList, BANIPLIST.getDescription()),
                Command.gm(BANIPRELOAD.getCommand(), Protocol::writeBannedIPReload, BANIPRELOAD.getDescription()),
                Command.gm(BLOQ.getCommand(), Protocol::writeTileBlockedToggle, BLOQ.getDescription()),
                Command.gm(CLEAR.getCommand(), Protocol::writeCleanSOS, CLEAR.getDescription()),
                Command.gm(CC.getCommand(), Protocol::writeSpawnListRequest, CC.getDescription()),
                Command.gm(CENTINEL.getCommand(), Protocol::writeToggleCentinelActivated, CENTINEL.getDescription()),
                Command.gm(DESTROYITEMS.getCommand(), Protocol::writeDestroyItems, DESTROYITEMS.getDescription()),
                Command.gm(DOBACKUP.getCommand(), Protocol::writeDoBackup, DOBACKUP.getDescription()),
                Command.gm(DESTROYTELEPORT.getCommand(), Protocol::writeTeleportDestroy, DESTROYTELEPORT.getDescription()),
                Command.gm(DUMPSECURITY.getCommand(), Protocol::writeDumpIPTables, DUMPSECURITY.getDescription()),
                Command.gm(KICKALL.getCommand(), Protocol::writeKickAllChars, KICKALL.getDescription()),
                Command.gm(SAVE.getCommand(), Protocol::writeSaveChars, SAVE.getDescription()),
                Command.gm(SAVEMAP.getCommand(), Protocol::writeSaveMap, SAVEMAP.getDescription()),
                Command.gm(ADMINSERVER.getCommand(), Protocol::writeServerOpenToUsersToggle, ADMINSERVER.getDescription()),
                Command.gm(HIDING.getCommand(), Protocol::writeHiding, HIDING.getDescription()),
                Command.gm(HOME.getCommand(), Protocol::writeHome, HOME.getDescription()),
                Command.gm(TIME.getCommand(), Protocol::writeServerTime, TIME.getDescription()),
                Command.gm(SHOWIGNORED.getCommand(), Protocol::writeIgnored, SHOWIGNORED.getDescription()),
                Command.gm(INVISIBILITY.getCommand(), Protocol::writeInvisible, INVISIBILITY.getDescription()),
                Command.gm(CLEAN.getCommand(), Protocol::writeCleanWorld, CLEAN.getDescription()),
                Command.gm(RAIN.getCommand(), Protocol::writeRainToggle, RAIN.getDescription()),
                Command.gm(REMOVE_ITEM_AREA.getCommand(), Protocol::writeDestroyAllItemsInArea, REMOVE_ITEM_AREA.getDescription()),
                Command.gm(REMOVE_NPC_AREA.getCommand(), Protocol::writeKillAllNearbyNPCs, REMOVE_NPC_AREA.getDescription()),
                Command.gm(REMOVE_NPC.getCommand(), Protocol::writeKillNPCNoRespawn, REMOVE_NPC.getDescription()),
                Command.gm(MOTD_CHANGE.getCommand(), Protocol::writeChangeMOTD, MOTD_CHANGE.getDescription()),
                Command.gm(NAVE.getCommand(), Protocol::writeNavigateToggle, NAVE.getDescription()),
                Command.gm(NIGHT.getCommand(), Protocol::writeNight, NIGHT.getDescription()),
                Command.gm(SHOW_HIDDEN_PLAYERS.getCommand(), Protocol::writeHiding, SHOW_HIDDEN_PLAYERS.getDescription()),
                Command.gm(ONLINE_CHAOS.getCommand(), Protocol::writeOnlineChaosLegion, ONLINE_CHAOS.getDescription()),
                Command.gm(ONLINE_GM.getCommand(), Protocol::writeOnlineGM, ONLINE_GM.getDescription()),
                Command.gm(ONLINE_ROYAL.getCommand(), Protocol::writeOnlineRoyalArmy, ONLINE_ROYAL.getDescription()),
                Command.gm(PANEL_GM.getCommand(), Protocol::writeGMPanel, PANEL_GM.getDescription()),
                Command.gm(PING.getCommand(), Protocol::writePing, PING.getDescription()),
                Command.gm(SHOW_OBJ_MAP.getCommand(), Protocol::writeItemsInTheFloor, SHOW_OBJ_MAP.getDescription()),
                Command.gm(RESTART.getCommand(), Protocol::writeRestart, RESTART.getDescription()),
                Command.gm(RELOAD_SPELLS.getCommand(), Protocol::writeReloadSpells, RELOAD_SPELLS.getDescription()),
                Command.gm(RELOAD_NPCS.getCommand(), Protocol::writeReloadNPCs, RELOAD_NPCS.getDescription()),
                Command.gm(RELOAD_OBJ.getCommand(), Protocol::writeReloadObjects, RELOAD_OBJ.getDescription()),
                Command.gm(RELOAD_SERVER_INI.getCommand(), Protocol::writeReloadServerIni, RELOAD_SERVER_INI.getDescription()),
                Command.gm(CLEAN_NPC_INV.getCommand(), Protocol::writeResetNPCInventory, CLEAN_NPC_INV.getDescription()),
                Command.gm(REMOVE_NPC_RESPAWN.getCommand(), Protocol::writeKillNPC, REMOVE_NPC_RESPAWN.getDescription()),
                Command.gm(NPC_FOLLOW.getCommand(), Protocol::writeNPCFollow, NPC_FOLLOW.getDescription()),
                Command.gm(SHOW_NAME.getCommand(), Protocol::writeShowName, SHOW_NAME.getDescription()),
                Command.gm(TELEPORT_TARGET.getCommand(), Protocol::writeWarpMeToTarget, TELEPORT_TARGET.getDescription()),
                Command.gm(SEE_WORKERS.getCommand(), Protocol::writeWorking, SEE_WORKERS.getDescription()),
                // Comandos de GM con handler
                Command.gm(AC.getCommand(), ChaosArmourCommand::new, AC.getDescription()),
                Command.gm(CREATE_NPC.getCommand(), CreateNpcCommand::new, CREATE_NPC.getDescription()),
                Command.gm(ACEPT_CONSE.getCommand(), AcceptRoyalCouncilMemberCommand::new, ACEPT_CONSE.getDescription()),
                Command.gm(ACEPT_CONSE_CHAOS.getCommand(), AcceptChaosCouncilMemberCommand::new, ACEPT_CONSE_CHAOS.getDescription()),
                Command.gm(WARNING.getCommand(), WarnUserCommand::new, WARNING.getDescription()),
                Command.gm(AEMAIL.getCommand(), AlterMailCommand::new, AEMAIL.getDescription()),
                Command.gm(AI.getCommand(), ImperialArmourCommand::new, AI.getDescription()),
                Command.gm(ANAME.getCommand(), AlterNameCommand::new, ANAME.getDescription()),
                Command.gm(APASS.getCommand(), AlterPasswordCommand::new, APASS.getDescription()),
                Command.gm(REQUEST_CHAR_GOLD.getCommand(), RequestCharGoldCommand::new, REQUEST_CHAR_GOLD.getDescription()),
                Command.gm(BAN.getCommand(), BanCharCommand::new, BAN.getDescription()),
                Command.gm(BAN_IP.getCommand(), BanIpCommand::new, BAN_IP.getDescription()),
                Command.gm(REMOVE_PUNISHMENT.getCommand(), RemovePunishmentCommand::new, REMOVE_PUNISHMENT.getDescription()),
                Command.gm(REQUEST_CHAR_BANK.getCommand(), RequestCharBankCommand::new, REQUEST_CHAR_BANK.getDescription()),
                Command.gm(CAOSMSG.getCommand(), ChaosLegionMessageCommand::new, CAOSMSG.getDescription()),
                Command.gm(JAIL.getCommand(), JailCommand::new, JAIL.getDescription()),
                Command.gm(CHAT_COLOR.getCommand(), ChatColorCommand::new, CHAT_COLOR.getDescription()),
                Command.gm(CIUMSG.getCommand(), CitizenMessageCommand::new, CIUMSG.getDescription()),
                Command.gm(CREATE_OBJ.getCommand(), CreateObjectCommand::new, CREATE_OBJ.getDescription()),
                Command.gm(CONDEN.getCommand(), TurnCriminalCommand::new, CONDEN.getDescription()),
                Command.gm(CRIMSG.getCommand(), CriminalMessageCommand::new, CRIMSG.getDescription()),
                Command.gm(CREATE_TELEPORT.getCommand(), TeleportCreateCommand::new, CREATE_TELEPORT.getDescription()),
                Command.gm(SHOW_LOCATION.getCommand(), WhereCommand::new, SHOW_LOCATION.getDescription()),
                Command.gm(KICK.getCommand(), KickCommand::new, KICK.getDescription()),
                Command.gm(EXECUTE.getCommand(), ExecuteCommand::new, EXECUTE.getDescription()),
                Command.gm(ESTUPID.getCommand(), MakeDumbCommand::new, ESTUPID.getDescription()),
                Command.gm(GMSG.getCommand(), GmMessageCommand::new, GMSG.getDescription()),
                Command.gm(PLAYER_INFO.getCommand(), RequestCharInfoCommand::new, PLAYER_INFO.getDescription()),
                Command.gm(PLAYER_INV.getCommand(), RequestCharInventoryCommand::new, PLAYER_INV.getDescription()),
                Command.gm(IP2NICK.getCommand(), IpToNickCommand::new, IP2NICK.getDescription()),
                Command.gm(TELEPORT_TO_PLAYER.getCommand(), GoToCharCommand::new, TELEPORT_TO_PLAYER.getDescription()),
                Command.gm(TELEPORT_NEAR_TO_PLAYER.getCommand(), GoNearbyCommand::new, TELEPORT_NEAR_TO_PLAYER.getDescription()),
                Command.gm(KICK_CONSE.getCommand(), CouncilKickCommand::new, KICK_CONSE.getDescription()),
                Command.gm(LAST_IP.getCommand(), LastIpCommand::new, LAST_IP.getDescription()),
                Command.gm(LAST_EMAIL.getCommand(), RequestCharMailCommand::new, LAST_EMAIL.getDescription()),
                Command.gm(MOD_PLAYER.getCommand(), EditCharCommand::new, MOD_PLAYER.getDescription()),
                Command.gm(MOD_MAP.getCommand(), MapInfoCommand::new, MOD_MAP.getDescription()),
                Command.gm(SHOW_MOBS.getCommand(), CreaturesInMapCommand::new, SHOW_MOBS.getDescription()),
                Command.gm(NICK2IP.getCommand(), NickToIpCommand::new, NICK2IP.getDescription()),
                Command.gm(REMOVE_PLAYER_FROM_CHAOS.getCommand(), ChaosLegionKickCommand::new, REMOVE_PLAYER_FROM_CHAOS.getDescription()),
                Command.gm(NO_STUPID.getCommand(), MakeDumbNoMoreCommand::new, NO_STUPID.getDescription()),
                Command.gm(NO_REAL.getCommand(), RoyalArmyKickCommand::new, NO_REAL.getDescription()),
                Command.gm(ONLINE_MAP.getCommand(), OnlineMapCommand::new, ONLINE_MAP.getDescription()),
                Command.gm(FORGIVE.getCommand(), ForgiveCommand::new, FORGIVE.getDescription()),
                Command.gm(PLAY_MUSIC.getCommand(), PlayMusicCommand::new, PLAY_MUSIC.getDescription()),
                Command.gm(PLAY_SOUND.getCommand(), PlaySoundCommand::new, PLAY_SOUND.getDescription()),
                Command.gm(RACC.getCommand(), CreateNPCWithRespawnCommand::new, RACC.getDescription()),
                Command.gm(RAJAR.getCommand(), ResetFactionsCommand::new, RAJAR.getDescription()),
                Command.gm(REALMSG.getCommand(), RoyaleArmyMessageCommand::new, REALMSG.getDescription()),
                Command.gm(REM.getCommand(), CommentCommand::new, REM.getDescription()),
                Command.gm(REVIVIR.getCommand(), ReviveCharCommand::new, REVIVIR.getDescription()),
                Command.gm(RMSG.getCommand(), ServerMessageCommand::new, RMSG.getDescription()),
                Command.gm(SETDESC.getCommand(), SetCharDescriptionCommand::new, SETDESC.getDescription()),
                Command.gm(SETINIVAR.getCommand(), SetIniVarCommand::new, SETINIVAR.getDescription()),
                Command.gm(SHOW.getCommand(), ShowCommand::new, SHOW.getDescription()),
                Command.gm(SILENT.getCommand(), SilenceCommand::new, SILENT.getDescription()),
                Command.gm(SHOW_PLAYER_SKILLS.getCommand(), RequestCharSkillsCommand::new, SHOW_PLAYER_SKILLS.getDescription()),
                Command.gm(SHOW_PLAYER_SLOT.getCommand(), CheckSlotCommand::new, SHOW_PLAYER_SLOT.getDescription()),
                Command.gm(SMSG.getCommand(), SystemMessageCommand::new, SMSG.getDescription()),
                Command.gm(STAT.getCommand(), RequestCharStatsCommand::new, STAT.getDescription()),
                Command.gm(SUM.getCommand(), SummonCharCommand::new, SUM.getDescription()),
                Command.gm(TALKAS.getCommand(), TalkAsNpcCommand::new, TALKAS.getDescription()),
                Command.gm(TELEPORT.getCommand(), WarpCharCommand::new, TELEPORT.getDescription()),
                Command.gm(TRIGGER.getCommand(), SetTriggerCommand::new, TRIGGER.getDescription()),
                Command.gm(UNBAN.getCommand(), UnbanCharCommand::new, UNBAN.getDescription()),
                Command.gm(UNBANIP.getCommand(), UnbanIpCommand::new, UNBANIP.getDescription())
        );
    }

}
