package org.aoclient.engine.utils.inits;

/**
 * <p>
 * Simula el "Type" o la estructura de {@code IndexHeads}, utilizada para almacenar los indices graficos de las cabezas de los
 * personajes.
 * <p>
 * Esta clase gestiona los diferentes indices graficos correspondientes a las cabezas de los personajes, organizados segun la
 * orientacion o direccion en la que mira el personaje.
 * <p>
 * Es utilizada durante la carga de datos de personajes para mapear correctamente los recursos graficos correspondientes a las
 * cabezas en cada una de las orientaciones posibles.
 *
 * @see HeadData
 * @see org.aoclient.engine.utils.GameData
 */

public final class IndexHeads {

    private short[] head = new short[5];

    public short getHead(int index) {
        return head[index];
    }

    public void setHead(int index, short head) {
        this.head[index] = head;
    }

}
