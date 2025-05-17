package org.aoclient.network.protocol.types;

/**
 * <p>
 * Representa un conjunto de tipos asociados a los colores de un apodo (nickname). Cada tipo tiene un identificador numerico unico
 * que puede ser utilizado para diferenciarlos.
 * <ul>
 *   <li>CRIMINAL: Representa a una entidad clasificada como criminal.
 *   <li>CIUDADANO: Representa a una entidad clasificada como ciudadano.
 *   <li>ATACABLE: Indica que una entidad puede ser objetivo de un ataque.
 * </ul>
 * <p>
 * Esta enumeracion permite clasificar entidades en diferentes categorias mediante el uso de su identificador.
 */

public enum NickColorType {

    CRIMINAL(1),
    CIUDADANO(2),
    ATACABLE(4);

    private final int id;

    NickColorType(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

}
