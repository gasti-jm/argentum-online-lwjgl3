package org.aoclient.engine.game.models;

/**
 * Representa las direcciones cardinales disponibles para el movimiento dentro del juego.
 * <p>
 * Estas direcciones permiten definir y gestionar el desplazamiento de los personajes en el entorno, utilizando valores
 * predefinidos que pueden ser interpretados tanto por el sistema de movimiento como por el motor grafico del juego.
 * <ul>
 * <li>UP: Representa la direccion hacia arriba.
 * <li>RIGHT: Representa la direccion hacia la derecha.
 * <li>DOWN: Representa la direccion hacia abajo.
 * <li>LEFT: Representa la direccion hacia la izquierda.
 * </ul>
 */

public enum Direction {

    UP(1),
    RIGHT(2),
    DOWN(3),
    LEFT(4);

    private final int id;

    Direction(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
