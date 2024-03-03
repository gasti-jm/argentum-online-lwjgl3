package org.aoclient.engine.game.models;

public enum E_Heading {
    NORTH(1), EAST(2), SOUTH(3), WEST(4);
    public final int value;

    E_Heading(int value) {
        this.value = value;
    }
}
