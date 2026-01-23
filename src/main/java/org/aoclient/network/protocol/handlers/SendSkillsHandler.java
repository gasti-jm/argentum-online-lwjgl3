package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.User;
import org.aoclient.engine.game.models.Skill;
import org.aoclient.network.PacketBuffer;
import org.tinylog.Logger;

public class SendSkillsHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        if (buffer.checkBytes(46)) return;
        buffer.readByte();

        // variables globales
        /* TODO: este 'userClase' se usa para cambiar la descripción de los skills
            por 'No disponible para tu clase' o algo asi
        */
        int userClase = buffer.readByte();
        /* TODO: este porcentajeSkills creo que es para mostrar algo en el form
            de estadìsticas
         */
        int porcentajeSkills[] = new int[20];

        for (Skill skill : Skill.values()) {
            User.INSTANCE.setSkill(skill.getId(), buffer.readByte());
            porcentajeSkills[skill.getId() - 1] = buffer.readByte();
        }

        int points = buffer.readLong();
        User.INSTANCE.setFreeSkillPoints(points);

        /* TODO: este flag es para mostrar el boton de asignar skills '+' del main
            como 'iluminado' para que sepas que tenes skills libres para agregar
        */
        // LlegaronSkills = true;
    }

}
