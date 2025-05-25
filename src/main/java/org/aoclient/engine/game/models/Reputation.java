package org.aoclient.engine.game.models;

/**
 * Representa los distintos niveles o categorias de reputacion que un personaje puede alcanzar en el mundo del juego.
 * <p>
 * La reputacion afecta la forma en que los NPCs (personajes no jugables) reaccionan ante el personaje del jugador, sus posibles
 * interacciones y eventos disponibles. Tambien puede influir en las relaciones con ciertas facciones o regiones.
 * <p>
 * Cada reputacion refleja el pasado y las acciones del personaje, creando oportunidades o dificultades unicas en el desarrollo de
 * la historia del jugador.
 */

public enum Reputation {

    ASSASSIN,
    BANDIT,
    BURGHER,
    THIEF,
    NOBLE,
    PLEBEIAN

}
