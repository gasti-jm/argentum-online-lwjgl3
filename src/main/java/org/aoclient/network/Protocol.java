package org.aoclient.network;

import org.aoclient.engine.Sound;
import org.aoclient.engine.Window;
import org.aoclient.engine.game.Console;
import org.aoclient.engine.game.Dialogs;
import org.aoclient.engine.game.Rain;
import org.aoclient.engine.game.User;
import org.aoclient.engine.game.models.E_FontType;
import org.aoclient.engine.game.models.E_Heading;
import org.aoclient.engine.game.models.E_ObjType;
import org.aoclient.engine.game.models.E_Skills;
import org.aoclient.engine.gui.ImGUISystem;
import org.aoclient.engine.gui.forms.FComerce;
import org.aoclient.engine.gui.forms.FMessage;
import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.engine.utils.GameData;
import org.aoclient.engine.utils.inits.BodyData;
import org.aoclient.engine.utils.inits.HeadData;
import org.aoclient.engine.utils.inits.ShieldData;
import org.aoclient.engine.utils.inits.WeaponData;
import org.aoclient.network.packets.*;
import org.tinylog.Logger;

import java.nio.charset.StandardCharsets;

import static org.aoclient.engine.Sound.*;
import static org.aoclient.engine.game.Dialogs.charDialogHitSet;
import static org.aoclient.engine.game.Dialogs.charDialogSet;
import static org.aoclient.engine.game.models.Character.*;
import static org.aoclient.engine.game.models.E_Skills.FundirMetal;
import static org.aoclient.engine.utils.GameData.*;
import static org.aoclient.network.Messages.*;
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
    static ByteQueue incomingData = new ByteQueue();
    static ByteQueue outgoingData = new ByteQueue();
    static int pingTime = 0;
    static int lastPacket;

    public static void handleIncomingData() {
        lastPacket = incomingData.peekByte();

        if (lastPacket > ServerPacketID.values().length) return;
        ServerPacketID packet = ServerPacketID.values()[lastPacket];
        //Logger.debug(packet + " #" + p);

        switch (packet) {
            case logged:
                handleLogged();
                break;
            case RemoveDialogs:
                handleRemoveDialogs();
                break;
            case RemoveCharDialog:
                handleRemoveCharDialog();
                break;
            case NavigateToggle:
                handleNavigateToggle();
                break;
            case Disconnect:
                handleDisconnect();
                break;
            case CommerceEnd:
                handleCommerceEnd();
                break;
            case CommerceChat:
                handleCommerceChat();
                break;
            case BankEnd:
                handleBankEnd();
                break;
            case CommerceInit:
                handleCommerceInit();
                break;
            case BankInit:
                handleBankInit();
                break;
            case UserCommerceInit:
                handleUserCommerceInit();
                break;
            case UserCommerceEnd:
                handleUserCommerceEnd();
                break;
            case UserOfferConfirm:
                handleUserOfferConfirm();
                break;
            case ShowBlacksmithForm:
                handleShowBlacksmithForm();
                break;
            case ShowCarpenterForm:
                handleShowCarpenterForm();
                break;
            case UpdateSta:
                handleUpdateSta();
                break;
            case UpdateMana:
                handleUpdateMana();
                break;
            case UpdateHP:
                handleUpdateHP();
                break;
            case UpdateGold:
                handleUpdateGold();
                break;
            case UpdateBankGold:
                handleUpdateBankGold();
                break;
            case UpdateExp:
                handleUpdateExp();
                break;
            case ChangeMap:
                handleChangeMap();
                break;
            case PosUpdate:
                handlePosUpdate();
                break;
            case ChatOverHead:
                handleChatOverHead();
                break;
            case ConsoleMsg:
                handleConsoleMessage();
                break;
            case GuildChat:
                handleGuildChat();
                break;
            case ShowMessageBox:
                handleShowMessageBox();
                break;
            case UserIndexInServer:
                handleUserIndexInServer();
                break;
            case UserCharIndexInServer:
                handleUserCharIndexInServer();
                break;
            case CharacterCreate:
                handleCharacterCreate();
                break;
            case CharacterRemove:
                handleCharacterRemove();
                break;
            case CharacterChangeNick:
                handleCharacterChangeNick();
                break;
            case CharacterMove:
                handleCharacterMove();
                break;
            case ForceCharMove:
                handleForceCharMove();
                break;
            case CharacterChange:
                handleCharacterChange();
                break;
            case ObjectCreate:
                handleObjectCreate();
                break;
            case ObjectDelete:
                handleObjectDelete();
                break;
            case BlockPosition:
                handleBlockPosition();
                break;
            case PlayMIDI:
                handlePlayMIDI();
                break;
            case PlayWave:
                handlePlayWave();
                break;
            case guildList:
                handleGuildList();
                break;
            case AreaChanged:
                handleAreaChanged();
                break;
            case PauseToggle:
                handlePauseToggle();
                break;
            case RainToggle:
                handleRainToggle();
                break;
            case CreateFX:
                handleCreateFX();
                break;
            case UpdateUserStats:
                handleUpdateUserStats();
                break;
            case WorkRequestTarget:
                handleWorkRequestTarget();
                break;
            case ChangeInventorySlot:
                handleChangeInventorySlot();
                break;
            case ChangeBankSlot:
                handleChangeBankSlot();
                break;
            case ChangeSpellSlot:
                handleChangeSpellSlot();
                break;
            case Atributes:
                handleAtributes();
                break;
            case BlacksmithWeapons:
                handleBlacksmithWeapons();
                break;
            case BlacksmithArmors:
                handleBlacksmithArmors();
                break;
            case CarpenterObjects:
                handleCarpenterObjects();
                break;
            case RestOK:
                handleRestOK();
                break;
            case ErrorMsg:
                handleErrorMessage();
                break;
            case Blind:
                handleBlind();
                break;
            case Dumb:
                handleDumb();
                break;
            case ShowSignal:
                handleShowSignal();
                break;
            case ChangeNPCInventorySlot:
                handleChangeNPCInventorySlot();
                break;
            case UpdateHungerAndThirst:
                handleUpdateHungerAndThirst();
                break;
            case Fame:
                handleFame();
                break;
            case MiniStats:
                handleMiniStats();
                break;
            case LevelUp:
                handleLevelUp();
                break;
            case AddForumMsg:
                handleAddForumMessage();
                break;
            case ShowForumForm:
                handleShowForumForm();
                break;
            case SetInvisible:
                handleSetInvisible();
                break;
            case DiceRoll:
                handleDiceRoll();
                break;
            case MeditateToggle:
                handleMeditateToggle();
                break;
            case BlindNoMore:
                handleBlindNoMore();
                break;
            case DumbNoMore:
                handleDumbNoMore();
                break;
            case SendSkills:
                handleSendSkills();
                break;
            case TrainerCreatureList:
                handleTrainerCreatureList();
                break;
            case guildNews:
                handleGuildNews();
                break;
            case OfferDetails:
                handleOfferDetails();
                break;
            case AlianceProposalsList:
                handleAlianceProposalsList();
                break;
            case PeaceProposalsList:
                handlePeaceProposalsList();
                break;
            case CharacterInfo:
                handleCharacterInfo();
                break;
            case GuildLeaderInfo:
                handleGuildLeaderInfo();
                break;
            case GuildDetails:
                handleGuildDetails();
                break;
            case ShowGuildFundationForm:
                handleShowGuildFundationForm();
                break;
            case ParalizeOK:
                handleParalizeOK();
                break;
            case ShowUserRequest:
                handleShowUserRequest();
                break;
            case TradeOK:
                handleTradeOK();
                break;
            case BankOK:
                handleBankOK();
                break;
            case ChangeUserTradeSlot:
                handleChangeUserTradeSlot();
                break;
            case SendNight:
                handleSendNight();
                break;
            case Pong:
                handlePong();
                break;
            case UpdateTagAndStatus:
                handleUpdateTagAndStatus();
                break;
            case GuildMemberInfo:
                handleGuildMemberInfo();
                break;

            //*******************
            // GM messages
            //*******************
            case SpawnList:
                handleSpawnList();
                break;
            case ShowSOSForm:
                handleShowSOSForm();
                break;
            case ShowMOTDEditionForm:
                handleShowMOTDEditionForm();
                break;
            case ShowGMPanelForm:
                handleShowGMPanelForm();
                break;
            case UserNameList:
                handleUserNameList();
                break;
            case ShowGuildAlign:
                handleShowGuildAlign();
                break;
            case ShowPartyForm:
                handleShowPartyForm();
                break;
            case UpdateStrenghtAndDexterity:
                handleUpdateStrenghtAndDexterity();
                break;
            case UpdateStrenght:
                handleUpdateStrenght();
                break;
            case UpdateDexterity:
                handleUpdateDexterity();
                break;
            case AddSlots:
                handleAddSlots();
                break;
            case MultiMessage:
                handleMultiMessage();
                break;
            case StopWorking:
                handleStopWorking();
                break;
            case CancelOfferItem:
                handleCancelOfferItem();
                break;
            default:
                return;
        }

        // Done with this packet, move on to next one
        if (incomingData.length() > 0) handleIncomingData();

    }

    private static void handleCancelOfferItem() {
        // Remove packet ID
        incomingData.readByte();

        int slot = incomingData.readByte();

        //With InvOfferComUsu(0)
        //        Amount = .Amount(slot)
        //
        //        ' No tiene sentido que se quiten 0 unidades
        //        If Amount <> 0 Then
        //            ' Actualizo el inventario general
        //            Call frmComerciarUsu.UpdateInvCom(.OBJIndex(slot), Amount)
        //
        //            ' Borro el item
        //            Call .SetItem(slot, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, "")
        //        End If
        //    End With
        //
        //    ' Si era el único ítem de la oferta, no puede confirmarla
        //    If Not frmComerciarUsu.HasAnyItem(InvOfferComUsu(0)) And _
        //        Not frmComerciarUsu.HasAnyItem(InvOroComUsu(1)) Then Call frmComerciarUsu.HabilitarConfirmar(False)
        //
        //    With FontTypes(FontTypeNames.FONTTYPE_INFO)
        //        Call frmComerciarUsu.PrintCommerceMsg("¡No puedes comerciar ese objeto!", FontTypeNames.FONTTYPE_INFO)
        //    End With
    }

    private static void handleStopWorking() {
        // Remove packet ID
        incomingData.readByte();

        //With FontTypes(FontTypeNames.FONTTYPE_INFO)
        //        Call ShowConsoleMsg("¡Has terminado de trabajar!", .red, .green, .blue, .bold, .italic)
        //    End With
        //
        //    If //FrmMain.macrotrabajo.Enabled Then Call //FrmMain.DesactivarMacroTrabajo
    }

    private static void handleMultiMessage() {
        int bodyPart;
        short damage;

        // Remove packet ID
        incomingData.readByte();

        int m = incomingData.readByte();

        if (m > E_Messages.values().length) return;
        E_Messages msg = E_Messages.values()[m];

        switch (msg) {
            case DontSeeAnything:
                console.addMsgToConsole(MENSAJE_NO_VES_NADA_INTERESANTE, false, false, new RGBColor(0.25f, 0.75f, 0.60f));
                break;

            case NPCSwing:
                console.addMsgToConsole(MENSAJE_CRIATURA_FALLA_GOLPE, false, false, new RGBColor(1f, 0f, 0f));
                break;

            case NPCKillUser:
                console.addMsgToConsole(MENSAJE_CRIATURA_MATADO, false, false, new RGBColor(1f, 0f, 0f));
                break;

            case BlockedWithShieldUser:
                console.addMsgToConsole(MENSAJE_RECHAZO_ATAQUE_ESCUDO, false, false, new RGBColor(1f, 0f, 0f));
                break;

            case BlockedWithShieldOther:
                console.addMsgToConsole(MENSAJE_USUARIO_RECHAZO_ATAQUE_ESCUDO, false, false, new RGBColor(1f, 0f, 0f));
                break;

            case UserSwing:
                console.addMsgToConsole(MENSAJE_FALLADO_GOLPE, false, false, new RGBColor(1f, 0f, 0f));
                charDialogHitSet(User.get().getUserCharIndex(), "*Fallas*");
                break;

            case SafeModeOn:
                console.addMsgToConsole("MODO SEGURO ACTIVADO", false, false, new RGBColor(0f, 1f, 0f));
                break;

            case SafeModeOff:
                console.addMsgToConsole("MODO SEGURO DESACTIVADO", false, false, new RGBColor(1f, 0f, 0f));
                break;

            case ResuscitationSafeOff:
                console.addMsgToConsole("MODO RESURECCION ACTIVADO", false, false, new RGBColor(0f, 1f, 0f));
                break;

            case ResuscitationSafeOn:
                console.addMsgToConsole("MODO RESURECCION DESACTIVADO", false, false, new RGBColor(1f, 0f, 0f));
                break;

            case NobilityLost:
                console.addMsgToConsole(MENSAJE_PIERDE_NOBLEZA, false, false, new RGBColor(1f, 0f, 0f));
                break;

            case CantUseWhileMeditating:
                console.addMsgToConsole(MENSAJE_USAR_MEDITANDO, false, false, new RGBColor(1f, 0f, 0f));
                break;

            case NPCHitUser:
                switch (incomingData.readByte()) {
                    case 1: // bCabeza
                        console.addMsgToConsole(MENSAJE_GOLPE_CABEZA + " " + incomingData.readInteger(),
                                false, false, new RGBColor(1f, 0f, 0f));
                        break;

                    case 2: // bBrazoIzquierdo
                        console.addMsgToConsole(MENSAJE_GOLPE_BRAZO_IZQ + " " + incomingData.readInteger(),
                                false, false, new RGBColor(1f, 0f, 0f));
                        break;

                    case 3: // bBrazoDerecho
                        console.addMsgToConsole(MENSAJE_GOLPE_BRAZO_DER + " " + incomingData.readInteger(),
                                false, false, new RGBColor(1f, 0f, 0f));
                        break;

                    case 4: // bPiernaIzquierda
                        console.addMsgToConsole(MENSAJE_GOLPE_PIERNA_IZQ + " " + incomingData.readInteger(),
                                false, false, new RGBColor(1f, 0f, 0f));
                        break;

                    case 5: // bPiernaDerecha
                        console.addMsgToConsole(MENSAJE_GOLPE_PIERNA_DER + " " + incomingData.readInteger(),
                                false, false, new RGBColor(1f, 0f, 0f));
                        break;

                    case 6: // bTorso
                        console.addMsgToConsole(MENSAJE_GOLPE_TORSO + " " + incomingData.readInteger(),
                                false, false, new RGBColor(1f, 0f, 0f));
                        break;
                }
                break;

            case UserHitNPC:
                final int d = incomingData.readLong();
                console.addMsgToConsole(MENSAJE_GOLPE_CRIATURA_1 + " " + d,
                        false, false, new RGBColor(1f, 0f, 0f));

                charDialogHitSet(User.get().getUserCharIndex(), d);

                break;

            case UserAttackedSwing:
                final short charIndexAttaker = incomingData.readInteger();

                console.addMsgToConsole(MENSAJE_1 + " " + charList[charIndexAttaker].getName() + MENSAJE_ATAQUE_FALLO,
                        false, false, new RGBColor(1f, 0f, 0f));

                charDialogHitSet(charIndexAttaker, "*Falla*");
                break;

            case UserHittedByUser:
                final int charIndexHitAttaker = incomingData.readInteger();
                String attackerName = "<" + charList[charIndexHitAttaker].getName() + ">";
                bodyPart = incomingData.readByte();
                damage = incomingData.readInteger();

                switch (bodyPart) {
                    case 1: // bCabeza
                        console.addMsgToConsole(MENSAJE_1 + attackerName + MENSAJE_RECIVE_IMPACTO_CABEZA + damage + MENSAJE_2,
                                false, false, new RGBColor(1f, 0f, 0f));
                        break;

                    case 2: // bBrazoIzquierdo
                        console.addMsgToConsole(MENSAJE_1 + attackerName + MENSAJE_RECIVE_IMPACTO_BRAZO_IZQ + damage + MENSAJE_2,
                                false, false, new RGBColor(1f, 0f, 0f));
                        break;

                    case 3: // bBrazoDerecho
                        console.addMsgToConsole(MENSAJE_1 + attackerName + MENSAJE_RECIVE_IMPACTO_BRAZO_DER + damage + MENSAJE_2,
                                false, false, new RGBColor(1f, 0f, 0f));
                        break;

                    case 4: // bPiernaIzquierda
                        console.addMsgToConsole(MENSAJE_1 + attackerName + MENSAJE_RECIVE_IMPACTO_PIERNA_IZQ + damage + MENSAJE_2,
                                false, false, new RGBColor(1f, 0f, 0f));
                        break;

                    case 5: // bPiernaDerecha
                        console.addMsgToConsole(MENSAJE_1 + attackerName + MENSAJE_RECIVE_IMPACTO_PIERNA_DER + damage + MENSAJE_2,
                                false, false, new RGBColor(1f, 0f, 0f));
                        break;

                    case 6: // bTorso
                        console.addMsgToConsole(MENSAJE_1 + attackerName + MENSAJE_RECIVE_IMPACTO_TORSO + damage + MENSAJE_2,
                                false, false, new RGBColor(1f, 0f, 0f));
                        break;
                }

                charDialogHitSet(charIndexHitAttaker, damage);

                break;

            case UserHittedUser:
                final int charIndexVictim = incomingData.readInteger();
                final String victimName = "<" + charList[charIndexVictim].getName() + ">";
                bodyPart = incomingData.readByte();
                damage = incomingData.readInteger();

                switch (bodyPart) {
                    case 1: // bCabeza
                        console.addMsgToConsole(MENSAJE_PRODUCE_IMPACTO_1 + victimName + MENSAJE_PRODUCE_IMPACTO_CABEZA + damage + MENSAJE_2,
                                false, false, new RGBColor(1f, 0f, 0f));
                        break;

                    case 2: // bBrazoIzquierdo
                        console.addMsgToConsole(MENSAJE_PRODUCE_IMPACTO_1 + victimName + MENSAJE_PRODUCE_IMPACTO_BRAZO_IZQ + damage + MENSAJE_2,
                                false, false, new RGBColor(1f, 0f, 0f));
                        break;

                    case 3: // bBrazoDerecho
                        console.addMsgToConsole(MENSAJE_PRODUCE_IMPACTO_1 + victimName + MENSAJE_RECIVE_IMPACTO_BRAZO_DER + damage + MENSAJE_2,
                                false, false, new RGBColor(1f, 0f, 0f));
                        break;

                    case 4: // bPiernaIzquierda
                        console.addMsgToConsole(MENSAJE_PRODUCE_IMPACTO_1 + victimName + MENSAJE_RECIVE_IMPACTO_PIERNA_IZQ + damage + MENSAJE_2,
                                false, false, new RGBColor(1f, 0f, 0f));
                        break;

                    case 5: // bPiernaDerecha
                        console.addMsgToConsole(MENSAJE_PRODUCE_IMPACTO_1 + victimName + MENSAJE_RECIVE_IMPACTO_PIERNA_DER + damage + MENSAJE_2,
                                false, false, new RGBColor(1f, 0f, 0f));
                        break;

                    case 6: // bTorso
                        console.addMsgToConsole(MENSAJE_PRODUCE_IMPACTO_1 + victimName + MENSAJE_RECIVE_IMPACTO_TORSO + damage + MENSAJE_2,
                                false, false, new RGBColor(1f, 0f, 0f));
                        break;
                }
                charDialogHitSet(charIndexVictim, damage);

                break;

            case WorkRequestTarget:
                final int usingSkill = incomingData.readByte();
                User.get().setUsingSkill(usingSkill);

                Window.get().setCursorCrosshair(true);

                switch (E_Skills.values()[usingSkill - 1]) {
                    case Magia:
                        console.addMsgToConsole(MENSAJE_TRABAJO_MAGIA, false, false, new RGBColor(0.39f, 0.39f, 0.47f));
                        break;

                    case Pesca:
                        console.addMsgToConsole(MENSAJE_TRABAJO_PESCA, false, false, new RGBColor(0.39f, 0.39f, 0.47f));
                        break;

                    case Robar:
                        console.addMsgToConsole(MENSAJE_TRABAJO_ROBAR, false, false, new RGBColor(0.39f, 0.39f, 0.47f));
                        break;

                    case Talar:
                        console.addMsgToConsole(MENSAJE_TRABAJO_TALAR, false, false, new RGBColor(0.39f, 0.39f, 0.47f));
                        break;

                    case Mineria:
                        console.addMsgToConsole(MENSAJE_TRABAJO_MINERIA, false, false, new RGBColor(0.39f, 0.39f, 0.47f));
                        break;

                    case Proyectiles:
                        console.addMsgToConsole(MENSAJE_TRABAJO_PROYECTILES, false, false, new RGBColor(0.39f, 0.39f, 0.47f));
                        break;
                }

                if (usingSkill == FundirMetal) {
                    console.addMsgToConsole(MENSAJE_TRABAJO_FUNDIRMETAL, false, false, new RGBColor(0.39f, 0.39f, 0.47f));
                }
                break;

            case HaveKilledUser:
                console.addMsgToConsole(MENSAJE_HAS_MATADO_A + charList[incomingData.readInteger()].getName() + MENSAJE_22,
                        false, false, new RGBColor(1f, 0f, 0f));

                final int level = incomingData.readLong();

                console.addMsgToConsole(MENSAJE_HAS_GANADO_EXPE_1 + level + MENSAJE_HAS_GANADO_EXPE_2,
                        false, false, new RGBColor(1f, 0f, 0f));

                // sistema de captura al matar.
                break;

            case UserKill:
                console.addMsgToConsole(charList[incomingData.readInteger()].getName() + MENSAJE_TE_HA_MATADO,
                        false, false, new RGBColor(1f, 0f, 0f));
                break;

            case EarnExp:
                console.addMsgToConsole(MENSAJE_HAS_GANADO_EXPE_1 + incomingData.readLong() + MENSAJE_HAS_GANADO_EXPE_2,
                        false, false, new RGBColor(1f, 0f, 0f));
                break;

            case GoHome:
                int distance = incomingData.readByte();
                short time = incomingData.readInteger();
                String hogar = incomingData.readASCIIString();

                console.addMsgToConsole("Estas a " + distance + " mapas de distancia de " + hogar + ", este viaje durara " + time + " segundos.",
                        false, false, new RGBColor(1f, 0f, 0f));
                break;

            case FinishHome:
                console.addMsgToConsole(MENSAJE_HOGAR, false, false, new RGBColor());
                break;

            case CancelGoHome:
                console.addMsgToConsole(MENSAJE_HOGAR_CANCEL, false, false, new RGBColor(1f, 0f, 0f));
                break;
        }

    }

    private static void handleAddSlots() {
        // Remove packet ID
        incomingData.readByte();
        int maxInventorySlots = incomingData.readByte();
    }

    private static void handleUpdateDexterity() {
        if (incomingData.checkPacketData(2)) return;

        // Remove packet ID
        incomingData.readByte();

        User.get().setUserDext(incomingData.readByte());
    }

    private static void handleUpdateStrenght() {
        if (incomingData.checkPacketData(2)) return;

        // Remove packet ID
        incomingData.readByte();

        User.get().setUserStrg(incomingData.readByte());
    }

    private static void handleUpdateStrenghtAndDexterity() {
        if (incomingData.checkPacketData(3)) return;

        // Remove packet ID
        incomingData.readByte();

        User.get().setUserStrg(incomingData.readByte());
        User.get().setUserDext(incomingData.readByte());
    }

    private static void handleShowPartyForm() {
        if (incomingData.checkPacketData(4)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(incomingData);

        // Remove packet ID
        buffer.readByte();

        int esPartyLeader = buffer.readByte();
        String members = buffer.readASCIIString();

        //Dim members() As String
        //    Dim i As Long
        //
        //    EsPartyLeader = CBool(Buffer.ReadByte())
        //
        //    members = Split(Buffer.ReadASCIIString(), SEPARATOR)
        //    For i = 0 To UBound(members())
        //        Call frmParty.lstMembers.AddItem(members(i))
        //    Next i
        //
        //    frmParty.lblTotalExp.Caption = Buffer.ReadLong
        //    frmParty.Show , //FrmMain

        incomingData.copyBuffer(buffer);
        Logger.debug("handleShowPartyForm Cargado! - FALTA TERMINAR!");
    }

    private static void handleShowGuildAlign() {
        // Remove packet ID
        incomingData.readByte();
        //frmEligeAlineacion.Show vbModeless, //FrmMain
    }

    private static void handleUserNameList() {
        if (incomingData.checkPacketData(3)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(incomingData);

        // Remove packet ID
        buffer.readByte();

        String userList = buffer.readASCIIString();

        //Dim userList() As String
        //    Dim i As Long
        //
        //    userList = Split(Buffer.ReadASCIIString(), SEPARATOR)
        //
        //    If frmPanelGm.Visible Then
        //        frmPanelGm.cboListaUsus.Clear
        //        For i = 0 To UBound(userList())
        //            Call frmPanelGm.cboListaUsus.AddItem(userList(i))
        //        Next i
        //        If frmPanelGm.cboListaUsus.ListCount > 0 Then frmPanelGm.cboListaUsus.ListIndex = 0
        //    End If


        incomingData.copyBuffer(buffer);
        Logger.debug("handleUserNameList Cargado! - FALTA TERMINAR!");
    }

    private static void handleShowGMPanelForm() {
        // Remove packet ID
        incomingData.readByte();
        //frmPanelGm.Show vbModeless, //FrmMain
    }

    private static void handleShowMOTDEditionForm() {
        if (incomingData.checkPacketData(3)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(incomingData);

        // Remove packet ID
        buffer.readByte();

        String txtMotd = buffer.readASCIIString();

        //frmCambiaMotd.txtMotd.Text = Buffer.ReadASCIIString()
        //    frmCambiaMotd.Show , //FrmMain
        //

        incomingData.copyBuffer(buffer);
        Logger.debug("handleShowMOTDEditionForm Cargado! - FALTA TERMINAR!");
    }

    private static void handleShowSOSForm() {
        if (incomingData.checkPacketData(3)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(incomingData);

        // Remove packet ID
        buffer.readByte();
        String sosList = buffer.readASCIIString();

        //Dim sosList() As String
        //    Dim i As Long
        //
        //    sosList = Split(Buffer.ReadASCIIString(), SEPARATOR)
        //
        //    For i = 0 To UBound(sosList())
        //        Call frmMSG.List1.AddItem(sosList(i))
        //    Next i
        //
        //    frmMSG.Show , //FrmMain

        incomingData.copyBuffer(buffer);
        Logger.debug("handleShowSOSForm Cargado! - FALTA TERMINAR!");
    }

    private static void handleSpawnList() {
        if (incomingData.checkPacketData(3)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(incomingData);

        // Remove packet ID
        buffer.readByte();

        String creatureList = buffer.readASCIIString();

        //Dim creatureList() As String
        //    Dim i As Long
        //
        //    creatureList = Split(Buffer.ReadASCIIString(), SEPARATOR)
        //
        //    For i = 0 To UBound(creatureList())
        //        Call frmSpawnList.lstCriaturas.AddItem(creatureList(i))
        //    Next i
        //    frmSpawnList.Show , //FrmMain
        //

        incomingData.copyBuffer(buffer);
        Logger.debug("handleSpawnList Cargado! - FALTA TERMINAR!");
    }

    private static void handleGuildMemberInfo() {
        if (incomingData.checkPacketData(5)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(incomingData);

        // Remove packet ID
        buffer.readByte();

        String guildNames = buffer.readASCIIString();
        String guildMembers = buffer.readASCIIString();


        //With frmGuildMember
        //        'Clear guild's list
        //        .lstClanes.Clear
        //
        //        GuildNames = Split(Buffer.ReadASCIIString(), SEPARATOR)
        //
        //        Dim i As Long
        //        For i = 0 To UBound(GuildNames())
        //            Call .lstClanes.AddItem(GuildNames(i))
        //        Next i
        //
        //        'Get list of guild's members
        //        GuildMembers = Split(Buffer.ReadASCIIString(), SEPARATOR)
        //        .lblCantMiembros.Caption = CStr(UBound(GuildMembers()) + 1)
        //
        //        'Empty the list
        //        Call .lstMiembros.Clear
        //
        //        For i = 0 To UBound(GuildMembers())
        //            Call .lstMiembros.AddItem(GuildMembers(i))
        //        Next i
        //
        //        'If we got here then packet is complete, copy data back to original queue
        //        Call incomingData.CopyBuffer(Buffer)
        //
        //        .Show vbModeless, //FrmMain
        //    End With

        incomingData.copyBuffer(buffer);
        Logger.debug("handleGuildMemberInfo Cargado! - FALTA TERMINAR!");
    }

    private static void handleUpdateTagAndStatus() {
        if (incomingData.checkPacketData(6)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(incomingData);

        // Remove packet ID
        buffer.readByte();

        short charIndex = buffer.readInteger();
        int nickColor = buffer.readByte();
        String userTag = buffer.readASCIIString();

        //Dim CharIndex As Integer
        //    Dim NickColor As Byte
        //    Dim UserTag As String
        //
        //    CharIndex = Buffer.ReadInteger()
        //    NickColor = Buffer.ReadByte()
        //    UserTag = Buffer.ReadASCIIString()
        //
        //    'Update char status adn tag!
        //    With charlist(CharIndex)
        //        If (NickColor And eNickColor.ieCriminal) <> 0 Then
        //            .Criminal = 1
        //        Else
        //            .Criminal = 0
        //        End If
        //
        //        .Atacable = (NickColor And eNickColor.ieAtacable) <> 0
        //
        //        .Nombre = UserTag
        //    End With

        incomingData.copyBuffer(buffer);
        Logger.debug("handleUpdateTagAndStatus Cargado! - FALTA TERMINAR!");
    }

    private static void handlePong() {
        // Remove packet ID
        incomingData.readByte();

        console.addMsgToConsole("El ping es " + (int) (glfwGetTime() - pingTime) + " ms.", false, false, new RGBColor(1f, 0f, 0f));
        pingTime = 0;

    }

    private static void handleSendNight() {
        if (incomingData.checkPacketData(2)) return;

        // Remove packet ID
        incomingData.readByte();

        boolean tBool = incomingData.readBoolean();
        Logger.debug("handleSendNight Cargado! - FALTA TERMINAR!");
    }

    private static void handleChangeUserTradeSlot() {
        if (incomingData.checkPacketData(22)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(incomingData);

        // Remove packet ID
        buffer.readByte();

        int offerSlot = buffer.readByte();

        buffer.readInteger();
        buffer.readLong();
        buffer.readInteger();
        buffer.readByte();
        buffer.readInteger();
        buffer.readInteger();
        buffer.readInteger();
        buffer.readInteger();
        buffer.readLong();
        buffer.readASCIIString();

        buffer.readInteger();
        buffer.readLong();
        buffer.readInteger();
        buffer.readByte();
        buffer.readInteger();
        buffer.readInteger();
        buffer.readInteger();
        buffer.readInteger();
        buffer.readLong();
        buffer.readASCIIString();

        //OfferSlot = Buffer.ReadByte
        //
        //    With Buffer
        //        If OfferSlot = GOLD_OFFER_SLOT Then
        //            Call InvOroComUsu(2).SetItem(1, .ReadInteger(), .ReadLong(), 0, _
        //                                            .ReadInteger(), .ReadByte(), .ReadInteger(), _
        //                                            .ReadInteger(), .ReadInteger(), .ReadInteger(), .ReadLong(), .ReadASCIIString())
        //        Else
        //            Call InvOfferComUsu(1).SetItem(OfferSlot, .ReadInteger(), .ReadLong(), 0, _
        //                                            .ReadInteger(), .ReadByte(), .ReadInteger(), _
        //                                            .ReadInteger(), .ReadInteger(), .ReadInteger(), .ReadLong(), .ReadASCIIString())
        //        End If
        //    End With
        //
        //    Call frmComerciarUsu.PrintCommerceMsg(TradingUserName & " ha modificado su oferta.", FontTypeNames.FONTTYPE_VENENO)

        incomingData.copyBuffer(buffer);
        Logger.debug("handleChangeUserTradeSlot Cargado! - FALTA TERMINAR!");
    }

    private static void handleBankOK() {
        // Remove packet ID
        incomingData.readByte();

        //Dim i As Long
        //
        //    If frmBancoObj.Visible Then
        //
        //        For i = 1 To Inventario.MaxObjs
        //            With Inventario
        //                Call InvBanco(1).SetItem(i, .OBJIndex(i), .Amount(i), _
        //                    .Equipped(i), .GrhIndex(i), .OBJType(i), .MaxHit(i), _
        //                    .MinHit(i), .MaxDef(i), .MinDef(i), .Valor(i), .ItemName(i))
        //            End With
        //        Next i
        //
        //        'Alter order according to if we bought or sold so the labels and grh remain the same
        //        If frmBancoObj.LasActionBuy Then
        //            'frmBancoObj.List1(1).ListIndex = frmBancoObj.LastIndex2
        //            'frmBancoObj.List1(0).ListIndex = frmBancoObj.LastIndex1
        //        Else
        //            'frmBancoObj.List1(0).ListIndex = frmBancoObj.LastIndex1
        //            'frmBancoObj.List1(1).ListIndex = frmBancoObj.LastIndex2
        //        End If
        //
        //        frmBancoObj.NoPuedeMover = False
        //    End If
    }

    private static void handleTradeOK() {
        // Remove packet ID
        incomingData.readByte();

        //If frmComerciar.Visible Then
        //        Dim i As Long
        //
        //        'Update user inventory
        //        For i = 1 To MAX_INVENTORY_SLOTS
        //            ' Agrego o quito un item en su totalidad
        //            If Inventario.OBJIndex(i) <> InvComUsu.OBJIndex(i) Then
        //                With Inventario
        //                    Call InvComUsu.SetItem(i, .OBJIndex(i), _
        //                    .Amount(i), .Equipped(i), .GrhIndex(i), _
        //                    .OBJType(i), .MaxHit(i), .MinHit(i), .MaxDef(i), .MinDef(i), _
        //                    .Valor(i), .ItemName(i))
        //                End With
        //            ' Vendio o compro cierta cantidad de un item que ya tenia
        //            ElseIf Inventario.Amount(i) <> InvComUsu.Amount(i) Then
        //                Call InvComUsu.ChangeSlotItemAmount(i, Inventario.Amount(i))
        //            End If
        //        Next i
        //
        //        ' Fill Npc inventory
        //        For i = 1 To 20
        //            ' Compraron la totalidad de un item, o vendieron un item que el npc no tenia
        //            If NPCInventory(i).OBJIndex <> InvComNpc.OBJIndex(i) Then
        //                With NPCInventory(i)
        //                    Call InvComNpc.SetItem(i, .OBJIndex, _
        //                    .Amount, 0, .GrhIndex, _
        //                    .OBJType, .MaxHit, .MinHit, .MaxDef, .MinDef, _
        //                    .Valor, .Name)
        //                End With
        //            ' Compraron o vendieron cierta cantidad (no su totalidad)
        //            ElseIf NPCInventory(i).Amount <> InvComNpc.Amount(i) Then
        //                Call InvComNpc.ChangeSlotItemAmount(i, NPCInventory(i).Amount)
        //            End If
        //        Next i
        //
        //    End If
    }

    private static void handleShowUserRequest() {
        if (incomingData.checkPacketData(3)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(incomingData);

        // Remove packet ID
        buffer.readByte();

        String recievePeticion = buffer.readASCIIString();

        // Call frmUserRequest.recievePeticion(Buffer.ReadASCIIString())
        //    Call frmUserRequest.Show(vbModeless, //FrmMain)
        //

        incomingData.copyBuffer(buffer);
        Logger.debug("handleShowUserRequest Cargado! - FALTA TERMINAR!");
    }

    private static void handleParalizeOK() {
        // Remove packet ID
        incomingData.readByte();

        if (charList[User.get().getUserCharIndex()].isParalizado()) {
            charList[User.get().getUserCharIndex()].setParalizado(false);
        } else {
            charList[User.get().getUserCharIndex()].setParalizado(true);
        }
    }

    private static void handleShowGuildFundationForm() {
        // Remove packet ID
        incomingData.readByte();

        //CreandoClan = True
        //    frmGuildFoundation.Show , //FrmMain
        Logger.debug("handleShowGuildFundationForm Cargado! - FALTA TERMINAR!");
    }

    private static void handleGuildDetails() {
        if (incomingData.checkPacketData(26)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(incomingData);

        // Remove packet ID
        buffer.readByte();

        buffer.readASCIIString();
        buffer.readASCIIString();
        buffer.readASCIIString();
        buffer.readASCIIString();
        buffer.readASCIIString();
        buffer.readInteger();

        buffer.readBoolean();

        buffer.readASCIIString();
        buffer.readInteger();
        buffer.readInteger();
        buffer.readASCIIString();

        buffer.readASCIIString();

        buffer.readASCIIString();

        //With frmGuildBrief
        //        .imgDeclararGuerra.Visible = .EsLeader
        //        .imgOfrecerAlianza.Visible = .EsLeader
        //        .imgOfrecerPaz.Visible = .EsLeader
        //
        //        .Nombre.Caption = Buffer.ReadASCIIString()
        //        .fundador.Caption = Buffer.ReadASCIIString()
        //        .creacion.Caption = Buffer.ReadASCIIString()
        //        .lider.Caption = Buffer.ReadASCIIString()
        //        .web.Caption = Buffer.ReadASCIIString()
        //        .Miembros.Caption = Buffer.ReadInteger()
        //
        //        If Buffer.ReadBoolean() Then
        //            .eleccion.Caption = "ABIERTA"
        //        Else
        //            .eleccion.Caption = "CERRADA"
        //        End If
        //
        //        .lblAlineacion.Caption = Buffer.ReadASCIIString()
        //        .Enemigos.Caption = Buffer.ReadInteger()
        //        .Aliados.Caption = Buffer.ReadInteger()
        //        .antifaccion.Caption = Buffer.ReadASCIIString()
        //
        //        Dim codexStr() As String
        //        Dim i As Long
        //
        //        codexStr = Split(Buffer.ReadASCIIString(), SEPARATOR)
        //
        //        For i = 0 To 7
        //            .Codex(i).Caption = codexStr(i)
        //        Next i
        //
        //        .Desc.Text = Buffer.ReadASCIIString()
        //    End With

        incomingData.copyBuffer(buffer);
        Logger.debug("handleGuildDetails Cargado! - FALTA TERMINAR!");
    }

    private static void handleGuildLeaderInfo() {
        if (incomingData.checkPacketData(9)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(incomingData);

        // Remove packet ID
        buffer.readByte();

        String guildNames = buffer.readASCIIString();
        guildNames = buffer.readASCIIString();
        String txtGuildNews = buffer.readASCIIString();
        String list = buffer.readASCIIString();

        //With frmGuildLeader
        //        'Get list of existing guilds
        //        GuildNames = Split(Buffer.ReadASCIIString(), SEPARATOR)
        //
        //        'Empty the list
        //        Call .guildslist.Clear
        //
        //        For i = 0 To UBound(GuildNames())
        //            Call .guildslist.AddItem(GuildNames(i))
        //        Next i
        //
        //        'Get list of guild's members
        //        GuildMembers = Split(Buffer.ReadASCIIString(), SEPARATOR)
        //        .Miembros.Caption = CStr(UBound(GuildMembers()) + 1)
        //
        //        'Empty the list
        //        Call .members.Clear
        //
        //        For i = 0 To UBound(GuildMembers())
        //            Call .members.AddItem(GuildMembers(i))
        //        Next i
        //
        //        .txtguildnews = Buffer.ReadASCIIString()
        //
        //        'Get list of join requests
        //        List = Split(Buffer.ReadASCIIString(), SEPARATOR)
        //
        //        'Empty the list
        //        Call .solicitudes.Clear
        //
        //        For i = 0 To UBound(List())
        //            Call .solicitudes.AddItem(List(i))
        //        Next i
        //
        //        .Show , //FrmMain
        //    End With

        incomingData.copyBuffer(buffer);
        Logger.debug("handleGuildLeaderInfo Cargado! - FALTA TERMINAR!");
    }

    private static void handleCharacterInfo() {
        if (incomingData.checkPacketData(35)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(incomingData);

        // Remove packet ID
        buffer.readByte();

        String nombre = buffer.readASCIIString();
        int raza = buffer.readByte();
        int clase = buffer.readByte();
        int genero = buffer.readByte();
        int nivel = buffer.readByte();
        int oro = buffer.readLong();
        int banco = buffer.readLong();
        int reputacion = buffer.readLong();

        String txtPeticiones = buffer.readASCIIString();
        String guildActual = buffer.readASCIIString();
        String txtMiembro = buffer.readASCIIString();

        boolean armada = buffer.readBoolean();
        boolean caos = buffer.readBoolean();

        int ciudadanos = buffer.readLong();
        int criminales = buffer.readLong();

        //With frmCharInfo
        //        If .frmType = CharInfoFrmType.frmMembers Then
        //            .imgRechazar.Visible = False
        //            .imgAceptar.Visible = False
        //            .imgEchar.Visible = True
        //            .imgPeticion.Visible = False
        //        Else
        //            .imgRechazar.Visible = True
        //            .imgAceptar.Visible = True
        //            .imgEchar.Visible = False
        //            .imgPeticion.Visible = True
        //        End If
        //
        //        .Nombre.Caption = Buffer.ReadASCIIString()
        //        .Raza.Caption = ListaRazas(Buffer.ReadByte())
        //        .Clase.Caption = ListaClases(Buffer.ReadByte())
        //
        //        If Buffer.ReadByte() = 1 Then
        //            .Genero.Caption = "Hombre"
        //        Else
        //            .Genero.Caption = "Mujer"
        //        End If
        //
        //        .Nivel.Caption = Buffer.ReadByte()
        //        .Oro.Caption = Buffer.ReadLong()
        //        .Banco.Caption = Buffer.ReadLong()
        //
        //        Dim reputation As Long
        //        reputation = Buffer.ReadLong()
        //
        //        .reputacion.Caption = reputation
        //
        //        .txtPeticiones.Text = Buffer.ReadASCIIString()
        //        .guildactual.Caption = Buffer.ReadASCIIString()
        //        .txtMiembro.Text = Buffer.ReadASCIIString()
        //
        //        Dim armada As Boolean
        //        Dim caos As Boolean
        //
        //        armada = Buffer.ReadBoolean()
        //        caos = Buffer.ReadBoolean()
        //
        //        If armada Then
        //            .ejercito.Caption = "Armada Real"
        //        ElseIf caos Then
        //            .ejercito.Caption = "Legión Oscura"
        //        End If
        //
        //        .Ciudadanos.Caption = CStr(Buffer.ReadLong())
        //        .criminales.Caption = CStr(Buffer.ReadLong())
        //
        //        If reputation > 0 Then
        //            .status.Caption = " Ciudadano"
        //            .status.ForeColor = vbBlue
        //        Else
        //            .status.Caption = " Criminal"
        //            .status.ForeColor = vbRed
        //        End If
        //
        //        Call .Show(vbModeless, //FrmMain)
        //    End With


        incomingData.copyBuffer(buffer);
        Logger.debug("handleCharacterInfo Cargado! - FALTA TERMINAR!");
    }

    private static void handlePeaceProposalsList() {
        if (incomingData.checkPacketData(3)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(incomingData);

        // Remove packet ID
        buffer.readByte();

        String guildList = buffer.readASCIIString();

        //Dim guildList() As String
        //    Dim i As Long
        //
        //    guildList = Split(Buffer.ReadASCIIString(), SEPARATOR)
        //
        //    Call frmPeaceProp.lista.Clear
        //    For i = 0 To UBound(guildList())
        //        Call frmPeaceProp.lista.AddItem(guildList(i))
        //    Next i
        //
        //    frmPeaceProp.ProposalType = TIPO_PROPUESTA.PAZ
        //    Call frmPeaceProp.Show(vbModeless, //FrmMain)

        incomingData.copyBuffer(buffer);
        Logger.debug("handlePeaceProposalsList Cargado! - FALTA TERMINAR!");
    }

    private static void handleAlianceProposalsList() {
        if (incomingData.checkPacketData(3)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(incomingData);

        // Remove packet ID
        buffer.readByte();

        String vsGuildList = buffer.readASCIIString();

        //Dim vsGuildList() As String
        //    Dim i As Long
        //
        //    vsGuildList = Split(Buffer.ReadASCIIString(), SEPARATOR)
        //
        //    Call frmPeaceProp.lista.Clear
        //    For i = 0 To UBound(vsGuildList())
        //        Call frmPeaceProp.lista.AddItem(vsGuildList(i))
        //    Next i
        //
        //    frmPeaceProp.ProposalType = TIPO_PROPUESTA.ALIANZA
        //    Call frmPeaceProp.Show(vbModeless, //FrmMain)

        incomingData.copyBuffer(buffer);
        Logger.debug("handleAlianceProposalsList Cargado! - FALTA TERMINAR!");
    }

    private static void handleOfferDetails() {
        if (incomingData.checkPacketData(3)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(incomingData);

        // Remove packet ID
        buffer.readByte();

        String recievePeticion = buffer.readASCIIString();

        incomingData.copyBuffer(buffer);
        Logger.debug("handleOfferDetails Cargado! - FALTA TERMINAR!");
    }

    private static void handleGuildNews() {
        if (incomingData.checkPacketData(7)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(incomingData);

        // Remove packet ID
        buffer.readByte();

        String news = buffer.readASCIIString();
        String guildList = buffer.readASCIIString();
        guildList = buffer.readASCIIString();

        //'Get news' string
        //    frmGuildNews.news = Buffer.ReadASCIIString()
        //
        //    'Get Enemy guilds list
        //    guildList = Split(Buffer.ReadASCIIString(), SEPARATOR)
        //
        //    For i = 0 To UBound(guildList)
        //        sTemp = frmGuildNews.txtClanesGuerra.Text
        //        frmGuildNews.txtClanesGuerra.Text = sTemp & guildList(i) & vbCrLf
        //    Next i
        //
        //    'Get Allied guilds list
        //    guildList = Split(Buffer.ReadASCIIString(), SEPARATOR)
        //
        //    For i = 0 To UBound(guildList)
        //        sTemp = frmGuildNews.txtClanesAliados.Text
        //        frmGuildNews.txtClanesAliados.Text = sTemp & guildList(i) & vbCrLf
        //    Next i
        //
        //    If ClientSetup.bGuildNews Then frmGuildNews.Show vbModeless, //FrmMain

        incomingData.copyBuffer(buffer);
        Logger.debug("handleGuildNews Cargado! - FALTA TERMINAR!");
    }

    private static void handleTrainerCreatureList() {
        if (incomingData.checkPacketData(3)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(incomingData);

        // Remove packet ID
        buffer.readByte();

        String creatures = buffer.readASCIIString();

        //creatures = Split(Buffer.ReadASCIIString(), SEPARATOR)
        //
        //    For i = 0 To UBound(creatures())
        //        Call frmEntrenador.lstCriaturas.AddItem(creatures(i))
        //    Next i
        //    frmEntrenador.Show , //FrmMain

        incomingData.copyBuffer(buffer);
        Logger.debug("handleTrainerCreatureList Cargado! - FALTA TERMINAR!");
    }

    private static void handleSendSkills() {
        if (incomingData.checkPacketData(2 + 20 * 2)) return;

        // Remove packet ID
        incomingData.readByte();

        // variables globales
        int userClase = incomingData.readByte();
        int userSkills[] = new int[20];
        int porcentajeSkills[] = new int[20];


        for (int i = 0; i < 20; i++) {
            userSkills[i] = incomingData.readByte();
            porcentajeSkills[i] = incomingData.readByte();
        }

        // LlegaronSkills = true;

        Logger.debug("handleSendSkills Cargado! - FALTA TERMINAR!");
    }

    private static void handleDumbNoMore() {
        // Remove packet ID
        incomingData.readByte();

        // userEstupido = false;
        Logger.debug("handleDumbNoMore Cargado! - FALTA TERMINAR!");
    }

    private static void handleBlindNoMore() {
        // Remove packet ID
        incomingData.readByte();

        //UserCiego = False
        Logger.debug("handleBlindNoMore Cargado! - FALTA TERMINAR!");
    }

    private static void handleMeditateToggle() {
        // Remove packet ID
        incomingData.readByte();

        //UserMeditar = Not UserMeditar
        Logger.debug("handleMeditateToggle Cargado! - FALTA TERMINAR!");
    }

    private static void handleDiceRoll() {
        if (incomingData.checkPacketData(6)) return;

        // Remove packet ID
        incomingData.readByte();

        int fuerza = incomingData.readByte();
        int agilidad = incomingData.readByte();
        int inteligencia = incomingData.readByte();
        int carisma = incomingData.readByte();
        int constitucion = incomingData.readByte();

        //UserAtributos(eAtributos.Fuerza) = incomingData.ReadByte()
        //    UserAtributos(eAtributos.Agilidad) = incomingData.ReadByte()
        //    UserAtributos(eAtributos.Inteligencia) = incomingData.ReadByte()
        //    UserAtributos(eAtributos.Carisma) = incomingData.ReadByte()
        //    UserAtributos(eAtributos.Constitucion) = incomingData.ReadByte()
        //
        //    With frmCrearPersonaje
        //        .lblAtributos(eAtributos.Fuerza) = UserAtributos(eAtributos.Fuerza)
        //        .lblAtributos(eAtributos.Agilidad) = UserAtributos(eAtributos.Agilidad)
        //        .lblAtributos(eAtributos.Inteligencia) = UserAtributos(eAtributos.Inteligencia)
        //        .lblAtributos(eAtributos.Carisma) = UserAtributos(eAtributos.Carisma)
        //        .lblAtributos(eAtributos.Constitucion) = UserAtributos(eAtributos.Constitucion)
        //
        //        .UpdateStats
        //    End With

        playSound(SND_DICE);

        Logger.debug("handleDiceRoll Cargado! - FALTA TERMINAR!");
    }

    private static void handleSetInvisible() {
        if (incomingData.checkPacketData(4)) return;

        // Remove packet ID
        incomingData.readByte();
        charList[incomingData.readInteger()].setInvisible(incomingData.readBoolean());
        Logger.debug("handleSetInvisible Cargado!");
    }

    private static void handleShowForumForm() {
        // Remove packet ID
        incomingData.readByte();

        int privilegios = incomingData.readByte();
        int canPostSticky = incomingData.readByte();

        //frmForo.Privilegios = incomingData.ReadByte
        //    frmForo.CanPostSticky = incomingData.ReadByte
        //
        //    If Not MirandoForo Then
        //        frmForo.Show , //FrmMain
        //    End If
        Logger.debug("handleShowForumForm Cargado! - FALTA TERMINAR!");
    }

    private static void handleAddForumMessage() {
        if (incomingData.checkPacketData(8)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(incomingData);

        // Remove packet ID
        buffer.readByte();


        int forumType = buffer.readByte();
        String title = buffer.readASCIIString();
        String autor = buffer.readASCIIString();
        String message = buffer.readASCIIString();

        //If Not frmForo.ForoLimpio Then
        //        clsForos.ClearForums
        //        frmForo.ForoLimpio = True
        //    End If
        //
        //    Call clsForos.AddPost(ForumAlignment(ForumType), Title, Author, Message, EsAnuncio(ForumType))


        incomingData.copyBuffer(buffer);
        Logger.debug("handleAddForumMessage Cargado! - FALTA TERMINAR!");
    }

    private static void handleLevelUp() {
        if (incomingData.checkPacketData(3)) return;

        // Remove packet ID
        incomingData.readByte();

        //  variable global:
        // skillPoints += incomingData.readInteger();
        short skillPoints = incomingData.readInteger();

        // //FrmMain.lightskillstar
        Logger.debug("handleLevelUp Cargado! - FALTA TERMINAR!");
    }

    private static void handleMiniStats() {
        if (incomingData.checkPacketData(20)) return;

        // Remove packet ID
        incomingData.readByte();

        incomingData.readLong();
        incomingData.readLong();
        incomingData.readLong();
        incomingData.readInteger();
        incomingData.readByte();
        incomingData.readLong();

        //With UserEstadisticas
        //        .CiudadanosMatados = incomingData.ReadLong()
        //        .CriminalesMatados = incomingData.ReadLong()
        //        .UsuariosMatados = incomingData.ReadLong()
        //        .NpcsMatados = incomingData.ReadInteger()
        //        .Clase = ListaClases(incomingData.ReadByte())
        //        .PenaCarcel = incomingData.ReadLong()
        //    End With

        Logger.debug("handleMiniStats Cargado! - FALTA TERMINAR!");
    }

    private static void handleFame() {
        if (incomingData.checkPacketData(29)) return;

        // Remove packet ID
        incomingData.readByte();

        incomingData.readLong();
        incomingData.readLong();
        incomingData.readLong();
        incomingData.readLong();
        incomingData.readLong();
        incomingData.readLong();
        incomingData.readLong();


        //With UserReputacion
        //        .AsesinoRep = incomingData.ReadLong()
        //        .BandidoRep = incomingData.ReadLong()
        //        .BurguesRep = incomingData.ReadLong()
        //        .LadronesRep = incomingData.ReadLong()
        //        .NobleRep = incomingData.ReadLong()
        //        .PlebeRep = incomingData.ReadLong()
        //        .Promedio = incomingData.ReadLong()
        //    End With
        // LlegoFama = True

        Logger.debug("handleFame Cargado! - FALTA TERMINAR!");
    }

    private static void handleUpdateHungerAndThirst() {
        if (incomingData.checkPacketData(5)) return;

        // Remove packet ID
        incomingData.readByte();

        User.get().setUserMaxAGU(incomingData.readByte());
        User.get().setUserMinAGU(incomingData.readByte());
        User.get().setUserMaxHAM(incomingData.readByte());
        User.get().setUserMinHAM(incomingData.readByte());
    }

    private static void handleChangeNPCInventorySlot() {
        if (incomingData.checkPacketData(21)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(incomingData);

        // Remove packet ID
        buffer.readByte();

        int slot = buffer.readByte();

        String name = buffer.readASCIIString();
        short amount = buffer.readInteger();
        float value = buffer.readFloat();
        short grhIndex = buffer.readInteger();
        short objIndex = buffer.readInteger();
        int objType = buffer.readByte();
        short maxHit = buffer.readInteger();
        short minHit = buffer.readInteger();
        short maxDef = buffer.readInteger();
        short minDef = buffer.readInteger();

        FComerce.invNPC.setItem(slot - 1, objIndex, amount, false, grhIndex, objType, maxHit, minHit, maxDef, minDef, value, name);
        incomingData.copyBuffer(buffer);
    }

    private static void handleShowSignal() {
        if (incomingData.checkPacketData(5)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(incomingData);

        // Remove packet ID
        buffer.readByte();

        String tmp = buffer.readASCIIString();
        short param = buffer.readInteger();
        // Call InitCartel(tmp, Buffer.ReadInteger())

        incomingData.copyBuffer(buffer);
        Logger.debug("handleShowSignal Cargado! - FALTA TERMINAR!");
    }

    private static void handleDumb() {
        // Remove packet ID
        incomingData.readByte();
        //UserEstupido = True
        Logger.debug("handleDumb Cargado! - FALTA TERMINAR!");
    }

    private static void handleBlind() {
        // Remove packet ID
        incomingData.readByte();
        //UserCiego = True
        Logger.debug("handleBlind Cargado! - FALTA TERMINAR!");
    }

    private static void handleErrorMessage() {
        if (incomingData.checkPacketData(3)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(incomingData);

        // Remove packet ID
        buffer.readByte();

        String errMsg = buffer.readASCIIString();
        ImGUISystem.get().show(new FMessage(errMsg));

        SocketConnection.get().disconnect();
        User.get().setUserConected(false);

        incomingData.copyBuffer(buffer);
    }

    private static void handleRestOK() {
        // Remove packet ID
        incomingData.readByte();

        //UserDescansar = Not UserDescansar
        Logger.debug("handleCarpenterObjects Cargado! - FALTA TERMINAR!");
    }

    private static void handleCarpenterObjects() {
        if (incomingData.checkPacketData(3)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(incomingData);

        // Remove packet ID
        buffer.readByte();

        short count = buffer.readInteger();

        for (int i = 0; i < count; i++) {
            buffer.readASCIIString();
            buffer.readInteger();
            buffer.readInteger();
            buffer.readInteger();
            buffer.readInteger();
            buffer.readInteger();
        }

        //ReDim ObjCarpintero(Count) As tItemsConstruibles
        //    ReDim CarpinteroMejorar(0) As tItemsConstruibles
        //
        //    For i = 1 To Count
        //        With ObjCarpintero(i)
        //            .Name = Buffer.ReadASCIIString()        'Get the object's name
        //            .GrhIndex = Buffer.ReadInteger()
        //            .Madera = Buffer.ReadInteger()          'The wood needed
        //            .MaderaElfica = Buffer.ReadInteger()    'The elfic wood needed
        //            .OBJIndex = Buffer.ReadInteger()
        //            .Upgrade = Buffer.ReadInteger()
        //        End With
        //    Next i
        //
        //    With frmCarp
        //        ' Inicializo los inventarios
        //        Call InvMaderasCarpinteria(1).Initialize(DirectDraw, .picMaderas0, 2, , , , , , False)
        //        Call InvMaderasCarpinteria(2).Initialize(DirectDraw, .picMaderas1, 2, , , , , , False)
        //        Call InvMaderasCarpinteria(3).Initialize(DirectDraw, .picMaderas2, 2, , , , , , False)
        //        Call InvMaderasCarpinteria(4).Initialize(DirectDraw, .picMaderas3, 2, , , , , , False)
        //
        //        Call .HideExtraControls(Count)
        //        Call .RenderList(1)
        //    End With
        //
        //    For i = 1 To Count
        //        With ObjCarpintero(i)
        //            If .Upgrade Then
        //                For k = 1 To Count
        //                    If .Upgrade = ObjCarpintero(k).OBJIndex Then
        //                        j = j + 1
        //
        //                        ReDim Preserve CarpinteroMejorar(j) As tItemsConstruibles
        //
        //                        CarpinteroMejorar(j).Name = .Name
        //                        CarpinteroMejorar(j).GrhIndex = .GrhIndex
        //                        CarpinteroMejorar(j).OBJIndex = .OBJIndex
        //                        CarpinteroMejorar(j).UpgradeName = ObjCarpintero(k).Name
        //                        CarpinteroMejorar(j).UpgradeGrhIndex = ObjCarpintero(k).GrhIndex
        //                        CarpinteroMejorar(j).Madera = ObjCarpintero(k).Madera - .Madera * 0.85
        //                        CarpinteroMejorar(j).MaderaElfica = ObjCarpintero(k).MaderaElfica - .MaderaElfica * 0.85
        //
        //                        Exit For
        //                    End If
        //                Next k
        //            End If
        //        End With
        //    Next i

        incomingData.copyBuffer(buffer);
        Logger.debug("handleCarpenterObjects Cargado! - FALTA TERMINAR!");
    }

    private static void handleBlacksmithArmors() {
        if (incomingData.checkPacketData(3)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(incomingData);

        // Remove packet ID
        buffer.readByte();

        short count = buffer.readInteger();

        for (int i = 0; i < count; i++) {
            buffer.readASCIIString();
            buffer.readInteger();
            buffer.readInteger();
            buffer.readInteger();
            buffer.readInteger();
            buffer.readInteger();
            buffer.readInteger();
        }

        //ReDim ArmadurasHerrero(Count) As tItemsConstruibles
        //
        //    For i = 1 To Count
        //        With ArmadurasHerrero(i)
        //            .Name = Buffer.ReadASCIIString()    'Get the object's name
        //            .GrhIndex = Buffer.ReadInteger()
        //            .LinH = Buffer.ReadInteger()        'The iron needed
        //            .LinP = Buffer.ReadInteger()        'The silver needed
        //            .LinO = Buffer.ReadInteger()        'The gold needed
        //            .OBJIndex = Buffer.ReadInteger()
        //            .Upgrade = Buffer.ReadInteger()
        //        End With
        //    Next i
        //
        //    j = UBound(HerreroMejorar)
        //
        //    For i = 1 To Count
        //        With ArmadurasHerrero(i)
        //            If .Upgrade Then
        //                For k = 1 To Count
        //                    If .Upgrade = ArmadurasHerrero(k).OBJIndex Then
        //                        j = j + 1
        //
        //                        ReDim Preserve HerreroMejorar(j) As tItemsConstruibles
        //
        //                        HerreroMejorar(j).Name = .Name
        //                        HerreroMejorar(j).GrhIndex = .GrhIndex
        //                        HerreroMejorar(j).OBJIndex = .OBJIndex
        //                        HerreroMejorar(j).UpgradeName = ArmadurasHerrero(k).Name
        //                        HerreroMejorar(j).UpgradeGrhIndex = ArmadurasHerrero(k).GrhIndex
        //                        HerreroMejorar(j).LinH = ArmadurasHerrero(k).LinH - .LinH * 0.85
        //                        HerreroMejorar(j).LinP = ArmadurasHerrero(k).LinP - .LinP * 0.85
        //                        HerreroMejorar(j).LinO = ArmadurasHerrero(k).LinO - .LinO * 0.85
        //
        //                        Exit For
        //                    End If
        //                Next k
        //            End If
        //        End With
        //    Next i

        incomingData.copyBuffer(buffer);
        Logger.debug("handleBlacksmithArmors Cargado! - FALTA TERMINAR!");

    }

    private static void handleBlacksmithWeapons() {
        if (incomingData.checkPacketData(3)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(incomingData);

        // Remove packet ID
        buffer.readByte();

        short count = buffer.readInteger();


        for (int i = 0; i < count; i++) {
            buffer.readASCIIString();
            buffer.readInteger();
            buffer.readInteger();
            buffer.readInteger();
            buffer.readInteger();
            buffer.readInteger();
            buffer.readInteger();
        }

        //ReDim ArmasHerrero(Count) As tItemsConstruibles
        //    ReDim HerreroMejorar(0) As tItemsConstruibles
        //
        //    For i = 1 To Count
        //        With ArmasHerrero(i)
        //            .Name = Buffer.ReadASCIIString()    'Get the object's name
        //            .GrhIndex = Buffer.ReadInteger()
        //            .LinH = Buffer.ReadInteger()        'The iron needed
        //            .LinP = Buffer.ReadInteger()        'The silver needed
        //            .LinO = Buffer.ReadInteger()        'The gold needed
        //            .OBJIndex = Buffer.ReadInteger()
        //            .Upgrade = Buffer.ReadInteger()
        //        End With
        //    Next i
        //
        //    With frmHerrero
        //        ' Inicializo los inventarios
        //        Call InvLingosHerreria(1).Initialize(DirectDraw, .picLingotes0, 3, , , , , , False)
        //        Call InvLingosHerreria(2).Initialize(DirectDraw, .picLingotes1, 3, , , , , , False)
        //        Call InvLingosHerreria(3).Initialize(DirectDraw, .picLingotes2, 3, , , , , , False)
        //        Call InvLingosHerreria(4).Initialize(DirectDraw, .picLingotes3, 3, , , , , , False)
        //
        //        Call .HideExtraControls(Count)
        //        Call .RenderList(1, True)
        //    End With
        //
        //    For i = 1 To Count
        //        With ArmasHerrero(i)
        //            If .Upgrade Then
        //                For k = 1 To Count
        //                    If .Upgrade = ArmasHerrero(k).OBJIndex Then
        //                        j = j + 1
        //
        //                        ReDim Preserve HerreroMejorar(j) As tItemsConstruibles
        //
        //                        HerreroMejorar(j).Name = .Name
        //                        HerreroMejorar(j).GrhIndex = .GrhIndex
        //                        HerreroMejorar(j).OBJIndex = .OBJIndex
        //                        HerreroMejorar(j).UpgradeName = ArmasHerrero(k).Name
        //                        HerreroMejorar(j).UpgradeGrhIndex = ArmasHerrero(k).GrhIndex
        //                        HerreroMejorar(j).LinH = ArmasHerrero(k).LinH - .LinH * 0.85
        //                        HerreroMejorar(j).LinP = ArmasHerrero(k).LinP - .LinP * 0.85
        //                        HerreroMejorar(j).LinO = ArmasHerrero(k).LinO - .LinO * 0.85
        //
        //                        Exit For
        //                    End If
        //                Next k
        //            End If
        //        End With
        //    Next i

        incomingData.copyBuffer(buffer);
        Logger.debug("handleBlacksmithWeapons Cargado! - FALTA TERMINAR!");
    }

    private static void handleAtributes() {
        if (incomingData.checkPacketData(6)) return;

        // Remove packet ID
        incomingData.readByte();

        int[] userAtributos = new int[5];
        for (int i = 0; i < 5; i++)
            userAtributos[i] = incomingData.readByte();

        //Dim i As Long
        //
        //    For i = 1 To NUMATRIBUTES
        //        UserAtributos(i) = incomingData.ReadByte()
        //    Next i
        //
        //    'Show them in character creation
        //    If EstadoLogin = E_MODO.Dados Then
        //        With frmCrearPersonaje
        //            If .Visible Then
        //                For i = 1 To NUMATRIBUTES
        //                    .lblAtributos(i).Caption = UserAtributos(i)
        //                Next i
        //
        //                .UpdateStats
        //            End If
        //        End With
        //    Else
        //        LlegaronAtrib = True
        //    End If

        Logger.debug("handleAtributes Cargado! - FALTA TERMINAR!");
    }

    private static void handleChangeSpellSlot() {
        if (incomingData.checkPacketData(6)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(incomingData);

        // Remove packet ID
        buffer.readByte();

        //int slot = buffer.readByte();
        buffer.readByte();
        //short hechizoNum = buffer.readInteger();
        buffer.readInteger();
        String hechizoName = buffer.readASCIIString();

        User.get().getInventorySpells().addSpell(hechizoName);

        incomingData.copyBuffer(buffer);
        Logger.debug("ChangeSpellSlot Cargado! - FALTA TERMINAR!");
    }

    private static void handleChangeBankSlot() {
        if (incomingData.checkPacketData(5)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(incomingData);

        // Remove packet ID
        buffer.readByte();

        int slot = buffer.readByte();

        short objIndex = buffer.readInteger();
        String name = buffer.readASCIIString();
        short amount = buffer.readInteger();
        short grhIndex = buffer.readInteger();
        int objType = buffer.readByte();
        short maxHit = buffer.readInteger();
        short minHit = buffer.readInteger();
        short maxDef = buffer.readInteger();
        short minDef = buffer.readInteger();
        int value = buffer.readLong();

        //With UserBancoInventory(slot)
        //        .OBJIndex = Buffer.ReadInteger()
        //        .Name = Buffer.ReadASCIIString()
        //        .Amount = Buffer.ReadInteger()
        //        .GrhIndex = Buffer.ReadInteger()
        //        .OBJType = Buffer.ReadByte()
        //        .MaxHit = Buffer.ReadInteger()
        //        .MinHit = Buffer.ReadInteger()
        //        .MaxDef = Buffer.ReadInteger()
        //        .MinDef = Buffer.ReadInteger
        //        .Valor = Buffer.ReadLong()
        //
        //        If Comerciando Then
        //            Call InvBanco(0).SetItem(slot, .OBJIndex, .Amount, _
        //                .Equipped, .GrhIndex, .OBJType, .MaxHit, _
        //                .MinHit, .MaxDef, .MinDef, .Valor, .Name)
        //        End If
        //    End With


        incomingData.copyBuffer(buffer);
        Logger.debug("handleChangeBankSlot Cargado! - FALTA TERMINAR!");
    }

    private static void handleChangeInventorySlot() {
        if (incomingData.checkPacketData(22)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(incomingData);

        // Remove packet
        buffer.readByte();

        final int slot = buffer.readByte();
        final short objIndex = buffer.readInteger();
        final String name = buffer.readASCIIString();
        final int amount = buffer.readInteger();
        final boolean equipped = buffer.readBoolean();
        final short grhIndex = buffer.readInteger();
        final int objType = buffer.readByte();
        final short maxHit = buffer.readInteger();
        final short minHit = buffer.readInteger();
        final short maxDef = buffer.readInteger();
        final short minDef = buffer.readInteger();
        final float value = buffer.readFloat();

        if (equipped) {
            switch (E_ObjType.values()[objType - 1]) {
                case otWeapon:
                    User.get().setUserWeaponEqpHit(minHit + "/" + maxHit);
                    User.get().setUserWeaponEqpSlot(slot);
                    break;

                case otArmor:
                    User.get().setUserArmourEqpDef(minDef + "/" + maxDef);
                    User.get().setUserArmourEqpSlot(slot);
                    break;

                case otShield:
                    User.get().setUserShieldEqpDef(minDef + "/" + maxDef);
                    User.get().setUserShieldEqpSlot(slot);
                    break;

                case otHelmet:
                    User.get().setUserHelmEqpDef(minDef + "/" + maxDef);
                    User.get().setUserHelmEqpSlot(slot);
                    break;
            }
        } else {
            if (slot == User.get().getUserWeaponEqpSlot()) {
                User.get().setUserWeaponEqpHit("0/0");
                User.get().setUserWeaponEqpSlot((byte) 0);

            } else if (slot == User.get().getUserArmourEqpSlot()) {
                User.get().setUserArmourEqpDef("0/0");
                User.get().setUserArmourEqpSlot((byte) 0);

            } else if (slot == User.get().getUserShieldEqpSlot()) {
                User.get().setUserShieldEqpDef("0/0");
                User.get().setUserShieldEqpSlot((byte) 0);

            } else if (slot == User.get().getUserHelmEqpSlot()) {
                User.get().setUserHelmEqpDef("0/0");
                User.get().setUserHelmEqpSlot((byte) 0);

            }
        }

        User.get().getUserInventory().setItem(slot - 1, objIndex, amount, equipped, grhIndex, objType,
                maxHit, minHit, maxDef, minDef, value, name);

        incomingData.copyBuffer(buffer);
    }

    private static void handleWorkRequestTarget() {
        if (incomingData.checkPacketData(2)) return;

        // Remove packet ID
        incomingData.readByte();

        final int usingSkill = incomingData.readByte();
        User.get().setUsingSkill(usingSkill);

        Window.get().setCursorCrosshair(true);

        switch (E_Skills.values()[usingSkill - 1]) {
            case Magia:
                console.addMsgToConsole(MENSAJE_TRABAJO_MAGIA, false, false, new RGBColor());
                break;

            case Pesca:
                console.addMsgToConsole(MENSAJE_TRABAJO_PESCA, false, false, new RGBColor());
                break;

            case Robar:
                console.addMsgToConsole(MENSAJE_TRABAJO_ROBAR, false, false, new RGBColor());
                break;

            case Talar:
                console.addMsgToConsole(MENSAJE_TRABAJO_TALAR, false, false, new RGBColor());
                break;

            case Mineria:
                console.addMsgToConsole(MENSAJE_TRABAJO_MINERIA, false, false, new RGBColor());
                break;

            case Proyectiles:
                console.addMsgToConsole(MENSAJE_TRABAJO_PROYECTILES, false, false, new RGBColor());
                break;
        }

        if (usingSkill == FundirMetal) {
            console.addMsgToConsole(MENSAJE_TRABAJO_FUNDIRMETAL, false, false, new RGBColor());
        }

        Logger.debug("handleWorkRequestTarget Cargado! - FALTA TESTIAR!");
    }

    private static void handleUpdateUserStats() {
        if (incomingData.checkPacketData(26)) return;

        // Remove packet ID
        incomingData.readByte();

        User.get().setUserMaxHP(incomingData.readInteger());
        User.get().setUserMinHP(incomingData.readInteger());
        User.get().setUserMaxMAN(incomingData.readInteger());
        User.get().setUserMinMAN(incomingData.readInteger());
        User.get().setUserMaxSTA(incomingData.readInteger());
        User.get().setUserMinSTA(incomingData.readInteger());
        User.get().setUserGLD(incomingData.readLong());
        User.get().setUserLvl(incomingData.readByte());
        User.get().setUserPasarNivel(incomingData.readLong());
        User.get().setUserExp(incomingData.readLong());


        charList[User.get().getUserCharIndex()].setDead(User.get().getUserMinHP() <= 0);

        //
        //    If UserMinHP = 0 Then
        //        UserEstado = 1
        //        If //FrmMain.TrainingMacro Then Call //FrmMain.DesactivarMacroHechizos
        //        If //FrmMain.macrotrabajo Then Call //FrmMain.DesactivarMacroTrabajo
        //    Else
        //        UserEstado = 0
        //    End If
        //
    }

    private static void handleCreateFX() {
        if (incomingData.checkPacketData(7)) return;

        // Remove packet ID
        incomingData.readByte();

        short charIndex = incomingData.readInteger();
        short fX = incomingData.readInteger();
        short loops = incomingData.readInteger();

        User.get().setCharacterFx(charIndex, fX, loops);
    }

    private static void handleRainToggle() {
        // Remove packet ID
        incomingData.readByte();

        final int userX = User.get().getUserPos().getX();
        final int userY = User.get().getUserPos().getY();
        if (User.get().inMapBounds(userX, userY)) return;

        User.get().setUnderCeiling(User.get().checkUnderCeiling());

        if (Rain.get().isRaining()) {
            Rain.get().setRainValue(false);
            Rain.get().stopRainingSoundLoop();
            Rain.get().playEndRainSound();
        } else {
            Rain.get().setRainValue(true);
        }

    }

    private static void handlePauseToggle() {
        // Remove packet ID
        incomingData.readByte();
        //pausa = Not pausa
        Logger.debug("handlePauseToggle CARGADO - FALTA TERMINAR!");
    }

    private static void handleAreaChanged() {
        if (incomingData.checkPacketData(3)) return;

        // Remove packet ID
        incomingData.readByte();

        int x = incomingData.readByte();
        int y = incomingData.readByte();

        User.get().areaChange(x, y);
    }

    private static void handleGuildList() {
        if (incomingData.checkPacketData(3)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(incomingData);

        // Remove packet ID
        buffer.readByte();

        String guildNames = buffer.readASCIIString();

        //With frmGuildAdm
        //        'Clear guild's list
        //        .guildslist.Clear
        //
        //        GuildNames = Split(Buffer.ReadASCIIString(), SEPARATOR)
        //
        //        Dim i As Long
        //        For i = 0 To UBound(GuildNames())
        //            Call .guildslist.AddItem(GuildNames(i))
        //        Next i
        //
        //        'If we got here then packet is complete, copy data back to original queue
        //        Call incomingData.CopyBuffer(Buffer)
        //
        //        .Show vbModeless, //FrmMain
        //    End With

        incomingData.copyBuffer(buffer);
        Logger.debug("handleGuildList CARGADO - FALTA TERMINAR!");
    }

    private static void handlePlayWave() {
        if (incomingData.checkPacketData(3)) return;

        // Remove packet ID
        incomingData.readByte();

        int wave = incomingData.readByte();
        int srcX = incomingData.readByte();
        int srcY = incomingData.readByte();

        // Call Audio.PlayWave(CStr(wave) & ".wav", srcX, srcY)
        playSound(String.valueOf(wave) + ".ogg");
    }

    private static void handlePlayMIDI() {
        if (incomingData.checkPacketData(4)) return;

        // Remove packet ID
        incomingData.readByte();

        int currentMusic = incomingData.readByte();

        if (currentMusic > 0) {
            incomingData.readInteger();
            // play music
            playMusic(String.valueOf(currentMusic) + ".ogg");
        } else {
            //Remove the bytes to prevent errors
            incomingData.readInteger();
        }

        Logger.debug("handlePlayMIDI Cargado! - FALTA TERMINAR!");
    }

    private static void handleBlockPosition() {
        if (incomingData.checkPacketData(4)) return;

        // Remove packet ID
        incomingData.readByte();

        int x = incomingData.readByte();
        int y = incomingData.readByte();

        mapData[x][y].setBlocked(incomingData.readBoolean());
    }

    private static void handleObjectDelete() {
        if (incomingData.checkPacketData(3)) return;

        // Remove packet ID
        incomingData.readByte();

        int x = incomingData.readByte();
        int y = incomingData.readByte();

        mapData[x][y].getObjGrh().setGrhIndex((short) 0);
    }

    private static void handleObjectCreate() {
        if (incomingData.checkPacketData(5)) return;

        // Remove packet ID
        incomingData.readByte();

        int x = incomingData.readByte();
        int y = incomingData.readByte();
        short grhIndex = incomingData.readInteger();

        mapData[x][y].getObjGrh().setGrhIndex(grhIndex);
        initGrh(mapData[x][y].getObjGrh(), mapData[x][y].getObjGrh().getGrhIndex(), true);
    }

    private static void handleCharacterChange() {
        if (incomingData.checkPacketData(18)) return;

        // Remove packet ID
        incomingData.readByte();

        short charIndex = incomingData.readInteger();
        short tempint = incomingData.readInteger();

        // esta dentro del rango del array de bodydata?
        if (tempint < 1 || tempint > bodyData.length) {
            charList[charIndex].setBody(new BodyData(bodyData[0]));
            charList[charIndex].setiBody(0);
        } else {
            charList[charIndex].setBody(new BodyData(bodyData[tempint]));
            charList[charIndex].setiBody(tempint);
        }

        short headIndex = incomingData.readInteger();

        if (headIndex < 1 || headIndex > headData.length) {
            charList[charIndex].setHead(headData[0]);
            charList[charIndex].setiHead(0);
        } else {
            charList[charIndex].setHead(headData[headIndex]);
            charList[charIndex].setiHead(headIndex);
        }

        charList[charIndex].setDead(headIndex == CASPER_HEAD);
        charList[charIndex].setHeading(E_Heading.values()[incomingData.readByte() - 1]);

        tempint = incomingData.readInteger();
        if (tempint != 0) {
            charList[charIndex].setWeapon(new WeaponData(weaponData[tempint]));
        }

        tempint = incomingData.readInteger();
        if (tempint != 0) {
            charList[charIndex].setShield(new ShieldData(shieldData[tempint]));
        }

        tempint = incomingData.readInteger();
        if (tempint != 0) {
            charList[charIndex].setHelmet(new HeadData(helmetsData[tempint]));
        }

        User.get().setCharacterFx(charIndex, incomingData.readInteger(), incomingData.readInteger());

        refreshAllChars();
        Logger.debug("handleCharacterChange Cargado! - FALTA TERMINAR!");
    }

    private static void handleForceCharMove() {
        if (incomingData.checkPacketData(2)) return;

        // Remove packet ID
        incomingData.readByte();

        E_Heading direction = E_Heading.values()[incomingData.readByte() - 1];
        short userCharIndex = User.get().getUserCharIndex();
        User.get().moveCharbyHead(userCharIndex, direction);
        User.get().moveScreen(direction);

        refreshAllChars();
    }

    private static void handleCharacterMove() {
        if (incomingData.checkPacketData(5)) return;

        // Remove packet ID
        incomingData.readByte();

        short charIndex = incomingData.readInteger();
        int x = incomingData.readByte();
        int y = incomingData.readByte();

        // Si esta meditando, removemos el FX.
        if (charList[charIndex].getFxIndex() >= 40 && charList[charIndex].getFxIndex() <= 49) {
            charList[charIndex].setFxIndex(0);
        }

        // Play steps sounds if the user is not an admin of any kind
        int priv = charList[charIndex].getPriv();
        if (priv != 1 && priv != 2 && priv != 3 && priv != 5 && priv != 25) {
            User.get().doPasosFx(charIndex);
        }

        User.get().moveCharbyPos(charIndex, x, y);
        refreshAllChars();
    }

    private static void handleCharacterChangeNick() {
        if (incomingData.checkPacketData(5)) return;

        // Remove packet ID
        incomingData.readByte();
        charList[incomingData.readInteger()].setName(incomingData.readASCIIString());
    }

    private static void handleCharacterRemove() {
        if (incomingData.checkPacketData(3)) return;

        // Remove packet ID
        incomingData.readByte();

        eraseChar(incomingData.readInteger());
        refreshAllChars();
    }

    private static void handleCharacterCreate() {
        if (incomingData.checkPacketData(24)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(incomingData);

        // Remove packet ID
        buffer.readByte();

        short charIndex = buffer.readInteger();
        short body = buffer.readInteger();
        short head = buffer.readInteger();
        int numHeading = buffer.readByte();
        E_Heading heading = E_Heading.values()[numHeading - 1];
        int x = buffer.readByte();
        int y = buffer.readByte();
        short weapon = buffer.readInteger();
        short shield = buffer.readInteger();
        short helmet = buffer.readInteger();

        User.get().setCharacterFx(charIndex, buffer.readInteger(), buffer.readInteger());

        charList[charIndex].setName(buffer.readASCIIString());

        int nickColor = buffer.readByte();
        int privs = buffer.readByte();

        if ((nickColor & NickColor.CRIMINAL.getId()) != 0) {
            charList[charIndex].setCriminal(true);
        } else {
            charList[charIndex].setCriminal(false);
        }

        charList[charIndex].setAttackable((nickColor & NickColor.ATACABLE.getId()) != 0);

        if (privs != 0) {
            if ((privs & PlayerType.CHAOS_COUNCIL.getId()) != 0 && (privs & PlayerType.USER.getId()) == 0) {
                privs = (short) (privs ^ PlayerType.CHAOS_COUNCIL.getId());
            }

            if ((privs & PlayerType.ROYAL_COUNCIL.getId()) != 0 && (privs & PlayerType.USER.getId()) == 0) {
                privs = (short) (privs ^ PlayerType.ROYAL_COUNCIL.getId());
            }

            if ((privs & PlayerType.ROLE_MASTER.getId()) != 0) {
                privs = PlayerType.ROLE_MASTER.getId();
            }

            final int logPrivs = (int) (Math.log(privs) / Math.log(2));
            charList[charIndex].setPriv(logPrivs);
        } else {
            charList[charIndex].setPriv(0);
        }

        makeChar(charIndex, body, head, heading, x, y, weapon, shield, helmet);
        refreshAllChars();

        incomingData.copyBuffer(buffer);
    }

    private static void handleUserCharIndexInServer() {
        if (incomingData.checkPacketData(3)) return;

        // Remove packet ID
        incomingData.readByte();

        User.get().setUserCharIndex(incomingData.readInteger());
        User.get().getUserPos().setX(charList[User.get().getUserCharIndex()].getPos().getX());
        User.get().getUserPos().setY(charList[User.get().getUserCharIndex()].getPos().getY());
        User.get().setUnderCeiling(User.get().checkUnderCeiling());
    }

    private static void handleUserIndexInServer() {
        if (incomingData.checkPacketData(3)) return;

        // Remove packet ID
        incomingData.readByte();

        int userIndex = incomingData.readInteger();
        Logger.debug("No le encontre utilidad a este paquete....");
    }

    private static void handleShowMessageBox() {
        if (incomingData.checkPacketData(3)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(incomingData);

        // Remove packet ID
        buffer.readByte();

        String msg = buffer.readASCIIString();
        ImGUISystem.get().show(new FMessage(msg));

        incomingData.copyBuffer(buffer);
    }

    private static void handleGuildChat() {
        if (incomingData.checkPacketData(3)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(incomingData);

        // Remove packet ID
        buffer.readByte();

        String chat = buffer.readASCIIString();

        //Dim str As String
        //    Dim r As Byte
        //    Dim g As Byte
        //    Dim b As Byte
        //    Dim tmp As Integer
        //    Dim Cont As Integer
        //
        //
        //    chat = Buffer.ReadASCIIString()
        //
        //    If Not DialogosClanes.Activo Then
        //        If InStr(1, chat, "~") Then
        //            str = ReadField(2, chat, 126)
        //            If Val(str) > 255 Then
        //                r = 255
        //            Else
        //                r = Val(str)
        //            End If
        //
        //            str = ReadField(3, chat, 126)
        //            If Val(str) > 255 Then
        //                g = 255
        //            Else
        //                g = Val(str)
        //            End If
        //
        //            str = ReadField(4, chat, 126)
        //            If Val(str) > 255 Then
        //                b = 255
        //            Else
        //                b = Val(str)
        //            End If
        //
        //            Call AddtoRichTextBox(//FrmMain.RecTxt, Left$(chat, InStr(1, chat, "~") - 1), r, g, b, Val(ReadField(5, chat, 126)) <> 0, Val(ReadField(6, chat, 126)) <> 0)
        //        Else
        //            With FontTypes(FontTypeNames.FONTTYPE_GUILDMSG)
        //                Call AddtoRichTextBox(//FrmMain.RecTxt, chat, .red, .green, .blue, .bold, .italic)
        //            End With
        //        End If
        //    Else
        //        Call DialogosClanes.PushBackText(ReadField(1, chat, 126))
        //    End If

        incomingData.copyBuffer(buffer);
        Logger.debug("handleGuildChat CARGADO - FALTA TERMINAR");
    }

    private static void handleConsoleMessage() {
        if (incomingData.checkPacketData(4)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(incomingData);

        // Remove packet ID
        buffer.readByte();

        String chat = buffer.readASCIIString();
        E_FontType fontType = E_FontType.values()[buffer.readByte()];

        console.addMsgToConsole(chat, false, false, new RGBColor(fontType.r, fontType.g, fontType.b));


        //        With FontTypes(FontIndex)
        //            Call AddtoRichTextBox(//FrmMain.RecTxt, chat, .red, .green, .blue, .bold, .italic)
        //        End With
        //
        //        ' Para no perder el foco cuando chatea por party
        //        If FontIndex = FontTypeNames.FONTTYPE_PARTY Then
        //            If MirandoParty Then frmParty.SendTxt.SetFocus
        //        End If
        //    End If

        incomingData.copyBuffer(buffer);
        Logger.debug("handleConsoleMessage CARGADO - FALTA TERMINAR!");
    }

    private static void handleChatOverHead() {
        if (incomingData.checkPacketData(8)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(incomingData);

        // Remove packet ID
        buffer.readByte();

        String chat = buffer.readASCIIString();
        short charIndex = buffer.readInteger();
        int r = buffer.readByte();
        int g = buffer.readByte();
        int b = buffer.readByte();

        // es un NPC?
        if (charList[charIndex].getName().length() <= 1) {
            Dialogs.removeDialogsNPCArea();
        }

        charDialogSet(charIndex, chat, new RGBColor((float) r / 255, (float) g / 255, (float) b / 255));

        incomingData.copyBuffer(buffer);
    }

    private static void handlePosUpdate() {
        if (incomingData.checkPacketData(3)) return;

        // Remove packet ID
        incomingData.readByte();

        int x = User.get().getUserPos().getX();
        int y = User.get().getUserPos().getY();
        int userCharIndex = User.get().getUserCharIndex();

        // Remove char form old position
        if (mapData[x][y].getCharIndex() == userCharIndex) {
            mapData[x][y].setCharIndex(0);
        }

        // Set new pos
        User.get().getUserPos().setX(incomingData.readByte());
        User.get().getUserPos().setY(incomingData.readByte());

        // again xd
        x = User.get().getUserPos().getX();
        y = User.get().getUserPos().getY();

        mapData[x][y].setCharIndex(userCharIndex);
        charList[userCharIndex].getPos().setX(x);
        charList[userCharIndex].getPos().setY(y);
    }

    private static void handleChangeMap() {
        if (incomingData.checkPacketData(5)) return;

        // Remove packet ID
        incomingData.readByte();

        short userMap = incomingData.readInteger();
        User.get().setUserMap(userMap);

        // Once on-the-fly editor is implemented check for map version before loading....
        // For now we just drop it
        incomingData.readInteger();

        GameData.loadMap(userMap);

        if (!bLluvia[userMap]) {
            if (Rain.get().isRaining()) {
                Rain.get().stopRainingSoundLoop();
            }
        }

    }

    private static void handleUpdateExp() {
        if (incomingData.checkPacketData(5)) return;

        // Remove packet ID
        incomingData.readByte();

        // Get data and update
        User.get().setUserExp(incomingData.readLong());
    }

    private static void handleUpdateBankGold() {
        if (incomingData.checkPacketData(5)) return;

        // Remove packet ID
        incomingData.readByte();
        int bankGold = incomingData.readLong();
        //frmBancoObj.lblUserGld.Caption = incomingData.ReadLong
        Logger.debug("handleUpdateBankGold CARGADO - FALTA TERMINAR!");
    }

    private static void handleUpdateGold() {
        if (incomingData.checkPacketData(5)) return;

        // Remove packet ID
        incomingData.readByte();

        User.get().setUserGLD(incomingData.readLong());
    }

    private static void handleUpdateHP() {
        if (incomingData.checkPacketData(3)) return;

        // Remove packet ID
        incomingData.readByte();

        User.get().setUserMinHP(incomingData.readInteger());

        charList[User.get().getUserCharIndex()].setDead(User.get().getUserMinHP() <= 0);

        //
        //    'Is the user alive??
        //    If UserMinHP = 0 Then
        //        UserEstado = 1
        //        If //FrmMain.TrainingMacro Then Call //FrmMain.DesactivarMacroHechizos
        //        If //FrmMain.macrotrabajo Then Call //FrmMain.DesactivarMacroTrabajo
        //    Else
        //        UserEstado = 0
        //    End If

        Logger.debug("handleUpdateHP CARGADO - FALTA DESACTIVAR MACROS DE TRABAJO!");
    }

    private static void handleUpdateMana() {
        if (incomingData.checkPacketData(3)) return;

        // Remove packet ID
        incomingData.readByte();

        // variable global
        User.get().setUserMinMAN(incomingData.readInteger());
    }

    private static void handleUpdateSta() {
        if (incomingData.checkPacketData(3)) return;

        // Remove packet ID
        incomingData.readByte();

        // variable global
        User.get().setUserMinSTA(incomingData.readInteger());
    }

    private static void handleShowCarpenterForm() {
        // Remove packet ID
        incomingData.readByte();

        //If //FrmMain.macrotrabajo.Enabled And (MacroBltIndex > 0) Then
        //        Call WriteCraftCarpenter(MacroBltIndex)
        //    Else
        //        frmCarp.Show , //FrmMain
        //    End If

        Logger.debug("handleShowCarpenterForm CARGADO - FALTA TERMINAR!");
    }

    private static void handleShowBlacksmithForm() {
        // Remove packet ID
        incomingData.readByte();

        //If //FrmMain.macrotrabajo.Enabled And (MacroBltIndex > 0) Then
        //        Call WriteCraftBlacksmith(MacroBltIndex)
        //    Else
        //        frmHerrero.Show , //FrmMain
        //    End If

        Logger.debug("handleShowBlacksmithForm CARGADO - FALTA TERMINAR!");
    }

    private static void handleUserOfferConfirm() {
        // Remove packet ID
        incomingData.readByte();

        // With frmComerciarUsu
        //        ' Now he can accept the offer or reject it
        //        .HabilitarAceptarRechazar True
        //
        //        .PrintCommerceMsg TradingUserName & " ha confirmado su oferta!", FontTypeNames.FONTTYPE_CONSE
        //    End With

        Logger.debug("handleUserOfferConfirm CARGADO - FALTA TERMINAR!");
    }

    private static void handleUserCommerceEnd() {
        // Remove packet ID
        incomingData.readByte();

        //Set InvComUsu = Nothing
        //    Set InvOroComUsu(0) = Nothing
        //    Set InvOroComUsu(1) = Nothing
        //    Set InvOroComUsu(2) = Nothing
        //    Set InvOfferComUsu(0) = Nothing
        //    Set InvOfferComUsu(1) = Nothing
        //
        //    'Destroy the form and reset the state
        //    Unload frmComerciarUsu
        //    Comerciando = False

        Logger.debug("handleUserCommerceEnd CARGADO - FALTA TERMINAR!");
    }

    private static void handleUserCommerceInit() {
        // Remove packet ID
        incomingData.readByte();

        // variable local vb6
        String tradingUserName = incomingData.readASCIIString();

        //' Initialize commerce inventories
        //    Call InvComUsu.Initialize(DirectDraw, frmComerciarUsu.picInvComercio, Inventario.MaxObjs)
        //    Call InvOfferComUsu(0).Initialize(DirectDraw, frmComerciarUsu.picInvOfertaProp, INV_OFFER_SLOTS)
        //    Call InvOfferComUsu(1).Initialize(DirectDraw, frmComerciarUsu.picInvOfertaOtro, INV_OFFER_SLOTS)
        //    Call InvOroComUsu(0).Initialize(DirectDraw, frmComerciarUsu.picInvOroProp, INV_GOLD_SLOTS, , TilePixelWidth * 2, TilePixelHeight, TilePixelWidth / 2)
        //    Call InvOroComUsu(1).Initialize(DirectDraw, frmComerciarUsu.picInvOroOfertaProp, INV_GOLD_SLOTS, , TilePixelWidth * 2, TilePixelHeight, TilePixelWidth / 2)
        //    Call InvOroComUsu(2).Initialize(DirectDraw, frmComerciarUsu.picInvOroOfertaOtro, INV_GOLD_SLOTS, , TilePixelWidth * 2, TilePixelHeight, TilePixelWidth / 2)
        //
        //    'Fill user inventory
        //    For i = 1 To MAX_INVENTORY_SLOTS
        //        If Inventario.OBJIndex(i) <> 0 Then
        //            With Inventario
        //                Call InvComUsu.SetItem(i, .OBJIndex(i), _
        //                .Amount(i), .Equipped(i), .GrhIndex(i), _
        //                .OBJType(i), .MaxHit(i), .MinHit(i), .MaxDef(i), .MinDef(i), _
        //                .Valor(i), .ItemName(i))
        //            End With
        //        End If
        //    Next i
        //
        //    ' Inventarios de oro
        //    Call InvOroComUsu(0).SetItem(1, ORO_INDEX, UserGLD, 0, ORO_GRH, 0, 0, 0, 0, 0, 0, "Oro")
        //    Call InvOroComUsu(1).SetItem(1, ORO_INDEX, 0, 0, ORO_GRH, 0, 0, 0, 0, 0, 0, "Oro")
        //    Call InvOroComUsu(2).SetItem(1, ORO_INDEX, 0, 0, ORO_GRH, 0, 0, 0, 0, 0, 0, "Oro")
        //
        //
        //    'Set state and show form
        //    Comerciando = True
        //    Call frmComerciarUsu.Show(vbModeless, //FrmMain)

        Logger.debug("handleUserCommerceInit CARGADO - FALTA TERMINAR!");
    }

    private static void handleBankInit() {
        // Remove packet ID
        incomingData.readByte();

        int bankGold = incomingData.readLong();
        //Call InvBanco(0).Initialize(DirectDraw, frmBancoObj.PicBancoInv, MAX_BANCOINVENTORY_SLOTS)
        //Call InvBanco(1).Initialize(DirectDraw, frmBancoObj.PicInv, Inventario.MaxObjs)

        // For i = 1 To Inventario.MaxObjs
        //        With Inventario
        //            Call InvBanco(1).SetItem(i, .OBJIndex(i), _
        //                .Amount(i), .Equipped(i), .GrhIndex(i), _
        //                .OBJType(i), .MaxHit(i), .MinHit(i), .MaxDef(i), .MinDef(i), _
        //                .Valor(i), .ItemName(i))
        //        End With
        //    Next i
        //
        //    For i = 1 To MAX_BANCOINVENTORY_SLOTS
        //        With UserBancoInventory(i)
        //            Call InvBanco(0).SetItem(i, .OBJIndex, _
        //                .Amount, .Equipped, .GrhIndex, _
        //                .OBJType, .MaxHit, .MinHit, .MaxDef, .MinDef, _
        //                .Valor, .Name)
        //        End With
        //    Next i
        //
        //    'Set state and show form
        //    Comerciando = True
        //
        //    frmBancoObj.lblUserGld.Caption = BankGold
        //
        //    frmBancoObj.Show , //FrmMain

        Logger.debug("handleBankInit CARGADO - FALTA TERMINAR!");

    }

    private static void handleCommerceInit() {
        // Remove packet ID
        incomingData.readByte();

        ImGUISystem.get().show(new FComerce());

        /*
            ' Initialize commerce inventories
            Call InvComUsu.Initialize(DirectDraw, frmComerciar.picInvUser, Inventario.MaxObjs)
            Call InvComNpc.Initialize(DirectDraw, frmComerciar.picInvNpc, MAX_NPC_INVENTORY_SLOTS)

            'Fill user inventory
            For i = 1 To MAX_INVENTORY_SLOTS
                If Inventario.OBJIndex(i) <> 0 Then
                    With Inventario
                        Call InvComUsu.SetItem(i, .OBJIndex(i), _
                        .Amount(i), .Equipped(i), .GrhIndex(i), _
                        .OBJType(i), .MaxHit(i), .MinHit(i), .MaxDef(i), .MinDef(i), _
                        .Valor(i), .ItemName(i))
                    End With
                End If
            Next i

            ' Fill Npc inventory
            For i = 1 To 50
                If NPCInventory(i).OBJIndex <> 0 Then
                    With NPCInventory(i)
                        Call InvComNpc.SetItem(i, .OBJIndex, _
                        .Amount, 0, .GrhIndex, _
                        .OBJType, .MaxHit, .MinHit, .MaxDef, .MinDef, _
                        .Valor, .Name)
                    End With
                End If
            Next i

            'Set state and show form
            Comerciando = True
            frmComerciar.Show , //FrmMain
         */

        Logger.debug("handleCommerceInit CARGADO - FALTA TERMINAR!");
    }

    private static void handleBankEnd() {
        // Remove packet ID
        incomingData.readByte();

        /*
            Set InvBanco(0) = Nothing
            Set InvBanco(1) = Nothing

            Unload frmBancoObj
            Comerciando = False
         */

        Logger.debug("handleBankEnd CARGADO - FALTA TERMINAR!");
    }

    private static void handleCommerceChat() {
        if (incomingData.checkPacketData(4)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(incomingData);

        // Remove packet ID
        buffer.readByte();

        String chat = buffer.readASCIIString();
        int fontSize = buffer.readByte();

        /*
        If InStr(1, chat, "~") Then
        str = ReadField(2, chat, 126)
            If Val(str) > 255 Then
                r = 255
            Else
                r = Val(str)
            End If

            str = ReadField(3, chat, 126)
            If Val(str) > 255 Then
                g = 255
            Else
                g = Val(str)
            End If

            str = ReadField(4, chat, 126)
            If Val(str) > 255 Then
                b = 255
            Else
                b = Val(str)
            End If

        Call AddtoRichTextBox(frmComerciarUsu.CommerceConsole, Left$(chat, InStr(1, chat, "~") - 1), r, g, b, Val(ReadField(5, chat, 126)) <> 0, Val(ReadField(6, chat, 126)) <> 0)
    Else
        With FontTypes(FontIndex)
            Call AddtoRichTextBox(frmComerciarUsu.CommerceConsole, chat, .red, .green, .blue, .bold, .italic)
        End With
    End If
         */


        incomingData.copyBuffer(buffer);

        Logger.debug("handleCommerceChat CARGADO - FALTA TERMINAR!");
    }

    private static void handleCommerceEnd() {
        // Remove packet ID
        incomingData.readByte();

        // Comerciando = false
        // Unload frmComerciar

        Logger.debug("handleCommerceEnd CARGADO - FALTA TERMINAR!");
    }

    private static void handleDisconnect() {
        // Remove packet ID
        incomingData.readByte();

        SocketConnection.get().disconnect();
        eraseAllChars();

        ImGUISystem.get().closeAllFrms();

        Sound.clearSounds();

        /*
        'Hide main form
    //FrmMain.Visible = False

    'Stop audio
    Call Audio.StopWave
    //FrmMain.IsPlaying = PlayLoop.plNone

    'Show connection form
    frmConnect.Visible = True

    'Reset global vars
    UserDescansar = False
    UserParalizado = False
    pausa = False
    UserCiego = False
    UserMeditar = False
    UserNavegando = False
    bRain = False
    bFogata = False
    SkillPoints = 0
    Comerciando = False
    'new
    Traveling = False
    'Delete all kind of dialogs
    Call CleanDialogs

    'Reset some char variables...
    For i = 1 To LastChar
        charlist(i).invisible = False
    Next i


    For i = 1 To MAX_INVENTORY_SLOTS
        Call Inventario.SetItem(i, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, "")
    Next i

    Call Audio.PlayMIDI("2.mid")
         */

        playMusic("2.ogg");
        Logger.debug("handleDisconnect CARGADO - FALTA TERMINAR!");
    }

    private static void handleNavigateToggle() {
        // Remove packet ID
        incomingData.readByte();

        if (User.get().isUserNavegando()) {
            User.get().setUserNavegando(false);
        } else {
            User.get().setUserNavegando(true);
        }
    }

    private static void handleRemoveCharDialog() {
        if (incomingData.checkPacketData(3)) return;

        // Remove packet ID
        incomingData.readByte();
        Dialogs.removeDialog(incomingData.readInteger());
    }

    private static void handleRemoveDialogs() {
        // Remove packet ID
        incomingData.readByte();
        Dialogs.removeAllDialogs();
    }

    private static void handleLogged() {
        incomingData.readByte();
        User.get().setUserConected(true);
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

}
