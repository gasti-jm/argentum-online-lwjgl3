package org.aoclient.network.protocol;

import org.aoclient.network.ByteQueue;
import org.aoclient.network.protocol.handlers.*;
import org.aoclient.network.protocol.handlers.gm.*;
import org.aoclient.network.packets.ServerPacketID;

import java.util.HashMap;
import java.util.Map;

/**
 * Patron Command para los handlers.
 */

public class PacketReceiver {

    private final Map<ServerPacketID, PacketHandler> handlers = new HashMap<>();
    public static ServerPacketID packetID;

    public PacketReceiver() {
        registerHandlers();
    }

    private void registerHandlers() {
        handlers.put(ServerPacketID.logged, new LoggedPacketHandler());
        handlers.put(ServerPacketID.RemoveDialogs, new LoggedPacketHandler());
        handlers.put(ServerPacketID.RemoveCharDialog, new RemoveCharDialogHandler());
        handlers.put(ServerPacketID.NavigateToggle, new NavigateToggleHandler());
        handlers.put(ServerPacketID.Disconnect, new DisconnectHandler());
        handlers.put(ServerPacketID.CommerceEnd, new CommerceEndHandler());
        handlers.put(ServerPacketID.CommerceChat, new CommerceChatHandler());
        handlers.put(ServerPacketID.BankEnd, new BankEndHandler());
        handlers.put(ServerPacketID.CommerceInit, new CommerceInitHandler());
        handlers.put(ServerPacketID.BankInit, new BankInitHandler());
        handlers.put(ServerPacketID.UserCommerceInit, new UserCommerceInitHandler());
        handlers.put(ServerPacketID.UserCommerceEnd, new UserCommerceEndHandler());
        handlers.put(ServerPacketID.UserOfferConfirm, new UserOfferConfirmHandler());
        handlers.put(ServerPacketID.ShowBlacksmithForm, new ShowBlacksmithFormHandler());
        handlers.put(ServerPacketID.ShowCarpenterForm, new ShowCarpenterFormHandler());
        handlers.put(ServerPacketID.UpdateSta, new UpdateStaHandler());
        handlers.put(ServerPacketID.UpdateMana, new UpdateManaHandler());
        handlers.put(ServerPacketID.UpdateHP, new UpdateHPHandler());
        handlers.put(ServerPacketID.UpdateGold, new UpdateGoldHandler());
        handlers.put(ServerPacketID.UpdateBankGold, new UpdateBankGoldHandler());
        handlers.put(ServerPacketID.UpdateExp, new UpdateExpHandler());
        handlers.put(ServerPacketID.ChangeMap, new ChangeMapHandler());
        handlers.put(ServerPacketID.PosUpdate, new PosUpdateHandler());
        handlers.put(ServerPacketID.ChatOverHead, new ChatOverHeadHandler());
        handlers.put(ServerPacketID.ConsoleMsg, new ConsoleMessageHandler());
        handlers.put(ServerPacketID.GuildChat, new GuildChatHandler());
        handlers.put(ServerPacketID.ShowMessageBox, new ShowMessageBoxHandler());
        handlers.put(ServerPacketID.UserIndexInServer, new UserIndexInServerHandler());
        handlers.put(ServerPacketID.UserCharIndexInServer, new UserCharIndexInServerHandler());
        handlers.put(ServerPacketID.CharacterCreate, new CharacterCreateHandler());
        handlers.put(ServerPacketID.CharacterRemove, new CharacterRemoveHandler());
        handlers.put(ServerPacketID.CharacterChangeNick, new CharacterChangeNickHandler());
        handlers.put(ServerPacketID.CharacterMove, new CharacterMoveHandler());
        handlers.put(ServerPacketID.ForceCharMove, new ForceCharMoveHandler());
        handlers.put(ServerPacketID.CharacterChange, new CharacterChangeHandler());
        handlers.put(ServerPacketID.ObjectCreate, new ObjectCreateHandler());
        handlers.put(ServerPacketID.ObjectDelete, new ObjectDeleteHandler());
        handlers.put(ServerPacketID.BlockPosition, new BlockPositionHandler());
        handlers.put(ServerPacketID.PlayMIDI, new PlayMIDIHandler());
        handlers.put(ServerPacketID.PlayWave, new PlayWaveHandler());
        handlers.put(ServerPacketID.guildList, new GuildListHandler());
        handlers.put(ServerPacketID.AreaChanged, new AreaChangedHandler());
        handlers.put(ServerPacketID.PauseToggle, new PauseToggleHandler());
        handlers.put(ServerPacketID.RainToggle, new RainToggleHandler());
        handlers.put(ServerPacketID.CreateFX, new CreateFXHandler());
        handlers.put(ServerPacketID.UpdateUserStats, new UpdateUserStatsHandler());
        handlers.put(ServerPacketID.WorkRequestTarget, new WorkRequestTargetHandler());
        handlers.put(ServerPacketID.ChangeInventorySlot, new ChangeInventorySlotHandler());
        handlers.put(ServerPacketID.ChangeSpellSlot, new ChangeSpellSlotHandler());
        handlers.put(ServerPacketID.Attributes, new AttributesHandler());
        handlers.put(ServerPacketID.BlacksmithWeapons, new BlacksmithWeaponsHandler());
        handlers.put(ServerPacketID.BlacksmithArmors, new BlacksmithArmorsHandler());
        handlers.put(ServerPacketID.CarpenterObjects, new CarpenterObjectsHandler());
        handlers.put(ServerPacketID.RestOK, new RestOKHandler());
        handlers.put(ServerPacketID.ErrorMsg, new ErrorMessageHandler());
        handlers.put(ServerPacketID.Blind, new BlindHandler());
        handlers.put(ServerPacketID.Dumb, new DumbHandler());
        handlers.put(ServerPacketID.ShowSignal, new ShowSignalHandler());
        handlers.put(ServerPacketID.ChangeNPCInventorySlot, new ChangeNPCInventorySlotHandler());
        handlers.put(ServerPacketID.UpdateHungerAndThirst, new UpdateHungerAndThirstHandler());
        handlers.put(ServerPacketID.Fame, new FameHandler());
        handlers.put(ServerPacketID.MiniStats, new MiniStatsHandler());
        handlers.put(ServerPacketID.LevelUp, new LevelUpHandler());
        handlers.put(ServerPacketID.AddForumMsg, new AddForumMessageHandler());
        handlers.put(ServerPacketID.ShowForumForm, new ShowForumFormHandler());
        handlers.put(ServerPacketID.SetInvisible, new SetInvisibleHandler());
        handlers.put(ServerPacketID.DiceRoll, new DiceRollHandler());
        handlers.put(ServerPacketID.MeditateToggle, new MeditateToggleHandler());
        handlers.put(ServerPacketID.BlindNoMore, new BlindNoMoreHandler());
        handlers.put(ServerPacketID.DumbNoMore, new DumbNoMoreHandler());
        handlers.put(ServerPacketID.SendSkills, new SendSkillsHandler());
        handlers.put(ServerPacketID.TrainerCreatureList, new TrainerCreatureListHandler());
        handlers.put(ServerPacketID.guildNews, new GuildNewsHandler());
        handlers.put(ServerPacketID.OfferDetails, new OfferDetailsHandler());
        handlers.put(ServerPacketID.AlianceProposalsList, new AlianceProposalsListHandler());
        handlers.put(ServerPacketID.PeaceProposalsList, new PeaceProposalsListHandler());
        handlers.put(ServerPacketID.CharacterInfo, new CharacterInfoHandler());
        handlers.put(ServerPacketID.GuildLeaderInfo, new GuildLeaderInfoHandler());
        handlers.put(ServerPacketID.GuildDetails, new GuildDetailsHandler());
        handlers.put(ServerPacketID.ShowGuildFundationForm, new ShowGuildFundationFormHandler());
        handlers.put(ServerPacketID.ParalizeOK, new ParalizeOKHandler());
        handlers.put(ServerPacketID.ShowUserRequest, new ShowUserRequestHandler());
        handlers.put(ServerPacketID.TradeOK, new TradeOKHandler());
        handlers.put(ServerPacketID.BankOK, new BankOKHandler());
        handlers.put(ServerPacketID.ChangeUserTradeSlot, new ChangeUserTradeSlotHandler());
        handlers.put(ServerPacketID.SendNight, new SendNightHandler());
        handlers.put(ServerPacketID.Pong, new PongHandler());
        handlers.put(ServerPacketID.UpdateTagAndStatus, new UpdateTagAndStatusHandler());
        handlers.put(ServerPacketID.GuildMemberInfo, new GuildMemberInfoHandler());
        handlers.put(ServerPacketID.SpawnList, new SpawnListHandler());
        handlers.put(ServerPacketID.ShowSOSForm, new ShowSOSFormHandler());
        handlers.put(ServerPacketID.ShowMOTDEditionForm, new ShowMOTDEditionFormHandler());
        handlers.put(ServerPacketID.ShowGMPanelForm, new ShowGMPanelFormHandler());
        handlers.put(ServerPacketID.UserNameList, new UserNameListHandler());
        handlers.put(ServerPacketID.ShowGuildAlign, new ShowGuildAlignHandler());
        handlers.put(ServerPacketID.ShowPartyForm, new ShowPartyFormHandler());
        handlers.put(ServerPacketID.UpdateStrenghtAndDexterity, new UpdateStrenghtAndDexterityHandler());
        handlers.put(ServerPacketID.UpdateStrenght, new UpdateStrenghtHandler());
        handlers.put(ServerPacketID.UpdateDexterity, new UpdateDexterityHandler());
        handlers.put(ServerPacketID.AddSlots, new AddSlotsHandler());
        handlers.put(ServerPacketID.MultiMessage, new MultiMessageHandler());
        handlers.put(ServerPacketID.StopWorking, new StopWorkingHandler());
        handlers.put(ServerPacketID.CancelOfferItem, new CancelOfferItemHandler());
    }

    public void processIncomingData(ByteQueue data) {
        if (data.length() == 0) return; // TODO Y si llega a ser -1?

        // Obtiene el tipo de paquete
        int packetId = data.peekByte();
        if (packetId >= ServerPacketID.values().length) return;

        ServerPacketID packet = ServerPacketID.values()[packetId];

        packetID = packet;

        // Logger.debug(packet + " #" + packet); // ?

        // Identifica el tipo de paquete y llama al handler correspondiente
        PacketHandler handler = handlers.get(packet);

        if (handler != null) {
            handler.handle(data);

            // Si quedan datos, continua procesando
            if (data.length() > 0) processIncomingData(data);

        }

    }

}
