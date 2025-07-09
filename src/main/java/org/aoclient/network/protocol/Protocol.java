package org.aoclient.network.protocol;

import org.aoclient.engine.game.Console;
import org.aoclient.engine.game.User;
import org.aoclient.engine.game.models.Direction;
import org.aoclient.engine.game.models.Skill;
import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.PacketBuffer;
import org.aoclient.network.SocketConnection;
import org.aoclient.network.protocol.types.GMCommand;

import java.nio.charset.StandardCharsets;

import static org.aoclient.engine.utils.GameData.charList;
import static org.lwjgl.glfw.GLFW.glfwGetTime;

/**
 * Maneja el protocolo de comunicacion entre el cliente y el servidor.
 * <p>
 * Implementa las operaciones necesarias para la comunicacion de red, procesando los paquetes entrantes desde el servidor y
 * preparando los paquetes salientes hacia el servidor.
 * <p>
 * Esta clase contiene implementaciones para todos los comandos del protocolo del juego, incluyendo acciones de personaje,
 * interacciones con el entorno, comunicacion con otros jugadores y comandos administrativos.
 * <p>
 * Trabaja en conjunto con {@link SocketConnection} para la transmision real de los datos, y utiliza {@link PacketBuffer} para
 * almacenar temporalmente los bytes entrantes y salientes antes de su manejo.
 * <p>
 * TODO Los nombres de los metodos tienen que coincidir con los nombres de los comandos
 */

public class Protocol {

    private static final Console CONSOLE = Console.INSTANCE;
    private static final User USER = User.INSTANCE;
    private static final PacketReceiver PACKET_RECEIVER = new PacketReceiver();
    public static int pingTime;
    /** Buffer para la salida de bytes (escribe lo que envia el cliente al servidor). */
    public static PacketBuffer outputBuffer = new PacketBuffer();
    /** Buffer para la entrada de bytes (lee lo que recibe el cliente del servidor). */
    public static PacketBuffer inputBuffer = new PacketBuffer();

    /**
     * Delega el manejo de los bytes del buffer de entrada a PacketReceiver.
     */
    public static void handleIncomingBytes() {
        PACKET_RECEIVER.handleIncomingBytes(inputBuffer);
    }

    /**
     * Escribe en el buffer de salida los bytes del cliente. En otras palabras, construye un paquete para la accion de logearse.
     *
     * @param username nombre de usuario
     * @param password contraseña
     */
    public static void loginExistingChar(String username, String password) {
        // Primero escribe el ID del paquete para que al momento de deserializar el paquete entrante, lea primero el ID del paquete
        outputBuffer.writeByte(ClientPacket.LOGIN_EXISTING_CHAR.getId());
        outputBuffer.writeCp1252String(username);
        outputBuffer.writeCp1252String(password);
        outputBuffer.writeByte(0); // App.Major ?
        outputBuffer.writeByte(13); // App.Minor ?
        outputBuffer.writeByte(0); // App.Revision ?
    }

    public static void throwDices() {
        outputBuffer.writeByte(ClientPacket.THROW_DICES.getId());
    }

    public static void loginNewChar(String userName, String userPassword, int userRaza, int userSexo, int userClase, int userHead, String userEmail, int userHogar) {
        outputBuffer.writeByte(ClientPacket.LOGIN_NEW_CHAR.getId());
        outputBuffer.writeCp1252String(userName);
        outputBuffer.writeCp1252String(userPassword);

        outputBuffer.writeByte(0);  // App.Major
        outputBuffer.writeByte(13); // App.Minor
        outputBuffer.writeByte(0);  // App.Revision

        outputBuffer.writeByte(userRaza);
        outputBuffer.writeByte(userSexo);
        outputBuffer.writeByte(userClase);
        outputBuffer.writeInteger((short) userHead); // Convierte el valor int (32 bits) a short (16 bits) descartando los 16 bits mas significativos para ser compatible con el tipo Integer de VB6 (2 bytes)


        outputBuffer.writeCp1252String(userEmail);
        outputBuffer.writeByte(userHogar);
    }

