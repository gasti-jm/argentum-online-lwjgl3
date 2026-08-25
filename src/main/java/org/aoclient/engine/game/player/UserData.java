package org.aoclient.engine.game.player;

import org.aoclient.engine.game.models.Attribute;
import org.aoclient.engine.game.models.KillCounter;
import org.aoclient.engine.game.models.Reputation;
import org.aoclient.engine.game.models.Skill;

public final class UserData {
    short maxHP;
    short minHP;

    short maxMANA;
    short minMANA;

    short maxSTA;
    short minSTA;
    int nextLevel;
    int exp;
    int gold;
    int lvl;
    int dext;
    int strg;

    int maxAGU;
    int minAGU;

    int maxHAM;
    int minHAM;

    int freeSkillPoints;
    int[] skills       = new int[Skill.values().length];
    int[] attributes   = new int[Attribute.values().length];
    int[] reputations  = new int[Reputation.values().length];
    int[] killCounters = new int[KillCounter.values().length];

    int role;
    int jailTime;

    String userWeaponEqpHit = "0/0";
    String userArmourEqpDef = "0/0";
    String userHelmEqpDef   = "0/0";
    String userShieldEqpDef = "0/0";

    int userWeaponEqpSlot;
    int userArmourEqpSlot;
    int userHelmEqpSlot;
    int userShieldEqpSlot;

    int privilege;
}
