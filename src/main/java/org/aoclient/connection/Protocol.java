package org.aoclient.connection;

import org.aoclient.connection.packets.ClientPacketID;
import org.aoclient.connection.packets.ServerPacketID;
import org.aoclient.engine.game.User;

public class Protocol {
    static ByteQueue incomingData = new ByteQueue();
    static ByteQueue outgoingData = new ByteQueue();

    /**
     * '***************************************************
     * 'Author: Juan Martin Sotuyo Dodero (Maraxus)
     * 'Last Modification: 05/17/06
     * '***************************************************
     */
    public static void handleIncomingData(){
        ServerPacketID packet = ServerPacketID.values()[incomingData.peekByte()];

        switch (packet) {
            case logged: handleLogged(); break;
            case RemoveDialogs: handleRemoveDialogs(); break;
            case RemoveCharDialog: handleRemoveCharDialog(); break;
            case NavigateToggle: handleNavigateToggle(); break;
            case Disconnect: handleDisconnect(); break;
            case CommerceEnd: handleCommerceEnd(); break;
            case CommerceChat: handleCommerceChat(); break;
            case BankEnd: handleBankEnd(); break;
            case CommerceInit: handleCommerceInit(); break;
            case BankInit: handleBankInit(); break;
            case UserCommerceInit: handleUserCommerceInit(); break;
            case UserCommerceEnd: handleUserCommerceEnd(); break;
            case UserOfferConfirm: handleUserOfferConfirm(); break;
            case ShowBlacksmithForm: handleShowBlacksmithForm(); break;
            case ShowCarpenterForm: handleShowCarpenterForm(); break;
            case UpdateSta: handleUpdateSta(); break;
            case UpdateMana: handleUpdateMana(); break;
            case UpdateHP: handleUpdateHP(); break;
            case UpdateGold: handleUpdateGold(); break;
            case UpdateBankGold: handleUpdateBankGold(); break;
            case UpdateExp: handleUpdateExp(); break;
            case ChangeMap: handleChangeMap(); break;
            case PosUpdate: handlePosUpdate(); break;
            case ChatOverHead: handleChatOverHead(); break;
            case ConsoleMsg: handleConsoleMessage(); break;
            case GuildChat: handleGuildChat(); break;
            case ShowMessageBox: handleShowMessageBox(); break;
            case UserIndexInServer: handleUserIndexInServer(); break;
            case UserCharIndexInServer: handleUserCharIndexInServer(); break;
            case CharacterCreate: handleCharacterCreate(); break;
            case CharacterRemove: handleCharacterRemove(); break;
            case CharacterChangeNick: handleCharacterChangeNick(); break;
            case CharacterMove: handleCharacterMove(); break;
            case ForceCharMove: handleForceCharMove(); break;
            case CharacterChange: handleCharacterChange(); break;
            case ObjectCreate: handleObjectCreate(); break;
            case ObjectDelete: handleObjectDelete(); break;
            case BlockPosition: handleBlockPosition(); break;
            case PlayMIDI: handlePlayMIDI(); break;
            case PlayWave: handlePlayWave(); break;
            case guildList: handleGuildList(); break;
            case AreaChanged: handleAreaChanged(); break;
            case PauseToggle: handlePauseToggle(); break;
            case RainToggle: handleRainToggle(); break;
            case CreateFX: handleCreateFX(); break;
            case UpdateUserStats: handleUpdateUserStats(); break;
            case WorkRequestTarget: handleWorkRequestTarget(); break;
            case ChangeInventorySlot: handleChangeInventorySlot(); break;
            case ChangeBankSlot: handleChangeBankSlot(); break;
            case ChangeSpellSlot: handleChangeSpellSlot(); break;
            case Atributes: handleAtributes(); break;
            case BlacksmithWeapons: handleBlacksmithWeapons(); break;
            case BlacksmithArmors: handleBlacksmithArmors(); break;
            case CarpenterObjects: handleCarpenterObjects(); break;
            case RestOK: handleRestOK(); break;
            case ErrorMsg: handleErrorMessage(); break;
            case Blind: handleBlind(); break;
            case Dumb: handleDumb(); break;
            case ShowSignal: handleShowSignal(); break;
            case ChangeNPCInventorySlot: handleChangeNPCInventorySlot(); break;
            case UpdateHungerAndThirst: handleUpdateHungerAndThirst(); break;
            case Fame: handleFame(); break;
            case MiniStats: handleMiniStats(); break;
            case LevelUp: handleLevelUp(); break;
            case AddForumMsg: handleAddForumMessage(); break;
            case ShowForumForm: handleShowForumForm(); break;
            case SetInvisible: handleSetInvisible(); break;
            case DiceRoll: handleDiceRoll(); break;
            case MeditateToggle: handleMeditateToggle(); break;
            case BlindNoMore: handleBlindNoMore(); break;
            case DumbNoMore: handleDumbNoMore(); break;
            case SendSkills: handleSendSkills(); break;
            case TrainerCreatureList: handleTrainerCreatureList(); break;
            case guildNews: handleGuildNews(); break;
            case OfferDetails: handleOfferDetails(); break;
            case AlianceProposalsList: handleAlianceProposalsList(); break;
            case PeaceProposalsList: handlePeaceProposalsList(); break;
            case CharacterInfo: handleCharacterInfo(); break;
            case GuildLeaderInfo: handleGuildLeaderInfo(); break;
            case GuildDetails: handleGuildDetails(); break;
            case ShowGuildFundationForm: handleShowGuildFundationForm(); break;
            case ParalizeOK: handleParalizeOK(); break;
            case ShowUserRequest: handleShowUserRequest(); break;
            case TradeOK: handleTradeOK(); break;
            case BankOK: handleBankOK(); break;
            case ChangeUserTradeSlot: handleChangeUserTradeSlot(); break;
            case SendNight: handleSendNight(); break;
            case Pong: handlePong(); break;
            case UpdateTagAndStatus: handleUpdateTagAndStatus(); break;
            case GuildMemberInfo: handleGuildMemberInfo(); break;

            //*******************
            //GM messages
            //*******************
            case SpawnList: handleSpawnList(); break;
            case ShowSOSForm: handleShowSOSForm(); break;
            case ShowMOTDEditionForm: handleShowMOTDEditionForm(); break;
            case ShowGMPanelForm: handleShowGMPanelForm(); break;
            case UserNameList: handleUserNameList(); break;
            case ShowGuildAlign: handleShowGuildAlign(); break;
            case ShowPartyForm: handleShowPartyForm(); break;
            case UpdateStrenghtAndDexterity: handleUpdateStrenghtAndDexterity(); break;
            case UpdateStrenght: handleUpdateStrenght(); break;
            case UpdateDexterity: handleUpdateDexterity(); break;
            case AddSlots: handleAddSlots(); break;
            case MultiMessage: handleMultiMessage(); break;
            case StopWorking: handleStopWorking(); break;
            case CancelOfferItem: handleCancelOfferItem(); break;
        }
    }

