package org.aoclient.engine.game.models;

/**
 * Representa las direcciones cardinales disponibles para el movimiento dentro del juego.
 * <p>
 * Estas direcciones permiten definir y gestionar el desplazamiento de los personajes en el entorno, utilizando valores
 * predefinidos que pueden ser interpretados tanto por el sistema de movimiento como por el motor grafico del juego.
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
