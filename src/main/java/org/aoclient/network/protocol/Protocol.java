package org.aoclient.network.protocol;

import org.aoclient.engine.game.console.Console;
import org.aoclient.engine.game.Messages;
import org.aoclient.engine.game.User;
import org.aoclient.engine.game.console.FontStyle;
import org.aoclient.engine.game.models.Direction;
import org.aoclient.engine.game.models.Skill;
import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.PacketBuffer;
import org.aoclient.network.protocol.types.GMCommand;

import static org.aoclient.engine.utils.GameData.charList;
import static org.lwjgl.glfw.GLFW.glfwGetTime;


public class Protocol {

    private static final Console CONSOLE = Console.INSTANCE;
    private static final User USER = User.INSTANCE;
    public static int pingTime; // TODO Se podria sacar de aca?
    /** Buffer para la salida de bytes (escribe lo que envia el cliente al servidor). */
    public static PacketBuffer outputBuffer = new PacketBuffer();
    /** Buffer para la entrada de bytes (lee lo que recibe el cliente del servidor). */
    public static PacketBuffer inputBuffer = new PacketBuffer();

    public static void acceptChaosCouncil(String player) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.ACCEPT_CHAOS_COUNCIL.getId());
        outputBuffer.writeCp1252String(player);
    }

    public static void acceptRoyalCouncil(String player) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.ACCEPT_ROYAL_COUNCIL.getId());
        outputBuffer.writeCp1252String(player);
    }

    public static void adminServer() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.ADMIN_SERVER.getId());
    }

    public static void alternatePassword(String playerWithoutPassword, String playerWithPassword) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.ALTERNATE_PASSWORD.getId());
        outputBuffer.writeCp1252String(playerWithoutPassword);
        outputBuffer.writeCp1252String(playerWithPassword);
    }

    public static void attack() {
        outputBuffer.writeByte(ClientPacket.ATTACK.getId());
    }

    public static void autoUpdate() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.AUTO_UPDATE.getId());
    }

    public static void backup() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.BACKUP.getId());
    }

    public static void balance() {
        if (charList[USER.getUserCharIndex()].isDead()) {
            CONSOLE.addMsgToConsole(Messages.get(Messages.MessageKey.ESTAS_MUERTO), FontStyle.ITALIC, new RGBColor());
            return;
        }
        outputBuffer.writeByte(ClientPacket.BALANCE.getId());
    }

    public static void ban(String player, String reason) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.BAN.getId());
        outputBuffer.writeCp1252String(player);
        outputBuffer.writeCp1252String(reason);
    }

    public static void banIP(boolean byIp, int[] ip, String nick, String reason) {
        if (byIp && ip.length != 4) return; // IP invalida
        // Escribir el mensaje "BanIP" en el buffer de datos salientes
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.BAN_IP.getId());
        // Escribir si es por IP o por nick
        outputBuffer.writeBoolean(byIp);
        // Si es por IP, escribe los componentes de la IP
        if (byIp) {
            for (int b : ip)
                outputBuffer.writeByte(b);
        } else outputBuffer.writeCp1252String(nick); // Si es por nick, escribe el nick
        // Escribe el motivo del baneo
        outputBuffer.writeCp1252String(reason);
    }

    public static void banIpList() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.BAN_IP_LIST.getId());
    }

    public static void banIpReload() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.BAN_IP_RELOAD.getId());
    }

    public static void bankDeposit(int slot, int amount) {
        outputBuffer.writeByte(ClientPacket.BANK_DEPOSIT.getId());
        outputBuffer.writeByte(slot);
        outputBuffer.writeInteger((short) amount);
    }

    public static void bankEnd() {
        outputBuffer.writeByte(ClientPacket.BANK_END.getId());
    }

    public static void bankExtractItem(int slot, int amount) {
        outputBuffer.writeByte(ClientPacket.BANK_EXTRACT_ITEM.getId());
        outputBuffer.writeByte(slot);
        outputBuffer.writeInteger((short) amount);
    }

    public static void bet(short amount) {
        outputBuffer.writeByte(ClientPacket.BET.getId());
        outputBuffer.writeInteger(amount);
    }

    public static void blockTile() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.BLOCK_TILE.getId());
    }

    public static void bug(String desc) {
        outputBuffer.writeByte(ClientPacket.BUG.getId());
        outputBuffer.writeCp1252String(desc);
    }

    public static void castSpell(int slot) {
        outputBuffer.writeByte(ClientPacket.CAST_SPELL.getId());
        outputBuffer.writeByte(slot);
    }

    public static void changeEmail(String nick, String email) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CHANGE_EMAIL.getId());
        outputBuffer.writeCp1252String(nick);
        outputBuffer.writeCp1252String(email);
    }

    public static void changeHeading(Direction direction) {
        outputBuffer.writeByte(ClientPacket.CHANGE_HEADING.getId());
        outputBuffer.writeByte(direction.getId());
    }

    public static void changeMOTD() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CHANGE_MOTD.getId());
    }

    public static void changeMapInfoBackup(boolean backup) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CHANGE_MAP_INFO_BACKUP.getId());
        outputBuffer.writeBoolean(backup);
    }

    public static void changeMapInfoLand(String land) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CHANGE_MAP_INFO_LAND.getId());
        outputBuffer.writeCp1252String(land);
    }

    public static void changeMapInfoNoInvi(boolean noInvi) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CHANGE_MAP_INFO_NO_INVI.getId());
        outputBuffer.writeBoolean(noInvi);
    }

    public static void changeMapInfoNoMagic(boolean noMagic) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CHANGE_MAP_INFO_NO_MAGIC.getId());
        outputBuffer.writeBoolean(noMagic);
    }

    public static void changeMapInfoNoResu(boolean noResu) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CHANGE_MAP_INFO_NO_RESU.getId());
        outputBuffer.writeBoolean(noResu);
    }

    public static void changeMapInfoPK(boolean isPK) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CHANGE_MAP_INFO_PK.getId());
        outputBuffer.writeBoolean(isPK);
    }

    public static void changeMapInfoRestricted(String restrict) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CHANGE_MAP_INFO_RESTRICTED.getId());
        outputBuffer.writeCp1252String(restrict);
    }

    public static void changeMapInfoZone(String zone) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CHANGE_MAP_INFO_ZONE.getId());
        outputBuffer.writeCp1252String(zone);
    }

    public static void changeNick(String nick, String newNick) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CHANGE_NICK.getId());
        outputBuffer.writeCp1252String(nick);
        outputBuffer.writeCp1252String(newNick);
    }

    public static void changePassword(String oldPass, String newPass) {
        outputBuffer.writeByte(ClientPacket.CHANGE_PASSWORD.getId());
        outputBuffer.writeCp1252String(oldPass);
        outputBuffer.writeCp1252String(newPass);
    }

    public static void chaosArmour(int armourIndex, short objectIndex) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CHAOS_ARMOUR.getId());
        outputBuffer.writeByte(armourIndex);
        outputBuffer.writeInteger(objectIndex);
    }

    public static void chaosMessage(String message) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CHAOS_MESSAGE.getId());
        outputBuffer.writeCp1252String(message);
    }

    public static void chatColor(int r, int g, int b) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CHAT_COLOR.getId());
        outputBuffer.writeByte(r);
        outputBuffer.writeByte(g);
        outputBuffer.writeByte(b);
    }

    public static void citizenMessage(String message) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CITIZEN_MESSAGE.getId());
        outputBuffer.writeCp1252String(message);
    }

    public static void cleanNpcInventory() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CLEAN_NPC_INVENTORY.getId());
    }

    public static void cleanSOS() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CLEAN_SOS.getId());
    }

    public static void cleanWorld() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CLEAN_WORLD.getId());
    }

    public static void commentServerLog(String message) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.COMMENT_SERVER_LOG.getId());
        outputBuffer.writeCp1252String(message);
    }

    public static void commerceBuy(int slot, int amount) {
        outputBuffer.writeByte(ClientPacket.COMMERCE_BUY.getId());
        outputBuffer.writeByte(slot);
        outputBuffer.writeInteger((short) amount);
    }

    public static void commerceEnd() {
        outputBuffer.writeByte(ClientPacket.COMMERCE_END.getId());
    }

    public static void commerceSell(int slot, int amount) {
        outputBuffer.writeByte(ClientPacket.COMMERCE_SELL.getId());
        outputBuffer.writeByte(slot);
        outputBuffer.writeInteger((short) amount);
    }

    public static void consultation() {
        outputBuffer.writeByte(ClientPacket.CONSULTATION.getId());
    }

    public static void councilMessage(String message) {
        outputBuffer.writeByte(ClientPacket.COUNCIL_MSG.getId());
        outputBuffer.writeCp1252String(message);
    }

    public static void createNpc(Short npc) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CREATE_NPC.getId());
        outputBuffer.writeInteger(npc);
    }

    public static void createNpcRespawn(Short npc) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CREATE_NPC_WITH_RESPAWN.getId());
        outputBuffer.writeInteger(npc);
    }

    public static void createObj(int objId) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CREATE_OBJ.getId());
        outputBuffer.writeInteger((short) objId);
    }

    public static void createTeleport(short map, int x, int y, int radio) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CREATE_TELEPORT.getId());
        outputBuffer.writeInteger(map);
        outputBuffer.writeByte(x);
        outputBuffer.writeByte(y);
        outputBuffer.writeByte(radio);
    }

    public static void criminalMessage(String message) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.CRIMINAL_MESSAGE.getId());
        outputBuffer.writeCp1252String(message);
    }

    public static void desc(String desc) {
        outputBuffer.writeByte(ClientPacket.DESC.getId());
        outputBuffer.writeCp1252String(desc);
    }

    public static void depositGold(int amount) {
        outputBuffer.writeByte(ClientPacket.DEPOSIT_GOLD.getId());
        outputBuffer.writeLong(amount);
    }

    public static void doubleClick(int x, int y) {
        outputBuffer.writeByte(ClientPacket.DOUBLE_CLICK.getId());
        outputBuffer.writeByte(x);
        outputBuffer.writeByte(y);
    }

    public static void drop(int slot, int amount) {
        outputBuffer.writeByte(ClientPacket.DROP.getId());
        outputBuffer.writeByte((byte) slot);
        outputBuffer.writeInteger((short) amount);
    }

    public static void dumb(String player) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.DUMB.getId());
        outputBuffer.writeCp1252String(player);
    }

    public static void dumpSecurity() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.DUMP_SECURITY.getId());
    }

    public static void enlist() {
        outputBuffer.writeByte(ClientPacket.ENLIST.getId());
    }

    public static void equipItem(int slot) {
        outputBuffer.writeByte(ClientPacket.EQUIP_ITEM.getId());
        outputBuffer.writeByte(slot);
    }

    public static void extractGold(int amount) {
        outputBuffer.writeByte(ClientPacket.EXTRACT_GOLD.getId());
        outputBuffer.writeLong(amount);
    }

    public static void forgive(String player) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.FORGIVE.getId());
        outputBuffer.writeCp1252String(player);
    }

    public static void GM() {
        outputBuffer.writeByte(ClientPacket.GM.getId());
    }

    public static void GMMessage(String message) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.GM_MESSAGE.getId());
        outputBuffer.writeCp1252String(message);
    }

    public static void GMPanel() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.GM_PANEL.getId());
    }

    public static void guildBan(String guild) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.GUILD_BAN.getId());
        outputBuffer.writeCp1252String(guild);
    }

    public static void guildFound() {
        outputBuffer.writeByte(ClientPacket.GUILD_FOUND.getId());
    }

    public static void guildFundation(int guildType) {
        outputBuffer.writeByte(ClientPacket.GUILD_FUNDATION.getId());
        outputBuffer.writeByte(guildType);
    }

    public static void guildKick(String player) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.GUILD_KICK.getId());
        outputBuffer.writeCp1252String(player);
    }

    public static void guildLeave() {
        outputBuffer.writeByte(ClientPacket.GUILD_LEAVE.getId());
    }

    public static void guildMembers(String guild) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.GUILD_MEMBERS.getId());
        outputBuffer.writeCp1252String(guild);
    }

    public static void guildMessage(String message) {
        outputBuffer.writeByte(ClientPacket.GUILD_MSG.getId());
        outputBuffer.writeCp1252String(message);
    }

    public static void guildMessages(String guild) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.SHOW_GUILD_MESSAGES.getId());
        outputBuffer.writeCp1252String(guild);
    }

    public static void guildOnline() {
        outputBuffer.writeByte(ClientPacket.GUILD_ONLINE.getId());
    }

    public static void guildOnlineSpecific(String guild) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.GUILD_ONLINE_MEMBERS.getId());
        outputBuffer.writeCp1252String(guild);
    }

    public static void guildVote(String player) {
        outputBuffer.writeByte(ClientPacket.GUILD_VOTE.getId());
        outputBuffer.writeCp1252String(player);
    }

    public static void heal() {
        outputBuffer.writeByte(ClientPacket.HEAL.getId());
    }

    public static void help() {
        outputBuffer.writeByte(ClientPacket.HELP.getId());
    }

    public static void home() {
        outputBuffer.writeByte(ClientPacket.HOME.getId());
    }

    public static void imperialArmour(int armourId, short objId) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.IMPERIAL_ARMOUR.getId());
        outputBuffer.writeByte(armourId);
        outputBuffer.writeInteger(objId);
    }

    public static void information() {
        outputBuffer.writeByte(ClientPacket.INFORMATION.getId());
    }

    public static void inquiry() {
        outputBuffer.writeByte(ClientPacket.INQUIRY.getId());
    }

    public static void invisibility() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.INVISIBILITY.getId());
    }

    public static void IpToNick(int[] ip) {
        // Valida que el tamaño del array sea de 4 bytes
        if (ip.length != 4) return; // IP invalida
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.IP_TO_NICK.getId());
        // Escribe cada byte de la IP en el buffer de datos salientes
        for (int b : ip) outputBuffer.writeByte(b);
    }

    public static void jail(String player, String reason, int time) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.JAIL.getId());
        outputBuffer.writeCp1252String(player);
        outputBuffer.writeCp1252String(reason);
        outputBuffer.writeByte(time);
    }

    public static void kick(String player) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.KICK.getId());
        outputBuffer.writeCp1252String(player);
    }

    public static void kickAll() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.KICK_ALL.getId());
    }

    public static void kickCouncil(String player) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.KICK_COUNCIL.getId());
        outputBuffer.writeCp1252String(player);
    }

    public static void kill(String player) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.KILL.getId());
        outputBuffer.writeCp1252String(player);
    }

    public static void lastIp(String player) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.LAST_IP.getId());
        outputBuffer.writeCp1252String(player);
    }

    public static void leaveFaction() {
        outputBuffer.writeByte(ClientPacket.LEAVE_FACTION.getId());
    }

    public static void leftClick(int x, int y) {
        outputBuffer.writeByte(ClientPacket.LEFT_CLICK.getId());
        outputBuffer.writeByte(x);
        outputBuffer.writeByte(y);
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

    public static void map(short map) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.ONLINE_MAP.getId());
        outputBuffer.writeInteger(map);
    }

    public static void meditate() {
        if (USER.getUserMaxMAN() == USER.getUserMinMAN()) return;
        if (charList[USER.getUserCharIndex()].isDead()) {
            CONSOLE.addMsgToConsole(Messages.get(Messages.MessageKey.ESTAS_MUERTO), FontStyle.ITALIC, new RGBColor());
            return;
        }
        outputBuffer.writeByte(ClientPacket.MEDITATE.getId());
    }

    public static void mobPanel() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.MOB_PANEL.getId());
    }

    public static void modPlayer(String player, int property, String value, String extraParam) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.MOD_PLAYER.getId());
        outputBuffer.writeCp1252String(player);
        outputBuffer.writeByte(property);
        outputBuffer.writeCp1252String(value);
        outputBuffer.writeCp1252String(extraParam);
    }

    public static void modifySkills(int[] skills) {
        outputBuffer.writeByte(ClientPacket.MODIFY_SKILLS.getId());
        for (Skill skill : Skill.values())
            outputBuffer.writeByte(skills[skill.getId() - 1]);
    }

    public static void train(int creatureIndex) {
        outputBuffer.writeByte(ClientPacket.TRAIN.getId());
        outputBuffer.writeByte(creatureIndex);
    }

    public static void MOTD() {
        outputBuffer.writeByte(ClientPacket.MOTD.getId());
    }

    public static void navigation() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.NAVIGATION.getId());
    }

    public static void nickToIp(String nick) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.NICK_TO_IP.getId());
        outputBuffer.writeCp1252String(nick);
    }

    public static void night() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.NIGHT.getId());
    }

    public static void noDumb(String player) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.NO_DUMB.getId());
        outputBuffer.writeCp1252String(player);
    }

    public static void NpcFollow() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.NPC_FOLLOW.getId());
    }

    public static void online() {
        outputBuffer.writeByte(ClientPacket.ONLINE.getId());
    }

    public static void onlineChaos() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.ONLINE_CHAOS.getId());
    }

    public static void onlineGM() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.ONLINE_GM.getId());
    }

    public static void onlineRoyal() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.ONLINE_ROYAL.getId());
    }

    public static void openBank() {
        outputBuffer.writeByte(ClientPacket.OPEN_BANK.getId());
    }

    public static void partyAccept(String player) {
        outputBuffer.writeByte(ClientPacket.PARTY_ACCEPT.getId());
        outputBuffer.writeCp1252String(player);
    }

    public static void partyCreate() {
        if (charList[USER.getUserCharIndex()].isDead()) {
            CONSOLE.addMsgToConsole(Messages.get(Messages.MessageKey.ESTAS_MUERTO), FontStyle.ITALIC, new RGBColor());
            return;
        }
        outputBuffer.writeByte(ClientPacket.PARTY_CREATE.getId());
    }

    public static void partyJoin() {
        if (charList[USER.getUserCharIndex()].isDead()) {
            CONSOLE.addMsgToConsole(Messages.get(Messages.MessageKey.ESTAS_MUERTO), FontStyle.ITALIC, new RGBColor());
            return;
        }
        outputBuffer.writeByte(ClientPacket.PARTY_JOIN.getId());
    }

    public static void partyKick(String player) {
        outputBuffer.writeByte(ClientPacket.PARTY_KICK.getId());
        outputBuffer.writeCp1252String(player);
    }

    public static void partyLeave() {
        outputBuffer.writeByte(ClientPacket.PARTY_LEAVE.getId());
    }

    public static void partyMessage(String message) {
        outputBuffer.writeByte(ClientPacket.PARTY_MESSAGE.getId());
        outputBuffer.writeCp1252String(message);
    }

    public static void partyOnline() {
        outputBuffer.writeByte(ClientPacket.PARTY_ONLINE.getId());
    }

    public static void partySetLeader(String player) {
        outputBuffer.writeByte(ClientPacket.PARTY_SET_LEADER.getId());
        outputBuffer.writeCp1252String(player);
    }

    public static void petFollow() {
        if (charList[USER.getUserCharIndex()].isDead()) {
            CONSOLE.addMsgToConsole(Messages.get(Messages.MessageKey.ESTAS_MUERTO), FontStyle.ITALIC, new RGBColor());
            return;
        }
        outputBuffer.writeByte(ClientPacket.PET_FOLLOW.getId());
    }

    public static void petRelease() {
        if (charList[USER.getUserCharIndex()].isDead()) {
            CONSOLE.addMsgToConsole(Messages.get(Messages.MessageKey.ESTAS_MUERTO), FontStyle.ITALIC, new RGBColor());
            return;
        }
        outputBuffer.writeByte(ClientPacket.PET_RELEASE.getId());
    }

    public static void petStay() {
        if (charList[USER.getUserCharIndex()].isDead()) {
            CONSOLE.addMsgToConsole(Messages.get(Messages.MessageKey.ESTAS_MUERTO), FontStyle.ITALIC, new RGBColor());
            return;
        }
        outputBuffer.writeByte(ClientPacket.PET_STAY.getId());
    }

    public static void pickUp() {
        outputBuffer.writeByte(ClientPacket.PICK_UP.getId());
    }

    public static void ping() {
        if (pingTime != 0) return;
        outputBuffer.writeByte(ClientPacket.PING.getId());
        pingTime = (int) glfwGetTime();
    }

    public static void playerBank(String player) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.PLAYER_BANK.getId());
        outputBuffer.writeCp1252String(player);
    }

    public static void playerEmail(String player) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.PLAYER_EMAIL.getId());
        outputBuffer.writeCp1252String(player);
    }

    public static void playerGold(String player) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.PLAYER_GOLD.getId());
        outputBuffer.writeCp1252String(player);
    }

    public static void playerInfo(String player) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.PLAYER_INFO.getId());
        outputBuffer.writeCp1252String(player);
    }

    public static void playerInv(String player) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.PLAYER_INVENTORY.getId());
        outputBuffer.writeCp1252String(player);
    }

    public static void playerLocation(String player) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.PLAYER_LOCATION.getId());
        outputBuffer.writeCp1252String(player);
    }

    public static void playerSkills(String player) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.PLAYER_SKILLS.getId());
        outputBuffer.writeCp1252String(player);
    }

    public static void playerSlot(String player, int slot) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.PLAYER_SLOT.getId());
        outputBuffer.writeCp1252String(player);
        outputBuffer.writeByte(slot);
    }

    public static void playerStats(String player) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.STATS.getId());
        outputBuffer.writeCp1252String(player);
    }

    public static void playMusic(int music) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.PLAY_MUSIC.getId());
        outputBuffer.writeByte(music);
    }

    public static void playMusicMap(int music, short map) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.PLAY_MUSIC_MAP.getId());
        outputBuffer.writeByte(music);
        outputBuffer.writeInteger(map);
    }

    public static void playSound(int idSound) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.PLAY_SOUND.getId());
        outputBuffer.writeByte(idSound);
    }

    public static void playSoundAtLocation(int sound, short map, int x, int y) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.PLAY_SOUND_AT_LOCATION.getId());
        outputBuffer.writeByte(sound);
        outputBuffer.writeInteger(map);
        outputBuffer.writeByte(x);
        outputBuffer.writeByte(y);
    }

    public static void poll(int option) {
        outputBuffer.writeByte(ClientPacket.POLL.getId());
        outputBuffer.writeByte(option);
    }

    public static void punishments(String message) {
        outputBuffer.writeByte(ClientPacket.PUNISHMENTS.getId());
        outputBuffer.writeCp1252String(message);
    }

    public static void quit() {
        if (charList[USER.getUserCharIndex()].isParalizado()) {
            CONSOLE.addMsgToConsole(Messages.get(Messages.MessageKey.NO_SALIR_PARALIZADO), FontStyle.ITALIC, new RGBColor());
            return;
        }
        outputBuffer.writeByte(ClientPacket.QUIT.getId());
    }

    public static void rain() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.RAIN.getId());
    }

    public static void reloadNPC() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.RELOAD_NPC.getId());
    }

    public static void reloadObj() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.RELOAD_OBJ.getId());
    }

    public static void reloadServerIni() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.RELOAD_SERVER_INI.getId());
    }

    public static void reloadSpell() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.RELOAD_SPELL.getId());
    }

    public static void removeArmy(String player) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.REMOVE_ARMY.getId());
        outputBuffer.writeCp1252String(player);
    }

    public static void removeChaos(String player) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.REMOVE_CHAOS.getId());
        outputBuffer.writeCp1252String(player);
    }

    public static void removeFactions(String player) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.REMOVE_FACTIONS.getId());
        outputBuffer.writeCp1252String(player);
    }

    public static void removeItem() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.REMOVE_ITEM.getId());
    }

    public static void removeItemArea() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.REMOVE_ITEM_AREA.getId());
    }

    public static void removeNpc() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.REMOVE_NPC.getId());
    }

    public static void removeNpcArea() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.REMOVE_NPC_AREA.getId());
    }

    public static void removeNpcNoRespawn() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.REMOVE_NPC_NO_RESPAWN.getId());
    }

    public static void removePunishment(String player, int punishment, String newText) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.REMOVE_PUNISHMENT.getId());
        outputBuffer.writeCp1252String(player);
        outputBuffer.writeByte(punishment);
        outputBuffer.writeCp1252String(newText);
    }

    public static void removeTeleport() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.REMOVE_TELEPORT.getId());
    }

    public static void report(String message) {
        outputBuffer.writeByte(ClientPacket.REPORT.getId());
        outputBuffer.writeCp1252String(message);
    }

    public static void requestAttributes() {
        outputBuffer.writeByte(ClientPacket.REQUEST_ATTRIBUTES.getId());
    }

    public static void requestFame() {
        outputBuffer.writeByte(ClientPacket.REQUEST_FAME.getId());
    }

    public static void requestGuildLeaderInfo() {
        outputBuffer.writeByte(ClientPacket.REQUEST_GUILD_LEADER_INFO.getId());
    }

    public static void requestMiniStats() {
        outputBuffer.writeByte(ClientPacket.REQUEST_MINI_STATS.getId());
    }

    public static void requestPositionUpdate() {
        outputBuffer.writeByte(ClientPacket.REQUEST_POSITION_UPDATE.getId());
    }

    public static void requestSkills() {
        outputBuffer.writeByte(ClientPacket.REQUEST_SKILLS.getId());
    }

    public static void rest() {
        if (charList[USER.getUserCharIndex()].isDead()) {
            CONSOLE.addMsgToConsole(Messages.get(Messages.MessageKey.ESTAS_MUERTO), FontStyle.ITALIC, new RGBColor());
            return;
        }
        outputBuffer.writeByte(ClientPacket.REST.getId());
    }

    public static void restart() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.RESTART.getId());
    }

    public static void resucitationToggle() {
        outputBuffer.writeByte(ClientPacket.RESUSCITATION_SAFE_TOGGLE.getId());
    }

    public static void resurrect() {
        outputBuffer.writeByte(ClientPacket.RESURRECT.getId());
    }

    public static void revive(String player) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.REVIVE.getId());
        outputBuffer.writeCp1252String(player);
    }

    public static void reward() {
        outputBuffer.writeByte(ClientPacket.REWARD.getId());
    }

    public static void rmsg(String message) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.RMSG.getId());
        outputBuffer.writeCp1252String(message);
    }

    public static void rol(String message) {
        outputBuffer.writeByte(ClientPacket.ROL.getId());
        outputBuffer.writeCp1252String(message);
    }

    public static void royalArmyMessage(String message) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.ROYAL_ARMY_MESSAGE.getId());
        outputBuffer.writeCp1252String(message);
    }

    public static void safeToggle() {
        outputBuffer.writeByte(ClientPacket.SAFE_TOGGLE.getId());
    }

    public static void saveChar() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.SAVE_CHAR.getId());
    }

    public static void saveMap() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.SAVE_MAP.getId());
    }

    public static void sentinel() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.SENTINEL.getId());
    }

    public static void sentinelCode(int code) {
        outputBuffer.writeByte(ClientPacket.SENTINEL_CODE.getId());
        outputBuffer.writeByte(code);
    }

    public static void setDesc(String desc) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.SET_DESC.getId());
        outputBuffer.writeCp1252String(desc);
    }

    public static void setIniVar(String sLlave, String sClave, String sValor) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.SET_INI_VAR.getId());
        outputBuffer.writeCp1252String(sLlave);
        outputBuffer.writeCp1252String(sClave);
        outputBuffer.writeCp1252String(sValor);
    }

    public static void setTrigger(int trigger) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.SET_TRIGGER.getId());
        outputBuffer.writeByte(trigger);
    }

    public static void shareNpc() {
        if (charList[USER.getUserCharIndex()].isDead()) {
            CONSOLE.addMsgToConsole(Messages.get(Messages.MessageKey.ESTAS_MUERTO), FontStyle.ITALIC, new RGBColor());
            return;
        }
        outputBuffer.writeByte(ClientPacket.SHARE_NPC.getId());
    }

    public static void showHidden() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.SHOW_HIDDEN.getId());
    }

    public static void showIgnored() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.SHOW_IGNORED.getId());
    }

    public static void showMobs(short map) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.SHOW_MOBS.getId());
        outputBuffer.writeInteger(map);
    }

    public static void showName() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.SHOW_NAME.getId());
    }

    public static void showObjMap() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.SHOW_OBJ_MAP.getId());
    }

    public static void showServerForm() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.SHOW_SERVER_FORM.getId());
    }

    public static void showTrigger() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.SHOW_TRIGGER.getId());
    }

    public static void showWorkers() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.SHOW_WORKERS.getId());
    }

    public static void shutdown() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.SHUTDOWN.getId());
    }

    public static void silence(String player) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.SILENCE.getId());
        outputBuffer.writeCp1252String(player);
    }

    public static void SOSShowList() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.SOS_SHOW_LIST.getId());
    }

    public static void SOSRemove(String player) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.SOS_REMOVE.getId());
        outputBuffer.writeCp1252String(player);
    }

    public static void spawnCreature(short creatureIndex) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.SPAWN_CREATURE.getId());
        outputBuffer.writeInteger(creatureIndex);
    }

    public static void spellInfo(final int slot) {
        outputBuffer.writeByte(ClientPacket.SPELL_INFO.getId());
        outputBuffer.writeByte(slot);
    }

    public static void stats() {
        outputBuffer.writeByte(ClientPacket.STATS.getId());
    }

    public static void stopSharingNpc() {
        if (charList[USER.getUserCharIndex()].isDead()) {
            CONSOLE.addMsgToConsole(Messages.get(Messages.MessageKey.ESTAS_MUERTO), FontStyle.ITALIC, new RGBColor());
            return;
        }
        outputBuffer.writeByte(ClientPacket.STOP_SHARING_NPC.getId());
    }

    public static void summon(String player) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.SUMMON.getId());
        outputBuffer.writeCp1252String(player);
    }

    public static void systemMessage(String message) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.SYSTEM_MESSAGE.getId());
        outputBuffer.writeCp1252String(message);
    }

    /**
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
     * public static void writeTalk(String message) {
     *     outputBuffer.writeByte(ClientPacket.TALK.getId());
     *     outputBuffer.writeCp1252String(message);
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
     * String message = tempBuffer.readCp1252String(); // El cliente añadio esto!
     * short charIndex = tempBuffer.readInteger(); // El servidor desde el codigo de VB6 añadio esto!
     * int r = tempBuffer.readByte(); // El servidor desde el codigo de VB6 añadio esto!
     * int g = tempBuffer.readByte(); // El servidor desde el codigo de VB6 añadio esto!
     * int b = tempBuffer.readByte(); // El servidor desde el codigo de VB6 añadio esto!
     * }</pre>
     * </ol>
     * <p>
     * Ver: network-guide.md
     */
    public static void talk(String message) {
        outputBuffer.writeByte(ClientPacket.TALK.getId());
        outputBuffer.writeCp1252String(message);
    }

    public static void talkAsNpc(String message) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.TALK_AS_NPC.getId());
        outputBuffer.writeCp1252String(message);
    }

    public static void teleport(String nick, short map, int x, int y) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.TELEPORT.getId());
        outputBuffer.writeCp1252String(nick);
        outputBuffer.writeInteger(map);
        outputBuffer.writeByte(x);
        outputBuffer.writeByte(y);
    }

    public static void teleportNearToPlayer(String player) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.TELEPORT_NEAR_TO_PLAYER.getId());
        outputBuffer.writeCp1252String(player);
    }

    public static void teleportToPlayer(String player) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.TELEPORT_TO_PLAYER.getId());
        outputBuffer.writeCp1252String(player);
    }

    public static void teleportToTarget() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.TELEPORT_TO_TARGET.getId());
    }

    public static void throwDices() {
        outputBuffer.writeByte(ClientPacket.THROW_DICES.getId());
    }

    public static void time() {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.TIME.getId());
    }

    public static void trade() {
        outputBuffer.writeByte(ClientPacket.TRADE.getId());
    }

    public static void trainList() {
        if (charList[USER.getUserCharIndex()].isDead()) {
            CONSOLE.addMsgToConsole(Messages.get(Messages.MessageKey.ESTAS_MUERTO), FontStyle.ITALIC, new RGBColor());
            return;
        }
        outputBuffer.writeByte(ClientPacket.TRAIN_LIST.getId());
    }

    public static void turnCriminal(String player) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.TURN_CRIMINAL.getId());
        outputBuffer.writeCp1252String(player);
    }

    public static void unban(String player) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.UNBAN.getId());
        outputBuffer.writeCp1252String(player);
    }

    public static void unbanIp(int[] ip) {
        if (ip.length != 4) return; // IP invalida
        // Escribir el mensaje "UnbanIP" en el buffer de datos salientes
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.UNBAN_IP.getId());
        // Escribir los componentes de la IP
        for (int b : ip) outputBuffer.writeByte(b);
    }

    public static void uptime() {
        outputBuffer.writeByte(ClientPacket.UPTIME.getId());
    }

    public static void useItem(int slot) {
        outputBuffer.writeByte(ClientPacket.USE_ITEM.getId());
        outputBuffer.writeByte(slot);
    }

    public static void walk(Direction direction) {
        outputBuffer.writeByte(ClientPacket.WALK.getId());
        outputBuffer.writeByte(direction.getId());
    }

    public static void warning(String player, String reason) {
        outputBuffer.writeByte(ClientPacket.GM_COMMANDS.getId());
        outputBuffer.writeByte(GMCommand.WARNING.getId());
        outputBuffer.writeCp1252String(player);
        outputBuffer.writeCp1252String(reason);
    }

    public static void whisper(short charIndex, String chat) {
        outputBuffer.writeByte(ClientPacket.WHISPER.getId());
        outputBuffer.writeInteger(charIndex);
        outputBuffer.writeCp1252String(chat);
    }

    public static void work(int skill) {
        if (USER.isDead()) {
            CONSOLE.addMsgToConsole(Messages.get(Messages.MessageKey.ESTAS_MUERTO), FontStyle.ITALIC, new RGBColor());
            return;
        }
        outputBuffer.writeByte(ClientPacket.WORK.getId());
        outputBuffer.writeByte(skill);
    }

    public static void workLeftClick(int x, int y, int skill) {
        outputBuffer.writeByte(ClientPacket.WORK_LEFT_CLICK.getId());
        outputBuffer.writeByte(x);
        outputBuffer.writeByte(y);
        outputBuffer.writeByte(skill);
    }

}
