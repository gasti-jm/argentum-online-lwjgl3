package org.aoclient.engine.game.models;

/**
 * Atributos que determinan las capacidades del personaje.
 */

public enum Attribute {

    STRENGTH(1),
    AGILITY(2),
    INTELLIGENCE(3),
    CHARISMA(4),
    CONSTITUTION(5);

    private final int id;

    Attribute(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
