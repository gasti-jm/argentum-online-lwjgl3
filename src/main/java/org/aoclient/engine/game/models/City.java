package org.aoclient.engine.game.models;

/**
 * Localizaciones urbanas donde los jugadores pueden comenzar su aventura.
 */

public enum City {

    ULLATHORPE(1),
    NIX(2),
    BANDERBILL(3),
    LINDOS(4),
    ARGHAL(5);

    private final int id;

    City(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
