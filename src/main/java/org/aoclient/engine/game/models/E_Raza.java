package org.aoclient.engine.game.models;

public enum E_Raza {
    HUMANO(1),
    ELFO(2),
    ELFO_DROW(3),
    GNOMO(4),
    ENANO(5);

    public final int value;

    E_Raza(int value) {
        this.value = value;
    }
}
