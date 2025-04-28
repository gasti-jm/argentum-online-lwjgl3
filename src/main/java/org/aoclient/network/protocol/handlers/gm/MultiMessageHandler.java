package org.aoclient.network.protocol.handlers.gm;

import org.aoclient.engine.Messages;
import org.aoclient.engine.Window;
import org.aoclient.engine.game.Console;
import org.aoclient.engine.game.User;
import org.aoclient.engine.game.models.E_Skills;
import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.ByteQueue;
import org.aoclient.network.protocol.handlers.PacketHandler;
import org.aoclient.network.protocol.types.MessageType;

import static org.aoclient.engine.Messages.MessageKey;
import static org.aoclient.engine.game.Dialogs.charDialogHitSet;
import static org.aoclient.engine.game.models.E_Skills.FundirMetal;
import static org.aoclient.engine.utils.GameData.charList;

public class MultiMessageHandler implements PacketHandler {

    private final Console console = Console.get();

    @Override
    public void handle(ByteQueue data) {
        int bodyPart;
        short damage;

        // Remove packet ID
        data.readByte();

        int m = data.readByte();

        if (m > MessageType.values().length) return;
        MessageType msg = MessageType.values()[m];

        switch (msg) {
            case DONT_SEE_ANYTHING:
                console.addMsgToConsole(Messages.get(MessageKey.NO_VES_NADA_INTERESANTE), false, false, new RGBColor(0.25f, 0.75f, 0.60f));
                break;

            case NPC_SWING:
                console.addMsgToConsole(Messages.get(MessageKey.CRIATURA_FALLA_GOLPE), false, false, new RGBColor(1f, 0f, 0f));
                break;

            case NPC_KILL_USER:
                console.addMsgToConsole(Messages.get(MessageKey.CRIATURA_MATADO), false, false, new RGBColor(1f, 0f, 0f));
                break;

            case BLOCKED_WITH_SHIELD_USER:
                console.addMsgToConsole(Messages.get(MessageKey.RECHAZO_ATAQUE_ESCUDO), false, false, new RGBColor(1f, 0f, 0f));
                break;

            case BLOCKED_WITH_SHIELD_OTHER:
                console.addMsgToConsole(Messages.get(MessageKey.USUARIO_RECHAZO_ATAQUE_ESCUDO), false, false, new RGBColor(1f, 0f, 0f));
                break;

            case USER_SWING:
                console.addMsgToConsole(Messages.get(MessageKey.FALLADO_GOLPE), false, false, new RGBColor(1f, 0f, 0f));
                charDialogHitSet(User.get().getUserCharIndex(), "*Fallas*");
                break;

            case SAFE_MODE_ON:
                console.addMsgToConsole("MODO SEGURO ACTIVADO", false, false, new RGBColor(0f, 1f, 0f));
                break;

            case SAFE_MODE_OFF:
                console.addMsgToConsole("MODO SEGURO DESACTIVADO", false, false, new RGBColor(1f, 0f, 0f));
                break;

            case RESUSCITATION_SAFE_OFF:
                console.addMsgToConsole("MODO RESURECCION ACTIVADO", false, false, new RGBColor(0f, 1f, 0f));
                break;

            case RESUSCITATION_SAFE_ON:
                console.addMsgToConsole("MODO RESURECCION DESACTIVADO", false, false, new RGBColor(1f, 0f, 0f));
                break;

            case NOBILITY_LOST:
                console.addMsgToConsole(Messages.get(MessageKey.PIERDE_NOBLEZA), false, false, new RGBColor(1f, 0f, 0f));
                break;

            case CANT_USE_WHILE_MEDITATING:
                console.addMsgToConsole(Messages.get(MessageKey.USAR_MEDITANDO), false, false, new RGBColor(1f, 0f, 0f));
                break;

            case NPC_HIT_USER:
                switch (data.readByte()) {
                    case 1: // bCabeza
                        console.addMsgToConsole(Messages.get(MessageKey.GOLPE_CABEZA) + " " + data.readInteger(),
                                false, false, new RGBColor(1f, 0f, 0f));
                        break;

                    case 2: // bBrazoIzquierdo
                        console.addMsgToConsole(Messages.get(MessageKey.GOLPE_BRAZO_IZQ) + " " + data.readInteger(),
                                false, false, new RGBColor(1f, 0f, 0f));
                        break;

                    case 3: // bBrazoDerecho
                        console.addMsgToConsole(Messages.get(MessageKey.GOLPE_BRAZO_DER) + " " + data.readInteger(),
                                false, false, new RGBColor(1f, 0f, 0f));
                        break;

                    case 4: // bPiernaIzquierda
                        console.addMsgToConsole(Messages.get(MessageKey.GOLPE_PIERNA_IZQ) + " " + data.readInteger(),
                                false, false, new RGBColor(1f, 0f, 0f));
                        break;

                    case 5: // bPiernaDerecha
                        console.addMsgToConsole(Messages.get(MessageKey.GOLPE_PIERNA_DER) + " " + data.readInteger(),
                                false, false, new RGBColor(1f, 0f, 0f));
                        break;

                    case 6: // bTorso
                        console.addMsgToConsole(Messages.get(MessageKey.GOLPE_TORSO) + " " + data.readInteger(),
                                false, false, new RGBColor(1f, 0f, 0f));
                        break;
                }
                break;

            case USER_HIT_NPC:
                final int d = data.readLong();
                console.addMsgToConsole(Messages.get(MessageKey.GOLPE_CRIATURA_1) + " " + d, false, false, new RGBColor(1f, 0f, 0f));

                charDialogHitSet(User.get().getUserCharIndex(), d);

                break;

            case USER_ATTACKED_SWING:
                final short charIndexAttaker = data.readInteger();

                console.addMsgToConsole(Messages.get(MessageKey.MENSAJE_1) + " " + charList[charIndexAttaker].getName() + Messages.get(MessageKey.ATAQUE_FALLO),
                        false, false, new RGBColor(1f, 0f, 0f));

                charDialogHitSet(charIndexAttaker, "*Falla*");
                break;

            case USER_HITTED_BY_USER:
                final int charIndexHitAttaker = data.readInteger();
                String attackerName = "<" + charList[charIndexHitAttaker].getName() + ">";
                bodyPart = data.readByte();
                damage = data.readInteger();

                switch (bodyPart) {
                    case 1: // bCabeza
                        console.addMsgToConsole(Messages.get(MessageKey.MENSAJE_1) + attackerName + Messages.get(MessageKey.RECIVE_IMPACTO_CABEZA) + damage + Messages.get(MessageKey.MENSAJE_2),
                                false, false, new RGBColor(1f, 0f, 0f));
                        break;

                    case 2: // bBrazoIzquierdo
                        console.addMsgToConsole(Messages.get(MessageKey.MENSAJE_1) + attackerName + Messages.get(MessageKey.RECIVE_IMPACTO_BRAZO_IZQ) + damage + Messages.get(MessageKey.MENSAJE_2),
                                false, false, new RGBColor(1f, 0f, 0f));
                        break;

                    case 3: // bBrazoDerecho
                        console.addMsgToConsole(Messages.get(MessageKey.MENSAJE_1) + attackerName + Messages.get(MessageKey.RECIVE_IMPACTO_BRAZO_DER) + damage + Messages.get(MessageKey.MENSAJE_2),
                                false, false, new RGBColor(1f, 0f, 0f));
                        break;

                    case 4: // bPiernaIzquierda
                        console.addMsgToConsole(Messages.get(MessageKey.MENSAJE_1) + attackerName + Messages.get(MessageKey.RECIVE_IMPACTO_PIERNA_IZQ) + damage + Messages.get(MessageKey.MENSAJE_2),
                                false, false, new RGBColor(1f, 0f, 0f));
                        break;

                    case 5: // bPiernaDerecha
                        console.addMsgToConsole(Messages.get(MessageKey.MENSAJE_1) + attackerName + Messages.get(MessageKey.RECIVE_IMPACTO_PIERNA_DER) + damage + Messages.get(MessageKey.MENSAJE_2),
                                false, false, new RGBColor(1f, 0f, 0f));
                        break;

                    case 6: // bTorso
                        console.addMsgToConsole(Messages.get(MessageKey.MENSAJE_1) + attackerName + Messages.get(MessageKey.RECIVE_IMPACTO_TORSO) + damage + Messages.get(MessageKey.MENSAJE_2),
                                false, false, new RGBColor(1f, 0f, 0f));
                        break;
                }

                charDialogHitSet(charIndexHitAttaker, damage);

                break;

            case USER_HITTED_USER:
                final int charIndexVictim = data.readInteger();
                final String victimName = "<" + charList[charIndexVictim].getName() + ">";
                bodyPart = data.readByte();
                damage = data.readInteger();

                switch (bodyPart) {
                    case 1: // bCabeza
                        console.addMsgToConsole(Messages.get(MessageKey.PRODUCE_IMPACTO_1) + victimName + Messages.get(MessageKey.PRODUCE_IMPACTO_CABEZA) + damage + Messages.get(MessageKey.MENSAJE_2),
                                false, false, new RGBColor(1f, 0f, 0f));
                        break;

                    case 2: // bBrazoIzquierdo
                        console.addMsgToConsole(Messages.get(MessageKey.PRODUCE_IMPACTO_1) + victimName + Messages.get(MessageKey.PRODUCE_IMPACTO_BRAZO_IZQ) + damage + Messages.get(MessageKey.MENSAJE_2),
                                false, false, new RGBColor(1f, 0f, 0f));
                        break;

                    case 3: // bBrazoDerecho
                        console.addMsgToConsole(Messages.get(MessageKey.PRODUCE_IMPACTO_1) + victimName + Messages.get(MessageKey.RECIVE_IMPACTO_BRAZO_DER) + damage + Messages.get(MessageKey.MENSAJE_2),
                                false, false, new RGBColor(1f, 0f, 0f));
                        break;

                    case 4: // bPiernaIzquierda
                        console.addMsgToConsole(Messages.get(MessageKey.PRODUCE_IMPACTO_1) + victimName + Messages.get(MessageKey.RECIVE_IMPACTO_PIERNA_IZQ) + damage + Messages.get(MessageKey.MENSAJE_2),
                                false, false, new RGBColor(1f, 0f, 0f));
                        break;

                    case 5: // bPiernaDerecha
                        console.addMsgToConsole(Messages.get(MessageKey.PRODUCE_IMPACTO_1) + victimName + Messages.get(MessageKey.RECIVE_IMPACTO_PIERNA_DER) + damage + Messages.get(MessageKey.MENSAJE_2),
                                false, false, new RGBColor(1f, 0f, 0f));
                        break;

                    case 6: // bTorso
                        console.addMsgToConsole(Messages.get(MessageKey.PRODUCE_IMPACTO_1) + victimName + Messages.get(MessageKey.RECIVE_IMPACTO_TORSO) + damage + Messages.get(MessageKey.MENSAJE_2),
                                false, false, new RGBColor(1f, 0f, 0f));
                        break;
                }
                charDialogHitSet(charIndexVictim, damage);

                break;

            case WORK_REQUEST_TARGET:
                final int usingSkill = data.readByte();
                User.get().setUsingSkill(usingSkill);

                Window.get().setCursorCrosshair(true);

                switch (E_Skills.values()[usingSkill - 1]) {
                    case MAGIA:
                        console.addMsgToConsole(Messages.get(MessageKey.TRABAJO_MAGIA), false, false, new RGBColor(0.39f, 0.39f, 0.47f));
                        break;

                    case PESCA:
                        console.addMsgToConsole(Messages.get(MessageKey.TRABAJO_PESCA), false, false, new RGBColor(0.39f, 0.39f, 0.47f));
                        break;

                    case ROBAR:
                        console.addMsgToConsole(Messages.get(MessageKey.TRABAJO_ROBAR), false, false, new RGBColor(0.39f, 0.39f, 0.47f));
                        break;

                    case TALAR:
                        console.addMsgToConsole(Messages.get(MessageKey.TRABAJO_TALAR), false, false, new RGBColor(0.39f, 0.39f, 0.47f));
                        break;

                    case MINERIA:
                        console.addMsgToConsole(Messages.get(MessageKey.TRABAJO_MINERIA), false, false, new RGBColor(0.39f, 0.39f, 0.47f));
                        break;

                    case PROYECTILES:
                        console.addMsgToConsole(Messages.get(MessageKey.TRABAJO_PROYECTILES), false, false, new RGBColor(0.39f, 0.39f, 0.47f));
                        break;
                }

                if (usingSkill == FundirMetal) {
                    console.addMsgToConsole(Messages.get(MessageKey.TRABAJO_FUNDIRMETAL), false, false, new RGBColor(0.39f, 0.39f, 0.47f));
                }
                break;

            case HAVE_KILLED_USER:
                console.addMsgToConsole(Messages.get(MessageKey.HAS_MATADO_A) + charList[data.readInteger()].getName() + Messages.get(MessageKey.MENSAJE_22),
                        false, false, new RGBColor(1f, 0f, 0f));

                final int level = data.readLong();

                console.addMsgToConsole(Messages.get(MessageKey.HAS_GANADO_EXPE_1) + level + Messages.get(MessageKey.HAS_GANADO_EXPE_2),
                        false, false, new RGBColor(1f, 0f, 0f));

                // sistema de captura al matar.
                break;

            case USER_KILL:
                console.addMsgToConsole(charList[data.readInteger()].getName() + Messages.get(MessageKey.TE_HA_MATADO),
                        false, false, new RGBColor(1f, 0f, 0f));
                break;

            case EARN_EXP:
                console.addMsgToConsole(Messages.get(MessageKey.HAS_GANADO_EXPE_1) + data.readLong() + Messages.get(MessageKey.HAS_GANADO_EXPE_2),
                        false, false, new RGBColor(1f, 0f, 0f));
                break;

            case GO_HOME:
                int distance = data.readByte();
                short time = data.readInteger();
                String hogar = data.readASCIIString();

                console.addMsgToConsole("Estas a " + distance + " mapas de distancia de " + hogar + ", este viaje durara " + time + " segundos.",
                        false, false, new RGBColor(1f, 0f, 0f));
                break;

            case FINISH_HOME:
                console.addMsgToConsole(Messages.get(MessageKey.HOGAR), false, false, new RGBColor());
                break;

            case CANCEL_GO_HOME:
                console.addMsgToConsole(Messages.get(MessageKey.HOGAR_CANCEL), false, false, new RGBColor(1f, 0f, 0f));
                break;
        }
    }
}
