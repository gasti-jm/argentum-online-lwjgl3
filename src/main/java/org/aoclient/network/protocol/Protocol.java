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
 * Clase responsable de manejar el protocolo de comunicacion entre el cliente y el servidor del juego Argentum Online.
 * <p>
 * {@code Protocol} implementa las operaciones necesarias para la comunicacion de red, procesando los paquetes de datos entrantes
 * desde el servidor y preparando los paquetes salientes hacia el servidor.
 * <p>
 * Esta clase contiene implementaciones para todos los comandos del protocolo del juego, incluyendo acciones de personaje,
 * interacciones con el entorno, comunicacion con otros jugadores y comandos administrativos.
 * <p>
 * Protocol trabaja en conjunto con {@link SocketConnection} para la transmision real de los datos, y utiliza {@link ByteQueue}
 * para almacenar temporalmente los datos entrantes y salientes antes de su procesamiento.
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
    public static ByteQueue incomingData = new ByteQueue();
    public static ByteQueue outgoingData = new ByteQueue();

    public static void handleIncomingData() {
        // Delega el procesamiento de paquetes entrantes a PacketReceiver
        receiver.processIncomingData(incomingData);
    }

    public static void writeLoginExistingChar(String username, String password) {
        outgoingData.writeByte(ClientPacket.LOGIN_EXISTING_CHAR.ordinal());
        outgoingData.writeASCIIString(username);
        outgoingData.writeASCIIString(password);
        outgoingData.writeByte(0);  // App.Major
        outgoingData.writeByte(13); // App.Minor
        outgoingData.writeByte(0);  // App.Revision
    }

    public static void writeThrowDices() {
        outgoingData.writeByte(ClientPacket.THROW_DICES.ordinal());
    }

    public static void writeLoginNewChar(String userName, String userPassword, int userRaza, int userSexo, int userClase, int userHead, String userEmail, int userHogar) {
        outgoingData.writeByte(ClientPacket.LOGIN_NEW_CHAR.ordinal());
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
        outgoingData.writeByte(ClientPacket.TALK.ordinal());
        outgoingData.writeASCIIString(chat);
    }

    public static void writeYell(String chat) {
        outgoingData.writeByte(ClientPacket.YELL.ordinal());
        outgoingData.writeASCIIString(chat);
    }

    public static void writeWhisper(short charIndex, String chat) {
        outgoingData.writeByte(ClientPacket.WHISPER.ordinal());
        outgoingData.writeInteger(charIndex);
        outgoingData.writeASCIIString(chat);
    }

    public static void writeWalk(E_Heading direction) {
        outgoingData.writeByte(ClientPacket.WALK.ordinal());
        outgoingData.writeByte(direction.value);
    }

    public static void writeDrop(int slot, int amount) {
        outgoingData.writeByte(ClientPacket.DROP.ordinal());
        outgoingData.writeByte((byte) slot);
        outgoingData.writeInteger((short) amount);
    }

    public static void writeRequestPositionUpdate() {
        outgoingData.writeByte(ClientPacket.REQUEST_POSITION_UPDATE.ordinal());
    }

    public static void writeAttack() {
        outgoingData.writeByte(ClientPacket.ATTACK.ordinal());
    }

    public static void writePickUp() {
        outgoingData.writeByte(ClientPacket.PICK_UP.ordinal());
    }

    public static void writeSafeToggle() {
        outgoingData.writeByte(ClientPacket.SAFE_TOGGLE.ordinal());
    }

    public static void writeResucitationToggle() {
        outgoingData.writeByte(ClientPacket.RESUSCITATION_SAFE_TOGGLE.ordinal());
    }

    public static void writeRequestGuildLeaderInfo() {
        outgoingData.writeByte(ClientPacket.REQUEST_GUILD_LEADER_INFO.ordinal());
    }

    public static void writeRequestAttributes() {
        outgoingData.writeByte(ClientPacket.REQUEST_ATTRIBUTES.ordinal());
    }

    public static void writeRequestFame() {
        outgoingData.writeByte(ClientPacket.REQUEST_FAME.ordinal());
    }

    public static void writeRequestSkills() {
        outgoingData.writeByte(ClientPacket.REQUEST_SKILLS.ordinal());
    }

    public static void writeRequestMiniStats() {
        outgoingData.writeByte(ClientPacket.REQUEST_MINI_STATS.ordinal());
    }

    public static void writeChangeHeading(E_Heading direction) {
        outgoingData.writeByte(ClientPacket.CHANGE_HEADING.ordinal());
        outgoingData.writeByte(direction.value);
    }

    public static void writeModifySkills(int[] skills) {
        outgoingData.writeByte(ClientPacket.MODIFY_SKILLS.ordinal());
        for (E_Skills skill: E_Skills.values()) {
            outgoingData.writeByte(skills[skill.getValue() - 1]);
        }
    }

    public static void writeLeftClick(int x, int y) {
        outgoingData.writeByte(ClientPacket.LEFT_CLICK.ordinal());
        outgoingData.writeByte(x);
        outgoingData.writeByte(y);
    }

    public static void writeWorkLeftClick(int x, int y, int skill) {
        outgoingData.writeByte(ClientPacket.WORK_LEFT_CLICK.ordinal());
        outgoingData.writeByte(x);
        outgoingData.writeByte(y);
        outgoingData.writeByte(skill);
    }

    public static void writeDoubleClick(int x, int y) {
        outgoingData.writeByte(ClientPacket.DOUBLE_CLICK.ordinal());
        outgoingData.writeByte(x);
        outgoingData.writeByte(y);
    }

    public static void writeUseItem(int slot) {
        outgoingData.writeByte(ClientPacket.USE_ITEM.ordinal());
        outgoingData.writeByte(slot);
    }

    public static void writeEquipItem(int slot) {
        outgoingData.writeByte(ClientPacket.EQUIP_ITEM.ordinal());
        outgoingData.writeByte(slot);
    }

    public static void writeWork(int skill) {
        if (User.get().isDead()) {
            console.addMsgToConsole(new String("¡¡Estás muerto!!".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outgoingData.writeByte(ClientPacket.WORK.ordinal());
        outgoingData.writeByte(skill);
    }

    public static void writeCastSpell(int slot) {
        outgoingData.writeByte(ClientPacket.CAST_SPELL.ordinal());
        outgoingData.writeByte(slot);
    }

    public static void writeQuit() {
        if (charList[User.get().getUserCharIndex()].isParalizado()) {
            console.addMsgToConsole(new String("No puedes salir estando paralizado.".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outgoingData.writeByte(ClientPacket.QUIT.ordinal());
    }

    public static void writeSpellInfo(final int slot) {
        outgoingData.writeByte(ClientPacket.SPELL_INFO.ordinal());
        outgoingData.writeByte(slot);
    }

    public static void writeCommerceEnd() {
        outgoingData.writeByte(ClientPacket.COMMERCE_END.ordinal());
    }

    public static void writeChangePassword(String oldPass, String newPass) {
        outgoingData.writeByte(ClientPacket.CHANGE_PASSWORD.ordinal());
        outgoingData.writeASCIIString(oldPass);
        outgoingData.writeASCIIString(newPass);
    }

    public static void writeOnline() {
        outgoingData.writeByte(ClientPacket.ONLINE.ordinal());
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

        outgoingData.writeByte(ClientPacket.MEDITATE.ordinal());
    }

    public static void writeCommerceStart() {
        outgoingData.writeByte(ClientPacket.COMMERCE_START.ordinal());
    }

    public static void writeBankStart() {
        outgoingData.writeByte(ClientPacket.BANK_START.ordinal());
    }

    public static void writeGuildLeave() {
        outgoingData.writeByte(ClientPacket.GUILD_LEAVE.ordinal());
    }

    public static void writeRequestAccountState() {
        if (charList[User.get().getUserCharIndex()].isDead()) {
            console.addMsgToConsole(new String("¡Estas muerto!".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outgoingData.writeByte(ClientPacket.REQUEST_ACCOUNT_STATE.ordinal());
    }

    public static void writePetStand() {
        if (charList[User.get().getUserCharIndex()].isDead()) {
            console.addMsgToConsole(new String("¡Estas muerto!".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outgoingData.writeByte(ClientPacket.PET_STAND.ordinal());
    }

    public static void writePetFollow() {
        if (charList[User.get().getUserCharIndex()].isDead()) {
            console.addMsgToConsole(new String("¡Estas muerto!".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outgoingData.writeByte(ClientPacket.PET_FOLLOW.ordinal());
    }

    public static void writeReleasePet() {
        if (charList[User.get().getUserCharIndex()].isDead()) {
            console.addMsgToConsole(new String("¡Estas muerto!".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outgoingData.writeByte(ClientPacket.RELEASE_PET.ordinal());
    }

    public static void writeTrainList() {
        if (charList[User.get().getUserCharIndex()].isDead()) {
            console.addMsgToConsole(new String("¡Estas muerto!".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outgoingData.writeByte(ClientPacket.TRAIN_LIST.ordinal());
    }

    public static void writeRest() {
        if (charList[User.get().getUserCharIndex()].isDead()) {
            console.addMsgToConsole(new String("¡Estas muerto!".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outgoingData.writeByte(ClientPacket.REST.ordinal());
    }

    public static void writeConsultation() {
        outgoingData.writeByte(ClientPacket.CONSULTA.ordinal());
    }

    public static void writeResucitate() {
        outgoingData.writeByte(ClientPacket.RESUCITATE.ordinal());
    }

    public static void writeHeal() {
        outgoingData.writeByte(ClientPacket.HEAL.ordinal());
    }

    public static void writeRequestStats() {
        outgoingData.writeByte(ClientPacket.REQUEST_STATS.ordinal());
    }

    public static void writeHelp() {
        outgoingData.writeByte(ClientPacket.HELP.ordinal());
    }

    public static void writeEnlist() {
        outgoingData.writeByte(ClientPacket.ENLIST.ordinal());
    }

    public static void writeInformation() {
        outgoingData.writeByte(ClientPacket.INFORMATION.ordinal());
    }

    public static void writeReward() {
        outgoingData.writeByte(ClientPacket.REWARD.ordinal());
    }

    public static void writeRequestMOTD() {
        outgoingData.writeByte(ClientPacket.REQUEST_MOTD.ordinal());
    }

    public static void writeUpTime() {
        outgoingData.writeByte(ClientPacket.UPTIME.ordinal());
    }

    public static void writePartyLeave() {
        outgoingData.writeByte(ClientPacket.PARTY_LEAVE.ordinal());
    }

    public static void writePartyCreate() {
        if (charList[User.get().getUserCharIndex()].isDead()) {
            console.addMsgToConsole(new String("¡Estas muerto!".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outgoingData.writeByte(ClientPacket.PARTY_CREATE.ordinal());
    }

    public static void writePartyJoin() {
        if (charList[User.get().getUserCharIndex()].isDead()) {
            console.addMsgToConsole(new String("¡Estas muerto!".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outgoingData.writeByte(ClientPacket.PARTY_JOIN.ordinal());
    }

    public static void writeShareNpc() {
        if (charList[User.get().getUserCharIndex()].isDead()) {
            console.addMsgToConsole(new String("¡Estas muerto!".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outgoingData.writeByte(ClientPacket.SHARE_NPC.ordinal());
    }

    public static void writeStopSharingNpc() {
        if (charList[User.get().getUserCharIndex()].isDead()) {
            console.addMsgToConsole(new String("¡Estas muerto!".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outgoingData.writeByte(ClientPacket.STOP_SHARING_NPC.ordinal());
    }

    public static void writeInquiry() {
        outgoingData.writeByte(ClientPacket.INQUIRY.ordinal());
    }

    public static void writeInquiryVote(int opt) {
        outgoingData.writeByte(ClientPacket.INQUIRY_VOTE.ordinal());
        outgoingData.writeByte(opt);
    }

    public static void writeGuildMessage(String message) {
        outgoingData.writeByte(ClientPacket.GUILD_MESSAGE.ordinal());
        outgoingData.writeASCIIString(message);
    }

    public static void writePartyMessage(String message) {
        outgoingData.writeByte(ClientPacket.PARTY_MESSAGE.ordinal());
        outgoingData.writeASCIIString(message);
    }

    public static void writeCentinelReport(int number) {
        outgoingData.writeByte(ClientPacket.CENTINEL_REPORT.ordinal());
        outgoingData.writeByte(number);
    }

    public static void writeGuildOnline() {
        outgoingData.writeByte(ClientPacket.GUILD_ONLINE.ordinal());
    }

    public static void writePartyOnline() {
        outgoingData.writeByte(ClientPacket.GUILD_ONLINE.ordinal());
    }

    public static void writeCouncilMessage(String message) {
        outgoingData.writeByte(ClientPacket.COUNCIL_MESSAGE.ordinal());
        outgoingData.writeASCIIString(message);
    }

    public static void writeRoleMasterRequest(String message) {
        outgoingData.writeByte(ClientPacket.ROLE_MASTER_REQUEST.ordinal());
        outgoingData.writeASCIIString(message);
    }

    public static void writeGMRequest() {
        outgoingData.writeByte(ClientPacket.GM_REQUEST.ordinal());
    }

    public static void writeBugReport(String message) {
        outgoingData.writeByte(ClientPacket.BUG_REPORT.ordinal());
        outgoingData.writeASCIIString(message);
    }

    public static void writeChangeDescription(String message) {
        outgoingData.writeByte(ClientPacket.CHANGE_DESCRIPTION.ordinal());
        outgoingData.writeASCIIString(message);
    }

    public static void writeGuildVote(String message) {
        outgoingData.writeByte(ClientPacket.GUILD_VOTE.ordinal());
        outgoingData.writeASCIIString(message);
    }

    public static void writePunishments(String message) {
        outgoingData.writeByte(ClientPacket.PUNISHMENTS.ordinal());
        outgoingData.writeASCIIString(message);
    }

    public static void writeGamble(short amount) {
        outgoingData.writeByte(ClientPacket.GAMBLE.ordinal());
        outgoingData.writeInteger(amount);
    }

    public static void writeLeaveFaction() {
        outgoingData.writeByte(ClientPacket.LEAVE_FACTION.ordinal());
    }

    public static void writeBankExtractGold(int amount) {
        outgoingData.writeByte(ClientPacket.BANK_EXTRACT_GOLD.ordinal());
        outgoingData.writeLong(amount);
    }

    public static void writeBankDepositGold(int amount) {
        outgoingData.writeByte(ClientPacket.BANK_DEPOSIT_GOLD.ordinal());
        outgoingData.writeLong(amount);
    }

    public static void writeDenounce(String message) {
        outgoingData.writeByte(ClientPacket.DENOUNCE.ordinal());
        outgoingData.writeASCIIString(message);
    }

    public static void writeGuildFundate() {
        outgoingData.writeByte(ClientPacket.GUILD_FUNDATE.ordinal());
    }

    public static void writeGuildFundation(int clanType) {
        outgoingData.writeByte(ClientPacket.GUILD_FUNDATION.ordinal());
        outgoingData.writeByte(clanType);
    }

    public static void writePartyKick(String userName) {
        outgoingData.writeByte(ClientPacket.PARTY_KICK.ordinal());
        outgoingData.writeASCIIString(userName);
    }

    public static void writePartySetLeader(String userName) {
        outgoingData.writeByte(ClientPacket.PARTY_SET_LEADER.ordinal());
        outgoingData.writeASCIIString(userName);
    }

    public static void writePartyAcceptMember(String userName) {
        outgoingData.writeByte(ClientPacket.PARTY_ACCEPT_MEMBER.ordinal());
        outgoingData.writeASCIIString(userName);
    }

     /* ##############################################
        #              COMANDOS DE GM                #
        ##############################################*/

    public static void writeGMMessage(String message) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.GM_MESSAGE.ordinal());

        outgoingData.writeASCIIString(message);
    }

    public static void writeShowName() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.SHOW_NAME.ordinal());
    }

    public static void writeOnlineRoyalArmy() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.ONLINE_ROYAL_ARMY.ordinal());
    }

    public static void writeOnlineChaosLegion() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.ONLINE_CHAOS_LEGION.ordinal());
    }

    public static void writeGoNearby(String userName) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.GO_NEARBY.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeComment(String message) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.COMMENT.ordinal());

        outgoingData.writeASCIIString(message);
    }

    public static void writeServerTime() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.SERVER_TIME.ordinal());
    }

    public static void writeWhere(String userName) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.WHERE.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeCreaturesInMap(short Map) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.CREATURES_IN_MAP.ordinal());

        outgoingData.writeInteger(Map);
    }

    public static void writeCreateItem(int itemIndex) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.CREATE_ITEM.ordinal());
        outgoingData.writeInteger((short) itemIndex);
    }

    public static void writeWarpMeToTarget() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.WARP_ME_TO_TARGET.ordinal());
    }

    public static void writeWarpChar(String userName, short map, int x, int y) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.WARP_CHAR.ordinal());

        outgoingData.writeASCIIString(userName);
        outgoingData.writeInteger(map);
        outgoingData.writeByte(x);
        outgoingData.writeByte(y);
    }

    public static void writeSilence(String userName) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.SILENCE.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeSOSShowList() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.SOS_SHOW_LIST.ordinal());
    }

    public static void writeShowServerForm() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.SHOW_SERVER_FORM.ordinal());
    }

    public static void writeGoToChar(String userName) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.GO_TO_CHAR.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeInvisible() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.INVISIBLE.ordinal());
    }

    public static void writeGMPanel() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.GM_PANEL.ordinal());
    }

    public static void writeWorking() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.WORKING.ordinal());
    }

    public static void writeHiding() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.HIDING.ordinal());
    }

    public static void writeJail(String userName, String reason, int time) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.JAIL.ordinal());

        outgoingData.writeASCIIString(userName);
        outgoingData.writeASCIIString(reason);
        outgoingData.writeByte(time);
    }

    public static void writeKillNPC() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.KILL_NPC.ordinal());
    }

    public static void writeWarnUser(String userName, String reason) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.WARN_USER.ordinal());

        outgoingData.writeASCIIString(userName);
        outgoingData.writeASCIIString(reason);
    }

    public static void writeEditChar(String userName, int editOption, String arg1, String arg2) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.EDIT_CHAR.ordinal());

        outgoingData.writeASCIIString(userName);

        outgoingData.writeByte(editOption);

        outgoingData.writeASCIIString(arg1);
        outgoingData.writeASCIIString(arg2);


    }

    public static void writeRequestCharInfo(String userName) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.REQUEST_CHAR_INFO.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeRequestCharStats(String userName) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.REQUEST_CHAR_STATS.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeRequestCharGold(String userName) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.REQUEST_CHAR_GOLD.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeRequestCharInventory(String userName) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.REQUEST_CHAR_INVENTORY.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeRequestCharBank(String userName) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.REQUEST_CHAR_BANK.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeRequestCharSkills(String userName) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.REQUEST_CHAR_SKILLS.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeReviveChar(String userName) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.REVIVE_CHAR.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeOnlineGM() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.ONLINE_GM.ordinal());
    }

    public static void writeOnlineMap(short map) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.ONLINE_MAP.ordinal());

        outgoingData.writeInteger(map);
    }

    public static void writeForgive(String userName) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.FORGIVE.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeKick(String userName) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.KICK.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeExecute(String userName) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.EXECUTE.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeBanChar(String userName, String reason) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.BAN_CHAR.ordinal());

        outgoingData.writeASCIIString(userName);
        outgoingData.writeASCIIString(reason);
    }

    public static void writeUnbanChar(String userName) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.UNBAN_CHAR.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeNPCFollow() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.NPC_FOLLOW.ordinal());
    }

    public static void writeSummonChar() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.SUMMON_CHAR.ordinal());
    }

    public static void writeSpawnListRequest() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.SPAWN_LIST_REQUEST.ordinal());
    }

    public static void writeResetNPCInventory() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.RESET_NPC_INVENTORY.ordinal());
    }

    public static void writeCleanWorld() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.CLEAN_WORLD.ordinal());
    }

    public static void writeServerMessage() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.SERVER_MESSAGE.ordinal());
    }

    public static void writeNickToIP(String userName) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.NICK_TO_IP.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeIPToNick(int[] ip) {
        // Validar que el tamaño del array sea 4 bytes
        if (ip.length != 4) return; // IP inválida

        // Escribir el mensaje "IPToNick" en el buffer de datos salientes
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.IP_TO_NICK.ordinal());

        // Escribir cada byte de la IP en el buffer de datos salientes
        for (int b : ip) {
            outgoingData.writeByte(b);
        }
    }

    public static void writeGuildOnlineMembers(String guild) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.GUILD_ONLINE_MEMBERS.ordinal());

        outgoingData.writeASCIIString(guild);
    }

    public static void writeTeleportCreate(short map, int x, int y, int radio) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.TELEPORT_CREATE.ordinal());

        outgoingData.writeInteger(map);
        outgoingData.writeByte(x);
        outgoingData.writeByte(y);
        outgoingData.writeByte(radio);
    }

    public static void writeTeleportDestroy() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.TELEPORT_DESTROY.ordinal());
    }

    public static void writeRainToggle() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.RAIN_TOGGLE.ordinal());
    }

    public static void writeSetCharDescription(String desc) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.SET_CHAR_DESCRIPTION.ordinal());

        outgoingData.writeASCIIString(desc);
    }

    public static void writeForceMIDIToMap(int midiID, short map) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.FORCE_MIDI_TO_MAP.ordinal());

        outgoingData.writeByte(midiID);
        outgoingData.writeInteger(map);
    }

    public static void writeForceWAVEToMap(int waveID, short map, int x, int y) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.FORCE_WAVE_TO_MAP.ordinal());

        outgoingData.writeByte(waveID);

        outgoingData.writeInteger(map);

        outgoingData.writeByte(x);
        outgoingData.writeByte(y);
    }

    public static void writeRoyaleArmyMessage(String message) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.ROYAL_ARMY_MESSAGE.ordinal());

        outgoingData.writeASCIIString(message);
    }

    public static void writeChaosLegionMessage(String message) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.CHAOS_LEGION_MESSAGE.ordinal());

        outgoingData.writeASCIIString(message);
    }

    public static void writeCitizenMessage(String message) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.CITIZEN_MESSAGE.ordinal());

        outgoingData.writeASCIIString(message);
    }

    public static void writeCriminalMessage(String message) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.CRIMINAL_MESSAGE.ordinal());

        outgoingData.writeASCIIString(message);
    }

    public static void writeTalkAsNPC(String message) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.TALK_AS_NPC.ordinal());

        outgoingData.writeASCIIString(message);
    }

    public static void writeDestroyAllItemsInArea() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.DESTROY_ALL_ITEMS_IN_AREA.ordinal());
    }

    public static void writeAcceptRoyalCouncilMember(String userName) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.ACCEPT_ROYAL_COUNCIL_MEMBER.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeAcceptChaosCouncilMember(String userName) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.ACCEPT_CHAOS_COUNCIL_MEMBER.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeItemsInTheFloor() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.ITEMS_IN_THE_FLOOR.ordinal());
    }

    public static void writeMakeDumb(String userName) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.MAKE_DUMB.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeMakeDumbNoMore(String userName) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.MAKE_DUMB_NO_MORE.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeDumpIPTables() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.DUMP_IP_TABLES.ordinal());
    }

    public static void writeCouncilKick(String userName) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.COUNCIL_KICK.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeSetTrigger(int trigger) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.SET_TRIGGER.ordinal());

        outgoingData.writeByte(trigger);
    }

    public static void writeAskTrigger() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.ASK_TRIGGER.ordinal());
    }

    public static void writeBannedIPList() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.BANNED_IP_LIST.ordinal());
    }

    public static void writeBannedIPReload() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.BANNED_IP_RELOAD.ordinal());
    }

    public static void writeGuildMemberList(String guild) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.GUILD_MEMBER_LIST.ordinal());

        outgoingData.writeASCIIString(guild);
    }

    public static void writeGuildBan(String guild) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.GUILD_BAN.ordinal());

        outgoingData.writeASCIIString(guild);
    }

    public static void writeBanIP(boolean byIp, int[] ip, String nick, String reason) {
        if (byIp && ip.length != 4) return; // IP inválida

        // Escribir el mensaje "BanIP" en el buffer de datos salientes
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.BAN_IP.ordinal());

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
        if (ip.length != 4) return; // IP inválida

        // Escribir el mensaje "UnbanIP" en el buffer de datos salientes
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.UNBAN_IP.ordinal());

        // Escribir los componentes de la IP
        for (int b : ip) {
            outgoingData.writeByte(b);
        }
    }

    public static void writeDestroyItems() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.DESTROY_ITEMS.ordinal());
    }

    public static void writeChaosLegionKick(String userName) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.CHAOS_LEGION_KICK.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeRoyalArmyKick(String userName) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.ROYAL_ARMY_KICK.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeForceMIDIAll(int midiID) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.FORCE_MIDI_ALL.ordinal());

        outgoingData.writeByte(midiID);
    }

    public static void writeForceWAVEAll(int waveID) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.FORCE_WAVE_ALL.ordinal());

        outgoingData.writeByte(waveID);
    }

    public static void writeRemovePunishment(String userName, int punishment, String newText) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.REMOVE_PUNISHMENT.ordinal());

        outgoingData.writeASCIIString(userName);
        outgoingData.writeByte(punishment);
        outgoingData.writeASCIIString(newText);
    }

    public static void writeTileBlockedToggle() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.TILE_BLOCKED_TOGGLE.ordinal());
    }

    public static void writeKillNPCNoRespawn() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.KILL_NPC_NO_RESPAWN.ordinal());
    }

    public static void writeKillAllNearbyNPCs() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.KILL_ALL_NEARBY_NPCS.ordinal());
    }

    public static void writeLastIP(String userName) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.LAST_IP.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeChangeMOTD() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.CHANGE_MOTD.ordinal());
    }

    public static void writeSystemMessage(String message) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.SYSTEM_MESSAGE.ordinal());

        outgoingData.writeASCIIString(message);
    }

    public static void writeCreateNPC(Short NPCIndex) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.CREATE_NPC.ordinal());

        outgoingData.writeInteger(NPCIndex);
    }

    public static void writeCreateNPCWithRespawn(Short NPCIndex) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.CREATE_NPC_WITH_RESPAWN.ordinal());

        outgoingData.writeInteger(NPCIndex);
    }

    public static void writeImperialArmour(int armourIndex, short objectIndex) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.IMPERIAL_ARMOUR.ordinal());

        outgoingData.writeByte(armourIndex);
        outgoingData.writeInteger(objectIndex);
    }

    public static void writeChaosArmour(int armourIndex, short objectIndex) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.CHAOS_ARMOUR.ordinal());

        outgoingData.writeByte(armourIndex);
        outgoingData.writeInteger(objectIndex);
    }

    public static void writeNavigateToggle() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.NAVIGATE_TOGGLE.ordinal());
    }

    public static void writeServerOpenToUsersToggle() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.SERVER_OPEN_TO_USERS_TOGGLE.ordinal());
    }

    public static void writeTurnOffServer() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.TURN_OFF_SERVER.ordinal());
    }

    public static void writeTurnCriminal(String userName) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.TURN_CRIMINAL.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeResetFactions(String userName) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.RESET_FACTIONS.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeRemoveCharFromGuild(String userName) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.REMOVE_CHAR_FROM_GUILD.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeRequestCharMail(String userName) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.REQUEST_CHAR_MAIL.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeAlterPassword(String userName, String copyFrom) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.ALTER_PASSWORD.ordinal());

        outgoingData.writeASCIIString(userName);
        outgoingData.writeASCIIString(copyFrom);
    }

    public static void writeAlterMail(String userName, String newMail) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.ALTER_MAIL.ordinal());

        outgoingData.writeASCIIString(userName);
        outgoingData.writeASCIIString(newMail);
    }

    public static void writeAlterName(String userName, String newName) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.ALTER_NAME.ordinal());

        outgoingData.writeASCIIString(userName);
        outgoingData.writeASCIIString(newName);
    }

    public static void writeCheckSlot(String userName, int slot) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.CHECK_SLOT.ordinal());

        outgoingData.writeASCIIString(userName);
        outgoingData.writeByte(slot);
    }

    public static void writeToggleCentinelActivated() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.TOGGLE_CENTINEL_ACTIVATED.ordinal());
    }

    public static void writeDoBackup() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.DO_BACKUP.ordinal());
    }

    public static void writeShowGuildMessages(String guild) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.SHOW_GUILD_MESSAGES.ordinal());

        outgoingData.writeASCIIString(guild);
    }

    public static void writeSaveMap() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.SAVE_MAP.ordinal());
    }

    public static void writeChangeMapInfoPK(boolean isPK) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.CHANGE_MAP_INFO_PK.ordinal());

        outgoingData.writeBoolean(isPK);
    }

    public static void writeChangeMapInfoBackup(boolean backup) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.CHANGE_MAP_INFO_BACKUP.ordinal());

        outgoingData.writeBoolean(backup);
    }

    public static void writeChangeMapInfoRestricted(String restrict) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.CHANGE_MAP_INFO_RESTRICTED.ordinal());

        outgoingData.writeASCIIString(restrict);
    }

    public static void writeChangeMapInfoNoMagic(boolean noMagic) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.CHANGE_MAP_INFO_NO_MAGIC.ordinal());

        outgoingData.writeBoolean(noMagic);
    }

    public static void writeChangeMapInfoNoInvi(boolean noInvi) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.CHANGE_MAP_INFO_NO_INVI.ordinal());

        outgoingData.writeBoolean(noInvi);
    }

    public static void writeChangeMapInfoNoResu(boolean noResu) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.CHANGE_MAP_INFO_NO_RESU.ordinal());

        outgoingData.writeBoolean(noResu);
    }

    public static void writeChangeMapInfoLand(String land) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.CHANGE_MAP_INFO_LAND.ordinal());

        outgoingData.writeASCIIString(land);
    }

    public static void writeChangeMapInfoZone(String zone) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.CHANGE_MAP_INFO_ZONE.ordinal());

        outgoingData.writeASCIIString(zone);
    }

    public static void writeSaveChars() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.SAVE_CHARS.ordinal());
    }

    public static void writeCleanSOS() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.CLEAN_SOS.ordinal());
    }

    public static void writeNight() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.NIGHT.ordinal());
    }

    public static void writeKickAllChars() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.KICK_ALL_CHARS.ordinal());
    }

    public static void writeReloadNPCs() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.RELOAD_NPCS.ordinal());
    }

    public static void writeReloadServerIni() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.RELOAD_SERVER_INI.ordinal());
    }

    public static void writeReloadSpells() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.RELOAD_SPELLS.ordinal());
    }

    public static void writeReloadObjects() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.RELOAD_OBJECTS.ordinal());
    }

    public static void writeRestart() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.RESTART.ordinal());
    }

    public static void writeResetAutoUpdate() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.RESET_AUTO_UPDATE.ordinal());
    }

    public static void writeChatColor(int r, int g, int b) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.CHAT_COLOR.ordinal());

        outgoingData.writeByte(r);
        outgoingData.writeByte(g);
        outgoingData.writeByte(b);
    }

    public static void writeIgnored() {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.IGNORED.ordinal());
    }

    public static void writePing() {
        if (pingTime != 0) return;
        outgoingData.writeByte(ClientPacket.PING.ordinal());
        pingTime = (int) glfwGetTime();
    }

    public static void writeSetIniVar(String sLlave, String sClave, String sValor) {
        outgoingData.writeByte(ClientPacket.GM_COMMANDS.ordinal());
        outgoingData.writeByte(GMCommand.SET_INI_VAR.ordinal());

        outgoingData.writeASCIIString(sLlave);
        outgoingData.writeASCIIString(sClave);
        outgoingData.writeASCIIString(sValor);
    }

    public static void writeHome() {
        outgoingData.writeByte(ClientPacket.HOME.ordinal());
    }

    public static void writeCommerceBuy(int slot, int amount) {
        outgoingData.writeByte(ClientPacket.COMMERCE_BUY.ordinal());
        outgoingData.writeByte(slot);
        outgoingData.writeInteger((short) amount);
    }

    public static void writeCommerceSell(int slot, int amount) {
        outgoingData.writeByte(ClientPacket.COMMERCE_SELL.ordinal());
        outgoingData.writeByte(slot);
        outgoingData.writeInteger((short) amount);
    }

}
