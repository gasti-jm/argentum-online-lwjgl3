package org.aoclient.engine.game.models;

/**
 * Representa todas las opciones de profesion que un jugador puede seleccionar al momento de crear su personaje.
 */

public enum E_Class {

    Mago(1),
    Clerigo(2),
    Guerrero(3),
    Asesino(4),
    Ladron(5),
    Bardo(6),
    Druida(7),
    Bandido(8),
    Paladin(9),
    Cazador(10),
    Trabajador(11),
    Pirata(12);

    private final int value;

    E_Class(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
