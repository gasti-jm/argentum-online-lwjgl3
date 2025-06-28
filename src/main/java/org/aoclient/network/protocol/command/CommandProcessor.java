package org.aoclient.network.protocol.command;

import org.aoclient.engine.game.Console;
import org.aoclient.engine.gui.ImGUISystem;
import org.aoclient.engine.gui.forms.FNewPassword;
import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.protocol.Protocol;
import org.aoclient.network.protocol.command.handlers.basic.*;
import org.aoclient.network.protocol.command.handlers.gm.*;
import org.aoclient.network.protocol.command.handlers.guild.*;
import org.aoclient.network.protocol.command.handlers.party.PartyAcceptMemberCommand;
import org.aoclient.network.protocol.command.handlers.party.PartyKickCommand;
import org.aoclient.network.protocol.command.handlers.party.PartyMessageCommand;
import org.aoclient.network.protocol.command.handlers.party.PartySetLeaderCommand;

import java.util.HashMap;
import java.util.Map;

import static org.aoclient.network.protocol.Protocol.*;

/**
 * Procesador de comandos del protocolo que procesa entradas de texto del usuario y las convierte en llamadas al protocolo de
 * comunicacion cliente-servidor utilizando el patron <b>command</b>.
 * <p>
 * Esta clase actua como el <b>Invoker</b> que mantiene el registro de comandos e invoca al comando apropiado sin conocer sus
 * detalles. La clase {@code Protocol} hace de <b>Receiver</b>, que se encarga de realizar las acciones reales. La escencia del
 * patron es <b>encapsular cada accion como un objeto</b>.
 * <p>
 * Los beneficios de este patron son el desacoplamiento (el procesador no necesita conocer los detalles de cada comando), la
 * extensibilidad (agregar nuevos comandos sin modificar el switch gigante), la reusabilidad (los comandos se pueden reutilizar en
 * otros contextos), el testing (cada comando se puede testear aisladamente), la mantenibilidad y reutilizacion.
 * <p>
 * TODO Autocompletado de comandos
 * TODO Se podria renombrar a TextProcessor, ya que pueden venir desde la consola mensajes simple o comandos, no solo comandos y esta clase procesa tambien texto comun
 */

public enum CommandProcessor {

    INSTANCE;

    // public final CommandValidator validator = new CommandValidator();
    private final Map<String, CommandHandler> commands = new HashMap<>();

    CommandProcessor() {
        registerCommands();
    }

    public void process(String rawCommand) {
        if (rawCommand == null || rawCommand.trim().isEmpty()) return; // TODO Es necesario hacer trim()?

        CommandContext context = new CommandContext(rawCommand);

        if (context.isCommand()) execute(context);
        else if (context.isYell()) Protocol.writeYell(context.getMessage());
        else Protocol.writeTalk(context.getMessage());

    }

