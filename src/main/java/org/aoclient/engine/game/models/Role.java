package org.aoclient.engine.game.models;


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

    MAGE(1),
    CLERIC(2),
    WARRIOR(3),
    ASSASSIN(4),
    THIEF(5),
    BARD(6),
    DRUID(7),
    BANDIT(8),
    PALADIN(9),
    HUNTER(10),
    WORKER(11),
    PIRATE(12);

    private final int id;

    Role(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
