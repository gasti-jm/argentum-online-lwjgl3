package org.aoclient.network.protocol.types;

/**
 * Representa un conjunto de tipos de edicion que pueden aplicarse a un personaje dentro de un sistema, como cambios en sus
 * atributos o caracteristicas.
 * <p>
 * Esta enumeracion proporciona valores predefinidos que identifican diferentes aspectos del personaje que pueden ser modificados.
 * Cada elemento tiene un valor entero unico asociado que facilita su manejo interno.
 */

public enum CharacterEditType {

    GOLD(1),
    EXPERIENCE(2),
    BODY(3),
    HEAD(4),
    CITIZENS_KILLED(5),
    CRIMINALS_KILLED(6),
    LEVEL(7),
    CLASS(8),
    SKILLS(9),
    SKILL_POINTS_LEFT(10),
    NOBILITY(11),
    ASSASSIN(12),
    SEX(13),
    RACE(14),
    ADD_GOLD(15);

    private final int value;

    CharacterEditType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
