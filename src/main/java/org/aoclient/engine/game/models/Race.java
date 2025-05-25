package org.aoclient.engine.game.models;

/**
 * Representa las razas que un jugador puede seleccionar al momento de crear su personaje.
 */

public enum Race {

    HUMAN(1),
    ELF(2),
    DROW_ELF(3),
    GNOME(4),
    DWARF(5);

    private final int id;

    Race(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
