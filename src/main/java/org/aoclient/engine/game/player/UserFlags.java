package org.aoclient.engine.game.player;

public final class UserFlags {
    boolean connected;
    boolean moving;
    boolean underCeiling;
    boolean browsing;
    boolean trading;
    boolean talking;
    boolean criminal;
    int usingSkill;

    UserFlags() {
        this.connected = false;
        this.moving = false;
        this.underCeiling = false;
        this.criminal = false;
        this.usingSkill = 0;
        this.talking = false;
        this.browsing = false;
        this.trading = false;
    }
}
