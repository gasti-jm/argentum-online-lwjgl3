package org.aoclient.engine.game.models;

/**
 * Representa una posicion en un espacio bidimensional.
 * <p>
 * Esta clase simple encapsula las coordenadas <b>x</b> e <b>y</b> que definen la ubicacion de un elemento en el mapa.
 * {@code Position} es utilizada ampliamente en todo el codigo para representar las posiciones de personajes, items, efectos y
 * otros elementos del mundo.
 * <p>
 * Esta implementacion simula el tipo o estructura de datos "position" utilizado en el diseño original.
 */

public final class Position {

    private int x;
    private int y;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

}
