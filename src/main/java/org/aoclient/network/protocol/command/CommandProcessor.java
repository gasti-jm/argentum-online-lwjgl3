package org.aoclient.network.protocol.command;

import org.aoclient.engine.game.Console;
import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.protocol.Protocol;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;

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
        autoRegisterAnnotatedCommands(); // Auto registro de comandos complejos con anotaciones y reflexion
        registerSimpleCommands(); // Registro directo de comandos simples con builder pattern
    }

    /**
     * Registra automaticamente todas las clases que estan anotadas con la anotacion {@code @Command}.
     * <p>
     * Este metodo realiza la busqueda de clases anotadas en un paquete especifico utilizando reflexion. Una vez que encuentra
     * estas clases, llama al metodo {@code registerCommand} para registrar cada clase como un comando en el sistema.
     * <p>
     * Es util para automatizar el proceso de descubrimiento y registro de comandos, eliminando la necesidad de hacerlo
     * manualmente por cada clase nueva que implemente comandos.
     */
    private void autoRegisterAnnotatedCommands() {
        new Reflections("org.aoclient.network.protocol.command.handlers")
                .getTypesAnnotatedWith(Command.class)
                .forEach(this::registerCommand);
    }

    /**
     * Registra una clase anotada con {@code @Command} como un comando en el sistema.
     * <p>
     * Este metodo utiliza reflexion para crear una instancia de la clase proporcionada, obtener la anotacion {@code @Command} y
     * registrar el comando junto con su manejador en un mapa interno.
     *
     * @param clazz La clase que representa el comando a registrar. Debe estar anotada con {@code @Command} y debe contar con un
     *              constructor sin argumentos para ser instanciada correctamente.
     */
    private void registerCommand(Class<?> clazz) {
        try {
            Command annotation = clazz.getAnnotation(Command.class);
            CommandHandler handler = (CommandHandler) clazz.getDeclaredConstructor().newInstance();
            commands.put(annotation.value(), handler);
        } catch (Exception e) {
            System.err.println("Failed to register command from class: " + clazz.getSimpleName());
        }
    }

    /**
     * Metodo privado encargado de registrar comandos simples en el procesador de comandos. Los comandos se agrupan en diferentes
     * categorias como: comandos basicos, comandos de GM, comandos de party, comandos de clan y comandos de mascota. Cada comando
     * esta asociado a una accion que se ejecuta al procesarse dicho comando.
     * <p>
     * La implementacion utiliza un patron de construccion fluido (builder pattern) proporcionado por la clase
     * {@link SimpleCommand}. Este builder permite definir el comando, asociarlo a una accion especifica y finalmente registrarlo
     * en el mapa de comandos disponible.
     */
    private void registerSimpleCommands() {
        SimpleCommand.builder()
                // Comandos basicos
                .command("/ayuda").action(Protocol::writeHelp)
                .command("/balance").action(Protocol::writeRequestAccountState)
                .command("/boveda").action(Protocol::writeBankStart)
                .command("/comerciar").action(Protocol::writeCommerceStart)
                .command("/compartirnpc").action(Protocol::writeShareNpc)
                .command("/consulta").action(Protocol::writeConsultation)
                .command("/curar").action(Protocol::writeHeal)
                .command("/descansar").action(Protocol::writeRest)
                .command("/enlistar").action(Protocol::writeEnlist)
                .command("/entrenar").action(Protocol::writeTrainList)
                .command("/est").action(Protocol::writeRequestStats)
                .command("/gm").action(Protocol::writeGMRequest)
                .command("/informacion").action(Protocol::writeInformation)
                .command("/meditar").action(Protocol::writeMeditate)
                .command("/motd").action(Protocol::writeRequestMOTD)
                .command("/nocompartirnpc").action(Protocol::writeStopSharingNpc)
                .command("/online").action(Protocol::writeOnline)
                .command("/recompensa").action(Protocol::writeReward)
                .command("/resucitar").action(Protocol::writeResucitate)
                .command("/retirarfaccion").action(Protocol::writeLeaveFaction)
                .command("/salir").action(Protocol::writeQuit)
                .command("/uptime").action(Protocol::writeUpTime)

                // Comandos de GM
                .command("/apagar").action(Protocol::writeTurnOffServer)
                .command("/autoupdate").action(Protocol::writeResetAutoUpdate)
                .command("/baniplist").action(Protocol::writeBannedIPList)
                .command("/banipreload").action(Protocol::writeBannedIPReload)
                .command("/bloq").action(Protocol::writeTileBlockedToggle)
                .command("/borrar").action(Protocol::writeCleanSOS)
                .command("/cc").action(Protocol::writeSpawnListRequest)
                .command("/centinelaactivado").action(Protocol::writeToggleCentinelActivated)
                .command("/dest").action(Protocol::writeDestroyItems)
                .command("/dobackup").action(Protocol::writeDoBackup)
                .command("/dt").action(Protocol::writeTeleportDestroy)
                .command("/dumpsecurity").action(Protocol::writeDumpIPTables)
                .command("/echartodospjs").action(Protocol::writeKickAllChars)
                .command("/grabar").action(Protocol::writeSaveChars)
                .command("/guardamapa").action(Protocol::writeSaveMap)
                .command("/habilitar").action(Protocol::writeServerOpenToUsersToggle)
                .command("/hogar").action(Protocol::writeHome)
                .command("/hora").action(Protocol::writeServerTime)
                .command("/ignorado").action(Protocol::writeIgnored)
                .command("/invisible").action(Protocol::writeInvisible)
                .command("/limpiar").action(Protocol::writeCleanWorld)
                .command("/lluvia").action(Protocol::writeRainToggle)
                .command("/masdest").action(Protocol::writeDestroyAllItemsInArea)
                .command("/masskill").action(Protocol::writeKillAllNearbyNPCs)
                .command("/mata").action(Protocol::writeKillNPCNoRespawn)
                .command("/motdcambia").action(Protocol::writeChangeMOTD)
                .command("/nave").action(Protocol::writeNavigateToggle)
                .command("/noche").action(Protocol::writeNight)
                .command("/ocultando").action(Protocol::writeHiding)
                .command("/onlinecaos").action(Protocol::writeOnlineChaosLegion)
                .command("/onlinegm").action(Protocol::writeOnlineGM)
                .command("/onlinereal").action(Protocol::writeOnlineRoyalArmy)
                .command("/panelgm").action(Protocol::writeGMPanel)
                .command("/ping").action(Protocol::writePing)
                .command("/piso").action(Protocol::writeItemsInTheFloor)
                .command("/reiniciar").action(Protocol::writeRestart)
                .command("/reloadhechizos").action(Protocol::writeReloadSpells)
                .command("/reloadnpcs").action(Protocol::writeReloadNPCs)
                .command("/reloadobj").action(Protocol::writeReloadObjects)
                .command("/reloadsini").action(Protocol::writeReloadServerIni)
                .command("/resetinv").action(Protocol::writeResetNPCInventory)
                .command("/rmata").action(Protocol::writeKillNPC)
                .command("/seguir").action(Protocol::writeNPCFollow)
                .command("/showname").action(Protocol::writeShowName)
                .command("/teleploc").action(Protocol::writeWarpMeToTarget)
                .command("/trabajando").action(Protocol::writeWorking)

                // Comandos de party
                .command("/crearparty").action(Protocol::writePartyCreate)
                .command("/onlineparty").action(Protocol::writePartyOnline)
                .command("/party").action(Protocol::writePartyJoin)
                .command("/salirparty").action(Protocol::writePartyLeave)

                // Comandos de clan
                .command("/onlineclan").action(Protocol::writeGuildOnline)
                .command("/salirclan").action(Protocol::writeGuildLeave)

                // Comandos de mascota
                .command("/acompañar").action(Protocol::writePetFollow)
                .command("/liberar").action(Protocol::writeReleasePet)
                .command("/quieto").action(Protocol::writePetStand)

                .registerTo(commands);
    }

    private void execute(CommandContext context) {
        CommandHandler handler = commands.get(context.getCommand());
        if (handler != null) {
            try {
                handler.handle(context);
            } catch (CommandException e) {
                System.err.println(e.getMessage());
            }
        } else Console.INSTANCE.addMsgToConsole("Unknown command: " + context.getCommand(), false, true, new RGBColor());
    }

}
