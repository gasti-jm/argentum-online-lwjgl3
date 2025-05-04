package org.aoclient.network.protocol;

import org.aoclient.engine.game.Console;
import org.aoclient.engine.game.User;
import org.aoclient.engine.game.models.E_Heading;
import org.aoclient.engine.game.models.E_Skills;
import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.ByteQueue;
import org.aoclient.network.SocketConnection;
import org.aoclient.network.protocol.types.GMCommand;

import java.nio.charset.StandardCharsets;

import static org.aoclient.engine.utils.GameData.charList;
import static org.lwjgl.glfw.GLFW.glfwGetTime;

/**
 * Clase responsable de manejar el protocolo de comunicacion entre el cliente y el servidor.
 * <p>
 * {@code Protocol} implementa las operaciones necesarias para la comunicacion de red, procesando los paquetes de datos entrantes
 * desde el servidor y preparando los paquetes salientes hacia el servidor.
 * <p>
 * Esta clase contiene implementaciones para todos los comandos del protocolo del juego, incluyendo acciones de personaje,
 * interacciones con el entorno, comunicacion con otros jugadores y comandos administrativos.
 * <p>
 * Trabaja en conjunto con {@link SocketConnection} para la transmision real de los datos, y utiliza {@link ByteQueue} para
 * almacenar temporalmente los datos entrantes y salientes antes de su procesamiento.
 * <p>
 * La mayoria de los metodos de esta clase siguen un patron de nomenclatura donde los metodos que comienzan con <b>write</b>
 * envian comandos al servidor, mientras que los metodos que comienzan con <b>handle</b> procesan las respuestas recibidas.
 * <p>
 * Cada comando de protocolo esta definido en los enumeradores {@link ClientPacket} y {@link ServerPacket} para garantizar una
 * comunicacion estandarizada.
 */

public class Protocol {

    private static final Console console = Console.get();
    private static final PacketReceiver receiver = new PacketReceiver();
    public static int pingTime;

    /**
     * <p>
     * Buffer de comunicacion que almacena temporalmente todos los mensajes que el cliente necesita enviar al servidor antes de su
     * transmision efectiva.
     * <p>
     * Este objeto actua como una cola intermedia que acumula y estructura los comandos y datos del cliente, permitiendo que sean
     * procesados y enviados al servidor en el momento adecuado. La separacion entre la preparacion de datos y su transmision
     * mejora la modularidad del codigo y el control del flujo de comunicacion.
     * <p>
     * Caracteristicas principales:
     * <ul>
     *   <li>Se crea durante la inicializacion de la clase {@code Protocol} para estar disponible globalmente como recurso
     *       estatico compartido
     *   <li>Almacena datos en formato binario con metodos especializados para diferentes tipos
     *   <li>Todos los metodos {@code write*} añaden datos a este buffer
     *   <li>Soporta la serializacion de diferentes tipos de datos (enteros, cadenas, booleanos, etc.)
     *   <li>El tamaño del buffer se amplia automaticamente segun sea necesario
     * </ul>
     * <p>
     * Flujo de trabajo:
     * <ol>
     *   <li>Los metodos {@code write*} de {@code Protocol} escriben datos estructurados en este buffer
     *   <li>La clase {@code SocketConnection}, mediante su metodo {@code write()}, lee los datos
     *       acumulados en este buffer
     *   <li>Los datos son enviados a traves del socket al servidor
     *   <li>El buffer se vacia una vez que los datos han sido transmitidos
     * </ol>
     * <p>
     * Este diseño permite que la creacion y preparacion de paquetes este completamente desacoplada de la logica de transmision de
     * red, facilitando la depuracion, las pruebas y el mantenimiento del codigo.
     *
     * @see ByteQueue Clase que implementa la funcionalidad de cola de bytes con operaciones especializadas para la comunicacion
     * acion de red
     * @see SocketConnection Clase responsable de transmitir los datos acumulados en este buffer
     */
    public static ByteQueue outgoingData = new ByteQueue(); // Buffer temporal para la salida de datos (escribe lo que envia el cliente al servidor)

    /**
     * Almacena temporalmente todos los mensajes recibidos del servidor antes de ser procesados por el cliente.
     * <p>
     * Este buffer actua como un almacen intermedio donde se acumulan los datos binarios recibidos desde el servidor a traves del
     * socket de comunicacion. Los datos permanecen en este buffer hasta que son interpretados y procesados por el handler
     * correspondiente.
     * <p>
     * El ciclo de vida de los datos en este buffer es el siguiente:
     * <ol>
     *   <li>La clase {@code SocketConnection}, a traves de su metodo {@code read()}, recibe datos
     *       del servidor y los escribe en este buffer mediante {@code writeBlock()}
     *   <li>El metodo {@code handleIncomingData()} envia este buffer al {@code PacketReceiver}
     *       para identificar el tipo de paquete y procesarlo
     *   <li>Los handlers de paquetes especificos ({@code PacketHandler}) leen y extraen
     *       los datos necesarios del buffer para su procesamiento
     *   <li>Los datos ya procesados son eliminados del buffer mediante operaciones de lectura
     *       que modifican la posicion del puntero interno
     * </ol>
     * <p>
     * Este mecanismo permite que multiples paquetes de datos sean procesados de manera secuencial en un solo ciclo, ya que cada
     * handler extrae solo la cantidad de datos que le corresponde del flujo de entrada.
     */
    public static ByteQueue incomingData = new ByteQueue(); // Buffer temporal para la entrada de datos (lee lo que recibe el cliente del servidor)

