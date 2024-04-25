package org.aoclient.network.packets;

public enum eEditOptions {
    eo_Gold(1),
    eo_Experience(2),
    eo_Body(3),
    eo_Head(4),
    eo_CiticensKilled(5),
    eo_CriminalsKilled(6),
    eo_Level(7),
    eo_Class(8),
    eo_Skills(9),
    eo_SkillPointsLeft(10),
    eo_Nobleza(11),
    eo_Asesino(12),
    eo_Sex(13),
    eo_Raza(14),
    eo_addGold(15);

    private final int value;

    eEditOptions(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
