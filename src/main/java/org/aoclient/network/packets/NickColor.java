package org.aoclient.network.packets;

public enum NickColor {
    CRIMINAL(1),
    CIUDADANO(2),
    ATACABLE(4);

    private final int id;

    NickColor(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }
}
