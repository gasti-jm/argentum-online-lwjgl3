package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.gui.ImGUISystem;
import org.aoclient.engine.gui.forms.FCreateCharacter;
import org.aoclient.network.PacketBuffer;
import org.tinylog.Logger;

import static org.aoclient.engine.audio.Sound.SND_DICE;
import static org.aoclient.engine.audio.Sound.playSound;

public class DiceRollHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        if (buffer.checkBytes(6)) return;
        buffer.readByte();

        int fuerza = buffer.readByte();
        int agilidad = buffer.readByte();
        int inteligencia = buffer.readByte();
        int carisma = buffer.readByte();
        int constitucion = buffer.readByte();

        playSound(SND_DICE);

        try {
            // Buscar el formulario FCreateCharacter abierto y actualizar atributos
            for (var frm : ImGUISystem.INSTANCE.getActiveForms()) {
                if (frm instanceof FCreateCharacter) {
                    ((FCreateCharacter) frm).setAtributos(fuerza, agilidad, inteligencia, carisma, constitucion);
                    break;
                }
            }
        } catch (Exception e) {
            Logger.error("Error actualizando atributos en FCreateCharacter: " + e.getMessage(), e);
        }
    }

}
