package org.aoclient.engine.game.models;

/**
 * Representa las habilidades que un personaje puede desarrollar, como combate, magia y otras destrezas.
 */

public enum Skill {

    MAGIC(1, "Ability to master the magical arts and cast spells."),
    THEFT(2, "Ability to steal gold and items."),
    EVASION(3, "Ability to dodge physical attacks in combat."),
    WEAPONRY(4, "Ability to handle melee combat weapons."),
    MEDITATION(5, "Ability to concentrate and recover mana faster."),
    STABBING(6, "Ability to perform critical attacks with short weapons."),
    CONCEALMENT(7, "Ability to hide and become temporarily invisible."),
    SURVIVAL(8, "Ability to survive in the wilderness and evaluate creatures."),
    WOODCUTTING(9, "Ability to fell trees and obtain wood efficiently."),
    TRADING(10, "Ability to negotiate better prices with merchants."),
    SHIELDING(11, "Ability to block attacks using shields."),
    FISHING(12, "Ability to fish successfully."),
    MINING(13, "Ability to extract minerals and create ingots."),
    CARPENTRY(14, "Ability to work with wood and create objects."),
    SMITHING(15, "Ability to forge metals and create equipment."),
    LEADERSHIP(16, "Ability to lead groups and create clans."),
    BEASTMASTERY(17, "Ability to tame creatures and make them allies."),
    ARCHERY(18, "Ability to use long-range weapons with precision."),
    WRESTLING(19, "Ability to fight without weapons using only fists."),
    NAVIGATION(20, "Ability to navigate ships without wrecking.");

    public static final int FundirMetal = 88;
    private final int id;
    private final String description;

    Skill(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

}
