package org.aoclient.network.protocol;

import org.aoclient.engine.game.Console;
import org.aoclient.engine.game.User;
import org.aoclient.engine.game.models.E_Heading;
import org.aoclient.engine.game.models.E_Skills;
import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.ByteQueue;
import org.aoclient.network.SocketConnection;
import org.aoclient.network.packets.ClientPacketID;
import org.aoclient.network.packets.ServerPacketID;
import org.aoclient.network.packets.eGMCommands;

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
 * Cada comando de protocolo esta definido en los enumeradores {@link ClientPacketID} y {@link ServerPacketID} para garantizar una
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
        outgoingData.writeByte(ClientPacketID.LoginExistingChar.ordinal());
        outgoingData.writeASCIIString(username);
        outgoingData.writeASCIIString(password);
        outgoingData.writeByte(0);  // App.Major
        outgoingData.writeByte(13); // App.Minor
        outgoingData.writeByte(0);  // App.Revision
    }

    public static void writeThrowDices() {
        outgoingData.writeByte(ClientPacketID.ThrowDices.ordinal());
    }

    public static void writeLoginNewChar(String userName, String userPassword, int userRaza, int userSexo, int userClase, int userHead, String userEmail, int userHogar) {
        outgoingData.writeByte(ClientPacketID.LoginNewChar.ordinal());
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
        outgoingData.writeByte(ClientPacketID.Talk.ordinal());
        outgoingData.writeASCIIString(chat);
    }

    public static void writeYell(String chat) {
        outgoingData.writeByte(ClientPacketID.Yell.ordinal());
        outgoingData.writeASCIIString(chat);
    }

    public static void writeWhisper(short charIndex, String chat) {
        outgoingData.writeByte(ClientPacketID.Whisper.ordinal());
        outgoingData.writeInteger(charIndex);
        outgoingData.writeASCIIString(chat);
    }

    public static void writeWalk(E_Heading direction) {
        outgoingData.writeByte(ClientPacketID.Walk.ordinal());
        outgoingData.writeByte(direction.value);
    }

    public static void writeDrop(int slot, int amount) {
        outgoingData.writeByte(ClientPacketID.Drop.ordinal());
        outgoingData.writeByte((byte) slot);
        outgoingData.writeInteger((short) amount);
    }

    public static void writeRequestPositionUpdate() {
        outgoingData.writeByte(ClientPacketID.RequestPositionUpdate.ordinal());
    }

    public static void writeAttack() {
        outgoingData.writeByte(ClientPacketID.Attack.ordinal());
    }

    public static void writePickUp() {
        outgoingData.writeByte(ClientPacketID.PickUp.ordinal());
    }

    public static void writeSafeToggle() {
        outgoingData.writeByte(ClientPacketID.SafeToggle.ordinal());
    }

    public static void writeResucitationToggle() {
        outgoingData.writeByte(ClientPacketID.ResuscitationSafeToggle.ordinal());
    }

    public static void writeRequestGuildLeaderInfo() {
        outgoingData.writeByte(ClientPacketID.RequestGuildLeaderInfo.ordinal());
    }

    public static void writeChangeHeading(E_Heading direction) {
        outgoingData.writeByte(ClientPacketID.ChangeHeading.ordinal());
        outgoingData.writeByte(direction.value);
    }

    public static void writeModifySkills(int[] skills) {
        outgoingData.writeByte(ClientPacketID.ModifySkills.ordinal());
        for (E_Skills skill: E_Skills.values()) {
            outgoingData.writeByte(skills[skill.getValue() - 1]);
        }
    }

    public static void writeLeftClick(int x, int y) {
        outgoingData.writeByte(ClientPacketID.LeftClick.ordinal());
        outgoingData.writeByte(x);
        outgoingData.writeByte(y);
    }

    public static void writeWorkLeftClick(int x, int y, int skill) {
        outgoingData.writeByte(ClientPacketID.WorkLeftClick.ordinal());
        outgoingData.writeByte(x);
        outgoingData.writeByte(y);
        outgoingData.writeByte(skill);
    }

    public static void writeDoubleClick(int x, int y) {
        outgoingData.writeByte(ClientPacketID.DoubleClick.ordinal());
        outgoingData.writeByte(x);
        outgoingData.writeByte(y);
    }

    public static void writeUseItem(int slot) {
        outgoingData.writeByte(ClientPacketID.UseItem.ordinal());
        outgoingData.writeByte(slot);
    }

    public static void writeEquipItem(int slot) {
        outgoingData.writeByte(ClientPacketID.EquipItem.ordinal());
        outgoingData.writeByte(slot);
    }

    public static void writeWork(int skill) {
        if (User.get().isDead()) {
            console.addMsgToConsole(new String("¡¡Estás muerto!!".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outgoingData.writeByte(ClientPacketID.Work.ordinal());
        outgoingData.writeByte(skill);
    }

    public static void writeCastSpell(int slot) {
        outgoingData.writeByte(ClientPacketID.CastSpell.ordinal());
        outgoingData.writeByte(slot);
    }

    public static void writeQuit() {
        if (charList[User.get().getUserCharIndex()].isParalizado()) {
            console.addMsgToConsole(new String("No puedes salir estando paralizado.".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outgoingData.writeByte(ClientPacketID.Quit.ordinal());
    }

    public static void writeSpellInfo(final int slot) {
        outgoingData.writeByte(ClientPacketID.SpellInfo.ordinal());
        outgoingData.writeByte(slot);
    }

    public static void writeCommerceEnd() {
        outgoingData.writeByte(ClientPacketID.CommerceEnd.ordinal());
    }

    public static void writeChangePassword(String oldPass, String newPass) {
        outgoingData.writeByte(ClientPacketID.ChangePassword.ordinal());
        outgoingData.writeASCIIString(oldPass);
        outgoingData.writeASCIIString(newPass);
    }

    public static void writeOnline() {
        outgoingData.writeByte(ClientPacketID.Online.ordinal());
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

        outgoingData.writeByte(ClientPacketID.Meditate.ordinal());
    }

    public static void writeCommerceStart() {
        outgoingData.writeByte(ClientPacketID.CommerceStart.ordinal());
    }

    public static void writeBankStart() {
        outgoingData.writeByte(ClientPacketID.BankStart.ordinal());
    }

    public static void writeGuildLeave() {
        outgoingData.writeByte(ClientPacketID.GuildLeave.ordinal());
    }

    public static void writeRequestAccountState() {
        if (charList[User.get().getUserCharIndex()].isDead()) {
            console.addMsgToConsole(new String("¡Estas muerto!".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outgoingData.writeByte(ClientPacketID.RequestAccountState.ordinal());
    }

    public static void writePetStand() {
        if (charList[User.get().getUserCharIndex()].isDead()) {
            console.addMsgToConsole(new String("¡Estas muerto!".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outgoingData.writeByte(ClientPacketID.PetStand.ordinal());
    }

    public static void writePetFollow() {
        if (charList[User.get().getUserCharIndex()].isDead()) {
            console.addMsgToConsole(new String("¡Estas muerto!".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outgoingData.writeByte(ClientPacketID.PetFollow.ordinal());
    }

    public static void writeReleasePet() {
        if (charList[User.get().getUserCharIndex()].isDead()) {
            console.addMsgToConsole(new String("¡Estas muerto!".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outgoingData.writeByte(ClientPacketID.ReleasePet.ordinal());
    }

    public static void writeTrainList() {
        if (charList[User.get().getUserCharIndex()].isDead()) {
            console.addMsgToConsole(new String("¡Estas muerto!".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outgoingData.writeByte(ClientPacketID.TrainList.ordinal());
    }

    public static void writeRest() {
        if (charList[User.get().getUserCharIndex()].isDead()) {
            console.addMsgToConsole(new String("¡Estas muerto!".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outgoingData.writeByte(ClientPacketID.Rest.ordinal());
    }

    public static void writeConsultation() {
        outgoingData.writeByte(ClientPacketID.Consulta.ordinal());
    }

    public static void writeResucitate() {
        outgoingData.writeByte(ClientPacketID.Resucitate.ordinal());
    }

    public static void writeHeal() {
        outgoingData.writeByte(ClientPacketID.Heal.ordinal());
    }

    public static void writeRequestStats() {
        outgoingData.writeByte(ClientPacketID.RequestStats.ordinal());
    }

    public static void writeHelp() {
        outgoingData.writeByte(ClientPacketID.Help.ordinal());
    }

    public static void writeEnlist() {
        outgoingData.writeByte(ClientPacketID.Enlist.ordinal());
    }

    public static void writeInformation() {
        outgoingData.writeByte(ClientPacketID.Information.ordinal());
    }

    public static void writeReward() {
        outgoingData.writeByte(ClientPacketID.Reward.ordinal());
    }

    public static void writeRequestMOTD() {
        outgoingData.writeByte(ClientPacketID.RequestMOTD.ordinal());
    }

    public static void writeUpTime() {
        outgoingData.writeByte(ClientPacketID.Uptime.ordinal());
    }

    public static void writePartyLeave() {
        outgoingData.writeByte(ClientPacketID.PartyLeave.ordinal());
    }

    public static void writePartyCreate() {
        if (charList[User.get().getUserCharIndex()].isDead()) {
            console.addMsgToConsole(new String("¡Estas muerto!".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outgoingData.writeByte(ClientPacketID.PartyCreate.ordinal());
    }

    public static void writePartyJoin() {
        if (charList[User.get().getUserCharIndex()].isDead()) {
            console.addMsgToConsole(new String("¡Estas muerto!".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outgoingData.writeByte(ClientPacketID.PartyJoin.ordinal());
    }

    public static void writeShareNpc() {
        if (charList[User.get().getUserCharIndex()].isDead()) {
            console.addMsgToConsole(new String("¡Estas muerto!".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outgoingData.writeByte(ClientPacketID.ShareNpc.ordinal());
    }

    public static void writeStopSharingNpc() {
        if (charList[User.get().getUserCharIndex()].isDead()) {
            console.addMsgToConsole(new String("¡Estas muerto!".getBytes(), StandardCharsets.UTF_8),
                    false, true, new RGBColor());

            return;
        }

        outgoingData.writeByte(ClientPacketID.StopSharingNpc.ordinal());
    }

    public static void writeInquiry() {
        outgoingData.writeByte(ClientPacketID.Inquiry.ordinal());
    }

    public static void writeInquiryVote(int opt) {
        outgoingData.writeByte(ClientPacketID.InquiryVote.ordinal());
        outgoingData.writeByte(opt);
    }

    public static void writeGuildMessage(String message) {
        outgoingData.writeByte(ClientPacketID.GuildMessage.ordinal());
        outgoingData.writeASCIIString(message);
    }

    public static void writePartyMessage(String message) {
        outgoingData.writeByte(ClientPacketID.PartyMessage.ordinal());
        outgoingData.writeASCIIString(message);
    }

    public static void writeCentinelReport(int number) {
        outgoingData.writeByte(ClientPacketID.CentinelReport.ordinal());
        outgoingData.writeByte(number);
    }

    public static void writeGuildOnline() {
        outgoingData.writeByte(ClientPacketID.GuildOnline.ordinal());
    }

    public static void writePartyOnline() {
        outgoingData.writeByte(ClientPacketID.GuildOnline.ordinal());
    }

    public static void writeCouncilMessage(String message) {
        outgoingData.writeByte(ClientPacketID.CouncilMessage.ordinal());
        outgoingData.writeASCIIString(message);
    }

    public static void writeRoleMasterRequest(String message) {
        outgoingData.writeByte(ClientPacketID.RoleMasterRequest.ordinal());
        outgoingData.writeASCIIString(message);
    }

    public static void writeGMRequest() {
        outgoingData.writeByte(ClientPacketID.GMRequest.ordinal());
    }

    public static void writeBugReport(String message) {
        outgoingData.writeByte(ClientPacketID.bugReport.ordinal());
        outgoingData.writeASCIIString(message);
    }

    public static void writeChangeDescription(String message) {
        outgoingData.writeByte(ClientPacketID.ChangeDescription.ordinal());
        outgoingData.writeASCIIString(message);
    }

    public static void writeGuildVote(String message) {
        outgoingData.writeByte(ClientPacketID.GuildVote.ordinal());
        outgoingData.writeASCIIString(message);
    }

    public static void writePunishments(String message) {
        outgoingData.writeByte(ClientPacketID.Punishments.ordinal());
        outgoingData.writeASCIIString(message);
    }

    public static void writeGamble(short amount) {
        outgoingData.writeByte(ClientPacketID.Gamble.ordinal());
        outgoingData.writeInteger(amount);
    }

    public static void writeLeaveFaction() {
        outgoingData.writeByte(ClientPacketID.LeaveFaction.ordinal());
    }

    public static void writeBankExtractGold(int amount) {
        outgoingData.writeByte(ClientPacketID.BankExtractGold.ordinal());
        outgoingData.writeLong(amount);
    }

    public static void writeBankDepositGold(int amount) {
        outgoingData.writeByte(ClientPacketID.BankDepositGold.ordinal());
        outgoingData.writeLong(amount);
    }

    public static void writeDenounce(String message) {
        outgoingData.writeByte(ClientPacketID.Denounce.ordinal());
        outgoingData.writeASCIIString(message);
    }

    public static void writeGuildFundate() {
        outgoingData.writeByte(ClientPacketID.GuildFundate.ordinal());
    }

    public static void writeGuildFundation(int clanType) {
        outgoingData.writeByte(ClientPacketID.GuildFundation.ordinal());
        outgoingData.writeByte(clanType);
    }

    public static void writePartyKick(String userName) {
        outgoingData.writeByte(ClientPacketID.PartyKick.ordinal());
        outgoingData.writeASCIIString(userName);
    }

    public static void writePartySetLeader(String userName) {
        outgoingData.writeByte(ClientPacketID.PartySetLeader.ordinal());
        outgoingData.writeASCIIString(userName);
    }

    public static void writePartyAcceptMember(String userName) {
        outgoingData.writeByte(ClientPacketID.PartyAcceptMember.ordinal());
        outgoingData.writeASCIIString(userName);
    }

     /* ##############################################
        #              COMANDOS DE GM                #
        ##############################################*/

    public static void writeGMMessage(String message) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.GMMessage.ordinal());

        outgoingData.writeASCIIString(message);
    }

    public static void writeShowName() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.showName.ordinal());
    }

    public static void writeOnlineRoyalArmy() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.OnlineRoyalArmy.ordinal());
    }

    public static void writeOnlineChaosLegion() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.OnlineChaosLegion.ordinal());
    }

    public static void writeGoNearby(String userName) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.GoNearby.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeComment(String message) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.Comment.ordinal());

        outgoingData.writeASCIIString(message);
    }

    public static void writeServerTime() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.serverTime.ordinal());
    }

    public static void writeWhere(String userName) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.Where.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeCreaturesInMap(short Map) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.CreaturesInMap.ordinal());

        outgoingData.writeInteger(Map);
    }

    public static void writeCreateItem(int itemIndex) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.CreateItem.ordinal());
        outgoingData.writeInteger((short) itemIndex);
    }

    public static void writeWarpMeToTarget() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.WarpMeToTarget.ordinal());
    }

    public static void writeWarpChar(String userName, short map, int x, int y) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.WarpChar.ordinal());

        outgoingData.writeASCIIString(userName);
        outgoingData.writeInteger(map);
        outgoingData.writeByte(x);
        outgoingData.writeByte(y);
    }

    public static void writeSilence(String userName) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.Silence.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeSOSShowList() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.SOSShowList.ordinal());
    }

    public static void writeShowServerForm() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.ShowServerForm.ordinal());
    }

    public static void writeGoToChar(String userName) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.GoToChar.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeInvisible() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.Invisible.ordinal());
    }

    public static void writeGMPanel() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.GMPanel.ordinal());
    }

    public static void writeWorking() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.Working.ordinal());
    }

    public static void writeHiding() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.Hiding.ordinal());
    }

    public static void writeJail(String userName, String reason, int time) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.Jail.ordinal());

        outgoingData.writeASCIIString(userName);
        outgoingData.writeASCIIString(reason);
        outgoingData.writeByte(time);
    }

    public static void writeKillNPC() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.KillNPC.ordinal());
    }

    public static void writeWarnUser(String userName, String reason) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.WarnUser.ordinal());

        outgoingData.writeASCIIString(userName);
        outgoingData.writeASCIIString(reason);
    }

    public static void writeEditChar(String userName, int editOption, String arg1, String arg2) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.EditChar.ordinal());

        outgoingData.writeASCIIString(userName);

        outgoingData.writeByte(editOption);

        outgoingData.writeASCIIString(arg1);
        outgoingData.writeASCIIString(arg2);


    }

    public static void writeRequestCharInfo(String userName) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.RequestCharInfo.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeRequestCharStats(String userName) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.RequestCharStats.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeRequestCharGold(String userName) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.RequestCharGold.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeRequestCharInventory(String userName) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.RequestCharInventory.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeRequestCharBank(String userName) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.RequestCharBank.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeRequestCharSkills(String userName) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.RequestCharSkills.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeReviveChar(String userName) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.ReviveChar.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeOnlineGM() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.OnlineGM.ordinal());
    }

    public static void writeOnlineMap(short map) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.OnlineMap.ordinal());

        outgoingData.writeInteger(map);
    }

    public static void writeForgive(String userName) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.Forgive.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeKick(String userName) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.Kick.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeExecute(String userName) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.Execute.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeBanChar(String userName, String reason) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.banChar.ordinal());

        outgoingData.writeASCIIString(userName);
        outgoingData.writeASCIIString(reason);
    }

    public static void writeUnbanChar(String userName) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.UnbanChar.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeNPCFollow() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.NPCFollow.ordinal());
    }

    public static void writeSummonChar() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.SummonChar.ordinal());
    }

    public static void writeSpawnListRequest() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.SpawnListRequest.ordinal());
    }

    public static void writeResetNPCInventory() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.ResetNPCInventory.ordinal());
    }

    public static void writeCleanWorld() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.CleanWorld.ordinal());
    }

    public static void writeServerMessage() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.ServerMessage.ordinal());
    }

    public static void writeNickToIP(String userName) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.nickToIP.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeIPToNick(int[] ip) {
        // Validar que el tamaño del array sea 4 bytes
        if (ip.length != 4) return; // IP inválida

        // Escribir el mensaje "IPToNick" en el buffer de datos salientes
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.IPToNick.ordinal());

        // Escribir cada byte de la IP en el buffer de datos salientes
        for (int b : ip) {
            outgoingData.writeByte(b);
        }
    }

    public static void writeGuildOnlineMembers(String guild) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.GuildOnlineMembers.ordinal());

        outgoingData.writeASCIIString(guild);
    }

    public static void writeTeleportCreate(short map, int x, int y, int radio) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.TeleportCreate.ordinal());

        outgoingData.writeInteger(map);
        outgoingData.writeByte(x);
        outgoingData.writeByte(y);
        outgoingData.writeByte(radio);
    }

    public static void writeTeleportDestroy() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.TeleportDestroy.ordinal());
    }

    public static void writeRainToggle() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.RainToggle.ordinal());
    }

    public static void writeSetCharDescription(String desc) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.SetCharDescription.ordinal());

        outgoingData.writeASCIIString(desc);
    }

    public static void writeForceMIDIToMap(int midiID, short map) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.ForceMIDIToMap.ordinal());

        outgoingData.writeByte(midiID);
        outgoingData.writeInteger(map);
    }

    public static void writeForceWAVEToMap(int waveID, short map, int x, int y) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.ForceWAVEToMap.ordinal());

        outgoingData.writeByte(waveID);

        outgoingData.writeInteger(map);

        outgoingData.writeByte(x);
        outgoingData.writeByte(y);
    }

    public static void writeRoyaleArmyMessage(String message) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.RoyalArmyMessage.ordinal());

        outgoingData.writeASCIIString(message);
    }

    public static void writeChaosLegionMessage(String message) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.ChaosLegionMessage.ordinal());

        outgoingData.writeASCIIString(message);
    }

    public static void writeCitizenMessage(String message) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.CitizenMessage.ordinal());

        outgoingData.writeASCIIString(message);
    }

    public static void writeCriminalMessage(String message) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.CriminalMessage.ordinal());

        outgoingData.writeASCIIString(message);
    }

    public static void writeTalkAsNPC(String message) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.TalkAsNPC.ordinal());

        outgoingData.writeASCIIString(message);
    }

    public static void writeDestroyAllItemsInArea() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.DestroyAllItemsInArea.ordinal());
    }

    public static void writeAcceptRoyalCouncilMember(String userName) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.AcceptRoyalCouncilMember.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeAcceptChaosCouncilMember(String userName) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.AcceptChaosCouncilMember.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeItemsInTheFloor() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.ItemsInTheFloor.ordinal());
    }

    public static void writeMakeDumb(String userName) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.MakeDumb.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeMakeDumbNoMore(String userName) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.MakeDumbNoMore.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeDumpIPTables() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.dumpIPTables.ordinal());
    }

    public static void writeCouncilKick(String userName) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.CouncilKick.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeSetTrigger(int trigger) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.SetTrigger.ordinal());

        outgoingData.writeByte(trigger);
    }

    public static void writeAskTrigger() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.AskTrigger.ordinal());
    }

    public static void writeBannedIPList() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.BannedIPList.ordinal());
    }

    public static void writeBannedIPReload() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.BannedIPReload.ordinal());
    }

    public static void writeGuildMemberList(String guild) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.GuildMemberList.ordinal());

        outgoingData.writeASCIIString(guild);
    }

    public static void writeGuildBan(String guild) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.GuildBan.ordinal());

        outgoingData.writeASCIIString(guild);
    }

    public static void writeBanIP(boolean byIp, int[] ip, String nick, String reason) {
        if (byIp && ip.length != 4) return; // IP inválida

        // Escribir el mensaje "BanIP" en el buffer de datos salientes
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.BanIP.ordinal());

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
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.UnbanIP.ordinal());

        // Escribir los componentes de la IP
        for (int b : ip) {
            outgoingData.writeByte(b);
        }
    }

    public static void writeDestroyItems() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.DestroyItems.ordinal());
    }

    public static void writeChaosLegionKick(String userName) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.ChaosLegionKick.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeRoyalArmyKick(String userName) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.RoyalArmyKick.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeForceMIDIAll(int midiID) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.ForceMIDIAll.ordinal());

        outgoingData.writeByte(midiID);
    }

    public static void writeForceWAVEAll(int waveID) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.ForceWAVEAll.ordinal());

        outgoingData.writeByte(waveID);
    }

    public static void writeRemovePunishment(String userName, int punishment, String newText) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.RemovePunishment.ordinal());

        outgoingData.writeASCIIString(userName);
        outgoingData.writeByte(punishment);
        outgoingData.writeASCIIString(newText);
    }

    public static void writeTileBlockedToggle() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.TileBlockedToggle.ordinal());
    }

    public static void writeKillNPCNoRespawn() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.KillNPCNoRespawn.ordinal());
    }

    public static void writeKillAllNearbyNPCs() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.KillAllNearbyNPCs.ordinal());
    }

    public static void writeLastIP(String userName) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.LastIP.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeChangeMOTD() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.ChangeMOTD.ordinal());
    }

    public static void writeSystemMessage(String message) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.SystemMessage.ordinal());

        outgoingData.writeASCIIString(message);
    }

    public static void writeCreateNPC(Short NPCIndex) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.CreateNPC.ordinal());

        outgoingData.writeInteger(NPCIndex);
    }

    public static void writeCreateNPCWithRespawn(Short NPCIndex) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.CreateNPCWithRespawn.ordinal());

        outgoingData.writeInteger(NPCIndex);
    }

    public static void writeImperialArmour(int armourIndex, short objectIndex) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.ImperialArmour.ordinal());

        outgoingData.writeByte(armourIndex);
        outgoingData.writeInteger(objectIndex);
    }

    public static void writeChaosArmour(int armourIndex, short objectIndex) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.ChaosArmour.ordinal());

        outgoingData.writeByte(armourIndex);
        outgoingData.writeInteger(objectIndex);
    }

    public static void writeNavigateToggle() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.NavigateToggle.ordinal());
    }

    public static void writeServerOpenToUsersToggle() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.ServerOpenToUsersToggle.ordinal());
    }

    public static void writeTurnOffServer() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.TurnOffServer.ordinal());
    }

    public static void writeTurnCriminal(String userName) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.TurnCriminal.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeResetFactions(String userName) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.ResetFactions.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeRemoveCharFromGuild(String userName) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.RemoveCharFromGuild.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeRequestCharMail(String userName) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.RequestCharMail.ordinal());

        outgoingData.writeASCIIString(userName);
    }

    public static void writeAlterPassword(String userName, String copyFrom) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.AlterPassword.ordinal());

        outgoingData.writeASCIIString(userName);
        outgoingData.writeASCIIString(copyFrom);
    }

    public static void writeAlterMail(String userName, String newMail) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.AlterMail.ordinal());

        outgoingData.writeASCIIString(userName);
        outgoingData.writeASCIIString(newMail);
    }

    public static void writeAlterName(String userName, String newName) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.AlterName.ordinal());

        outgoingData.writeASCIIString(userName);
        outgoingData.writeASCIIString(newName);
    }

    public static void writeCheckSlot(String userName, int slot) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.AlterName.ordinal());

        outgoingData.writeASCIIString(userName);
        outgoingData.writeByte(slot);
    }

    public static void writeToggleCentinelActivated() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.ToggleCentinelActivated.ordinal());
    }

    public static void writeDoBackup() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.DoBackUp.ordinal());
    }

    public static void writeShowGuildMessages(String guild) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.ShowGuildMessages.ordinal());

        outgoingData.writeASCIIString(guild);
    }

    public static void writeSaveMap() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.SaveMap.ordinal());
    }

    public static void writeChangeMapInfoPK(boolean isPK) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.ChangeMapInfoPK.ordinal());

        outgoingData.writeBoolean(isPK);
    }

    public static void writeChangeMapInfoBackup(boolean backup) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.ChangeMapInfoBackup.ordinal());

        outgoingData.writeBoolean(backup);
    }

    public static void writeChangeMapInfoRestricted(String restrict) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.ChangeMapInfoBackup.ordinal());

        outgoingData.writeASCIIString(restrict);
    }

    public static void writeChangeMapInfoNoMagic(boolean noMagic) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.ChangeMapInfoNoMagic.ordinal());

        outgoingData.writeBoolean(noMagic);
    }

    public static void writeChangeMapInfoNoInvi(boolean noInvi) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.ChangeMapInfoNoInvi.ordinal());

        outgoingData.writeBoolean(noInvi);
    }

    public static void writeChangeMapInfoNoResu(boolean noResu) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.ChangeMapInfoNoResu.ordinal());

        outgoingData.writeBoolean(noResu);
    }

    public static void writeChangeMapInfoLand(String land) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.ChangeMapInfoLand.ordinal());

        outgoingData.writeASCIIString(land);
    }

    public static void writeChangeMapInfoZone(String zone) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.ChangeMapInfoZone.ordinal());

        outgoingData.writeASCIIString(zone);
    }

    public static void writeSaveChars() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.SaveChars.ordinal());
    }

    public static void writeCleanSOS() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.CleanSOS.ordinal());
    }

    public static void writeNight() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.night.ordinal());
    }

    public static void writeKickAllChars() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.KickAllChars.ordinal());
    }

    public static void writeReloadNPCs() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.ReloadNPCs.ordinal());
    }

    public static void writeReloadServerIni() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.ReloadServerIni.ordinal());
    }

    public static void writeReloadSpells() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.ReloadSpells.ordinal());
    }

    public static void writeReloadObjects() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.ReloadObjects.ordinal());
    }

    public static void writeRestart() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.Restart.ordinal());
    }

    public static void writeResetAutoUpdate() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.ResetAutoUpdate.ordinal());
    }

    public static void writeChatColor(int r, int g, int b) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.ChatColor.ordinal());

        outgoingData.writeByte(r);
        outgoingData.writeByte(g);
        outgoingData.writeByte(b);
    }

    public static void writeIgnored() {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.Ignored.ordinal());
    }

    public static void writePing() {
        if (pingTime != 0) return;
        outgoingData.writeByte(ClientPacketID.Ping.ordinal());
        pingTime = (int) glfwGetTime();
    }

    public static void writeSetIniVar(String sLlave, String sClave, String sValor) {
        outgoingData.writeByte(ClientPacketID.GMCommands.ordinal());
        outgoingData.writeByte(eGMCommands.SetIniVar.ordinal());

        outgoingData.writeASCIIString(sLlave);
        outgoingData.writeASCIIString(sClave);
        outgoingData.writeASCIIString(sValor);
    }

    public static void writeHome() {
        outgoingData.writeByte(ClientPacketID.Home.ordinal());
    }

    public static void writeCommerceBuy(int slot, int amount) {
        outgoingData.writeByte(ClientPacketID.CommerceBuy.ordinal());
        outgoingData.writeByte(slot);
        outgoingData.writeInteger((short) amount);
    }

    public static void writeCommerceSell(int slot, int amount) {
        outgoingData.writeByte(ClientPacketID.CommerceSell.ordinal());
        outgoingData.writeByte(slot);
        outgoingData.writeInteger((short) amount);
    }

}
