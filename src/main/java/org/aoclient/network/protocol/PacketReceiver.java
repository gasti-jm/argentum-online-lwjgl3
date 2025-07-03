package org.aoclient.network.protocol;

import org.aoclient.network.PacketBuffer;
import org.aoclient.network.protocol.handlers.*;
import org.aoclient.network.protocol.handlers.gm.*;
import org.tinylog.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Actua como un despachador que redirige cada paquete a su correspondiente manejador mediante una asociacion entre el tipo de
 * paquete del servidor y una implementacion concreta del manejador.
 */

public class PacketReceiver {

    public static ServerPacket serverPacket;
    /** Almacena la asociacion entre paquetes del servidor y sus respectivos handlers. */
    private final Map<ServerPacket, PacketHandler> handlers = new HashMap<>();

    public PacketReceiver() {
        registerHandlers();
    }

    /**
     * Registra los handlers asociados a los diferentes paquetes del servidor.
     */
    private void registerHandlers() {
        handlers.put(ServerPacket.LOGGED, new LoggedHandler());
        handlers.put(ServerPacket.REMOVE_DIALOGS, new RemoveDialogsHandler());
        handlers.put(ServerPacket.REMOVE_CHAR_DIALOG, new RemoveCharDialogHandler());
        handlers.put(ServerPacket.NAVIGATE_TOGGLE, new NavigateToggleHandler());
        handlers.put(ServerPacket.DISCONNECT, new DisconnectHandler());
        handlers.put(ServerPacket.COMMERCE_END, new CommerceEndHandler());
        handlers.put(ServerPacket.COMMERCE_CHAT, new CommerceChatHandler());
        handlers.put(ServerPacket.BANK_END, new BankEndHandler());
        handlers.put(ServerPacket.COMMERCE_INIT, new CommerceInitHandler());
        handlers.put(ServerPacket.BANK_INIT, new BankInitHandler());
        handlers.put(ServerPacket.USER_COMMERCE_INIT, new UserCommerceInitHandler());
        handlers.put(ServerPacket.USER_COMMERCE_END, new UserCommerceEndHandler());
        handlers.put(ServerPacket.USER_OFFER_CONFIRM, new UserOfferConfirmHandler());
        handlers.put(ServerPacket.SHOW_BLACKSMITH_FORM, new ShowBlacksmithFormHandler());
        handlers.put(ServerPacket.SHOW_CARPENTER_FORM, new ShowCarpenterFormHandler());
        handlers.put(ServerPacket.UPDATE_STA, new UpdateStaHandler());
        handlers.put(ServerPacket.UPDATE_MANA, new UpdateManaHandler());
        handlers.put(ServerPacket.UPDATE_HP, new UpdateHPHandler());
        handlers.put(ServerPacket.UPDATE_GOLD, new UpdateGoldHandler());
        handlers.put(ServerPacket.UPDATE_BANK_GOLD, new UpdateBankGoldHandler());
        handlers.put(ServerPacket.UPDATE_EXP, new UpdateExpHandler());
        handlers.put(ServerPacket.CHANGE_MAP, new ChangeMapHandler());
        handlers.put(ServerPacket.POS_UPDATE, new PosUpdateHandler());
        handlers.put(ServerPacket.CHAT_OVER_HEAD, new ChatOverHeadHandler());
        handlers.put(ServerPacket.CONSOLE_MSG, new ConsoleMessageHandler());
        handlers.put(ServerPacket.GUILD_CHAT, new GuildChatHandler());
        handlers.put(ServerPacket.SHOW_MESSAGE_BOX, new ShowMessageBoxHandler());
        handlers.put(ServerPacket.USER_INDEX_IN_SERVER, new UserIndexInServerHandler());
        handlers.put(ServerPacket.USER_CHAR_INDEX_IN_SERVER, new UserCharIndexInServerHandler());
        handlers.put(ServerPacket.CHARACTER_CREATE, new CharacterCreateHandler());
        handlers.put(ServerPacket.CHARACTER_REMOVE, new CharacterRemoveHandler());
        handlers.put(ServerPacket.CHARACTER_CHANGE_NICK, new CharacterChangeNickHandler());
        handlers.put(ServerPacket.CHARACTER_MOVE, new CharacterMoveHandler());
        handlers.put(ServerPacket.FORCE_CHAR_MOVE, new ForceCharMoveHandler());
        handlers.put(ServerPacket.CHARACTER_CHANGE, new CharacterChangeHandler());
        handlers.put(ServerPacket.OBJECT_CREATE, new ObjectCreateHandler());
        handlers.put(ServerPacket.OBJECT_DELETE, new ObjectDeleteHandler());
        handlers.put(ServerPacket.BLOCK_POSITION, new BlockPositionHandler());
        handlers.put(ServerPacket.PLAY_MIDI, new PlayMIDIHandler());
        handlers.put(ServerPacket.PLAY_WAVE, new PlayWaveHandler());
        handlers.put(ServerPacket.GUILD_LIST, new GuildListHandler());
        handlers.put(ServerPacket.AREA_CHANGED, new AreaChangedHandler());
        handlers.put(ServerPacket.PAUSE_TOGGLE, new PauseToggleHandler());
        handlers.put(ServerPacket.RAIN_TOGGLE, new RainToggleHandler());
        handlers.put(ServerPacket.CREATE_FX, new CreateFXHandler());
        handlers.put(ServerPacket.UPDATE_USER_STATS, new UpdateUserStatsHandler());
        handlers.put(ServerPacket.WORK_REQUEST_TARGET, new WorkRequestTargetHandler());
        handlers.put(ServerPacket.CHANGE_BANK_SLOT, new ChangeBankSlotHandler());
        handlers.put(ServerPacket.CHANGE_INVENTORY_SLOT, new ChangeInventorySlotHandler());
        handlers.put(ServerPacket.CHANGE_SPELL_SLOT, new ChangeSpellSlotHandler());
        handlers.put(ServerPacket.ATTRIBUTES, new AttributesHandler());
        handlers.put(ServerPacket.BLACKSMITH_WEAPONS, new BlacksmithWeaponsHandler());
        handlers.put(ServerPacket.BLACKSMITH_ARMORS, new BlacksmithArmorsHandler());
        handlers.put(ServerPacket.CARPENTER_OBJECTS, new CarpenterObjectsHandler());
        handlers.put(ServerPacket.REST_OK, new RestOKHandler());
        handlers.put(ServerPacket.ERROR_MSG, new ErrorMessageHandler());
        handlers.put(ServerPacket.BLIND, new BlindHandler());
        handlers.put(ServerPacket.DUMB, new DumbHandler());
        handlers.put(ServerPacket.SHOW_SIGNAL, new ShowSignalHandler());
        handlers.put(ServerPacket.CHANGE_NPC_INVENTORY_SLOT, new ChangeNPCInventorySlotHandler());
        handlers.put(ServerPacket.UPDATE_HUNGER_AND_THIRST, new UpdateHungerAndThirstHandler());
        handlers.put(ServerPacket.FAME, new FameHandler());
        handlers.put(ServerPacket.MINI_STATS, new MiniStatsHandler());
        handlers.put(ServerPacket.LEVEL_UP, new LevelUpHandler());
        handlers.put(ServerPacket.ADD_FORUM_MSG, new AddForumMessageHandler());
        handlers.put(ServerPacket.SHOW_FORUM_FORM, new ShowForumFormHandler());
        handlers.put(ServerPacket.SET_INVISIBLE, new SetInvisibleHandler());
        handlers.put(ServerPacket.DICE_ROLL, new DiceRollHandler());
        handlers.put(ServerPacket.MEDITATE_TOGGLE, new MeditateToggleHandler());
        handlers.put(ServerPacket.BLIND_NO_MORE, new BlindNoMoreHandler());
        handlers.put(ServerPacket.DUMB_NO_MORE, new DumbNoMoreHandler());
        handlers.put(ServerPacket.SEND_SKILLS, new SendSkillsHandler());
        handlers.put(ServerPacket.TRAINER_CREATURE_LIST, new TrainerCreatureListHandler());
        handlers.put(ServerPacket.GUILD_NEWS, new GuildNewsHandler());
        handlers.put(ServerPacket.OFFER_DETAILS, new OfferDetailsHandler());
        handlers.put(ServerPacket.ALIANCE_PROPOSALS_LIST, new AlianceProposalsListHandler());
        handlers.put(ServerPacket.PEACE_PROPOSALS_LIST, new PeaceProposalsListHandler());
        handlers.put(ServerPacket.CHARACTER_INFO, new CharacterInfoHandler());
        handlers.put(ServerPacket.GUILD_LEADER_INFO, new GuildLeaderInfoHandler());
        handlers.put(ServerPacket.GUILD_DETAILS, new GuildDetailsHandler());
        handlers.put(ServerPacket.SHOW_GUILD_FUNDATION_FORM, new ShowGuildFundationFormHandler());
        handlers.put(ServerPacket.PARALIZE_OK, new ParalizeOKHandler());
        handlers.put(ServerPacket.SHOW_USER_REQUEST, new ShowUserRequestHandler());
        handlers.put(ServerPacket.TRADE_OK, new TradeOKHandler());
        handlers.put(ServerPacket.BANK_OK, new BankOKHandler());
        handlers.put(ServerPacket.CHANGE_USER_TRADE_SLOT, new ChangeUserTradeSlotHandler());
        handlers.put(ServerPacket.SEND_NIGHT, new SendNightHandler());
        handlers.put(ServerPacket.PONG, new PingHandler());
        handlers.put(ServerPacket.UPDATE_TAG_AND_STATUS, new UpdateTagAndStatusHandler());
        handlers.put(ServerPacket.GUILD_MEMBER_INFO, new GuildMemberInfoHandler());
        handlers.put(ServerPacket.SPAWN_LIST, new SpawnListHandler());
        handlers.put(ServerPacket.SHOW_SOS_FORM, new ShowSOSFormHandler());
        handlers.put(ServerPacket.SHOW_MOTD_EDITION_FORM, new ShowMOTDEditionFormHandler());
        handlers.put(ServerPacket.SHOW_GM_PANEL_FORM, new ShowGMPanelFormHandler());
        handlers.put(ServerPacket.USER_NAME_LIST, new UserNameListHandler());
        handlers.put(ServerPacket.SHOW_GUILD_ALIGN, new ShowGuildAlignHandler());
        handlers.put(ServerPacket.SHOW_PARTY_FORM, new ShowPartyFormHandler());
        handlers.put(ServerPacket.UPDATE_STRENGHT_AND_DEXTERITY, new UpdateStrenghtAndDexterityHandler());
        handlers.put(ServerPacket.UPDATE_STRENGHT, new UpdateStrenghtHandler());
        handlers.put(ServerPacket.UPDATE_DEXTERITY, new UpdateDexterityHandler());
        handlers.put(ServerPacket.ADD_SLOTS, new AddSlotsHandler());
        handlers.put(ServerPacket.MULTI_MESSAGE, new MultiMessageHandler());
        handlers.put(ServerPacket.STOP_WORKING, new StopWorkingHandler());
        handlers.put(ServerPacket.CANCEL_OFFER_ITEM, new CancelOfferItemHandler());
    }

