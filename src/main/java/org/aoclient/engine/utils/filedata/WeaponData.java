package org.aoclient.engine.utils.filedata;

import org.aoclient.engine.utils.GameData;

public class WeaponData {
    private GrhInfo[] WeaponWalk = new GrhInfo[4];

    public WeaponData() {
        WeaponWalk[0] = new GrhInfo();
        WeaponWalk[1] = new GrhInfo();
        WeaponWalk[2] = new GrhInfo();
        WeaponWalk[3] = new GrhInfo();
    }

    public WeaponData(WeaponData other) {
        WeaponWalk[0] = new GrhInfo(other.WeaponWalk[0]);
        WeaponWalk[1] = new GrhInfo(other.WeaponWalk[1]);
        WeaponWalk[2] = new GrhInfo(other.WeaponWalk[2]);
        WeaponWalk[3] = new GrhInfo(other.WeaponWalk[3]);
    }


    public GrhInfo getWeaponWalk(int index) {
        return WeaponWalk[index];
    }

    public void setWeaponWalk(int index, GrhInfo weaponWalk) {
        WeaponWalk[index] = weaponWalk;
    }
}
