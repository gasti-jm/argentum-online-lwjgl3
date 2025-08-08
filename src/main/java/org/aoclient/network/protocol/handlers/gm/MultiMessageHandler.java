package org.aoclient.network.protocol.handlers.gm;

import org.aoclient.engine.game.Messages;
import org.aoclient.engine.Window;
import org.aoclient.engine.game.Console;
import org.aoclient.engine.game.User;
import org.aoclient.engine.game.models.Skill;
import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.PacketBuffer;
import org.aoclient.network.protocol.handlers.PacketHandler;
import org.aoclient.network.protocol.types.MessageType;

import static org.aoclient.engine.game.Messages.MessageKey;
import static org.aoclient.engine.game.Dialogs.charDialogHitSet;
import static org.aoclient.engine.game.models.Skill.FundirMetal;
import static org.aoclient.engine.utils.GameData.charList;

public class MultiMessageHandler implements PacketHandler {

    private final Console console = Console.INSTANCE;

    @Override
    public void handle(PacketBuffer buffer) {
        int bodyPart;
        short damage;
        final RGBColor red = new RGBColor(1f, 0f, 0f);

        // TODO No se esta leyendo dos veces el ID (o eso supongo)?
        buffer.readByte();

        int m = buffer.readByte();

        if (m > MessageType.values().length) return;
        MessageType msg = MessageType.values()[m];

        switch (msg) {
            case DONT_SEE_ANYTHING:
                console.addMsgToConsole(Messages.get(MessageKey.NO_VES_NADA_INTERESANTE), false, false, new RGBColor(0.25f, 0.75f, 0.60f));
                break;

            case NPC_SWING:
                console.addMsgToConsole(Messages.get(MessageKey.CRIATURA_FALLA_GOLPE), false, false, red);
                break;

            case NPC_KILL_USER:
                console.addMsgToConsole(Messages.get(MessageKey.CRIATURA_MATADO), false, false, red);
                break;

            case BLOCKED_WITH_SHIELD_USER:
                console.addMsgToConsole(Messages.get(MessageKey.RECHAZO_ATAQUE_ESCUDO), false, false, red);
                break;

            case BLOCKED_WITH_SHIELD_OTHER:
                console.addMsgToConsole(Messages.get(MessageKey.USUARIO_RECHAZO_ATAQUE_ESCUDO), false, false, red);
                break;

            case USER_SWING:
                console.addMsgToConsole(Messages.get(MessageKey.FALLADO_GOLPE), false, false, red);
                charDialogHitSet(User.INSTANCE.getUserCharIndex(), "*Fallas*");
                break;

            case SAFE_MODE_ON:
                console.addMsgToConsole(Messages.get(MessageKey.SEGURO_ACTIVADO), false, false, new RGBColor(0f, 1f, 0f));
                break;

            case SAFE_MODE_OFF:
                console.addMsgToConsole(Messages.get(MessageKey.SEGURO_DESACTIVADO), false, false, red);
                break;

            case RESUSCITATION_SAFE_OFF:
                console.addMsgToConsole(Messages.get(MessageKey.SEGURO_RESU_ON), false, false, new RGBColor(0f, 1f, 0f));
                break;

            case RESUSCITATION_SAFE_ON:
                console.addMsgToConsole(Messages.get(MessageKey.SEGURO_RESU_OFF), false, false, red);
                break;

            case NOBILITY_LOST:
                console.addMsgToConsole(Messages.get(MessageKey.PIERDE_NOBLEZA), false, false, red);
                break;

            case CANT_USE_WHILE_MEDITATING:
                console.addMsgToConsole(Messages.get(MessageKey.USAR_MEDITANDO), false, false, red);
                break;

            case NPC_HIT_USER:
                switch (buffer.readByte()) {
                    case 1: // bCabeza
                        console.addMsgToConsole(Messages.get(MessageKey.GOLPE_CABEZA), false, false,
                                red, buffer.readInteger());
                        break;

                    case 2: // bBrazoIzquierdo
                        console.addMsgToConsole(Messages.get(MessageKey.GOLPE_BRAZO_IZQ), false, false,
                                red, buffer.readInteger());
                        break;

                    case 3: // bBrazoDerecho
                        console.addMsgToConsole(Messages.get(MessageKey.GOLPE_BRAZO_DER), false, false,
                                red, buffer.readInteger());
                        break;

                    case 4: // bPiernaIzquierda
                        console.addMsgToConsole(Messages.get(MessageKey.GOLPE_PIERNA_IZQ), false, false,
                                red, buffer.readInteger());
                        break;

                    case 5: // bPiernaDerecha
                        console.addMsgToConsole(Messages.get(MessageKey.GOLPE_PIERNA_DER), false, false,
                                red, buffer.readInteger());
                        break;

                    case 6: // bTorso
                        console.addMsgToConsole(Messages.get(MessageKey.GOLPE_TORSO), false, false,
                                red, buffer.readInteger());
                        break;
                }
                break;

            case USER_HIT_NPC:
                final int d = buffer.readLong();
                console.addMsgToConsole(Messages.get(MessageKey.GOLPE_CRIATURA), false, false, red, d);
                charDialogHitSet(User.INSTANCE.getUserCharIndex(), d);
                break;

            case USER_ATTACKED_SWING:
                final short charIndexAttaker = buffer.readInteger();

                console.addMsgToConsole(Messages.get(MessageKey.ATAQUE_FALLO), false, false, red,
                        charList[charIndexAttaker].getName());

                charDialogHitSet(charIndexAttaker, "*Falla*");
                break;

            case USER_HITTED_BY_USER:
                final int charIndexHitAttaker = buffer.readInteger();
                String attackerName = "<" + charList[charIndexHitAttaker].getName() + ">";
                bodyPart = buffer.readByte();
                damage = buffer.readInteger();

                switch (bodyPart) {
                    case 1: // bCabeza
                        console.addMsgToConsole(Messages.get(MessageKey.RECIVE_IMPACTO_CABEZA),
                                false, false, red, attackerName, damage);
                        break;

                    case 2: // bBrazoIzquierdo
                        console.addMsgToConsole(Messages.get(MessageKey.RECIVE_IMPACTO_BRAZO_IZQ),
                                false, false, red, attackerName, damage);
                        break;

                    case 3: // bBrazoDerecho
                        console.addMsgToConsole(Messages.get(MessageKey.RECIVE_IMPACTO_BRAZO_DER),
                                false, false, red, attackerName, damage);
                        break;

                    case 4: // bPiernaIzquierda
                        console.addMsgToConsole(Messages.get(MessageKey.RECIVE_IMPACTO_PIERNA_IZQ),
                                false, false, red, attackerName, damage);
                        break;

                    case 5: // bPiernaDerecha
                        console.addMsgToConsole(Messages.get(MessageKey.RECIVE_IMPACTO_PIERNA_DER),
                                false, false, red, attackerName, damage);
                        break;

                    case 6: // bTorso
                        console.addMsgToConsole(Messages.get(MessageKey.RECIVE_IMPACTO_TORSO),
                                false, false, red, attackerName, damage);
                        break;
                }

                charDialogHitSet(charIndexHitAttaker, damage);

                break;

            case USER_HITTED_USER:
                final int charIndexVictim = buffer.readInteger();
                final String victimName = "<" + charList[charIndexVictim].getName() + ">";
                bodyPart = buffer.readByte();
                damage = buffer.readInteger();

                switch (bodyPart) {
                    case 1: // bCabeza
                        console.addMsgToConsole(Messages.get(MessageKey.PRODUCE_IMPACTO), false, false,
                                red, victimName, Messages.get(MessageKey.PRODUCE_IMPACTO_CABEZA), damage);
                        break;

                    case 2: // bBrazoIzquierdo
                        console.addMsgToConsole(Messages.get(MessageKey.PRODUCE_IMPACTO), false, false,
                                red, victimName, Messages.get(MessageKey.PRODUCE_IMPACTO_BRAZO_IZQ), damage);
                        break;

                    case 3: // bBrazoDerecho
                        console.addMsgToConsole(Messages.get(MessageKey.PRODUCE_IMPACTO), false, false,
                                red, victimName, Messages.get(MessageKey.PRODUCE_IMPACTO_BRAZO_DER), damage);
                        break;

                    case 4: // bPiernaIzquierda
                        console.addMsgToConsole(Messages.get(MessageKey.PRODUCE_IMPACTO), false, false,
                                red, victimName, Messages.get(MessageKey.PRODUCE_IMPACTO_PIERNA_IZQ), damage);
                        break;

                    case 5: // bPiernaDerecha
                        console.addMsgToConsole(Messages.get(MessageKey.PRODUCE_IMPACTO), false, false,
                                red, victimName, Messages.get(MessageKey.PRODUCE_IMPACTO_PIERNA_DER), damage);
                        break;

                    case 6: // bTorso
                        console.addMsgToConsole(Messages.get(MessageKey.PRODUCE_IMPACTO), false, false,
                                red, victimName, Messages.get(MessageKey.PRODUCE_IMPACTO_TORSO), damage);
                        break;
                }
                charDialogHitSet(User.INSTANCE.getUserCharIndex(), damage);

                break;

            case WORK_REQUEST_TARGET:
                final int usingSkill = buffer.readByte();
                User.INSTANCE.setUsingSkill(usingSkill);

                Window.INSTANCE.setCursorCrosshair(true);

                switch (Skill.values()[usingSkill - 1]) {
                    case MAGIC:
                        console.addMsgToConsole(Messages.get(MessageKey.TRABAJO_MAGIA), false, false, new RGBColor(0.39f, 0.39f, 0.47f));
                        break;

                    case FISHING:
                        console.addMsgToConsole(Messages.get(MessageKey.TRABAJO_PESCA), false, false, new RGBColor(0.39f, 0.39f, 0.47f));
                        break;

                    case THEFT:
                        console.addMsgToConsole(Messages.get(MessageKey.TRABAJO_ROBAR), false, false, new RGBColor(0.39f, 0.39f, 0.47f));
                        break;

                    case WOODCUTTING:
                        console.addMsgToConsole(Messages.get(MessageKey.TRABAJO_TALAR), false, false, new RGBColor(0.39f, 0.39f, 0.47f));
                        break;

                    case MINING:
                        console.addMsgToConsole(Messages.get(MessageKey.TRABAJO_MINERIA), false, false, new RGBColor(0.39f, 0.39f, 0.47f));
                        break;

                    case ARCHERY:
                        console.addMsgToConsole(Messages.get(MessageKey.TRABAJO_PROYECTILES), false, false, new RGBColor(0.39f, 0.39f, 0.47f));
                        break;
                }

                if (usingSkill == FundirMetal) {
                    console.addMsgToConsole(Messages.get(MessageKey.TRABAJO_FUNDIRMETAL), false, false, new RGBColor(0.39f, 0.39f, 0.47f));
                }
                break;

            case HAVE_KILLED_USER:
                console.addMsgToConsole(Messages.get(MessageKey.HAS_MATADO_A), false, false, red,
                        charList[buffer.readInteger()].getName());

                final int exp = buffer.readLong();

                console.addMsgToConsole(Messages.get(MessageKey.HAS_GANADO_EXPE), false, false, red, exp);

                // sistema de captura al matar.
                break;

            case USER_KILL:
                console.addMsgToConsole(Messages.get(MessageKey.TE_HA_MATADO), false, false, red,
                        charList[buffer.readInteger()].getName());
                break;

            case EARN_EXP:
                console.addMsgToConsole(Messages.get(MessageKey.HAS_GANADO_EXPE), false, false, red, buffer.readLong());
                break;

            case GO_HOME:
                int distance = buffer.readByte();
                short time = buffer.readInteger();
                String hogar = buffer.readCp1252String();

                console.addMsgToConsole(Messages.get(MessageKey.IR_HOGAR), false, false, red,
                        distance, hogar, time);
                break;

            case FINISH_HOME:
                console.addMsgToConsole(Messages.get(MessageKey.HOGAR), false, false, new RGBColor());
                break;

            case CANCEL_GO_HOME:
                console.addMsgToConsole(Messages.get(MessageKey.HOGAR_CANCEL), false, false, red);
                break;
        }
    }
}