    private void registerCommands() {
        // Comandos basicos
        commands.put("/online", cmd -> writeOnline());
        commands.put("/salir", cmd -> writeQuit());
        commands.put("/balance", cmd -> writeRequestAccountState());
        commands.put("/entrenar", cmd -> writeTrainList());
        commands.put("/descansar", cmd -> writeRest());
        commands.put("/meditar", cmd -> writeMeditate());
        commands.put("/consulta", cmd -> writeConsultation());
        commands.put("/resucitar", cmd -> writeResucitate());
        commands.put("/curar", cmd -> writeHeal());
        commands.put("/est", cmd -> writeRequestStats());
        commands.put("/ayuda", cmd -> writeHelp());
        commands.put("/comerciar", cmd -> writeCommerceStart());
        commands.put("/boveda", cmd -> writeBankStart());
        commands.put("/enlistar", cmd -> writeEnlist());
        commands.put("/informacion", cmd -> writeInformation());
        commands.put("/recompensa", cmd -> writeReward());
        commands.put("/motd", cmd -> writeRequestMOTD());
        commands.put("/uptime", cmd -> writeUpTime());
        commands.put("/compartirnpc", cmd -> writeShareNpc());
        commands.put("/nocompartirnpc", cmd -> writeStopSharingNpc());
        commands.put("/encuesta", new InquiryCommand());
        commands.put("/centinela", new CentinelReportCommand());
        commands.put("/bmsg", new CouncilMessageCommand());
        commands.put("/rol", new RoleMasterRequestCommand());
        commands.put("/gm", cmd -> writeGMRequest());
        commands.put("/bug", new BugReportCommand());
        commands.put("/desc", new ChangeDescriptionCommand());
        commands.put("/contraseña", cmd -> ImGUISystem.INSTANCE.show(new FNewPassword()));
        commands.put("/apostar", new GambleCommand());
        commands.put("/retirarfaccion", cmd -> writeLeaveFaction());
        commands.put("/retirar", new BankExtractGoldCommand());
        commands.put("/depositar", new BankDepositGoldCommand());
        commands.put("/denunciar", new DenounceCommand());

        // Comandos de GM
        commands.put("/gmsg", new GmMessageCommand());
        commands.put("/showname", cmd -> writeShowName());
        commands.put("/onlinereal", cmd -> writeOnlineRoyalArmy());
        commands.put("/onlinecaos", cmd -> writeOnlineChaosLegion());
        commands.put("/ircerca", new GoNearbyCommand());
        commands.put("/rem", new CommentCommand());
        commands.put("/hora", cmd -> writeServerTime());
        commands.put("/donde", new WhereCommand());
        commands.put("/nene", new CreaturesInMapCommand());
        commands.put("/teleploc", cmd -> writeWarpMeToTarget());
        commands.put("/telep", new WarpCharCommand());
        commands.put("/silenciar", new SilenceCommand());
        commands.put("/show", new ShowCommand());
        commands.put("/ira", new GoToCharCommand());
        commands.put("/invisible", cmd -> writeInvisible());
        commands.put("/panelgm", cmd -> writeGMPanel());
        commands.put("/trabajando", cmd -> writeWorking());
        commands.put("/ocultando", cmd -> writeHiding());
        commands.put("/carcel", new JailCommand());
        commands.put("/rmata", cmd -> writeKillNPC());
        commands.put("/advertencia", new WarnUserCommand());
        commands.put("/mod", new EditCharCommand());
        commands.put("/info", new RequestCharInfoCommand());
        commands.put("/stat", new RequestCharStatsCommand());
        commands.put("/bal", new RequestCharGoldCommand());
        commands.put("/inv", new RequestCharInventoryCommand());
        commands.put("/bov", new RequestCharBankCommand());
        commands.put("/skills", new RequestCharSkillsCommand());
        commands.put("/revivir", new ReviveCharCommand());
        commands.put("/onlinegm", cmd -> writeOnlineGM());
        commands.put("/onlinemap", new OnlineMapCommand());
        commands.put("/perdon", new ForgiveCommand());
        commands.put("/echar", new KickCommand());
        commands.put("/ejecutar", new ExecuteCommand());
        commands.put("/ban", new BanCharCommand());
        commands.put("/unban", new UnbanCharCommand());
        commands.put("/seguir", cmd -> writeNPCFollow());
        commands.put("/sum", new SummonCharCommand());
        commands.put("/cc", cmd -> writeSpawnListRequest());
        commands.put("/resetinv", cmd -> writeResetNPCInventory());
        commands.put("/limpiar", cmd -> writeCleanWorld());
        commands.put("/rmsg", new ServerMessageCommand());
        commands.put("/nick2ip", new NickToIpCommand());
        commands.put("/ip2nick", new IpToNickCommand());
        commands.put("/ct", new TeleportCreateCommand());
        commands.put("/dt", cmd -> writeTeleportDestroy());
        commands.put("/lluvia", cmd -> writeRainToggle());
        commands.put("/setdesc", new SetCharDescriptionCommand());
        commands.put("/playmusic", new PlayMusicCommand());
        commands.put("/realmsg", new RoyaleArmyMessageCommand());
        commands.put("/caosmsg", new ChaosLegionMessageCommand());
        commands.put("/ciumsg", new CitizenMessageCommand());
        commands.put("/crimsg", new CriminalMessageCommand());
        commands.put("/talkas", new TalkAsNpcCommand());
        commands.put("/masdest", cmd -> writeDestroyAllItemsInArea());
        commands.put("/aceptconse", new AcceptRoyalCouncilMemberCommand());
        commands.put("/aceptconsecaos", new AcceptChaosCouncilMemberCommand());
        commands.put("/piso", cmd -> writeItemsInTheFloor());
        commands.put("/estupido", new MakeDumbCommand());
        commands.put("/noestupido", new MakeDumbNoMoreCommand());
        commands.put("/dumpsecurity", cmd -> writeDumpIPTables());
        commands.put("/kickconse", new CouncilKickCommand());
        commands.put("/trigger", new SetTriggerCommand());
        commands.put("/baniplist", cmd -> writeBannedIPList());
        commands.put("/banipreload", cmd -> writeBannedIPReload());
        commands.put("/miembrosclan", new GuildMemberListCommand());
        commands.put("/banclan", new GuildBanCommand());
        commands.put("/banip", new BanIpCommand());
        commands.put("/unbanip", new UnbanIpCommand());
        commands.put("/co", new CreateObjectCommand());
        commands.put("/dest", cmd -> writeDestroyItems());
        commands.put("/nocaos", new ChaosLegionKickCommand());
        commands.put("/noreal", new RoyalArmyKickCommand());
        commands.put("/playsound", new PlaySoundCommand());
        commands.put("/borrarpena", new RemovePunishmentCommand());
        commands.put("/bloq", cmd -> writeTileBlockedToggle());
        commands.put("/mata", cmd -> writeKillNPCNoRespawn());
        commands.put("/masskill", cmd -> writeKillAllNearbyNPCs());
        commands.put("/lastip", new LastIpCommand());
        commands.put("/motdcambia", cmd -> writeChangeMOTD());
        commands.put("/smsg", new SystemMessageCommand());
        commands.put("/acc", new CreateNpcCommand());
        commands.put("/racc", new CreateNPCWithRespawnCommand());
        commands.put("/ai", new ImperialArmourCommand());
        commands.put("/ac", new ChaosArmourCommand());
        commands.put("/nave", cmd -> writeNavigateToggle());
        commands.put("/habilitar", cmd -> writeServerOpenToUsersToggle());
        commands.put("/apagar", cmd -> writeTurnOffServer());
        commands.put("/conden", new TurnCriminalCommand());
        commands.put("/rajar", new ResetFactionsCommand());
        commands.put("/lastemail", new RequestCharMailCommand());
        commands.put("/apass", new AlterPasswordCommand());
        commands.put("/aemail", new AlterMailCommand());
        commands.put("/aname", new AlterNameCommand());
        commands.put("/slot", new CheckSlotCommand());
        commands.put("/centinelaactivado", cmd -> writeToggleCentinelActivated());
        commands.put("/dobackup", cmd -> writeDoBackup());
        commands.put("/guardamapa", cmd -> writeSaveMap());
        commands.put("/modmapinfo", new MapInfoCommand());
        commands.put("/grabar", cmd -> writeSaveChars());
        commands.put("/borrar", cmd -> writeCleanSOS());
        commands.put("/noche", cmd -> writeNight());
        commands.put("/echartodospjs", cmd -> writeKickAllChars());
        commands.put("/reloadnpcs", cmd -> writeReloadNPCs());
        commands.put("/reloadsini", cmd -> writeReloadServerIni());
        commands.put("/reloadhechizos", cmd -> writeReloadSpells());
        commands.put("/reloadobj", cmd -> writeReloadObjects());
        commands.put("/reiniciar", cmd -> writeRestart());
        commands.put("/autoupdate", cmd -> writeResetAutoUpdate());
        commands.put("/chatcolor", new ChatColorCommand());
        commands.put("/ignorado", cmd -> writeIgnored());
        commands.put("/ping", cmd -> writePing());
        commands.put("/setinivar", new SetIniVarCommand());
        commands.put("/hogar", cmd -> writeHome());

        // Comandos de party
        commands.put("/salirparty", cmd -> writePartyLeave());
        commands.put("/crearparty", cmd -> writePartyCreate());
        commands.put("/party", cmd -> writePartyJoin());
        commands.put("/pmsg", new PartyMessageCommand());
        commands.put("/onlineparty", cmd -> writePartyOnline());
        commands.put("/echarparty", new PartyKickCommand());
        commands.put("/partylider", new PartySetLeaderCommand());
        commands.put("/acceptparty", new PartyAcceptMemberCommand());

        // Comandos de clan
        commands.put("/salirclan", cmd -> writeGuildLeave());
        commands.put("/cmsg", new GuildMessageCommand());
        commands.put("/onlineclan", cmd -> writeGuildOnline());
        commands.put("/fundarclan", new GuildFundateCommand());
        commands.put("/voto", new GuildVoteCommand());
        commands.put("/onclan", new GuildOnlineMembersCommand());
        commands.put("/rajarclan", new RemoveCharFromGuildCommand());
        commands.put("/showcmsg", new ShowGuildMessagesCommand());

        // Comandos de mascota
        commands.put("/quieto", cmd -> writePetStand());
        commands.put("/acompañar", cmd -> writePetFollow());
        commands.put("/liberar", cmd -> writeReleasePet());

    }

    private void execute(CommandContext context) {
        CommandHandler handler = commands.get(context.getCommand());
        if (handler != null) {
            try {
                handler.handle(context);
            } catch (CommandException e) {
                System.err.println(e.getMessage());
            }
        } else
            Console.INSTANCE.addMsgToConsole("Unknown command: " + context.getCommand(), false, true, new RGBColor());
    }

}
