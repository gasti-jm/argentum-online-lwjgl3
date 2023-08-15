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
        byte p = incomingData.peekByte();

        if (p > ServerPacketID.values().length) return;

        ServerPacketID packet = ServerPacketID.values()[p];
        System.out.println("Numero de paquete: " + p);
        //System.out.println(packet + " " + packet.ordinal());


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
            default: return;
        }

        // Done with this packet, move on to next one
        if(incomingData.length() > 0) {
            handleIncomingData();
        }
    }

    private static void handleCancelOfferItem() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleStopWorking() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleMultiMessage() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleAddSlots() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleUpdateDexterity() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleUpdateStrenght() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleUpdateStrenghtAndDexterity() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleShowPartyForm() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleShowGuildAlign() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleUserNameList() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleShowGMPanelForm() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleShowMOTDEditionForm() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleShowSOSForm() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleSpawnList() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleGuildMemberInfo() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleUpdateTagAndStatus() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handlePong() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleSendNight() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleChangeUserTradeSlot() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleBankOK() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleTradeOK() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleShowUserRequest() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleParalizeOK() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleShowGuildFundationForm() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleGuildDetails() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleGuildLeaderInfo() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleCharacterInfo() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handlePeaceProposalsList() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleAlianceProposalsList() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleOfferDetails() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleGuildNews() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleTrainerCreatureList() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleSendSkills() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleDumbNoMore() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleBlindNoMore() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleMeditateToggle() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleDiceRoll() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleSetInvisible() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleShowForumForm() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleAddForumMessage() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleLevelUp() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleMiniStats() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleFame() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleUpdateHungerAndThirst() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleChangeNPCInventorySlot() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleShowSignal() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleDumb() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleBlind() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleErrorMessage() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleRestOK() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleCarpenterObjects() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleBlacksmithArmors() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleBlacksmithWeapons() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleAtributes() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleChangeSpellSlot() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleChangeBankSlot() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleChangeInventorySlot() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleWorkRequestTarget() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleUpdateUserStats() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleCreateFX() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleRainToggle() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handlePauseToggle() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleAreaChanged() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleGuildList() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handlePlayWave() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handlePlayMIDI() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleBlockPosition() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleObjectDelete() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleObjectCreate() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleCharacterChange() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleForceCharMove() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleCharacterMove() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleCharacterChangeNick() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleCharacterRemove() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleCharacterCreate() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleUserCharIndexInServer() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleUserIndexInServer() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleShowMessageBox() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleGuildChat() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleConsoleMessage() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleChatOverHead() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handlePosUpdate() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleChangeMap() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleUpdateExp() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleUpdateBankGold() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleUpdateGold() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleUpdateHP() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleUpdateMana() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleUpdateSta() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleShowCarpenterForm() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleShowBlacksmithForm() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleUserOfferConfirm() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleUserCommerceEnd() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleUserCommerceInit() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleBankInit() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleCommerceInit() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleBankEnd() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleCommerceChat() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleCommerceEnd() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleDisconnect() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleNavigateToggle() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleRemoveCharDialog() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleRemoveDialogs() {
        // Remove packet ID
        incomingData.readByte();
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
