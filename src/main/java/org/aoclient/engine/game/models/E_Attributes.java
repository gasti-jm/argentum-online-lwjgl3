package org.aoclient.engine.game.models;

/**
 * Representa las atributos que determinan las capacidades de un personaje.
 */

public enum E_Attributes {

    Fuerza(1),
    Agilidad(2),
    Inteligencia(3),
    Carisma(4),
    Constitucion(5);

    public final int value;

    E_Attributes(int value) {
        this.value = value;
    }

}
