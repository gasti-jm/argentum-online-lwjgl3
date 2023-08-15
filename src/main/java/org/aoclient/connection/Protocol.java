package org.aoclient.connection;

import org.aoclient.connection.packets.ClientPacketID;
import org.aoclient.connection.packets.E_Messages;
import org.aoclient.connection.packets.ServerPacketID;
import org.aoclient.engine.game.User;
import org.aoclient.engine.game.models.E_Heading;
import org.aoclient.engine.utils.GameData;
import org.aoclient.engine.utils.filedata.GrhInfo;

import static org.aoclient.connection.Messages.*;
import static org.aoclient.engine.game.models.Character.makeChar;
import static org.aoclient.engine.game.models.Character.refreshAllChars;
import static org.aoclient.engine.utils.GameData.*;

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
        System.out.println(packet + " #" + p);

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
        byte bodyPart;
        short damage;

        // Remove packet ID
        incomingData.readByte();

        byte m = incomingData.readByte();

        if (m > E_Messages.values().length) return;
        E_Messages msg = E_Messages.values()[m];


        switch (msg) {
            case DontSeeAnything:
                System.out.println(MENSAJE_NO_VES_NADA_INTERESANTE);
                break;

            case NPCSwing:
                System.out.println(MENSAJE_CRIATURA_FALLA_GOLPE);
                break;

            case NPCKillUser:
                System.out.println(MENSAJE_CRIATURA_MATADO);
                break;

            case BlockedWithShieldUser:
                System.out.println(MENSAJE_RECHAZO_ATAQUE_ESCUDO);
                break;

            case BlockedWithShieldOther:
                System.out.println(MENSAJE_USUARIO_RECHAZO_ATAQUE_ESCUDO);
                break;

            case UserSwing:
                System.out.println(MENSAJE_FALLADO_GOLPE);
                break;

            case SafeModeOn:
                System.out.println("MODO SEGURO ACTIVADO - frmMain");
                break;

            case SafeModeOff:
                System.out.println("MODO SEGURO DESACTIVADO - frmMain");
                break;

            case ResuscitationSafeOff:
                System.out.println("MODO RESURECCION ACTIVADO - frmMain");
                break;

            case ResuscitationSafeOn:
                System.out.println("MODO RESURECCION DESACTIVADO - frmMain");
                break;

            case NobilityLost:
                System.out.println(MENSAJE_PIERDE_NOBLEZA);
                break;

            case CantUseWhileMeditating:
                System.out.println(MENSAJE_USAR_MEDITANDO);
                break;

            case NPCHitUser:
                switch (incomingData.readByte()) {
                    case 1: // bCabeza
                        System.out.println(MENSAJE_GOLPE_CABEZA + " " + incomingData.readInteger());
                        break;

                    case 2: // bBrazoIzquierdo
                        System.out.println(MENSAJE_GOLPE_BRAZO_IZQ + " " + incomingData.readInteger());
                        break;

                    case 3: // bBrazoDerecho
                        System.out.println(MENSAJE_GOLPE_BRAZO_DER + " " + incomingData.readInteger());
                        break;

                    case 4: // bPiernaIzquierda
                        System.out.println(MENSAJE_GOLPE_PIERNA_IZQ + " " + incomingData.readInteger());
                        break;

                    case 5: // bPiernaDerecha
                        System.out.println(MENSAJE_GOLPE_PIERNA_DER + " " + incomingData.readInteger());
                        break;

                    case 6: // bTorso
                        System.out.println(MENSAJE_GOLPE_TORSO + " " + incomingData.readInteger());
                        break;
                }
                break;

            case UserHitNPC:
                System.out.println(MENSAJE_GOLPE_CRIATURA_1 + " " + incomingData.readLong());
                break;

            case UserAttackedSwing:
                System.out.println(MENSAJE_1 + " " + charList[incomingData.readInteger()].getName() +  MENSAJE_ATAQUE_FALLO);
                break;

            case UserHittedByUser:
                String attackerName = "<" + charList[incomingData.readInteger()].getName() + ">";
                 bodyPart = incomingData.readByte();
                 damage = incomingData.readInteger();

                switch (bodyPart) {
                    case 1: // bCabeza
                        System.out.println(MENSAJE_1 + attackerName  + MENSAJE_RECIVE_IMPACTO_CABEZA + damage + MENSAJE_2);
                        break;

                    case 2: // bBrazoIzquierdo
                        System.out.println(MENSAJE_1 + attackerName  + MENSAJE_RECIVE_IMPACTO_BRAZO_IZQ + damage + MENSAJE_2);
                        break;

                    case 3: // bBrazoDerecho
                        System.out.println(MENSAJE_1 + attackerName  + MENSAJE_RECIVE_IMPACTO_BRAZO_DER + damage + MENSAJE_2);
                        break;

                    case 4: // bPiernaIzquierda
                        System.out.println(MENSAJE_1 + attackerName  + MENSAJE_RECIVE_IMPACTO_PIERNA_IZQ + damage + MENSAJE_2);
                        break;

                    case 5: // bPiernaDerecha
                        System.out.println(MENSAJE_1 + attackerName  + MENSAJE_RECIVE_IMPACTO_PIERNA_DER + damage + MENSAJE_2);
                        break;

                    case 6: // bTorso
                        System.out.println(MENSAJE_1 + attackerName  + MENSAJE_RECIVE_IMPACTO_TORSO + damage + MENSAJE_2);
                        break;
                }
                break;

            case UserHittedUser:
                String victimName = "<" + charList[incomingData.readInteger()].getName() + ">";
                bodyPart = incomingData.readByte();
                damage = incomingData.readInteger();

                switch (bodyPart) {
                    case 1: // bCabeza
                        System.out.println(MENSAJE_PRODUCE_IMPACTO_1 + victimName  + MENSAJE_PRODUCE_IMPACTO_CABEZA + damage + MENSAJE_2);
                        break;

                    case 2: // bBrazoIzquierdo
                        System.out.println(MENSAJE_PRODUCE_IMPACTO_1 + victimName  + MENSAJE_PRODUCE_IMPACTO_BRAZO_IZQ + damage + MENSAJE_2);
                        break;

                    case 3: // bBrazoDerecho
                        System.out.println(MENSAJE_PRODUCE_IMPACTO_1 + victimName  + MENSAJE_RECIVE_IMPACTO_BRAZO_DER + damage + MENSAJE_2);
                        break;

                    case 4: // bPiernaIzquierda
                        System.out.println(MENSAJE_PRODUCE_IMPACTO_1 + victimName  + MENSAJE_RECIVE_IMPACTO_PIERNA_IZQ + damage + MENSAJE_2);
                        break;

                    case 5: // bPiernaDerecha
                        System.out.println(MENSAJE_PRODUCE_IMPACTO_1 + victimName  + MENSAJE_RECIVE_IMPACTO_PIERNA_DER + damage + MENSAJE_2);
                        break;

                    case 6: // bTorso
                        System.out.println(MENSAJE_PRODUCE_IMPACTO_1 + victimName  + MENSAJE_RECIVE_IMPACTO_TORSO + damage + MENSAJE_2);
                        break;
                }
                break;

            case WorkRequestTarget:
                short usingSkill = incomingData.readByte();

                // frmmain.mousepointer = 2

                switch (usingSkill) {
                    case 0: // magia
                        System.out.println(MENSAJE_TRABAJO_MAGIA);
                        break;

                    case 1:
                        System.out.println(MENSAJE_TRABAJO_PESCA);
                        break;

                    case 2:
                        System.out.println(MENSAJE_TRABAJO_ROBAR);
                        break;

                    case 3:
                        System.out.println(MENSAJE_TRABAJO_TALAR);
                        break;

                    case 4:
                        System.out.println(MENSAJE_TRABAJO_MINERIA);
                        break;

                    case 5:
                        System.out.println(MENSAJE_TRABAJO_FUNDIRMETAL);
                        break;

                    case 6:
                        System.out.println(MENSAJE_TRABAJO_PROYECTILES);
                        break;
                }
                break;

            case HaveKilledUser:
                System.out.println(MENSAJE_HAS_MATADO_A + charList[incomingData.readInteger()].getName() + MENSAJE_22);
                int level = incomingData.readLong();
                System.out.println(MENSAJE_HAS_GANADO_EXPE_1 + level + MENSAJE_HAS_GANADO_EXPE_2);
                // sistema de captura al matar.
                break;

            case UserKill:
                System.out.println(charList[incomingData.readInteger()].getName() + MENSAJE_TE_HA_MATADO);
                break;

            case EarnExp:
                System.out.println(MENSAJE_HAS_GANADO_EXPE_1 + incomingData.readLong() + MENSAJE_HAS_GANADO_EXPE_2);
                break;

            case GoHome:
                byte distance = incomingData.readByte();
                short time = incomingData.readInteger();
                String hogar = incomingData.readASCIIString();
                System.out.println("Estas a " + distance + " mapas de distancia de " + hogar + ", este viaje durara " + time + " segundos.");
                break;

            case FinishHome:
                System.out.println(MENSAJE_HOGAR);
                break;

            case CancelGoHome:
                System.out.println(MENSAJE_HOGAR_CANCEL);
                break;

        }



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
        if (incomingData.length() < 3) {
            System.out.println("ERROR " + incomingData.getNotEnoughDataErrCode() + " en handleUpdateStrenghtAndDexterity");
            return;
        }

        // Remove packet ID
        incomingData.readByte();

        byte userFuerza = incomingData.readByte();
        byte userAgilidad = incomingData.readByte();

        System.out.println("handleUpdateStrenghtAndDexterity CARGADO - FALTA TERMINAR");
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
        if (incomingData.length() < 2 + 20 * 2) {
            System.out.println("ERROR " + incomingData.getNotEnoughDataErrCode() + " en handleSendSkills");
            return;
        }

        // Remove packet ID
        incomingData.readByte();

        // variables globales
        short userClase = incomingData.readByte();
        byte userSkills[] = new byte[20];
        byte porcentajeSkills[] = new byte[20];


        for(int i = 0; i < 20; i++) {
            userSkills[i] = incomingData.readByte();
            porcentajeSkills[i] = incomingData.readByte();
        }

        // LlegaronSkills = true;

        System.out.println("handleSendSkills Cargado! - FALTA TERMINAR!");
    }

    private static void handleDumbNoMore() {
        // Remove packet ID
        incomingData.readByte();

        // userEstupido = false;
        System.out.println("handleDumbNoMore Cargado! - FALTA TERMINAR!");
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
        if (incomingData.length() < 3) {
            System.out.println("ERROR " + incomingData.getNotEnoughDataErrCode() + " en handleLevelUp");
            return;
        }

        // Remove packet ID
        incomingData.readByte();

        //  variable global:
        // skillPoints += incomingData.readInteger();
        short skillPoints = incomingData.readInteger();

        // frmmain.lightskillstar
        System.out.println("handleLevelUp Cargado! - FALTA TERMINAR!");
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
        if (incomingData.length() < 5) {
            System.out.println("ERROR " + incomingData.getNotEnoughDataErrCode() + " en handleUpdateHungerAndThirst");
            return;
        }

        // Remove packet ID
        incomingData.readByte();

        byte userMaxAGU = incomingData.readByte();
        byte userMinAGU = incomingData.readByte();
        byte userMaxHAM = incomingData.readByte();
        byte userMinHAM = incomingData.readByte();
        System.out.println("handleUpdateHungerAndThirst, cargado... - FALTA TERMINAR!");
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
        if (incomingData.length() < 6) {
            System.out.println("ERROR " + incomingData.getNotEnoughDataErrCode() + " en handleChangeSpellSlot");
            return;
        }

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(incomingData);

        // Remove packet ID
        buffer.readByte();

        byte slot = buffer.readByte();
        short hechizoNum = buffer.readInteger();
        String hechizoName = buffer.readASCIIString();

        incomingData.copyBuffer(buffer);
        System.out.println("ChangeSpellSlot Cargado! - FALTA TERMINAR!");
    }

    private static void handleChangeBankSlot() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleChangeInventorySlot() {
        if (incomingData.length() < 22) {
            System.out.println("ERROR " + incomingData.getNotEnoughDataErrCode() + " en handleChangeInventorySlot");
            return;
        }

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(incomingData);

        // Remove packet ID
        buffer.readByte();

        byte slot = buffer.readByte();
        short objIndex = buffer.readInteger();
        String name = buffer.readASCIIString(); //(Hay que arreglar a este puto)
        short amount = buffer.readInteger();
        boolean equipped = buffer.readBoolean();
        short grhIndex = buffer.readInteger();
        byte objType = buffer.readByte();
        short maxHit = buffer.readInteger();
        short minHit = buffer.readInteger();
        short maxDef = buffer.readInteger();
        short minDef = buffer.readInteger();
        float value = buffer.readFloat();


        /*
        If Equipped Then
        Select Case OBJType
            Case eObjType.otWeapon
                frmMain.lblWeapon = MinHit & "/" & MaxHit
                UserWeaponEqpSlot = slot
            Case eObjType.otArmadura
                frmMain.lblArmor = MinDef & "/" & MaxDef
                UserArmourEqpSlot = slot
            Case eObjType.otescudo
                frmMain.lblShielder = MinDef & "/" & MaxDef
                UserHelmEqpSlot = slot
            Case eObjType.otcasco
                frmMain.lblHelm = MinDef & "/" & MaxDef
                UserShieldEqpSlot = slot
        End Select
    Else
        Select Case slot
            Case UserWeaponEqpSlot
                frmMain.lblWeapon = "0/0"
                UserWeaponEqpSlot = 0
            Case UserArmourEqpSlot
                frmMain.lblArmor = "0/0"
                UserArmourEqpSlot = 0
            Case UserHelmEqpSlot
                frmMain.lblShielder = "0/0"
                UserHelmEqpSlot = 0
            Case UserShieldEqpSlot
                frmMain.lblHelm = "0/0"
                UserShieldEqpSlot = 0
        End Select
    End If

    Call Inventario.SetItem(slot, OBJIndex, Amount, Equipped, GrhIndex, OBJType, MaxHit, MinHit, MaxDef, MinDef, value, Name)

         */

        incomingData.copyBuffer(buffer);
        System.out.println("ChangeInventorySlot Cargado! - FALTA TERMINAR!");
    }

    private static void handleWorkRequestTarget() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleUpdateUserStats() {
        if (incomingData.length() < 26) {
            System.out.println("ERROR " + incomingData.getNotEnoughDataErrCode() + " en handleUpdateUserStats");
            return;
        }

        // Remove packet ID
        incomingData.readByte();

        short userMaxHP = incomingData.readInteger();
        short userMinHP = incomingData.readInteger();
        short userMaxMAN = incomingData.readInteger();
        short userMinMAN = incomingData.readInteger();
        short userMaxSTA = incomingData.readInteger();
        short userMinSTA = incomingData.readInteger();
        int userGLD = incomingData.readLong();
        byte userLvl = incomingData.readByte();
        int userPasarNivel = incomingData.readLong();
        int userExp = incomingData.readLong();

        System.out.println("handleUpdateUserStats Cargado!");
    }

    private static void handleCreateFX() {
        if (incomingData.length() < 7) {
            System.out.println("ERROR " + incomingData.getNotEnoughDataErrCode() + " en handleCreateFX");
            return;
        }

        // Remove packet ID
        incomingData.readByte();

        short charIndex = incomingData.readInteger();
        short fX = incomingData.readInteger();
        short loops = incomingData.readInteger();

        User.getInstance().setCharacterFx(charIndex, fX, loops);
        System.out.println("handleCreateFX Cargado!");
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
        if (incomingData.length() < 3) {
            System.out.println("ERROR " + incomingData.getNotEnoughDataErrCode() + " en handleAreaChanged");
            return;
        }

        // Remove packet ID
        incomingData.readByte();

        byte x = incomingData.readByte();
        byte y = incomingData.readByte();

        User.getInstance().areaChange(x, y);

        System.out.println("handleAreaChanged Cargado!");
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
        if (incomingData.length() < 4) {
            System.out.println("ERROR " + incomingData.getNotEnoughDataErrCode() + " en handlePlayMIDI");
            return;
        }

        // Remove packet ID
        incomingData.readByte();

        byte currentMidi = incomingData.readByte();

        if(currentMidi > 0) {
            incomingData.readInteger();
            // play midi
        } else {
            incomingData.readInteger();
        }

        System.out.println("handlePlayMIDI Cargado! - FALTA TERMINAR!");
    }

    private static void handleBlockPosition() {
        if (incomingData.length() < 4) {
            System.out.println("ERROR " + incomingData.getNotEnoughDataErrCode() + " en handleBlockPosition");
            return;
        }

        // Remove packet ID
        incomingData.readByte();

        byte x = incomingData.readByte();
        byte y = incomingData.readByte();

        mapData[x][y].setBlocked(incomingData.readBoolean());

        System.out.println("handleBlockPosition Cargado!");
    }

    private static void handleObjectDelete() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleObjectCreate() {
        if (incomingData.length() < 5) {
            System.out.println("ERROR " + incomingData.getNotEnoughDataErrCode() + " en handleObjectCreate");
            return;
        }

        // Remove packet ID
        incomingData.readByte();

        byte x = incomingData.readByte();
        byte y = incomingData.readByte();

        mapData[x][y].getObjGrh().setGrhIndex(incomingData.readInteger());

        initGrh(mapData[x][y].getObjGrh(), mapData[x][y].getObjGrh().getGrhIndex(), true);

        System.out.println("handleObjectCreate Cargado! - FALTA TERMINAR!");
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
        if (incomingData.length() < 24) {
            System.out.println("ERROR " + incomingData.getNotEnoughDataErrCode() + " en handleCharacterCreate");
            return;
        }

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(incomingData);

        // Remove packet ID
        buffer.readByte();

        int charIndex = buffer.readInteger();
        short body = buffer.readInteger();
        short head = buffer.readInteger();
        byte numHeading = buffer.readByte();
        E_Heading heading = E_Heading.values()[numHeading - 1];
        byte x = buffer.readByte();
        byte y = buffer.readByte();
        short weapon = buffer.readInteger();
        short shield = buffer.readInteger();
        short helmet = buffer.readInteger();


        charList[charIndex].setfX(new GrhInfo());
        User.getInstance().setCharacterFx(charIndex, buffer.readInteger(), buffer.readInteger());
        charList[charIndex].setName(buffer.readASCIIString());

        byte nickColor = buffer.readByte();
        short privs = buffer.readByte();

        /*
        If (NickColor And eNickColor.ieCriminal) <> 0 Then
            .Criminal = 1
        Else
            .Criminal = 0
        End If

        .Atacable = (NickColor And eNickColor.ieAtacable) <> 0

        If privs <> 0 Then
            'If the player belongs to a council AND is an admin, only whos as an admin
            If (privs And PlayerType.ChaosCouncil) <> 0 And (privs And PlayerType.User) = 0 Then
                privs = privs Xor PlayerType.ChaosCouncil
            End If

            If (privs And PlayerType.RoyalCouncil) <> 0 And (privs And PlayerType.User) = 0 Then
                privs = privs Xor PlayerType.RoyalCouncil
            End If

            'If the player is a RM, ignore other flags
            If privs And PlayerType.RoleMaster Then
                privs = PlayerType.RoleMaster
            End If

            'Log2 of the bit flags sent by the server gives our numbers ^^
            .priv = Log(privs) / Log(2)
        Else
            .priv = 0
        End If
         */

        makeChar(charIndex, body, head, heading, x, y, weapon, shield, helmet);
        refreshAllChars();

        incomingData.copyBuffer(buffer);
        System.out.println("handleCharacterCreate Cargado! - FALTA TERMINAR!");
    }

    private static void handleUserCharIndexInServer() {
        if (incomingData.length() < 3) {
            System.out.println("ERROR " + incomingData.getNotEnoughDataErrCode() + " en handleUserCharIndexInServer");
            return;
        }

        // Remove packet ID
        incomingData.readByte();

        User.getInstance().setUserCharIndex(incomingData.readInteger());
        User.getInstance().setUserPos(charList[User.getInstance().getUserCharIndex()].getPos());

        System.out.println("handleUserCharIndexInServer Cargado! - FALTA TERMINAR!");
    }

    private static void handleUserIndexInServer() {
        if (incomingData.length() < 3) {
            System.out.println("ERROR " + incomingData.getNotEnoughDataErrCode() + " en handleChangeInventorySlot");
            return;
        }

        // Remove packet ID
        incomingData.readByte();

        int userIndex = incomingData.readInteger();
        System.out.println("handleUserIndexInServer Cargado! - FALTA TERMINAR!");
    }

    private static void handleShowMessageBox() {
        // Remove packet ID
        incomingData.readByte();
    }

    private static void handleGuildChat() {
        if (incomingData.length() < 3) {
            System.out.println("ERROR " + incomingData.getNotEnoughDataErrCode() + " en handleGuildChat");
            return;
        }

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(incomingData);

        // Remove packet ID
        buffer.readByte();

        String chat = buffer.readASCIIString();

        incomingData.copyBuffer(buffer);

        System.out.println("handleGuildChat CARGADO - FALTA TERMINAR");
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
        if (incomingData.length() < 5) {
            System.out.println("ERROR " + incomingData.getNotEnoughDataErrCode() + " en handleChangeMap");
            return;
        }

        // Remove packet ID
        incomingData.readByte();

        int userMap = incomingData.readInteger();
        User.getInstance().setUserMap( (short) userMap);

        // Todo: Once on-the-fly editor is implemented check for map version before loading....
        // For now we just drop it
        incomingData.readInteger();

        GameData.loadMap(userMap);

        /*
        If bLluvia(UserMap) = 0 Then
            If bRain Then
                Call Audio.StopWave(RainBufferIndex)
                RainBufferIndex = 0
                frmMain.IsPlaying = PlayLoop.plNone
            End If
        End If
         */

        System.out.println("handleChangeMap Cargado! - FALTA TERMINAR!");

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

    public static void writeLoginNewChar(){

    }

}
