package org.aoclient.network.protocol;

import org.aoclient.engine.game.Console;
import org.aoclient.engine.game.User;
import org.aoclient.engine.game.models.E_Heading;
import org.aoclient.engine.game.models.E_Skills;
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
 */

public class Protocol {

    private static final Console CONSOLE = Console.get();
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
    public static void writeLoginExistingChar(String username, String password) {
        // Primero escribe el ID del paquete para que al momento de deserializar el paquete entrante, lea primero el ID del paquete
        outputBuffer.writeByte(ClientPacket.LOGIN_EXISTING_CHAR.getId());
        outputBuffer.writeUTF8String(username);
        outputBuffer.writeUTF8String(password);
        outputBuffer.writeByte(0); // App.Major ?
        outputBuffer.writeByte(13); // App.Minor ?
        outputBuffer.writeByte(0); // App.Revision ?
    }

    public static void writeThrowDices() {
        outputBuffer.writeByte(ClientPacket.THROW_DICES.getId());
    }

    public static void writeLoginNewChar(String userName, String userPassword, int userRaza, int userSexo, int userClase, int userHead, String userEmail, int userHogar) {
        outputBuffer.writeByte(ClientPacket.LOGIN_NEW_CHAR.getId());
        outputBuffer.writeUTF8String(userName);
        outputBuffer.writeUTF8String(userPassword);

        outputBuffer.writeByte(0);  // App.Major
        outputBuffer.writeByte(13); // App.Minor
        outputBuffer.writeByte(0);  // App.Revision

        outputBuffer.writeByte(userRaza);
        outputBuffer.writeByte(userSexo);
        outputBuffer.writeByte(userClase);
        outputBuffer.writeInteger((short) userHead); // Convierte el valor int (32 bits) a short (16 bits) descartando los 16 bits mas significativos para ser compatible con el tipo Integer de VB6 (2 bytes)


        outputBuffer.writeUTF8String(userEmail);
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
     *     outputBuffer.writeUTF8String(chat);
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
     * String chat = buffer.readUTF8String(); // El mensaje
     * short charIndex = buffer.readInteger(); // El indice del personaje (añadido por el servidor)
     * int r = buffer.readByte(); // Componente rojo (añadido por el servidor)
     * int g = buffer.readByte(); // Componente verde (añadido por el servidor)
     * int b = buffer.readByte(); // Componente azul (añadido por el servidor)
     * }</pre>
     * </ol>
     * <p>
     * Ver: estructura-general-de-los-paquetes.md
     */
    public static void writeTalk(String chat) {
        outputBuffer.writeByte(ClientPacket.TALK.getId());
        outputBuffer.writeUTF8String(chat);
    }

    public static void writeYell(String chat) {
        outputBuffer.writeByte(ClientPacket.YELL.getId());
        outputBuffer.writeUTF8String(chat);
    }

    public static void writeWhisper(short charIndex, String chat) {
        outputBuffer.writeByte(ClientPacket.WHISPER.getId());
        outputBuffer.writeInteger(charIndex);
        outputBuffer.writeUTF8String(chat);
    }

    public static void writeWalk(E_Heading direction) {
        outputBuffer.writeByte(ClientPacket.WALK.getId());
        outputBuffer.writeByte(direction.value);
    }

    public static void writeDrop(int slot, int amount) {
        outputBuffer.writeByte(ClientPacket.DROP.getId());
        outputBuffer.writeByte((byte) slot);
        outputBuffer.writeInteger((short) amount);
    }

    public static void writeRequestPositionUpdate() {
        outputBuffer.writeByte(ClientPacket.REQUEST_POSITION_UPDATE.getId());
    }

    public static void writeAttack() {
        outputBuffer.writeByte(ClientPacket.ATTACK.getId());
    }

    public static void writePickUp() {
        outputBuffer.writeByte(ClientPacket.PICK_UP.getId());
    }

    public static void writeSafeToggle() {
        outputBuffer.writeByte(ClientPacket.SAFE_TOGGLE.getId());
    }

    public static void writeResucitationToggle() {
        outputBuffer.writeByte(ClientPacket.RESUSCITATION_SAFE_TOGGLE.getId());
    }

    public static void writeRequestGuildLeaderInfo() {
        outputBuffer.writeByte(ClientPacket.REQUEST_GUILD_LEADER_INFO.getId());
    }

    public static void writeRequestAttributes() {
        outputBuffer.writeByte(ClientPacket.REQUEST_ATTRIBUTES.getId());
    }

