package org.aoclient.engine.game.models;


import org.aoclient.engine.game.Messages;

import static org.aoclient.engine.game.Messages.MessageKey.*;

/**
 * Representa los diferentes roles disponibles que un jugador puede seleccionar para su personaje.
 * <p>
 * Los roles representan arquetipos clasicos de personajes en juegos de rol y determinan ciertas habilidades, estilos de combate y
 * formas de interaccion en el mundo del juego.
 * <p>
 * Roles disponibles:
 * <ul>
 * <li>MAGE: Personaje enfocado en el uso de habilidades magicas y hechizos.
 * <li>CLERIC: Sanador principal y protector, con habilidades en restauracion.
 * <li>WARRIOR: Combatiente cuerpo a cuerpo con alta resistencia y dano.
 * <li>ASSASSIN: Personaje especializado en ataques rapidos y letales.
 * <li>THIEF: Experto en sigilo y hurto, con habilidades agiles y evasivas.
 * <li>BARD: Maestro del arte del entretenimiento y soporte con canciones.
 * <li>DRUID: Protector de la naturaleza, especializado en habilidades de transformacion y curacion.
 * <li>BANDIT: Luchador astuto con habilidades de combate callejero.
 * <li>PALADIN: Guerrero sagrado enfocado en defensa y habilidades de inspiracion.
 * <li>HUNTER: Experto en el uso de armas a distancia y tacticas de supervivencia.
 * <li>WORKER: Personaje centrado en habilidades de trabajo como mineria, carpinteria y pesca.
 * <li>PIRATE: Aventurero maritimo especializado en combate y navegacion.
 * </ul>
 */

public enum Role {

    MAGE(1, ROLE_MAGE),
    CLERIC(2, ROLE_CLERIC),
    WARRIOR(3, ROLE_WARRIOR),
    ASSASSIN(4, ROLE_ASSASSIN),
    THIEF(5, ROLE_THIEF),
    BARD(6, ROLE_BARD),
    DRUID(7, ROLE_DRUID),
    BANDIT(8, ROLE_BANDIT),
    PALADIN(9, ROLE_PALADIN),
    HUNTER(10, ROLE_HUNTER),
    WORKER(11, ROLE_WORKER),
    PIRATE(12, ROLE_PIRATE);

    private final int id;
    private final Messages.MessageKey name;

    Role(int id, Messages.MessageKey name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return Messages.get(name);
    }

}
