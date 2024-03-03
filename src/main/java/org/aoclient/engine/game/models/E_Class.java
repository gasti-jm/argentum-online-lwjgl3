package org.aoclient.engine.game.models;

public enum E_Class {
    MAGE(1),
    CLERIC(2),       //Clérigo
    WARRIOR(3),      //Guerrero
    ASSASIN(4),      //Asesino
    THIEF(5),        //Ladrón
    BARD(6),         //Bardo
    DRUID(7),        //Druida
    BANDIT(8),       //Bandido
    PALADIN(9),      //Paladín
    HUNTER(10),      //Cazador
    WORKER(11),      //Trabajador
    PIRAT(12);       //Pirata

    public final int value;

    E_Class(int value) {
        this.value = value;
    }
}
