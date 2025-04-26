package org.aoclient.network.protocol.types;

public enum NickColorType {

    CRIMINAL(1),
    CIUDADANO(2),
    ATACABLE(4);

    private final int id;

    NickColorType(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

}
