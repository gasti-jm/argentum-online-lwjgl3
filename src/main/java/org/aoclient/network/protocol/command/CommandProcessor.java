package org.aoclient.network.protocol.command;

import org.aoclient.engine.game.Console;
import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.protocol.Protocol;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

    /** Mapa para almacenar los comandos y sus handlers, usando comandos como clave y sus handlers como valor. */
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
        // Auto registro de comandos complejos con anotaciones y reflexion
        autoRegisterAnnotatedCommands();
        // Registro directo de comandos simples con builder pattern
        registerSimpleCommands();
    }

    /**
     * Registra una serie de comandos simples en el sistema que corresponden a diversas funcionalidades para los diferentes tipos
     * de usuarios, incluyendo jugadores regulares, gamemasters (GM), y otras funcionalidades especiales como manejo de mascotas,
     * clanes, y sistemas de party.
     * <p>
     * El metodo utiliza un builder de {@code SimpleCommand} para definir las acciones asociadas a distintos comandos,
     * especificando su funcionalidad mediante llamadas a metodos que invocan las acciones correspondientes en la clase
     * {@code Protocol}.
     * <p>
     * El metodo registra todos los comandos definidos invocando el metodo {@code registerTo}, que integra dichas definiciones en
     * el mapa {@code commands} utilizado por el sistema para procesar comandos.
     */
    private void registerSimpleCommands() {
        SimpleCommand.builder()
                // Comandos basicos
                .command("/online").action(Protocol::writeOnline)
                .command("/salir").action(Protocol::writeQuit)
                .command("/balance").action(Protocol::writeRequestAccountState)
                .command("/entrenar").action(Protocol::writeTrainList)
                .command("/descansar").action(Protocol::writeRest)
                .command("/meditar").action(Protocol::writeMeditate)
                .command("/consulta").action(Protocol::writeConsultation)
                .command("/resucitar").action(Protocol::writeResucitate)
                .command("/curar").action(Protocol::writeHeal)
                .command("/est").action(Protocol::writeRequestStats)
                .command("/ayuda").action(Protocol::writeHelp)
                .command("/comerciar").action(Protocol::writeCommerceStart)
                .command("/boveda").action(Protocol::writeBankStart)
                .command("/enlistar").action(Protocol::writeEnlist)
                .command("/informacion").action(Protocol::writeInformation)
                .command("/recompensa").action(Protocol::writeReward)
                .command("/motd").action(Protocol::writeRequestMOTD)
                .command("/uptime").action(Protocol::writeUpTime)
                .command("/compartirnpc").action(Protocol::writeShareNpc)
                .command("/nocompartirnpc").action(Protocol::writeStopSharingNpc)
                .command("/gm").action(Protocol::writeGMRequest)
                .command("/retirarfaccion").action(Protocol::writeLeaveFaction)

                // Comandos de GM
                .command("/showname").action(Protocol::writeShowName)
                .command("/onlinereal").action(Protocol::writeOnlineRoyalArmy)
                .command("/onlinecaos").action(Protocol::writeOnlineChaosLegion)
                .command("/hora").action(Protocol::writeServerTime)
                .command("/teleploc").action(Protocol::writeWarpMeToTarget)
                .command("/invisible").action(Protocol::writeInvisible)
                .command("/panelgm").action(Protocol::writeGMPanel)
                .command("/trabajando").action(Protocol::writeWorking)
                .command("/ocultando").action(Protocol::writeHiding)
                .command("/rmata").action(Protocol::writeKillNPC)
                .command("/onlinegm").action(Protocol::writeOnlineGM)
                .command("/seguir").action(Protocol::writeNPCFollow)
                .command("/cc").action(Protocol::writeSpawnListRequest)
                .command("/resetinv").action(Protocol::writeResetNPCInventory)
                .command("/limpiar").action(Protocol::writeCleanWorld)
                .command("/dt").action(Protocol::writeTeleportDestroy)
                .command("/lluvia").action(Protocol::writeRainToggle)
                .command("/masdest").action(Protocol::writeDestroyAllItemsInArea)
                .command("/piso").action(Protocol::writeItemsInTheFloor)
                .command("/dumpsecurity").action(Protocol::writeDumpIPTables)
                .command("/baniplist").action(Protocol::writeBannedIPList)
                .command("/banipreload").action(Protocol::writeBannedIPReload)
                .command("/dest").action(Protocol::writeDestroyItems)
                .command("/bloq").action(Protocol::writeTileBlockedToggle)
                .command("/mata").action(Protocol::writeKillNPCNoRespawn)
                .command("/masskill").action(Protocol::writeKillAllNearbyNPCs)
                .command("/motdcambia").action(Protocol::writeChangeMOTD)
                .command("/nave").action(Protocol::writeNavigateToggle)
                .command("/habilitar").action(Protocol::writeServerOpenToUsersToggle)
                .command("/apagar").action(Protocol::writeTurnOffServer)
                .command("/centinelaactivado").action(Protocol::writeToggleCentinelActivated)
                .command("/dobackup").action(Protocol::writeDoBackup)
                .command("/guardamapa").action(Protocol::writeSaveMap)
                .command("/grabar").action(Protocol::writeSaveChars)
                .command("/borrar").action(Protocol::writeCleanSOS)
                .command("/noche").action(Protocol::writeNight)
                .command("/echartodospjs").action(Protocol::writeKickAllChars)
                .command("/reloadnpcs").action(Protocol::writeReloadNPCs)
                .command("/reloadsini").action(Protocol::writeReloadServerIni)
                .command("/reloadhechizos").action(Protocol::writeReloadSpells)
                .command("/reloadobj").action(Protocol::writeReloadObjects)
                .command("/reiniciar").action(Protocol::writeRestart)
                .command("/autoupdate").action(Protocol::writeResetAutoUpdate)
                .command("/ignorado").action(Protocol::writeIgnored)
                .command("/ping").action(Protocol::writePing)
                .command("/hogar").action(Protocol::writeHome)

                // Comandos de party
                .command("/salirparty").action(Protocol::writePartyLeave)
                .command("/crearparty").action(Protocol::writePartyCreate)
                .command("/party").action(Protocol::writePartyJoin)
                .command("/onlineparty").action(Protocol::writePartyOnline)

                // Comandos de clan
                .command("/salirclan").action(Protocol::writeGuildLeave)
                .command("/onlineclan").action(Protocol::writeGuildOnline)

                // Comandos de mascota
                .command("/quieto").action(Protocol::writePetStand)
                .command("/acompañar").action(Protocol::writePetFollow)
                .command("/liberar").action(Protocol::writeReleasePet)

                .registerTo(commands);
    }

    /**
     * Registra automaticamente las clases que implementan comandos en el sistema. Este proceso se basa en la deteccion de clases
     * anotadas con {@link Command} en un paquete especifico, las cuales son dinamicamente instanciadas y registradas en un mapa
     * para su posterior utilizacion.
     * <p>
     * El metodo realiza las siguientes acciones:
     * <ol>
     *  <li>Utiliza la biblioteca Reflections para analizar el paquete
     *      {@code org.aoclient.network.protocol.command.handlers}, identificando las clases anotadas con {@link Command}.
     *  <li>Verifica que dichas clases implementen la interfaz {@link CommandHandler} antes de proceder con su registro.
     *  <li>Extrae el valor de la anotacion {@code Command} de cada clase, el cual se utiliza como clave unica para mapear la
     *      instancia del handler de comandos correspondiente.
     *  <li>Las instancias de los handlers de comandos se crean mediante refleccion, llamando al constructor por defecto de
     *      cada clase detectada.
     * </ol>
     * <p>
     * <b>Nota:</b> Este metodo es esencial para garantizar una forma flexible y dinamica de registrar nuevos comandos sin la
     * necesidad de modificar directamente el codigo fuente. Las clases de comandos simplemente necesitan declararse en el paquete
     * especificado y ser anotadas con {@code @Command} para ser automaticamente consideradas por el sistema.
     */
    private void autoRegisterAnnotatedCommands() {
        try {
            /* Crea una instancia de Reflections que escaneara el paquete org.aoclient.network.protocol.command.handlers
             * permitiendo hacer introspeccion del codigo en tiempo de ejecucion. */
            Reflections reflections = new Reflections("org.aoclient.network.protocol.command.handlers");
            /* Busca todas las clases dentro del paquete que tengan la anotacion @Command a travez del metodo
             * getTypesAnnotatedWith() que retorna un conjunto de clases que cumplen con ese criterio. Esto SOLO funciona porque
             * @Command tiene @Retention(RUNTIME). */
            Set<Class<?>> commandClasses = reflections.getTypesAnnotatedWith(Command.class);
            // Itera todas las clases del conjuto
            for (Class<?> clazz : commandClasses) {
                // Si la clase no implementa la interfaz CommandHandler
                if (!CommandHandler.class.isAssignableFrom(clazz)) continue;
                // Obtiene la anotacion @Command de la clase, esto SOLO funciona porque @Command tiene @Target(TYPE)
                Command annotation = clazz.getAnnotation(Command.class);
                // Crea una instancia del handler usando reflexion
                CommandHandler handler = (CommandHandler) clazz.getDeclaredConstructor().newInstance();
                // Registra el comando en el mapa usando el valor de la anotacion como clave
                commands.put(annotation.value(), handler);
            }
        } catch (Exception e) {
            System.err.println("Error auto-registering commands: " + e.getMessage());
        }
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
