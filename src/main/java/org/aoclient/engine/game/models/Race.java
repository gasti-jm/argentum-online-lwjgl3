package org.aoclient.engine.game.models;

import org.aoclient.engine.game.Messages;

import static org.aoclient.engine.game.Messages.MessageKey.*;

/**
 * Representa las razas que un jugador puede seleccionar al momento de crear su personaje.
 */

public enum Race {

    HUMAN(1, RACE_HUMAN),
    ELF(2, RACE_ELF),
    DROW_ELF(3, RACE_DROW_ELF),
    GNOME(4, RACE_GNOME),
    DWARF(5, RACE_DWARF);

    private final int id;
    private final Messages.MessageKey name;

    Race(int id, Messages.MessageKey name) {
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