    /**
     * <p>
     * Escribe el paquete TALK para que sea procesado desde el servidor en el codigo de VB6 desde {@code Protocol.bas} dentro del
     * metodo {@code HandleTalk(ByVal UserIndex As Integer)}. Este metodo es llamado cuando el servidor recibe un paquete con ID 3
     * (TALK). Luego, el metodo {@code PrepareMessageChatOverHead} construye el paquete {@code CHAT_OVER_HEAD} que sera enviado a
     * los clientes:
     * <pre>{@code
     * Public Function PrepareMessageChatOverHead(ByVal Chat As String, ByVal CharIndex As Integer, ByVal color As Long) As String
     * }</pre>
     * Observa que esta funcion requiere parametros que no vienen en el paquete TALK original:
     * <ul>
     *   <li>El mensaje (Chat) - Este si viene del cliente
     *   <li>El indice del personaje (CharIndex) - Este lo determina el servidor
     *   <li>El color (color) - Este lo determina el servidor
     * </ul>
     * <h2>Flujo completo del proceso</h2>
     * <p>
     * Cuando alguien habla en el juego:
     * <ol>
     * <li><b>Cliente Java envia un paquete TALK simple</b>:
     * <pre>{@code
     * public static void writeTalk(String chat) {
     *     outputBuffer.writeByte(ClientPacket.TALK.getId());
     *     outputBuffer.writeCp1252String(chat);
     * }
     * }</pre>
     * <li><b>El servidor VB6 procesa el paquete en {@code HandleTalk}</b>:
     * <ul>
     *   <li>Lee el texto del mensaje
     *   <li>Obtiene el CharIndex del personaje que habla
     *   <li>Determina el color del texto basado en atributos del jugador
     *   <li>Llama a {@code PrepareMessageChatOverHead} para crear un nuevo paquete
     *   <li>Envia ese paquete a todos los jugadores relevantes
     * </ul>
     * <li><b>El cliente Java recibe y procesa un paquete CHAT_OVER_HEAD completo</b>:
     * <pre>{@code
     * // En ChatOverHeadHandler.java
     * String chat = tempBuffer.readCp1252String(); // El cliente añadio esto!
     * short charIndex = tempBuffer.readInteger(); // El servidor desde el codigo de VB6 añadio esto!
     * int r = tempBuffer.readByte(); // El servidor desde el codigo de VB6 añadio esto!
     * int g = tempBuffer.readByte(); // El servidor desde el codigo de VB6 añadio esto!
     * int b = tempBuffer.readByte(); // El servidor desde el codigo de VB6 añadio esto!
     * }</pre>
     * </ol>
     * <p>
     * Ver: communication-protocol.md
     */
    public static void talk(String chat) {
        outputBuffer.writeByte(ClientPacket.TALK.getId());
        outputBuffer.writeCp1252String(chat);
    }

    public static void whisper(short charIndex, String chat) {
        outputBuffer.writeByte(ClientPacket.WHISPER.getId());
        outputBuffer.writeInteger(charIndex);
        outputBuffer.writeCp1252String(chat);
    }

    public static void walk(Direction direction) {
        outputBuffer.writeByte(ClientPacket.WALK.getId());
        outputBuffer.writeByte(direction.getId());
    }

    public static void drop(int slot, int amount) {
        outputBuffer.writeByte(ClientPacket.DROP.getId());
        outputBuffer.writeByte((byte) slot);
        outputBuffer.writeInteger((short) amount);
    }

    public static void requestPositionUpdate() {
        outputBuffer.writeByte(ClientPacket.REQUEST_POSITION_UPDATE.getId());
    }

    public static void attack() {
        outputBuffer.writeByte(ClientPacket.ATTACK.getId());
    }

    public static void pickUp() {
        outputBuffer.writeByte(ClientPacket.PICK_UP.getId());
    }

    public static void safeToggle() {
        outputBuffer.writeByte(ClientPacket.SAFE_TOGGLE.getId());
    }

    public static void resucitationToggle() {
        outputBuffer.writeByte(ClientPacket.RESUSCITATION_SAFE_TOGGLE.getId());
    }

    public static void requestGuildLeaderInfo() {
        outputBuffer.writeByte(ClientPacket.REQUEST_GUILD_LEADER_INFO.getId());
    }

    public static void requestAttributes() {
        outputBuffer.writeByte(ClientPacket.REQUEST_ATTRIBUTES.getId());
    }

    public static void requestFame() {
        outputBuffer.writeByte(ClientPacket.REQUEST_FAME.getId());
    }

    public static void requestSkills() {
        outputBuffer.writeByte(ClientPacket.REQUEST_SKILLS.getId());
    }

    public static void requestMiniStats() {
        outputBuffer.writeByte(ClientPacket.REQUEST_MINI_STATS.getId());
    }

    public static void changeHeading(Direction direction) {
        outputBuffer.writeByte(ClientPacket.CHANGE_HEADING.getId());
        outputBuffer.writeByte(direction.getId());
    }

    public static void modifySkills(int[] skills) {
        outputBuffer.writeByte(ClientPacket.MODIFY_SKILLS.getId());
        for (Skill skill : Skill.values())
            outputBuffer.writeByte(skills[skill.getId() - 1]);
    }

    public static void leftClick(int x, int y) {
        outputBuffer.writeByte(ClientPacket.LEFT_CLICK.getId());
        outputBuffer.writeByte(x);
        outputBuffer.writeByte(y);
    }

    public static void workLeftClick(int x, int y, int skill) {
        outputBuffer.writeByte(ClientPacket.WORK_LEFT_CLICK.getId());
        outputBuffer.writeByte(x);
        outputBuffer.writeByte(y);
        outputBuffer.writeByte(skill);
    }

    public static void doubleClick(int x, int y) {
        outputBuffer.writeByte(ClientPacket.DOUBLE_CLICK.getId());
        outputBuffer.writeByte(x);
        outputBuffer.writeByte(y);
    }