    private static void handleCancelOfferItem() {
    }

    private static void handleStopWorking() {
    }

    private static void handleMultiMessage() {
    }

    private static void handleAddSlots() {
    }

    private static void handleUpdateDexterity() {
    }

    private static void handleUpdateStrenght() {
    }

    private static void handleUpdateStrenghtAndDexterity() {
    }

    private static void handleShowPartyForm() {
    }

    private static void handleShowGuildAlign() {
    }

    private static void handleUserNameList() {
    }

    private static void handleShowGMPanelForm() {
    }

    private static void handleShowMOTDEditionForm() {
    }

    private static void handleShowSOSForm() {
    }

    private static void handleSpawnList() {
    }

    private static void handleGuildMemberInfo() {
    }

    private static void handleUpdateTagAndStatus() {
    }

    private static void handlePong() {
    }

    private static void handleSendNight() {
    }

    private static void handleChangeUserTradeSlot() {
    }

    private static void handleBankOK() {
    }

    private static void handleTradeOK() {
    }

    private static void handleShowUserRequest() {
    }

    private static void handleParalizeOK() {
    }

    private static void handleShowGuildFundationForm() {
    }

    private static void handleGuildDetails() {
    }

    private static void handleGuildLeaderInfo() {
    }

    private static void handleCharacterInfo() {
    }

    private static void handlePeaceProposalsList() {
    }

    private static void handleAlianceProposalsList() {
    }

    private static void handleOfferDetails() {
    }

