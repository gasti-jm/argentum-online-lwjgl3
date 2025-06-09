package org.aoclient.network.protocol.command;

import org.aoclient.engine.game.Console;
import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.protocol.Protocol;
import org.aoclient.network.protocol.command.handlers.basic.*;
import org.aoclient.network.protocol.command.handlers.gm.*;
import org.aoclient.network.protocol.command.handlers.guild.*;
import org.aoclient.network.protocol.command.handlers.party.*;
import org.aoclient.network.protocol.command.handlers.pet.PetFollowCommand;
import org.aoclient.network.protocol.command.handlers.pet.PetStandCommand;
import org.aoclient.network.protocol.command.handlers.pet.ReleasePetCommand;

import java.util.HashMap;
import java.util.Map;

/**
 * Procesador de comandos del protocolo que procesa entradas de texto del usuario y las convierte en llamadas al protocolo de
 * comunicacion cliente-servidor utilizando el patron <b>command</b>.
 * <p>
 * Esta clase actua como el <b>Invoker</b> que mantiene el registro de comandos e invoca al comando apropiado sin conocer sus
 * detalles. La clase {@code Protocol} hace de <b>Receiver</b>, que se encarga de realizar las acciones reales, y los comandos
 * concretos como {@code OnlineCommand}. La escencia del patron es <b>encapsular cada accion como un objeto</b>.
 * <p>
 * Los beneficios de este patron son el desacoplamiento (el procesador no necesita conocer los detalles de cada comando), la
 * extensibilidad (agregar nuevos comandos sin modificar el switch gigante), la reusabilidad (los comandos se pueden reutilizar en
 * otros contextos), el testing (cada comando se puede testear aisladamente), la mantenibilidad y reutilizacion.
 */

public enum CommandProcessor {

    INSTANCE;

    public final CommandValidator validator = new CommandValidator();
    private final Map<String, CommandHandler> commands = new HashMap<>();

    CommandProcessor() {
        registerCommands();
    }

    public void process(String rawCommand) {
        if (rawCommand == null || rawCommand.trim().isEmpty()) return;

        CommandContext context = new CommandContext(rawCommand);

        if (context.isCommand()) handleCommand(context);
        else if (context.isYell()) Protocol.writeYell(context.getMessage());
        else Protocol.writeTalk(context.getMessage());

    }

