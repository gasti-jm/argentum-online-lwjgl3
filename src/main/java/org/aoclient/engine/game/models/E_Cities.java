package org.aoclient.engine.game.models;

public enum E_Cities {

    Ullathorpe(1),
    Nix(2),
    Banderbill(3),
    Lindos(4),
    Arghal(5);

    private final int value;

    E_Cities(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
