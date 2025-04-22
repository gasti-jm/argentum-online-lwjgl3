package org.aoclient.network.protocol.handlers.gm;

import org.aoclient.engine.Window;
import org.aoclient.engine.game.Console;
import org.aoclient.engine.game.User;
import org.aoclient.engine.game.models.E_Skills;
import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.ByteQueue;
import org.aoclient.network.protocol.PacketHandler;
import org.aoclient.network.packets.E_Messages;

import static org.aoclient.engine.game.Dialogs.charDialogHitSet;
import static org.aoclient.engine.game.models.E_Skills.FundirMetal;
import static org.aoclient.engine.utils.GameData.charList;
import static org.aoclient.network.Messages.*;

public class MultiMessageHandler implements PacketHandler {

    private final Console console = Console.get();

    @Override
    public void handle(ByteQueue data) {
        int bodyPart;
        short damage;

        // Remove packet ID
        data.readByte();

        int m = data.readByte();

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
                switch (data.readByte()) {
                    case 1: // bCabeza
                        console.addMsgToConsole(MENSAJE_GOLPE_CABEZA + " " + data.readInteger(),
                                false, false, new RGBColor(1f, 0f, 0f));
                        break;

                    case 2: // bBrazoIzquierdo
                        console.addMsgToConsole(MENSAJE_GOLPE_BRAZO_IZQ + " " + data.readInteger(),
                                false, false, new RGBColor(1f, 0f, 0f));
                        break;

                    case 3: // bBrazoDerecho
                        console.addMsgToConsole(MENSAJE_GOLPE_BRAZO_DER + " " + data.readInteger(),
                                false, false, new RGBColor(1f, 0f, 0f));
                        break;

                    case 4: // bPiernaIzquierda
                        console.addMsgToConsole(MENSAJE_GOLPE_PIERNA_IZQ + " " + data.readInteger(),
                                false, false, new RGBColor(1f, 0f, 0f));
                        break;

                    case 5: // bPiernaDerecha
                        console.addMsgToConsole(MENSAJE_GOLPE_PIERNA_DER + " " + data.readInteger(),
                                false, false, new RGBColor(1f, 0f, 0f));
                        break;

                    case 6: // bTorso
                        console.addMsgToConsole(MENSAJE_GOLPE_TORSO + " " + data.readInteger(),
                                false, false, new RGBColor(1f, 0f, 0f));
                        break;
                }
                break;

            case UserHitNPC:
                final int d = data.readLong();
                console.addMsgToConsole(MENSAJE_GOLPE_CRIATURA_1 + " " + d,
                        false, false, new RGBColor(1f, 0f, 0f));

                charDialogHitSet(User.get().getUserCharIndex(), d);

                break;

            case UserAttackedSwing:
                final short charIndexAttaker = data.readInteger();

                console.addMsgToConsole(MENSAJE_1 + " " + charList[charIndexAttaker].getName() + MENSAJE_ATAQUE_FALLO,
                        false, false, new RGBColor(1f, 0f, 0f));

                charDialogHitSet(charIndexAttaker, "*Falla*");
                break;

            case UserHittedByUser:
                final int charIndexHitAttaker = data.readInteger();
                String attackerName = "<" + charList[charIndexHitAttaker].getName() + ">";
                bodyPart = data.readByte();
                damage = data.readInteger();

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
                final int charIndexVictim = data.readInteger();
                final String victimName = "<" + charList[charIndexVictim].getName() + ">";
                bodyPart = data.readByte();
                damage = data.readInteger();

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
                final int usingSkill = data.readByte();
                User.get().setUsingSkill(usingSkill);

                Window.get().setCursorCrosshair(true);

                switch (E_Skills.values()[usingSkill - 1]) {
                    case MAGIA:
                        console.addMsgToConsole(MENSAJE_TRABAJO_MAGIA, false, false, new RGBColor(0.39f, 0.39f, 0.47f));
                        break;

                    case PESCA:
                        console.addMsgToConsole(MENSAJE_TRABAJO_PESCA, false, false, new RGBColor(0.39f, 0.39f, 0.47f));
                        break;

                    case ROBAR:
                        console.addMsgToConsole(MENSAJE_TRABAJO_ROBAR, false, false, new RGBColor(0.39f, 0.39f, 0.47f));
                        break;

                    case TALAR:
                        console.addMsgToConsole(MENSAJE_TRABAJO_TALAR, false, false, new RGBColor(0.39f, 0.39f, 0.47f));
                        break;

                    case MINERIA:
                        console.addMsgToConsole(MENSAJE_TRABAJO_MINERIA, false, false, new RGBColor(0.39f, 0.39f, 0.47f));
                        break;

                    case PROYECTILES:
                        console.addMsgToConsole(MENSAJE_TRABAJO_PROYECTILES, false, false, new RGBColor(0.39f, 0.39f, 0.47f));
                        break;
                }

                if (usingSkill == FundirMetal) {
                    console.addMsgToConsole(MENSAJE_TRABAJO_FUNDIRMETAL, false, false, new RGBColor(0.39f, 0.39f, 0.47f));
                }
                break;

            case HaveKilledUser:
                console.addMsgToConsole(MENSAJE_HAS_MATADO_A + charList[data.readInteger()].getName() + MENSAJE_22,
                        false, false, new RGBColor(1f, 0f, 0f));

                final int level = data.readLong();

                console.addMsgToConsole(MENSAJE_HAS_GANADO_EXPE_1 + level + MENSAJE_HAS_GANADO_EXPE_2,
                        false, false, new RGBColor(1f, 0f, 0f));

                // sistema de captura al matar.
                break;

            case UserKill:
                console.addMsgToConsole(charList[data.readInteger()].getName() + MENSAJE_TE_HA_MATADO,
                        false, false, new RGBColor(1f, 0f, 0f));
                break;

            case EarnExp:
                console.addMsgToConsole(MENSAJE_HAS_GANADO_EXPE_1 + data.readLong() + MENSAJE_HAS_GANADO_EXPE_2,
                        false, false, new RGBColor(1f, 0f, 0f));
                break;

            case GoHome:
                int distance = data.readByte();
                short time = data.readInteger();
                String hogar = data.readASCIIString();

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
}