    public static void writeRequestFame() {
        outputBuffer.writeByte(ClientPacket.REQUEST_FAME.getId());
    }

    public static void writeRequestSkills() {
        outputBuffer.writeByte(ClientPacket.REQUEST_SKILLS.getId());
    }

    public static void writeRequestMiniStats() {
        outputBuffer.writeByte(ClientPacket.REQUEST_MINI_STATS.getId());
    }

    public static void writeChangeHeading(E_Heading direction) {
        outputBuffer.writeByte(ClientPacket.CHANGE_HEADING.getId());
        outputBuffer.writeByte(direction.value);
    }

    public static void writeModifySkills(int[] skills) {
        outputBuffer.writeByte(ClientPacket.MODIFY_SKILLS.getId());
        for (E_Skills skill : E_Skills.values())
            outputBuffer.writeByte(skills[skill.getValue() - 1]);
    }

    public static void writeLeftClick(int x, int y) {
        outputBuffer.writeByte(ClientPacket.LEFT_CLICK.getId());
        outputBuffer.writeByte(x);
        outputBuffer.writeByte(y);
    }

    public static void writeWorkLeftClick(int x, int y, int skill) {
        outputBuffer.writeByte(ClientPacket.WORK_LEFT_CLICK.getId());
        outputBuffer.writeByte(x);
        outputBuffer.writeByte(y);
        outputBuffer.writeByte(skill);
    }

    public static void writeDoubleClick(int x, int y) {
        outputBuffer.writeByte(ClientPacket.DOUBLE_CLICK.getId());
        outputBuffer.writeByte(x);
        outputBuffer.writeByte(y);
    }

    public static void writeUseItem(int slot) {
        outputBuffer.writeByte(ClientPacket.USE_ITEM.getId());
        outputBuffer.writeByte(slot);
    }

    public static void writeEquipItem(int slot) {
        outputBuffer.writeByte(ClientPacket.EQUIP_ITEM.getId());
        outputBuffer.writeByte(slot);
    }

