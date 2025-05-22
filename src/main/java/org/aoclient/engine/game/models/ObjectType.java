package org.aoclient.engine.game.models;

/**
 * Representa los diferentes tipos de objetos que pueden existir en el juego.
 * <p>
 * Este enum define una amplia variedad de tipos de objetos, cada uno identificado por un ID unico. Algunos de estos tipos pueden
 * ser equipables, como armas, armaduras y otros elementos especificos, mientras que otros son simplemente interactuables o tienen
 * un proposito unico.
 * <p>
 * Los tipos de objetos cubren una amplia gama de funcionalidades para representar elementos en el mundo del juego, desde
 * equipamiento hasta objetos consumibles, decorativos o estructurales.
 */

public enum ObjectType {

    USE_ONCE(1),
    WEAPON(2, true),
    ARMOR(3, true),
    TREE(4),
    GOLD(5),
    DOOR(6),
    CONTAINER(7),
    SIGN(8),
    KEY(9),
    FORUM(10),
    POTION(11),
    BOOK(12),
    DRINK(13),
    FIREWOOD(14),
    BONFIRE(15),
    SHIELD(16, true),
    HELMET(17, true),
    RING(18, true),
    TELEPORT(19),
    FURNITURE(20),
    JEWEL(21),
    MINE(22),
    MINERAL(23),
    SCROLL(24),
    AURA(25),
    INSTRUMENT(26),
    ANVIL(27),
    FORGE(28),
    GEM(29),
    FLOWER(30),
    BOAT(31, true),
    ARROW(32, true),
    EMPTY_BOTTLE(33),
    FULL_BOTTLE(34),
    STAIN(35),
    ELVEN_TREE(36),
    BACKPACK(37),
    ANY(1000);

    private final int id;
    private final boolean equippable;

    ObjectType(int id, boolean equippable) {
        this.id = id;
        this.equippable = equippable;
    }

    ObjectType(int id) {
        this.id = id;
        this.equippable = false;
    }

    public int getId() {
        return id;
    }

    public boolean isEquippable() {
        return equippable;
    }

}