    public static void useItem(int slot) {
        outputBuffer.writeByte(ClientPacket.USE_ITEM.getId());
        outputBuffer.writeByte(slot);
    }

    public static void equipItem(int slot) {
        outputBuffer.writeByte(ClientPacket.EQUIP_ITEM.getId());
        outputBuffer.writeByte(slot);
    }

    public static void work(int skill) {
        if (USER.isDead()) {
            CONSOLE.addMsgToConsole(new String("¡¡Estas muerto!!".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outputBuffer.writeByte(ClientPacket.WORK.getId());
        outputBuffer.writeByte(skill);
    }

    public static void castSpell(int slot) {
        outputBuffer.writeByte(ClientPacket.CAST_SPELL.getId());
        outputBuffer.writeByte(slot);
    }

    public static void quit() {
        if (charList[USER.getUserCharIndex()].isParalizado()) {
            CONSOLE.addMsgToConsole(new String("No puedes salir estando paralizado.".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outputBuffer.writeByte(ClientPacket.QUIT.getId());
    }

    public static void spellInfo(final int slot) {
        outputBuffer.writeByte(ClientPacket.SPELL_INFO.getId());
        outputBuffer.writeByte(slot);
    }

    public static void commerceEnd() {
        outputBuffer.writeByte(ClientPacket.COMMERCE_END.getId());
    }

    public static void bankEnd() {
        outputBuffer.writeByte(ClientPacket.BANK_END.getId());
    }

    public static void changePassword(String oldPass, String newPass) {
        outputBuffer.writeByte(ClientPacket.CHANGE_PASSWORD.getId());
        outputBuffer.writeCp1252String(oldPass);
        outputBuffer.writeCp1252String(newPass);
    }

    public static void online() {
        outputBuffer.writeByte(ClientPacket.ONLINE.getId());
    }

    public static void meditate() {

        if (USER.getUserMaxMAN() == USER.getUserMinMAN()) {
            return;
        }

        if (charList[USER.getUserCharIndex()].isDead()) {
            CONSOLE.addMsgToConsole(new String("¡Estas muerto!".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outputBuffer.writeByte(ClientPacket.MEDITATE.getId());
    }

    public static void commerceStart() {
        outputBuffer.writeByte(ClientPacket.COMMERCE_START.getId());
    }

    public static void bankStart() {
        outputBuffer.writeByte(ClientPacket.BANK_START.getId());
    }

    public static void guildLeave() {
        outputBuffer.writeByte(ClientPacket.GUILD_LEAVE.getId());
    }

    public static void requestAccountState() {
        if (charList[USER.getUserCharIndex()].isDead()) {
            CONSOLE.addMsgToConsole(new String("¡Estas muerto!".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outputBuffer.writeByte(ClientPacket.REQUEST_ACCOUNT_STATE.getId());
    }

    public static void petStand() {
        if (charList[USER.getUserCharIndex()].isDead()) {
            CONSOLE.addMsgToConsole(new String("¡Estas muerto!".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outputBuffer.writeByte(ClientPacket.PET_STAND.getId());
    }

    public static void petFollow() {
        if (charList[USER.getUserCharIndex()].isDead()) {
            CONSOLE.addMsgToConsole(new String("¡Estas muerto!".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outputBuffer.writeByte(ClientPacket.PET_FOLLOW.getId());
    }

    public static void releasePet() {
        if (charList[USER.getUserCharIndex()].isDead()) {
            CONSOLE.addMsgToConsole(new String("¡Estas muerto!".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outputBuffer.writeByte(ClientPacket.RELEASE_PET.getId());
    }

    public static void trainList() {
        if (charList[USER.getUserCharIndex()].isDead()) {
            CONSOLE.addMsgToConsole(new String("¡Estas muerto!".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outputBuffer.writeByte(ClientPacket.TRAIN_LIST.getId());
    }

    public static void rest() {
        if (charList[USER.getUserCharIndex()].isDead()) {
            CONSOLE.addMsgToConsole(new String("¡Estas muerto!".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outputBuffer.writeByte(ClientPacket.REST.getId());
    }

    public static void consultation() {
        outputBuffer.writeByte(ClientPacket.CONSULTA.getId());
    }

    public static void resucitate() {
        outputBuffer.writeByte(ClientPacket.RESUCITATE.getId());
    }

    public static void heal() {
        outputBuffer.writeByte(ClientPacket.HEAL.getId());
    }

    public static void requestStats() {
        outputBuffer.writeByte(ClientPacket.REQUEST_STATS.getId());
    }

    public static void help() {
        outputBuffer.writeByte(ClientPacket.HELP.getId());
    }

    public static void enlist() {
        outputBuffer.writeByte(ClientPacket.ENLIST.getId());
    }

    public static void information() {
        outputBuffer.writeByte(ClientPacket.INFORMATION.getId());
    }

    public static void reward() {
        outputBuffer.writeByte(ClientPacket.REWARD.getId());
    }

    public static void requestMOTD() {
        outputBuffer.writeByte(ClientPacket.REQUEST_MOTD.getId());
    }

    public static void upTime() {
        outputBuffer.writeByte(ClientPacket.UPTIME.getId());
    }

    public static void partyLeave() {
        outputBuffer.writeByte(ClientPacket.PARTY_LEAVE.getId());
    }

    public static void partyCreate() {
        if (charList[USER.getUserCharIndex()].isDead()) {
            CONSOLE.addMsgToConsole(new String("¡Estas muerto!".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outputBuffer.writeByte(ClientPacket.PARTY_CREATE.getId());
    }

    public static void partyJoin() {
        if (charList[USER.getUserCharIndex()].isDead()) {
            CONSOLE.addMsgToConsole(new String("¡Estas muerto!".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outputBuffer.writeByte(ClientPacket.PARTY_JOIN.getId());
    }

    public static void shareNpc() {
        if (charList[USER.getUserCharIndex()].isDead()) {
            CONSOLE.addMsgToConsole(new String("¡Estas muerto!".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outputBuffer.writeByte(ClientPacket.SHARE_NPC.getId());
    }

    public static void stopSharingNpc() {
        if (charList[USER.getUserCharIndex()].isDead()) {
            CONSOLE.addMsgToConsole(new String("¡Estas muerto!".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outputBuffer.writeByte(ClientPacket.STOP_SHARING_NPC.getId());
    }

    public static void inquiry() {
        outputBuffer.writeByte(ClientPacket.INQUIRY.getId());
    }

    public static void inquiryVote(int op) {
        outputBuffer.writeByte(ClientPacket.INQUIRY_VOTE.getId());
        outputBuffer.writeByte(op);
    }

    public static void guildMessage(String message) {
        outputBuffer.writeByte(ClientPacket.GUILD_MESSAGE.getId());
        outputBuffer.writeCp1252String(message);
    }

    public static void partyMessage(String message) {
        outputBuffer.writeByte(ClientPacket.PARTY_MESSAGE.getId());
        outputBuffer.writeCp1252String(message);
    }

    public static void centinelReport(int number) {
        outputBuffer.writeByte(ClientPacket.CENTINEL_REPORT.getId());
        outputBuffer.writeByte(number);
    }

    public static void guildOnline() {
        outputBuffer.writeByte(ClientPacket.GUILD_ONLINE.getId());
    }

    public static void partyOnline() {
        outputBuffer.writeByte(ClientPacket.GUILD_ONLINE.getId());
    }

    public static void councilMessage(String message) {
        outputBuffer.writeByte(ClientPacket.COUNCIL_MESSAGE.getId());
        outputBuffer.writeCp1252String(message);
    }

    public static void roleMasterRequest(String message) {
        outputBuffer.writeByte(ClientPacket.ROLE_MASTER_REQUEST.getId());
        outputBuffer.writeCp1252String(message);
    }

    public static void GMRequest() {
        outputBuffer.writeByte(ClientPacket.GM_REQUEST.getId());
    }

    public static void bugReport(String message) {
        outputBuffer.writeByte(ClientPacket.BUG_REPORT.getId());
        outputBuffer.writeCp1252String(message);
    }

    public static void changeDescription(String message) {
        outputBuffer.writeByte(ClientPacket.CHANGE_DESCRIPTION.getId());
        outputBuffer.writeCp1252String(message);
    }

    public static void guildVote(String message) {
        outputBuffer.writeByte(ClientPacket.GUILD_VOTE.getId());
        outputBuffer.writeCp1252String(message);
    }

    public static void punishments(String message) {
        outputBuffer.writeByte(ClientPacket.PUNISHMENTS.getId());
        outputBuffer.writeCp1252String(message);
    }

    public static void gamble(short amount) {
        outputBuffer.writeByte(ClientPacket.GAMBLE.getId());
        outputBuffer.writeInteger(amount);
    }

    public static void leaveFaction() {
        outputBuffer.writeByte(ClientPacket.LEAVE_FACTION.getId());
    }

    public static void bankExtractGold(int amount) {
        outputBuffer.writeByte(ClientPacket.BANK_EXTRACT_GOLD.getId());
        outputBuffer.writeLong(amount);
    }

    public static void bankDepositGold(int amount) {
        outputBuffer.writeByte(ClientPacket.BANK_DEPOSIT_GOLD.getId());
        outputBuffer.writeLong(amount);
    }

    public static void denounce(String message) {
        outputBuffer.writeByte(ClientPacket.DENOUNCE.getId());
        outputBuffer.writeCp1252String(message);
    }

    public static void guildFundate() {
        outputBuffer.writeByte(ClientPacket.GUILD_FUNDATE.getId());
    }

    public static void guildFundation(int clanType) {
        outputBuffer.writeByte(ClientPacket.GUILD_FUNDATION.getId());
        outputBuffer.writeByte(clanType);
    }

    public static void partyKick(String userName) {
        outputBuffer.writeByte(ClientPacket.PARTY_KICK.getId());
        outputBuffer.writeCp1252String(userName);
    }

    public static void partySetLeader(String userName) {
        outputBuffer.writeByte(ClientPacket.PARTY_SET_LEADER.getId());
        outputBuffer.writeCp1252String(userName);
    }

    public static void partyAcceptMember(String userName) {
        outputBuffer.writeByte(ClientPacket.PARTY_ACCEPT_MEMBER.getId());
        outputBuffer.writeCp1252String(userName);
    }

     /* ##############################################
        #              COMANDOS DE GM                #
        ##############################################*/

    public static void GMMessage(String message) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.GM_MESSAGE.getId());

        outputBuffer.writeCp1252String(message);
    }

    public static void showName() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.SHOW_NAME.getId());
    }

    public static void onlineRoyalArmy() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.ONLINE_ROYAL_ARMY.getId());
    }

    public static void onlineChaosLegion() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.ONLINE_CHAOS_LEGION.getId());
    }

    public static void goNearby(String userName) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.GO_NEARBY.getId());

        outputBuffer.writeCp1252String(userName);
    }

    public static void comment(String message) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.COMMENT.getId());

        outputBuffer.writeCp1252String(message);
    }

    public static void serverTime() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.SERVER_TIME.getId());
    }

    public static void where(String userName) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.WHERE.getId());

        outputBuffer.writeCp1252String(userName);
    }

    public static void creaturesInMap(short Map) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CREATURES_IN_MAP.getId());

        outputBuffer.writeInteger(Map);
    }

    public static void createObject(int objId) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CREATE_OBJECT.getId());
        outputBuffer.writeInteger((short) objId);
    }

    public static void warpMeToTarget() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.WARP_ME_TO_TARGET.getId());
    }

    public static void warpChar(String userName, short map, int x, int y) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.WARP_CHAR.getId());

        outputBuffer.writeCp1252String(userName);
        outputBuffer.writeInteger(map);
        outputBuffer.writeByte(x);
        outputBuffer.writeByte(y);
    }

    public static void silence(String userName) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.SILENCE.getId());

        outputBuffer.writeCp1252String(userName);
    }

    public static void SOSShowList() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.SOS_SHOW_LIST.getId());
    }

    public static void showServerForm() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.SHOW_SERVER_FORM.getId());
    }

    public static void goToChar(String userName) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.GO_TO_CHAR.getId());

        outputBuffer.writeCp1252String(userName);
    }

    public static void invisible() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.INVISIBLE.getId());
    }

    public static void GMPanel() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.GM_PANEL.getId());
    }

    public static void working() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.WORKING.getId());
    }

    public static void hiding() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.HIDING.getId());
    }

    public static void jail(String userName, String reason, int time) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.JAIL.getId());

        outputBuffer.writeCp1252String(userName);
        outputBuffer.writeCp1252String(reason);
        outputBuffer.writeByte(time);
    }

    public static void killNPC() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.KILL_NPC.getId());
    }

    public static void warnUser(String userName, String reason) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.WARN_USER.getId());

        outputBuffer.writeCp1252String(userName);
        outputBuffer.writeCp1252String(reason);
    }

    public static void editChar(String userName, int editOption, String arg1, String arg2) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.EDIT_CHAR.getId());

        outputBuffer.writeCp1252String(userName);

        outputBuffer.writeByte(editOption);

        outputBuffer.writeCp1252String(arg1);
        outputBuffer.writeCp1252String(arg2);


    }

    public static void requestCharInfo(String userName) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.REQUEST_CHAR_INFO.getId());

        outputBuffer.writeCp1252String(userName);
    }

    public static void requestCharStats(String userName) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.REQUEST_CHAR_STATS.getId());

        outputBuffer.writeCp1252String(userName);
    }

    public static void requestCharGold(String userName) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.REQUEST_CHAR_GOLD.getId());

        outputBuffer.writeCp1252String(userName);
    }

    public static void requestCharInventory(String userName) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.REQUEST_CHAR_INVENTORY.getId());

        outputBuffer.writeCp1252String(userName);
    }

    public static void requestCharBank(String userName) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.REQUEST_CHAR_BANK.getId());

        outputBuffer.writeCp1252String(userName);
    }

    public static void requestCharSkills(String userName) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.REQUEST_CHAR_SKILLS.getId());

        outputBuffer.writeCp1252String(userName);
    }

    public static void reviveChar(String userName) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.REVIVE_CHAR.getId());

        outputBuffer.writeCp1252String(userName);
    }

    public static void onlineGM() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.ONLINE_GM.getId());
    }

    public static void onlineMap(short map) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.ONLINE_MAP.getId());
        outputBuffer.writeInteger(map);
    }

    public static void forgive(String userName) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.FORGIVE.getId());

        outputBuffer.writeCp1252String(userName);
    }

    public static void kick(String userName) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.KICK.getId());

        outputBuffer.writeCp1252String(userName);
    }

    public static void execute(String userName) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.EXECUTE.getId());

        outputBuffer.writeCp1252String(userName);
    }

    public static void banChar(String userName, String reason) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.BAN_CHAR.getId());

        outputBuffer.writeCp1252String(userName);
        outputBuffer.writeCp1252String(reason);
    }

    public static void unbanChar(String userName) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.UNBAN_CHAR.getId());

        outputBuffer.writeCp1252String(userName);
    }

    public static void NPCFollow() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.NPC_FOLLOW.getId());
    }

    public static void summonChar(String userName) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.SUMMON_CHAR.getId());
        outputBuffer.writeCp1252String(userName);
    }

    public static void spawnListRequest() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.SPAWN_LIST_REQUEST.getId());
    }

    public static void spawnCreature(short creatureIndex) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.SPAWN_CREATURE.getId());
        outputBuffer.writeInteger(creatureIndex);
    }

    public static void resetNPCInventory() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.RESET_NPC_INVENTORY.getId());
    }

    public static void cleanWorld() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CLEAN_WORLD.getId());
    }

    public static void serverMessage() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.SERVER_MESSAGE.getId());
    }

    public static void nickToIP(String userName) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.NICK_TO_IP.getId());

        outputBuffer.writeCp1252String(userName);
    }

    public static void IPToNick(int[] ip) {
        // Validar que el tamaño del array sea 4 bytes
        if (ip.length != 4) return; // IP invalida

        // Escribir el mensaje "IPToNick" en el buffer de datos salientes
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.IP_TO_NICK.getId());

        // Escribir cada byte de la IP en el buffer de datos salientes
        for (int b : ip) {
            outputBuffer.writeByte(b);
        }
    }

    public static void guildOnlineMembers(String guild) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.GUILD_ONLINE_MEMBERS.getId());
        outputBuffer.writeCp1252String(guild);
    }

    public static void teleportCreate(short map, int x, int y, int radio) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.TELEPORT_CREATE.getId());
        outputBuffer.writeInteger(map);
        outputBuffer.writeByte(x);
        outputBuffer.writeByte(y);
        outputBuffer.writeByte(radio);
    }

    public static void teleportDestroy() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.TELEPORT_DESTROY.getId());
    }

    public static void rainToggle() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.RAIN_TOGGLE.getId());
    }

    public static void setCharDescription(String desc) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.SET_CHAR_DESCRIPTION.getId());
        outputBuffer.writeCp1252String(desc);
    }

    public static void playMusic(int musicId) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.FORCE_MIDI_ALL.getId());
        outputBuffer.writeByte(musicId);
    }

    public static void playMusicOnMap(int musicId, short map) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.FORCE_MIDI_TO_MAP.getId());
        outputBuffer.writeByte(musicId);
        outputBuffer.writeInteger(map);
    }

    public static void playSound(int idSound) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.FORCE_WAVE_ALL.getId());
        outputBuffer.writeByte(idSound);
    }

    public static void playSoundAtTheSpecifiedLocation(int soundId, short map, int x, int y) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.FORCE_WAVE_TO_MAP.getId());
        outputBuffer.writeByte(soundId);
        outputBuffer.writeInteger(map);
        outputBuffer.writeByte(x);
        outputBuffer.writeByte(y);
    }

    public static void royaleArmyMessage(String message) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.ROYAL_ARMY_MESSAGE.getId());

        outputBuffer.writeCp1252String(message);
    }

    public static void chaosLegionMessage(String message) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CHAOS_LEGION_MESSAGE.getId());

        outputBuffer.writeCp1252String(message);
    }

    public static void citizenMessage(String message) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CITIZEN_MESSAGE.getId());

        outputBuffer.writeCp1252String(message);
    }

    public static void criminalMessage(String message) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CRIMINAL_MESSAGE.getId());

        outputBuffer.writeCp1252String(message);
    }

    public static void talkAsNPC(String message) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.TALK_AS_NPC.getId());

        outputBuffer.writeCp1252String(message);
    }

    public static void destroyAllItemsInArea() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.DESTROY_ALL_ITEMS_IN_AREA.getId());
    }

    public static void acceptRoyalCouncilMember(String userName) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.ACCEPT_ROYAL_COUNCIL_MEMBER.getId());

        outputBuffer.writeCp1252String(userName);
    }

    public static void acceptChaosCouncilMember(String userName) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.ACCEPT_CHAOS_COUNCIL_MEMBER.getId());

        outputBuffer.writeCp1252String(userName);
    }

    public static void itemsInTheFloor() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.ITEMS_IN_THE_FLOOR.getId());
    }

    public static void makeDumb(String userName) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.MAKE_DUMB.getId());

        outputBuffer.writeCp1252String(userName);
    }

    public static void makeDumbNoMore(String userName) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.MAKE_DUMB_NO_MORE.getId());

        outputBuffer.writeCp1252String(userName);
    }

    public static void dumpIPTables() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.DUMP_IP_TABLES.getId());
    }

    public static void councilKick(String userName) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.COUNCIL_KICK.getId());

        outputBuffer.writeCp1252String(userName);
    }

    public static void setTrigger(int trigger) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.SET_TRIGGER.getId());

        outputBuffer.writeByte(trigger);
    }

    public static void askTrigger() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.ASK_TRIGGER.getId());
    }

    public static void bannedIPList() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.BANNED_IP_LIST.getId());
    }

    public static void bannedIPReload() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.BANNED_IP_RELOAD.getId());
    }

    public static void guildMemberList(String guild) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.GUILD_MEMBER_LIST.getId());
        outputBuffer.writeCp1252String(guild);
    }

    public static void guildBan(String guild) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.GUILD_BAN.getId());
        outputBuffer.writeCp1252String(guild);
    }

    public static void banIP(boolean byIp, int[] ip, String nick, String reason) {
        if (byIp && ip.length != 4) return; // IP invalida

        // Escribir el mensaje "BanIP" en el buffer de datos salientes
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.BAN_IP.getId());

        // Escribir si es por IP o por nick
        outputBuffer.writeBoolean(byIp);

        // Si es por IP, escribir los componentes de la IP
        if (byIp) {
            for (int b : ip) {
                outputBuffer.writeByte(b);
            }
        } else {
            // Si es por nick, escribir el nick
            outputBuffer.writeCp1252String(nick);
        }

        // Escribir el motivo del baneo
        outputBuffer.writeCp1252String(reason);
    }

    public static void unbanIP(int[] ip) {
        if (ip.length != 4) return; // IP invalida

        // Escribir el mensaje "UnbanIP" en el buffer de datos salientes
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.UNBAN_IP.getId());

        // Escribir los componentes de la IP
        for (int b : ip) {
            outputBuffer.writeByte(b);
        }
    }

    public static void destroyItems() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.DESTROY_ITEMS.getId());
    }

    public static void chaosLegionKick(String userName) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CHAOS_LEGION_KICK.getId());

        outputBuffer.writeCp1252String(userName);
    }

    public static void royalArmyKick(String userName) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.ROYAL_ARMY_KICK.getId());

        outputBuffer.writeCp1252String(userName);
    }

    public static void removePunishment(String userName, int punishment, String newText) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.REMOVE_PUNISHMENT.getId());

        outputBuffer.writeCp1252String(userName);
        outputBuffer.writeByte(punishment);
        outputBuffer.writeCp1252String(newText);
    }

    public static void tileBlockedToggle() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.TILE_BLOCKED_TOGGLE.getId());
    }

    public static void killNPCNoRespawn() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.KILL_NPC_NO_RESPAWN.getId());
    }

    public static void killAllNearbyNPCs() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.KILL_ALL_NEARBY_NPCS.getId());
    }

    public static void lastIP(String userName) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.LAST_IP.getId());

        outputBuffer.writeCp1252String(userName);
    }

    public static void changeMOTD() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CHANGE_MOTD.getId());
    }

    public static void systemMessage(String message) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.SYSTEM_MESSAGE.getId());

        outputBuffer.writeCp1252String(message);
    }

    public static void createNPC(Short NPCIndex) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CREATE_NPC.getId());

        outputBuffer.writeInteger(NPCIndex);
    }

    public static void createNPCWithRespawn(Short NPCIndex) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CREATE_NPC_WITH_RESPAWN.getId());

        outputBuffer.writeInteger(NPCIndex);
    }

    public static void imperialArmour(int armourIndex, short objectIndex) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.IMPERIAL_ARMOUR.getId());

        outputBuffer.writeByte(armourIndex);
        outputBuffer.writeInteger(objectIndex);
    }

    public static void chaosArmour(int armourIndex, short objectIndex) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CHAOS_ARMOUR.getId());
        outputBuffer.writeByte(armourIndex);
        outputBuffer.writeInteger(objectIndex);
    }

    public static void navigateToggle() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.NAVIGATE_TOGGLE.getId());
    }

    public static void serverOpenToUsersToggle() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.SERVER_OPEN_TO_USERS_TOGGLE.getId());
    }

    public static void turnOffServer() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.TURN_OFF_SERVER.getId());
    }

    public static void turnCriminal(String userName) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.TURN_CRIMINAL.getId());

        outputBuffer.writeCp1252String(userName);
    }

    public static void resetFactions(String userName) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.RESET_FACTIONS.getId());
        outputBuffer.writeCp1252String(userName);
    }

    public static void removeCharFromGuild(String userName) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.REMOVE_CHAR_FROM_GUILD.getId());
        outputBuffer.writeCp1252String(userName);
    }

    public static void requestCharMail(String userName) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.REQUEST_CHAR_MAIL.getId());

        outputBuffer.writeCp1252String(userName);
    }

    public static void alterPassword(String userName, String copyFrom) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.ALTER_PASSWORD.getId());

        outputBuffer.writeCp1252String(userName);
        outputBuffer.writeCp1252String(copyFrom);
    }

    public static void alterMail(String userName, String newMail) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.ALTER_MAIL.getId());

        outputBuffer.writeCp1252String(userName);
        outputBuffer.writeCp1252String(newMail);
    }

    public static void alterName(String userName, String newName) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.ALTER_NAME.getId());

        outputBuffer.writeCp1252String(userName);
        outputBuffer.writeCp1252String(newName);
    }

    public static void checkSlot(String userName, int slot) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CHECK_SLOT.getId());

        outputBuffer.writeCp1252String(userName);
        outputBuffer.writeByte(slot);
    }

    public static void toggleCentinelActivated() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.TOGGLE_CENTINEL_ACTIVATED.getId());
    }

    public static void doBackup() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.DO_BACKUP.getId());
    }

    public static void showGuildMessages(String guild) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.SHOW_GUILD_MESSAGES.getId());
        outputBuffer.writeCp1252String(guild);
    }

    public static void saveMap() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.SAVE_MAP.getId());
    }

    public static void changeMapInfoPK(boolean isPK) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CHANGE_MAP_INFO_PK.getId());

        outputBuffer.writeBoolean(isPK);
    }

    public static void changeMapInfoBackup(boolean backup) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CHANGE_MAP_INFO_BACKUP.getId());

        outputBuffer.writeBoolean(backup);
    }

    public static void changeMapInfoRestricted(String restrict) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CHANGE_MAP_INFO_RESTRICTED.getId());

        outputBuffer.writeCp1252String(restrict);
    }

    public static void changeMapInfoNoMagic(boolean noMagic) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CHANGE_MAP_INFO_NO_MAGIC.getId());

        outputBuffer.writeBoolean(noMagic);
    }

    public static void changeMapInfoNoInvi(boolean noInvi) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CHANGE_MAP_INFO_NO_INVI.getId());

        outputBuffer.writeBoolean(noInvi);
    }

    public static void changeMapInfoNoResu(boolean noResu) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CHANGE_MAP_INFO_NO_RESU.getId());

        outputBuffer.writeBoolean(noResu);
    }

    public static void changeMapInfoLand(String land) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CHANGE_MAP_INFO_LAND.getId());

        outputBuffer.writeCp1252String(land);
    }

    public static void changeMapInfoZone(String zone) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CHANGE_MAP_INFO_ZONE.getId());

        outputBuffer.writeCp1252String(zone);
    }

    public static void saveChars() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.SAVE_CHARS.getId());
    }

    public static void cleanSOS() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CLEAN_SOS.getId());
    }

    public static void night() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.NIGHT.getId());
    }

    public static void kickAllChars() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.KICK_ALL_CHARS.getId());
    }

    public static void reloadNPCs() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.RELOAD_NPCS.getId());
    }

    public static void reloadServerIni() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.RELOAD_SERVER_INI.getId());
    }

    public static void reloadSpells() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.RELOAD_SPELLS.getId());
    }

    public static void reloadObjects() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.RELOAD_OBJECTS.getId());
    }

    public static void restart() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.RESTART.getId());
    }

    public static void resetAutoUpdate() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.RESET_AUTO_UPDATE.getId());
    }

    public static void chatColor(int r, int g, int b) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CHAT_COLOR.getId());

        outputBuffer.writeByte(r);
        outputBuffer.writeByte(g);
        outputBuffer.writeByte(b);
    }

    public static void ignored() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.IGNORED.getId());
    }

    public static void ping() {
        if (pingTime != 0) return;
        outputBuffer.writeByte(ClientPacket.PING.getId());
        pingTime = (int) glfwGetTime();
    }

    public static void setIniVar(String sLlave, String sClave, String sValor) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.SET_INI_VAR.getId());

        outputBuffer.writeCp1252String(sLlave);
        outputBuffer.writeCp1252String(sClave);
        outputBuffer.writeCp1252String(sValor);
    }

    public static void home() {
        outputBuffer.writeByte(ClientPacket.HOME.getId());
    }

    public static void commerceBuy(int slot, int amount) {
        outputBuffer.writeByte(ClientPacket.COMMERCE_BUY.getId());
        outputBuffer.writeByte(slot);
        outputBuffer.writeInteger((short) amount);
    }

    public static void commerceSell(int slot, int amount) {
        outputBuffer.writeByte(ClientPacket.COMMERCE_SELL.getId());
        outputBuffer.writeByte(slot);
        outputBuffer.writeInteger((short) amount);
    }

    public static void bankDeposit(int slot, int amount) {
        outputBuffer.writeByte(ClientPacket.BANK_DEPOSIT.getId());
        outputBuffer.writeByte(slot);
        outputBuffer.writeInteger((short) amount);
    }

    public static void bankExtractItem(int slot, int amount) {
        outputBuffer.writeByte(ClientPacket.BANK_EXTRACT_ITEM.getId());
        outputBuffer.writeByte(slot);
        outputBuffer.writeInteger((short) amount);
    }

}
