package org.aoclient.engine.utils.inits;

/**
 * Simula el "Type" o la estructura de WeaponData.
 */

public final class WeaponData {

    private final GrhInfo[] WeaponWalk = new GrhInfo[5];

    public WeaponData() {
        WeaponWalk[1] = new GrhInfo();
        WeaponWalk[2] = new GrhInfo();
        WeaponWalk[3] = new GrhInfo();
        WeaponWalk[4] = new GrhInfo();
    }

    public WeaponData(WeaponData other) {
        WeaponWalk[1] = new GrhInfo(other.WeaponWalk[1]);
        WeaponWalk[2] = new GrhInfo(other.WeaponWalk[2]);
        WeaponWalk[3] = new GrhInfo(other.WeaponWalk[3]);
        WeaponWalk[4] = new GrhInfo(other.WeaponWalk[4]);
    }

    public GrhInfo getWeaponWalk(int index) {
        return WeaponWalk[index];
    }

    public void setWeaponWalk(int index, GrhInfo weaponWalk) {
        WeaponWalk[index] = weaponWalk;
    }

}
