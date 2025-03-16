package org.aoclient.engine.game.models;

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
