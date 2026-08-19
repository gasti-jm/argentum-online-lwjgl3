package org.aoclient.engine.game.models;

public enum SMType {
    // TODO: OTRO HARDCODEO... DIOS.
    sResucitation(4978, 4982),
    sSafemode(4979,4983),
    mSpells(4980,4984),
    mWork(4981,4985);

    private final int grhIndexON, grhIndexOFF;

    SMType(int grhIndexON, int grhIndexOFF) {
        this.grhIndexON = grhIndexON;
        this.grhIndexOFF = grhIndexOFF;
    }

    public int getGrhIndexON() {
        return grhIndexON;
    }

    public int getGrhIndexOFF() {
        return grhIndexOFF;
    }
}
