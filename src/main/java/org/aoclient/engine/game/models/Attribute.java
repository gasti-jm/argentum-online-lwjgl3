package org.aoclient.engine.game.models;

/**
 * Atributos que determinan las capacidades del personaje.
 */

public enum Attribute {

    FUERZA(1),
    AGILIDAD(2),
    INTELIGENCIA(3),
    CARISMA(4),
    CONSTITUCION(5);

    private final int id;

    Attribute(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
