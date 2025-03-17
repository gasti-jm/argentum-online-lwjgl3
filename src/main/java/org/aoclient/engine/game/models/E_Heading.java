package org.aoclient.engine.game.models;

/**
 * Especifica las cuatro orientaciones posibles (Norte, Este, Sur y Oeste) que determinan la direccion del personaje.
 * <p>
 * El enum es fundamental para varios sistemas:
 * <ul>
 * <li>Sistema de movimiento del personaje
 * <li>Determinacion de la animacion apropiada a mostrar
 * <li>Orientacion de los ataques y habilidades
 * <li>Renderizacion correcta de los sprites en el mundo
 * </ul>
 * <p>
 * Los valores estan diseñados de forma secuencial para facilitar calculos relacionados con cambios de direccion y rotaciones del
 * personaje.
 */

public enum E_Heading {

    NORTH(1), EAST(2), SOUTH(3), WEST(4);
    public final int value;

    E_Heading(int value) {
        this.value = value;
    }

}
