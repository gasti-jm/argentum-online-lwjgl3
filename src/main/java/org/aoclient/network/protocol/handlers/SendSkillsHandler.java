package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.User;
import org.aoclient.engine.game.models.E_Skills;
import org.aoclient.network.ByteQueue;
import org.aoclient.network.protocol.PacketHandler;
import org.tinylog.Logger;

public class SendSkillsHandler implements PacketHandler {
    @Override
    public void handle(ByteQueue data) {
        if (data.checkPacketData(2 + 20 * 2)) return;

        // Remove packet ID
        data.readByte();

        // variables globales
        /* TODO: este 'userClase' se usa para cambiar la descripción de los skills
            por 'No disponible para tu clase' o algo asi
        */
        int userClase = data.readByte();
        /* TODO: este porcentajeSkills creo que es para mostrar algo en el form
            de estadìsticas
         */
        int porcentajeSkills[] = new int[20];

        for (E_Skills skill: E_Skills.values()) {
            User.get().setSkill(skill.getValue(), data.readByte());
            porcentajeSkills[skill.getValue() - 1] = data.readByte();
        }

        /* TODO: este flag es para mostrar el boton de asignar skills '+' del main
            como 'iluminado' para que sepas que tenes skills libres para agregar
        */
        // LlegaronSkills = true;

        Logger.debug("handleSendSkills Cargado! - FALTA TERMINAR!");
    }
}