    public static void writeWork(int skill) {
        if (User.get().isDead()) {
            CONSOLE.addMsgToConsole(new String("¡¡Estas muerto!!".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outputBuffer.writeByte(ClientPacket.WORK.getId());
        outputBuffer.writeByte(skill);
    }

    public static void writeCastSpell(int slot) {
        outputBuffer.writeByte(ClientPacket.CAST_SPELL.getId());
        outputBuffer.writeByte(slot);
    }

    public static void writeQuit() {
        if (charList[User.get().getUserCharIndex()].isParalizado()) {
            CONSOLE.addMsgToConsole(new String("No puedes salir estando paralizado.".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outputBuffer.writeByte(ClientPacket.QUIT.getId());
    }

    public static void writeSpellInfo(final int slot) {
        outputBuffer.writeByte(ClientPacket.SPELL_INFO.getId());
        outputBuffer.writeByte(slot);
    }

    public static void writeCommerceEnd() {
        outputBuffer.writeByte(ClientPacket.COMMERCE_END.getId());
    }

    public static void writeBankEnd() {
        outputBuffer.writeByte(ClientPacket.BANK_END.getId());
    }

    public static void writeChangePassword(String oldPass, String newPass) {
        outputBuffer.writeByte(ClientPacket.CHANGE_PASSWORD.getId());
        outputBuffer.writeUTF8String(oldPass);
        outputBuffer.writeUTF8String(newPass);
    }

    public static void writeOnline() {
        outputBuffer.writeByte(ClientPacket.ONLINE.getId());
    }

    public static void writeMeditate() {

        if (User.get().getUserMaxMAN() == User.get().getUserMinMAN()) {
            return;
        }

        if (charList[User.get().getUserCharIndex()].isDead()) {
            CONSOLE.addMsgToConsole(new String("¡Estas muerto!".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outputBuffer.writeByte(ClientPacket.MEDITATE.getId());
    }

    public static void writeCommerceStart() {
        outputBuffer.writeByte(ClientPacket.COMMERCE_START.getId());
    }

    public static void writeBankStart() {
        outputBuffer.writeByte(ClientPacket.BANK_START.getId());
    }

    public static void writeGuildLeave() {
        outputBuffer.writeByte(ClientPacket.GUILD_LEAVE.getId());
    }

    public static void writeRequestAccountState() {
        if (charList[User.get().getUserCharIndex()].isDead()) {
            CONSOLE.addMsgToConsole(new String("¡Estas muerto!".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outputBuffer.writeByte(ClientPacket.REQUEST_ACCOUNT_STATE.getId());
    }

    public static void writePetStand() {
        if (charList[User.get().getUserCharIndex()].isDead()) {
            CONSOLE.addMsgToConsole(new String("¡Estas muerto!".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outputBuffer.writeByte(ClientPacket.PET_STAND.getId());
    }

    public static void writePetFollow() {
        if (charList[User.get().getUserCharIndex()].isDead()) {
            CONSOLE.addMsgToConsole(new String("¡Estas muerto!".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outputBuffer.writeByte(ClientPacket.PET_FOLLOW.getId());
    }

    public static void writeReleasePet() {
        if (charList[User.get().getUserCharIndex()].isDead()) {
            CONSOLE.addMsgToConsole(new String("¡Estas muerto!".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outputBuffer.writeByte(ClientPacket.RELEASE_PET.getId());
    }

    public static void writeTrainList() {
        if (charList[User.get().getUserCharIndex()].isDead()) {
            CONSOLE.addMsgToConsole(new String("¡Estas muerto!".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outputBuffer.writeByte(ClientPacket.TRAIN_LIST.getId());
    }

    public static void writeRest() {
        if (charList[User.get().getUserCharIndex()].isDead()) {
            CONSOLE.addMsgToConsole(new String("¡Estas muerto!".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outputBuffer.writeByte(ClientPacket.REST.getId());
    }

    public static void writeConsultation() {
        outputBuffer.writeByte(ClientPacket.CONSULTA.getId());
    }

    public static void writeResucitate() {
        outputBuffer.writeByte(ClientPacket.RESUCITATE.getId());
    }

    public static void writeHeal() {
        outputBuffer.writeByte(ClientPacket.HEAL.getId());
    }

    public static void writeRequestStats() {
        outputBuffer.writeByte(ClientPacket.REQUEST_STATS.getId());
    }

    public static void writeHelp() {
        outputBuffer.writeByte(ClientPacket.HELP.getId());
    }

    public static void writeEnlist() {
        outputBuffer.writeByte(ClientPacket.ENLIST.getId());
    }

    public static void writeInformation() {
        outputBuffer.writeByte(ClientPacket.INFORMATION.getId());
    }

    public static void writeReward() {
        outputBuffer.writeByte(ClientPacket.REWARD.getId());
    }

    public static void writeRequestMOTD() {
        outputBuffer.writeByte(ClientPacket.REQUEST_MOTD.getId());
    }

    public static void writeUpTime() {
        outputBuffer.writeByte(ClientPacket.UPTIME.getId());
    }

    public static void writePartyLeave() {
        outputBuffer.writeByte(ClientPacket.PARTY_LEAVE.getId());
    }

    public static void writePartyCreate() {
        if (charList[User.get().getUserCharIndex()].isDead()) {
            CONSOLE.addMsgToConsole(new String("¡Estas muerto!".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outputBuffer.writeByte(ClientPacket.PARTY_CREATE.getId());
    }

    public static void writePartyJoin() {
        if (charList[User.get().getUserCharIndex()].isDead()) {
            CONSOLE.addMsgToConsole(new String("¡Estas muerto!".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outputBuffer.writeByte(ClientPacket.PARTY_JOIN.getId());
    }

    public static void writeShareNpc() {
        if (charList[User.get().getUserCharIndex()].isDead()) {
            CONSOLE.addMsgToConsole(new String("¡Estas muerto!".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outputBuffer.writeByte(ClientPacket.SHARE_NPC.getId());
    }

    public static void writeStopSharingNpc() {
        if (charList[User.get().getUserCharIndex()].isDead()) {
            CONSOLE.addMsgToConsole(new String("¡Estas muerto!".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outputBuffer.writeByte(ClientPacket.STOP_SHARING_NPC.getId());
    }

    public static void writeInquiry() {
        outputBuffer.writeByte(ClientPacket.INQUIRY.getId());
    }

    public static void writeInquiryVote(int opt) {
        outputBuffer.writeByte(ClientPacket.INQUIRY_VOTE.getId());
        outputBuffer.writeByte(opt);
    }

    public static void writeGuildMessage(String message) {
        outputBuffer.writeByte(ClientPacket.GUILD_MESSAGE.getId());
        outputBuffer.writeUTF8String(message);
    }

    public static void writePartyMessage(String message) {
        outputBuffer.writeByte(ClientPacket.PARTY_MESSAGE.getId());
        outputBuffer.writeUTF8String(message);
    }

    public static void writeCentinelReport(int number) {
        outputBuffer.writeByte(ClientPacket.CENTINEL_REPORT.getId());
        outputBuffer.writeByte(number);
    }

    public static void writeGuildOnline() {
        outputBuffer.writeByte(ClientPacket.GUILD_ONLINE.getId());
    }

    public static void writePartyOnline() {
        outputBuffer.writeByte(ClientPacket.GUILD_ONLINE.getId());
    }

    public static void writeCouncilMessage(String message) {
        outputBuffer.writeByte(ClientPacket.COUNCIL_MESSAGE.getId());
        outputBuffer.writeUTF8String(message);
    }

    public static void writeRoleMasterRequest(String message) {
        outputBuffer.writeByte(ClientPacket.ROLE_MASTER_REQUEST.getId());
        outputBuffer.writeUTF8String(message);
    }

    public static void writeGMRequest() {
        outputBuffer.writeByte(ClientPacket.GM_REQUEST.getId());
    }

    public static void writeBugReport(String message) {
        outputBuffer.writeByte(ClientPacket.BUG_REPORT.getId());
        outputBuffer.writeUTF8String(message);
    }

    public static void writeChangeDescription(String message) {
        outputBuffer.writeByte(ClientPacket.CHANGE_DESCRIPTION.getId());
        outputBuffer.writeUTF8String(message);
    }

    public static void writeGuildVote(String message) {
        outputBuffer.writeByte(ClientPacket.GUILD_VOTE.getId());
        outputBuffer.writeUTF8String(message);
    }

    public static void writePunishments(String message) {
        outputBuffer.writeByte(ClientPacket.PUNISHMENTS.getId());
        outputBuffer.writeUTF8String(message);
    }

    public static void writeGamble(short amount) {
        outputBuffer.writeByte(ClientPacket.GAMBLE.getId());
        outputBuffer.writeInteger(amount);
    }

    public static void writeLeaveFaction() {
        outputBuffer.writeByte(ClientPacket.LEAVE_FACTION.getId());
    }

    public static void writeBankExtractGold(int amount) {
        outputBuffer.writeByte(ClientPacket.BANK_EXTRACT_GOLD.getId());
        outputBuffer.writeLong(amount);
    }

    public static void writeBankDepositGold(int amount) {
        outputBuffer.writeByte(ClientPacket.BANK_DEPOSIT_GOLD.getId());
        outputBuffer.writeLong(amount);
    }

    public static void writeDenounce(String message) {
        outputBuffer.writeByte(ClientPacket.DENOUNCE.getId());
        outputBuffer.writeUTF8String(message);
    }

    public static void writeGuildFundate() {
        outputBuffer.writeByte(ClientPacket.GUILD_FUNDATE.getId());
    }

    public static void writeGuildFundation(int clanType) {
        outputBuffer.writeByte(ClientPacket.GUILD_FUNDATION.getId());
        outputBuffer.writeByte(clanType);
    }

    public static void writePartyKick(String userName) {
        outputBuffer.writeByte(ClientPacket.PARTY_KICK.getId());
        outputBuffer.writeUTF8String(userName);
    }

    public static void writePartySetLeader(String userName) {
        outputBuffer.writeByte(ClientPacket.PARTY_SET_LEADER.getId());
        outputBuffer.writeUTF8String(userName);
    }

    public static void writePartyAcceptMember(String userName) {
        outputBuffer.writeByte(ClientPacket.PARTY_ACCEPT_MEMBER.getId());
        outputBuffer.writeUTF8String(userName);
    }

     /* ##############################################
        #              COMANDOS DE GM                #
        ##############################################*/

    public static void writeGMMessage(String message) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.GM_MESSAGE.getId());

        outputBuffer.writeUTF8String(message);
    }

    public static void writeShowName() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.SHOW_NAME.getId());
    }

    public static void writeOnlineRoyalArmy() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.ONLINE_ROYAL_ARMY.getId());
    }

    public static void writeOnlineChaosLegion() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.ONLINE_CHAOS_LEGION.getId());
    }

    public static void writeGoNearby(String userName) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.GO_NEARBY.getId());

        outputBuffer.writeUTF8String(userName);
    }

    public static void writeComment(String message) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.COMMENT.getId());

        outputBuffer.writeUTF8String(message);
    }

    public static void writeServerTime() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.SERVER_TIME.getId());
    }

    public static void writeWhere(String userName) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.WHERE.getId());

        outputBuffer.writeUTF8String(userName);
    }

    public static void writeCreaturesInMap(short Map) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CREATURES_IN_MAP.getId());

        outputBuffer.writeInteger(Map);
    }

    public static void writeCreateItem(int itemIndex) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CREATE_ITEM.getId());
        outputBuffer.writeInteger((short) itemIndex);
    }

    public static void writeWarpMeToTarget() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.WARP_ME_TO_TARGET.getId());
    }

    public static void writeWarpChar(String userName, short map, int x, int y) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.WARP_CHAR.getId());

        outputBuffer.writeUTF8String(userName);
        outputBuffer.writeInteger(map);
        outputBuffer.writeByte(x);
        outputBuffer.writeByte(y);
    }

    public static void writeSilence(String userName) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.SILENCE.getId());

        outputBuffer.writeUTF8String(userName);
    }

    public static void writeSOSShowList() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.SOS_SHOW_LIST.getId());
    }

    public static void writeShowServerForm() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.SHOW_SERVER_FORM.getId());
    }

    public static void writeGoToChar(String userName) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.GO_TO_CHAR.getId());

        outputBuffer.writeUTF8String(userName);
    }

    public static void writeInvisible() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.INVISIBLE.getId());
    }

    public static void writeGMPanel() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.GM_PANEL.getId());
    }

    public static void writeWorking() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.WORKING.getId());
    }

    public static void writeHiding() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.HIDING.getId());
    }

    public static void writeJail(String userName, String reason, int time) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.JAIL.getId());

        outputBuffer.writeUTF8String(userName);
        outputBuffer.writeUTF8String(reason);
        outputBuffer.writeByte(time);
    }

    public static void writeKillNPC() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.KILL_NPC.getId());
    }

    public static void writeWarnUser(String userName, String reason) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.WARN_USER.getId());

        outputBuffer.writeUTF8String(userName);
        outputBuffer.writeUTF8String(reason);
    }

    public static void writeEditChar(String userName, int editOption, String arg1, String arg2) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.EDIT_CHAR.getId());

        outputBuffer.writeUTF8String(userName);

        outputBuffer.writeByte(editOption);

        outputBuffer.writeUTF8String(arg1);
        outputBuffer.writeUTF8String(arg2);


    }

    public static void writeRequestCharInfo(String userName) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.REQUEST_CHAR_INFO.getId());

        outputBuffer.writeUTF8String(userName);
    }

    public static void writeRequestCharStats(String userName) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.REQUEST_CHAR_STATS.getId());

        outputBuffer.writeUTF8String(userName);
    }

    public static void writeRequestCharGold(String userName) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.REQUEST_CHAR_GOLD.getId());

        outputBuffer.writeUTF8String(userName);
    }

    public static void writeRequestCharInventory(String userName) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.REQUEST_CHAR_INVENTORY.getId());

        outputBuffer.writeUTF8String(userName);
    }

    public static void writeRequestCharBank(String userName) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.REQUEST_CHAR_BANK.getId());

        outputBuffer.writeUTF8String(userName);
    }

    public static void writeRequestCharSkills(String userName) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.REQUEST_CHAR_SKILLS.getId());

        outputBuffer.writeUTF8String(userName);
    }

    public static void writeReviveChar(String userName) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.REVIVE_CHAR.getId());

        outputBuffer.writeUTF8String(userName);
    }

    public static void writeOnlineGM() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.ONLINE_GM.getId());
    }

    public static void writeOnlineMap(short map) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.ONLINE_MAP.getId());

        outputBuffer.writeInteger(map);
    }

    public static void writeForgive(String userName) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.FORGIVE.getId());

        outputBuffer.writeUTF8String(userName);
    }

    public static void writeKick(String userName) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.KICK.getId());

        outputBuffer.writeUTF8String(userName);
    }

    public static void writeExecute(String userName) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.EXECUTE.getId());

        outputBuffer.writeUTF8String(userName);
    }

    public static void writeBanChar(String userName, String reason) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.BAN_CHAR.getId());

        outputBuffer.writeUTF8String(userName);
        outputBuffer.writeUTF8String(reason);
    }

    public static void writeUnbanChar(String userName) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.UNBAN_CHAR.getId());

        outputBuffer.writeUTF8String(userName);
    }

    public static void writeNPCFollow() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.NPC_FOLLOW.getId());
    }

    public static void writeSummonChar() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.SUMMON_CHAR.getId());
    }

    public static void writeSpawnListRequest() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.SPAWN_LIST_REQUEST.getId());
    }

    public static void writeSpawnCreature(short creatureIndex) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.SPAWN_CREATURE.getId());
        outputBuffer.writeInteger(creatureIndex);
    }
    public static void writeResetNPCInventory() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.RESET_NPC_INVENTORY.getId());
    }

    public static void writeCleanWorld() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CLEAN_WORLD.getId());
    }

    public static void writeServerMessage() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.SERVER_MESSAGE.getId());
    }

    public static void writeNickToIP(String userName) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.NICK_TO_IP.getId());

        outputBuffer.writeUTF8String(userName);
    }

    public static void writeIPToNick(int[] ip) {
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

    public static void writeGuildOnlineMembers(String guild) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.GUILD_ONLINE_MEMBERS.getId());

        outputBuffer.writeUTF8String(guild);
    }

    public static void writeTeleportCreate(short map, int x, int y, int radio) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.TELEPORT_CREATE.getId());

        outputBuffer.writeInteger(map);
        outputBuffer.writeByte(x);
        outputBuffer.writeByte(y);
        outputBuffer.writeByte(radio);
    }

    public static void writeTeleportDestroy() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.TELEPORT_DESTROY.getId());
    }

    public static void writeRainToggle() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.RAIN_TOGGLE.getId());
    }

    public static void writeSetCharDescription(String desc) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.SET_CHAR_DESCRIPTION.getId());

        outputBuffer.writeUTF8String(desc);
    }

    public static void writeForceMIDIToMap(int midiID, short map) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.FORCE_MIDI_TO_MAP.getId());

        outputBuffer.writeByte(midiID);
        outputBuffer.writeInteger(map);
    }

    public static void writeForceWAVEToMap(int waveID, short map, int x, int y) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.FORCE_WAVE_TO_MAP.getId());

        outputBuffer.writeByte(waveID);

        outputBuffer.writeInteger(map);

        outputBuffer.writeByte(x);
        outputBuffer.writeByte(y);
    }

    public static void writeRoyaleArmyMessage(String message) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.ROYAL_ARMY_MESSAGE.getId());

        outputBuffer.writeUTF8String(message);
    }

    public static void writeChaosLegionMessage(String message) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CHAOS_LEGION_MESSAGE.getId());

        outputBuffer.writeUTF8String(message);
    }

    public static void writeCitizenMessage(String message) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CITIZEN_MESSAGE.getId());

        outputBuffer.writeUTF8String(message);
    }

    public static void writeCriminalMessage(String message) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CRIMINAL_MESSAGE.getId());

        outputBuffer.writeUTF8String(message);
    }

    public static void writeTalkAsNPC(String message) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.TALK_AS_NPC.getId());

        outputBuffer.writeUTF8String(message);
    }

    public static void writeDestroyAllItemsInArea() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.DESTROY_ALL_ITEMS_IN_AREA.getId());
    }

    public static void writeAcceptRoyalCouncilMember(String userName) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.ACCEPT_ROYAL_COUNCIL_MEMBER.getId());

        outputBuffer.writeUTF8String(userName);
    }

    public static void writeAcceptChaosCouncilMember(String userName) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.ACCEPT_CHAOS_COUNCIL_MEMBER.getId());

        outputBuffer.writeUTF8String(userName);
    }

    public static void writeItemsInTheFloor() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.ITEMS_IN_THE_FLOOR.getId());
    }

    public static void writeMakeDumb(String userName) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.MAKE_DUMB.getId());

        outputBuffer.writeUTF8String(userName);
    }

    public static void writeMakeDumbNoMore(String userName) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.MAKE_DUMB_NO_MORE.getId());

        outputBuffer.writeUTF8String(userName);
    }

    public static void writeDumpIPTables() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.DUMP_IP_TABLES.getId());
    }

    public static void writeCouncilKick(String userName) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.COUNCIL_KICK.getId());

        outputBuffer.writeUTF8String(userName);
    }

    public static void writeSetTrigger(int trigger) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.SET_TRIGGER.getId());

        outputBuffer.writeByte(trigger);
    }

    public static void writeAskTrigger() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.ASK_TRIGGER.getId());
    }

    public static void writeBannedIPList() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.BANNED_IP_LIST.getId());
    }

    public static void writeBannedIPReload() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.BANNED_IP_RELOAD.getId());
    }

    public static void writeGuildMemberList(String guild) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.GUILD_MEMBER_LIST.getId());

        outputBuffer.writeUTF8String(guild);
    }

    public static void writeGuildBan(String guild) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.GUILD_BAN.getId());

        outputBuffer.writeUTF8String(guild);
    }

    public static void writeBanIP(boolean byIp, int[] ip, String nick, String reason) {
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
            outputBuffer.writeUTF8String(nick);
        }

        // Escribir el motivo del baneo
        outputBuffer.writeUTF8String(reason);
    }

    public static void writeUnbanIP(int[] ip) {
        if (ip.length != 4) return; // IP invalida

        // Escribir el mensaje "UnbanIP" en el buffer de datos salientes
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.UNBAN_IP.getId());

        // Escribir los componentes de la IP
        for (int b : ip) {
            outputBuffer.writeByte(b);
        }
    }

    public static void writeDestroyItems() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.DESTROY_ITEMS.getId());
    }

    public static void writeChaosLegionKick(String userName) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CHAOS_LEGION_KICK.getId());

        outputBuffer.writeUTF8String(userName);
    }

    public static void writeRoyalArmyKick(String userName) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.ROYAL_ARMY_KICK.getId());

        outputBuffer.writeUTF8String(userName);
    }

    public static void writeForceMIDIAll(int midiID) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.FORCE_MIDI_ALL.getId());

        outputBuffer.writeByte(midiID);
    }

    public static void writeForceWAVEAll(int waveID) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.FORCE_WAVE_ALL.getId());

        outputBuffer.writeByte(waveID);
    }

    public static void writeRemovePunishment(String userName, int punishment, String newText) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.REMOVE_PUNISHMENT.getId());

        outputBuffer.writeUTF8String(userName);
        outputBuffer.writeByte(punishment);
        outputBuffer.writeUTF8String(newText);
    }

    public static void writeTileBlockedToggle() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.TILE_BLOCKED_TOGGLE.getId());
    }

    public static void writeKillNPCNoRespawn() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.KILL_NPC_NO_RESPAWN.getId());
    }

    public static void writeKillAllNearbyNPCs() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.KILL_ALL_NEARBY_NPCS.getId());
    }

    public static void writeLastIP(String userName) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.LAST_IP.getId());

        outputBuffer.writeUTF8String(userName);
    }

    public static void writeChangeMOTD() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CHANGE_MOTD.getId());
    }

    public static void writeSystemMessage(String message) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.SYSTEM_MESSAGE.getId());

        outputBuffer.writeUTF8String(message);
    }

    public static void writeCreateNPC(Short NPCIndex) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CREATE_NPC.getId());

        outputBuffer.writeInteger(NPCIndex);
    }

    public static void writeCreateNPCWithRespawn(Short NPCIndex) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CREATE_NPC_WITH_RESPAWN.getId());

        outputBuffer.writeInteger(NPCIndex);
    }

    public static void writeImperialArmour(int armourIndex, short objectIndex) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.IMPERIAL_ARMOUR.getId());

        outputBuffer.writeByte(armourIndex);
        outputBuffer.writeInteger(objectIndex);
    }

    public static void writeChaosArmour(int armourIndex, short objectIndex) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CHAOS_ARMOUR.getId());

        outputBuffer.writeByte(armourIndex);
        outputBuffer.writeInteger(objectIndex);
    }

    public static void writeNavigateToggle() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.NAVIGATE_TOGGLE.getId());
    }

    public static void writeServerOpenToUsersToggle() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.SERVER_OPEN_TO_USERS_TOGGLE.getId());
    }

    public static void writeTurnOffServer() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.TURN_OFF_SERVER.getId());
    }

    public static void writeTurnCriminal(String userName) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.TURN_CRIMINAL.getId());

        outputBuffer.writeUTF8String(userName);
    }

    public static void writeResetFactions(String userName) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.RESET_FACTIONS.getId());

        outputBuffer.writeUTF8String(userName);
    }

    public static void writeRemoveCharFromGuild(String userName) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.REMOVE_CHAR_FROM_GUILD.getId());

        outputBuffer.writeUTF8String(userName);
    }

    public static void writeRequestCharMail(String userName) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.REQUEST_CHAR_MAIL.getId());

        outputBuffer.writeUTF8String(userName);
    }

    public static void writeAlterPassword(String userName, String copyFrom) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.ALTER_PASSWORD.getId());

        outputBuffer.writeUTF8String(userName);
        outputBuffer.writeUTF8String(copyFrom);
    }

    public static void writeAlterMail(String userName, String newMail) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.ALTER_MAIL.getId());

        outputBuffer.writeUTF8String(userName);
        outputBuffer.writeUTF8String(newMail);
    }

    public static void writeAlterName(String userName, String newName) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.ALTER_NAME.getId());

        outputBuffer.writeUTF8String(userName);
        outputBuffer.writeUTF8String(newName);
    }

    public static void writeCheckSlot(String userName, int slot) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CHECK_SLOT.getId());

        outputBuffer.writeUTF8String(userName);
        outputBuffer.writeByte(slot);
    }

    public static void writeToggleCentinelActivated() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.TOGGLE_CENTINEL_ACTIVATED.getId());
    }

    public static void writeDoBackup() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.DO_BACKUP.getId());
    }

    public static void writeShowGuildMessages(String guild) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.SHOW_GUILD_MESSAGES.getId());

        outputBuffer.writeUTF8String(guild);
    }

    public static void writeSaveMap() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.SAVE_MAP.getId());
    }

    public static void writeChangeMapInfoPK(boolean isPK) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CHANGE_MAP_INFO_PK.getId());

        outputBuffer.writeBoolean(isPK);
    }

    public static void writeChangeMapInfoBackup(boolean backup) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CHANGE_MAP_INFO_BACKUP.getId());

        outputBuffer.writeBoolean(backup);
    }

    public static void writeChangeMapInfoRestricted(String restrict) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CHANGE_MAP_INFO_RESTRICTED.getId());

        outputBuffer.writeUTF8String(restrict);
    }

    public static void writeChangeMapInfoNoMagic(boolean noMagic) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CHANGE_MAP_INFO_NO_MAGIC.getId());

        outputBuffer.writeBoolean(noMagic);
    }

    public static void writeChangeMapInfoNoInvi(boolean noInvi) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CHANGE_MAP_INFO_NO_INVI.getId());

        outputBuffer.writeBoolean(noInvi);
    }

    public static void writeChangeMapInfoNoResu(boolean noResu) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CHANGE_MAP_INFO_NO_RESU.getId());

        outputBuffer.writeBoolean(noResu);
    }

    public static void writeChangeMapInfoLand(String land) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CHANGE_MAP_INFO_LAND.getId());

        outputBuffer.writeUTF8String(land);
    }

    public static void writeChangeMapInfoZone(String zone) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CHANGE_MAP_INFO_ZONE.getId());

        outputBuffer.writeUTF8String(zone);
    }

    public static void writeSaveChars() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.SAVE_CHARS.getId());
    }

    public static void writeCleanSOS() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CLEAN_SOS.getId());
    }

    public static void writeNight() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.NIGHT.getId());
    }

    public static void writeKickAllChars() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.KICK_ALL_CHARS.getId());
    }

    public static void writeReloadNPCs() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.RELOAD_NPCS.getId());
    }

    public static void writeReloadServerIni() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.RELOAD_SERVER_INI.getId());
    }

    public static void writeReloadSpells() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.RELOAD_SPELLS.getId());
    }

    public static void writeReloadObjects() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.RELOAD_OBJECTS.getId());
    }

    public static void writeRestart() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.RESTART.getId());
    }

    public static void writeResetAutoUpdate() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.RESET_AUTO_UPDATE.getId());
    }

    public static void writeChatColor(int r, int g, int b) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CHAT_COLOR.getId());

        outputBuffer.writeByte(r);
        outputBuffer.writeByte(g);
        outputBuffer.writeByte(b);
    }

    public static void writeIgnored() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.IGNORED.getId());
    }

    public static void writePing() {
        if (pingTime != 0) return;
        outputBuffer.writeByte(ClientPacket.PING.getId());
        pingTime = (int) glfwGetTime();
    }

    public static void writeSetIniVar(String sLlave, String sClave, String sValor) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.SET_INI_VAR.getId());

        outputBuffer.writeUTF8String(sLlave);
        outputBuffer.writeUTF8String(sClave);
        outputBuffer.writeUTF8String(sValor);
    }

    public static void writeHome() {
        outputBuffer.writeByte(ClientPacket.HOME.getId());
    }

    public static void writeCommerceBuy(int slot, int amount) {
        outputBuffer.writeByte(ClientPacket.COMMERCE_BUY.getId());
        outputBuffer.writeByte(slot);
        outputBuffer.writeInteger((short) amount);
    }

    public static void writeCommerceSell(int slot, int amount) {
        outputBuffer.writeByte(ClientPacket.COMMERCE_SELL.getId());
        outputBuffer.writeByte(slot);
        outputBuffer.writeInteger((short) amount);
    }

}