    public static void handleIncomingData() {
        // Delega el procesamiento de paquetes entrantes a PacketReceiver
        receiver.processIncomingData(incomingData);
    }

    /**
     * <p>
     * Escribe en el buffer temporal de salida los datos entrantes del cliente. En otras palabras, construye un paquete para la
     * accion de logearse.
     *
     * @param username nombre de usuario
     * @param password contraseña
     */
    public static void writeLoginExistingChar(String username, String password) {
        // Añade primero el paquete identificador para que al momento de deserializar el paquete entrante, lea primero el ID del paquete
        outgoingData.writeByte(ClientPacket.LOGIN_EXISTING_CHAR.getId());
        outgoingData.writeASCIIString(username);
        outgoingData.writeASCIIString(password);
        outgoingData.writeByte(0); // App.Major
        outgoingData.writeByte(13); // App.Minor
        outgoingData.writeByte(0); // App.Revision
    }

    public static void writeThrowDices() {
        outgoingData.writeByte(ClientPacket.THROW_DICES.getId());
    }

    public static void writeLoginNewChar(String userName, String userPassword, int userRaza, int userSexo, int userClase, int userHead, String userEmail, int userHogar) {
        outgoingData.writeByte(ClientPacket.LOGIN_NEW_CHAR.getId());
        outgoingData.writeASCIIString(userName);
        outgoingData.writeASCIIString(userPassword);

        outgoingData.writeByte(0);  // App.Major
        outgoingData.writeByte(13); // App.Minor
        outgoingData.writeByte(0);  // App.Revision

        outgoingData.writeByte(userRaza);
        outgoingData.writeByte(userSexo);
        outgoingData.writeByte(userClase);
        outgoingData.writeInteger((short) userHead);

        outgoingData.writeASCIIString(userEmail);
        outgoingData.writeByte(userHogar);
    }

    public static void writeTalk(String chat) {
        outgoingData.writeByte(ClientPacket.TALK.getId());
        outgoingData.writeASCIIString(chat);
    }

    public static void writeYell(String chat) {
        outgoingData.writeByte(ClientPacket.YELL.getId());
        outgoingData.writeASCIIString(chat);
    }

    public static void writeWhisper(short charIndex, String chat) {
        outgoingData.writeByte(ClientPacket.WHISPER.getId());
        outgoingData.writeInteger(charIndex);
        outgoingData.writeASCIIString(chat);
    }

    public static void writeWalk(E_Heading direction) {
        outgoingData.writeByte(ClientPacket.WALK.getId());
        outgoingData.writeByte(direction.value);
    }

    public static void writeDrop(int slot, int amount) {
        outgoingData.writeByte(ClientPacket.DROP.getId());
        outgoingData.writeByte((byte) slot);
        outgoingData.writeInteger((short) amount);
    }

    public static void writeRequestPositionUpdate() {
        outgoingData.writeByte(ClientPacket.REQUEST_POSITION_UPDATE.getId());
    }

    public static void writeAttack() {
        outgoingData.writeByte(ClientPacket.ATTACK.getId());
    }

    public static void writePickUp() {
        outgoingData.writeByte(ClientPacket.PICK_UP.getId());
    }

    public static void writeSafeToggle() {
        outgoingData.writeByte(ClientPacket.SAFE_TOGGLE.getId());
    }

    public static void writeResucitationToggle() {
        outgoingData.writeByte(ClientPacket.RESUSCITATION_SAFE_TOGGLE.getId());
    }

    public static void writeRequestGuildLeaderInfo() {
        outgoingData.writeByte(ClientPacket.REQUEST_GUILD_LEADER_INFO.getId());
    }

    public static void writeRequestAttributes() {
        outgoingData.writeByte(ClientPacket.REQUEST_ATTRIBUTES.getId());
    }

    public static void writeRequestFame() {
        outgoingData.writeByte(ClientPacket.REQUEST_FAME.getId());
    }

    public static void writeRequestSkills() {
        outgoingData.writeByte(ClientPacket.REQUEST_SKILLS.getId());
    }

    public static void writeRequestMiniStats() {
        outgoingData.writeByte(ClientPacket.REQUEST_MINI_STATS.getId());
    }