    private void registerCommands() {
        // Comandos basicos
        commands.put("/ONLINE", new OnlineCommand());
        commands.put("/SALIR", new QuitCommand());
        commands.put("/BALANCE", new RequestAccountStateCommand());
        commands.put("/ENTRENAR", new TrainListCommand());
        commands.put("/DESCANSAR", new RestCommand());
        commands.put("/MEDITAR", new MeditateCommand());
        commands.put("/CONSULTA", new ConsultationCommand());
        commands.put("/RESUCITAR", new ResucitateCommand());
        commands.put("/CURAR", new HealCommand());
        commands.put("/EST", new RequestStatsCommand());
        commands.put("/AYUDA", new HelpCommand());
        commands.put("/COMERCIAR", new CommerceStartCommand());
        commands.put("/BOVEDA", new BankStartCommand());
        commands.put("/ENLISTAR", new EnlistCommand());
        commands.put("/INFORMACION", new InformationCommand());
        commands.put("/RECOMPENSA", new RewardCommand());
        commands.put("/MOTD", new RequestMotdCommand());
        commands.put("/UPTIME", new UptimeCommand());
        commands.put("/COMPARTIRNPC", new ShareNpcCommand());
        commands.put("/NOCOMPARTIRNPC", new StopSharingNpcCommand());
        commands.put("/ENCUESTA", new InquiryCommand());
        commands.put("/CENTINELA", new CentinelReportCommand());
        commands.put("/BMSG", new CouncilMessageCommand());
        commands.put("/ROL", new RoleMasterRequestCommand());
        commands.put("/GM", new GmRequestCommand());
        commands.put("/_BUG", new BugReportCommand());
        commands.put("/DESC", new ChangeDescriptionCommand());
        commands.put("/CONSTRASEÑA", new PasswordCommand());
        commands.put("/APOSTAR", new GambleCommand());
        commands.put("/RETIRARFACCION", new LeaveFactionCommand());
        commands.put("/RETIRAR", new BankExtractGoldCommand());
        commands.put("/DEPOSITAR", new BankDepositGoldCommand());
        commands.put("/DENUNCIAR", new DenounceCommand());

        // Comandos de GM
        commands.put("/GMSG", new GmMessageCommand());
        commands.put("/SHOWNAME", new ShowNameCommand());
        commands.put("/ONLINEREAL", new OnlineRoyalArmyCommand());
        commands.put("/ONLINECAOS", new OnlineChaosLegionCommand());
        commands.put("/IRCERCA", new GoNearbyCommand());
        commands.put("/REM", new CommentCommand());
        commands.put("/HORA", new ServerTimeCommand());
        commands.put("/DONDE", new WhereCommand());
        commands.put("/NENE", new CreaturesInMapCommand());
        commands.put("/TELEPLOC", new WarpMeToTargetCommand());
        commands.put("/TELEP", new WarpCharCommand());
        commands.put("/SILENCIAR", new SilenceCommand());
        commands.put("/SHOW", new ShowCommand());
        commands.put("/IRA", new GoToCharCommand());
        commands.put("/INVISIBLE", new InvisibleCommand());
        commands.put("/PANELGM", new GmPanelCommand());
        commands.put("/TRABAJANDO", new WorkingCommand());
        commands.put("/OCULTANDO", new HidingCommand());
        commands.put("/CARCEL", new JailCommand());
        commands.put("/RMATA", new KillNpcCommand());
        commands.put("/ADVERTENCIA", new WarnUserCommand());
        commands.put("/MOD", new EditCharCommand());
        commands.put("/INFO", new RequestCharInfoCommand());
        commands.put("/STAT", new RequestCharStatsCommand());
        commands.put("/BAL", new RequestCharGoldCommand());
        commands.put("/INV", new RequestCharInventoryCommand());
        commands.put("/BOV", new RequestCharBankCommand());
        commands.put("/SKILLS", new RequestCharSkillsCommand());
        commands.put("/REVIVIR", new ReviveCharCommand());
        commands.put("/ONLINEGM", new OnlineGmCommand());
        commands.put("/ONLINEMAP", new OnlineMapCommand());
        commands.put("/PERDON", new ForgiveCommand());
        commands.put("/ECHAR", new KickCommand());
        commands.put("/EJECUTAR", new ExecuteCommand());
        commands.put("/BAN", new BanCharCommand());
        commands.put("/UNBAN", new UnbanCharCommand());
        commands.put("/SEGUIR", new NpcFollowCommand());
        commands.put("/SUM", new SummonCharCommand());
        commands.put("/CC", new SpawnListRequestCommand());
        commands.put("/RESETINV", new ResetNpcInventoryCommand());
        commands.put("/LIMPIAR", new CleanWorldCommand());
        commands.put("/RMSG", new ServerMessageCommand());
        commands.put("/NICK2IP", new NickToIpCommand());
        commands.put("/IP2NICK", new IpToNickCommand());
        commands.put("/CT", new TeleportCreateCommand());
        commands.put("/DT", new TeleportDestroyCommand());
        commands.put("/LLUVIA", new RainToggleCommand());
        commands.put("/SETDESC", new SetCharDescriptionCommand());
        commands.put("/FORCEMIDIMAP", new ForceMidiToMapCommand());
        commands.put("/FORCEWAVMAP", new ForceWaveToMapCommand());
        commands.put("/REALMSG", new RoyaleArmyMessageCommand());
        commands.put("/CAOSMSG", new ChaosLegionMessageCommand());
        commands.put("/CIUMSG", new CitizenMessageCommand());
        commands.put("/CRIMSG", new CriminalMessageCommand());
        commands.put("/TALKAS", new TalkAsNpcCommand());
        commands.put("/MASDEST", new DestroyAllItemsInAreaCommand());
        commands.put("/ACEPTCONSE", new AcceptRoyalCouncilMemberCommand());
        commands.put("/ACEPTCONSECAOS", new AcceptChaosCouncilMemberCommand());
        commands.put("/PISO", new ItemsInTheFloorCommand());
        commands.put("/ESTUPIDO", new MakeDumbCommand());
        commands.put("/NOESTUPIDO", new MakeDumbNoMoreCommand());
        commands.put("/DUMPSECURITY", new DumpIpTablesCommand());
        commands.put("/KICKCONSE", new CouncilKickCommand());
        commands.put("/TRIGGER", new SetTriggerCommand());
        commands.put("/BANIPLIST", new BannedIpListCommand());
        commands.put("/BANIPRELOAD", new BannedIpReloadCommand());
        commands.put("/MIEMBROSCLAN", new GuildMemberListCommand());
        commands.put("/BANCLAN", new GuildBanCommand());
        commands.put("/BANIP", new BanIpCommand());
        commands.put("/UNBANIP", new UnbanIpCommand());
        commands.put("/CI", new CreateItemCommand());
        commands.put("/DEST", new DestroyItemsCommand());
        commands.put("/NOCAOS", new ChaosLegionKickCommand());
        commands.put("/NOREAL", new RoyalArmyKickCommand());
        commands.put("/FORCEMIDI", new ForceMidiAllCommand());
        commands.put("/FORCEWAV", new ForceWavAllCommand());
        commands.put("/BORRARPENA", new RemovePunishmentCommand());
        commands.put("/BLOQ", new TileBlockedToggleCommand());
        commands.put("/MATA", new KillNpcNoRespawnCommand());
        commands.put("/MASSKILL", new KillAllNearbyNPCsCommand());
        commands.put("/LASTIP", new LastIpCommand());
        commands.put("/MOTDCAMBIA", new ChangeMOTDCommand());
        commands.put("/SMSG", new SystemMessageCommand());
        commands.put("/ACC", new CreateNpcCommand());
        commands.put("/RACC", new CreateNPCWithRespawnCommand());
        commands.put("/AI", new ImperialArmourCommand());
        commands.put("/AC", new ChaosArmourCommand());
        commands.put("/NAVE", new NavigateToggleCommand());
        commands.put("/HABILITAR", new ServerOpenToUsersToggleCommand());
        commands.put("/APAGAR", new TurnOffServerCommand());
        commands.put("/CONDEN", new TurnCriminalCommand());
        commands.put("/RAJAR", new ResetFactionsCommand());
        commands.put("/LASTEMAIL", new RequestCharMailCommand());
        commands.put("/APASS", new AlterPasswordCommand());
        commands.put("/AEMAIL", new AlterMailCommand());
        commands.put("/ANAME", new AlterNameCommand());
        commands.put("/SLOT", new CheckSlotCommand());
        commands.put("/CENTINELAACTIVADO", new ToggleCentinelActivatedCommand());
        commands.put("/DOBACKUP", new DoBackupCommand());
        commands.put("/GUARDAMAPA", new SaveMapCommand());
        commands.put("/MODMAPINFO", new MapInfoCommand());
        commands.put("/GRABAR", new SaveCharsCommand());
        commands.put("/BORRAR", new CleanSOSCommand());
        commands.put("/NCOHE", new NightCommand()); // FIXME NCOHE?
        commands.put("/ECHARTODOSPJS", new KickAllCharsCommand());
        commands.put("/RELOADNPCS", new ReloadNpcsCommand());
        commands.put("/RELOADSINI", new ReloadServerIniCommand());
        commands.put("/RELOADHECHIZOS", new ReloadSpellsCommand());
        commands.put("/RELOADOBJ", new ReloadObjectsCommand());
        commands.put("/REINICIAR", new RestartCommand());
        commands.put("/AUTOUPDATE", new ResetAutoUpdateCommand());
        commands.put("/CHATCOLOR", new ChatColorCommand());
        commands.put("/IGNORADO", new IgnoredCommand());
        commands.put("/PING", new PingCommand());
        commands.put("/SETINIVAR", new SetIniVarCommand());
        commands.put("/HOGAR", new HomeCommand());

        // Comandos de party
        commands.put("/SALIRPARTY", new PartyLeaveCommand());
        commands.put("/CREARPARTY", new PartyCreateCommand());
        commands.put("/PARTY", new PartyJoinCommand());
        commands.put("/PMSG", new PartyMessageCommand());
        commands.put("/ONLINEPARTY", new PartyOnlineCommand());
        commands.put("/ECHARPARTY", new PartyKickCommand());
        commands.put("/PARTYLIDER", new PartySetLeaderCommand());
        commands.put("/ACCEPTPARTY", new PartyAcceptMemberCommand());

        // Comandos de clan
        commands.put("/SALIRCLAN", new GuildLeaveCommand());
        commands.put("/CMSG", new GuildMessageCommand());
        commands.put("/ONLINECLAN", new GuildOnlineCommand());
        commands.put("/FUNDARCLAN", new GuildFundateCommand());
        commands.put("/VOTO", new GuildVoteCommand());
        commands.put("/ONCLAN", new GuildOnlineMembersCommand());
        commands.put("/RAJARCLAN", new RemoveCharFromGuildCommand());
        commands.put("/SHOWCMSG", new ShowGuildMessagesCommand());

        // Comandos de mascota
        commands.put("/QUIETO", new PetStandCommand());
        commands.put("/ACOMPAÑAR", new PetFollowCommand());
        commands.put("/LIBERAR", new ReleasePetCommand());

    }

    private void handleCommand(CommandContext context) {
        CommandHandler handler = commands.get(context.getCommand());
        if (handler != null) {
            try {
                handler.handle(context);
            } catch (CommandException e) {
                Console.INSTANCE.addMsgToConsole(e.getMessage(), false, true, new RGBColor());
            }
        } else Console.INSTANCE.addMsgToConsole("Unknown command: " + context.getCommand(), false, true, new RGBColor());
    }

}
