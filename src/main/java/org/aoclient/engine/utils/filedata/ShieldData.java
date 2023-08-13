package org.aoclient.engine.utils.filedata;

public final class ShieldData {
    private GrhInfo[] shieldWalk = new GrhInfo[4];

    public ShieldData() {
        shieldWalk[0] = new GrhInfo();
        shieldWalk[1] = new GrhInfo();
        shieldWalk[2] = new GrhInfo();
        shieldWalk[3] = new GrhInfo();
    }

    public ShieldData(ShieldData other) {
        shieldWalk[0] = new GrhInfo(other.shieldWalk[0]);
        shieldWalk[1] = new GrhInfo(other.shieldWalk[1]);
        shieldWalk[2] = new GrhInfo(other.shieldWalk[2]);
        shieldWalk[3] = new GrhInfo(other.shieldWalk[3]);
    }

    public GrhInfo getShieldWalk(int index) {
        return shieldWalk[index];
    }

    public void setShieldWalk(int index, GrhInfo shieldWalk) {
        this.shieldWalk[index] = shieldWalk;
    }
}