    public static void writeChangeHeading(E_Heading direction) {
        outgoingData.writeByte(ClientPacket.CHANGE_HEADING.getId());
        outgoingData.writeByte(direction.value);
    }

    public static void writeModifySkills(int[] skills) {
        outgoingData.writeByte(ClientPacket.MODIFY_SKILLS.getId());
        for (E_Skills skill : E_Skills.values())
            outgoingData.writeByte(skills[skill.getValue() - 1]);
    }

    public static void writeLeftClick(int x, int y) {
        outgoingData.writeByte(ClientPacket.LEFT_CLICK.getId());
        outgoingData.writeByte(x);
        outgoingData.writeByte(y);
    }

    public static void writeWorkLeftClick(int x, int y, int skill) {
        outgoingData.writeByte(ClientPacket.WORK_LEFT_CLICK.getId());
        outgoingData.writeByte(x);
        outgoingData.writeByte(y);
        outgoingData.writeByte(skill);
    }

    public static void writeDoubleClick(int x, int y) {
        outgoingData.writeByte(ClientPacket.DOUBLE_CLICK.getId());
        outgoingData.writeByte(x);
        outgoingData.writeByte(y);
    }

    public static void writeUseItem(int slot) {
        outgoingData.writeByte(ClientPacket.USE_ITEM.getId());
        outgoingData.writeByte(slot);
    }

    public static void writeEquipItem(int slot) {
        outgoingData.writeByte(ClientPacket.EQUIP_ITEM.getId());
        outgoingData.writeByte(slot);
    }

