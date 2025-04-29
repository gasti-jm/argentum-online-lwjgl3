package org.aoclient.network.protocol.types;

/**
 * <p>
 * Representa los diferentes tipos de jugadores. Cada tipo de jugador tiene un nivel asignado, el cual se identifica por un ID
 * unico.
 * <p>
 * La enumeracion PlayerType busca clasificar y organizar los roles de los jugadores de acuerdo con su importancia, privilegios o
 * funciones dentro del sistema.
 * <ul>
 *   <li>USER: Representa un usuario basico sin roles o permisos especiales.
 *   <li>CONSEJERO: Un jugador con permisos adicionales que puede aconsejar a otros.
 *   <li>SEMI_DIOS: Un jugador con mayor status o habilidades especiales.
 *   <li>DIOS: Representa un nivel avanzado con mayores privilegios.
 *   <li>ADMIN: Un administrador con control significativo en el sistema.
 *   <li>ROLE_MASTER: Un jugador que puede gestionar roles y permisos.
 *   <li>CHAOS_COUNCIL: Miembro de un consejo encargado, potencialmente, de desbalancear o alterar el flujo del juego.
 *   <li>ROYAL_COUNCIL: Miembro de un consejo con alta responsabilidad y autoridad.
 * </ul>
 * <p>
 * Se puede acceder al ID de cada tipo de jugador mediante el metodo {@link #getId()}.
 * <p>
 * Esta estructura es ideal para definir jerarquias y facilitar la gestion de permisos o privilegios dentro del juego o sistema.
 * <p>
 * TODO Creo que seria mejor separar entre solo el usuario y el admin para evitar tanto rangos innecesarios
 */

public enum PlayerType {

    USER(1),
    CONSEJERO(2),
    SEMI_DIOS(4),
    DIOS(8),
    ADMIN(16),
    ROLE_MASTER(32),
    CHAOS_COUNCIL(64),
    ROYAL_COUNCIL(128);

    private final int id;

    PlayerType(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

}
