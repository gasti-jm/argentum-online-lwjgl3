package org.aoclient.network.packets;

public enum PlayerType {
    USER(1),
    CONSEJERO(2),
    SEMI_DIOS(4),
    DIOS(8),
    ADMIN(16),
    ROLE_MASTER(32),
    CHAOS_COUNCIL(64),
    ROYAL_COUNCIL(128);

    private final int id;

    PlayerType(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }
}