    public static void writeWork(int skill) {
        if (User.get().isDead()) {
            console.addMsgToConsole(new String("¡¡Estas muerto!!".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outgoingData.writeByte(ClientPacket.WORK.getId());
        outgoingData.writeByte(skill);
    }

    public static void writeCastSpell(int slot) {
        outgoingData.writeByte(ClientPacket.CAST_SPELL.getId());
        outgoingData.writeByte(slot);
    }

    public static void writeQuit() {
        if (charList[User.get().getUserCharIndex()].isParalizado()) {
            console.addMsgToConsole(new String("No puedes salir estando paralizado.".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outgoingData.writeByte(ClientPacket.QUIT.getId());
    }

    public static void writeSpellInfo(final int slot) {
        outgoingData.writeByte(ClientPacket.SPELL_INFO.getId());
        outgoingData.writeByte(slot);
    }

    public static void writeCommerceEnd() {
        outgoingData.writeByte(ClientPacket.COMMERCE_END.getId());
    }

    public static void writeChangePassword(String oldPass, String newPass) {
        outgoingData.writeByte(ClientPacket.CHANGE_PASSWORD.getId());
        outgoingData.writeASCIIString(oldPass);
        outgoingData.writeASCIIString(newPass);
    }

    public static void writeOnline() {
        outgoingData.writeByte(ClientPacket.ONLINE.getId());
    }

    public static void writeMeditate() {

        if (User.get().getUserMaxMAN() == User.get().getUserMinMAN()) {
            return;
        }

        if (charList[User.get().getUserCharIndex()].isDead()) {
            console.addMsgToConsole(new String("¡Estas muerto!".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outgoingData.writeByte(ClientPacket.MEDITATE.getId());
    }

    public static void writeCommerceStart() {
        outgoingData.writeByte(ClientPacket.COMMERCE_START.getId());
    }

    public static void writeBankStart() {
        outgoingData.writeByte(ClientPacket.BANK_START.getId());
    }

    public static void writeGuildLeave() {
        outgoingData.writeByte(ClientPacket.GUILD_LEAVE.getId());
    }

    public static void writeRequestAccountState() {
        if (charList[User.get().getUserCharIndex()].isDead()) {
            console.addMsgToConsole(new String("¡Estas muerto!".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outgoingData.writeByte(ClientPacket.REQUEST_ACCOUNT_STATE.getId());
    }

    public static void writePetStand() {
        if (charList[User.get().getUserCharIndex()].isDead()) {
            console.addMsgToConsole(new String("¡Estas muerto!".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outgoingData.writeByte(ClientPacket.PET_STAND.getId());
    }

    public static void writePetFollow() {
        if (charList[User.get().getUserCharIndex()].isDead()) {
            console.addMsgToConsole(new String("¡Estas muerto!".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outgoingData.writeByte(ClientPacket.PET_FOLLOW.getId());
    }

    public static void writeReleasePet() {
        if (charList[User.get().getUserCharIndex()].isDead()) {
            console.addMsgToConsole(new String("¡Estas muerto!".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outgoingData.writeByte(ClientPacket.RELEASE_PET.getId());
    }

    public static void writeTrainList() {
        if (charList[User.get().getUserCharIndex()].isDead()) {
            console.addMsgToConsole(new String("¡Estas muerto!".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outgoingData.writeByte(ClientPacket.TRAIN_LIST.getId());
    }

    public static void writeRest() {
        if (charList[User.get().getUserCharIndex()].isDead()) {
            console.addMsgToConsole(new String("¡Estas muerto!".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outgoingData.writeByte(ClientPacket.REST.getId());
    }

    public static void writeConsultation() {
        outgoingData.writeByte(ClientPacket.CONSULTA.getId());
    }

    public static void writeResucitate() {
        outgoingData.writeByte(ClientPacket.RESUCITATE.getId());
    }

    public static void writeHeal() {
        outgoingData.writeByte(ClientPacket.HEAL.getId());
    }

    public static void writeRequestStats() {
        outgoingData.writeByte(ClientPacket.REQUEST_STATS.getId());
    }

    public static void writeHelp() {
        outgoingData.writeByte(ClientPacket.HELP.getId());
    }

    public static void writeEnlist() {
        outgoingData.writeByte(ClientPacket.ENLIST.getId());
    }

    public static void writeInformation() {
        outgoingData.writeByte(ClientPacket.INFORMATION.getId());
    }

    public static void writeReward() {
        outgoingData.writeByte(ClientPacket.REWARD.getId());
    }

    public static void writeRequestMOTD() {
        outgoingData.writeByte(ClientPacket.REQUEST_MOTD.getId());
    }

    public static void writeUpTime() {
        outgoingData.writeByte(ClientPacket.UPTIME.getId());
    }

    public static void writePartyLeave() {
        outgoingData.writeByte(ClientPacket.PARTY_LEAVE.getId());
    }

    public static void writePartyCreate() {
        if (charList[User.get().getUserCharIndex()].isDead()) {
            console.addMsgToConsole(new String("¡Estas muerto!".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outgoingData.writeByte(ClientPacket.PARTY_CREATE.getId());
    }

    public static void writePartyJoin() {
        if (charList[User.get().getUserCharIndex()].isDead()) {
            console.addMsgToConsole(new String("¡Estas muerto!".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outgoingData.writeByte(ClientPacket.PARTY_JOIN.getId());
    }

    public static void writeShareNpc() {
        if (charList[User.get().getUserCharIndex()].isDead()) {
            console.addMsgToConsole(new String("¡Estas muerto!".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outgoingData.writeByte(ClientPacket.SHARE_NPC.getId());
    }

    public static void writeStopSharingNpc() {
        if (charList[User.get().getUserCharIndex()].isDead()) {
            console.addMsgToConsole(new String("¡Estas muerto!".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outgoingData.writeByte(ClientPacket.STOP_SHARING_NPC.getId());
    }

    public static void writeInquiry() {
        outgoingData.writeByte(ClientPacket.INQUIRY.getId());
    }

    public static void writeInquiryVote(int opt) {
        outgoingData.writeByte(ClientPacket.INQUIRY_VOTE.getId());
        outgoingData.writeByte(opt);
    }

    public static void writeGuildMessage(String message) {
        outgoingData.writeByte(ClientPacket.GUILD_MESSAGE.getId());
        outgoingData.writeASCIIString(message);
    }

    public static void writePartyMessage(String message) {
        outgoingData.writeByte(ClientPacket.PARTY_MESSAGE.getId());
        outgoingData.writeASCIIString(message);
    }

    public static void writeCentinelReport(int number) {
        outgoingData.writeByte(ClientPacket.CENTINEL_REPORT.getId());
        outgoingData.writeByte(number);
    }

    public static void writeGuildOnline() {
        outgoingData.writeByte(ClientPacket.GUILD_ONLINE.getId());
    }

    public static void writePartyOnline() {
        outgoingData.writeByte(ClientPacket.GUILD_ONLINE.getId());
    }

    public static void writeCouncilMessage(String message) {
        outgoingData.writeByte(ClientPacket.COUNCIL_MESSAGE.getId());
        outgoingData.writeASCIIString(message);
    }

    public static void writeRoleMasterRequest(String message) {
        outgoingData.writeByte(ClientPacket.ROLE_MASTER_REQUEST.getId());
        outgoingData.writeASCIIString(message);
    }

    public static void writeGMRequest() {
        outgoingData.writeByte(ClientPacket.GM_REQUEST.getId());
    }

    public static void writeBugReport(String message) {
        outgoingData.writeByte(ClientPacket.BUG_REPORT.getId());
        outgoingData.writeASCIIString(message);
    }

    public static void writeChangeDescription(String message) {
        outgoingData.writeByte(ClientPacket.CHANGE_DESCRIPTION.getId());
        outgoingData.writeASCIIString(message);
    }

    public static void writeGuildVote(String message) {
        outgoingData.writeByte(ClientPacket.GUILD_VOTE.getId());
        outgoingData.writeASCIIString(message);
    }

    public static void writePunishments(String message) {
        outgoingData.writeByte(ClientPacket.PUNISHMENTS.getId());
        outgoingData.writeASCIIString(message);
    }

    public static void writeGamble(short amount) {
        outgoingData.writeByte(ClientPacket.GAMBLE.getId());
        outgoingData.writeInteger(amount);
    }

    public static void writeLeaveFaction() {
        outgoingData.writeByte(ClientPacket.LEAVE_FACTION.getId());
    }

    public static void writeBankExtractGold(int amount) {
        outgoingData.writeByte(ClientPacket.BANK_EXTRACT_GOLD.getId());
        outgoingData.writeLong(amount);
    }

    public static void writeBankDepositGold(int amount) {
        outgoingData.writeByte(ClientPacket.BANK_DEPOSIT_GOLD.getId());
        outgoingData.writeLong(amount);
    }

    public static void writeDenounce(String message) {
        outgoingData.writeByte(ClientPacket.DENOUNCE.getId());
        outgoingData.writeASCIIString(message);
    }

    public static void writeGuildFundate() {
        outgoingData.writeByte(ClientPacket.GUILD_FUNDATE.getId());
    }

    public static void writeGuildFundation(int clanType) {
        outgoingData.writeByte(ClientPacket.GUILD_FUNDATION.getId());
        outgoingData.writeByte(clanType);
    }

    public static void writePartyKick(String userName) {
        outgoingData.writeByte(ClientPacket.PARTY_KICK.getId());
        outgoingData.writeASCIIString(userName);
    }

    public static void writePartySetLeader(String userName) {
        outgoingData.writeByte(ClientPacket.PARTY_SET_LEADER.getId());
        outgoingData.writeASCIIString(userName);
    }

    public static void writePartyAcceptMember(String userName) {
        outgoingData.writeByte(ClientPacket.PARTY_ACCEPT_MEMBER.getId());
        outgoingData.writeASCIIString(userName);
    }

     /* ##############################################
        #              COMANDOS DE GM                #
        ##############################################*/

    public static void writeGMMessage(String message) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.GM_MESSAGE.getId());

        outgoingData.writeASCIIString(message);
    }

    public static void writeShowName() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.SHOW_NAME.getId());
    }

    public static void writeOnlineRoyalArmy() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.ONLINE_ROYAL_ARMY.getId());
    }

    public static void writeOnlineChaosLegion() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.ONLINE_CHAOS_LEGION.getId());
    }

    public static void writeGoNearby(String userName) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.GO_NEARBY.getId());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeComment(String message) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.COMMENT.getId());

        outgoingData.writeASCIIString(message);
    }

    public static void writeServerTime() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.SERVER_TIME.getId());
    }

    public static void writeWhere(String userName) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.WHERE.getId());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeCreaturesInMap(short Map) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.CREATURES_IN_MAP.getId());

        outgoingData.writeInteger(Map);
    }

    public static void writeCreateItem(int itemIndex) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.CREATE_ITEM.getId());
        outgoingData.writeInteger((short) itemIndex);
    }

    public static void writeWarpMeToTarget() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.WARP_ME_TO_TARGET.getId());
    }

    public static void writeWarpChar(String userName, short map, int x, int y) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.WARP_CHAR.getId());

        outgoingData.writeASCIIString(userName);
        outgoingData.writeInteger(map);
        outgoingData.writeByte(x);
        outgoingData.writeByte(y);
    }

    public static void writeSilence(String userName) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.SILENCE.getId());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeSOSShowList() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.SOS_SHOW_LIST.getId());
    }

    public static void writeShowServerForm() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.SHOW_SERVER_FORM.getId());
    }

    public static void writeGoToChar(String userName) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.GO_TO_CHAR.getId());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeInvisible() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.INVISIBLE.getId());
    }

    public static void writeGMPanel() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.GM_PANEL.getId());
    }

    public static void writeWorking() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.WORKING.getId());
    }

    public static void writeHiding() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.HIDING.getId());
    }

    public static void writeJail(String userName, String reason, int time) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.JAIL.getId());

        outgoingData.writeASCIIString(userName);
        outgoingData.writeASCIIString(reason);
        outgoingData.writeByte(time);
    }

    public static void writeKillNPC() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.KILL_NPC.getId());
    }

    public static void writeWarnUser(String userName, String reason) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.WARN_USER.getId());

        outgoingData.writeASCIIString(userName);
        outgoingData.writeASCIIString(reason);
    }

    public static void writeEditChar(String userName, int editOption, String arg1, String arg2) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.EDIT_CHAR.getId());

        outgoingData.writeASCIIString(userName);

        outgoingData.writeByte(editOption);

        outgoingData.writeASCIIString(arg1);
        outgoingData.writeASCIIString(arg2);


    }

    public static void writeRequestCharInfo(String userName) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.REQUEST_CHAR_INFO.getId());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeRequestCharStats(String userName) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.REQUEST_CHAR_STATS.getId());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeRequestCharGold(String userName) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.REQUEST_CHAR_GOLD.getId());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeRequestCharInventory(String userName) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.REQUEST_CHAR_INVENTORY.getId());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeRequestCharBank(String userName) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.REQUEST_CHAR_BANK.getId());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeRequestCharSkills(String userName) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.REQUEST_CHAR_SKILLS.getId());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeReviveChar(String userName) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.REVIVE_CHAR.getId());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeOnlineGM() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.ONLINE_GM.getId());
    }

    public static void writeOnlineMap(short map) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.ONLINE_MAP.getId());

        outgoingData.writeInteger(map);
    }

    public static void writeForgive(String userName) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.FORGIVE.getId());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeKick(String userName) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.KICK.getId());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeExecute(String userName) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.EXECUTE.getId());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeBanChar(String userName, String reason) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.BAN_CHAR.getId());

        outgoingData.writeASCIIString(userName);
        outgoingData.writeASCIIString(reason);
    }

    public static void writeUnbanChar(String userName) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.UNBAN_CHAR.getId());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeNPCFollow() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.NPC_FOLLOW.getId());
    }

    public static void writeSummonChar() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.SUMMON_CHAR.getId());
    }

    public static void writeSpawnListRequest() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.SPAWN_LIST_REQUEST.getId());
    }

    public static void writeResetNPCInventory() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.RESET_NPC_INVENTORY.getId());
    }

    public static void writeCleanWorld() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.CLEAN_WORLD.getId());
    }

    public static void writeServerMessage() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.SERVER_MESSAGE.getId());
    }

    public static void writeNickToIP(String userName) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.NICK_TO_IP.getId());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeIPToNick(int[] ip) {
        // Validar que el tamaño del array sea 4 bytes
        if (ip.length != 4) return; // IP invalida

        // Escribir el mensaje "IPToNick" en el buffer de datos salientes
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.IP_TO_NICK.getId());

        // Escribir cada byte de la IP en el buffer de datos salientes
        for (int b : ip) {
            outgoingData.writeByte(b);
        }
    }

    public static void writeGuildOnlineMembers(String guild) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.GUILD_ONLINE_MEMBERS.getId());

        outgoingData.writeASCIIString(guild);
    }

    public static void writeTeleportCreate(short map, int x, int y, int radio) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.TELEPORT_CREATE.getId());

        outgoingData.writeInteger(map);
        outgoingData.writeByte(x);
        outgoingData.writeByte(y);
        outgoingData.writeByte(radio);
    }

    public static void writeTeleportDestroy() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.TELEPORT_DESTROY.getId());
    }

    public static void writeRainToggle() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.RAIN_TOGGLE.getId());
    }

    public static void writeSetCharDescription(String desc) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.SET_CHAR_DESCRIPTION.getId());

        outgoingData.writeASCIIString(desc);
    }

    public static void writeForceMIDIToMap(int midiID, short map) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.FORCE_MIDI_TO_MAP.getId());

        outgoingData.writeByte(midiID);
        outgoingData.writeInteger(map);
    }

    public static void writeForceWAVEToMap(int waveID, short map, int x, int y) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.FORCE_WAVE_TO_MAP.getId());

        outgoingData.writeByte(waveID);

        outgoingData.writeInteger(map);

        outgoingData.writeByte(x);
        outgoingData.writeByte(y);
    }

    public static void writeRoyaleArmyMessage(String message) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.ROYAL_ARMY_MESSAGE.getId());

        outgoingData.writeASCIIString(message);
    }

    public static void writeChaosLegionMessage(String message) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.CHAOS_LEGION_MESSAGE.getId());

        outgoingData.writeASCIIString(message);
    }

    public static void writeCitizenMessage(String message) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.CITIZEN_MESSAGE.getId());

        outgoingData.writeASCIIString(message);
    }

    public static void writeCriminalMessage(String message) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.CRIMINAL_MESSAGE.getId());

        outgoingData.writeASCIIString(message);
    }

    public static void writeTalkAsNPC(String message) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.TALK_AS_NPC.getId());

        outgoingData.writeASCIIString(message);
    }

    public static void writeDestroyAllItemsInArea() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.DESTROY_ALL_ITEMS_IN_AREA.getId());
    }

    public static void writeAcceptRoyalCouncilMember(String userName) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.ACCEPT_ROYAL_COUNCIL_MEMBER.getId());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeAcceptChaosCouncilMember(String userName) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.ACCEPT_CHAOS_COUNCIL_MEMBER.getId());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeItemsInTheFloor() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.ITEMS_IN_THE_FLOOR.getId());
    }

    public static void writeMakeDumb(String userName) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.MAKE_DUMB.getId());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeMakeDumbNoMore(String userName) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.MAKE_DUMB_NO_MORE.getId());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeDumpIPTables() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.DUMP_IP_TABLES.getId());
    }

    public static void writeCouncilKick(String userName) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.COUNCIL_KICK.getId());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeSetTrigger(int trigger) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.SET_TRIGGER.getId());

        outgoingData.writeByte(trigger);
    }

    public static void writeAskTrigger() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.ASK_TRIGGER.getId());
    }

    public static void writeBannedIPList() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.BANNED_IP_LIST.getId());
    }

    public static void writeBannedIPReload() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.BANNED_IP_RELOAD.getId());
    }

    public static void writeGuildMemberList(String guild) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.GUILD_MEMBER_LIST.getId());

        outgoingData.writeASCIIString(guild);
    }

    public static void writeGuildBan(String guild) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.GUILD_BAN.getId());

        outgoingData.writeASCIIString(guild);
    }

    public static void writeBanIP(boolean byIp, int[] ip, String nick, String reason) {
        if (byIp && ip.length != 4) return; // IP invalida

        // Escribir el mensaje "BanIP" en el buffer de datos salientes
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.BAN_IP.getId());

        // Escribir si es por IP o por nick
        outgoingData.writeBoolean(byIp);

        // Si es por IP, escribir los componentes de la IP
        if (byIp) {
            for (int b : ip) {
                outgoingData.writeByte(b);
            }
        } else {
            // Si es por nick, escribir el nick
            outgoingData.writeASCIIString(nick);
        }

        // Escribir el motivo del baneo
        outgoingData.writeASCIIString(reason);
    }

    public static void writeUnbanIP(int[] ip) {
        if (ip.length != 4) return; // IP invalida

        // Escribir el mensaje "UnbanIP" en el buffer de datos salientes
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.UNBAN_IP.getId());

        // Escribir los componentes de la IP
        for (int b : ip) {
            outgoingData.writeByte(b);
        }
    }

    public static void writeDestroyItems() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.DESTROY_ITEMS.getId());
    }

    public static void writeChaosLegionKick(String userName) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.CHAOS_LEGION_KICK.getId());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeRoyalArmyKick(String userName) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.ROYAL_ARMY_KICK.getId());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeForceMIDIAll(int midiID) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.FORCE_MIDI_ALL.getId());

        outgoingData.writeByte(midiID);
    }

    public static void writeForceWAVEAll(int waveID) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.FORCE_WAVE_ALL.getId());

        outgoingData.writeByte(waveID);
    }

    public static void writeRemovePunishment(String userName, int punishment, String newText) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.REMOVE_PUNISHMENT.getId());

        outgoingData.writeASCIIString(userName);
        outgoingData.writeByte(punishment);
        outgoingData.writeASCIIString(newText);
    }

    public static void writeTileBlockedToggle() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.TILE_BLOCKED_TOGGLE.getId());
    }

    public static void writeKillNPCNoRespawn() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.KILL_NPC_NO_RESPAWN.getId());
    }

    public static void writeKillAllNearbyNPCs() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.KILL_ALL_NEARBY_NPCS.getId());
    }

    public static void writeLastIP(String userName) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.LAST_IP.getId());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeChangeMOTD() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.CHANGE_MOTD.getId());
    }

    public static void writeSystemMessage(String message) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.SYSTEM_MESSAGE.getId());

        outgoingData.writeASCIIString(message);
    }

    public static void writeCreateNPC(Short NPCIndex) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.CREATE_NPC.getId());

        outgoingData.writeInteger(NPCIndex);
    }

    public static void writeCreateNPCWithRespawn(Short NPCIndex) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.CREATE_NPC_WITH_RESPAWN.getId());

        outgoingData.writeInteger(NPCIndex);
    }

    public static void writeImperialArmour(int armourIndex, short objectIndex) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.IMPERIAL_ARMOUR.getId());

        outgoingData.writeByte(armourIndex);
        outgoingData.writeInteger(objectIndex);
    }

    public static void writeChaosArmour(int armourIndex, short objectIndex) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.CHAOS_ARMOUR.getId());

        outgoingData.writeByte(armourIndex);
        outgoingData.writeInteger(objectIndex);
    }

    public static void writeNavigateToggle() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.NAVIGATE_TOGGLE.getId());
    }

    public static void writeServerOpenToUsersToggle() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.SERVER_OPEN_TO_USERS_TOGGLE.getId());
    }

    public static void writeTurnOffServer() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.TURN_OFF_SERVER.getId());
    }

    public static void writeTurnCriminal(String userName) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.TURN_CRIMINAL.getId());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeResetFactions(String userName) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.RESET_FACTIONS.getId());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeRemoveCharFromGuild(String userName) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.REMOVE_CHAR_FROM_GUILD.getId());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeRequestCharMail(String userName) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.REQUEST_CHAR_MAIL.getId());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeAlterPassword(String userName, String copyFrom) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.ALTER_PASSWORD.getId());

        outgoingData.writeASCIIString(userName);
        outgoingData.writeASCIIString(copyFrom);
    }

    public static void writeAlterMail(String userName, String newMail) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.ALTER_MAIL.getId());

        outgoingData.writeASCIIString(userName);
        outgoingData.writeASCIIString(newMail);
    }

    public static void writeAlterName(String userName, String newName) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.ALTER_NAME.getId());

        outgoingData.writeASCIIString(userName);
        outgoingData.writeASCIIString(newName);
    }

    public static void writeCheckSlot(String userName, int slot) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.CHECK_SLOT.getId());

        outgoingData.writeASCIIString(userName);
        outgoingData.writeByte(slot);
    }

    public static void writeToggleCentinelActivated() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.TOGGLE_CENTINEL_ACTIVATED.getId());
    }

    public static void writeDoBackup() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.DO_BACKUP.getId());
    }

    public static void writeShowGuildMessages(String guild) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.SHOW_GUILD_MESSAGES.getId());

        outgoingData.writeASCIIString(guild);
    }

    public static void writeSaveMap() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.SAVE_MAP.getId());
    }

    public static void writeChangeMapInfoPK(boolean isPK) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.CHANGE_MAP_INFO_PK.getId());

        outgoingData.writeBoolean(isPK);
    }

    public static void writeChangeMapInfoBackup(boolean backup) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.CHANGE_MAP_INFO_BACKUP.getId());

        outgoingData.writeBoolean(backup);
    }

    public static void writeChangeMapInfoRestricted(String restrict) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.CHANGE_MAP_INFO_RESTRICTED.getId());

        outgoingData.writeASCIIString(restrict);
    }

    public static void writeChangeMapInfoNoMagic(boolean noMagic) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.CHANGE_MAP_INFO_NO_MAGIC.getId());

        outgoingData.writeBoolean(noMagic);
    }

    public static void writeChangeMapInfoNoInvi(boolean noInvi) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.CHANGE_MAP_INFO_NO_INVI.getId());

        outgoingData.writeBoolean(noInvi);
    }

    public static void writeChangeMapInfoNoResu(boolean noResu) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.CHANGE_MAP_INFO_NO_RESU.getId());

        outgoingData.writeBoolean(noResu);
    }

    public static void writeChangeMapInfoLand(String land) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.CHANGE_MAP_INFO_LAND.getId());

        outgoingData.writeASCIIString(land);
    }

    public static void writeChangeMapInfoZone(String zone) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.CHANGE_MAP_INFO_ZONE.getId());

        outgoingData.writeASCIIString(zone);
    }

    public static void writeSaveChars() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.SAVE_CHARS.getId());
    }

    public static void writeCleanSOS() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.CLEAN_SOS.getId());
    }

    public static void writeNight() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.NIGHT.getId());
    }

    public static void writeKickAllChars() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.KICK_ALL_CHARS.getId());
    }

    public static void writeReloadNPCs() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.RELOAD_NPCS.getId());
    }

    public static void writeReloadServerIni() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.RELOAD_SERVER_INI.getId());
    }

    public static void writeReloadSpells() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.RELOAD_SPELLS.getId());
    }

    public static void writeReloadObjects() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.RELOAD_OBJECTS.getId());
    }

    public static void writeRestart() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.RESTART.getId());
    }

    public static void writeResetAutoUpdate() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.RESET_AUTO_UPDATE.getId());
    }

    public static void writeChatColor(int r, int g, int b) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.CHAT_COLOR.getId());

        outgoingData.writeByte(r);
        outgoingData.writeByte(g);
        outgoingData.writeByte(b);
    }

    public static void writeIgnored() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.IGNORED.getId());
    }

    public static void writePing() {
        if (pingTime != 0) return;
        outgoingData.writeByte(ClientPacket.PING.getId());
        pingTime = (int) glfwGetTime();
    }

    public static void writeSetIniVar(String sLlave, String sClave, String sValor) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.getId());
        outgoingData.writeByte(GMCommand.SET_INI_VAR.getId());

        outgoingData.writeASCIIString(sLlave);
        outgoingData.writeASCIIString(sClave);
        outgoingData.writeASCIIString(sValor);
    }

    public static void writeHome() {
        outgoingData.writeByte(ClientPacket.HOME.getId());
    }

    public static void writeCommerceBuy(int slot, int amount) {
        outgoingData.writeByte(ClientPacket.COMMERCE_BUY.getId());
        outgoingData.writeByte(slot);
        outgoingData.writeInteger((short) amount);
    }

    public static void writeCommerceSell(int slot, int amount) {
        outgoingData.writeByte(ClientPacket.COMMERCE_SELL.getId());
        outgoingData.writeByte(slot);
        outgoingData.writeInteger((short) amount);
    }

}