    private static void handleGuildNews() {
    }

    private static void handleTrainerCreatureList() {
    }

    private static void handleSendSkills() {
    }

    private static void handleDumbNoMore() {
    }

    private static void handleBlindNoMore() {
    }

    private static void handleMeditateToggle() {
    }

    private static void handleDiceRoll() {
    }

    private static void handleSetInvisible() {
    }

    private static void handleShowForumForm() {
    }

    private static void handleAddForumMessage() {
    }

    private static void handleLevelUp() {
    }

    private static void handleMiniStats() {
    }

    private static void handleFame() {
    }

    private static void handleUpdateHungerAndThirst() {
    }

    private static void handleChangeNPCInventorySlot() {
    }

    private static void handleShowSignal() {
    }

    private static void handleDumb() {
    }

    private static void handleBlind() {
    }

    private static void handleErrorMessage() {
    }

    private static void handleRestOK() {
    }

    private static void handleCarpenterObjects() {
    }

    private static void handleBlacksmithArmors() {
    }

    private static void handleBlacksmithWeapons() {
    }

    private static void handleAtributes() {
    }

    private static void handleChangeSpellSlot() {
    }

    private static void handleChangeBankSlot() {
    }

    private static void handleChangeInventorySlot() {
    }

    private static void handleWorkRequestTarget() {
    }

    private static void handleUpdateUserStats() {
    }

    private static void handleCreateFX() {
    }

    private static void handleRainToggle() {
    }

    private static void handlePauseToggle() {
    }

    private static void handleAreaChanged() {
    }

    private static void handleGuildList() {
    }

    private static void handlePlayWave() {
    }

    private static void handlePlayMIDI() {
    }

    private static void handleBlockPosition() {
    }

    private static void handleObjectDelete() {
    }

    private static void handleObjectCreate() {
    }

    private static void handleCharacterChange() {
    }

    private static void handleForceCharMove() {
    }

    private static void handleCharacterMove() {
    }

    private static void handleCharacterChangeNick() {
    }

    private static void handleCharacterRemove() {
    }

    private static void handleCharacterCreate() {
    }

    private static void handleUserCharIndexInServer() {
    }

    private static void handleUserIndexInServer() {
    }

    private static void handleShowMessageBox() {
    }

    private static void handleGuildChat() {
    }

    private static void handleConsoleMessage() {
    }

    private static void handleChatOverHead() {
    }

    private static void handlePosUpdate() {
    }

    private static void handleChangeMap() {
    }

    private static void handleUpdateExp() {
    }

    private static void handleUpdateBankGold() {
    }

    private static void handleUpdateGold() {
    }

    private static void handleUpdateHP() {
    }

    private static void handleUpdateMana() {
    }

    private static void handleUpdateSta() {
    }

    private static void handleShowCarpenterForm() {
    }

    private static void handleShowBlacksmithForm() {
    }

    private static void handleUserOfferConfirm() {
    }

    private static void handleUserCommerceEnd() {
    }

    private static void handleUserCommerceInit() {
    }

    private static void handleBankInit() {
    }

    private static void handleCommerceInit() {
    }

    private static void handleBankEnd() {
    }

    private static void handleCommerceChat() {
    }

    private static void handleCommerceEnd() {
    }

    private static void handleDisconnect() {
    }

    private static void handleNavigateToggle() {
    }

    private static void handleRemoveCharDialog() {
    }

    private static void handleRemoveDialogs() {
    }

    private static void handleLogged() {
        incomingData.readByte();
        User.getInstance().setConnected();
    }


    public static void writeLoginExistingChar() {
        outgoingData.writeByte(ClientPacketID.LoginExistingChar.ordinal());
        outgoingData.writeASCIIString("gs");
        outgoingData.writeASCIIString("gszone");
        outgoingData.writeByte(0);  // App.Major
        outgoingData.writeByte(13); // App.Minor
        outgoingData.writeByte(0);  // App.Revision
    }

    /**
     * '***************************************************
     * 'Author: Juan Martin Sotuyo Dodero (Maraxus)
     * 'Last Modification: 05/17/06
     * 'Writes the "LoginNewChar" message to the outgoing data buffer
     * '***************************************************
     */
    public static void writeLoginNewChar(){

    }

}
