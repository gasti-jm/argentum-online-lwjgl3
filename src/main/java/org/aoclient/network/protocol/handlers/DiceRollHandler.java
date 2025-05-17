package org.aoclient.network.protocol.handlers;

import org.aoclient.network.PacketBuffer;
import org.tinylog.Logger;

import static org.aoclient.engine.Sound.SND_DICE;
import static org.aoclient.engine.Sound.playSound;

public class DiceRollHandler implements PacketHandler {
    @Override
    public void handle(PacketBuffer data) {
        if (data.checkBytes(6)) return;

        data.readByte();

        int fuerza = data.readByte();
        int agilidad = data.readByte();
        int inteligencia = data.readByte();
        int carisma = data.readByte();
        int constitucion = data.readByte();

        playSound(SND_DICE);

        try {
            // Buscar el formulario FCreateCharacter abierto y actualizar atributos
            for (var frm : org.aoclient.engine.gui.ImGUISystem.get().getActiveForms()) {
                if (frm instanceof org.aoclient.engine.gui.forms.FCreateCharacter) {
                    ((org.aoclient.engine.gui.forms.FCreateCharacter) frm).setAtributos(fuerza, agilidad, inteligencia, carisma, constitucion);
                    break;
                }
            }
        } catch (Exception e) {
            Logger.error("Error actualizando atributos en FCreateCharacter: " + e.getMessage(), e);
        }
    }
}