    /**
     * Maneja los bytes entrantes desde el buffer del paquete del servidor, validando el paquete y procesandolo a traves del
     * handler correspondiente, si esta disponible.
     *
     * @param buffer buffer que contiene los bytes del paquete recibido del servidor
     */
    public void handleIncomingBytes(PacketBuffer buffer) {
        if (buffer.getLength() <= 0) return;

        // Obtiene el ID del paquete del servidor
        int packetId = buffer.peekByte();

        // Valida el ID del paquete del servidor antes de procesarlo
        if (!isValidPacketId(packetId)) {
            Logger.debug("Invalid package ID received: " + packetId);
            return;
        }

        // Obtiene el paquete del servidor a partir del ID del paquete del servidor
        ServerPacket serverPacket = ServerPacket.getPacket(packetId);

        Logger.debug("Processing server packet [" + serverPacket + "] with ID " + packetId);

        // Guarda la referencia del paquete del servidor en la variable estatica serverPacket de clase para poder usarla sin instanciarla en el metodo disconnect() de PacketBuffer
        PacketReceiver.serverPacket = serverPacket;

        // Obtiene el handler del paquete del servidor
        PacketHandler handler = handlers.get(serverPacket);

        if (handler != null) {

            // Maneja los bytes del buffer del paquete del servidor en el handler correspondiente
            handler.handle(buffer);

            // Si quedan bytes, continua manejando los bytes del buffer del paquete del servidor
            if (buffer.getLength() > 0) handleIncomingBytes(buffer);

        }

    }

    /**
     * Verifica si existe un paquete del servidor con el ID especificado.
     *
     * @param packetId ID del paquete a validar
     * @return true si existe un paquete con el ID especificado, false en caso contrario
     */
    private boolean isValidPacketId(int packetId) {
        return ServerPacket.PACKET_REGISTRY.containsKey(packetId);
    }

}
